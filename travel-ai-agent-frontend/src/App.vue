<script setup>
import { computed, nextTick, onBeforeUnmount, onMounted, ref } from 'vue';

const API_BASE = 'http://localhost:8123/api';

const chatId = ref('');
const inputText = ref('');
const loading = ref(false);
const messages = ref([]);
const chatListRef = ref(null);
let currentEventSource = null;

const canSend = computed(() => inputText.value.trim().length > 0 && !loading.value);

function generateChatId() {
  const rand = Math.random().toString(36).slice(2, 8).toUpperCase();
  return `CHAT-${Date.now()}-${rand}`;
}

function addMessage(role, content, status = 'done') {
  messages.value.push({
    id: `${Date.now()}-${Math.random().toString(36).slice(2, 8)}`,
    role,
    content,
    status,
  });
  scrollToBottom();
}

function updateMessageContent(id, content, status) {
  const target = messages.value.find((item) => item.id === id);
  if (!target) {
    return;
  }
  target.content = content;
  if (status) {
    target.status = status;
  }
  scrollToBottom();
}

function scrollToBottom() {
  nextTick(() => {
    const el = chatListRef.value;
    if (!el) {
      return;
    }
    el.scrollTop = el.scrollHeight;
  });
}

function closeSSE() {
  if (currentEventSource) {
    currentEventSource.close();
    currentEventSource = null;
  }
}

function parseSSEChunk(raw) {
  if (!raw) {
    return '';
  }

  const trimmed = raw.trim();
  if (!trimmed) {
    return '';
  }

  if (trimmed === '[DONE]') {
    return '__DONE__';
  }

  try {
    const data = JSON.parse(trimmed);
    if (typeof data === 'string') {
      return data;
    }
    if (typeof data.content === 'string') {
      return data.content;
    }
    if (typeof data.text === 'string') {
      return data.text;
    }
    return JSON.stringify(data);
  } catch {
    return trimmed;
  }
}

function sendMessage() {
  if (!canSend.value) {
    return;
  }

  closeSSE();

  const userContent = inputText.value.trim();
  inputText.value = '';
  loading.value = true;

  addMessage('user', userContent);

  const aiMessageId = `${Date.now()}-ai-${Math.random().toString(36).slice(2, 8)}`;
  messages.value.push({
    id: aiMessageId,
    role: 'assistant',
    content: '',
    status: 'streaming',
  });
  scrollToBottom();

  const queryMessage = encodeURIComponent(userContent);
  const queryChatId = encodeURIComponent(chatId.value);
  const url = `${API_BASE}/ai/manus/chat?message=${queryMessage}&chatId=${queryChatId}`;
  const eventSource = new EventSource(url);
  currentEventSource = eventSource;

  eventSource.onmessage = (event) => {
    const chunk = parseSSEChunk(event.data);

    if (chunk === '__DONE__') {
      updateMessageContent(aiMessageId, messages.value.find((m) => m.id === aiMessageId)?.content || '', 'done');
      loading.value = false;
      closeSSE();
      return;
    }

    if (!chunk) {
      return;
    }

    const currentContent = messages.value.find((m) => m.id === aiMessageId)?.content || '';
    updateMessageContent(aiMessageId, `${currentContent}${chunk}`);
  };

  eventSource.onerror = () => {
    const currentContent = messages.value.find((m) => m.id === aiMessageId)?.content || '';
    if (!currentContent.trim()) {
      updateMessageContent(aiMessageId, '连接中断，请检查后端服务是否启动。', 'error');
    } else {
      updateMessageContent(aiMessageId, currentContent, 'done');
    }
    loading.value = false;
    closeSSE();
  };
}

function handleEnter(event) {
  if (event.isComposing) {
    return;
  }
  if (event.key === 'Enter' && !event.shiftKey) {
    event.preventDefault();
    sendMessage();
  }
}

onMounted(() => {
  chatId.value = generateChatId();
  addMessage('assistant', '你好，我是旅行计划 AI 超级智能体。告诉我你的目的地、天数和预算，我会为你生成行程。');
});

onBeforeUnmount(() => {
  closeSSE();
});
</script>

<template>
  <div class="page">
    <div class="chat-shell">
      <header class="chat-header">
        <h1>旅行计划 AI 超级智能体</h1>
        <p>会话 ID：{{ chatId }}</p>
      </header>

      <main ref="chatListRef" class="chat-list">
        <div
          v-for="message in messages"
          :key="message.id"
          class="message-row"
          :class="message.role === 'user' ? 'message-row-user' : 'message-row-ai'"
        >
          <div class="bubble" :class="message.role === 'user' ? 'bubble-user' : 'bubble-ai'">
            <div class="bubble-role">{{ message.role === 'user' ? '你' : 'AI' }}</div>
            <div class="bubble-content">{{ message.content }}</div>
            <div v-if="message.status === 'streaming'" class="typing-dot"></div>
          </div>
        </div>
      </main>

      <footer class="chat-input-wrap">
        <textarea
          v-model="inputText"
          class="chat-input"
          placeholder="输入你的旅行需求，如：帮我规划一个 5 天的东京自由行"
          rows="2"
          @keydown="handleEnter"
        ></textarea>
        <button class="send-btn" :disabled="!canSend" @click="sendMessage">发送</button>
      </footer>
    </div>
  </div>
</template>

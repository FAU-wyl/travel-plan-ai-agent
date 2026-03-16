package com.yupi.yuaiagent.demo.invoke;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;

public class LangChainAiInvoke {

    public static void main(String[] args) {
        ChatLanguageModel qwenChatModel = QwenChatModel.builder()
                .apiKey(TestApiKey.API_KEY)
                .modelName("qwen-flash")
                .build();
        String answer = qwenChatModel.chat("我是程序员宇龙，这是AI复习备考超级智能体项目");
        System.out.println(answer);
    }
}

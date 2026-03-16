package com.yupi.yuaiagent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class LoveAppTest {

    @Resource
    private LoveApp loveApp;

    @Test
    void testChat() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是宇龙";
        String answer = loveApp.doChat(message, chatId);
        // 第二轮
        message = "我最近要准备去希腊旅行";
        answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第三轮
        message = "我要去旅行的地方叫什么来着？刚跟你说过，帮我回忆一下";
        answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        String message = "你好，我是程序员宇龙，我想计划去希腊旅行，但我不知道该怎么做";
        LoveApp.LoveReport loveReport = loveApp.doChatWithReport(message, chatId);
        Assertions.assertNotNull(loveReport);
    }

    @Test
    void doChatWithRag() {
        String chatId = UUID.randomUUID().toString();
        String message = "制定一份去希腊的旅行计划";
        String answer = loveApp.doChatWithRag(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithTools() {
        // 测试联网搜索问题的答案
//        testMessage("联网搜索Relu函数什么意思？");
//
//        // 测试网页抓取：恋爱案例分析
//        testMessage("看看Github网站（github.com）的其他人是怎么学习的？");

        // 测试资源下载：图片下载
        testMessage("直接下载一张适合作为全连接神经网络的图片");

        // 测试终端操作：执行代码
//        testMessage("执行 Python3 脚本来生成数据分析报告");

        // 测试文件操作：保存用户档案
//        testMessage("保存我的恋爱档案为文件");

        // 测试 PDF 生成
//        testMessage("生成一份学习神经网络的计划书");
    }

    private void testMessage(String message) {
        String chatId = UUID.randomUUID().toString();
        String answer = loveApp.doChatWithTools(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithMcp() {
        String chatId = UUID.randomUUID().toString();
        // 测试地图 MCP
//        String message = "我的另一半居住在上海静安区，请帮我找到 5 公里内合适的约会地点";
//        String answer =  loveApp.doChatWithMcp(message, chatId);
//        Assertions.assertNotNull(answer);
        // 测试图片搜索 MCP
//        String message = "帮我搜索一些哄另一半开心的图片";
        // 测试面试鸭MCP
        String message = "MYSQL的事务是怎么实现的？从面试鸭搜索题目并给出学习计划的PDF";
        String answer =  loveApp.doChatWithMcp(message, chatId);
        Assertions.assertNotNull(answer);
    }
}

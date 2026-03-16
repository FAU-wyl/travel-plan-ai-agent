package com.yupi.yuaiagent.agent;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class YuManusTest {

    @Resource
    private YuManus yuManus;

    @Test
    public void run() {
        String userPrompt = """
                我正在学习Neural network，给我指出我需要学习哪些知识点
                并结合一些网络图片，制定一份详细的学习计划，
                并以 PDF 格式输出""";
        String answer = yuManus.run(userPrompt);
        Assertions.assertNotNull(answer);
    }
}
package com.yupi.springbootinit.manager;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AiManagerTest {
    @Autowired
    private AiManager aiManager;
    @Test
    void doChat() {
        String answer = aiManager.doChat(1820717741044072450L,"你好");
        System.out.println(answer);
    }
}
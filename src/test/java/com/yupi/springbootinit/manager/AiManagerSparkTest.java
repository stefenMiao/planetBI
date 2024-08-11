package com.yupi.springbootinit.manager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AiManagerSparkTest {
    @Autowired
    private AiManagerSpark aiManagerSpark;
    @Test
    void sendMesToAIUseXingHuo() {
        System.out.println(aiManagerSpark.sendMesToAIUseXingHuo("{\"name\":\"苹果\",\"price\":\"10\",\"num\":\"100\",\"unit\":\"个\",\"total\":\"1000\"}"));


    }
}
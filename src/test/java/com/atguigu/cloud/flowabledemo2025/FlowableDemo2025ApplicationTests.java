package com.atguigu.cloud.flowabledemo2025;

import org.flowable.engine.ProcessEngine;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FlowableDemo2025ApplicationTests {
    @Autowired
    ProcessEngine  processEngine;
    @Test
    void contextLoads() {
        System.out.println(processEngine);
    }

}

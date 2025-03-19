package com.atguigu.cloud.flowabledemo2025;

import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/***
 * 第一个测试
 */
@SpringBootTest
class FlowableDemo2025ApplicationTests {
    @Autowired
    ProcessEngine  processEngine;
    @Autowired
    RepositoryService repositoryService;
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    TaskService taskService;
    @Autowired
    HistoryService historyService;
    @Autowired
    IdentityService identityService;
    @Test
    void contextLoads() {
        System.out.println(processEngine);
        Deployment qingjia = repositoryService.createDeployment().addClasspathResource("bpmnfiles/ContainGateway.bpmn20.xml")
                .name("qingjia").deploy();

        System.out.println("qingjia.getId() = " + qingjia.getId());
        System.out.println("qingjia.getName() = " + qingjia.getName());
    }

}

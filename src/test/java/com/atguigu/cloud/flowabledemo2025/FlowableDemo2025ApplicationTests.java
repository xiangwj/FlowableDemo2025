package com.atguigu.cloud.flowabledemo2025;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

/***
 * 第一个测试
 */
@SpringBootTest
@Slf4j
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
    @Test
    void startProcess() {
        HashMap<String, Object> variables = new HashMap<>();
        variables.put("assignee0","zhangsan");
        variables.put("assignee1","lisi");
        runtimeService.startProcessInstanceByKey("SpringBootTest000Key",variables);

    }
    @Test
    void testZhangsanCompleteTask() {
        Task task = taskService.createTaskQuery().processDefinitionKey("SpringBootTest000Key").taskAssignee("zhangsan").singleResult();
        log.info("task.getId() = " + task.getId());
        log.info("task.getName() = " + task.getName());
        log.info("task.getAssignee() = " + task.getAssignee());
        taskService.complete(task.getId());
    }
    @Test
    void testLisiCompleteTask() {
        Task task = taskService.createTaskQuery().processDefinitionKey("SpringBootTest000Key").taskAssignee("lisi").singleResult();
        log.info("task.getId() = " + task.getId());
        log.info("task.getName() = " + task.getName());
        log.info("task.getAssignee() = " + task.getAssignee());
        taskService.complete(task.getId());
    }

}

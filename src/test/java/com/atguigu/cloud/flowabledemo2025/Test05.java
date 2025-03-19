package com.atguigu.cloud.flowabledemo2025;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 加上tasklistener
 */
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class Test05 {
    ProcessEngine processEngine;

    @BeforeAll
    public void setup() {
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("test05.cfg.xml");
        processEngine = configuration.buildProcessEngine();
    }

    @Test
    public void Testdeploy() {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder deployment = repositoryService.createDeployment();
        Deployment deploy = deployment.addClasspathResource("bpmnfiles/MyHoliday2.bpmn20.xml")
                .name("test05请假流程")
                .deploy();
        log.info("deploy.getId():" + deploy.getId());
        log.info("deploy.getKey():" + deploy.getKey());
        log.info("deploy.getName():" + deploy.getName());
    }

    @Test
    public void TestRunProcess() {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance myHolidayKey2 = runtimeService.startProcessInstanceByKey("MyHolidayKey");//这里只能用流程定义里的<process id="MyHolidayKey" name="MyHoliday" isExecutable="true">
        log.info("myHolidayKey2.getId():" + myHolidayKey2.getId());
        log.info("myHolidayKey2.getName():" + myHolidayKey2.getName());

    }

    @Test
    public void testTaskXiaoMin() {
        TaskService taskService = processEngine.getTaskService();
        Task xiaomintask = taskService.createTaskQuery().taskAssignee("小明").singleResult();
        taskService.complete(xiaomintask.getId());
    }

    @Test
    public void testTaskXiaoLi() {
        TaskService taskService = processEngine.getTaskService();
        Task xiaomintask = taskService.createTaskQuery().taskAssignee("小李").singleResult();
        taskService.complete(xiaomintask.getId());
    }
}

package com.atguigu.cloud.flowabledemo2025;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Slf4j
public class Test04 {
    ProcessEngine processEngine;

    @BeforeAll
    public void setup() {
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("test04.cfg.xml");
        processEngine = configuration.buildProcessEngine();
    }

    @Test
    public void testDeploy() {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder deployment = repositoryService.createDeployment();
        Deployment deploy = deployment.addClasspathResource("bpmnfiles/MyHoliday.bpmn20.xml")
                .key("MyHolidayKey")
                .name("test04请假流程")
                .deploy();
        log.info("deploy.getId():" + deploy.getId());
        log.info("deploy.getKey():" + deploy.getKey());
        log.info("deploy.getName():" + deploy.getName());

    }

    @Test
    public void testRunProcess() {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        HashMap<String, Object> variables = new HashMap<>();
        variables.put("assignee0", "zhangsan");
        variables.put("assignee1", "lisi");
        runtimeService.startProcessInstanceByKey("MyHolidayKey", variables);
    }

    @Test
    public void testCompleteTask() {
        TaskService taskService = processEngine.getTaskService();
        TaskQuery taskQuery = taskService.createTaskQuery();
        Task task = taskQuery.processDefinitionKey("MyHolidayKey").taskAssignee("zhangsan").singleResult();
        log.info("task.getId():" + task.getId());
        log.info("task.getName():" + task.getName());
        taskService.complete(task.getId());
    }
}

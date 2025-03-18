package com.atguigu.cloud.flowabledemo2025;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.*;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class TestExclusiveGateway {
    ProcessEngine processEngine;

    @BeforeAll
    public void setup() {
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("exclusive.cfg.xml");
        processEngine = configuration.buildProcessEngine();
    }

    @Test
    public void testDeploy() {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder deployment = repositoryService.createDeployment();
        deployment.addClasspathResource("bpmnfiles/ExclusiveGateWay.bpmn20.xml")
                .name("ExclusiveGateWay")
                .key("ExclusiveGateWayKey")
                .deploy();
    }
    @Test
    public void testRunProcess(){
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance exclusiveGateWayKey = runtimeService.startProcessInstanceByKey("ExclusiveGateWayKey");
        log.info("exclusiveGateWayKey.getId():"+exclusiveGateWayKey.getId());
    }
    @Test
    public void testCompleteTask(){
        TaskService taskService = processEngine.getTaskService();
        TaskQuery taskQuery = taskService.createTaskQuery();
        Task task = taskQuery.processDefinitionKey("ExclusiveGateWayKey").taskAssignee("zhangsan").singleResult();
        Map<String, Object> variables = task.getProcessVariables();
        variables.put("num",2);
        taskService.complete(task.getId(),variables);

    }
}

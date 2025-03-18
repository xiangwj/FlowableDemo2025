package com.atguigu.cloud.flowabledemo2025;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.*;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class TestInclusive {
    ProcessEngine processEngine;
    @BeforeAll
    public void setup() {
        ProcessEngineConfiguration configure = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("inclusive.cfg.xml");
        processEngine = configure.buildProcessEngine();
    }
    @Test
    public void testDeploy() {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder deployment = repositoryService.createDeployment();
        deployment.addClasspathResource("bpmnfiles/ContainGateway.bpmn20.xml").name("ContainGateway").deploy();
    }
    @Test
    public void testStartProcess() {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.startProcessInstanceByKey("ContainGatewayKey");
    }
    @Test
    public void testZhansan() {
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().processDefinitionKey("ContainGatewayKey").taskAssignee("zhansan").singleResult();
        Map<String, Object> variables = task.getProcessVariables();
        variables.put("num",4);
        taskService.complete(task.getId(), variables);
    }
    @Test
    public void testrs() {
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().processDefinitionKey("ContainGatewayKey").taskAssignee("rs").singleResult();
        taskService.complete(task.getId());
    }
    @Test
    public void testxmjl() {
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().processDefinitionKey("ContainGatewayKey").taskAssignee("xmjl").singleResult();
        taskService.complete(task.getId());
    }
    @Test
    public void testzjl() {
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().processDefinitionKey("ContainGatewayKey").taskAssignee("zjl").singleResult();
        taskService.complete(task.getId());
    }
}

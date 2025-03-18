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
public class ParallelTest {
    ProcessEngine processEngine;
    @BeforeAll
    public void setUp()  {
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("parallel.cfg.xml");
        processEngine = configuration.buildProcessEngine();
    }
    @Test
    public void testDeploy(){
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder deployment = repositoryService.createDeployment();
        deployment.addClasspathResource("bpmnfiles/ParallelGateWayTest.bpmn20.xml").name("ParallelGateWayTest").key("ParallelGateWayTestKey");
        deployment.deploy();
    }
    @Test
    public void testStartProcess(){
        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.startProcessInstanceByKey("ParallelGateWayTestKey");
    }
    @Test
    public void testRequest(){
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().processDefinitionKey("ParallelGateWayTestKey").taskAssignee("zhansan").singleResult();
        Map<String, Object> variables = task.getProcessVariables();
        variables.put("num",4);
        taskService.complete(task.getId(),variables);
    }
    @Test
    public void testjsjl(){
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().processDefinitionKey("ParallelGateWayTestKey").taskAssignee("jsjl").singleResult();
        taskService.complete(task.getId());
    }
    @Test
    public void testxmjl(){
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().processDefinitionKey("ParallelGateWayTestKey").taskAssignee("xmjl").singleResult();
        taskService.complete(task.getId());
    }
    @Test
    public void testzjl(){
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().processDefinitionKey("ParallelGateWayTestKey").taskAssignee("zjl").singleResult();
        taskService.complete(task.getId());
    }
}

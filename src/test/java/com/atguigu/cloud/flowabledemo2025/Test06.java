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
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class Test06 {
    ProcessEngine processEngine;

    @BeforeAll
    public void setup() {
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("test06.cfg.xml");
        processEngine = configuration.buildProcessEngine();
    }

    @Test
    public void Testdeploy() {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder deployment = repositoryService.createDeployment();
        Deployment deploy = deployment.addClasspathResource("bpmnfiles/test06-businesstrip-bpmn20.xml")
                .name("出差申请流程")
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
        variables.put("assignee2", "wangwu");
        variables.put("assignee3", "zhaokuaiji");
        runtimeService.startProcessInstanceByKey("businessTrip", variables);
    }

    /***
     * 完成任务，设变量值
     */
    @Test
    public void testCompleteTaskzhangsan() {
        TaskService taskService = processEngine.getTaskService();
        TaskQuery taskQuery = taskService.createTaskQuery();
        Task task = taskQuery.processDefinitionKey("businessTrip").taskAssignee("zhangsan").singleResult();
        Map<String, Object> variables = task.getProcessVariables();
        variables.put("num",2);
        taskService.complete(task.getId(), variables);
    }
    /**
     *根据Task编号来更新流程局部变量
     * */
    @Test
    public void testUpdateVariables() {
        TaskService taskService = processEngine.getTaskService();
        TaskQuery taskQuery = taskService.createTaskQuery();
        Task task = taskQuery.processDefinitionKey("businessTrip").taskAssignee("lisi").singleResult();
        Map<String, Object> variables = task.getProcessVariables();
        variables.put("num",5);
        taskService.setVariablesLocal(task.getId(),  variables);
    }
    /**
     *根据Task编号来更新流程全局变量
     * */
    @Test
    public void testUpdateVariablesGoable() {
        TaskService taskService = processEngine.getTaskService();
        TaskQuery taskQuery = taskService.createTaskQuery();
        Task task = taskQuery.processDefinitionKey("businessTrip").taskAssignee("lisi").singleResult();
        Map<String, Object> variables = task.getProcessVariables();
        variables.put("num",6);
        taskService.setVariables(task.getId(),  variables);
    }
    /**
     *根据Task编号来更新流程全局变量
     * */
    @Test
    public void testComplete() {
        TaskService taskService = processEngine.getTaskService();
        TaskQuery taskQuery = taskService.createTaskQuery();
        Task task = taskQuery.processDefinitionKey("businessTrip").taskAssignee("lisi").singleResult();
        taskService.complete(task.getId());
    }
}

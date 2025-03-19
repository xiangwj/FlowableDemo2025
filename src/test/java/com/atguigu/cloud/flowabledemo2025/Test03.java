package com.atguigu.cloud.flowabledemo2025;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.*;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Slf4j
public class Test03 {
    @Value("${mydatabase.driver}")
    String databaseDriver;
    @Value("${mydatabase.username}")
    String databaseUserName;
    @Value("${mydatabase.passwd}")
    String databasePassword;
    @Value("${mydatabase.url}")
    String databaseUrl;
    ProcessEngineConfiguration configuration = null;
    ProcessEngine processEngine;

    @BeforeAll
    public void setUp() {
        configuration = new StandaloneProcessEngineConfiguration();
        configuration.setJdbcDriver(databaseDriver);
        configuration.setJdbcUsername(databaseUserName);
        configuration.setJdbcPassword(databasePassword);
        configuration.setJdbcUrl(databaseUrl);
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        processEngine = configuration.buildProcessEngine();
    }

    @Test
    public void testDeploy() {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("HolidayUI.bpmn20.xml").name("请假流程02").deploy();
        log.info("deploy.getId():" + deploy.getId());
        log.info("deploy.getName():" + deploy.getName());
    }

    @Test
    public void testStartProcess() {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceById("HolidayUI:4:30004");
        log.info("processInstance.getProcessDefinitionId():" + processInstance.getProcessDefinitionId());
        log.info("processInstance.getActivityId():" + processInstance.getActivityId());
        log.info("processInstance.getId():" + processInstance.getId());
    }

    @Test
    public void testCompleteTask4User1() {
        TaskService taskService = processEngine.getTaskService();
        TaskQuery taskQuery = taskService.createTaskQuery();
        List<Task> user1Tasks = taskQuery.taskAssignee("user1").list();
        user1Tasks.forEach(task -> {
            log.info("task.getId():" + task.getId());
            taskService.complete(task.getId());
        });
    }

    @Test
    public void testCompleteTask4User2() {
        TaskService taskService = processEngine.getTaskService();
        TaskQuery taskQuery = taskService.createTaskQuery();
        List<Task> user1Tasks = taskQuery.taskAssignee("user2").list();
        user1Tasks.forEach(task -> {
            log.info("task.getId():" + task.getId());
            taskService.complete(task.getId());
        });
    }

    /***
     * 测试激活或挂起
     */
    @Test
    public void testSuspendProcess() {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId("HolidayUI:4:30004").singleResult();
        if (processDefinition.isSuspended()) {
            log.info("激活流程:" + processDefinition.getId() + "-" + processDefinition.getName());
            repositoryService.activateProcessDefinitionById(processDefinition.getId());
        } else {
            log.info("挂起流程:" + processDefinition.getId() + "-" + processDefinition.getName());
            repositoryService.suspendProcessDefinitionById(processDefinition.getId());

        }
    }

    /**
     * 挂起的流程启动会报错
     */
    @Test
    public void testRunSuspendProcess() {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.startProcessInstanceById("HolidayUI:4:30004");

    }
}

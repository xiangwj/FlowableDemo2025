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

/***
 * 候选人
 */
@SpringBootApplication
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class Test07 {
    ProcessEngine processEngine;

    @BeforeAll
    public void setup() {
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("test07.cfg.xml");
        processEngine = configuration.buildProcessEngine();
    }
    @Test
    public void Testdeploy() {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder deployment = repositoryService.createDeployment();
        Deployment deploy = deployment.addClasspathResource("bpmnfiles/Holiday07.bpmn20.xml")
                .name("Holiday07")
                .deploy();
        log.info("deploy.getId():" + deploy.getId());
        log.info("deploy.getKey():" + deploy.getKey());
        log.info("deploy.getName():" + deploy.getName());
    }
    @Test
    public void TestRun() {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Map<String, Object> variables = new HashMap<>();
        variables.put("candidate0","zhangsan");
        variables.put("candidate1","lisi");
        variables.put("candidate2","wanger");
        runtimeService.startProcessInstanceByKey("Holiday07",variables);

    }
    /***
     * 根据候选人查任务
     */
    @Test
    public void TestQuery(){
        TaskService taskService = processEngine.getTaskService();
        TaskQuery taskQuery = taskService.createTaskQuery();
        Task task = taskQuery.processDefinitionKey("Holiday07").taskCandidateUser("lisi").singleResult();
        log.info("task.getId():" + task.getId());
        log.info("task.getName():" + task.getName());
    }
    /***'
     * 拾取任务
     */
    @Test
    public void testClaimTask(){
        TaskService taskService = processEngine.getTaskService();
        TaskQuery taskQuery = taskService.createTaskQuery();
        Task task = taskQuery.processDefinitionKey("Holiday07").taskCandidateUser("zhangsan").singleResult();
        taskService.claim(task.getId(), "zhangsan");
        log.info("任务拾取成功");
    }
    /***
     * 退还任务
     */
    @Test
    public void testUnclaimTask(){
        TaskService taskService = processEngine.getTaskService();
        TaskQuery taskQuery = taskService.createTaskQuery();
        Task task = taskQuery.processDefinitionKey("Holiday07").taskAssignee("zhangsan").singleResult();
        taskService.unclaim(task.getId());
    }
    /***
     * 任务转发
     */
    @Test
    public void testTransfor(){
        TaskService taskService = processEngine.getTaskService();
        TaskQuery taskQuery = taskService.createTaskQuery();
        Task task = taskQuery.processDefinitionKey("Holiday07").taskCandidateUser("zhangsan").singleResult();
        taskService.setAssignee(task.getId(),"lisi");

    }
    /***
     * 完成任务
     */
    @Test
    public void testComplete(){
        TaskService taskService = processEngine.getTaskService();
        TaskQuery taskQuery = taskService.createTaskQuery();
        Task task = taskQuery.processDefinitionKey("Holiday07").taskAssignee("lisi").singleResult();
        taskService.complete(task.getId());

    }
}

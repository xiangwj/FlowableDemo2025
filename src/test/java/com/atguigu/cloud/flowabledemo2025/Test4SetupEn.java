package com.atguigu.cloud.flowabledemo2025;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.*;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Slf4j
public class Test4SetupEn {
    @Value("${mydatabase.driver}")
    String databaseDriver;
    @Value("${mydatabase.username}")
    String databaseUserName;
    @Value("${mydatabase.passwd}")
    String databasePassword;
    @Value("${mydatabase.url}")
    String databaseUrl;
    static ProcessEngineConfiguration configuration = null;

    @BeforeAll
    public void setUp() {
        configuration = new StandaloneProcessEngineConfiguration();
        configuration.setJdbcDriver(databaseDriver);
        configuration.setJdbcUsername(databaseUserName);
        configuration.setJdbcPassword(databasePassword);
        configuration.setJdbcUrl(databaseUrl);
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

    }

    @Test
    public void testDeploy() {
        ProcessEngine processEngine = configuration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder deployment = repositoryService.createDeployment();
        Deployment deploy = deployment.addClasspathResource("holiday-request.bpmn20.xml").deploy();
        log.info("deployid=" + deploy.getId());
    }

    @Test
    public void testDeployQuery() {
        ProcessEngine processEngine = configuration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId("1").singleResult();
        log.info("processDefinition.getId():" + processDefinition.getId());
        log.info("processDefinition.getName():" + processDefinition.getName());
    }

    @Test
    public void testDeleteDeployment() {
        ProcessEngine processEngine = configuration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.deleteDeployment("10001");
    }

    @Test
    public void testRunProcess() {
        ProcessEngine processEngine = configuration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().list().get(0);
        String processDefinitionId = processDefinition.getKey();

        RuntimeService runtimeService = processEngine.getRuntimeService();
        HashMap<String, Object> variables = new HashMap<>();
        variables.put("employee", "张三");
        variables.put("nrOfHolidays", "3");
        variables.put("description", "出去玩啊");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinition.getKey(), variables);
        log.info("ProcessDefinitionId" + processInstance.getProcessDefinitionId());
        log.info("ActivityId" + processInstance.getActivityId());
        log.info("ActivityId" + processInstance.getActivityId());

    }
    @Test
    public void testQueryTask(){
        ProcessEngine processEngine = configuration.buildProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        TaskQuery taskQuery = taskService.createTaskQuery();
        taskQuery.processDefinitionKey("holidayRequest").taskAssignee("zhangsan");
        List<Task> tasks = taskQuery.list();
        tasks.stream().forEach(task->{
            log.info("task.getId():" + task.getId());
            log.info("task.getName():" + task.getName());
            log.info("task.getAssignee:" + task.getAssignee());
        });
    }
    /**
     * 完成任务
     */
    @Test
    public void testDoTask(){
        ProcessEngine processEngine = configuration.buildProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        TaskQuery taskQuery = taskService.createTaskQuery();
        taskQuery.processDefinitionKey("holidayRequest").taskAssignee("zhangsan");
        List<Task> tasks = taskQuery.list();
        HashMap<String, Object> variables = new HashMap<>();
        variables.put("approved",false);
        tasks.stream().forEach(task->{
            taskService.complete(task.getId(),variables);
        });
    }

}

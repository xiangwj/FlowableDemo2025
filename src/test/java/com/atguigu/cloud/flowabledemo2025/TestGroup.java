package com.atguigu.cloud.flowabledemo2025;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.GroupQuery;
import org.flowable.idm.api.User;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;

/***
 * 组和用户
 */
@SpringBootApplication
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class TestGroup {
    ProcessEngine processEngine;

    @BeforeAll
    public void setup() {
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("test08.cfg.xml");
        processEngine = configuration.buildProcessEngine();
    }

    /***
     * 创建用户
     */
    @Test
    public void createUser() {
        IdentityService identityService = processEngine.getIdentityService();
        User user002 = identityService.newUser("user002");
        user002.setFirstName("user");
        user002.setLastName("0002");
        identityService.saveUser(user002);
    }

    /***
     * 创建组
     */
    @Test
    public void createGroup() {
        IdentityService identityService = processEngine.getIdentityService();
        Group group001 = identityService.newGroup("group001");
        group001.setName("group001");
        group001.setType("selfgroup");
        identityService.saveGroup(group001);

    }

    /****
     * 分配用户到组
     */
    @Test
    public void assignUserToGroup() {
        IdentityService identityService = processEngine.getIdentityService();
        GroupQuery groupQuery = identityService.createGroupQuery();
        Group group001 = groupQuery.groupId("group001").singleResult();
        User user001 = identityService.createUserQuery().userId("user001").singleResult();
        User user002 = identityService.createUserQuery().userId("user002").singleResult();
        identityService.createMembership(user001.getId(), group001.getId());
        identityService.createMembership(user002.getId(), group001.getId());
    }

    /***
     *发布
     */
    @Test
    public void Testdeploy() {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder deployment = repositoryService.createDeployment();
        Deployment deploy = deployment.addClasspathResource("bpmnfiles/MyGroupAndUser.bpmn20.xml")
                .key("MyGroupAndUserKey")
                .name("MyGroupAndUserKey")
                .deploy();
        log.info("deploy.getId():" + deploy.getId());
        log.info("deploy.getKey():" + deploy.getKey());
        log.info("deploy.getName():" + deploy.getName());
    }

    /***
     * 启动流程
     */
    @Test
    public void TestRunProcessInstance() {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        HashMap<String, Object> variables = new HashMap<>();
        variables.put("g1", "group001");
        runtimeService.startProcessInstanceByKey("MyGroupAndUserKey", variables);
    }

    /**
     * 通过组id来查询任务，完成任务
     */
    @Test
    public void testQueryTaskByGroup() {
        TaskService taskService = processEngine.getTaskService();
        IdentityService identityService = processEngine.getIdentityService();
        Group targetGroup = identityService.createGroupQuery().groupMember("user001").singleResult();
        System.out.println("targetGroup = " + targetGroup);
        Task task = taskService.createTaskQuery().processDefinitionKey("MyGroupAndUserKey")
                .taskCandidateGroup(targetGroup.getId()).singleResult();

    }
    /**
     * 拾取任务
     */
    @Test
    public void testClaimTask() {
        TaskService taskService = processEngine.getTaskService();
        IdentityService identityService = processEngine.getIdentityService();
        Group targetGroup = identityService.createGroupQuery().groupMember("user001").singleResult();
        System.out.println("targetGroup = " + targetGroup);
        Task task = taskService.createTaskQuery().processDefinitionKey("MyGroupAndUserKey")
                .taskCandidateGroup(targetGroup.getId()).singleResult();
        if(task != null) {
            taskService.claim(task.getId(),"user001");
            taskService.complete(task.getId());
            System.out.println("拾取任务成功 " );
        }
    }
}

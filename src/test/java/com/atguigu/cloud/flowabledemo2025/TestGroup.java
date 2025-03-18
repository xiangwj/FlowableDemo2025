package com.atguigu.cloud.flowabledemo2025;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.IdentityService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.GroupQuery;
import org.flowable.idm.api.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
        identityService.createMembership(user001.getId(),group001.getId());
        identityService.createMembership(user002.getId(),group001.getId());


    }
}

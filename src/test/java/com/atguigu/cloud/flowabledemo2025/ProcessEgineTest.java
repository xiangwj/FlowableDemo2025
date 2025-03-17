package com.atguigu.cloud.flowabledemo2025;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.ProcessEngines;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Slf4j
public class ProcessEgineTest {
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
    /**
     *读取flowable.cfg.xml，把flowablelearn1下的表全部建立起来
     */
    @Test
    public void processEngine2() {
        //读取flowable.cfg.xml，把flowablelearn1下的表全部建立起来
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        System.out.println("defaultProcessEngine = " + defaultProcessEngine);
    }

    /**
     * 加载自定义的配置文件 selfflowable.cfg.xml
     */
    @Test
    public void processEngine3() {
        //读取application.yml，把flowablelearn1下的表全部建立起来
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("selfflowable.cfg.xml");
        ProcessEngine processEngine = configuration.buildProcessEngine();
        System.out.println("defaultProcessEngine = " + processEngine);
    }
}

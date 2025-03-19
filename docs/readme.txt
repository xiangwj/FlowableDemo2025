视频
https://www.bilibili.com/video/BV1PY411F7ir?spm_id_from=333.788.player.switch&vd_source=5d5cbae218c85563a0476defe2a6d9af&p=10
https://www.bilibili.com/video/BV1PY411F7ir?spm_id_from=333.788.player.switch&vd_source=5d5cbae218c85563a0476defe2a6d9af&p=11

官方文档
https://tkjohn.github.io/flowable-userguide/#sources
重建数据库
DROP DATABASE flowablelearn;
CREATE DATABASE flowablelearn
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;


flowable下载地址
https://github.com/flowable/flowable-engine/releases
用的是6.7.2
放到tomcat里
http://localhost:8080/flowable-ui/
用户名/密码：admin/test
tomcat启动有乱码，需要把conf/logging.properties java.util.logging.ConsoleHandler.encoding = GBK

springboot自动部署
放在resource/processes/下的流程定义





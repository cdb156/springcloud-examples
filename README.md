Spring Cloud 微服务示例
=====================

* 根据业务模块划分服务种类
* 每个服务可独立部署且相互隔离
* 通过轻量级API调用服务
* 服务需保证良好的高可用

## 一、工程说明

* [node-gateway](https://github.com/cjp1016/springcloud-examples/tree/master/node-gateway) 基于nodejs的网关服务开发，调用zk获取springboot-hello提供的服务。

* [springboot-docker](https://github.com/cjp1016/springcloud-examples/tree/master/springboot-docker) spring-boot-admin-client 1.5.x和基于docker远程部署示例。

* [springboot-hello](https://github.com/cjp1016/springcloud-examples/tree/master/springboot-hello) 简单的服务端，采用zk注册服务，提供客户端调用。

* [springboot-monitor](https://github.com/cjp1016/springcloud-examples/tree/master/springboot-monitor) spring-boot-admin-server 1.5.x 监控服务端。

* [springcloud-discovery-eureka](https://github.com/cjp1016/springcloud-examples/tree/master/springcloud-discovery-eureka) springcloud-discovery-eureka 基于Eureka服务发现的服务端，推荐集群模式


## 二、版本说明

核心发布说明：

* [https://github.com/spring-projects/spring-cloud/wiki](https://github.com/spring-projects/spring-cloud/wiki)

版本说明：

* [https://github.com/spring-cloud/spring-cloud-release/releases](https://github.com/spring-cloud/spring-cloud-release/releases)

* [https://github.com/spring-cloud/spring-cloud-release/milestones](https://github.com/spring-cloud/spring-cloud-release/milestones)

| SpringCloud版本               | SpringBoot版本 | 发布说明                                                     | 发布时间   |
| ----------------------------- | -------------- | ------------------------------------------------------------ | ---------- |
| Greenwich SR2  GA 格林威治 镇 | 2.1.6.RELEASE  | [Spring-Cloud-Greenwich-Release-Notes](https://github.com/spring-projects/spring-cloud/wiki/Spring-Cloud-Greenwich-Release-Notes) |            |
| Finchley SR4 GA  芬奇利 市    | 2.0.9.RELEASE  | [Spring-Cloud-Finchley-Release-Notes](https://github.com/spring-projects/spring-cloud/wiki/Spring-Cloud-Finchley-Release-Notes) | 2019-06-08 |
| Edgware SR6 GA  艾奇韦尔 站   | 1.5.21.RELEASE | [Spring-Cloud-Edgware-Release-Notes](https://github.com/spring-projects/spring-cloud/wiki/Spring-Cloud-Edgware-Release-Notes) | 2019-05-23 |
| Dalston SR5 GA  达尔斯顿 区   | 1.5.4.RELEASE  | [Spring-Cloud-Dalston-Release-Notes](https://github.com/spring-projects/spring-cloud/wiki/Spring-Cloud-Dalston-Release-Notes) | 2017-12-26 |

Spring Cloud Finchley builds on Spring Boot 2.0.x and is not compatible with 1.x.y.



文档地址：

* [https://cloud.spring.io/spring-cloud-static/Greenwich.SR2/single/spring-cloud.html](https://cloud.spring.io/spring-cloud-static/Greenwich.SR2/single/spring-cloud.html)

* [https://cloud.spring.io/spring-cloud-static/Finchley.SR4/single/spring-cloud.html](https://cloud.spring.io/spring-cloud-static/Finchley.SR4/single/spring-cloud.html)
* [https://cloud.spring.io/spring-cloud-static/Edgware.SR6/single/spring-cloud.html](https://cloud.spring.io/spring-cloud-static/Edgware.SR6/single/spring-cloud.html)

* [https://cloud.spring.io/spring-cloud-static/Dalston.SR5/single/spring-cloud.html](https://cloud.spring.io/spring-cloud-static/Dalston.SR5/single/spring-cloud.html)
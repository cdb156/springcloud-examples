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

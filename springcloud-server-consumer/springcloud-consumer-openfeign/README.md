spring-cloud-openfeign
====================

官方文档：

[https://cloud.spring.io/spring-cloud-static/spring-cloud-openfeign/2.0.4.RELEASE/](https://cloud.spring.io/spring-cloud-static/spring-cloud-openfeign/2.0.4.RELEASE/)



## 使用说明

在`Spring Cloud OpenFeign`中，除了`OpenFeign`自身提供的标注（annotation）之外，还可以使用`JAX-RS`标注，或者`Spring MVC`标注。

`OpenFeign`提供了两个重要标注`@FeignClient`和`@EnableFeignClients`。

* `@FeignClient`  ：标注用于声明Feign客户端可访问的Web服务。

* `@EnableFeignClients`  ：标注用于修饰Spring Boot应用的入口类，以通知Spring Boot启动应用时，扫描应用中声明的Feign客户端可访问的Web服务。


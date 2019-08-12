springcloud-gateway-zuul
=======================

> `@EnableZuulServer`、`@EnableZuulProxy`两个注解

`@EnableZuulProxy`简单理解为`@EnableZuulServer`的增强版，当`Zuul`与`Eureka`、`Ribbon`等组件配合使用时，我们使用`@EnableZuulProxy`。 

## 网关配置

### 代码配置：pom.xml 、application.yml 、ZuulApplication

pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>springcloud-examples</artifactId>
        <groupId>cn.selinx</groupId>
        <version>0.0.1</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <groupId>cn.selinx</groupId>
    <artifactId>springcloud-gateway-zuul</artifactId>
    <version>0.0.1</version>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>

        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
		<!-- zuul 网关 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>


</project>
```

application.yml配置

```yaml
spring:
  application:
    name: @project.artifactId@

server:
  port: 9000

# eureka 注册中心
eureka:
  instance:
    prefer-ip-address: true
  client:
    service-url:
      defaultZone: http://master.docker:8761/eureka/,http://master.docker:8762/eureka/,http://master.docker:8763/eureka/

# zuul 配置路由
zuul:
  routes:
    bms-1:
      path: /server/**
      serviceId: springcloud-server-bms

```

ZuulApplication.java

```java
package cn.selinx.cloud.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * 开启路由网关功能,开启eureka注解
 * @author JiePeng Chen
 * @since 2019/8/9 9:13
 */
@EnableZuulProxy
@EnableEurekaClient
@SpringBootApplication
public class ZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication.class, args);
    }
}

```

### 网关的默认路由规则

但是如果后端服务多达十几个的时候，每一个都这样配置也挺麻烦的，spring cloud zuul已经帮我们做了默认配置。默认情况下，Zuul会代理所有注册到Eureka Server的微服务，并且Zuul的路由规则如下：`http://ZUUL_HOST:ZUUL_PORT/微服务在Eureka上的serviceId/**`会被转发到serviceId对应的微服务。
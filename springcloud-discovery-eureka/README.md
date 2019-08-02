springcloud-discovery-eureka
=======================

## server服务端配置

### pom.xml 配置

#### parent父模块的pom.xml配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>cn.selinx</groupId>
    <artifactId>springcloud-examples</artifactId>
    <packaging>pom</packaging>
    <version>0.0.1</version>

    <!-- 提供SpringBoot依赖 -->
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.9.RELEASE</version>
        <relativePath />
    </parent>

	<!-- 提供SpringCloud依赖 -->
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>Finchley.SR4</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <modules>
        <module>springcloud-discovery-eureka</module>
    </modules>
    
</project>
```

#### server子模块的pom.xml配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>cn.selinx</groupId>
    <artifactId>springcloud-discovery-eureka</artifactId>
    <version>0.0.1</version>

    <parent>
        <groupId>cn.selinx</groupId>
        <artifactId>springcloud-examples</artifactId>
        <version>0.0.1</version>
    </parent>


    <properties>
        <spring-cloud.version>Finchley.SR4</spring-cloud.version>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>

        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
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



### 单机模式运行

```yaml
# 1. 采用默认端口8761模式运行
#####################
# 默认模式：端口 8761
server:
  port: 8761

# 服务信息
spring:
  application:
    name: @project.artifactId@
    
#####################
# 2. 修改服务端口模式，需要指定客户端注册服务url
---
# 单机模式
# eureka.client.register-with-eureka: 是否将自己注册到 Eureka server,默认true
# eureka.client.fetch-registry: 此服务是否要去注册中心，获取服务列表信息,默认true
spring:
  profiles: singleton

server:
  port: 8080

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8080/eureka/
#####################
# 在vm option
-Dspring.profiles.active=singleton
```

![eureka-singleton.png](/images/eureka-singleton.png)



### 集群模式运行

指定`application.yml`的多环境配置

```yaml
######--------集群模式-------######
---
spring:
  profiles: cluster-1
  application:
    name: @project.artifactId@-1

server:
  port: 8761

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/,http://localhost:8762/eureka/,http://localhost:8763/eureka/

---
spring:
  profiles: cluster-2
  application:
    name: @project.artifactId@-2

server:
  port: 8762

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/,http://localhost:8762/eureka/,http://localhost:8763/eureka/

---
spring:
  profiles: cluster-3
  application:
    name: @project.artifactId@-3

server:
  port: 8763

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/,http://localhost:8762/eureka/,http://localhost:8763/eureka/
```

启动说明，开启3个cmd窗口，运行集群

```shell
# 打包编译
mvn -DskitTests -U clean package
# 第一台机器
java -server -Xms128m -Xmx128m -jar target/springcloud-discovery-eureka-0.0.1.jar --spring.profiles.active=cluster-1
# 第二台机器
java -server -Xms128m -Xmx128m -jar target/springcloud-discovery-eureka-0.0.1.jar --spring.profiles.active=cluster-2
# 第三台机器
java -server -Xms128m -Xmx128m -jar target/springcloud-discovery-eureka-0.0.1.jar --spring.profiles.active=cluster-3
```

![eureka-cluster](/images/eureka-cluster.png)

#### SpringBoot打包和运行命令说明

```shell
mvn -DskitTests -U clean package
# 指定堆大小，以服务器模式运行，指定端口,指定激活profiles
java -server -Xms128m -Xmx128m -jar target/springcloud-discovery-eureka-0.0.1.jar --server.port=8080 --spring.profiles.active=xxx
```


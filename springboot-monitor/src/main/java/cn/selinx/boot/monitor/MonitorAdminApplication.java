package cn.selinx.boot.monitor;

import de.codecentric.boot.admin.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

/**
 * Spring-Boot-Admin 1.5.7 版本
 *
 * http://codecentric.github.io/spring-boot-admin/1.5.7/
 *
 * spring-boot-admin 服务端
 * @author JiePeng Chen
 * @since 2019/7/24 10:10
 */
@SpringBootApplication
@EnableAdminServer
public class MonitorAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonitorAdminApplication.class, args);
    }
}

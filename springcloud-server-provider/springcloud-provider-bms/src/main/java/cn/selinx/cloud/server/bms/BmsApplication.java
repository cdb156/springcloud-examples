package cn.selinx.cloud.server.bms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * back manage service 后台管理服务
 * @author JiePeng Chen
 * @since 2019/8/6 16:46
 */
@SpringBootApplication
@EnableEurekaClient
public class BmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BmsApplication.class, args);
    }
}

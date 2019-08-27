package cn.selinx.cloud.api.hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author JiePeng Chen
 * @since 2019/8/13 14:20
 */
@SpringBootApplication
@EnableEurekaClient
public class HelloServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloServerApplication.class, args);
    }

}

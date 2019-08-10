package cn.selinx.cloud.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author JiePeng Chen
 * @since 2019/8/8 22:12
 */
@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
public class ConsumerOpenFeignApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerOpenFeignApplication.class, args);
    }
}

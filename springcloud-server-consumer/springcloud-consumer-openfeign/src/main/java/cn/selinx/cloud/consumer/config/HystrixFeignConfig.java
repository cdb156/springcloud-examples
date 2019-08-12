package cn.selinx.cloud.consumer.config;

import feign.Contract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author JiePeng Chen
 * @since 2019/8/10 18:35
 */
//@Configuration
public class HystrixFeignConfig {

    /**
     * 默认配置
     */
    @Bean
    public Contract getContract() {
        return new feign.Contract.Default();

    }
}

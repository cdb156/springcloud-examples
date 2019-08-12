package cn.selinx.cloud.consumer.config;

import feign.Contract;
import feign.Feign;
import feign.Logger;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 局部关闭熔断器 {@link FeignClientsConfiguration}
 * <p>
 * 注意：是否应该不加入 {@link Configuration} 注解
 *
 * @author JiePeng Chen
 * @since 2019/8/10 17:21
 */
//@Configuration
public class HystrixCloseConfig {

    /**
     * 默认配置
     */
    @Bean
    public Contract getContract() {
        return new feign.Contract.Default();

    }

    /**
     * feign日志配置
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    @Scope(SCOPE_PROTOTYPE)
    public Feign.Builder feignBuilder() {
        return Feign.builder();
    }
}

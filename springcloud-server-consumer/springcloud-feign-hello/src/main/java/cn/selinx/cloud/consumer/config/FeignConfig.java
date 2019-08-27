package cn.selinx.cloud.consumer.config;

import cn.selinx.cloud.api.hello.HelloApiFeignScanConfig;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * @author JiePeng Chen
 * @since 2019/8/13 16:26
 */
@Configuration
@EnableFeignClients(basePackageClasses = HelloApiFeignScanConfig.class)
public class FeignConfig {
}

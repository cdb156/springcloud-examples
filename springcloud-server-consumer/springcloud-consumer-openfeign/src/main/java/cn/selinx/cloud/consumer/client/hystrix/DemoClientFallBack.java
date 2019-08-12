package cn.selinx.cloud.consumer.client.hystrix;

import cn.selinx.cloud.consumer.client.DemoClient;
import cn.selinx.cloud.consumer.client.HelloClient;
import org.springframework.stereotype.Component;

/**
 * 远程调用-熔断器服务
 *
 * @author JiePeng Chen
 * @since 2019/8/10 15:24
 */
@Component
public class DemoClientFallBack implements DemoClient {

    @Override
    public String hello() {
        return "Hello,HystricFeign--HelloClient";
    }
}

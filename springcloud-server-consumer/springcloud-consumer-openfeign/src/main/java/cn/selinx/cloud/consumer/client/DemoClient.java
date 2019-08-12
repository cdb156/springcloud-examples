package cn.selinx.cloud.consumer.client;

import cn.selinx.cloud.consumer.client.hystrix.HelloClientFallBack;
import cn.selinx.cloud.consumer.config.HystrixCloseConfig;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 远程调用服务,加入熔断器
 * @author JiePeng Chen
 * @since 2019/8/8 22:41
 */
@FeignClient(name = "springcloud-gateway-zuul",
             fallback = HelloClientFallBack.class)
public interface DemoClient {

//    @RequestMapping(value = "/springcloud-server-bms/bms/user/hello",method = GET)
    @RequestLine("GET /demo/demo")
    String hello();

}

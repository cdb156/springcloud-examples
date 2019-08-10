package cn.selinx.cloud.consumer.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 *
 * @author JiePeng Chen
 * @since 2019/8/8 22:41
 */
@FeignClient(name = "springcloud-gateway-zuul")
public interface HelloClient {

    @RequestMapping(value = "/springcloud-server-bms/bms/user/hello",method = GET)
    String hello();
}

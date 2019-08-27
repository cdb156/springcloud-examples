package cn.selinx.cloud.api.hello.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author JiePeng Chen
 * @since 2019/8/13 14:29
 */
@FeignClient("hello-server")
public interface HelloClient {

    @RequestMapping(value = "/",method = GET)
    String hello();
}

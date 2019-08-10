package cn.selinx.cloud.consumer.web;

import cn.selinx.cloud.consumer.client.HelloClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JiePeng Chen
 * @since 2019/8/8 22:40
 */
@RestController
public class HelloController {

    @Autowired
    HelloClient helloClient;

    @GetMapping("/")
    public String hello() {
        String str = helloClient.hello();
        return str;
    }
}

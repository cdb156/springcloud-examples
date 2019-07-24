package cn.selinx.boot.hello.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JiePeng Chen
 * @since 2019/7/19 23:29
 */
@RestController
public class HelloController {

    /**
     * name 属性作为服务发现，服务名称
     * @return
     */
    @GetMapping(name = "HelloService", path = "/hello")
    public String hello() {
        System.out.println("正在请求：HelloService服务");
        return "boot";
    }
}

package cn.selinx.cloud.api.hello.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JiePeng Chen
 * @since 2019/8/13 14:22
 */
@RestController
public class HelloController {

    @GetMapping("/")
    public String hello(){
        return "hello-server";
    }
}

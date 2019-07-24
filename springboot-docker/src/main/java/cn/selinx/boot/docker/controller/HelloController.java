package cn.selinx.boot.docker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JiePeng Chen
 * @since 2019/7/23 18:00
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        return "Hello,Docker";
    }
}

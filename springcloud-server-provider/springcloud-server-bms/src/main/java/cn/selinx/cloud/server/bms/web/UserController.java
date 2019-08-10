package cn.selinx.cloud.server.bms.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户Controller
 *
 * @author JiePeng Chen
 * @since 2019/8/6 16:43
 */
@RestController
@RequestMapping("/bms/user")
public class UserController {

    @GetMapping("/hello")
    public String hello(){
        return "hello,user2";
    }


}

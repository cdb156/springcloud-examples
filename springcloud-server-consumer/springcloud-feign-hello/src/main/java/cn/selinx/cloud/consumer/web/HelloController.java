package cn.selinx.cloud.consumer.web;

import cn.selinx.cloud.api.hello.client.HelloClient;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author JiePeng Chen
 * @since 2019/8/8 22:40
 */
@RestController
public class HelloController {

    @Autowired
    HelloClient helloClient;

    @Autowired
    EurekaClient eurekaClient;

    @GetMapping("/")
    public String hello() {
        String str = helloClient.hello();
        return str;
    }

    @GetMapping("/instance/{id}")
    public List<InstanceInfo> instance(@PathVariable String id) {
        List<InstanceInfo> instances = this.eurekaClient.getInstancesById(id);
        return instances;
    }
}

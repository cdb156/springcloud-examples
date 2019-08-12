package cn.selinx.cloud.monitor.hystrix;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

/**
 * 基于turbine的集群熔断监控
 *
 * @author JiePeng Chen
 * @since 2019/8/10 19:58
 */
@SpringCloudApplication
@EnableHystrixDashboard
@EnableTurbine
public class HystrixApplication {
    public static void main(String[] args) {
        SpringApplication.run(HystrixApplication.class, args);
    }
}

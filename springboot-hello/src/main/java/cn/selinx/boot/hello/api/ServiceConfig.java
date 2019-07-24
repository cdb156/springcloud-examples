package cn.selinx.boot.hello.api;

import cn.selinx.boot.hello.api.impl.ServiceRegistryImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author JiePeng Chen
 * @since 2019/7/22 16:01
 */
@Configuration
@ConfigurationProperties(prefix = "registry")
public class ServiceConfig {

    private String servers;

    @Bean
    public ServiceRegistry serviceRegistry(){
        return new ServiceRegistryImpl(servers);
    }

    public String getServers() {
        return servers;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }
}

package cn.selinx.cloud.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.env.RandomValuePropertySource;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.CommandLinePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.web.context.support.StandardServletEnvironment;

import java.util.*;

/**
 * 参考SpirngCloud-Config读取配置的核心
 * 资源主类:PropertyPlaceholderAutoConfiguration
 * @author JiePeng Chen
 * @since 2019/8/23 17:16
 */
public class ConfigFileApplication {

    private static Logger logger = LoggerFactory.getLogger(ConfigFileApplication.class);

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        SpringApplicationBuilder builder = new SpringApplicationBuilder(PropertyPlaceholderAutoConfiguration.class);
        ConfigurableEnvironment environment = new StandardEnvironment();
        builder.environment(environment)
                .web(WebApplicationType.NONE)
                .bannerMode(Banner.Mode.OFF)
                .application().setListeners(Arrays.asList(new ConfigFileApplicationListener()));
        if(null == args || args.length ==0){
            args = getArgs("D:/config-file/config-repo-native/order-dev.properties");
        }
        // 此时environment已经读取到外部化配置文件
        ConfigurableApplicationContext context = builder.run(args);
        logger.warn("清理前PropertySources数目 = {}",environment.getPropertySources().size());
        clean(environment);
        logger.warn("清理后PropertySources数目 = {}",environment.getPropertySources().size());
        long endTime=System.currentTimeMillis();
        System.out.printf("程序运行时间：%d 毫秒 %n",endTime-startTime);
        logger.warn("读取配置文件profile = {}",environment.getProperty("profile"));

    }

    private static String[] getArgs(String location) {
        List<String> list = new ArrayList<String>();
        list.add("--spring.config.name=application");
        list.add("--spring.cloud.bootstrap.enabled=false");
        list.add("--encrypt.failOnError=false");
        list.add("--spring.config.location="+location);
        return list.toArray(new String[0]);
    }

    // 清理
    private static void clean(ConfigurableEnvironment environment){
        // 标准Sources
        Set<String> standardSources = new HashSet<String>(Arrays.asList("vcap",
                StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME,
                StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME,
                CommandLinePropertySource.COMMAND_LINE_PROPERTY_SOURCE_NAME,
                CommandLinePropertySource.DEFAULT_NON_OPTION_ARGS_PROPERTY_NAME,
                RandomValuePropertySource.RANDOM_PROPERTY_SOURCE_NAME,
                StandardServletEnvironment.JNDI_PROPERTY_SOURCE_NAME,
                "springCloudClientHostInfo",
                StandardServletEnvironment.SERVLET_CONFIG_PROPERTY_SOURCE_NAME,
                StandardServletEnvironment.SERVLET_CONTEXT_PROPERTY_SOURCE_NAME));
        for(String name : standardSources){
            if(environment.getPropertySources().contains(name)){
                environment.getPropertySources().remove(name);
            }
        }

    }

}

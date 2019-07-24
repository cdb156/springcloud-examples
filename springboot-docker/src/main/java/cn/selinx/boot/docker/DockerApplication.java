package cn.selinx.boot.docker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 指定堆大小：-Xms50m -Xmx50m
 *
 * @author JiePeng Chen
 * @since 2019/7/23 18:00
 */
@SpringBootApplication
public class DockerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DockerApplication.class, args);
    }
}

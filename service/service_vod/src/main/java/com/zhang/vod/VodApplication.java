package com.zhang.vod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
//要用 自动填充，swagger，相关的返回结果
@ComponentScan(basePackages = {"com.zhang"})
public class VodApplication {


    public static void main(String[] args) {
        SpringApplication.run(VodApplication.class, args);
    }
}

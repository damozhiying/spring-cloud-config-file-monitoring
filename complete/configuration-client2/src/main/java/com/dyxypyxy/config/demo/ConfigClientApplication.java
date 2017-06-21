package com.dyxypyxy.config.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class ConfigClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigClientApplication.class, args);
    }
}

@RefreshScope
@RestController
@Configuration
class MessageRestController {

    @Value("${db.url:<url not defined>}")
    private String dbUrl;

    @Value("${db.provider:<provider not defined>}")
    private String dbProvider;

    @RequestMapping("/config")
    String getConfig() {
        return "DB URL : " + this.dbUrl + " -- Provider: " + dbProvider;
    }
}
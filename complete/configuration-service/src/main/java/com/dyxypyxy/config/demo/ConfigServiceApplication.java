package com.dyxypyxy.config.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableConfigServer
//@EnableAdminServer
@SpringBootApplication
public class ConfigServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ConfigServiceApplication.class, args);
    }
}

@RestController
class MessageRestController {

    @RequestMapping("/healthcheck")
    String doHealthcheck() {
        return "0";
    }
}

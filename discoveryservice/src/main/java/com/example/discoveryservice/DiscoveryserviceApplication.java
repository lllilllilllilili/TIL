package com.example.discoveryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer //spring 3버전에서 제대로 동작하려나..?
public class DiscoveryserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscoveryserviceApplication.class, args);
    }

}
//No active profile set, falling back to 1 default profile: "default"
//Tomcat started on port 8761 (http) with context path ''
//http://127.0.0.1:8761/ 로 접속
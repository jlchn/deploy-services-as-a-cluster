package com.jlchn.demoservice1;


import java.util.Collections;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.jlchn.demoservice1.context.UserContextCopyInterceptor;

@SpringBootApplication
@RefreshScope
@EnableCircuitBreaker
@EnableDiscoveryClient
public class Application {

    /**
     * create a rest template which supporting client round robbin
     * @return
     */
    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate(){
        RestTemplate template = new RestTemplate();
        List interceptors = template.getInterceptors();
        if (interceptors == null) {
            template.setInterceptors(Collections.singletonList(new UserContextCopyInterceptor()));
        } else {
            interceptors.add(new UserContextCopyInterceptor());
            template.setInterceptors(interceptors);
        }

        return template;
    }
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

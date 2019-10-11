package com.jlchn.demoservice1.config;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

import com.jlchn.demoservice1.hystrix.ThreadLocalCopyStrategy;
import com.netflix.hystrix.strategy.HystrixPlugins;

@Configuration
public class HystrixThreadLocalConfig {

    @PostConstruct
    public void init() {
        HystrixPlugins.getInstance().registerConcurrencyStrategy(new ThreadLocalCopyStrategy());
    }
}

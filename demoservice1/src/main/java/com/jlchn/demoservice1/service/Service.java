package com.jlchn.demoservice1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.jlchn.demoservice1.hystrix.UserContextHolder;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Component
public class Service {

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "fallback",
            threadPoolKey = "my-thread-pool",
            threadPoolProperties =
                    {@HystrixProperty(name = "coreSize",value="30"),
                            @HystrixProperty(name="maxQueueSize", value="10")},
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "500"),
                    @HystrixProperty(name="circuitBreaker.requestVolumeThreshold", value="10"),
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value="50"),
                    @HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds", value="7000"),
                    @HystrixProperty(name="metrics.rollingStats.timeInMilliseconds", value="10000"),
                    @HystrixProperty(name="metrics.rollingStats.numBuckets", value="10")
            /**
             * https://stackoverflow.com/a/38525660
             * https://github.com/Netflix/Hystrix/wiki/How-it-Works
             * https://segmentfault.com/a/1190000014053426#articleHeader5
             * metrics.rollingStats.timeInMilliseconds % metrics.rollingStats.numBuckets == 0
            */
            }
    )
    public String getFromRemote(){
        ResponseEntity<String> responseEntity = restTemplate.exchange("http://demoservice2/v1/", HttpMethod.GET, null, String.class);
        System.out.println(Thread.currentThread().getName() + " " + UserContextHolder.getContext());
        return responseEntity.getBody();
    }

    private String fallback(){
        System.out.println(Thread.currentThread().getName() + " fallback");
        return "fallback";
    }
}

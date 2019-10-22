package com.jlchn.demoservice2.controllers;

import java.util.Random;

import com.jlchn.demoservice2.config.ServiceConfig;
import com.jlchn.demoservice2.context.UserContextHolder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value="v1")
@Slf4j
public class DemoServiceController {

    @Autowired
    private ServiceConfig serviceConfig;

    @Autowired
    private Tracer tracer;

    @RequestMapping(value="/",method = RequestMethod.GET)
    public String getLicenses() {
        randomSleep();
        log.info("{}", UserContextHolder.getContext());
        return serviceConfig.getExampleProperty();
    }


    private void randomSleep(){
        Random rand = new Random();
        int num = rand.nextInt(3) + 1;
        Span span = tracer.createSpan("readFromDatabase");

        if (num == 3) {

            try {
                System.out.println("lets sleep 2 seconds");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        span.tag("peer.service", "database");
        span.logEvent(Span.CLIENT_RECV);
        tracer.close(span);
    }
}

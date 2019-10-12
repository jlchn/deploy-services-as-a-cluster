package com.jlchn.demoservice1.controllers;

import com.jlchn.demoservice1.context.UserContextHolder;
import com.jlchn.demoservice1.service.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value="v1")
@Slf4j
public class DemoServiceController {

    @Autowired
    private Service service;

    @RequestMapping(value="/",method = RequestMethod.GET)
    public String getLicenses() {

        log.info("{} : {}", Thread.currentThread().getName(), UserContextHolder.getContext());
       return service.getFromRemote();
    }



}

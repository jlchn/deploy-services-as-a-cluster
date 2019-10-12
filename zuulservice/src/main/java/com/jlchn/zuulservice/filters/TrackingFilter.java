package com.jlchn.zuulservice.filters;

import com.netflix.zuul.ZuulFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TrackingFilter extends ZuulFilter{

    @Autowired
    private Tracer tracer;

    @Autowired
    private  FilterUtils filterUtils;

    @Override
    public String filterType() {
        return FilterUtils.PRE_FILTER_TYPE;
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    public boolean shouldFilter() {
        return true;
    }


    public Object run() {

        if (filterUtils.getSessionId() == null) {
            filterUtils.setSessionId(tracer.getCurrentSpan().traceIdString());
            log.info("session-id generated: {}.", filterUtils.getSessionId());
        }
        return null;
    }
}
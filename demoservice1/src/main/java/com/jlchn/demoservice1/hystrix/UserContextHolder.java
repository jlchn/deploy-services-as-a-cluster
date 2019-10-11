package com.jlchn.demoservice1.hystrix;

import org.springframework.util.Assert;

public class UserContextHolder {
    private static final ThreadLocal<String> userContext = new ThreadLocal<>();

    public static final String getContext(){
        String context = userContext.get();

        if (context == null) {
            context = "new context set in spring boot thread";
            userContext.set(context);

        }
        return userContext.get();
    }

    public static final void setContext(String context) {
        Assert.notNull(context, "Only non-null UserContext instances are permitted");
        userContext.set(context);
    }

}

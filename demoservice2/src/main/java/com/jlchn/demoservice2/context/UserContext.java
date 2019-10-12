package com.jlchn.demoservice2.context;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class UserContext {
    public static final String SESSION_ID = "session-id";
    public static final String TOKEN = "token";
    public static final String USER_ID = "user-id";

    private String sessionId;
    private String token;
    private String userId;

}
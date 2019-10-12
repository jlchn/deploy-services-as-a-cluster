package com.jlchn.zuulservice.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * bring user info to the callee services
 */
public class UserContextCopyInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(
            HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {

        HttpHeaders headers = request.getHeaders();
        headers.add(UserContext.SESSION_ID, UserContextHolder.getContext().getSessionId());
        headers.add(UserContext.TOKEN, UserContextHolder.getContext().getToken());

        return execution.execute(request, body);
    }
}
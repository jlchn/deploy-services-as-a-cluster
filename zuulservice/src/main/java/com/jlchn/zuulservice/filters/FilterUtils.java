package com.jlchn.zuulservice.filters;

import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

@Component
public class FilterUtils {

    public static final String SESSION_ID = "session-id";
    public static final String AUTH_TOKEN = "token";
    public static final String USER_ID = "user-id";
    public static final String PRE_FILTER_TYPE = "pre";
    public static final String POST_FILTER_TYPE = "post";
    public static final String ROUTE_FILTER_TYPE = "route";

    public String getSessionId(){

        RequestContext ctx = RequestContext.getCurrentContext();

        if (ctx.getRequest().getHeader(SESSION_ID) != null) {
            return ctx.getRequest().getHeader(SESSION_ID);
        }

        return ctx.getZuulRequestHeaders().get(SESSION_ID);

    }

    public void setSessionId(String correlationId){
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.addZuulRequestHeader(SESSION_ID, correlationId);
    }


    public final String getUserId(){
        RequestContext ctx = RequestContext.getCurrentContext();
        if (ctx.getRequest().getHeader(USER_ID) !=null) {
            return ctx.getRequest().getHeader(USER_ID);
        }
        else{
            return  ctx.getZuulRequestHeaders().get(USER_ID);
        }
    }

    public void setUserId(String userId){
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.addZuulRequestHeader(USER_ID,  userId);
    }

    public final String getToken(){
        RequestContext ctx = RequestContext.getCurrentContext();
        return ctx.getRequest().getHeader(AUTH_TOKEN);
    }


}

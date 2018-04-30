package com.xmg.p2p.base.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by machao on 2017/8/31.
 */
public class MyRequestContextHolder {
    private static ThreadLocal<HttpServletRequest> local = new ThreadLocal<HttpServletRequest>();
    public static void setHttpServletRequest(HttpServletRequest request){
        local.set(request);
    }
    public static HttpServletRequest getHttpServletRequest(){
        return local.get();
    }
}

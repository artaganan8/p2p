package com.xmg.p2p.listener;

import com.xmg.p2p.base.util.MyRequestContextHolder;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by machao on 2017/8/31.
 */
public class MyRequestContextListener implements ServletRequestListener {
    @Override
    public void requestDestroyed(ServletRequestEvent sre) {

    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
        MyRequestContextHolder.setHttpServletRequest(request);
    }
}

package com.xmg.p2p.listener;

import com.xmg.p2p.base.service.ILogininfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Created by machao on 2017/9/1.
 */
@Component
public class InitAdminListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private ILogininfoService logininfoService;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logininfoService.initAdmin();
    }
}

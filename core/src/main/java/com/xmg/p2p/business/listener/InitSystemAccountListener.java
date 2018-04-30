package com.xmg.p2p.business.listener;

import com.xmg.p2p.business.service.ISystemAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Created by machao on 2017/9/10.
 */
@Component
public class InitSystemAccountListener implements ApplicationListener<ContextRefreshedEvent>{
    @Autowired
    private ISystemAccountService systemAccountService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        systemAccountService.initSystemAccount();
    }
}

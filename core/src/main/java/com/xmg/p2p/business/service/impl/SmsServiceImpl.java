package com.xmg.p2p.business.service.impl;

import com.xmg.p2p.base.domain.RealAuth;
import com.xmg.p2p.base.even.RealAuthSuccessEvent;
import com.xmg.p2p.business.service.ISmsService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

/**
 * Created by machao on 2017/9/23.
 */
@Service
public class SmsServiceImpl implements ISmsService,ApplicationListener<RealAuthSuccessEvent> {
    @Override
    public void sendSms(RealAuth realAuth) {
        System.out.println("用户" + realAuth.getApplier().getUsername() + "实名认证成功");
    }

    @Override
    public void onApplicationEvent(RealAuthSuccessEvent event) {
        this.sendSms(event.getRealAuth());
    }
}

package com.xmg.p2p.business.service.impl;

import com.xmg.p2p.base.domain.RealAuth;
import com.xmg.p2p.business.service.IEmailService;
import org.springframework.stereotype.Service;

/**
 * Created by machao on 2017/9/23.
 */
@Service
public class EmailServiceImpl implements IEmailService {
    @Override
    public void sendEmail(RealAuth realAuth) {
        System.out.println("用户" + realAuth.getApplier().getUsername() + "认证成功");
    }
}

package com.xmg.p2p.business.service;

import com.xmg.p2p.base.domain.RealAuth;

/**
 * Created by machao on 2017/9/23.
 */
public interface ISmsService {
    /**
     * 实名认证发送短信
     *
     * @param realAuth
     */
    void sendSms(RealAuth realAuth);
}

package com.xmg.p2p.business.service;

import com.xmg.p2p.base.domain.RealAuth;

/**
 * 发送邮件的服务
 *
 * Created by machao on 2017/9/23.
 */
public interface IEmailService {
    /**
     * 认证成功之后发送邮件
     *
     * @param realAuth
     */
    void sendEmail(RealAuth realAuth);
}

package com.xmg.p2p.base.service;

/**
 * Created by machao on 2017/9/2.
 */
public interface IVerifyCodeService {

    void sendVerifyCode(String phoneNumber);

    Boolean validate(String phoneNumber, String verityCode);
}

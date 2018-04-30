package com.xmg.p2p.base.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by machao on 2017/9/2.
 */
@Setter@Getter
public class VerifyCodeVo {
    private String phoneNumber;
    private String verifyCode;
    private Date sendTime;
}

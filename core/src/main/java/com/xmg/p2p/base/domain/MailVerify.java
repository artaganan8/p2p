package com.xmg.p2p.base.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by machao on 2017/9/3.
 */
@Setter@Getter
public class MailVerify extends BaseDomain {
    private String uuid;
    private String email;
    private Long userinfoId;
    private Date sendTime;
}

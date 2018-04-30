package com.xmg.p2p.base.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by machao on 2017/9/1.
 */
@Setter@Getter
public class IpLog extends BaseDomain {
    public static final int LOGIN_SUCCESS=0;
    public static final int LOGIN_FAILED = 1;
    public static final int USERTYPE_USER = 0;//普通用户
    public static final int USERTYPE_MANAGER = 1;//管理员
    private String ip;
    private String username;
    private Date loginTime;
    private int state;
    private int userType;
    public String getStateDisplay(){
        return state==IpLog.LOGIN_SUCCESS ? "登录成功":"登录失败";
    }
    public String getUserTypeDisplay(){
        return userType==IpLog.USERTYPE_USER ? "普通用户":"管理员";
    }
}

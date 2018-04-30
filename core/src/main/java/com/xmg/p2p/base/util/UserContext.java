package com.xmg.p2p.base.util;

import com.xmg.p2p.base.domain.Logininfo;
import com.xmg.p2p.base.vo.VerifyCodeVo;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by machao on 2017/8/30.
 */
public class UserContext {
    private UserContext(){}
    //定义常量
    public static final String USER_IN_SESSION= "logininfo";
    public static final String VERIFY_IN_SESSION= "verifyCode";
    //提供获取session的方法
    public static HttpSession getSession(){
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getSession();
    }
    //在工具类中提供当前用户get和set方法
    public static Logininfo getCurrent(){
       return (Logininfo) getSession().getAttribute(USER_IN_SESSION);
    }
    public static void setCurrent(Logininfo logininfo){
        getSession().setAttribute(USER_IN_SESSION,logininfo);
    }
    //提供验证码的get和set方法
    public static VerifyCodeVo getVerifyCode(){
        return (VerifyCodeVo) getSession().getAttribute(VERIFY_IN_SESSION);
    }
    public static void setVerifyCode(VerifyCodeVo verifyCode){
        getSession().setAttribute(VERIFY_IN_SESSION,verifyCode);
    }
}

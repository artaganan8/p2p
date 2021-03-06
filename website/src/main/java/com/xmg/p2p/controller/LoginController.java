package com.xmg.p2p.controller;

import com.xmg.p2p.base.domain.Logininfo;
import com.xmg.p2p.base.service.ILogininfoService;
import com.xmg.p2p.base.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by machao on 2017/8/30.
 */
@Controller
public class LoginController {
    @Autowired
    private ILogininfoService logininfoService;
    @RequestMapping("login")
    @ResponseBody
    public JSONResult login(String username, String password, HttpServletRequest request){
        JSONResult result = null;
        //普通用户登录
        Logininfo logininfo = logininfoService.login(username, password, request,Logininfo.USERTYPE_USER);
        if (logininfo==null){
            result = new JSONResult(false, "登录失败");
        }else{
            result = new JSONResult();
        }
        return result;
    }
}

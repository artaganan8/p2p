package com.xmg.p2p.controller;

import com.xmg.p2p.base.service.ILogininfoService;
import com.xmg.p2p.base.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by machao on 2017/9/14.
 */
@RestController
@RequestMapping(value = "api/userinfos/",produces = "application/json")
public class UserinfoRestController {
    @Autowired
    private ILogininfoService logininfoService;

    @RequestMapping(value = "register",method = RequestMethod.POST)
    public JSONResult register(String username,String password){
        JSONResult result = new JSONResult();
        try{
            this.logininfoService.register(username,password);
            result.setSuccess(true);
        }catch (RuntimeException e){
            result.setSuccess(false);
            result.setMsg("该用户已存在");
        }
        return result;
    }
}

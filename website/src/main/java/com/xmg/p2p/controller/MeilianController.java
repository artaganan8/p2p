package com.xmg.p2p.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by machao on 2017/9/2.
 */
@Controller
public class MeilianController {
    @RequestMapping("sendSms")
    @ResponseBody
    public String sendSms(String username,String password,String apikey,String phoneNumber,String content){
        System.out.println("username:"+username);
        System.out.println("password:"+password);
        System.out.println("apikey:"+apikey);
        System.out.println("phoneNumber:"+phoneNumber);
        System.out.println("content:"+content);
        return "msg:success";
    }
}

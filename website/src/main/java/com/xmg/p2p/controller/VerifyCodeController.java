package com.xmg.p2p.controller;

import com.xmg.p2p.base.service.IVerifyCodeService;
import com.xmg.p2p.base.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by machao on 2017/9/2.
 */
@Controller
public class VerifyCodeController {
    @Autowired
    private IVerifyCodeService verifyCodeService;
    @RequestMapping("sendVerifyCode")
    @ResponseBody
    public JSONResult sendVerifyCode(String phoneNumber){
        JSONResult result = null;
        try {
            verifyCodeService.sendVerifyCode(phoneNumber);
            result = new JSONResult("发送成功!");
        } catch (Exception e) {
            e.printStackTrace();
            result = new JSONResult(false, "发送失败:" + e.getMessage());
        }
        return result;
    }
}

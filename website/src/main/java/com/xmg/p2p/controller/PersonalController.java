package com.xmg.p2p.controller;

import com.xmg.p2p.base.domain.Account;
import com.xmg.p2p.base.domain.Userinfo;
import com.xmg.p2p.base.service.IAccountService;
import com.xmg.p2p.base.service.IMailService;
import com.xmg.p2p.base.service.IUserinfoService;
import com.xmg.p2p.base.util.JSONResult;
import com.xmg.p2p.business.service.IExpAccountService;
import com.xmg.p2p.util.RequireLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by machao on 2017/9/1.
 */
@Controller
public class PersonalController {
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IUserinfoService userinfoService;
    @Autowired
    private IMailService mailService;
    @Autowired
    private IExpAccountService expAccountService;

    @RequestMapping("personal")
    @RequireLogin
    public String personal(Model model) {

        Account account = accountService.getCurrent();
        Userinfo userinfo = userinfoService.getCurrent();
        model.addAttribute("account", account);
        model.addAttribute("userinfo", userinfo);
        model.addAttribute("expAccount",expAccountService.get(userinfo.getId()));
        return "personal";
    }

    @RequestMapping("bindPhone")
    @ResponseBody
    public JSONResult bindPhone(String phoneNumber, String verifyCode) {
        JSONResult result = null;
        try {
            userinfoService.bindPhone(phoneNumber, verifyCode);
            result = new JSONResult();
        } catch (Exception e) {
            e.printStackTrace();
            result = new JSONResult(false, e.getMessage());
        }
        return result;
    }

    @RequestMapping("sendEmail")
    @ResponseBody
    public JSONResult sendEmail(String email) {
        JSONResult result = null;
        try {
            mailService.bindEmail(email);
            result = new JSONResult();
        } catch (Exception e) {
            e.printStackTrace();
            result = new JSONResult(false, e.getMessage());
        }
        return result;
    }
    //绑定邮件
    @RequestMapping("bindEmail")
    public String bindEmail(String key,Model model) {
        try {
            userinfoService.bindEmail(key);
            model.addAttribute("success",true);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("success",false);
            model.addAttribute("msg",e.getMessage());
        }
        return "checkmail_result";
    }

}
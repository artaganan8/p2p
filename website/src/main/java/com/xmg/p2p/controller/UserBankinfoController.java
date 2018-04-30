package com.xmg.p2p.controller;

import com.xmg.p2p.base.domain.Userinfo;
import com.xmg.p2p.base.service.IUserinfoService;
import com.xmg.p2p.business.domain.UserBankinfo;
import com.xmg.p2p.business.service.IUserBankinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by machao on 2017/9/11.
 */
@Controller
public class UserBankinfoController {
    @Autowired
    private IUserinfoService userinfoService;
    @Autowired
    private IUserBankinfoService userBankinfoService;
    @RequestMapping("bankInfo")
    public String userBankinfoPage(Model model){
        Userinfo userinfo = userinfoService.getCurrent();
        if (userinfo.getisBindBank()){
            model.addAttribute("bankInfo",userBankinfoService.selectByUserinfoId(userinfo.getId()));
            return "bankinfo_result";
        }else{
            model.addAttribute("userinfo",userinfo);
            return "bankinfo";
        }
    }
    @RequestMapping("bankInfo_save")
    public String bankInfoSave(UserBankinfo userBankinfo){
        userBankinfoService.bankinfoSave(userBankinfo);
        return "redirect:/bankInfo.do";
    }
}

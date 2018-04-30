package com.xmg.p2p.controller;

import com.xmg.p2p.base.domain.Userinfo;
import com.xmg.p2p.base.service.IAccountService;
import com.xmg.p2p.base.service.IUserinfoService;
import com.xmg.p2p.base.util.JSONResult;
import com.xmg.p2p.business.service.IMoneyWithdrawService;
import com.xmg.p2p.business.service.IUserBankinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

/**
 * Created by machao on 2017/9/11.
 */
@Controller
public class MoneyWithdrawController  {
    @Autowired
    private IUserBankinfoService userBankinfoService;
    @Autowired
    private IUserinfoService userinfoService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IMoneyWithdrawService moneyWithdrawService;
    @RequestMapping("moneyWithdraw")
    public String moneyWithdrawPage(Model model){
        Userinfo current = userinfoService.getCurrent();
        model.addAttribute("haveProcessing",current.gethasMoneyWithdrawProcess());
        model.addAttribute("bankInfo",userBankinfoService.selectByUserinfoId(current.getId()));
        model.addAttribute("account",accountService.getCurrent());
        return "moneyWithdraw_apply";
    }
    @RequestMapping("moneyWithdraw_apply")
    public JSONResult moneyWithdrawApply(BigDecimal moneyAmount){
        JSONResult result = null;
        try {
            moneyWithdrawService.apply(moneyAmount);
            result = new JSONResult();
        } catch (Exception e) {
            e.printStackTrace();
            result = new JSONResult(false, e.getMessage());
        }
        return result;
    }
}

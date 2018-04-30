package com.xmg.p2p.controller;

import com.xmg.p2p.base.util.JSONResult;
import com.xmg.p2p.business.query.MoneyWithdrawQueryObject;
import com.xmg.p2p.business.service.IMoneyWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by machao on 2017/9/11.
 */
@Controller
public class MoneyWithdrawController {
    @Autowired
    private IMoneyWithdrawService moneyWithdrawService;
    @RequestMapping("moneyWithdraw")
    public String moneyWithdrawPage(@ModelAttribute("qo")MoneyWithdrawQueryObject qo, Model model){
        model.addAttribute("pageResult",moneyWithdrawService.queryPage(qo));
        return "moneywithdraw/list";
    }
    @RequestMapping("moneyWithdraw_audit")
    @ResponseBody
    public JSONResult moneyWithdrawAudit(Long id,int state,String remark){
        JSONResult result = null;
        try {
            moneyWithdrawService.moneyWithdrawAudit(id,state,remark);
            result = new JSONResult();
        } catch (Exception e) {
            e.printStackTrace();
            result = new JSONResult(false, e.getMessage());
        }
        return result;
    }
}

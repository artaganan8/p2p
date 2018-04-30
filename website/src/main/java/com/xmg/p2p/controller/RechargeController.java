package com.xmg.p2p.controller;

import com.xmg.p2p.base.util.JSONResult;
import com.xmg.p2p.business.domain.RechargeOffLine;
import com.xmg.p2p.business.service.IPlatFormBankInfoService;
import com.xmg.p2p.business.service.IRechargeOffLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by machao on 2017/9/8.
 */
@Controller
public class RechargeController {
    @Autowired
    private IPlatFormBankInfoService platFormBankInfoService;
    @Autowired
    private IRechargeOffLineService rechargeOffLineService;
    @RequestMapping("recharge")
    public String rechargePage(Model model){
        model.addAttribute("banks",platFormBankInfoService.selectAll());
        return "recharge";
    }
    @RequestMapping("recharge_save")
    @ResponseBody
    public JSONResult rechargeSave(RechargeOffLine rechargeOffLine){
        JSONResult result = null;
        try {
            rechargeOffLineService.rechargeSave(rechargeOffLine);
            result = new JSONResult();
        } catch (Exception e) {
            e.printStackTrace();
            result = new JSONResult(false, e.getMessage());
        }
        return result;
    }
}

package com.xmg.p2p.controller;

import com.xmg.p2p.base.util.JSONResult;
import com.xmg.p2p.business.query.RechargeOffLineQueryObject;
import com.xmg.p2p.business.service.IPlatFormBankInfoService;
import com.xmg.p2p.business.service.IRechargeOffLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by machao on 2017/9/9.
 */
@Controller
public class RechargeOfflineController {
    @Autowired
    private IRechargeOffLineService rechargeOffLineService;
    @Autowired
    private IPlatFormBankInfoService platFormBankInfoService;
    @RequestMapping("rechargeOffline")
    public String rechargeOffline(@ModelAttribute("qo") RechargeOffLineQueryObject qo, Model model){
        model.addAttribute("pageResult",rechargeOffLineService.queryPage(qo));
        model.addAttribute("banks",platFormBankInfoService.selectAll());
        return "rechargeoffline/list";
    }
    @RequestMapping("rechargeOffline_audit")
    @ResponseBody
    public JSONResult rechargeOfflineAudit(Long id ,String remark,int state){
        JSONResult result = null;
        try {
            rechargeOffLineService.audit(id,remark,state);
            result = new JSONResult();
        } catch (Exception e) {
            e.printStackTrace();
            result = new JSONResult(false, e.getMessage());
        }
        return result;
    }
}

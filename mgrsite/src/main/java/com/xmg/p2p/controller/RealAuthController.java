package com.xmg.p2p.controller;

import com.xmg.p2p.base.query.RealAuthQueryObject;
import com.xmg.p2p.base.service.IRealAuthService;
import com.xmg.p2p.base.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by machao on 2017/9/5.
 * 后台realAuthCOntroller
 */
@Controller
public class RealAuthController {
    @Autowired
    private IRealAuthService realAuthService;
    @RequestMapping("realAuth")
    public String realAuth(@ModelAttribute("qo")RealAuthQueryObject qo,Model model){
        model.addAttribute("pageResult",realAuthService.queryPage(qo));
        return "realAuth/list";
    }
    @RequestMapping("realAuth_audit")
    @ResponseBody
    public JSONResult realAuthAudit(Long id,int state,String remark){
        JSONResult result = null;
        try {
            realAuthService.realAuthAudit(id,state,remark);
            result = new JSONResult();
        } catch (Exception e) {
            e.printStackTrace();
            result = new JSONResult(false,e.getMessage());
        }
        return result;
    }
}

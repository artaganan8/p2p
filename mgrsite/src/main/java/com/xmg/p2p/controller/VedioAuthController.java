package com.xmg.p2p.controller;

import com.xmg.p2p.base.domain.Logininfo;
import com.xmg.p2p.base.query.VedioAuthQueryObject;
import com.xmg.p2p.base.service.ILogininfoService;
import com.xmg.p2p.base.service.IVedioAuthService;
import com.xmg.p2p.base.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by machao on 2017/9/6.
 */
@Controller
public class VedioAuthController {
    @Autowired
    private IVedioAuthService vedioAuthService;
    @Autowired
    private ILogininfoService logininfoService;
    @RequestMapping("vedioAuth")
    public String vedioAuth(@ModelAttribute("qo")VedioAuthQueryObject qo , Model model){
        try {
            model.addAttribute("pageResult",vedioAuthService.queryPage(qo));
        }catch (Exception e){
            e.printStackTrace();
        }
        return "vedioAuth/list";
    }
    @RequestMapping("vedioAuth_audit")
    @ResponseBody
    public JSONResult vedioAuthAudit(Long loginInfoValue,int state,String remark){
        JSONResult result = null;
        try {
            vedioAuthService.vedioAuthAudit(loginInfoValue,state,remark);
            result = new JSONResult();
        } catch (Exception e) {
            e.printStackTrace();
            result = new JSONResult(false, e.getMessage());
        }
        return result;
    }
    @RequestMapping("vedioAuth_autocomplete")
    @ResponseBody
    public List<Logininfo> vedioAuthAutocomplete(String keyword){
        List<Logininfo> result = null;
        result = logininfoService.queryListByKeyword(keyword);
        return  result;
    }

}

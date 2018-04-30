package com.xmg.p2p.controller;

import com.xmg.p2p.business.domain.PlatFormBankInfo;
import com.xmg.p2p.business.query.PlatFormBankInfoQueryObject;
import com.xmg.p2p.business.service.IPlatFormBankInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by machao on 2017/9/8.
 */
@Controller
public class PlatFormBankInfoCOntroller {
    @Autowired
    private IPlatFormBankInfoService platFormBankInfoService;
    @RequestMapping("companyBank_list")
    public String platFormInfoPage(PlatFormBankInfoQueryObject qo,Model model){
        model.addAttribute("pageResult",platFormBankInfoService.queryPage(qo));
        return "platformbankinfo/list";
    }
    @RequestMapping("companyBank_update")
    public String saveOrUpdate(PlatFormBankInfo platFormBankInfo){
        platFormBankInfoService.saveOrUpdate(platFormBankInfo);
        return "redirect:/companyBank_list.do";
    }
}

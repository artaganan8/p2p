package com.xmg.p2p.controller;

import com.xmg.p2p.base.domain.Userinfo;
import com.xmg.p2p.base.service.ISystemDictionaryItemService;
import com.xmg.p2p.base.service.IUserinfoService;
import com.xmg.p2p.base.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by machao on 2017/9/4.
 */
@Controller
public class BasicInfoController {
    @Autowired
    private IUserinfoService userinfoService;

    @Autowired
    private ISystemDictionaryItemService systemDictionaryItemService;
    @RequestMapping("basicInfo")
    public String baseInfo(Model model){
        model.addAttribute("userinfo",userinfoService.getCurrent());
        model.addAttribute("educationBackgrounds",systemDictionaryItemService.listBySn("educationBackground"));
        model.addAttribute("incomeGrades",systemDictionaryItemService.listBySn("incomeGrade"));
        model.addAttribute("marriages",systemDictionaryItemService.listBySn("marriage"));
        model.addAttribute("kidCounts",systemDictionaryItemService.listBySn("kidCount"));
        model.addAttribute("houseConditions",systemDictionaryItemService.listBySn("houseCondition"));
        return "userInfo";
    }
    @RequestMapping("basicInfo_save")
    @ResponseBody
    public JSONResult basicInfoSave(Userinfo userinfo){
        JSONResult result = null;
        try {
            userinfoService.basicInfoSave(userinfo);
            result = new JSONResult("注册成功!");
        } catch (Exception e) {
            e.printStackTrace();
            result = new JSONResult(false, "注册失败:" + e.getMessage());
        }
        return result;
    }
}

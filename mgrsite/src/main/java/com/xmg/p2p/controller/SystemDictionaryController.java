package com.xmg.p2p.controller;

import com.xmg.p2p.base.domain.SystemDictionary;
import com.xmg.p2p.base.query.SystemDictionaryQueryObject;
import com.xmg.p2p.base.service.ISystemDictionaryService;
import com.xmg.p2p.base.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by machao on 2017/9/4.
 */
@Controller
public class SystemDictionaryController {
    @Autowired
    private ISystemDictionaryService systemDictionaryService;
    @RequestMapping("systemDictionary_list")
    public String systemDictionaryList(@ModelAttribute("qo")SystemDictionaryQueryObject qo , Model model){
        model.addAttribute("pageResult",systemDictionaryService.queryPage(qo));
        return "systemdic/systemDictionary_list";
    }
    @RequestMapping("systemDictionary_update")
    @ResponseBody
    public JSONResult saveOrUpdate(SystemDictionary systemDictionary){
        JSONResult result = null;
        try {
            systemDictionaryService.saveOrUpdate(systemDictionary);
            result = new JSONResult("注册成功!");
        } catch (Exception e) {
            e.printStackTrace();
            result = new JSONResult(false, "注册失败:" + e.getMessage());
        }
        return result;
    }
}

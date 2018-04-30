package com.xmg.p2p.controller;

import com.xmg.p2p.base.query.UserFileQueryObject;
import com.xmg.p2p.base.service.IUserFileService;
import com.xmg.p2p.base.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by machao on 2017/9/7.
 */
@Controller
public class UserFileAuthController {
    @Autowired
    private IUserFileService userFileService;
    @RequestMapping("userFileAuth")
    public String userFileAuth(@ModelAttribute("qo")UserFileQueryObject qo, Model model){
        model.addAttribute("pageResult",userFileService.queryPage(qo));
        return "userFileAuth/list";
    }
    @RequestMapping("userFile_audit")
    @ResponseBody
    public JSONResult userFileAudit(Long id,int state,int score,String remark){
        JSONResult result = null;
        try {
            userFileService.userAuthAudit(id,state,score,remark);
            result = new JSONResult();
        } catch (Exception e) {
            e.printStackTrace();
            result = new JSONResult(false, e.getMessage());
        }
        return result;
    }
}

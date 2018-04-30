package com.xmg.p2p.controller;

import com.xmg.p2p.base.query.IpLogQueryObject;
import com.xmg.p2p.base.service.IIpLogService;
import com.xmg.p2p.base.util.UserContext;
import com.xmg.p2p.util.RequireLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by machao on 2017/9/1.
 */

@Controller
public class IpLogController {
    @Autowired
    private IIpLogService ipLogService;
    @RequestMapping("ipLog")
    @RequireLogin
    public String ipLogList(@ModelAttribute("qo")IpLogQueryObject qo, Model model){
        qo.setUsername(UserContext.getCurrent().getUsername());
        model.addAttribute("pageResult",this.ipLogService.query(qo));
        return "iplog_list";
    }
}

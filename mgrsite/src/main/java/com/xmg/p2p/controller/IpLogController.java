package com.xmg.p2p.controller;

import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.base.query.IpLogQueryObject;
import com.xmg.p2p.base.service.IIpLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by machao on 2017/9/2.
 */
@Controller
public class IpLogController {
    @Autowired
    private IIpLogService ipLogService;
    @RequestMapping("ipLog")

    public String list(@ModelAttribute("qo")IpLogQueryObject qo, Model model){
        PageResult pageResult = ipLogService.query(qo);
        model.addAttribute("pageResult",pageResult);
        return "ipLog/list";
    }
}

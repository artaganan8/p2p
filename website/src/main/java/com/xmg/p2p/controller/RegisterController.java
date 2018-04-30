package com.xmg.p2p.controller;

import com.sun.java.swing.plaf.motif.MotifTreeCellRenderer;
import com.sun.xml.internal.txw2.output.TXWResult;
import com.xmg.p2p.base.service.ILogininfoService;
import com.xmg.p2p.base.util.JSONResult;
import freemarker.cache.TemplateLookupResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.security.util.Password;


/**
 * Created by machao on 2017/8/29.
 */
@Controller
public class RegisterController {
    @Autowired
    private ILogininfoService logininfoService;
    @RequestMapping("register")
    @ResponseBody
    public JSONResult register(String username,String password) {
        JSONResult result = null;
        try {
            logininfoService.register(username, password);
            result = new JSONResult("注册成功!");
        } catch (Exception e) {
            e.printStackTrace();
            result = new JSONResult(false, "注册失败:" + e.getMessage());
        }
        return result;
    }
    @RequestMapping("checkUsernameExist")
    @ResponseBody
    public boolean checkUsernameExist(String username){
        return logininfoService.checkUsernameExist(username);
    }
}

package com.xmg.p2p.controller;

import com.xmg.p2p.base.domain.RealAuth;
import com.xmg.p2p.base.domain.Userinfo;
import com.xmg.p2p.base.service.IRealAuthService;
import com.xmg.p2p.base.service.IUserinfoService;
import com.xmg.p2p.base.util.JSONResult;
import com.xmg.p2p.util.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;

/**
 * Created by machao on 2017/9/4.
 */
@Controller
public class RealAuthController {
    @Autowired
    private IUserinfoService userinfoService;
    @Autowired
    private IRealAuthService realAuthService;
    @Autowired
    private ServletContext servletContext;
    @RequestMapping("realAuth")
    public String realAuth(Model model){
        Userinfo userinfo = userinfoService.getCurrent();
        if (userinfo.getIsRealAuth()){
            //如果通过,从userinfo中取到realAuthId,查询对于的实名认证对象
            //跳转到realAuth_result.ftl auditing=false realAuth对象
            model.addAttribute("auditing",false);
            model.addAttribute("realAuth",realAuthService.get(userinfo.getRealAuthId()));
            return "realAuth_result";
        }else{
            //没有通过
            if (userinfo.getRealAuthId()==null){
                //如果为null,需要取到审核申请页面的realAuth.ftl
                return "realAuth";
            }else{
                //查看userinfo中的realAuthId是否为null
                //如果不为null 目前处于待审核状态,realAuth_result.ftl auditing=true
                model.addAttribute("auditing",true);
                return "realAuth_result";
            }
        }
    }
    @RequestMapping("realAuth_save")
    @ResponseBody
    public JSONResult realAuthSave(RealAuth realAuth){
        JSONResult result = null;
        try {
            realAuthService.realAuthSave(realAuth);
            result = new JSONResult();
        } catch (Exception e) {
            e.printStackTrace();
            result = new JSONResult(false, e.getMessage());
        }
        return result;
    }
    @RequestMapping("uploadImage")
    @ResponseBody
    public String uploadImage(MultipartFile img){
        String dir = "/upload/";
        String fileName = UploadUtil.upload(img, servletContext.getRealPath(dir));
      return dir+ fileName;
    }
}

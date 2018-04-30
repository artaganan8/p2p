package com.xmg.p2p.restful;

import com.xmg.p2p.base.domain.Logininfo;
import com.xmg.p2p.base.service.ILogininfoService;
import com.xmg.p2p.base.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by machao on 2017/9/14.
 */
@RestController
@RequestMapping(value = "api/tokens" ,produces = "application/json")
public class TokenController {
    @Autowired
    private ILogininfoService logininfoService;
    @Autowired
    private ITokenService tokenService;
    @RequestMapping(method = RequestMethod.POST)
    public JSONResult createToken(String username, String password, HttpServletRequest request){
        JSONResult result = new JSONResult();
        Logininfo current = this.logininfoService.login(username, password, request, Logininfo.USERTYPE_USER);
        if (current!=null){
            String token = this.tokenService.createToken(current);
            result.setSuccess(true);
        }else {
            result.setSuccess(false);
            result.setMsg("账号或者密码错误");
        }
        return result;
    }

}

package com.xmg.p2p.base.service.impl;

import com.xmg.p2p.base.domain.Account;
import com.xmg.p2p.base.domain.IpLog;
import com.xmg.p2p.base.domain.Logininfo;
import com.xmg.p2p.base.domain.Userinfo;
import com.xmg.p2p.base.mapper.LogininfoMapper;
import com.xmg.p2p.base.service.IAccountService;
import com.xmg.p2p.base.service.IIpLogService;
import com.xmg.p2p.base.service.ILogininfoService;
import com.xmg.p2p.base.service.IUserinfoService;
import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.base.util.MD5;
import com.xmg.p2p.base.util.UserContext;
import com.xmg.p2p.business.domain.ExpAccount;
import com.xmg.p2p.business.service.IExpAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by machao on 2017/8/29.
 */
@Service
public class LogininfoServiceImpl implements ILogininfoService {
    @Autowired
    private LogininfoMapper logininfoMapper;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IUserinfoService userinfoService;
    @Autowired
    private IIpLogService ipLogService;
    @Autowired
    private IExpAccountService expAccountService;
    @Override
    public Logininfo register(String username, String password) {
        int count = logininfoMapper.selectCountbyUsername(username);
        if(count>0){
            throw new RuntimeException("该账号已存在");
        }
        Logininfo logininfo = new Logininfo();
        logininfo.setUsername(username);
        logininfo.setPassword(MD5.encode(password));
        logininfo.setState(Logininfo.STATE_NORMAL);
        logininfoMapper.insert(logininfo);
        /*初始化账单account对象和用户信息userinfo对象*/
        Account account = new Account();
        account.setId(logininfo.getId());
        accountService.save(account);
        Userinfo userinfo = new Userinfo();
        userinfo.setId(logininfo.getId());
        userinfoService.save(userinfo);

        //创建体验金账户
        ExpAccount expAccount = new ExpAccount();
        expAccount.setId(userinfo.getId());
        this.expAccountService.save(expAccount);
        //发放体验金
        this.expAccountService.grantExpMoney(expAccount.getId(),new IExpAccountService.LastTime(1,IExpAccountService.LastTimeUnit.MONTH),
                BidConst.REGISTER_GRANT_EXPMONEY,BidConst.EXPMONEY_TYPE_REGISTER);
        return logininfo;
    }

    @Override
    public boolean checkUsernameExist(String username) {
        return logininfoMapper.selectCountbyUsername(username)<=0;
    }

    @Override
    public Logininfo login(String username, String password, HttpServletRequest request,int userType) {
        //查询数据库中的记录,看是否有该用户,密码使用MD5加密
        Logininfo logininfo = logininfoMapper.login(username,MD5.encode(password),userType);
        IpLog ipLog = new IpLog();
        ipLog.setLoginTime(new Date());
        ipLog.setUsername(username);
        ipLog.setIp(request.getRemoteAddr());
        ipLog.setUserType(userType);
        //如果没有抛出异常
        if (logininfo==null){
            ipLog.setState(IpLog.LOGIN_FAILED);
        }else{
            ipLog.setState(IpLog.LOGIN_SUCCESS);
            //如果有将登陆信息保存到session中
            UserContext.setCurrent(logininfo);
        }
        int count = ipLogService.save(ipLog);
        return logininfo;
    }

    @Override
    public void initAdmin() {
        //根据用户类型查询是否已经存在管理员
        int count= logininfoMapper.selectByUserType(Logininfo.USERTYPE_MANAGER);
        //如果没有管理员,那么我们创建一个
        if(count<=0){
            Logininfo logininfo = new Logininfo();
            logininfo.setState(Logininfo.STATE_NORMAL);
            logininfo.setUserType(Logininfo.USERTYPE_MANAGER);
            logininfo.setUsername(BidConst.DEFAULT_ADMIN_NAME);
            logininfo.setPassword(MD5.encode(BidConst.DEFAULT_ADMIN_PASSWORD));
            logininfoMapper.insert(logininfo);
        }

    }

    @Override
    public List<Logininfo> queryListByKeyword(String keyword) {
        return logininfoMapper.queryListByKeyword(keyword);
    }
}

package com.xmg.p2p.base.service;

import com.xmg.p2p.base.domain.Logininfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by machao on 2017/8/29.
 */

public interface ILogininfoService {
    public Logininfo register(String username,String password);

    boolean checkUsernameExist(String username);

    Logininfo login(String username, String password, HttpServletRequest request,int userType);

    void initAdmin();

    List<Logininfo> queryListByKeyword(String keyword);
}

package com.xmg.p2p.base.service;

import com.xmg.p2p.base.domain.Userinfo;

public interface IUserinfoService {
    int save(Userinfo userinfo);
    int update(Userinfo userinfo);
    Userinfo get(Long id);

    void bindPhone(String phoneNumber, String verityCode);

    Userinfo getCurrent();

    void bindEmail(String key);

    void basicInfoSave(Userinfo userinfo);
}

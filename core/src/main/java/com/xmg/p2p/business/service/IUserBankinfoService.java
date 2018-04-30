package com.xmg.p2p.business.service;

import com.xmg.p2p.business.domain.UserBankinfo;

public interface IUserBankinfoService {
    int save(UserBankinfo userBankinfo);
    UserBankinfo get(Long id);

    UserBankinfo selectByUserinfoId(Long id);

    void bankinfoSave(UserBankinfo userBankinfo);


}

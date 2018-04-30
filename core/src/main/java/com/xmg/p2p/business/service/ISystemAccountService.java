package com.xmg.p2p.business.service;

import com.xmg.p2p.business.domain.SystemAccount;

public interface ISystemAccountService {
    int save(SystemAccount systemAccount);
    int update(SystemAccount systemAccount);
    SystemAccount getCurrent();

    void initSystemAccount();


}

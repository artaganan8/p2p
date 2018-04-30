package com.xmg.p2p.base.service;

import com.xmg.p2p.base.domain.Account;

public interface IAccountService {
    int save(Account account);
    int update(Account account);
    Account get(Long id);

    Account getCurrent();
}

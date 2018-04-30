package com.xmg.p2p.base.service.impl;

import com.xmg.p2p.base.domain.Account;
import com.xmg.p2p.base.mapper.AccountMapper;
import com.xmg.p2p.base.service.IAccountService;
import com.xmg.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements IAccountService {
	@Autowired
	private AccountMapper accountMapper;

	@Override
	public int save(Account account) {
		return accountMapper.insert(account);
	}

	@Override
	public int update(Account account) {
        int count = accountMapper.updateByPrimaryKey(account);
        if(count<=0){
            throw new RuntimeException("乐观锁异常,accountVersion"+account.getVersion());
        }
        return count;
	}

	@Override
	public Account get(Long id) {
		return accountMapper.selectByPrimaryKey(id);
	}

	@Override
	public Account getCurrent() {
		Long id = UserContext.getCurrent().getId();
		return this.get(id);
	}
}

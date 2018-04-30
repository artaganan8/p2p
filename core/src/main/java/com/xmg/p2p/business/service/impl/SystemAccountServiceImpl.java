package com.xmg.p2p.business.service.impl;

import com.xmg.p2p.business.domain.SystemAccount;
import com.xmg.p2p.business.mapper.SystemAccountMapper;
import com.xmg.p2p.business.service.ISystemAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemAccountServiceImpl implements ISystemAccountService {
	@Autowired
	private SystemAccountMapper systemAccountMapper;

	@Override
	public int save(SystemAccount systemAccount) {
		return systemAccountMapper.insert(systemAccount);
	}

	@Override
	public int update(SystemAccount systemAccount) {
        int count = systemAccountMapper.updateByPrimaryKey(systemAccount);
        if (count==0){
            throw new RuntimeException("更新系统用户信息时乐观锁异常!");
        }
        return count;
	}

	@Override
	public SystemAccount getCurrent() {
		return systemAccountMapper.selectCurrent();
	}

    @Override
    public void initSystemAccount() {
        SystemAccount current = this.getCurrent();
        if (current==null){
            current = new SystemAccount();
            this.save(current);
        }
    }
}

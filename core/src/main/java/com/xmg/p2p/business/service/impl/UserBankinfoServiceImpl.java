package com.xmg.p2p.business.service.impl;

import com.xmg.p2p.base.domain.Account;
import com.xmg.p2p.base.domain.Userinfo;
import com.xmg.p2p.base.service.IAccountService;
import com.xmg.p2p.base.service.IUserinfoService;
import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.base.util.BitStatesUtils;
import com.xmg.p2p.base.util.UserContext;
import com.xmg.p2p.business.domain.MoneyWithdraw;
import com.xmg.p2p.business.domain.UserBankinfo;
import com.xmg.p2p.business.mapper.UserBankinfoMapper;
import com.xmg.p2p.business.service.IUserBankinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class UserBankinfoServiceImpl implements IUserBankinfoService {
	@Autowired
	private UserBankinfoMapper userBankinfoMapper;
    @Autowired
    private IUserinfoService userinfoService;
    @Autowired
    private IAccountService accountService;
	@Override
	public int save(UserBankinfo userBankinfo) {
		return userBankinfoMapper.insert(userBankinfo);
	}

	@Override
	public UserBankinfo get(Long id) {
		return userBankinfoMapper.selectByPrimaryKey(id);
	}

	@Override
	public UserBankinfo selectByUserinfoId(Long id) {
		return userBankinfoMapper.selectByUserinfoId(id);
	}

    @Override
    public void bankinfoSave(UserBankinfo userBankinfo) {
        Userinfo current = userinfoService.getCurrent();
        if (current.getIsRealAuth()&&!current.getisBindBank()){
            UserBankinfo bankinfo = new UserBankinfo();
            bankinfo.setBankForkName(userBankinfo.getBankForkName());
            bankinfo.setBankName(userBankinfo.getBankName());
            bankinfo.setAccountNumber(userBankinfo.getAccountNumber());
            bankinfo.setAccountName(current.getRealName());
            bankinfo.setUserinfoId(current.getId());
            this.save(bankinfo);
            current.addState(BitStatesUtils.OP_BIND_BANKINFO);
            userinfoService.update(current);
        }
    }


}

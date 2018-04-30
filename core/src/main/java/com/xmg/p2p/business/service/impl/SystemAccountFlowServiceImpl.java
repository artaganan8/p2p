package com.xmg.p2p.business.service.impl;

import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.business.domain.SystemAccount;
import com.xmg.p2p.business.domain.SystemAccountFlow;
import com.xmg.p2p.business.mapper.SystemAccountFlowMapper;
import com.xmg.p2p.business.service.ISystemAccountFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class SystemAccountFlowServiceImpl implements ISystemAccountFlowService {
    @Autowired
    protected SystemAccountFlowMapper systemAccountFlowMapper;

	@Override
	public int save(SystemAccountFlow systemAccountFlow) {
		return systemAccountFlowMapper.insert(systemAccountFlow);
	}

	@Override
	public void creatReceviceManagermentChargeFlow(SystemAccount systemAccount, BigDecimal managermentCharge) {
        createFlow(systemAccount,managermentCharge, BidConst.SYSTEM_ACCOUNT_ACTIONTYPE_MANAGE_CHARGE,"收取借款手续费,金额"+managermentCharge+"元");
	}

    @Override
    public void createMoneyWithdrawFlow(SystemAccount systemAccount, BigDecimal amount) {
        createFlow(systemAccount,amount, BidConst.SYSTEM_ACCOUNT_ACTIONTYPE_WITHDRAW_MANAGE_CHARGE,"收取提现手续费,金额"+amount+"元");
    }

    @Override
    public void createReceiveInterestManagerChargeFlow(SystemAccount systemAccount, BigDecimal amount) {
        createFlow(systemAccount,amount, BidConst.SYSTEM_ACCOUNT_ACTIONTYPE_INTREST_MANAGE_CHARGE,"系统账户收取利息管理费用,金额"+amount+"元");
    }

    private void createFlow(SystemAccount account,BigDecimal amount,int actionType,String remark){
        SystemAccountFlow flow = new SystemAccountFlow();
        flow.setUsableAmount(account.getUsableAmount());
        flow.setFreezedAmount(account.getFreezedAmount());
        flow.setActionTime(new Date());
        flow.setActionType(actionType);
        flow.setAmount(amount);
        flow.setRemark(remark);
        this.save(flow);
    }
}

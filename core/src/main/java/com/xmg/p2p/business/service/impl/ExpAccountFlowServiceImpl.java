package com.xmg.p2p.business.service.impl;

import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.business.domain.ExpAccount;
import com.xmg.p2p.business.domain.ExpAccountFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xmg.p2p.business.mapper.ExpAccountFlowMapper;
import com.xmg.p2p.business.service.IExpAccountFlowService;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class ExpAccountFlowServiceImpl implements IExpAccountFlowService {
	@Autowired
	private ExpAccountFlowMapper expAccountFlowMapper;

	@Override
	public int save(ExpAccountFlow expAccountFlow) {
		return expAccountFlowMapper.insert(expAccountFlow);
	}



    /**
	 * 记录用户注册体验金发放流水
	 *
	 * @param expAccount
	 * @param expmoney
	 */
    @Override
    public void createExpAccountFlow(ExpAccount expAccount, BigDecimal expmoney) {
        createFlow(expAccount,expmoney, BidConst.EXPMONEY_TYPE_REGISTER,"注册账号,发放体验金,金额为"+expmoney+"元");
    }

    /**
     * 创建流水的方法
     * @param expAccount
     * @param expmoney
     * @param actionType
     * @param remark
     */
	private void createFlow(ExpAccount expAccount, BigDecimal expmoney,int actionType,String remark) {
        ExpAccountFlow expAccountFlow = new ExpAccountFlow();
        expAccountFlow.setAmount(expmoney);
        expAccountFlow.setActionTime(new Date());
        expAccountFlow.setActionType(actionType);
        expAccountFlow.setExpAccountId(expAccount.getId());
        expAccountFlow.setNote(remark);
        expAccountFlow.setFreezedAmount(expAccount.getFreezedAmount());
        expAccountFlow.setUsableAmount(expAccount.getUsableAmount());
        this.save(expAccountFlow);
    }
}

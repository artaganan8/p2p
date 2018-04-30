package com.xmg.p2p.business.service.impl;

import com.xmg.p2p.base.domain.Account;
import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.business.domain.AccountFlow;
import com.xmg.p2p.business.mapper.AccountFlowMapper;
import com.xmg.p2p.business.service.IAccountFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class AccountFlowServiceImpl implements IAccountFlowService {
	@Autowired
	private AccountFlowMapper accountFlowMapper;

	@Override
	public int save(AccountFlow accountFlow) {
		return accountFlowMapper.insert(accountFlow);
	}

	@Override
	public AccountFlow get(Long id) {
		return accountFlowMapper.selectByPrimaryKey(id);
	}

	/**
	 * 生成冻结金额的流水
	 * @param account
	 * @param amount
	 */
	@Override
	public void createBidFlow(Account account, BigDecimal amount) {
		createFlow(account,amount,BidConst.ACCOUNT_ACTIONTYPE_BID_FREEZED,"投标冻结金额"+amount+"元!");
	}

	/**
	 * 生成满标拒绝的流水
	 * @param account
	 * @param amount
	 */
	@Override
	public void createBidFaileFlow(Account account, BigDecimal amount) {
		createFlow(account,amount,BidConst.ACCOUNT_ACTIONTYPE_BID_UNFREEZED,"投标失败,取消冻结"+amount+"成功!");
	}

	/**
	 * 生成借款流水
	 * @param account
	 * @param amount
	 */
	@Override
	public void creatBorrowSuccessFlow(Account account, BigDecimal amount) {
		createFlow(account,amount,BidConst.ACCOUNT_ACTIONTYPE_BIDREQUEST_SUCCESSFUL,"借款成功,可用余额增加"+amount+"元!");
	}

	@Override
	public void createPayManagermentChargeFlow(Account account, BigDecimal amount) {
		createFlow(account,amount,BidConst.ACCOUNT_ACTIONTYPE_CHARGE,"支付平台管理费"+amount+"元!");
	}

	/**
	 * 生成投标成功的流水
	 * @param biduserAccount
	 * @param availableAmount
	 */
	@Override
	public void createBidSuccessFlow(Account account, BigDecimal amount) {
		createFlow(account,amount,BidConst.ACCOUNT_ACTIONTYPE_BID_SUCCESSFUL,"投标成功,冻结金额减少"+amount+"元!");
	}

	@Override
	public void createMoneyWithdrawFlow(Account account, BigDecimal amount) {
		createFlow(account,amount,BidConst.ACCOUNT_ACTIONTYPE_WITHDRAW_FREEZED,"申请提现,冻结金额增加"+amount+"元!");
	}
//
	@Override
	public void withDrawChargeFee(Account account, BigDecimal amount) {
		createFlow(account,amount,BidConst.ACCOUNT_ACTIONTYPE_INTEREST_SHARE,"支付平台手续费流水,冻结金额减少"+amount+"元!");
	}

	@Override
	public void withDrawFailed(Account account, BigDecimal amount) {
		createFlow(account,amount,BidConst.ACCOUNT_ACTIONTYPE_WITHDRAW_UNFREEZED,"申请提现失败,冻结金额减少,可用余额增加"+amount+"元!");
	}

	@Override
	public void withDrawSuccess(Account account, BigDecimal amount) {
		createFlow(account,amount,BidConst.ACCOUNT_ACTIONTYPE_WITHDRAW,"申请提现成功,冻结金额减少"+amount+"元!");
	}

	@Override
	public void createReturnMoneyFlow(Account account, BigDecimal amount) {
		createFlow(account,amount,BidConst.ACCOUNT_ACTIONTYPE_RETURN_MONEY,"还款成功,可用金额减少"+amount+"元!");
	}

	@Override
	public void createPayInterestManagerFlow(Account account, BigDecimal amount) {
		createFlow(account,amount,BidConst.ACCOUNT_ACTIONTYPE_INTEREST_SHARE,"支付利息管理费用,可用金额减少"+amount+"元!");
	}

	/**
	 * 生成充值的流水
	 * @param account
	 * @param amount
	 */
	public void createRechargeFlow(Account account, BigDecimal amount) {
		createFlow(account,amount,BidConst.ACCOUNT_ACTIONTYPE_RECHARGE_OFFLINE,"线下充值"+amount+"成功!");
	}
	private void createFlow(Account account, BigDecimal amount,int actionType,String remark) {
		AccountFlow flow = new AccountFlow();
		flow.setAmount(amount);
		flow.setAccountId(account.getId());
		flow.setTradeTime(new Date());
		flow.setActionType(actionType);
		flow.setUsableAmount(account.getUsableAmount());
		flow.setFreezedAmount(account.getFreezedAmount());
		flow.setRemark(remark);
		this.save(flow);
	}
}

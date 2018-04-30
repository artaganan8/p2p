package com.xmg.p2p.business.service.impl;

import com.xmg.p2p.base.domain.Account;
import com.xmg.p2p.base.domain.Userinfo;
import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.base.service.IAccountService;
import com.xmg.p2p.base.service.IUserinfoService;
import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.base.util.BitStatesUtils;
import com.xmg.p2p.base.util.UserContext;
import com.xmg.p2p.business.domain.MoneyWithdraw;
import com.xmg.p2p.business.domain.SystemAccount;
import com.xmg.p2p.business.domain.UserBankinfo;
import com.xmg.p2p.business.mapper.MoneyWithdrawMapper;
import com.xmg.p2p.business.query.MoneyWithdrawQueryObject;
import com.xmg.p2p.business.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class MoneyWithdrawServiceImpl implements IMoneyWithdrawService {
	@Autowired
	private MoneyWithdrawMapper moneyWithdrawMapper;
	@Autowired
	private IUserinfoService userinfoService;
	@Autowired
	private IAccountService accountService;
	@Autowired
	private IUserBankinfoService userBankinfoService;
	@Autowired
	private IAccountFlowService accountFlowService;
	@Autowired
	private ISystemAccountService systemAccountService;
	@Autowired
	private ISystemAccountFlowService systemAccountFlowService;
	@Override
	public int save(MoneyWithdraw moneyWithdraw) {
		return moneyWithdrawMapper.insert(moneyWithdraw);
	}

	@Override
	public int update(MoneyWithdraw moneyWithdraw) {
		return moneyWithdrawMapper.updateByPrimaryKey(moneyWithdraw);
	}

	@Override
	public MoneyWithdraw get(Long id) {
		return moneyWithdrawMapper.selectByPrimaryKey(id);
	}

	@Override
	public void apply(BigDecimal moneyAmount) {

			Userinfo userinfo = userinfoService.getCurrent();
			Account account = accountService.getCurrent();
			if (userinfo.getHasBidRequestProcess()&&//用户正在体现的流程
					userinfo.getisBindBank()&&//判断是否绑定银行卡
					moneyAmount.compareTo(BidConst.MIN_WITHDRAW_AMOUNT)>=0&&//z最小系统细线金额<=体现金额
					moneyAmount.compareTo(account.getUsableAmount())<=0//体现金额小于等于用户的可用余额
					){
			}
			// 得到用户绑定的银行卡信息
			UserBankinfo ub = this.userBankinfoService.selectByUserinfoId(userinfo
					.getId());

			// 创建一个提现申请对象,设置相关属性;
			MoneyWithdraw m = new MoneyWithdraw();
			m.setAccountName(ub.getAccountName());
			m.setAccountNumber(ub.getAccountNumber());
			m.setAmount(moneyAmount);
			m.setApplier(UserContext.getCurrent());
			m.setApplyTime(new Date());
			m.setBankForkName(ub.getBankForkName());
			m.setBankName(ub.getBankName());
			m.setCharge(BidConst.MONEY_WITHDRAW_CHARGEFEE);
			m.setState(MoneyWithdraw.STATE_NORMAL);
			this.save(m);
		//申请人的可用金额减少,冻结金额增加,生成提现冻结流水
		account.setUsableAmount(account.getUsableAmount().subtract(moneyAmount));
		account.setFreezedAmount(account.getFreezedAmount().add(moneyAmount));
		accountService.update(account);
		accountFlowService.createMoneyWithdrawFlow(account,moneyAmount);
		//申请人的userinfo添加提现的流程状态码
		userinfo.addState(BitStatesUtils.OP_HAS_MONEYWITHDRAW_PROCESS);
		//更新申请人的userinfo对象
		userinfoService.update(userinfo);

	}

	@Override
	public PageResult queryPage(MoneyWithdrawQueryObject qo) {
		Long count = moneyWithdrawMapper.queryPageCount(qo);
		if (count==0){
			return PageResult.empty(qo.getPageSize());
		}
		List<MoneyWithdraw> list = moneyWithdrawMapper.queryPageData(qo);
		return new PageResult(list,count.intValue(),qo.getCurrentPage(),qo.getPageSize());
	}

	@Override
	public void moneyWithdrawAudit(Long id, int state, String remark) {
		// 得到提现申请单
		MoneyWithdraw m = this.moneyWithdrawMapper.selectByPrimaryKey(id);
		// 1,判断:提现单状态
		if (m != null && m.getState() == MoneyWithdraw.STATE_NORMAL) {
			// 2,设置相关参数
			m.setAuditor(UserContext.getCurrent());
			m.setAudiTime(new Date());
			m.setRemark(remark);
			m.setState(state);

			Account account = this.accountService.get(m.getApplier().getId());
			if (state == MoneyWithdraw.STATE_PASS) {
				// 3,如果审核通过
				// 3,冻结金额减少(减少提现金额);增加提现成功流水;
				BigDecimal realWithdrawFee = m.getAmount().subtract(
						m.getCharge());
				account.setFreezedAmount(account.getFreezedAmount().subtract(
						realWithdrawFee));
				this.accountFlowService.withDrawSuccess(account,realWithdrawFee);

				// 1,冻结金额减少(减少手续费),增加提现支付手续费流水;
				account.setFreezedAmount(account.getFreezedAmount().subtract(
						m.getCharge()));
				this.accountFlowService.withDrawChargeFee(account,m.getCharge());
				// 2,系统账户增加可用余额,增加收取提现手续费流水;
                SystemAccount systemAccount = this.systemAccountService.getCurrent();
                systemAccount.setUsableAmount(systemAccount.getUsableAmount().add(m.getCharge()));
                this.systemAccountService.update(systemAccount);
                systemAccountFlowService.createMoneyWithdrawFlow(systemAccount,m.getCharge());
			} else {
				// 4,如果审核拒绝
				// 1,取消冻结金额,可用余额增加,增加去掉冻结流水
				account.setFreezedAmount(account.getFreezedAmount().subtract(
						m.getAmount()));
				account.setUsableAmount(account.getUsableAmount().add(
						m.getAmount()));
				this.accountFlowService.withDrawFailed(account,m.getAmount());
			}
			this.accountService.update(account);
			this.moneyWithdrawMapper.updateByPrimaryKey(m);
			// 5,取消用户状态码
			Userinfo userinfo = this.userinfoService
					.get(m.getApplier().getId());
			userinfo.removeState(BitStatesUtils.OP_HAS_MONEYWITHDRAW_PROCESS);
			this.userinfoService.update(userinfo);
		}
	}
}

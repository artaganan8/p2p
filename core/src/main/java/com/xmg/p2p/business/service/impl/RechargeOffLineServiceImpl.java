package com.xmg.p2p.business.service.impl;

import com.xmg.p2p.base.domain.Account;
import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.base.service.IAccountService;
import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.base.util.UserContext;
import com.xmg.p2p.business.domain.AccountFlow;
import com.xmg.p2p.business.domain.RechargeOffLine;
import com.xmg.p2p.business.mapper.RechargeOffLineMapper;
import com.xmg.p2p.business.query.RechargeOffLineQueryObject;
import com.xmg.p2p.business.service.IAccountFlowService;
import com.xmg.p2p.business.service.IRechargeOffLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RechargeOffLineServiceImpl implements IRechargeOffLineService {
	@Autowired
	private RechargeOffLineMapper rechargeOffLineMapper;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IAccountFlowService accountFlowService;

	@Override
	public int save(RechargeOffLine rechargeOffLine) {
		return rechargeOffLineMapper.insert(rechargeOffLine);
	}

	@Override
	public int update(RechargeOffLine rechargeOffLine) {
		return rechargeOffLineMapper.updateByPrimaryKey(rechargeOffLine);
	}
    public RechargeOffLine get(Long id){
        return rechargeOffLineMapper.selectByPrimaryKey(id);
    }

	@Override
	public void rechargeSave(RechargeOffLine rechargeOffLine) {
		if (rechargeOffLine!=null){
            RechargeOffLine rech = new RechargeOffLine();
            rech.setApplier(UserContext.getCurrent());
            rech.setApplyTime(new Date());
            rech.setAmount(rechargeOffLine.getAmount());
            rech.setBankInfo(rechargeOffLine.getBankInfo());
            rech.setTradeCode(rechargeOffLine.getTradeCode());
            rech.setTradeTime(rechargeOffLine.getTradeTime());
            rech.setState(RechargeOffLine.STATE_NORMAL);
            rech.setNote(rechargeOffLine.getNote());
            this.save(rech);
        }
	}

    @Override
    public PageResult queryPage(RechargeOffLineQueryObject qo) {
        Long count = rechargeOffLineMapper.queryPageCount(qo);
        if (count==0){
        	return PageResult.empty(qo.getPageSize());
        }
        List<RechargeOffLine> list = rechargeOffLineMapper.queryPageData(qo);
        return new PageResult(list,count.intValue(),qo.getCurrentPage(),qo.getPageSize());
    }
    //线下充值后台审核
    @Override
    public void audit(Long id, String remark, int state) {
        //根据id获取需要审核的对象
        RechargeOffLine rechargeOffLine = this.get(id);
        //判断如果获取的线下充值对象不为null,我们进行审核逻辑操作
        if(rechargeOffLine!=null){
            //申请人的id
            Account account = accountService.get(rechargeOffLine.getApplier().getId());
            //创建线下充值流水对象
            AccountFlow accountFlow = new AccountFlow();
            //设置相关参数
            accountFlow.setAmount(rechargeOffLine.getAmount());
            accountFlow.setTradeTime(rechargeOffLine.getTradeTime());
            accountFlow.setRemark(remark);
            //关联账户的account_id是哪个
            accountFlow.setAccountId(account.getId());
            //资金变化类型
            accountFlow.setActionType(BidConst.ACCOUNT_ACTIONTYPE_RECHARGE_OFFLINE);
            accountFlow.setFreezedAmount(account.getFreezedAmount());
            if (state== RechargeOffLine.STATE_PASS) {
                account.setUsableAmount(account.getUsableAmount().add(accountFlow.getAmount()));
                accountFlow.setUsableAmount(account.getUsableAmount());
                rechargeOffLine.setState(RechargeOffLine.STATE_PASS);
                accountService.update(account);
            }else {
                accountFlow.setUsableAmount(account.getUsableAmount());
                rechargeOffLine.setState(RechargeOffLine.STATE_REJECT);
            }
            this.update(rechargeOffLine);
            accountFlowService.save(accountFlow);
        }
    }
}

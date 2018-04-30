package com.xmg.p2p.business.service.impl;

import com.xmg.p2p.business.domain.ExpAccount;
import com.xmg.p2p.business.domain.ExpAccountGrantRecord;
import com.xmg.p2p.business.mapper.ExpAccountMapper;
import com.xmg.p2p.business.service.IExpAccountFlowService;
import com.xmg.p2p.business.service.IExpAccountGrantRecordService;
import com.xmg.p2p.business.service.IExpAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class ExpAccountServiceImpl implements IExpAccountService {
	@Autowired
	private ExpAccountMapper expAccountMapper;
    @Autowired
    private IExpAccountGrantRecordService expAccountGrantRecordService;
    @Autowired
    private IExpAccountFlowService expAccountFlowService;
	@Override
	public int save(ExpAccount expAccount) {
		return expAccountMapper.insert(expAccount);
	}

	@Override
	public int update(ExpAccount expAccount) {
        int count = expAccountMapper.updateByPrimaryKey(expAccount);
        if (count == 0){
            throw new RuntimeException("乐观锁异常"+expAccount.getId());
        }
        return count;
	}

	@Override
	public ExpAccount get(Long id) {
		return expAccountMapper.selectByPrimaryKey(id);
	}

    /**
     * 发放体验金
     * @param id  发放体验金用户的id
     * @param lastTime  体验金的有效期限
     * @param expmoney  体验金的金额
     * @param expmoneyType  发放体验金的原因
     */
    @Override
    public void grantExpMoney(Long id, LastTime lastTime, BigDecimal expmoney, int expmoneyType) {
        //根据id获取体验金账户
        ExpAccount expAccount = this.get(id);
        expAccount.setUsableAmount(expAccount.getUsableAmount().add(expmoney));
        this.update(expAccount);

        //创建一个发放回收记录对象
        ExpAccountGrantRecord expAccountGrantRecord = new ExpAccountGrantRecord();
        expAccountGrantRecord.setAmount(expmoney);
        expAccountGrantRecord.setGrantDate(new Date());
        expAccountGrantRecord.setGrantType(expmoneyType);
        expAccountGrantRecord.setGrantUserId(id);
        expAccountGrantRecord.setNote("发放奖励金金额为"+expmoney);
        expAccountGrantRecord.setReturnDate(lastTime.getReturnDate(new Date()));
        expAccountGrantRecord.setState(ExpAccountGrantRecord.STATE_NORMAL);
        expAccountGrantRecordService.save(expAccountGrantRecord);

        //增加一条体验金流水
        expAccountFlowService.createExpAccountFlow(expAccount,expmoney);

    }
}

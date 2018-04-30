package com.xmg.p2p.business.service.impl;

import com.xmg.p2p.base.domain.Account;
import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.base.service.IAccountService;
import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.business.domain.BidRequest;
import com.xmg.p2p.business.domain.PaymentSchedule;
import com.xmg.p2p.business.domain.PaymentScheduleDetail;
import com.xmg.p2p.business.domain.SystemAccount;
import com.xmg.p2p.business.mapper.PaymentScheduleMapper;
import com.xmg.p2p.business.query.PaymentScheduleQueryObject;
import com.xmg.p2p.business.service.*;
import com.xmg.p2p.business.util.CalculatetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class PaymentScheduleServiceImpl implements IPaymentScheduleService {
    @Autowired
    private PaymentScheduleMapper paymentScheduleMapper;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IPaymentScheduleDetailService paymentScheduleDetailService;
    @Autowired
    private IAccountFlowService accountFlowService;
    @Autowired
    private ISystemAccountService systemAccountService;
    @Autowired
    private ISystemAccountFlowService systemAccountFlowService;
    @Autowired
    private IBidRequestService bidRequestService;
    @Autowired
    private IBidService bidService;



    @Override
    public int save(PaymentSchedule paymentSchedule) {
        return paymentScheduleMapper.insert(paymentSchedule);
    }

    @Override
    public int update(PaymentSchedule paymentSchedule) {
        return paymentScheduleMapper.updateByPrimaryKey(paymentSchedule);
    }

    @Override
    public PaymentSchedule get(Long id) {
        return paymentScheduleMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageResult queryPage(PaymentScheduleQueryObject qo) {
        Long count = paymentScheduleMapper.queryPageCount(qo);
        if (count == 0) {
            return PageResult.empty(qo.getPageSize());
        }
        List<PaymentSchedule> list = paymentScheduleMapper.queryPageData(qo);
        return new PageResult(list, count.intValue(), qo.getCurrentPage(), qo.getPageSize());
    }

    @Override
    public void returnMoney(Long id) {
        //根据id获取对应的还款对象
        PaymentSchedule ps = this.get(id);
        Account account = accountService.getCurrent();
        if (ps != null && ps.getState() == BidConst.PAYMENT_STATE_NORMAL &&//
                account.getUsableAmount().compareTo(ps.getTotalAmount()) >= 0 &&//可用解大于还款金额
                account.getId().equals(ps.getBorrowUser().getId())//当前登录人员和改期的还款人员是同一个人
                ) {
            //对于还款对象和还款明细
            //设置还款时间
            ps.setPayDate(new Date());
            paymentScheduleDetailService.updatePayDate(ps.getId(), new Date());
            //设置状态
            ps.setState(BidConst.PAYMENT_STATE_DONE);
            this.update(ps);
            //对于借款人
            //账户的可用金额减少,待还本息减少,剩余信用额度增加
            account.setUsableAmount(account.getUsableAmount().subtract(ps.getTotalAmount()));
            account.setUnReturnAmount(account.getUnReturnAmount().subtract(ps.getTotalAmount()));
            account.setRemainBorrowLimit(account.getRemainBorrowLimit().add(ps.getPrincipal()));
            accountService.update(account);
            //增加账户的流水
            accountFlowService.createReturnMoneyFlow(account, ps.getTotalAmount());
            //对于投资人
            List<PaymentScheduleDetail> details = ps.getDetails();
            //遍历所有的还款明细,取出所有的投资人
            HashMap<Long, Account> accountMap = new HashMap<>();
            Long bidUserId = null;
            Account bidUserAccount = null;
            for (PaymentScheduleDetail psd : details) {
                bidUserId = psd.getInvestorId();
                bidUserAccount = accountMap.get(bidUserId);
                if (bidUserAccount == null) {
                    bidUserAccount = accountService.get(bidUserId);
                    accountMap.put(bidUserId, bidUserAccount);
                }
                //投资人账户可用余额增加,代收本金,本息减少,生成流水
                bidUserAccount.setUsableAmount(bidUserAccount.getUsableAmount().add(psd.getTotalAmount()));
                bidUserAccount.setUnReceivePrincipal(bidUserAccount.getUnReceivePrincipal().subtract(psd.getPrincipal()));
                bidUserAccount.setUnReceiveInterest(bidUserAccount.getUnReceiveInterest().subtract(psd.getInterest()));
                accountFlowService.createReturnMoneyFlow(bidUserAccount, psd.getTotalAmount());
                //支付利息管理费,生成流水
                BigDecimal interestManagerCharge = CalculatetUtil.calInterestManagerCharge(psd.getInterest());
                bidUserAccount.setUsableAmount(bidUserAccount.getUsableAmount().subtract(interestManagerCharge));
                accountFlowService.createPayInterestManagerFlow(bidUserAccount, interestManagerCharge);
                //系统账户收取利息管理费用,生成流水
                SystemAccount systemAccount = systemAccountService.getCurrent();
                systemAccount.setUsableAmount(systemAccount.getUsableAmount().add(interestManagerCharge));
                systemAccountService.update(systemAccount);
                systemAccountFlowService.createReceiveInterestManagerChargeFlow(systemAccount, interestManagerCharge);
            }
            //对账户进行统一的修改
            for (Account ac : accountMap.values()) {
                accountService.update(ac);
            }
            //其他
            //如果是最后一期还款
            //根据借款Id获取所有的还款对象
            List<PaymentSchedule> paymentSchedules = this.paymentScheduleMapper.queryBiBidRequestId(ps.getBidRequestId());
            //遍历还款对象,判断每期的状态是否等于已还
            boolean isAllReturn = true;
            for (PaymentSchedule paymentSchedule : paymentSchedules) {
                if (paymentSchedule.getState() != BidConst.PAYMENT_STATE_DONE) {
                    isAllReturn = false;
                }
            }
            if (isAllReturn) {
                //设置借款对象的状态----已完成
                BidRequest bidRequest = bidRequestService.get(ps.getBidRequestId());
                bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK);
                bidRequestService.update(bidRequest);
                //批量修改投标对象的状态,-----已完成
                bidService.updateState(bidRequest.getId(),BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK);
            }
        }
    }
}

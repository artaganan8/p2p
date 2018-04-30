package com.xmg.p2p.business.service.impl;

import com.xmg.p2p.base.domain.Account;
import com.xmg.p2p.base.domain.Userinfo;
import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.base.service.IAccountService;
import com.xmg.p2p.base.service.IUserinfoService;
import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.base.util.BitStatesUtils;
import com.xmg.p2p.base.util.UserContext;
import com.xmg.p2p.business.domain.*;
import com.xmg.p2p.business.event.BidrequestAudit2SuccessEvent;
import com.xmg.p2p.business.mapper.BidRequestMapper;
import com.xmg.p2p.business.query.BidRequestQueryObject;
import com.xmg.p2p.business.service.*;
import com.xmg.p2p.business.util.CalculatetUtil;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class BidRequestServiceImpl implements IBidRequestService {
    @Autowired
    private BidRequestMapper bidRequestMapper;
    @Autowired
    private IUserinfoService userinfoService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IBidRequestAuditHistoryService bidRequestAuditHistoryService;
    @Autowired
    private IBidService bidService;
    @Autowired
    private IAccountFlowService accountFlowServie;
    @Autowired
    private ISystemAccountService systemAccountService;
    @Autowired
    private ISystemAccountFlowService systemAccountFlowService;
    @Autowired
    private IPaymentScheduleService paymentScheduleService;
    @Autowired
    private IPaymentScheduleDetailService paymentScheduleDetailService;
    @Autowired
    private ApplicationContext ctx;
    @Override
    public int save(BidRequest bidRequest) {
        return bidRequestMapper.insert(bidRequest);
    }

    @Override
    public int update(BidRequest bidRequest) {
        int count = bidRequestMapper.updateByPrimaryKey(bidRequest);
        if (count == 0) {
            throw new RuntimeException("乐观锁异常");
        }
        return count;
    }

    @Override
    public BidRequest get(Long id) {
        return bidRequestMapper.selectByPrimaryKey(id);
    }

    /*
    * 该方法判断用户是否具有借款的条件
    * */
    @Override
    public boolean canApplyBidRequest(Userinfo userinfo) {
        if (userinfo.getIsBasicInfo() &&//基本信息认证
                userinfo.getIsRealAuth() &&//实名认证
                userinfo.getIsVedioAuth() &&//视频认证
                userinfo.getScore() >= BidConst.BASE_BORROW_SCORE//风控材料分数大于30
                ) {
            return true;
        }
        return false;
    }

    @Override
    public void apply(BidRequest bidRequest) {
        //1,先判断当前的用户是够有借款条件
        //2,系统最小的借款金额<=借款金额<=用户剩余的信用额度
        //3,系统最小利率<=借款利率<=系统最大的利率
        //4,没有正在借款流程
        Userinfo userinfo = userinfoService.getCurrent();
        Account account = accountService.getCurrent();
        if (this.canApplyBidRequest(userinfo) &&//用户使用有借款条件
                !userinfo.getHasBidRequestProcess() &&//用户没有正在审核的借款
                bidRequest.getBidRequestAmount().compareTo(BidConst.SMALLEST_BIDREQUEST_AMOUNT) >= 0 &&//
                bidRequest.getBidRequestAmount().compareTo(account.getRemainBorrowLimit()) <= 0 &&//
                bidRequest.getCurrentRate().compareTo(BidConst.SMALLEST_CURRENT_RATE) >= 0 &&//
                bidRequest.getCurrentRate().compareTo(BidConst.MAX_CURRENT_RATE) <= 0 &&//
                bidRequest.getMinBidAmount().compareTo(BidConst.SMALLEST_BID_AMOUNT) >= 0
                ) {
            //创建借款的对象bidRequest
            BidRequest br = new BidRequest();
            br.setApplyTime(new Date());
            br.setBidRequestAmount(bidRequest.getBidRequestAmount());
            br.setBidRequestState(BidConst.BIDREQUEST_STATE_PUBLISH_PENDING);
            br.setBidRequestType(BidConst.BIDREQUEST_TYPE_NORMAL);
            br.setCreateUser(UserContext.getCurrent());
            br.setDescription(bidRequest.getDescription());
            br.setDisableDays(bidRequest.getDisableDays());
            br.setMinBidAmount(bidRequest.getMinBidAmount());
            br.setCurrentRate(bidRequest.getCurrentRate());
            br.setMonthes2Return(bidRequest.getMonthes2Return());
            br.setReturnType(BidConst.RETURN_TYPE_MONTH_INTEREST_PRINCIPAL);
            br.setTitle(bidRequest.getTitle());
            br.setTotalRewardAmount(CalculatetUtil.calTotalInterest(br.getReturnType(),
                    br.getBidRequestAmount(), br.getCurrentRate(), br.getMonthes2Return()));
            this.save(br);
            //设置相关属性
            userinfo.addState(BitStatesUtils.OP_HAS_BIDREQUEST_PROCESS);
            //给当前用户添加状态,正在申请的流程状态
            userinfoService.update(userinfo);
        }
    }
    @Override
    public void applyExpBid(BidRequest bidRequest) {
        Userinfo userinfo = userinfoService.getCurrent();
        Account account = accountService.getCurrent();
        if (bidRequest!=null&&bidRequest.getBidRequestState()==BidConst.BIDREQUEST_STATE_BIDDING
                &&bidRequest.getBidRequestAmount().compareTo(bidRequest.getMinBidAmount())>=0&&
                bidRequest.getBidRequestAmount().compareTo(BidConst.SMALLEST_BIDREQUEST_AMOUNT) >= 0 &&//
                bidRequest.getBidRequestAmount().compareTo(account.getRemainBorrowLimit()) <= 0
                ){

        }
    }

    @Override
    public void applyExp(BidRequest bidRequest) {

        if (bidRequest.getBidRequestAmount().compareTo(BidConst.SMALLEST_BIDREQUEST_AMOUNT) >= 0 &&
                bidRequest.getMinBidAmount().compareTo(BidConst.SMALLEST_BID_AMOUNT) >= 0
                ) {
            //创建体验标对象bidRequest
            BidRequest br = new BidRequest();
            br.setApplyTime(new Date());
            br.setBidRequestAmount(bidRequest.getBidRequestAmount());
            br.setBidRequestState(BidConst.BIDREQUEST_STATE_PUBLISH_PENDING);
            br.setBidRequestType(BidConst.BIDREQUEST_TYPE_EXP);
            br.setCreateUser(UserContext.getCurrent());
            br.setDescription(bidRequest.getDescription());
            br.setDisableDays(bidRequest.getDisableDays());
            br.setMinBidAmount(bidRequest.getMinBidAmount());
            br.setCurrentRate(bidRequest.getCurrentRate());
            br.setMonthes2Return(bidRequest.getMonthes2Return());
            br.setReturnType(BidConst.RETURN_TYPE_MONTH_INTEREST_PRINCIPAL);
            br.setTitle(bidRequest.getTitle());
            br.setTotalRewardAmount(CalculatetUtil.calTotalInterest(br.getReturnType(), br.getBidRequestAmount(), br.getCurrentRate(), br.getMonthes2Return()));
            this.save(br);
        }
    }



    @Override
    public PageResult queryPage(BidRequestQueryObject qo) {
        Long count = bidRequestMapper.queryPageCount(qo);
        if (count == 0) {
            return PageResult.empty(qo.getPageSize());
        }
        List<BidRequest> list = bidRequestMapper.queryPageData(qo);
        return new PageResult(list, count.intValue(), qo.getCurrentPage(), qo.getPageSize());
    }

    @Override
    public void publishAudit(Long id, String remark, int state) {
        //查询借款对象
        BidRequest bidRequest = this.get(id);
        //判断该借款对象是否存在且处于待审核状态
        if (bidRequest != null && bidRequest.getBidRequestState() == BidConst.BIDREQUEST_STATE_PUBLISH_PENDING) {
            //创建审核历史记录对象,设置相应的参数
            BidRequestAuditHistory brah = new BidRequestAuditHistory();
            brah.setRemark(remark);
            brah.setApplier(bidRequest.getCreateUser());
            brah.setApplyTime(bidRequest.getApplyTime());
            brah.setAuditor(UserContext.getCurrent());
            brah.setAudiTime(new Date());
            brah.setBidRequestId(bidRequest.getId());
            brah.setAuditType(BidRequestAuditHistory.PUBLISH_AUDIT);
            //判断当前的审核状态时候通过
            if (state == BidRequestAuditHistory.STATE_PASS) {
                //审核通过,设置审核状态,设置对象的发布,截至时间,风控,设置借款状态
                brah.setState(BidRequestAuditHistory.STATE_PASS);
                //设置借款对象
                bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_BIDDING);
                bidRequest.setPublishTime(new Date());
                bidRequest.setDisableDate(DateUtils.addDays(bidRequest.getPublishTime(), bidRequest.getDisableDays()));
                bidRequest.setNote(remark);
            } else {
                //审核拒绝,设置状态,
                brah.setState(BidRequestAuditHistory.STATE_REJECT);
                //设置借款对象的状态
                bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_PUBLISH_REFUSE);
                if (bidRequest.getBidRequestType() == BidConst.BIDREQUEST_TYPE_NORMAL) {
                    //移除借款人的状态码
                    Userinfo applier = userinfoService.get(bidRequest.getCreateUser().getId());
                    applier.removeState(BitStatesUtils.OP_HAS_BIDREQUEST_PROCESS);
                    userinfoService.update(applier);
                }
            }
            bidRequestAuditHistoryService.save(brah);
            this.update(bidRequest);
        }

    }

    @Override
    public List<BidRequest> listIndexData(int expBidRequestType) {
        BidRequestQueryObject qo = new BidRequestQueryObject();
        qo.setStates(new int[]{BidConst.BIDREQUEST_STATE_BIDDING, BidConst.BIDREQUEST_STATE_PAYING_BACK, BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK});
        qo.setBidRequestType(expBidRequestType);
        return bidRequestMapper.listIndexData(qo);
    }

    /**
     * 投标操作
     *
     * @param bidRequestId
     * @param amount
     */
    @Override
    public void borrowBid(Long bidRequestId, BigDecimal amount) {
        //根据id查询借款对象
        BidRequest bidRequest = this.get(bidRequestId);
        Account account = accountService.getCurrent();
        if (bidRequest != null &&
                amount.compareTo(BidConst.SMALLEST_BID_AMOUNT) >= 0 &&//系统最小投标<=投标金额
                amount.compareTo(account.getUsableAmount().min(bidRequest.getRemainAmount())) <= 0 &&//投标金额<=min(账户余额和标剩余可投的金额)
                !account.getId().equals(bidRequest.getCreateUser().getId())//投标人不是借款人
                ) {
            //借款对象的操作
            //bidCount+1,currentSUM加上投标金额
            bidRequest.setBidCount(bidRequest.getBidCount() + 1);
            bidRequest.setCurrentSum(bidRequest.getCurrentSum().add(amount));
            //投标对象
            //创建bid对象,设置相关的参数,保存入库
            Bid bid = new Bid();
            bid.setActualRate(bidRequest.getCurrentRate());
            bid.setAvailableAmount(amount);
            bid.setBidRequestId(bidRequest.getId());
            bid.setBidRequestTitle(bidRequest.getTitle());
            bid.setBidTime(new Date());
            bid.setBidUser(UserContext.getCurrent());
            bid.setBidRequestState(BidConst.BIDREQUEST_STATE_BIDDING);
            bidService.save(bid);
            //投资人,可用金额减少,冻结金额增加
            account.setUsableAmount(account.getUsableAmount().subtract(amount));
            account.setFreezedAmount(account.getFreezedAmount().add(amount));
            accountService.update(account);
            //生成投标冻结流水信息
            accountFlowServie.createBidFlow(account, amount);
            //判断是否投满
            if (bidRequest.getCurrentSum().compareTo(bidRequest.getBidRequestAmount()) == 0) {
                //标进入满标一审状态
                bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1);
                //投标对象中的状态需要统一的修改
                bidService.updateState(bidRequest.getId(), BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1);
            }
            this.update(bidRequest);
        }
    }

    //满标一审
    @Override
    public void audit1(Long id, int state, String remark) {
        //根据id获取借款对象
        BidRequest bidRequest = this.get(id);
        if (bidRequest != null && bidRequest.getBidRequestState() == BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1) {
            //创建审核历史对象,设置相关属性,并保存入库
            createBidRequestAuditHistory(bidRequest, remark, state, BidRequestAuditHistory.PULL_AUDIT1);
            if (state == BidRequestAuditHistory.STATE_PASS) {
                //更新借款对象中的BidRequestState----满标二审
                bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2);
                //批量更新修改投标对象的BidRequestState----满标二审
                bidService.updateState(bidRequest.getId(), BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2);
            } else {
                //满标审核拒绝
                auditReject(bidRequest);
            }
            this.update(bidRequest);
        }
    }

    /**
     * 满标二审
     *
     * @param id
     * @param state
     * @param remark
     */
    @Override
    public void audit2(Long id, int state, String remark) {
        BidRequest bidRequest = this.get(id);
        if (bidRequest != null && bidRequest.getBidRequestState() == BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2) {
            //创建审核历史对象,设置相关属性,并保存入库
            createBidRequestAuditHistory(bidRequest, remark, state, BidRequestAuditHistory.PULL_AUDIT2);
            if (state == BidRequestAuditHistory.STATE_PASS) {
                //对借款和投标对象的状态的修改
                //更新借款状态,还款中
                bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_PAYING_BACK);
                //批量更新投标对象的状态,还款中
                bidService.updateState(bidRequest.getId(), BidConst.BIDREQUEST_STATE_PAYING_BACK);
                //借款人?
                //账户的可用余额增加,生成借款成功的流水
                Account createUserAccount = accountService.get(bidRequest.getCreateUser().getId());
                createUserAccount.setUsableAmount(createUserAccount.getUsableAmount().add(bidRequest.getBidRequestAmount()));
                accountFlowServie.creatBorrowSuccessFlow(createUserAccount, bidRequest.getBidRequestAmount());
                createUserAccount.setUnReturnAmount(createUserAccount.getUnReturnAmount().add(bidRequest.getBidRequestAmount().add(bidRequest.getTotalRewardAmount())));
                //账户的剩余授信额度减少
                createUserAccount.setRemainBorrowLimit(createUserAccount.getRemainBorrowLimit().subtract(bidRequest.getBidRequestAmount()));
                //支付借款手续费,可用金额减少,生成支付手续费的流水
                BigDecimal managermentCharge = CalculatetUtil.calAccountManagementCharge(bidRequest.getBidRequestAmount());
                createUserAccount.setUsableAmount(createUserAccount.getUsableAmount().subtract(managermentCharge));
                accountService.update(createUserAccount);
                accountFlowServie.createPayManagermentChargeFlow(createUserAccount, managermentCharge);
                //系统账号收取借款手续费()
                SystemAccount systemAccount = systemAccountService.getCurrent();
                systemAccount.setUsableAmount(systemAccount.getUsableAmount().add(managermentCharge));
                systemAccountService.update(systemAccount);
                //生成系统账号的收取借款手续费的流水
                systemAccountFlowService.creatReceviceManagermentChargeFlow(systemAccount, managermentCharge);
                //移除借款人的userinfo的正在借款的状态码
                Userinfo userinfo = userinfoService.get(bidRequest.getCreateUser().getId());
                userinfo.removeState(BitStatesUtils.OP_HAS_BIDREQUEST_PROCESS);
                //更新借款人信息userinfo
                userinfoService.update(userinfo);
                //投资人
                HashMap<Long, Account> accountMap = new HashMap<>();
                Long bidUserId = null;
                Account biduserAccount = null;
                //遍历所有的投标记录
                for (Bid bid : bidRequest.getBids()) {
                    bidUserId = bid.getBidUser().getId();
                    biduserAccount = accountMap.get(bidUserId);
                    if (biduserAccount == null) {
                        biduserAccount = accountService.get(bidUserId);
                        accountMap.put(bidUserId, biduserAccount);
                    }
                    //获取到每一个投标用户Account,冻结金额减少,生成投标成功的流水
                    biduserAccount.setFreezedAmount(biduserAccount.getFreezedAmount().subtract(bid.getAvailableAmount()));
                    accountFlowServie.createBidSuccessFlow(biduserAccount, bid.getAvailableAmount());
                }//生成还款对象(根据还款的月数来定)
                List<PaymentSchedule> paymentSchedules = createPaymentSchedule(bidRequest);
                Account buAccount = null;
                for (PaymentSchedule ps : paymentSchedules) {
                    for (PaymentScheduleDetail psd : ps.getDetails()) {
                        //获取
                        buAccount = accountMap.get(psd.getInvestorId());
                        buAccount.setUnReceivePrincipal(buAccount.getUnReceivePrincipal().add(psd.getPrincipal()));
                        buAccount.setUnReceiveInterest(buAccount.getUnReceiveInterest().add(psd.getInterest()));
                    }
                }
                //投标acoount账户的更新
                for (Account account : accountMap.values()) {
                    accountService.update(account);
                }
            }


            //在满标二审成功时抛出事件
            ctx.publishEvent(new BidrequestAudit2SuccessEvent(this,bidRequest));

        } else {
            auditReject(bidRequest);
        }
        this.update(bidRequest);
    }


    private void createBidRequestAuditHistory(BidRequest bidRequest, String remark, int state, int auditType) {
        //创建审核历史对象,设置相关属性,并保存入库
        BidRequestAuditHistory brah = new BidRequestAuditHistory();
        brah.setApplier(bidRequest.getCreateUser());
        brah.setApplyTime(new Date());
        brah.setAuditor(UserContext.getCurrent());
        brah.setAudiTime(new Date());
        brah.setAuditType(auditType);
        brah.setBidRequestId(bidRequest.getId());
        brah.setRemark(remark);
        if (state == BidRequestAuditHistory.STATE_PASS) {
            //如果审核通过,
            brah.setState(BidRequestAuditHistory.STATE_PASS);
        } else {
            //审核失败
            brah.setState(BidRequestAuditHistory.STATE_REJECT);
        }
        bidRequestAuditHistoryService.save(brah);
    }

    /**
     * 满标审核拒绝
     *
     * @param bidRequest
     */
    private void auditReject(BidRequest bidRequest) {
        //更新借款对象中的BidRequestState----满标拒绝
        bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_REJECTED);
        //批量更新修改投标对象的BidRequestState----满标拒绝
        bidService.updateState(bidRequest.getId(), BidConst.BIDREQUEST_STATE_REJECTED);
        //遍历投标对象
        Account account = null;
        Long accountId = null;
        //使用map优化代码,减少查询和更新的操作
        HashMap<Long, Account> accountMap = new HashMap<>();
        for (Bid bid : bidRequest.getBids()) {
            //获取到投资人的account账号可用金额增加,冻结金额减少
            accountId = bid.getBidUser().getId();
            account = accountMap.get(accountId);
            if (account == null) {
                account = accountService.get(accountId);
                accountMap.put(accountId, account);
            }
            account.setUsableAmount(account.getUsableAmount().add(bid.getAvailableAmount()));
            account.setFreezedAmount(account.getFreezedAmount().subtract(bid.getAvailableAmount()));
            //生成投标失败的流水
            accountFlowServie.createBidFaileFlow(account, bid.getAvailableAmount());
        }
        //对对象执行统一的更新操作
        for (Account bidaccount : accountMap.values()) {
            accountService.update(bidaccount);
        }
        //找到借款人的userinfo对象,溢出正在借款给的状态码
        Userinfo userinfo = userinfoService.get(bidRequest.getCreateUser().getId());
        userinfo.removeState(BitStatesUtils.OP_HAS_BIDREQUEST_PROCESS);
        //更新借款人的userinfo对象.
        userinfoService.update(userinfo);
    }

    private List<PaymentSchedule> createPaymentSchedule(BidRequest bidRequest) {
        List<PaymentSchedule> paymentSchedules = new ArrayList<PaymentSchedule>();
        PaymentSchedule ps = null;
        BigDecimal inserestTemp = BigDecimal.ZERO;
        BigDecimal principalTemp = BigDecimal.ZERO;
        for (int i = 0; i < bidRequest.getMonthes2Return(); i++) {
            //创建还款对象
            ps = new PaymentSchedule();
            ps.setBidRequestId(bidRequest.getId());
            ps.setBidRequestTitle(bidRequest.getTitle());
            ps.setBidRequestType(bidRequest.getBidRequestType());
            ps.setBorrowUser(bidRequest.getCreateUser());
            ps.setMonthIndex(i + 1);
            ps.setDeadLine(DateUtils.addMonths(bidRequest.getPublishTime(), i + 1));
            ps.setReturnType(bidRequest.getReturnType());
            if (i < bidRequest.getMonthes2Return() - 1) {
                ps.setInterest(CalculatetUtil.calMonthlyInterest(bidRequest.getReturnType(), bidRequest.getBidRequestAmount(), bidRequest.getCurrentRate(), i + 1, bidRequest.getMonthes2Return()));
                ps.setTotalAmount(CalculatetUtil.calMonthToReturnMoney(bidRequest.getReturnType(), bidRequest.getBidRequestAmount(), bidRequest.getCurrentRate(), i + 1, bidRequest.getMonthes2Return()));
                ps.setPrincipal(ps.getTotalAmount().subtract(ps.getInterest()));
                inserestTemp = inserestTemp.add(ps.getInterest());
                principalTemp = principalTemp.add(ps.getPrincipal());
            } else {
                //最后一期的本金=这次借款的金额-前面几期累加的本金
                ps.setPrincipal(bidRequest.getBidRequestAmount().subtract(principalTemp));
                //最后一期的利息=这次借款的利息-前面几期累加的利息
                ps.setInterest(bidRequest.getTotalRewardAmount().subtract(inserestTemp));
                //带换=这期的本金+这期的利息
                ps.setTotalAmount(ps.getPrincipal().add(ps.getInterest()));
            }
            //保存还款对象
            paymentScheduleService.save(ps);
            paymentSchedules.add(ps);
            //生成还款明细还款对象
            createPaymentScheduleDetails(ps, bidRequest);

        }
        return paymentSchedules;
    }

    private void createPaymentScheduleDetails(PaymentSchedule ps, BidRequest bidRequest) {
        PaymentScheduleDetail psd = null;
        Bid bid = null;
        BigDecimal principalTemp = BidConst.ZERO;
        BigDecimal inserestTemp = BidConst.ZERO;
        for (int i = 0; i < bidRequest.getBids().size(); i++) {
            bid = bidRequest.getBids().get(i);
            psd = new PaymentScheduleDetail();
            psd.setDeadLine(ps.getDeadLine());
            psd.setReturnType(ps.getReturnType());
            psd.setBidAmount(bid.getAvailableAmount());
            psd.setBidId(bid.getId());
            psd.setBidRequestId(bidRequest.getId());
            psd.setBorrowUser(bidRequest.getCreateUser());
            psd.setInvestorId(bid.getBidUser().getId());
            psd.setPaymentScheduleId(ps.getId());
            if (i < bidRequest.getBids().size() - 1) {
                BigDecimal rate = bid.getAvailableAmount().divide(bidRequest.getBidRequestAmount(), BidConst.CAL_SCALE, RoundingMode.HALF_UP);
                psd.setPrincipal(ps.getPrincipal().multiply(rate).setScale(BidConst.STORE_SCALE, RoundingMode.HALF_UP));
                psd.setInterest(ps.getInterest().multiply(rate).setScale(BidConst.STORE_SCALE, RoundingMode.HALF_UP));
                psd.setTotalAmount(psd.getPrincipal().add(psd.getInterest()));
                principalTemp = principalTemp.add(psd.getPrincipal());
                inserestTemp = inserestTemp.add(psd.getInterest());
            } else {
                //
                psd.setPrincipal(ps.getPrincipal().subtract(principalTemp));
                psd.setInterest(ps.getInterest().subtract(inserestTemp));
                psd.setTotalAmount(psd.getPrincipal().add(psd.getInterest()));
            }
            paymentScheduleDetailService.save(psd);
            ps.getDetails().add(psd);
        }

    }
}

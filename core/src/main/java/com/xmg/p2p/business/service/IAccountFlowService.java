package com.xmg.p2p.business.service;

import com.xmg.p2p.base.domain.Account;
import com.xmg.p2p.business.domain.AccountFlow;

import java.math.BigDecimal;

public interface IAccountFlowService {
    int save(AccountFlow accountFlow);
    AccountFlow get(Long id);

    void createBidFlow(Account account, BigDecimal amount);

    void createBidFaileFlow(Account account, BigDecimal availableAmount);

    void creatBorrowSuccessFlow(Account createUserAccount, BigDecimal bidRequestAmount);

    void createPayManagermentChargeFlow(Account createUserAccount, BigDecimal managermentCharge);

    void createBidSuccessFlow(Account biduserAccount, BigDecimal availableAmount);

    void createMoneyWithdrawFlow(Account account, BigDecimal moneyAmount);

    void withDrawChargeFee(Account account, BigDecimal charge);

    void withDrawFailed(Account account, BigDecimal amount);

    void withDrawSuccess(Account account, BigDecimal realWithdrawFee);

    void createReturnMoneyFlow(Account account, BigDecimal totalAmount);

    void createPayInterestManagerFlow(Account bidUserAccount, BigDecimal interestManagerCharge);
}

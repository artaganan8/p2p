package com.xmg.p2p.business.service;

import com.xmg.p2p.business.domain.SystemAccount;
import com.xmg.p2p.business.domain.SystemAccountFlow;

import java.math.BigDecimal;

public interface ISystemAccountFlowService {
    int save(SystemAccountFlow systemAccountFlow);

    void creatReceviceManagermentChargeFlow(SystemAccount systemAccount, BigDecimal managermentCharge);

    void createMoneyWithdrawFlow(SystemAccount account, BigDecimal charge);

    void createReceiveInterestManagerChargeFlow(SystemAccount systemAccount, BigDecimal interestManagerCharge);
}

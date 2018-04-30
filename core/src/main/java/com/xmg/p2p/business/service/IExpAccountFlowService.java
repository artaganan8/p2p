package com.xmg.p2p.business.service;

import com.xmg.p2p.business.domain.ExpAccount;
import com.xmg.p2p.business.domain.ExpAccountFlow;

import java.math.BigDecimal;

public interface IExpAccountFlowService {
    int save(ExpAccountFlow expAccountFlow);

    void createExpAccountFlow(ExpAccount expAccount, BigDecimal expmoney);
}

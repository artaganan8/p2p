package com.xmg.p2p.business.service;

import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.business.domain.MoneyWithdraw;
import com.xmg.p2p.business.query.MoneyWithdrawQueryObject;

import java.math.BigDecimal;

public interface IMoneyWithdrawService {
    int save(MoneyWithdraw moneyWithdraw);
    int update(MoneyWithdraw moneyWithdraw);
    MoneyWithdraw get(Long id);

    void apply(BigDecimal moneyAmount);

    PageResult queryPage(MoneyWithdrawQueryObject qo);

    void moneyWithdrawAudit(Long id, int state, String remark);
}

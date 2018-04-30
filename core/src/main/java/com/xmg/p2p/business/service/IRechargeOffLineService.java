package com.xmg.p2p.business.service;

import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.business.domain.RechargeOffLine;
import com.xmg.p2p.business.query.RechargeOffLineQueryObject;

public interface IRechargeOffLineService {
    int save(RechargeOffLine rechargeOffLine);
    int update(RechargeOffLine rechargeOffLine);
    RechargeOffLine get(Long id);

    void rechargeSave(RechargeOffLine rechargeOffLine);

    PageResult queryPage(RechargeOffLineQueryObject qo);

    void audit(Long id, String remark, int state);
}

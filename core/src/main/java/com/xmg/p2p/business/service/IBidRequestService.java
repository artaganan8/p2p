package com.xmg.p2p.business.service;

import com.xmg.p2p.base.domain.Userinfo;
import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.business.domain.BidRequest;
import com.xmg.p2p.business.query.BidRequestQueryObject;

import java.math.BigDecimal;
import java.util.List;

public interface IBidRequestService {
    int save(BidRequest bidRequest);
    int update(BidRequest bidRequest);
    BidRequest get(Long id);

    boolean canApplyBidRequest(Userinfo userinfo);

    void apply(BidRequest bidRequest);

    PageResult queryPage(BidRequestQueryObject qo);

    void publishAudit(Long id, String remark, int state);

    List<BidRequest> listIndexData(int expBidRequestType);

    void borrowBid(Long bidRequestId, BigDecimal amount);

    void audit1(Long id, int state, String remark);

    void audit2(Long id, int state, String remark);

    void applyExp(BidRequest bidRequest);

    void applyExpBid(BidRequest bidRequest);
}

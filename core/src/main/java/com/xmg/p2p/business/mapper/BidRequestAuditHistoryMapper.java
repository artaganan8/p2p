package com.xmg.p2p.business.mapper;

import com.xmg.p2p.base.query.QueryObject;
import com.xmg.p2p.business.domain.BidRequestAuditHistory;

import java.util.List;

public interface BidRequestAuditHistoryMapper {
    int insert(BidRequestAuditHistory record);
    BidRequestAuditHistory selectByPrimaryKey(Long id);

	Long queryPageCount(QueryObject qo);
	List<BidRequestAuditHistory> queryPageData(QueryObject qo);

    List<BidRequestAuditHistory> queryListByBidRequestId(Long bidRequestId);
}
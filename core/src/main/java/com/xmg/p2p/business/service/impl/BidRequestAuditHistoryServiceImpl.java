package com.xmg.p2p.business.service.impl;

import com.xmg.p2p.business.domain.BidRequestAuditHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xmg.p2p.business.mapper.BidRequestAuditHistoryMapper;
import com.xmg.p2p.business.service.IBidRequestAuditHistoryService;

import java.util.List;

@Service
public class BidRequestAuditHistoryServiceImpl implements IBidRequestAuditHistoryService {
	@Autowired
	private BidRequestAuditHistoryMapper bidRequestAuditHistoryMapper;

	@Override
	public int save(BidRequestAuditHistory bidRequestAuditHistory) {
		return bidRequestAuditHistoryMapper.insert(bidRequestAuditHistory);
	}

	@Override
	public List<BidRequestAuditHistory> queryListByBidRequestId(Long bidRequestId) {
		return bidRequestAuditHistoryMapper.queryListByBidRequestId(bidRequestId);
	}
}

package com.xmg.p2p.business.service.impl;

import com.xmg.p2p.business.domain.Bid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xmg.p2p.business.mapper.BidMapper;
import com.xmg.p2p.business.service.IBidService;

@Service
public class BidServiceImpl implements IBidService {
	@Autowired
	private BidMapper bidMapper;

	@Override
	public int save(Bid bid) {
		return bidMapper.insert(bid);
	}

	@Override
	public int update(Bid bid) {
		return bidMapper.updateByPrimaryKey(bid);
	}

	@Override
	public Bid get(Long id) {
		return bidMapper.selectByPrimaryKey(id);
	}

	@Override
	public void updateState(Long bidRequestId, int bidRequestState) {
		bidMapper.updateState(bidRequestId,bidRequestState);
	}
}

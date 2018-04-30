package com.xmg.p2p.business.service;

import com.xmg.p2p.business.domain.Bid;

public interface IBidService {
    int save(Bid bid);
    int update(Bid bid);
    Bid get(Long id);

    void updateState(Long bidRequestId, int bidRequestState);
}

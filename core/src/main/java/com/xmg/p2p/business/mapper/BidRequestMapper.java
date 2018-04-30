package com.xmg.p2p.business.mapper;

import com.xmg.p2p.base.query.QueryObject;
import com.xmg.p2p.business.domain.BidRequest;
import com.xmg.p2p.business.query.BidRequestQueryObject;

import java.util.List;

public interface BidRequestMapper {
    int insert(BidRequest record);
    BidRequest selectByPrimaryKey(Long id);
    int updateByPrimaryKey(BidRequest record);
	Long queryPageCount(QueryObject qo);
	List<BidRequest> queryPageData(QueryObject qo);

    List<BidRequest> listIndexData(BidRequestQueryObject qo);
}
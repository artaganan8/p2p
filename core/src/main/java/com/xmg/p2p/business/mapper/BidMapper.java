package com.xmg.p2p.business.mapper;

import com.xmg.p2p.base.query.QueryObject;
import com.xmg.p2p.business.domain.Bid;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BidMapper {
    int insert(Bid record);
    Bid selectByPrimaryKey(Long id);
    int updateByPrimaryKey(Bid record);
	Long queryPageCount(QueryObject qo);
	List<Bid> queryPageData(QueryObject qo);

    void updateState(@Param("bidRequestId") Long bidRequestId, @Param("bidRequestState") int bidRequestState);
}
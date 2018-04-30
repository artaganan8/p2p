package com.xmg.p2p.business.mapper;

import com.xmg.p2p.base.query.QueryObject;
import com.xmg.p2p.business.domain.RechargeOffLine;

import java.util.List;

public interface RechargeOffLineMapper {
    int insert(RechargeOffLine record);
    RechargeOffLine selectByPrimaryKey(Long id);
    int updateByPrimaryKey(RechargeOffLine record);
	Long queryPageCount(QueryObject qo);
	List<RechargeOffLine> queryPageData(QueryObject qo);
}
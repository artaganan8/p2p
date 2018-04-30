package com.xmg.p2p.business.mapper;

import com.xmg.p2p.base.query.QueryObject;
import com.xmg.p2p.business.domain.ExpAccountGrantRecord;

import java.util.List;

public interface ExpAccountGrantRecordMapper {
    int insert(ExpAccountGrantRecord record);
    ExpAccountGrantRecord selectByPrimaryKey(Long id);
    int updateByPrimaryKey(ExpAccountGrantRecord record);
	Long queryPageCount(QueryObject qo);
	List<ExpAccountGrantRecord> queryPageData(QueryObject qo);
}
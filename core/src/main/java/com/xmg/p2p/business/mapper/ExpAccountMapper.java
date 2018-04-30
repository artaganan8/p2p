package com.xmg.p2p.business.mapper;

import com.xmg.p2p.base.query.QueryObject;
import com.xmg.p2p.business.domain.ExpAccount;

import java.util.List;

public interface ExpAccountMapper {
    int insert(ExpAccount record);
    ExpAccount selectByPrimaryKey(Long id);
    int updateByPrimaryKey(ExpAccount record);
	Long queryPageCount(QueryObject qo);
	List<ExpAccount> queryPageData(QueryObject qo);
}
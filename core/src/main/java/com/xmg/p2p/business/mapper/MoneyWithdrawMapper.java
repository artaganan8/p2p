package com.xmg.p2p.business.mapper;

import com.xmg.p2p.base.query.QueryObject;
import com.xmg.p2p.business.domain.MoneyWithdraw;

import java.util.List;

public interface MoneyWithdrawMapper {
    int insert(MoneyWithdraw record);
    MoneyWithdraw selectByPrimaryKey(Long id);
    int updateByPrimaryKey(MoneyWithdraw record);
	Long queryPageCount(QueryObject qo);
	List<MoneyWithdraw> queryPageData(QueryObject qo);
}
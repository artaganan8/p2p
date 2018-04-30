package com.xmg.p2p.business.mapper;

import com.xmg.p2p.base.query.QueryObject;
import com.xmg.p2p.business.domain.AccountFlow;

import java.util.List;

public interface AccountFlowMapper {
    int insert(AccountFlow record);
    AccountFlow selectByPrimaryKey(Long id);

	Long queryPageCount(QueryObject qo);
	List<AccountFlow> queryPageData(QueryObject qo);
}
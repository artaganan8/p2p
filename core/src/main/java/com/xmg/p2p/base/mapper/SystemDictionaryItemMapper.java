package com.xmg.p2p.base.mapper;

import com.xmg.p2p.base.domain.SystemDictionaryItem;
import com.xmg.p2p.base.query.QueryObject;

import java.util.List;

public interface SystemDictionaryItemMapper {
    int insert(SystemDictionaryItem record);
    SystemDictionaryItem selectByPrimaryKey(Long id);
    int updateByPrimaryKey(SystemDictionaryItem record);
	Long queryPageCount(QueryObject qo);
	List<SystemDictionaryItem> queryPageData(QueryObject qo);

    List<SystemDictionaryItem> listBySn(String sn);
}
package com.xmg.p2p.base.mapper;

import com.xmg.p2p.base.domain.SystemDictionary;
import com.xmg.p2p.base.query.QueryObject;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SystemDictionaryMapper {
    int insert(SystemDictionary record);
    SystemDictionary selectByPrimaryKey(Long id);
    int updateByPrimaryKey(SystemDictionary record);
	Long queryPageCount(QueryObject qo);
	List<SystemDictionary> queryPageData(QueryObject qo);

    List<SystemDictionary> selectAll();
}
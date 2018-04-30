package com.xmg.p2p.base.mapper;

import com.xmg.p2p.base.domain.RealAuth;
import com.xmg.p2p.base.query.QueryObject;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RealAuthMapper {
    int insert(RealAuth record);
    RealAuth selectByPrimaryKey(Long id);
    int updateByPrimaryKey(RealAuth record);
	Long queryPageCount(QueryObject qo);
	List<RealAuth> queryPageData(QueryObject qo);
}
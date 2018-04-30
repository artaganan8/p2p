package com.xmg.p2p.base.mapper;

import com.xmg.p2p.base.domain.VedioAuth;
import com.xmg.p2p.base.query.QueryObject;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface VedioAuthMapper {
    int insert(VedioAuth record);
    VedioAuth selectByPrimaryKey(Long id);
    int updateByPrimaryKey(VedioAuth record);
	Long queryPageCount(QueryObject qo);
	List<VedioAuth> queryPageData(QueryObject qo);
}
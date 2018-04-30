package com.xmg.p2p.base.mapper;

import com.xmg.p2p.base.domain.IpLog;
import com.xmg.p2p.base.query.QueryObject;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface IpLogMapper {
    int insert(IpLog record);
    IpLog selectByPrimaryKey(Long id);
    int updateByPrimaryKey(IpLog record);
	Long queryPageCount(QueryObject qo);
	List<IpLog> queryPageData(QueryObject qo);
}
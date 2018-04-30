package com.xmg.p2p.business.mapper;

import com.xmg.p2p.base.query.QueryObject;
import com.xmg.p2p.business.domain.PlatFormBankInfo;

import java.util.List;

public interface PlatFormBankInfoMapper {
    int insert(PlatFormBankInfo record);
    PlatFormBankInfo selectByPrimaryKey(Long id);
    int updateByPrimaryKey(PlatFormBankInfo record);
	Long queryPageCount(QueryObject qo);
	List<PlatFormBankInfo> queryPageData(QueryObject qo);

    List<PlatFormBankInfo> selectAll();
}
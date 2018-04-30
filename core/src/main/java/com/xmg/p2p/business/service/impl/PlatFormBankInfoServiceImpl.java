package com.xmg.p2p.business.service.impl;

import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.business.domain.PlatFormBankInfo;
import com.xmg.p2p.business.mapper.PlatFormBankInfoMapper;
import com.xmg.p2p.business.query.PlatFormBankInfoQueryObject;
import com.xmg.p2p.business.service.IPlatFormBankInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlatFormBankInfoServiceImpl implements IPlatFormBankInfoService {
	@Autowired
	private PlatFormBankInfoMapper platFormBankInfoMapper;

	@Override
	public int save(PlatFormBankInfo platFormBankInfo) {
		return platFormBankInfoMapper.insert(platFormBankInfo);
	}

	@Override
	public int update(PlatFormBankInfo platFormBankInfo) {
		return platFormBankInfoMapper.updateByPrimaryKey(platFormBankInfo);
	}

	@Override
	public PageResult queryPage(PlatFormBankInfoQueryObject qo) {
		Long count = platFormBankInfoMapper.queryPageCount(qo);
		if (count==0){
			return PageResult.empty(qo.getPageSize());
		}
		List<PlatFormBankInfo> list = platFormBankInfoMapper.queryPageData(qo);
		return new PageResult(list,count.intValue(),qo.getCurrentPage(),qo.getPageSize());
	}

	@Override
	public void saveOrUpdate(PlatFormBankInfo platFormBankInfo) {
		if (platFormBankInfo.getId()==null){
			platFormBankInfoMapper.insert(platFormBankInfo);
		}else{
			platFormBankInfoMapper.updateByPrimaryKey(platFormBankInfo);
		}
	}

	@Override
	public List<PlatFormBankInfo> selectAll() {
		return platFormBankInfoMapper.selectAll();
	}

}

package com.xmg.p2p.business.service.impl;

import com.xmg.p2p.business.domain.ExpAccountGrantRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xmg.p2p.business.mapper.ExpAccountGrantRecordMapper;
import com.xmg.p2p.business.service.IExpAccountGrantRecordService;

@Service
public class ExpAccountGrantRecordServiceImpl implements IExpAccountGrantRecordService {
	@Autowired
	private ExpAccountGrantRecordMapper expAccountGrantRecordMapper;

	@Override
	public int save(ExpAccountGrantRecord expAccountGrantRecord) {
		return expAccountGrantRecordMapper.insert(expAccountGrantRecord);
	}

	@Override
	public int update(ExpAccountGrantRecord expAccountGrantRecord) {
		return expAccountGrantRecordMapper.updateByPrimaryKey(expAccountGrantRecord);
	}

	@Override
	public ExpAccountGrantRecord get(Long id) {
		return expAccountGrantRecordMapper.selectByPrimaryKey(id);
	}
}

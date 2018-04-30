package com.xmg.p2p.base.service.impl;

import com.xmg.p2p.base.domain.IpLog;
import com.xmg.p2p.base.mapper.IpLogMapper;
import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.base.query.IpLogQueryObject;
import com.xmg.p2p.base.service.IIpLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class IpLogServiceImpl implements IIpLogService {
	@Autowired
	private IpLogMapper ipLogMapper;

	@Override
	public PageResult query(IpLogQueryObject qo) {
		Long count = ipLogMapper.queryPageCount(qo);
		if(count<=0){
			return new PageResult(Collections.EMPTY_LIST,0,qo.getCurrentPage(),qo.getPageSize());
		}
        List<IpLog> pageData = ipLogMapper.queryPageData(qo);
        PageResult pageResult = new PageResult(pageData, count.intValue(), qo.getCurrentPage(), qo.getPageSize());
        return pageResult;
	}

    @Override
    public int save(IpLog ipLog) {
        return ipLogMapper.insert(ipLog);
    }

}

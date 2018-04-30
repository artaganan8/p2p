package com.xmg.p2p.base.service.impl;

import com.xmg.p2p.base.domain.SystemDictionaryItem;
import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.base.query.SystemDictionaryItemQueryObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xmg.p2p.base.mapper.SystemDictionaryItemMapper;
import com.xmg.p2p.base.service.ISystemDictionaryItemService;

import java.util.List;

@Service
public class SystemDictionaryItemServiceImpl implements ISystemDictionaryItemService {
	@Autowired
	private SystemDictionaryItemMapper systemDictionaryItemMapper;

	@Override
	public PageResult queryPage(SystemDictionaryItemQueryObject qo) {
		Long count = systemDictionaryItemMapper.queryPageCount(qo);
		if (count==0){
			return PageResult.empty(qo.getPageSize());
		}
        List<SystemDictionaryItem> list = systemDictionaryItemMapper.queryPageData(qo);
        return new PageResult(list,count.intValue(),qo.getCurrentPage(),qo.getPageSize());
	}

    @Override
    public void saveOrUpdate(SystemDictionaryItem systemDictionaryItem) {
        if(systemDictionaryItem.getId()==null){
            systemDictionaryItemMapper.insert(systemDictionaryItem);
        }else{
            systemDictionaryItemMapper.updateByPrimaryKey(systemDictionaryItem);
        }
    }

    @Override
    public List<SystemDictionaryItem> listBySn(String sn) {
        return systemDictionaryItemMapper.listBySn(sn);
    }
}

package com.xmg.p2p.base.service;

import com.xmg.p2p.base.domain.SystemDictionaryItem;
import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.base.query.SystemDictionaryItemQueryObject;

import java.util.List;

public interface ISystemDictionaryItemService {
    PageResult queryPage(SystemDictionaryItemQueryObject qo);

    void saveOrUpdate(SystemDictionaryItem systemDictionaryItem);

    List<SystemDictionaryItem> listBySn(String educationBackground);
}

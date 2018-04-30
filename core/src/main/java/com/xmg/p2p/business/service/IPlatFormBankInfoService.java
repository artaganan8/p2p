package com.xmg.p2p.business.service;

import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.business.domain.PlatFormBankInfo;
import com.xmg.p2p.business.query.PlatFormBankInfoQueryObject;

import java.util.List;

public interface IPlatFormBankInfoService {
    int save(PlatFormBankInfo platFormBankInfo);
    int update(PlatFormBankInfo platFormBankInfo);

    PageResult queryPage(PlatFormBankInfoQueryObject qo);

    void saveOrUpdate(PlatFormBankInfo platFormBankInfo);

    List<PlatFormBankInfo> selectAll();
}

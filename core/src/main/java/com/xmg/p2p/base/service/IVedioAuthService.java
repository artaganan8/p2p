package com.xmg.p2p.base.service;

import com.xmg.p2p.base.domain.VedioAuth;
import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.base.query.VedioAuthQueryObject;

public interface IVedioAuthService {
    int save(VedioAuth vedioAuth);

    PageResult queryPage(VedioAuthQueryObject qo);

    void vedioAuthAudit(Long loginInfoValue, int state, String remark);
}

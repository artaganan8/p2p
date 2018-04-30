package com.xmg.p2p.base.service;

import com.xmg.p2p.base.domain.RealAuth;
import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.base.query.RealAuthQueryObject;

public interface IRealAuthService {
    void save(RealAuth realAuth);
    int update(RealAuth realAuth);
    RealAuth get(Long realAuthId);

    void realAuthSave(RealAuth realAuth);

    PageResult queryPage(RealAuthQueryObject qo);

    void realAuthAudit(Long id, int state, String remark);
}

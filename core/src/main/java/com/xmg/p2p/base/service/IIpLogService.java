package com.xmg.p2p.base.service;

import com.xmg.p2p.base.domain.IpLog;
import com.xmg.p2p.base.domain.Logininfo;
import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.base.query.IpLogQueryObject;

public interface IIpLogService {
    PageResult query(IpLogQueryObject qo);

    int save(IpLog ipLog);

}

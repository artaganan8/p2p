package com.xmg.p2p.business.service;

import com.xmg.p2p.business.domain.ExpAccountGrantRecord;

public interface IExpAccountGrantRecordService {
    int save(ExpAccountGrantRecord expAccountGrantRecord);
    int update(ExpAccountGrantRecord expAccountGrantRecord);
    ExpAccountGrantRecord get(Long id);
}

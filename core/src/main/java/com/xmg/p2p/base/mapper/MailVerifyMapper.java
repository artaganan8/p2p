package com.xmg.p2p.base.mapper;

import com.xmg.p2p.base.domain.MailVerify;

public interface MailVerifyMapper {
    int insert(MailVerify record);
    MailVerify selectByuuid(String uuid);
}
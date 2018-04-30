package com.xmg.p2p.base.service;

import com.xmg.p2p.base.domain.MailVerify;

public interface IMailVerifyService {
    int save(MailVerify mailVerify);
    MailVerify selectByuuid(String uuid);
}

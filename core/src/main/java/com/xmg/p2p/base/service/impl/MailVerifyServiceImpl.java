package com.xmg.p2p.base.service.impl;

import com.xmg.p2p.base.domain.MailVerify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xmg.p2p.base.mapper.MailVerifyMapper;
import com.xmg.p2p.base.service.IMailVerifyService;

@Service
public class MailVerifyServiceImpl implements IMailVerifyService {
	@Autowired
	private MailVerifyMapper mailVerifyMapper;

	@Override
	public int save(MailVerify mailVerify) {
		return mailVerifyMapper.insert(mailVerify);
	}

	@Override
	public MailVerify selectByuuid(String uuid) {
		return mailVerifyMapper.selectByuuid(uuid);
	}
}

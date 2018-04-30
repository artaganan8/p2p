package com.xmg.p2p.business.service.impl;

import com.xmg.p2p.business.domain.PaymentScheduleDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xmg.p2p.business.mapper.PaymentScheduleDetailMapper;
import com.xmg.p2p.business.service.IPaymentScheduleDetailService;

import java.util.Date;

@Service
public class PaymentScheduleDetailServiceImpl implements IPaymentScheduleDetailService {
	@Autowired
	private PaymentScheduleDetailMapper paymentScheduleDetailMapper;

	@Override
	public int save(PaymentScheduleDetail paymentScheduleDetail) {
		return paymentScheduleDetailMapper.insert(paymentScheduleDetail);
	}

	@Override
	public int update(PaymentScheduleDetail paymentScheduleDetail) {
		return paymentScheduleDetailMapper.updateByPrimaryKey(paymentScheduleDetail);
	}

	@Override
	public PaymentScheduleDetail get(Long id) {
		return paymentScheduleDetailMapper.selectByPrimaryKey(id);
	}

	@Override
	public void updatePayDate(Long id, Date date) {
		paymentScheduleDetailMapper.updatePayDate(id,date);
	}
}

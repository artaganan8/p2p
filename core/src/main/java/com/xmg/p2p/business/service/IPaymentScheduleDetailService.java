package com.xmg.p2p.business.service;

import com.xmg.p2p.business.domain.PaymentScheduleDetail;

import java.util.Date;

public interface IPaymentScheduleDetailService {
    int save(PaymentScheduleDetail paymentScheduleDetail);
    int update(PaymentScheduleDetail paymentScheduleDetail);
    PaymentScheduleDetail get(Long id);

    void updatePayDate(Long id, Date date);
}

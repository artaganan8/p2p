package com.xmg.p2p.business.mapper;

import com.xmg.p2p.base.query.QueryObject;
import com.xmg.p2p.business.domain.PaymentSchedule;

import java.util.List;

public interface PaymentScheduleMapper {
    int insert(PaymentSchedule record);
    PaymentSchedule selectByPrimaryKey(Long id);
    int updateByPrimaryKey(PaymentSchedule record);
	Long queryPageCount(QueryObject qo);
	List<PaymentSchedule> queryPageData(QueryObject qo);

    List<PaymentSchedule> queryBiBidRequestId(Long bidRequestId);
}
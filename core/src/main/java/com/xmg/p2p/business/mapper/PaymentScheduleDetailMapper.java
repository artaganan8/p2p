package com.xmg.p2p.business.mapper;

import com.xmg.p2p.base.query.QueryObject;
import com.xmg.p2p.business.domain.PaymentScheduleDetail;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface PaymentScheduleDetailMapper {
    int insert(PaymentScheduleDetail record);
    PaymentScheduleDetail selectByPrimaryKey(Long id);
    int updateByPrimaryKey(PaymentScheduleDetail record);
	Long queryPageCount(QueryObject qo);
	List<PaymentScheduleDetail> queryPageData(QueryObject qo);

    void updatePayDate(@Param("id") Long id, @Param("date") Date date);
}
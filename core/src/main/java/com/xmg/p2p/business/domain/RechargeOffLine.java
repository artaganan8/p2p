package com.xmg.p2p.business.domain;

import com.alibaba.fastjson.JSON;
import com.xmg.p2p.base.domain.BaseAuthDomain;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by machao on 2017/9/8.
 */
@Setter@Getter
public class RechargeOffLine extends BaseAuthDomain {
    private PlatFormBankInfo bankInfo;//
    private String tradeCode;//交易号
    private BigDecimal amount;//充值金额
    private Date tradeTime;//充值时间
    private String note;//充值说明
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public void setTradeTime(Date tradeTime){
        this.tradeTime = tradeTime;
    }
    public String getJsonString(){
        Map<Object, Object> map = new HashMap<>();
        map.put("id",id);
        map.put("tradeCode",tradeCode);
        map.put("amount",amount);
        map.put("tradeTime",tradeTime);
        map.put("username",getApplier().getUsername());
        return JSON.toJSONString(map);
    }
}

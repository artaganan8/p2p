package com.xmg.p2p.business.domain;

import com.alibaba.fastjson.JSON;
import com.xmg.p2p.base.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by machao on 2017/9/8.
 */
@Setter@Getter
public class PlatFormBankInfo extends BaseDomain {
    private String bankName;    //银行名称
    private String accountNumber;  //银行账号
    private String bankForkName;    //支行名称
    private String accountName; //开户人姓名
    public String getJsonString(){
        Map<Object, Object> map = new HashMap<>();
        map.put("id",id);
        map.put("bankName",bankName);
        map.put("accountNumber",accountNumber);
        map.put("bankForkName",bankForkName);
        map.put("accountName",accountName);
        return JSON.toJSONString(map);
    }
}

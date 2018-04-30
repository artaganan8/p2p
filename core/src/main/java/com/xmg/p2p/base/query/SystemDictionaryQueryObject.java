package com.xmg.p2p.base.query;

import com.alibaba.druid.util.StringUtils;
import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class SystemDictionaryQueryObject extends QueryObject {
    private String keyword;
    public String getKeyword(){
        return StringUtils.isEmpty(keyword)?null:keyword;
    }
}

package com.xmg.p2p.base.query;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class SystemDictionaryItemQueryObject extends QueryObject {
    private Long parentId;
    public Long getParentId(){
        return parentId==null?null:parentId;
    }
}

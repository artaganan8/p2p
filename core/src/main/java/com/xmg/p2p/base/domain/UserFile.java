package com.xmg.p2p.base.domain;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by machao on 2017/9/7.
 */
@Setter@Getter
public class UserFile extends BaseAuthDomain {
    private int score;
    private String image;
    private SystemDictionaryItem fileType;
    public String getJsonString(){
        Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        map.put("applier",applier.getUsername());
        map.put("fileType",fileType.getTitle());
        map.put("image",image);
        return JSON.toJSONString(map);
    }
}

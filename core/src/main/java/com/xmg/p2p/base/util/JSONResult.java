package com.xmg.p2p.base.util;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JSONResult {
    private boolean success = true;
    private String msg;

    public JSONResult(){}
    //标记为错误状态,设置错误信息
    public JSONResult(String msg) {
        this.msg = msg;
    }
    public JSONResult(boolean success,String msg) {
        this.success = false;
        this.msg = msg;
    }
}

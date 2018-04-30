package com.xmg.p2p.base.query;

import com.xmg.p2p.base.util.DateUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by machao on 2017/9/5.
 */
@Setter@Getter
public class AuthQueryObject extends QueryObject {
    protected int state=-1;
    protected Date beginDate;
    protected Date endDate;
    public Date getBeginDate(){
        return DateUtil.getBeginDate(beginDate);
    }
    @DateTimeFormat(pattern="yyyy-MM-dd")
    public void setBeginDate(Date beginDate){
        this.beginDate=beginDate;
    }
    public Date getEndDate(){
        return DateUtil.getEndDate(endDate);
    }
    @DateTimeFormat(pattern="yyyy-MM-dd")
    public void setEndDate(Date endDate){
        this.endDate = endDate;
    }
}

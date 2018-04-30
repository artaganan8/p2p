package com.xmg.p2p.business.query;

import com.xmg.p2p.base.query.QueryObject;
import com.xmg.p2p.base.util.DateUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Setter@Getter
public class BidRequestQueryObject extends QueryObject {
    private int bidRequestState = -1;
    private int[] states;
    private int bidRequestType = -1;

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
    private String orderByType;
}

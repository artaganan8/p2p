package com.xmg.p2p.base.query;

import com.alibaba.druid.util.StringUtils;
import com.xmg.p2p.base.util.DateUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Setter@Getter
public class IpLogQueryObject extends QueryObject {
    private String username;
    private Date beginDate;
    private Date endDate;
    private int state = -1;
    private int userType = -1;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public String getUsername(){
        return StringUtils.isEmpty(username)?null:username;
    }
    public Date getBeginDate(){
        return DateUtil.getBeginDate(beginDate);
    }
    public Date getEndDate(){
        return DateUtil.getEndDate(endDate);
    }
}

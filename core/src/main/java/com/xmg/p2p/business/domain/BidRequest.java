package com.xmg.p2p.business.domain;

import com.alibaba.fastjson.JSONObject;
import com.xmg.p2p.base.domain.BaseDomain;
import com.xmg.p2p.base.domain.Logininfo;
import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.business.util.CalculatetUtil;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static com.xmg.p2p.base.util.BidConst.*;

/**
 * Created by machao on 2017/9/7.
 */
@Setter@Getter
public class BidRequest extends BaseDomain {
    private int version;// 版本号
    private int returnType;// 还款类型(等额本息)
    private int bidRequestType;// 借款类型(信用标)
    private int bidRequestState;// 借款状态
    private BigDecimal bidRequestAmount;// 借款总金额
    private BigDecimal currentRate;// 年化利率
    private BigDecimal minBidAmount;// 最小投標金额
    private int monthes2Return;// 还款月数
    private int bidCount = 0;// 已投标次数(冗余)
    private BigDecimal totalRewardAmount;// 总回报金额(总利息)
    private BigDecimal currentSum = BidConst.ZERO;// 当前已投标总金额
    private String title;// 借款标题
    private String description;// 借款描述
    private String note;// 风控意见
    private Date disableDate;// 招标截止日期
    private int disableDays;// 招标天数
    private Logininfo createUser;// 借款人
    private Date applyTime;// 申请时间
    private Date publishTime;// 发标时间
    private List<Bid> bids = new ArrayList<>();// 针对该借款的投标

    private String staticUrl;//静态访问地址
    /**
     * 剩余金额
     * @return
     */
    public BigDecimal getRemainAmount(){
        return this.bidRequestAmount.subtract(this.currentSum);
    }

    /**
     * 筹标进度百分比
     * @return
     */
    public BigDecimal getPersent(){
        return this.currentSum.multiply(CalculatetUtil.ONE_HUNDRED)
                .divide(this.bidRequestAmount,BidConst.DISPLAY_SCALE, RoundingMode.HALF_UP);
    }
    public String getBidRequestStateDisplay() {
        switch (this.bidRequestState) {
            case BIDREQUEST_STATE_PUBLISH_PENDING:
                return "待发布";
            case BIDREQUEST_STATE_BIDDING:
                return "招标中";
            case BIDREQUEST_STATE_UNDO:
                return "已撤销";
            case BIDREQUEST_STATE_BIDDING_OVERDUE:
                return "流标";
            case BIDREQUEST_STATE_APPROVE_PENDING_1:
                return "满标一审";
            case BIDREQUEST_STATE_APPROVE_PENDING_2:
                return "满标二审";
            case BIDREQUEST_STATE_REJECTED:
                return "满标审核被拒";
            case BIDREQUEST_STATE_PAYING_BACK:
                return "还款中";
            case BIDREQUEST_STATE_COMPLETE_PAY_BACK:
                return "完成";
            case BIDREQUEST_STATE_PAY_BACK_OVERDUE:
                return "逾期";
            case BIDREQUEST_STATE_PUBLISH_REFUSE:
                return "发标拒绝";
            default:
                return "";
        }
    }
    public String getReturnTypeDisplay() {
        return returnType == BidConst.RETURN_TYPE_MONTH_INTEREST ? "按月到期"
                : "等额本息";
    }
    public String getBidRequestTypeDisplay(){
        return this.bidRequestType == BidConst.BIDREQUEST_TYPE_EXP ? "体"
                : "信";
    }
    public String getJsonString() {
        Map<String, Object> json = new HashMap<>();
        json.put("id", id);
        json.put("username", this.createUser.getUsername());
        json.put("title", title);
        json.put("bidRequestAmount", bidRequestAmount);
        json.put("currentRate", currentRate);
        json.put("monthes2Return", monthes2Return);
        json.put("returnType", getReturnTypeDisplay());
        json.put("totalRewardAmount", totalRewardAmount);
        return JSONObject.toJSONString(json);
    }
}

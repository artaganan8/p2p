package com.xmg.p2p.controller;

import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.base.util.JSONResult;
import com.xmg.p2p.business.domain.BidRequest;
import com.xmg.p2p.business.query.BidRequestQueryObject;
import com.xmg.p2p.business.service.IBidRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by machao on 2017/9/12.
 */
@Controller
public class ExpBidRequestController {
    @Autowired
    private IBidRequestService bidRequestService;

    /**
     * 导向到发布页面
     * @param model
     * @return
     */
    @RequestMapping("expBidRequestPublishPage")
    public String expBidRequestPublishPage(Model model){
        model.addAttribute("minBidRequestAmount", BidConst.SMALLEST_BIDREQUEST_AMOUNT);
        model.addAttribute("minBidAmount", BidConst.SMALLEST_BID_AMOUNT);
        return "expbidrequest/expbidrequestpublish";
    }
    @RequestMapping("expBidRequestPublish")
    @ResponseBody
    public JSONResult expBidRequestPublish(BidRequest bidRequest){
        JSONResult result = null;
        try {
            bidRequestService.applyExp(bidRequest);
            result = new JSONResult();
        } catch (Exception e) {
            e.printStackTrace();
            result = new JSONResult(false, e.getMessage());
        }
        return result;
    }
    @RequestMapping("expBidRequest_list")
    public String expBidRequestList(@ModelAttribute("qo")BidRequestQueryObject qo,Model model){
        qo.setBidRequestType(BidConst.BIDREQUEST_TYPE_EXP);
        model.addAttribute("pageResult",bidRequestService.queryPage(qo));
        return "expbidrequest/expbidrequestlist";
    }
}

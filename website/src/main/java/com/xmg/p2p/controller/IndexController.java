package com.xmg.p2p.controller;

import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.business.query.BidRequestQueryObject;
import com.xmg.p2p.business.service.IBidRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by machao on 2017/9/8.
 */
@Controller
public class IndexController {
    @Autowired
    private IBidRequestService bidRequestService;

    @RequestMapping("loadIndexInvestList")
    public String indexPage(Model model){
        //只是查询,招标中,还款中,已完成的标,5条信用标
        model.addAttribute("bidRequests", bidRequestService.listIndexData(BidConst.BIDREQUEST_TYPE_NORMAL));
        return "bidrequestlist";
    }
    @RequestMapping("loadIndexTiList")
    public String loadIndexTiList(Model model){
        //列出体验标列表,默认显示前5个
        model.addAttribute("expBidRequests", bidRequestService.listIndexData(BidConst.BIDREQUEST_TYPE_EXP));
        return "explist";
    }
    @RequestMapping("loadheadlist")
    public String loadheadlist(){
        return "common/head-tpl";
    }
    @RequestMapping("invest")
    public String investPage(){
        return "invest";
    }
    @RequestMapping("invest_list")
    public String investList(@ModelAttribute("qo") BidRequestQueryObject qo, Model model){
        if (qo.getBidRequestState()==-1){
            qo.setStates(new int[]{BidConst.BIDREQUEST_STATE_BIDDING,
                    BidConst.BIDREQUEST_STATE_PAYING_BACK,BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK});
        }
        qo.setOrderByType(" b.bidRequestState ASC ");
        model.addAttribute("pageResult",bidRequestService.queryPage(qo));
        return "invest_list";
    }
}

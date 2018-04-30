package com.xmg.p2p.controller;

import com.xmg.p2p.base.domain.Account;
import com.xmg.p2p.base.domain.Userinfo;
import com.xmg.p2p.base.service.IAccountService;
import com.xmg.p2p.base.service.IUserinfoService;
import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.base.util.UserContext;
import com.xmg.p2p.business.service.IBidRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by machao on 2017/9/4.
 */
@Controller
public class BorrowController {
    @Autowired
    private IUserinfoService userinfoService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IBidRequestService bidRequestService;
    @RequestMapping("borrow")
    public String borrow(Model model){
        if (UserContext.getCurrent()==null){
            return "redirect:/borrow.html";
        }
        Userinfo userinfo = userinfoService.getCurrent();
        Account account = accountService.getCurrent();
        model.addAttribute("userinfo",userinfo);
        model.addAttribute("account",account);
        model.addAttribute("creditBorrowScore", BidConst.BASE_BORROW_SCORE);
        return "borrow";
    }
    @RequestMapping("borrowInfo")
    public String borrowInfoPage(Model model){
        Userinfo userinfo = userinfoService.getCurrent();
        //判断用户是否具有借款的条件
        if (bidRequestService.canApplyBidRequest(userinfo)){
            //如果有借款的条件,判断是否有借款的流程
            if(userinfo.getHasBidRequestProcess()){
                //如果有进入borrow_apply_result.ftl
                return "borrow_apply_result";
            }else{
                //如果没有进入borrow_apply.ftl
                model.addAttribute("minBidRequestAmount",BidConst.SMALLEST_BIDREQUEST_AMOUNT);
                model.addAttribute("minBidAmount",BidConst.SMALLEST_BID_AMOUNT);
                model.addAttribute("account",accountService.getCurrent());
                return "borrow_apply";
            }
        }else{
            //没有借款条件,跳转至借款条件页面
            return "redirect:/borrow.do";
        }
    }
   /* @RequestMapping("borrow_apply")
    public String borrowApply(BidRequest bidRequest){
        if (bidRequest.getBidRequestType() == BidConst.BIDREQUEST_TYPE_NORMAL){
            bidRequestService.apply(bidRequest);
        }else{
            bidRequestService.applyExpBid(bidRequest);
        }
        return "redirect:borrowInfo.do";
    }*/
}

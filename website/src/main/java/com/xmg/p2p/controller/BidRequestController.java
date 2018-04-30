package com.xmg.p2p.controller;

import com.xmg.p2p.base.domain.Logininfo;
import com.xmg.p2p.base.domain.UserFile;
import com.xmg.p2p.base.domain.Userinfo;
import com.xmg.p2p.base.service.IAccountService;
import com.xmg.p2p.base.service.IRealAuthService;
import com.xmg.p2p.base.service.IUserFileService;
import com.xmg.p2p.base.service.IUserinfoService;
import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.base.util.JSONResult;
import com.xmg.p2p.base.util.UserContext;
import com.xmg.p2p.business.domain.BidRequest;
import com.xmg.p2p.business.query.PaymentScheduleQueryObject;
import com.xmg.p2p.business.service.IBidRequestService;
import com.xmg.p2p.business.service.IExpAccountService;
import com.xmg.p2p.business.service.IPaymentScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

/**
 * Created by machao on 2017/9/8.
 */
@Controller
public class BidRequestController {
    @Autowired
    private IBidRequestService bidRequestService;
    @Autowired
    private IUserFileService userFileService;
    @Autowired
    private IUserinfoService userinfoService;
    @Autowired
    private IRealAuthService realAuthService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IPaymentScheduleService paymentScheduleService;
    @Autowired
    private IExpAccountService expAccountService;

    @RequestMapping("borrow_info")
    public String borrowInfoPage(Long id, Model model) {
        //当前的id对于借款给对象
        //借款人的userinfo信息
        //当前登录用户的account对象
        //借款人的风控材料信息
        BidRequest bidRequest = bidRequestService.get(id);
        String returnPage = "";
        if (bidRequest != null) {
            model.addAttribute("bidRequest", bidRequest);
            Logininfo current = UserContext.getCurrent();
            //信用标
            if (bidRequest.getBidRequestType() == BidConst.BIDREQUEST_TYPE_NORMAL) {
                //userinfo对象信息
                Userinfo userinfo = userinfoService.get(bidRequest.getCreateUser().getId());
                model.addAttribute("userInfo", userinfo);
                //realAuth实名认证信息
                model.addAttribute("realAuth", realAuthService.get(userinfo.getRealAuthId()));
                //userFiles
                model.addAttribute("userFiles", userFileService.querByUserinfoId(userinfo.getId(), UserFile.STATE_PASS));

                //当前登录用户的account对象
                if (current!= null) {
                    //判断是否有登录
                    //判断借款人和当前登录的人时候为同一个
                    if (UserContext.getCurrent().getId().equals(bidRequest.getCreateUser().getId())) {
                        //当前登录的人就是借款人
                        model.addAttribute("self", true);
                    } else {
                        model.addAttribute("self", false);
                        model.addAttribute("account", accountService.getCurrent());
                    }
                } else {
                    //设置self
                    model.addAttribute("self", false);
                }
                returnPage = "borrow_info";
            }else{
                //体验标
                if (current!=null) {
                    model.addAttribute("expAccount", this.expAccountService.get(current.getId()));
                }
                returnPage = "exp_borrow_info";
            }
        }
        return returnPage;
    }

    /**
     * 投标操作
     */
    @RequestMapping("borrow_bid")
    @ResponseBody
    public JSONResult borrowBid(Long bidRequestId, BigDecimal amount) {
        JSONResult result = null;
        try {
            bidRequestService.borrowBid(bidRequestId, amount);
            result = new JSONResult();
        } catch (Exception e) {
            e.printStackTrace();
            result = new JSONResult(false, e.getMessage());
        }
        return result;
    }

    @RequestMapping("borrowBidReturn_list")
    public String returnMonery(@ModelAttribute("qo") PaymentScheduleQueryObject qo, Model model) {
        model.addAttribute("account", accountService.getCurrent());
        model.addAttribute("pageResult", paymentScheduleService.queryPage(qo));
        return "returnmoney_list";
    }

    @RequestMapping("returnMoney")
    @ResponseBody
    public JSONResult returnMoney(Long id) {
        JSONResult result = null;
        try {
            paymentScheduleService.returnMoney(id);
            result = new JSONResult();
        } catch (Exception e) {
            e.printStackTrace();
            result = new JSONResult(false, e.getMessage());
        }
        return result;
    }
}

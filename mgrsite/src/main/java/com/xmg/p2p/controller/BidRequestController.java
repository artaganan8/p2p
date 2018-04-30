package com.xmg.p2p.controller;

import com.xmg.p2p.base.domain.UserFile;
import com.xmg.p2p.base.domain.Userinfo;
import com.xmg.p2p.base.service.IRealAuthService;
import com.xmg.p2p.base.service.IUserFileService;
import com.xmg.p2p.base.service.IUserinfoService;
import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.base.util.JSONResult;
import com.xmg.p2p.business.domain.BidRequest;
import com.xmg.p2p.business.query.BidRequestQueryObject;
import com.xmg.p2p.business.service.IBidRequestAuditHistoryService;
import com.xmg.p2p.business.service.IBidRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by machao on 2017/9/8.
 */
@Controller
public class BidRequestController {
    @Autowired
    private IBidRequestService bidRequestService;
    @Autowired
    private IUserinfoService userinfoService;
    @Autowired
    private IBidRequestAuditHistoryService bidRequestAuditHistoryService;
    @Autowired
    private IRealAuthService realAuthService;
    @Autowired
    private IUserFileService userFileService;
    @RequestMapping("bidrequest_publishaudit_list")
    public String bidRequestAuthPage(@ModelAttribute("qo")BidRequestQueryObject qo,Model model){
        qo.setBidRequestState(BidConst.BIDREQUEST_STATE_PUBLISH_PENDING);//只查询等待状态
        model.addAttribute("pageResult", bidRequestService.queryPage(qo));
        return "bidRequest/publish_audit";
    }
    @RequestMapping("bidrequest_publishaudit")
    @ResponseBody
    public JSONResult publishAudit(Long id,String remark,int state){
        JSONResult result = null;
        try {
            bidRequestService.publishAudit(id,remark,state);
            result = new JSONResult();
        } catch (Exception e) {
            e.printStackTrace();
            result = new JSONResult(false, e.getMessage());
        }
        return result;
    }
    @RequestMapping("borrowInfo")
    public String borrowInfo(Long id,Model model){
        //bidRequest借款对象
        BidRequest bidRequest = bidRequestService.get(id);
        if(bidRequest!=null){
            model.addAttribute("bidRequest",bidRequest);
            //userinfo对象信息
            Userinfo userinfo = userinfoService.get(bidRequest.getCreateUser().getId());
            model.addAttribute("userInfo",userinfo);
            //audits审核历史记录
            model.addAttribute("audits",bidRequestAuditHistoryService.queryListByBidRequestId(bidRequest.getId()));
            //realAuth实名认证信息
            model.addAttribute("realAuth",realAuthService.get(userinfo.getRealAuthId()));
            //userFiles
            model.addAttribute("userFiles",userFileService.querByUserinfoId(userinfo.getId(), UserFile.STATE_PASS));
        }

        return "bidrequest/borrow_info";
    }
    @RequestMapping("borrow_info")
    public String ExpBorrowInfo(Long id,Model model){
        //bidRequest借款对象
        BidRequest bidRequest = bidRequestService.get(id);
        if(bidRequest!=null){
            model.addAttribute("bidRequest",bidRequest);
            //audits审核历史记录
            model.addAttribute("audits",bidRequestAuditHistoryService.queryListByBidRequestId(id));
        }
        return "expbidrequest/borrow_info";
    }
    @RequestMapping("bidrequest_audit1_list")
    public String bidrequestAudit1List(@ModelAttribute("qo")BidRequestQueryObject qo, Model model){
        qo.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1);//只查询等待状态
        model.addAttribute("pageResult", bidRequestService.queryPage(qo));
        return "bidRequest/audit1";
    }

    @RequestMapping("bidrequest_audit2_list")
    public String bidrequestAudit2List(@ModelAttribute("qo")BidRequestQueryObject qo, Model model){
        qo.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2);//只查询等待状态
        model.addAttribute("pageResult", bidRequestService.queryPage(qo));
        return "bidRequest/audit2";
    }

    @RequestMapping("bidrequest_audit1")
    @ResponseBody
    public JSONResult audit1(Long id,int state,String remark){
        JSONResult result = null;
        try {
            bidRequestService.audit1(id,state,remark);
            result = new JSONResult();
        } catch (Exception e) {
            e.printStackTrace();
            result = new JSONResult(false, e.getMessage());
        }
        return result;
    }
    @RequestMapping("bidrequest_audit2")
    @ResponseBody
    public JSONResult audit2(Long id,int state,String remark){
        JSONResult result = null;
        try {
            bidRequestService.audit2(id,state,remark);
            result = new JSONResult();
        } catch (Exception e) {
            e.printStackTrace();
            result = new JSONResult(false, e.getMessage());
        }
        return result;
    }
}

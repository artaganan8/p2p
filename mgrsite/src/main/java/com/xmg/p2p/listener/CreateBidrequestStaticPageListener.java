package com.xmg.p2p.listener;

import com.xmg.p2p.base.domain.UserFile;
import com.xmg.p2p.base.domain.Userinfo;
import com.xmg.p2p.base.service.IRealAuthService;
import com.xmg.p2p.base.service.IUserFileService;
import com.xmg.p2p.base.service.IUserinfoService;
import com.xmg.p2p.business.domain.BidRequest;
import com.xmg.p2p.business.event.BidrequestAudit2SuccessEvent;
import com.xmg.p2p.business.service.IBidRequestAuditHistoryService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by machao on 2017/9/24.
 */
@Component
public class CreateBidrequestStaticPageListener implements ApplicationListener<BidrequestAudit2SuccessEvent> {
    @Autowired
    private FreeMarkerConfig config;
    @Autowired
    private IUserinfoService userinfoService;
    @Autowired
    private IUserFileService userFileService;
    @Autowired
    private IRealAuthService realAuthService;
    @Autowired
    private IBidRequestAuditHistoryService bidRequestAuditHistoryService;
    @Autowired
    private ServletContext ctx;
    @Override
    public void onApplicationEvent(BidrequestAudit2SuccessEvent event) {
        //生成静态页面
        try{

        //1.得到模板对象
        Configuration configuration = config.getConfiguration();
            Template template = configuration.getTemplate("bidrequest/borrow_info.ftl");
            //2.填充模型
            BidRequest bidRequest = event.getBidRequest();
            Map<String,Object> model = new HashMap<>();
            if(bidRequest!=null){
                model.put("bidRequest",bidRequest);
                //userinfo对象信息
                Userinfo userinfo = userinfoService.get(bidRequest.getCreateUser().getId());
                model.put("userInfo",userinfo);
                //audits审核历史记录
                model.put("audits",bidRequestAuditHistoryService.queryListByBidRequestId(bidRequest.getId()));
                //realAuth实名认证信息
                model.put("realAuth",realAuthService.get(userinfo.getRealAuthId()));
                //userFiles
                model.put("userFiles",userFileService.querByUserinfoId(userinfo.getId(), UserFile.STATE_PASS));
            }

            //3.创建要生成的模板文件
            String targetFileName = UUID.randomUUID().toString() + ".html";
           File targetFile = new File(this.ctx.getRealPath("/static/"),targetFileName);
            //4.合成静态页面
            //template.process(model,new FileWriter(targetFile,"UTF-8"));
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(targetFile), "UTF-8");
            template.process(model,outputStreamWriter);
            //5.把合成好的bidrequest页面路径设置给br;
            bidRequest.setStaticUrl("/static/"+targetFileName);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

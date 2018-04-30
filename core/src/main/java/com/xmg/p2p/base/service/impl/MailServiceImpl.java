package com.xmg.p2p.base.service.impl;

import com.xmg.p2p.base.domain.MailVerify;
import com.xmg.p2p.base.domain.Userinfo;
import com.xmg.p2p.base.service.IMailService;
import com.xmg.p2p.base.service.IMailVerifyService;
import com.xmg.p2p.base.service.IUserinfoService;
import com.xmg.p2p.base.util.BidConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

/**
 * Created by machao on 2017/9/3.
 */
@Service
public class MailServiceImpl implements IMailService {
    @Value("${email.applicationURL}")
    private String applicationURL;
    @Value("${mail.host}")
    private String host;

    @Value("${mail.username}")
    private String username;

    @Value("${mail.password}")
    private String password;
    @Autowired
    private IUserinfoService userinfoService;
    @Autowired
    private IMailVerifyService mailVerifyService;
    @Override
    public void bindEmail(String email) {
        //判断当前用户是否绑定邮箱,如果已经绑定不在发邮件
        Userinfo userinfo = userinfoService.getCurrent();
        if (userinfo.getIsBindEmail()){
            throw new RuntimeException("您已经绑定邮箱,请不要重复绑定");
        }

        String uuid = UUID.randomUUID().toString();
        //拼接邮件发送内容
        StringBuilder stringBuilder = new StringBuilder(100);
        stringBuilder.append("感谢注册p2p账号,点击<a href='").append(applicationURL).append("/bindEmail.do?key=").append(uuid)
                .append("'>这里</a>完成认证,请尽快认证,邮件有效期为").append(BidConst.VERIFYEMAIL_VAILDATE_DAY).append("天");
        //发送邮件,
        System.out.println(stringBuilder);
        try {
            sendRealEmail(email,stringBuilder.toString());
            //把发送邮箱,当前用户的id,发送时间,uuid存储到数据库中
            MailVerify mailVerify = new MailVerify();
            mailVerify.setEmail(email);
            mailVerify.setSendTime(new Date());
            mailVerify.setUserinfoId(userinfo.getId());
            mailVerify.setUuid(uuid);
            mailVerifyService.save(mailVerify);
        } catch (Exception e) {
            throw  new RuntimeException(e.getMessage());
        }

    }
    //真实发送email的方法
    private void sendRealEmail(String email,String content)throws Exception{
        //创建sender发送对象
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        //设置服务器的地址
        mailSender.setHost(host);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        Properties prop = new Properties();
        prop.put("mail.smtp.auth","true");
        prop.put("mail.smtp.timeout","25000");
        mailSender.setJavaMailProperties(prop);
        //创建消息
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setTo(email);
        helper.setSubject("这是一封认证邮件");
        helper.setText(content,true);
        mailSender.send(mimeMessage);
    }
}

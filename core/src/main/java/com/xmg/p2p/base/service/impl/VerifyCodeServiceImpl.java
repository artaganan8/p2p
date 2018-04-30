package com.xmg.p2p.base.service.impl;

import com.xmg.p2p.base.service.IVerifyCodeService;
import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.base.util.DateUtil;
import com.xmg.p2p.base.util.UserContext;
import com.xmg.p2p.base.vo.VerifyCodeVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.UUID;

/**
 * Created by machao on 2017/9/2.
 */
@Service
public class VerifyCodeServiceImpl implements IVerifyCodeService {
    @Value("${sms.url}")
    private String smsUrl;
    @Value("${sms.username}")
    private String username;
    @Value("${sms.password}")
    private String password;
    @Value("${sms.apikey}")
    private String apikey;
    @Override
    public void sendVerifyCode(String phoneNumber) {
        //先获取session中的vo对象
        VerifyCodeVo verifyCodeVo = UserContext.getVerifyCode();
        //如果之前没有验证码,或者验证码过期,那么我们创建验证码
        if (verifyCodeVo == null || DateUtil.
                getDateBetween(verifyCodeVo.getSendTime(), new Date()) > BidConst.MESSAGE_VALUE_TIME * 60) {
            //生成验证码
            String verifyCode = UUID.randomUUID().toString().substring(0, 4);
            // 拼接POST请求的内容
            StringBuilder stringBuilder = new StringBuilder(100);
            stringBuilder.append("【马超】验证码是:").append(verifyCode).append("有效时间为:")
                    .append(BidConst.MESSAGE_VALUE_TIME).append("分钟,请在5分钟内使用");
            // 模拟打印
            System.out.println(stringBuilder);
            try {
                sendSms(phoneNumber,stringBuilder.toString());
                // 把手机号码,验证码,发送时间装配到VO中并保存到session
                VerifyCodeVo vo = new VerifyCodeVo();
                vo.setSendTime(new Date());
                vo.setPhoneNumber(phoneNumber);
                vo.setVerifyCode(verifyCode);
                UserContext.setVerifyCode(vo);
            } catch (Exception e) {
               throw new RuntimeException(e.getMessage());
            }
        }else {
            throw new RuntimeException("短信发送太频繁,请稍后再发送!");
        }
    }
    public void sendSms(String phoneNumber,String content) throws Exception {
        URL url = new  URL("http://utf8.api.smschinese.cn");
        //打开浏览器,创建链接
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        //请求方式为post
        urlConnection.setRequestMethod("POST");
        urlConnection.setDoOutput(true);
        //拼接发送的参数
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Uid=").append("machao123")
                .append("&Key=").append("15488a2255f4d81ca6f6")
                .append("&smsMob=").append(phoneNumber)
                .append("&smsText=").append(content);
        urlConnection.getOutputStream().write(stringBuilder.toString()
        .getBytes(Charset.forName("UTF-8")));
        //发送请求
        urlConnection.connect();
        String responseStr = StreamUtils.copyToString(urlConnection.getInputStream(), Charset.forName("UTF-8"));
        if(responseStr.indexOf("-")>0){
            throw new RuntimeException("发送短信失败!!!");
        }
    }
    /**
     * 判断验证码是否失效
     * @param phoneNumber
     * @param verityCode
     * @return
     */
    @Override
    public Boolean validate(String phoneNumber, String verityCode) {
        //获取session中的VerifyCodevo
        VerifyCodeVo verifyCodeVo = UserContext.getVerifyCode();
        if (verifyCodeVo==null||//之前没有没有发送验证码失效
                !verifyCodeVo.getVerifyCode().equals(verityCode)||//输入的验证码和session中的不一致,验证失败
                !verifyCodeVo.getPhoneNumber().equals(phoneNumber)||//输入的手机号和session中的不一致,验证失败
                //验证码过期
                DateUtil.getDateBetween(verifyCodeVo.getSendTime(),new Date())>BidConst.MESSAGE_VALUE_TIME*60*1000
                ){
            return false;
        }
        return true;
    }
}

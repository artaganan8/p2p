package com.xmg.p2p.base.service.impl;

import com.xmg.p2p.base.domain.MailVerify;
import com.xmg.p2p.base.domain.Userinfo;
import com.xmg.p2p.base.mapper.UserinfoMapper;
import com.xmg.p2p.base.service.IMailVerifyService;
import com.xmg.p2p.base.service.IUserinfoService;
import com.xmg.p2p.base.service.IVerifyCodeService;
import com.xmg.p2p.base.util.BidConst;
import com.xmg.p2p.base.util.BitStatesUtils;
import com.xmg.p2p.base.util.DateUtil;
import com.xmg.p2p.base.util.UserContext;
import org.apache.ibatis.jdbc.RuntimeSqlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserinfoServiceImpl implements IUserinfoService {
	@Autowired
	private UserinfoMapper userinfoMapper;
	@Autowired
	private IVerifyCodeService verityCodeService;
	@Autowired
	private IMailVerifyService mailVerifyService;
	@Override
	public int save(Userinfo userinfo) {
		return userinfoMapper.insert(userinfo);
	}

	@Override
	public int update(Userinfo userinfo) {
		int count = userinfoMapper.updateByPrimaryKey(userinfo);
		if(count <=0){
			throw new RuntimeException("乐观锁异常,userinfoId"+userinfo.getId());
		}
		return count;
	}

	@Override
	public Userinfo get(Long id) {
		return userinfoMapper.selectByPrimaryKey(id);
	}

	@Override
	public Userinfo getCurrent() {
		Long id = UserContext.getCurrent().getId();
		return this.get(id);
	}

	//绑定邮件的方法
	@Override
	public void bindEmail(String key) {
		//根据传入的key查询数据库,看是否有这条记录
		MailVerify mailVerify = mailVerifyService.selectByuuid(key);
		if(mailVerify==null){
			throw new RuntimeSqlException("地址有误,请重新发送邮件!");
		}
		if(DateUtil.getDateBetween(mailVerify.getSendTime(),new Date())> BidConst.VERIFYEMAIL_VAILDATE_DAY*24*60*60*1000){
			throw new RuntimeSqlException("邮件已经失效,请重新发送邮件");
		}
		//判断用户是否绑定邮箱,如果已经绑定,提示无需在此绑定
		Userinfo userinfo = this.get(mailVerify.getUserinfoId());
		if (userinfo.getIsBindEmail()){
			throw new RuntimeException("您已经绑定邮箱,请不要重复绑定");
		}
		userinfo.addState(BitStatesUtils.OP_BIND_EMAIL);
		userinfo.setEmail(mailVerify.getEmail());
		this.update(userinfo);
	}

	@Override
	public void bindPhone(String phoneNumber, String verityCode) {
		//对传入的验证码进行验证,
		Boolean vaild = verityCodeService.validate(phoneNumber,verityCode);
		if(vaild){
            Userinfo userinfo = this.getCurrent();
            if(userinfo.getIsBindPhone()){
                //判断用户是否已经手机认证
                throw new RuntimeException("您已经绑定了手机,请不要重复的绑定!");
            }
			//给用户添加手机认证的状态码,设置userinfo中phoneNumber,更新userinfo对象
		    userinfo.addState(BitStatesUtils.OP_BIND_PHONE);
            userinfo.setPhoneNumber(phoneNumber);
            this.update(userinfo);
		}else{
			throw new RuntimeException("验证码验证失败,请重新发送验证码");
		}
	}

	@Override
	public void basicInfoSave(Userinfo userinfo) {
        //获取当前登录用户的userinfo信息
        Userinfo current = this.getCurrent();
        //设置相关属性
        current.setEducationBackground(userinfo.getEducationBackground());
        current.setHouseCondition(userinfo.getHouseCondition());
        current.setIncomeGrade(userinfo.getIncomeGrade());
        current.setKidCount(userinfo.getKidCount());
        current.setMarriage(userinfo.getMarriage());
        //如果该用户中没有基本认证,给该userinfo添加基本认证信息状态
        if (!current.getIsBasicInfo()){
            current.addState(BitStatesUtils.OP_BASIC_INFO);
        }
        //更新对象
        this.update(current);
    }


}

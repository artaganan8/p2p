package com.xmg.p2p.base.service.impl;

import com.xmg.p2p.base.domain.RealAuth;
import com.xmg.p2p.base.domain.Userinfo;
import com.xmg.p2p.base.even.RealAuthSuccessEvent;
import com.xmg.p2p.base.mapper.RealAuthMapper;
import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.base.query.RealAuthQueryObject;
import com.xmg.p2p.base.service.IRealAuthService;
import com.xmg.p2p.base.service.IUserinfoService;
import com.xmg.p2p.base.util.BitStatesUtils;
import com.xmg.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RealAuthServiceImpl implements IRealAuthService {
	@Autowired
	private RealAuthMapper realAuthMapper;
	@Autowired
	private IUserinfoService userinfoService;

    @Autowired
    private ApplicationContext ctx;


    @Override
    public void save(RealAuth realAuth) {
        realAuthMapper.insert(realAuth);
    }

    @Override
	public RealAuth get(Long realAuthId) {
		return realAuthMapper.selectByPrimaryKey(realAuthId);
	}

	@Override
	public void realAuthSave(RealAuth realAuth) {
		//获取userinfo判断是否已经实名认证
        Userinfo userinfo = userinfoService.getCurrent();
        if (!userinfo.getIsRealAuth()){
            RealAuth ra = new RealAuth();
            ra.setRealName(realAuth.getRealName());
            ra.setAddress(realAuth.getAddress());
            ra.setApplier(UserContext.getCurrent());
            ra.setApplyTime(new Date());
            ra.setBornDate(realAuth.getBornDate());
            ra.setIdNumber(realAuth.getIdNumber());
            ra.setImage1(realAuth.getImage1());
            ra.setImage2(realAuth.getImage2());
            ra.setSex(realAuth.getSex());
            ra.setState(RealAuth.STATE_NORMAL);
            this.save(ra);
            //给userinfo对象设置realAuthId,更新userinfo对象
            userinfo.setRealAuthId(ra.getId());
            userinfoService.update(userinfo);
        }
	}

    @Override
    public PageResult queryPage(RealAuthQueryObject qo) {
        Long count = realAuthMapper.queryPageCount(qo);
        if (count==0){
        	return PageResult.empty(qo.getPageSize());
        }
        List<RealAuth> list = realAuthMapper.queryPageData(qo);
        return new PageResult(list,count.intValue(),qo.getCurrentPage(),qo.getPageSize());
    }

    @Override
    public void realAuthAudit(Long id, int state, String remark) {
        //获取认证信息
        RealAuth realAuth = this.get(id);
        if (realAuth!=null){
            //判断申请人是否已经实名认证
            Userinfo applier = userinfoService.get(realAuth.getApplier().getId());
            if(!applier.getIsRealAuth()){
                //设置审核人,审核时间,审核意见,审核状态
                realAuth.setAuditor(UserContext.getCurrent());
                realAuth.setAudiTime(new Date());
                realAuth.setRemark(remark);
                if(state==RealAuth.STATE_PASS){
                    //审核通过,给审核人添加实名认证信息,给申请人设置realName和idNumber
                    realAuth.setState(RealAuth.STATE_PASS);
                    applier.addState(BitStatesUtils.OP_REAL_AUTH);
                    applier.setIdNumber(realAuth.getIdNumber());
                    applier.setRealName(realAuth.getRealName());
                //实名认证成功发送短信和邮件,通过容器发布事件对象
                    ctx.publishEvent(new RealAuthSuccessEvent(this,realAuth));


                }else {
                    //审核失败,把userinfo中的lAuthId设置为null
                    realAuth.setState(RealAuth.STATE_REJECT);
                    applier.setRealAuthId(null);
                }
                this.update(realAuth);
                userinfoService.update(applier);
            }

        }
    }

    public int update(RealAuth realAuth) {
        int count = realAuthMapper.updateByPrimaryKey(realAuth);
        if(count <=0){
            throw new RuntimeException("乐观锁异常,userinfoId"+realAuth.getId());
        }
        return count;
    }
}

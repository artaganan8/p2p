package com.xmg.p2p.base.service.impl;

import com.xmg.p2p.base.domain.Logininfo;
import com.xmg.p2p.base.domain.Userinfo;
import com.xmg.p2p.base.domain.VedioAuth;
import com.xmg.p2p.base.mapper.VedioAuthMapper;
import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.base.query.VedioAuthQueryObject;
import com.xmg.p2p.base.service.IUserinfoService;
import com.xmg.p2p.base.service.IVedioAuthService;
import com.xmg.p2p.base.util.BitStatesUtils;
import com.xmg.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class VedioAuthServiceImpl implements IVedioAuthService {
	@Autowired
	private VedioAuthMapper vedioAuthMapper;
	@Autowired
	private IUserinfoService userinfoService;
	@Override
	public int save(VedioAuth vedioAuth) {
		return vedioAuthMapper.insert(vedioAuth);
	}

	@Override
	public PageResult queryPage(VedioAuthQueryObject qo) {
		Long count = vedioAuthMapper.queryPageCount(qo);
		if (count==0){
			return PageResult.empty(qo.getPageSize());
		}
		List<VedioAuth> list = vedioAuthMapper.queryPageData(qo);
		return new PageResult(list,count.intValue(),qo.getCurrentPage(),qo.getPageSize());
	}

	@Override
	public void vedioAuthAudit(Long loginInfoValue, int state, String remark) {
		//根据id查询对应的用户对象,判断是否已经进行视频认证
        Userinfo userinfo = userinfoService.get(loginInfoValue);
        if (userinfo!=null){
            //创建一个视频认证对象
            //设置相关的参数
            VedioAuth va = new VedioAuth();
            Logininfo applier = new Logininfo();
            applier.setId(loginInfoValue);
            va.setApplier(applier);
            va.setApplyTime(new Date());
            va.setAuditor(UserContext.getCurrent());
            va.setAudiTime(new Date());
            va.setRemark(remark);
            if (state==VedioAuth.STATE_PASS){
                //如果审核通过,给用户添加视频认证的状态
                va.setState(VedioAuth.STATE_PASS);
                userinfo.addState(BitStatesUtils.OP_VEDIO_AUTH);
            }else {
                va.setState(VedioAuth.STATE_REJECT);
            }
            //如果审核失败
            //保存视频审核对象
            this.save(va);
            userinfoService.update(userinfo);
        }
	}
}

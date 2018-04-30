package com.xmg.p2p.base.service.impl;

import com.xmg.p2p.base.domain.SystemDictionaryItem;
import com.xmg.p2p.base.domain.UserFile;
import com.xmg.p2p.base.domain.Userinfo;
import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.base.query.UserFileQueryObject;
import com.xmg.p2p.base.service.IUserinfoService;
import com.xmg.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xmg.p2p.base.mapper.UserFileMapper;
import com.xmg.p2p.base.service.IUserFileService;

import java.util.Date;
import java.util.List;

@Service
public class UserFileServiceImpl implements IUserFileService {
	@Autowired
	private UserFileMapper userFileMapper;
    @Autowired
    private IUserinfoService userinfoService;
	@Override
	public int save(UserFile userFile) {
		return userFileMapper.insert(userFile);
	}

	@Override
	public int update(UserFile userFile) {
		return userFileMapper.updateByPrimaryKey(userFile);
	}

	@Override
	public UserFile get(Long id) {
		return userFileMapper.selectByPrimaryKey(id);
	}

	/**
	 * 上传风控材料
	 * @param s
	 */
	@Override
	public void apply(String s) {
		//创建风控材料对象
        UserFile userFile = new UserFile();
        userFile.setApplier(UserContext.getCurrent());
        userFile.setApplyTime(new Date());
        userFile.setImage(s);
        userFile.setState(UserFile.STATE_NORMAL);
        //保存入库
        try {
            this.save(userFile);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public List<UserFile> queryFileTypeList(boolean b) {
        return userFileMapper.queryFileTypeList(b,UserContext.getCurrent().getId());
    }

    @Override
    public void choiceFileType(Long[] ids, Long[] fileTypes) {
        if(ids!=null&&fileTypes!=null&&ids.length==fileTypes.length){
            UserFile uf= null;
            SystemDictionaryItem fileType = null;
            for (int i = 0 ;i<ids.length ; i++){
                uf = this.get(ids[i]);
                if(uf.getApplier().getId().equals(UserContext.getCurrent().getId())){
                    fileType = new SystemDictionaryItem();
                    fileType.setId(fileTypes[i]);
                    uf.setFileType(fileType);
                    this.update(uf);
                }
            }
        }
    }

    @Override
    public PageResult queryPage(UserFileQueryObject qo) {
       Long count = userFileMapper.queryPageCount(qo);
       if (count==0){
       	return PageResult.empty(qo.getPageSize());
       }
       List<UserFile> list = userFileMapper.queryPageData(qo);
       return new PageResult(list,count.intValue(),qo.getCurrentPage(),qo.getPageSize());
    }

    /**
     * 该方法用于风控材料审核操作
     * @param id
     * @param state
     * @param score
     * @param remark
     */
    @Override
    public void userAuthAudit(Long id, int state, int score, String remark) {
        //根据id获取userFile对象
        UserFile userFile = this.get(id);
        //当userFile对象不为空,且该对象没有进行审核的时候,我们进行审核操作
        if(userFile!=null&&userFile.getState()==UserFile.STATE_NORMAL){
            //设置相关属性
            userFile.setAuditor(UserContext.getCurrent());
            userFile.setAudiTime(new Date());
            userFile.setRemark(remark);
            userFile.setScore(score);
            //判断当前的审核状态为审核通过还是审核失败
            if(state==UserFile.STATE_PASS){
                //设置审核状态,更新userinfo中的风控分数
                userFile.setState(UserFile.STATE_PASS);
                Userinfo applier = userinfoService.get(userFile.getApplier().getId());
                applier.setScore(applier.getScore()+score);
                userinfoService.update(applier);
            }else{
                //审核失败,设置审核状态
                userFile.setState(UserFile.STATE_REJECT);
            }
            this.update(userFile);
        }
    }

    @Override
    public List<UserFile> querByUserinfoId(Long userinfoId, int statePass) {
        return userFileMapper.querByUserinfoId(userinfoId,statePass);
    }
}

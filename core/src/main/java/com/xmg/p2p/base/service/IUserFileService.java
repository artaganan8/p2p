package com.xmg.p2p.base.service;

import com.xmg.p2p.base.domain.UserFile;
import com.xmg.p2p.base.page.PageResult;
import com.xmg.p2p.base.query.UserFileQueryObject;

import java.util.List;

public interface IUserFileService {
    int save(UserFile userFile);
    int update(UserFile userFile);
    UserFile get(Long id);


    void apply(String s);

    List<UserFile> queryFileTypeList(boolean b);

    void choiceFileType(Long[] ids, Long[] fileTypes);

    PageResult queryPage(UserFileQueryObject qo);

    void userAuthAudit(Long id, int state, int score, String remark);

    List<UserFile> querByUserinfoId(Long id, int statePass);
}

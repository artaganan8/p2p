package com.xmg.p2p.base.mapper;

import com.xmg.p2p.base.domain.UserFile;
import com.xmg.p2p.base.query.QueryObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserFileMapper {
    int insert(UserFile record);
    UserFile selectByPrimaryKey(Long id);
    int updateByPrimaryKey(UserFile record);
	Long queryPageCount(QueryObject qo);
	List<UserFile> queryPageData(QueryObject qo);

    List<UserFile> queryFileTypeList(@Param("b") boolean b, @Param("id")Long id);

    List<UserFile> querByUserinfoId(@Param("userinfoId") Long userinfoId, @Param("statePass") int statePass);
}
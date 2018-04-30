package com.xmg.p2p.controller;

import com.xmg.p2p.base.domain.UserFile;
import com.xmg.p2p.base.service.ISystemDictionaryItemService;
import com.xmg.p2p.base.service.IUserFileService;
import com.xmg.p2p.base.util.JSONResult;
import com.xmg.p2p.util.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by machao on 2017/9/7.
 */
@Controller
public class UserFileController {
    @Autowired
    private IUserFileService userFileService;
    @Autowired
    private ISystemDictionaryItemService systemDictionaryItemService;
    @Autowired
    private ServletContext servletContext;
    @RequestMapping("userFile")
    public String userFile(Model model, HttpSession session){
        //查询是否存在未选择类型的认证材料
        List<UserFile> userFiles = userFileService.queryFileTypeList(false);
        //解决火狐浏览器不同会话的问题
        model.addAttribute("jsessionid", session.getId());
        if(userFiles.size()==0){
            model.addAttribute("userFiles",userFiles);
            return "userFiles";
        }else{
            model.addAttribute("userFiles",userFiles);
            model.addAttribute("fileTypes",systemDictionaryItemService.listBySn("userFileType"));
            return "userFiles_commit";
        }
    }
    @RequestMapping("userFileUpload")
    @ResponseBody
    public String userFileUpload(MultipartFile file){
        String dir = "/upload/";
        String fileName = UploadUtil.upload(file,servletContext.getRealPath(dir));
        userFileService.apply(dir+fileName);
        return dir + fileName;
    }
    @RequestMapping("userFile_selectType")
    @ResponseBody
    public JSONResult userFileSelectType(Long[] id,Long[] fileType){
        JSONResult result = null;
        try {
            userFileService.choiceFileType(id,fileType);
            result = new JSONResult();
        } catch (Exception e) {
            e.printStackTrace();
            result = new JSONResult(false, e.getMessage());
        }
        return result;

    }
}

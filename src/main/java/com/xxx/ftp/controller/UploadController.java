package com.xxx.ftp.controller;

import com.xxx.ftp.model.DbUser;
import com.xxx.ftp.service.FtpService;
import com.xxx.ftp.service.UploadService;
import com.xxx.ftp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Author JN
 * @Date 2020/5/1 16:24
 * @Version 1.0
 * @Description
 **/
@Controller
public class UploadController {

    @Autowired
    private FtpService ftpService;
    @Autowired
    private UserService userService;

    @RequestMapping("/turnAjaxPage")
    public String turnAjaxPage() {
        return "upload_ajax";
    }

    @RequestMapping("/ajaxUpload")
    @ResponseBody
    public Map<String, Object> ajaxUpload(MultipartFile file,HttpServletRequest request) {
        return ftpService.upload(file,request);
    }

    @RequestMapping("/upload")
    public String upload(MultipartFile file, HttpSession session, ModelMap modelMap,HttpServletRequest request) {
        Map<String, Object> resultMap = ftpService.upload(file,request);
        // 思考问题！！！ 返回值是Boolean类型，但是需要获取图片的路径信息以及图片的名称，需要存入数据库
        if(!(Boolean)resultMap.get("result")) {
            // 说明上传失败！！需要跳转到错误页面
            return "error";
        } else {
            String headPic =  (String)resultMap.get("picPath");
            if(!"".equals(headPic)) {
                // 说明文件上传成功
                // 文件上传成功后，需要更新进用户的数据中
                // 获取用户的id信息
                DbUser user = (DbUser) session.getAttribute("user");
                Long id = user.getId();
                // 根据用户的id进行更新！！！
                DbUser du = new DbUser();
                du.setId(id);
                du.setHeadPic(headPic);
                int updateResult = userService.updateHeadPicById(du);
                if (updateResult > 0) {
                    // 说明更新成功
                    // 需要跳转到显示页面
                    modelMap.addAttribute("headPic", headPic);
                    return "show_pic";
                }

            }
        }

        return "error";
    }
}

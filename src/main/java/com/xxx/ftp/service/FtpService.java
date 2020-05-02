package com.xxx.ftp.service;

import com.xxx.ftp.config.FtpPropertiesConfig;
import com.xxx.ftp.model.DbUser;
import com.xxx.ftp.utils.FileNameUtil;
import com.xxx.ftp.utils.FtpUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author JN
 * @Date 2020/5/1 20:38
 * @Version 1.0
 * @Description
 **/
@Service
public class FtpService {

    @Autowired
    private FtpPropertiesConfig ftpPropertiesConfig;


    public Map<String, Object> upload(MultipartFile file, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        // 1.获取文件的原始名称
        String oldFileName = file.getOriginalFilename();
        HttpSession session = request.getSession(false);
        DbUser user = (DbUser)session.getAttribute("user");
        // 2.创建新的文件名称
        String newFileName = FileNameUtil.getFileName(user.getId());
        // 3.截取原始名称的后缀
        String substring = oldFileName.substring(oldFileName.lastIndexOf("."));
        // 4.把截取出的后缀拼接到新的文件名中
        newFileName = newFileName + substring;
        // 5.创建文件目标的目录
        String filePath = new DateTime().toString("yyyy/MM/dd");// 2019/7/30
        // 6.使用工具类进行连接Ftp和上传功能
        // 如何获取输入流？  file.getInputStream();
        // 所有的异常信息尽可能的精确！！！！
        try {
            Boolean uploadResult = FtpUtil.uploadFile(ftpPropertiesConfig.getHost(), Integer.parseInt(ftpPropertiesConfig.getPort()),
                    ftpPropertiesConfig.getUsername(), ftpPropertiesConfig.getPassword(), ftpPropertiesConfig.getBasePath(),
                    filePath, newFileName, file.getInputStream());
            String picPath = ftpPropertiesConfig.getHttpPath()+"/"+filePath+"/"+newFileName;
            resultMap.put("result", uploadResult);
            resultMap.put("picPath", picPath);
        } catch (IOException e) {
            resultMap.put("result", false);
            e.printStackTrace();
        }
        return resultMap;
    }
}

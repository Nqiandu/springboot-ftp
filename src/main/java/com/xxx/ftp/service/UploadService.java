package com.xxx.ftp.service;

import com.xxx.ftp.config.FtpPropertiesConfig;
import com.xxx.ftp.mapper.DbUserMapper;
import com.xxx.ftp.model.DbUser;
import com.xxx.ftp.utils.FileNameUtil;
import com.xxx.ftp.utils.FtpUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author JN
 * @Date 2020/5/1 16:55
 * @Version 1.0
 * @Description
 **/
@Service
public class UploadService {

    @Autowired
    private FtpPropertiesConfig ftpPropertiesConfig;

    @Autowired
    private DbUserMapper userMapper;

    public Map<String, Object> uploadHead(MultipartFile file, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        // 1.获取原始文件名(需要获取文件的后缀名)
        String oldFileName = file.getOriginalFilename();
        try {
            // 2.生成新的文件名
            // id + 随机数 + 时间戳 作为新的文件名
            // 因为需要id，所以必须要从session中获取
            // getSession():是需要传参的，参数是Boolean，默认值为true
            // true和false有什么区别？
            // 如果传入值为true，在获取系统session的时候，如果session为null，也就是说系统中没有session则会默认自动创建一个
            // 如果传入的值为false，不会创建，直接返回为null
            HttpSession session = request.getSession(false);
            DbUser user = (DbUser)session.getAttribute("user");
            String newFileName = FileNameUtil.getFileName(user.getId());
            // 3.获取原始文件的后缀名
            String substring = oldFileName.substring(oldFileName.lastIndexOf("."));
            // 4.完成新的文件名
            newFileName = newFileName + substring;
            // 5.生成文件上传路径
            String filePath = new DateTime().toString("yyyy/MM/dd");
            // 6.调用上传工具类
            System.out.println(ftpPropertiesConfig);
            System.out.println(ftpPropertiesConfig.getHost());
            Boolean ifSuccess = FtpUtil.uploadFile(ftpPropertiesConfig.getHost(), Integer.parseInt(ftpPropertiesConfig.getPort()), ftpPropertiesConfig.getUsername(),
                    ftpPropertiesConfig.getPassword(), ftpPropertiesConfig.getBasePath(), filePath, newFileName, file.getInputStream());
            // 7.判断是否上传成功
            if(ifSuccess) {
                // 说明上传成功，把文件的路径和文件新的名称以及文件的原始名称更新进数据库
                // 通过id进行更新，也就是说只需要获取到headPicPath,newFilename,originalName就可以了
                // headPicPath:http://ip地址/2019/10/21/文件的新名称
                // http://ip地址:ftpProperties.getHttpPath()
                // 2019/10/21:filePath
                // 文件的新名称:newFileName;
                String headPicPath = ftpPropertiesConfig.getHttpPath() + "/" + filePath + "/" + newFileName;
                DbUser u = new DbUser();
                u.setId(user.getId());
                u.setHeadPic(headPicPath);
                u.setFilName(newFileName);
                u.setFileNameOriginal(oldFileName);
                Integer updateResult = userMapper.updateHeadPicById(u);
                if(updateResult > 0) {
                    // 说明更新成功，需要把头像图片显示在页面上，也就是说必须要把headPicPath返回给controller
                    resultMap.put("code", "200");
                    resultMap.put("data", headPicPath);
                } else {
                    resultMap.put("code", "404");
                    resultMap.put("msg", "头像保存失败");
                }
            } else {
                resultMap.put("code", "404");
                resultMap.put("msg", "头像上传失败");
            }
        } catch (Exception e) {
            resultMap.put("code", "404");
            resultMap.put("msg", "用户获取失败");
            e.printStackTrace();
        }
        return resultMap;
    }
}

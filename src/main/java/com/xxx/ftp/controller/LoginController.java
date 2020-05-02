package com.xxx.ftp.controller;

import com.xxx.ftp.model.DbUser;
import com.xxx.ftp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Author JN
 * @Date 2020/5/1 16:23
 * @Version 1.0
 * @Description
 **/
@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String turnLoginPage() {
        return "login";
    }

    @RequestMapping("/login")
    public String login(DbUser dbUser, HttpSession session) {
        DbUser user = userService.login(dbUser, session);
        if(null != user) {
            // 说明登录成功，需要跳转到上传页面中
            return "upload_ajax";
        }
        return "error";
    }
}

package com.xxx.ftp.service;

import com.xxx.ftp.mapper.DbUserMapper;
import com.xxx.ftp.model.DbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author JN
 * @Date 2020/5/1 16:20
 * @Version 1.0
 * @Description
 **/
@Service
public class UserService {

    @Autowired
    private DbUserMapper dbUserMapper;

    /**
     * @Author JN
     * @Description 注册
     * @Date 17:02 2020/5/1
     * @Param [user]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    public Map<String, Object> registerUser(DbUser user) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        int insertResult = dbUserMapper.insert(user);
        if(insertResult > 0) {
            resultMap.put("code", "200");
            resultMap.put("msg", "注册成功");
        } else {
            resultMap.put("code", "400");
            resultMap.put("msg", "注册失败");
        }
        return resultMap;
    }
    /**
     * @Author JN
     * @Description 登陆
     * @Date 17:05 2020/5/1
     * @Param [user, request]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    public DbUser login(DbUser dbUser, HttpSession session) {
        DbUser user = dbUserMapper.selectUserByUsernamePassword(dbUser);
        if(null != user && 0L != user.getId()) {
            session.setAttribute("user", user);
            return user;
        }
        return null;
    }

    public int updateHeadPicById(DbUser dbUser) {
        return dbUserMapper.updateHeadPicById(dbUser);
    }
}

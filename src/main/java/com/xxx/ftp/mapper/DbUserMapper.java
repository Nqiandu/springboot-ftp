package com.xxx.ftp.mapper;

import com.xxx.ftp.model.DbUser;

import java.util.List;

public interface DbUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DbUser record);

    DbUser selectByPrimaryKey(Long id);

    List<DbUser> selectAll();

    int updateByPrimaryKey(DbUser record);

    DbUser selectUserByUsernamePassword(DbUser dbUser);

    int updateHeadPicById(DbUser dbUser);
}
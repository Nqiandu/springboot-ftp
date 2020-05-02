package com.xxx.ftp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author JN
 * @Date 2020/5/1 15:57
 * @Version 1.0
 * @Description
 **/
@SpringBootApplication
@MapperScan("com.xxx.ftp.mapper")
public class ApplicationRun {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationRun.class, args);
    }

}


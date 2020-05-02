package com.xxx.ftp.utils;

import java.util.Random;

/**
 * @Author JN
 * @Date 2020/5/1 15:53
 * @Version 1.0
 * @Description
 *      文件名工具类
 **/
public class FileNameUtil {

    public static String getFileName(Long id) {
        // 1.获取当前系统的时间毫秒数
        long millis = System.currentTimeMillis();
        // 2.生成随机数(0-999之间进行随机)
        Random random = new Random();
        int randomNum = random.nextInt(999);
        // 3.需要进行占位符(需要把当前系统时间的毫秒数和随机数整合在一起)
        // %:占位符   03:三位(如果不足三位往前补0)  d:数字
        String format = String.format("%03d", randomNum);
        // 5.生成最终的文件名
        String fileName = id + millis + format;
        // 6.返回文件名
        return fileName;
    }

    public static void main(String[] args) {
        String fileName = FileNameUtil.getFileName(1L);
        System.out.println(fileName);
    }
}

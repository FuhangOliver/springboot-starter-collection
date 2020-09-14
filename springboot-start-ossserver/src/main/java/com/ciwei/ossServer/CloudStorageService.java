package com.ciwei.ossServer;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.Date;

/**
 * @author fuhang
 * @description: 云存储(支持七牛 、 阿里云 、 腾讯云 、 又拍云)
 * @date 2020/8/29 16:37
 */
public abstract class CloudStorageService {
    /**
     * 云存储配置信息
     */
    CloudStorageConfig config;

    /**
     * 文件路径
     *
     * @param prefix 前缀
     * @return 返回上传路径
     */
    /**
     * 默认不指定key的情况下，以文件内容的hash值作为文件名
     *
     * @param file   文件名
     * @param prefix 路径前缀
     * @return String
     */
    public String getPath(File file, String prefix) {
        String path = new StringBuilder().append(DateUtil.format(new Date(), "yyyyMMdd")).append("/").append(DateUtil.format(new Date(), "HHmmssS")).toString();
        if (StrUtil.isNotEmpty(prefix)) {
            path = new StringBuilder().append(prefix).append("/").append(path).toString();
        }
        String fileName = file.getName();
        path = new StringBuilder().append(path).append("/").append(SecureUtil.md5(file)).append(".").append(FileUtil.getSuffix(fileName)).toString();
        return path;
    }

    public String getPath(MultipartFile file, String prefix) {
        String path = new StringBuilder().append(DateUtil.format(new Date(), "yyyyMMdd")).append("/").append(DateUtil.format(new Date(), "HHmmssS")).toString();
        if (StrUtil.isNotEmpty(prefix)) {
            path = new StringBuilder().append(prefix).append("/").append(path).toString();
        }
        String fileName = file.getOriginalFilename();
        path = new StringBuilder().append(path).append("/").append(SecureUtil.md5(file.getOriginalFilename())).append(".").append(FileUtil.getSuffix(fileName)).toString();
        return path;
    }

    /**
     * 文件上传
     *
     * @param path 文件路径
     * @return 返回文件地址路径
     */
    public abstract String getUrl(String path);

    /**
     * 文件上传
     *
     * @param file 文件
     * @return 返回文件地址路径
     */
    public abstract String upload(File file);

    /**
     * 文件上传
     *
     * @param file 文件
     * @return 返回文件地址路径
     */
    public abstract String upload(MultipartFile file);

    /**
     * 文件上传
     *
     * @param data 文件字节数组
     * @param path 文件路径，包含文件名
     * @return 返回文件地址路径
     */
    public abstract String upload(byte[] data, String path);

    /**
     * 文件上传
     *
     * @param inputStream 字节流
     * @param path        文件路径，包含文件名
     * @return 返回文件地址路径
     */
    public abstract String upload(InputStream inputStream, String path);

    public void checkSize(long maxSize, long size) {
        if (size > (maxSize * 1024 * 1024)) {
            throw new RuntimeException("文件超出规定大小");
        }
    }
}

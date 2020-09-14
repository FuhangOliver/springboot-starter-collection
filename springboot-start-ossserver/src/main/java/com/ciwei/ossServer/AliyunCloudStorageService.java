package com.ciwei.ossServer;

import cn.hutool.core.io.FileUtil;
import com.aliyun.oss.OSSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author fuhang
 * @description: 阿里云存储
 * @date 2020/8/29 16:37
 */
public class AliyunCloudStorageService extends CloudStorageService {
    private OSSClient client;

    @Autowired
    public AliyunCloudStorageService(CloudStorageConfig config) {
        this.config = config;
        //初始化
        //init();
    }

    private void init() {
        client = new OSSClient(config.getAliyunEndPoint(), config.getAliyunAccessKeyId(),
                config.getAliyunAccessKeySecret());
    }

    @Override
    public String getUrl(String path) {
        return new StringBuilder().append(config.getAliyunDomain()).append("/").append(path).toString();
    }

    @Override
    public String upload(File file) {
        this.checkSize(config.getMaxFileSize(), file.length());
        return upload(FileUtil.getInputStream(file), getPath(file, config.getAliyunPrefix()));
    }

    @Override
    public String upload(MultipartFile file) {
        try {
            this.checkSize(config.getMaxFileSize(), file.getSize());
            return upload(file.getInputStream(), this.getPath(file, config.getAliyunPrefix()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String upload(byte[] data, String path) {
        return upload(new ByteArrayInputStream(data), path);
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        try {
            this.init();
            client.putObject(config.getAliyunBucketName(), path, inputStream);
        } catch (Exception e) {
            throw new RuntimeException("上传文件失败，请检查配置信息", e);
        }
        return path;
    }
}

package com.ciwei.ossServer;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author fuhang
 * @description: 七牛云存储
 * @date 2020/8/29 16:37
 */
public class QiniuCloudStorageService extends CloudStorageService {

    private UploadManager uploadManager;
    private String token;

    @Autowired
    public QiniuCloudStorageService(CloudStorageConfig config) {
        this.config = config;
        //初始化
        //init();
    }

    private void init() {
        uploadManager = new UploadManager(new Configuration(Region.autoRegion()));
        token = Auth.create(config.getQiniuAccessKey(), config.getQiniuSecretKey()).
                uploadToken(config.getQiniuBucketName());
    }

    @Override
    public String getUrl(String path) {
        return new StringBuilder().append(config.getQiniuDomain()).append("/").append(path).toString();
    }

    @Override
    public String upload(File file) {
        this.checkSize(config.getMaxFileSize(), file.length());
        return upload(FileUtil.getInputStream(file), getPath(file, config.getQiniuPrefix()));
    }

    @Override
    public String upload(MultipartFile file) {
        try {
            this.checkSize(config.getMaxFileSize(), file.getSize());
            return upload(file.getInputStream(), this.getPath(file, config.getQiniuPrefix()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String upload(byte[] data, String path) {
        try {
            this.init();
            Response res = uploadManager.put(data, path, token);
            if (!res.isOK()) {
                throw new RuntimeException("上传七牛出错：" + res.toString());
            }
        } catch (Exception e) {
            throw new RuntimeException("上传文件失败，请核对七牛配置信息", e);
        }
        return path;
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        byte[] data = IoUtil.readBytes(inputStream);
        return this.upload(data, path);
    }
}

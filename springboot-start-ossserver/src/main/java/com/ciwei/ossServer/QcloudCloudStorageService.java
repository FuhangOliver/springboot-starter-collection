package com.ciwei.ossServer;

import cn.hutool.core.io.FileUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.UploadResult;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.Upload;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author fuhang
 * @description: 腾讯云存储
 * @date 2020/8/29 16:37
 */
public class QcloudCloudStorageService extends CloudStorageService {
    private COSClient client;

    @Autowired
    public QcloudCloudStorageService(CloudStorageConfig config) {
        this.config = config;
        // 初始化
        //init();
    }

    private void init() {
        COSCredentials credentials = new BasicCOSCredentials(config.getQcloudSecretId(), config.getQcloudSecretKey());
        // 初始化客户端配置 设置bucket所在的区域，华南：gz 华北：tj 华东：sh
        ClientConfig clientConfig = new ClientConfig(new Region(config.getQcloudRegion()));
        client = new COSClient(credentials, clientConfig);

    }

    @Override
    public String getUrl(String path) {
        return new StringBuilder().append(config.getQcloudDomain()).append(path).toString();
    }

    @Override
    public String upload(File file) {
        this.checkSize(config.getMaxFileSize(), file.length());
        return upload(FileUtil.getInputStream(file), getPath(file, config.getQcloudPrefix()));
    }

    @Override
    public String upload(MultipartFile file) {
        try {
            this.checkSize(config.getMaxFileSize(), file.getSize());
            return upload(file.getInputStream(), this.getPath(file, config.getQcloudPrefix()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        // 腾讯云必需要以"/"开头
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        ExecutorService threadPool = Executors.newFixedThreadPool(32);
        // 传入一个 threadpool, 若不传入线程池, 默认 TransferManager 中会生成一个单线程的线程池。
        TransferManager transferManager = new TransferManager(client, threadPool);
        // .....(提交上传下载请求, 如下文所属)
        // 创建上传Object的Metadata
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentType(ContentType.APPLICATION_OCTET_STREAM.getMimeType());
        try {
            // 这里有个风险，因为available返回的是int类型，有长度限制，如果文件大，这个不适用。
            meta.setContentLength(inputStream.available());
        } catch (IOException e) {
            throw new RuntimeException("文件流错误，" + e.getMessage());
        }
        PutObjectRequest putObjectRequest = new PutObjectRequest(config.getQcloudBucketName(),
                path, inputStream, meta);
        // 本地文件上传
        Upload upload = transferManager.upload(putObjectRequest);
        // 等待传输结束（如果想同步的等待上传结束，则调用 waitForCompletion）
        try {
            this.init();
            UploadResult uploadResult = upload.waitForUploadResult();
        } catch (CosServiceException e) {
            throw new RuntimeException("服务异常，" + e.getErrorMessage());
        } catch (CosClientException e) {
            throw new RuntimeException("客户端异常，" + e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException("系统异常，" + e.getMessage());
        } finally {
            // 关闭 TransferManger
            transferManager.shutdownNow();
        }
        // 例如：https://paddy-1256559626.cosbj.myqcloud.com/images/demo/20180426/0034397501f917.png
        return path;
    }

    @Override
    @Deprecated
    public String upload(byte[] data, String path) {
        // 这个方法在腾讯新版sdk中已经弃用
        return null;
    }

    public void delete(String path) {
        client.deleteObject(config.getQcloudBucketName(), path);
    }
}

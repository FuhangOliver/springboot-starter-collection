package com.ciwei.ossServer;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.stream.FileImageOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author fuhang
 * @description: 服务器存储
 * @date 2020/8/29 16:37
 */
public class DiskCloudStorageService extends CloudStorageService {

    @Autowired
    public DiskCloudStorageService(CloudStorageConfig config) {
        this.config = config;
    }

    @Override
    public String getUrl(String path) {
        return new StringBuilder().append(config.getProxyServer()).append(path).toString();
    }

    @Override
    public String upload(File file) {
        this.checkSize(config.getMaxFileSize(), file.length());
        return upload(FileUtil.getInputStream(file), getPath(file,""));
    }

    @Override
    public String upload(MultipartFile file) {
        try {
            this.checkSize(config.getMaxFileSize(), file.getSize());
            return upload(file.getInputStream(), this.getPath(file, ""));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String upload(byte[] data, String path) {
        if (data.length < 3 || path.equals(""))
            throw new RuntimeException("上传文件为空");
        //本地存储必需要以"/"开头
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        try {
            String fileName = config.getDiskPath() + path;
            String dateDir = path.split("/")[1];
            //文件夹
            File dirFile = new File(config.getDiskPath() + "/" + dateDir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileImageOutputStream imageOutput = new FileImageOutputStream(file);//打开输入流
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("上传文件失败", e);
        }
        return path;
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        byte[] data = IoUtil.readBytes(inputStream);
        return this.upload(data, path);
    }
}

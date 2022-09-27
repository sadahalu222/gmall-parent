package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.common.util.DateUtil;
import com.atguigu.gmall.product.config.MinioProperties;
import com.atguigu.gmall.product.service.BaseTrademarkService;
import com.atguigu.gmall.product.service.FileUploadService;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;

@Service
public class FileUploadServiceImpl implements FileUploadService {
    @Autowired
    MinioClient minioClient;
    @Autowired
    MinioProperties minioProperties;

    @Override
    public String upload(MultipartFile file) throws Exception {

        // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象


        // 使用putObject上传一个文件到存储桶中。
        //String bucketName, String objectName, InputStream stream, PutObjectOptions options
        InputStream inputStream = file.getInputStream();

        PutObjectOptions putObjectOptions = new PutObjectOptions(file.getSize(), -1L);
        String contentType = file.getContentType();
        putObjectOptions.setContentType(contentType);
         //防止文件重名
        String name = UUID.randomUUID().toString().replaceAll("-", "") + "_" + file.getOriginalFilename();

        //以日期建立文件夹进行分组
        String dateStr = DateUtil.formatDate(new Date());

        minioClient.putObject(minioProperties.getBucketName(), dateStr+"/"+ name, inputStream, putObjectOptions);


        return minioProperties.getEndpoint()+"/"+minioProperties.getBucketName()+"/" +dateStr+"/"+ name;


    }
}

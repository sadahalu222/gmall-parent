package com.atguigu.gmall.product;

import com.atguigu.gmall.product.service.BaseAttrValueService;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import io.minio.errors.MinioException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;


public class MinioTest {



    @Test
    void uploadFile()throws Exception {
        try {
            // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
            MinioClient minioClient = new MinioClient("http://192.168.6.102:9000",
                    "admin",
                    "admin123456");

            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists("gmall");
            if(isExist) {
                System.out.println("Bucket already exists.");
            } else {
                // 创建一个名为asiatrip的存储桶，用于存储照片的zip文件。
                minioClient.makeBucket("asiatrip");
            }

            // 使用putObject上传一个文件到存储桶中。
            //String bucketName, String objectName, InputStream stream, PutObjectOptions options
            File file = new File("D:/课程笔记/01_9电商项目/商品会/资料/03 商品图片/品牌/p0po.png");
            FileInputStream fileInputStream = new FileInputStream(file);
            PutObjectOptions putObjectOptions = new PutObjectOptions(fileInputStream.available(), -1L);
            putObjectOptions.setContentType("image/png");
            minioClient.putObject("gmall","dfsadf.png",fileInputStream,putObjectOptions);
            System.out.println("上传成功");
        } catch(MinioException e) {
            System.out.println("失败" + e);



        }
    }

}

package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.product.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传
 */
@RequestMapping("/admin/product")
@RestController
public class FileuploadController {

    @Autowired
    FileUploadService fileUploadService;

    /**
     * 文件上传功能
     * 1.前端把文件流放到哪里了? 我们该怎么拿到
     *         Post请求数据子啊请求体中(包含了文件[流])
     * 如何接:
     *     @RequestParam("file")MultipartFile file :可以接 但是不专业
     *     @RequestPart("file")MultipartFile file  :专门处理文件
     *
     * 各种注解接不同位置的请求数据
     * @RequestParam: 无论什么请求 接请求参数; 多个参数可以用一个Pojo把所有的数据都接了
     * @RequestPart: 接请求参数里面的文件项
     * @RequestBody: 接请求体中的所有数据(json转为pojo)
     * @PathVariable: 接路径上的动态变量
     * @RequestHeader: 获取浏览器发送的请求的请求头中的某些值
     * @CookieValue: 获取浏览器发送的请求头中的某些值
     * - 如果多个就写数组,否则就写单个对象
     *
     *
     * @param file
     * @return
     */
    @PostMapping("fileUpload")
    public Result fileUpload(@RequestParam("file")MultipartFile file) {


        String url = null;
        try {
            url = fileUploadService.upload(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.ok(url);
    }

}

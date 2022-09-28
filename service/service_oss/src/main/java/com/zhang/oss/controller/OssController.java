package com.zhang.oss.controller;

import com.zhang.commonutils.R;
import com.zhang.oss.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/eduoss/fileoss")
@CrossOrigin
public class OssController {


    @Autowired
    private OssService ossService;
    //将用户的头像上传到阿里云
    @PostMapping
    public R uploadOssFile(MultipartFile file){
        //MultipartFile 获取上传文件
        String url = ossService.uploadFileAvatar(file);

        return R.ok().data("url",url);
    }
}

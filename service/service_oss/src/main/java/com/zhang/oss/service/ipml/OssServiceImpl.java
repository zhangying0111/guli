package com.zhang.oss.service.ipml;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.zhang.oss.service.OssService;
import com.zhang.oss.util.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {

    //上传头像到oss
    @Override
    public String uploadFileAvatar(MultipartFile file) {
        // 工具类获取值
        String endpoint = ConstantPropertiesUtils.END_POIND;
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;

        try {
            // 创建OSS实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            //获取上传文件输入流
            InputStream inputStream = file.getInputStream();
            //获取文件名称
            String fileName = file.getOriginalFilename();
            /**
             * 1、在文件名称里面添加唯一随机值
             *
             * 不这样做最后一次上传的同名文件会将之前的覆盖掉
             */
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            fileName = uuid+fileName;
            /**
             * 2、把文件按照日期分来
             * //2019/11/12/01.jpg
             */
            //获取当前日期 用引入的工具类

            String datePath = new DateTime().toString("yyyy/MM/dd");
            fileName = datePath+"/"+fileName; // 2022/3/15/123122432432a.jpg

            //调用oss方法实现上传
            //第一个参数  Bucket名称
            //第二个参数  上传到oss文件路径和文件名称   aa/bb/1.jpg   这里是你想上传到oss的位置
            //第三个参数  上传文件输入流
            ossClient.putObject(bucketName,fileName,inputStream);
           // System.out.println(fileName);
            // 关闭OSSClient。
            ossClient.shutdown();

            //把上传之后文件路径返回
            //需要把上传到阿里云oss路径手动拼接出来
            //https://edu-guli-1010.oss-cn-beijing.aliyuncs.com/01.jpg
            String url = "https://"+bucketName+"."+endpoint+"/"+fileName;
            return url;
        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}


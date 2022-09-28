package com.zhang.eduservice.client;

import com.zhang.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//edu 通过 这个接口 来调用 服务注册中心的 vod中的方法
@FeignClient("service-vod")//注解用于指定从哪个服务中调用功能 ，名称与被调用的服务名保持一致。
public interface VodClient {

    //@PathVariable注解一定要指定参数名称，否则出错
    @DeleteMapping("/eduvod/video/removeAlyVideo/{id}")
    public R removeAlyVideo(@PathVariable("id") String id);

    //删除多个阿里云的视频
    @DeleteMapping("/eduvod/video/delete-batch")
    public R deleteBatch(@RequestParam("videoList") List<String> videoIdList);
}

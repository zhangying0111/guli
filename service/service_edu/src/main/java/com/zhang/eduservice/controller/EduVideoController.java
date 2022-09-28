package com.zhang.eduservice.controller;


import com.zhang.commonutils.R;
import com.zhang.eduservice.client.VodClient;
import com.zhang.eduservice.entity.EduVideo;
import com.zhang.eduservice.service.EduVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author zhangying
 * @since 2022-03-17
 */
@RestController
@RequestMapping("/eduservice/video")
@CrossOrigin
public class EduVideoController {
    @Autowired
    private EduVideoService videoService;

    @Autowired
    private VodClient vodClient;

    //添加小节
    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo) {
        videoService.save(eduVideo);
        return R.ok();
    }

    //删除小节
    //TODO 后面需要完善，删除小节的时候，同时把里面的视频删除

    /**
     * 这里与原本的区别  原本只是在数据库当中删除了这条记录，但是阿里云中的视频还在
     * 这里通过远程调用vod 删除阿里云视频的方法  实现了删除小节的时候不仅删除数据中的内容 还删除了阿里云中的视频
     * 做到了真正的删除！！！
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    public R deleteVideo(@PathVariable String id) {
        //根据小节id 删除视频id
        EduVideo eduVideo = videoService.getById(id);
        String videoSourceId = eduVideo.getVideoSourceId();

        //判断小节视频里面是否有id
        if (!StringUtils.isEmpty(videoSourceId)) {
            //根据视频视频id 远程调用实现删除
           vodClient.removeAlyVideo(videoSourceId);
        }
        videoService.removeById(id);
        return R.ok();
    }

    //根据id查询小节信息
    @GetMapping("getVideoInfo/{videoId}")
    public R getVideoInfo(@PathVariable String videoId) {
        EduVideo eduVideo = videoService.getById(videoId);
        return R.ok().data("video", eduVideo);
    }

    //修改小节  需要先查询对应的小节信息
    @PostMapping("updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo) {
        videoService.updateById(eduVideo);
        return R.ok();
    }
}


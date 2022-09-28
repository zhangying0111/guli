package com.zhang.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhang.eduservice.client.VodClient;
import com.zhang.eduservice.entity.EduVideo;
import com.zhang.eduservice.mapper.EduVideoMapper;
import com.zhang.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author zhangying
 * @since 2022-03-17
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {
    //注入vodClient
    @Autowired
    private VodClient vodClient;

    //根据课程id删除小节
    //TODO 删除小节，删除对应视频
    @Override
    public void removeVideoByCourseId(String courseId) {
        //根据课程id查询所有视频id
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();
        wrapperVideo.eq("course_id", courseId);
        //该list中是所有与课程id一样的video的集合
        //但是我们只需要视频id即可
        wrapperVideo.select("video_source_id");//找所有video_source_id字段的信息即可  不要全部的video信息
        List<EduVideo> eduVideoList = baseMapper.selectList(wrapperVideo);
        //List<EduVide> 变成 List<String>
        List<String> videoIds = new ArrayList<>();
        for (int i = 0; i < eduVideoList.size(); i++) {
            EduVideo eduVideo = eduVideoList.get(i);
            String videoSourceId = eduVideo.getVideoSourceId();
            //放到videoIds集合里
            if (!StringUtils.isEmpty(videoSourceId)){
                videoIds.add(videoSourceId);
            }

        }
        if (videoIds.size() > 0){
            //根据多个视频id 删除视频
            vodClient.deleteBatch(videoIds);
        }


        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        baseMapper.delete(wrapper);

    }
}

package com.zhang.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhang.eduservice.entity.EduChapter;
import com.zhang.eduservice.entity.EduVideo;
import com.zhang.eduservice.entity.chapter.ChapterVo;
import com.zhang.eduservice.entity.chapter.VideoVo;
import com.zhang.eduservice.mapper.EduChapterMapper;
import com.zhang.eduservice.mapper.EduVideoMapper;
import com.zhang.eduservice.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhang.eduservice.service.EduVideoService;
import com.zhang.exceptionhandler.GuliException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author zhangying
 * @since 2022-03-17
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {
    //注入EduVideoService
    @Autowired
    private EduVideoService eduVideoService;

    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {


        //1、根据课程id查询所有chapter
        QueryWrapper<EduChapter> chapterWrapper = new QueryWrapper<>();
        chapterWrapper.eq("course_id", courseId);
        List<EduChapter> allChapter = baseMapper.selectList(chapterWrapper);//可以查到该课程下的所有章节

        //2、根据课程id查询所有video【小节】

        QueryWrapper<EduVideo> videoWrapper = new QueryWrapper<>();
        videoWrapper.eq("course_id", courseId);
        List<EduVideo> allVideo = eduVideoService.list(videoWrapper);//可以长训到该课程下的所有小节

        //接下来就是对数据进行封装
        List<ChapterVo> finalList = new ArrayList<>();


        //3、遍历章节list进行数据封装
        for (int i = 0; i < allChapter.size(); i++) {
            //每个章节
            EduChapter everyChapter = allChapter.get(i);
            //将遍历出的每个章节的数据复制到vo里面  然后再放到finalList
            //创建chapterVo存放数据
            ChapterVo chapterVo = new ChapterVo();
            //数据复制
            BeanUtils.copyProperties(everyChapter, chapterVo);
            finalList.add(chapterVo);
            //每个章节加入之后  开始遍历小节前  创建一个小节的list  存放每个章节对应的小节数据
            List<VideoVo> videoVoList = new ArrayList<>();

            //4、遍历小节list对 数据进行封装
            for (int m = 0; m < allVideo.size(); m++) {

                //每个小节  检查每个小节
                EduVideo everyVideo = allVideo.get(m);
                //思路：如果小节的chapterId和章节的id相同  说明这个小节是属于这个章节的
                if (everyVideo.getChapterId().equals(everyChapter.getId())) {//说明这个小节是这个章节当中的
                    //进行封装
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(everyVideo, videoVo);//这个小节的数据 复制到  videoVo
                    videoVoList.add(videoVo);//这个集合里面就这个章节下的一个小节
                }
            }
            //这个for循环结束说明一个章节的所有小节一节全部找到  放到大集合里面
            //设置章节list的孩子【存放小节的数据】
            chapterVo.setChildren(videoVoList);//这样就结束了  返回这个集合即可
        }
        return finalList;
    }

    @Override
    public boolean deleteChapter(String chapterId) {
        //根据chapter章节id，查询小节数，如果有小节数据  不进行删除
        QueryWrapper<EduVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("chapter_id",chapterId);
        int count = eduVideoService.count(queryWrapper);
        if (count > 0){//有小节
            throw new GuliException(20001,"不能删除");
        }else {
            int result = baseMapper.deleteById(chapterId);
            return result > 0;//
        }
    }
//根据课程id删除章节
    @Override
    public void removeChapterByCourseId(String courseId) {
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);
    }
}

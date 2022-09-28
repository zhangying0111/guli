package com.zhang.eduservice.service.impl;

import com.zhang.eduservice.entity.EduChapter;
import com.zhang.eduservice.entity.EduCourse;
import com.zhang.eduservice.entity.EduCourseDescription;
import com.zhang.eduservice.entity.EduVideo;
import com.zhang.eduservice.entity.vo.CourseInfoVo;
import com.zhang.eduservice.entity.vo.CoursePublishVo;
import com.zhang.eduservice.mapper.EduCourseMapper;
import com.zhang.eduservice.service.EduChapterService;
import com.zhang.eduservice.service.EduCourseDescriptionService;
import com.zhang.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhang.eduservice.service.EduVideoService;
import com.zhang.exceptionhandler.GuliException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author zhangying
 * @since 2022-03-17
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    private EduCourseDescriptionService courseDescriptionService;
    //注入小节和章节的service
    @Autowired
    private EduVideoService videoService;
    @Autowired
    private EduChapterService chapterService;
    //添加课程信息基本方法
    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        //1、向课程表添加课程基本信息
        //CourseInfoVo对象转换eduCourse对象
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int insert = baseMapper.insert(eduCourse);
        if (insert <=0){
            throw new GuliException(20001,"添加课程信息失败");
        }
        //获取添加之后课程的id
        String cid = eduCourse.getId();
        //2、像课程简介表添加课程简介
        EduCourseDescription courseDescription = new EduCourseDescription();
        courseDescription.setDescription(courseInfoVo.getDescription());
        courseDescription.setId(cid);
        courseDescriptionService.save(courseDescription);
        return cid;
    }

    @Override
    public CourseInfoVo getCourseInfo(String id) {
        //根据id查询课程信息
        EduCourse eduCourse = baseMapper.selectById(id);
        CourseInfoVo courseInfoVo = new CourseInfoVo();
        //数据复制
        BeanUtils.copyProperties(eduCourse,courseInfoVo);

        //根据id查询课程描信息数据
        EduCourseDescription description = courseDescriptionService.getById(id);
        courseInfoVo.setDescription(description.getDescription());
        return courseInfoVo;
    }

    //更新课程信息
    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        EduCourse eduCourse = new EduCourse();
        //复制数据
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int update = baseMapper.updateById(eduCourse);
        if (update == 0){
            throw new GuliException(20001,"数据更新失败");
        }

        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setId(courseInfoVo.getId());
        eduCourseDescription.setDescription(courseInfoVo.getDescription());
        boolean flag = courseDescriptionService.updateById(eduCourseDescription);
        if (!flag){
            throw new GuliException(20001,"数据更新失败");
        }
    }

    @Override
    public CoursePublishVo publishCourseInfo(String id) {
        CoursePublishVo publishCourseInfo = baseMapper.getPublishCourseInfo(id);
        return publishCourseInfo;
    }

    @Override
    public void removeCourse(String courseId) {
        //1、根据课程id删除小节
        videoService.removeVideoByCourseId(courseId);

        //2、根据课程id删除章节
        chapterService.removeChapterByCourseId(courseId);
        //3、根据课程id删除描述
        courseDescriptionService.removeById(courseId); //直mp方法即可  因为课程描述id和课程id是一对一
        //4、根据课程id删除课程本身
        int result = baseMapper.deleteById(courseId);
        if (result == 0){//失败返回
            throw  new GuliException(20001,"删除失败！");
        }
    }
}

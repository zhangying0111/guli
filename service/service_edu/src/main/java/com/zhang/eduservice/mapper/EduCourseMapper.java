package com.zhang.eduservice.mapper;

import com.zhang.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhang.eduservice.entity.vo.CoursePublishVo;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author zhangying
 * @since 2022-03-17
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {
        public CoursePublishVo getPublishCourseInfo(String courseId);
}

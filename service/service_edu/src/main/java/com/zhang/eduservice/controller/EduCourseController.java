package com.zhang.eduservice.controller;


import com.zhang.commonutils.R;
import com.zhang.eduservice.entity.EduCourse;
import com.zhang.eduservice.entity.EduVideo;
import com.zhang.eduservice.entity.vo.CourseInfoVo;
import com.zhang.eduservice.entity.vo.CoursePublishVo;
import com.zhang.eduservice.service.EduCourseService;
import javafx.geometry.Pos;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author zhangying
 * @since 2022-03-17
 */
@RestController
@RequestMapping("/eduservice/course")
@CrossOrigin
public class EduCourseController {

    @Autowired
    private EduCourseService courseService;

    //课程列表 基本实现
    //TODO 完善条件查询带分页
    @GetMapping
    public R getCourseList(){
        List<EduCourse> list = courseService.list(null);
        return R.ok().data("list",list);
    }

    @PostMapping("addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        //返回添加课程之后的id 为了后面的课程大纲使用
        String id = courseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId",id);
    }

    //根据课程id查询课程信息
    @GetMapping("getCourseInfo/{id}")
    public R getCourseInfo(@PathVariable String id){
        CourseInfoVo courseInfoVo = courseService.getCourseInfo(id);
        return R.ok().data("courseInfoVo",courseInfoVo);
    }
    //修改传的是一个对象，通过对象做到
    //修改课程信息如果使用RequestBody 必须使用PostMapping
    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo){

        courseService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }

    //根据课程id查询课程确认信息
    @GetMapping("getPublishCourseInfo/{id}")
    public R getPublishCourseInfo(@PathVariable String id){
        CoursePublishVo coursePublishVo = courseService.publishCourseInfo(id);
        return R.ok().data("publishCourse",coursePublishVo);
    }

    //课程最终发布
    //修改课程状态
    @PostMapping("publishCourse/{id}")
    public R publishCourse(@PathVariable String id){
        EduCourse eduCourse  = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus("Normal");//设置课程发布状态  Draft = Normal
        courseService.updateById(eduCourse);
        return R.ok();

    }
    //删除课程
    @DeleteMapping("{courseId}")
    public R removeCourse(@PathVariable String courseId){
        courseService.removeCourse(courseId);
        return R.ok();
    }
}


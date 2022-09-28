package com.zhang.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhang.commonutils.R;
import com.zhang.eduservice.entity.EduTeacher;
import com.zhang.eduservice.entity.vo.TeacherQuery;
import com.zhang.eduservice.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author zhangying
 * @since 2022-03-11
 */
@RestController
@RequestMapping("/eduservice/teacher")
@CrossOrigin //解决跨域问题
public class EduTeacherController {
    @Autowired
    private EduTeacherService teacherService;
    //1、查询所有数据
    @GetMapping("findAllTeacher")
    private R finAll(){
        List<EduTeacher> teachers = teacherService.list(null);
        return R.ok().data("items",teachers);
    }
    @DeleteMapping("{id}")
    private R removeTeacher(@PathVariable String id){
        boolean flag = teacherService.removeById(id);
        if (flag){
          return   R.ok();
        }else {
            return R.error();
        }
    }

    @GetMapping("pageTeacher/{current}/{limit}")
    public R pageList(@PathVariable long current,
                      @PathVariable long limit){
        //创建page对象
        Page<EduTeacher> pageTeacher = new Page<>(current, limit);
        /**
         * 模拟异常
         */
        /*try{
            int i = 10/0;
        }catch (Exception e){
            throw new GuliException(20001,"执行了自定义的异常。。");
        }*/

        //调用方法实现分页
        teacherService.page(pageTeacher,null);
        long total = pageTeacher.getTotal();//总记录数
        List<EduTeacher> records = pageTeacher.getRecords();//每页list数据的集合

        return R.ok().data("total",total).data("rows",records);


    }

    /**
     *  @RequestBody : 把请求体中的数据，读取出来， 转为java对象使用
     *  所以必须使用PostMapping
     *  请求参数存放位置
     *  Get：请求头
     *  Post：请求体
     */
    //条件查询带分页的方法
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(@PathVariable long current,
                                  @PathVariable long limit,
                                  @RequestBody(required = false) TeacherQuery teacherQuery){

        //创建page对象
        Page<EduTeacher> teacherPage = new Page<>(current, limit);
        //构建条件
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        //wrapper
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        //判断条件值是否为空，如果不为空，拼接条件
        if (!StringUtils.isEmpty(name)){
            wrapper.like("name",name);
        }
        if (!StringUtils.isEmpty(level)){
            wrapper.eq("level",name);
        }
        if (!StringUtils.isEmpty(begin)){
            wrapper.ge("gmt_create",begin);
        }
        if (!StringUtils.isEmpty(end)){
            wrapper.le("gmt_modified",end);
        }
        wrapper.orderByDesc("gmt_create");//每次新加的数据都在第一个排序
        //创建方法实现条件查询分页
        teacherService.page(teacherPage,wrapper);
        long total = teacherPage.getTotal();
        List<EduTeacher> records = teacherPage.getRecords();
        return R.ok().data("total",total).data("rows",records);

    }

    @PostMapping("addTeacher")
    public R saveTeacher(@RequestBody EduTeacher teacher){
        boolean flag = teacherService.save(teacher);
        return flag?R.ok():R.error();
    }
    
    @GetMapping("getTeacher/{id}")
    public R getTeacher(@PathVariable String id){
        EduTeacher teacher = teacherService.getById(id);
        return R.ok().data("teacher",teacher);
    }

    /**
     * @param teacher
     * @return
     */
    @PostMapping("updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher teacher){
        boolean flag = teacherService.updateById(teacher);
        return flag?R.ok():R.error();
    }
}


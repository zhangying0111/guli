package com.zhang.eduservice.controller;


import com.zhang.commonutils.R;
import com.zhang.eduservice.entity.subject.OneSubject;
import com.zhang.eduservice.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author zhangying
 * @since 2022-03-16
 */
@RestController
@RequestMapping("/eduservice/subject")
@CrossOrigin
public class EduSubjectController {


    @Autowired
    private EduSubjectService subjectService;

    //添加课程分类
    //获取上传过来的文件，把文件内容读出来

    /**
     * MultipartFile file : 可以得到上传的文件
     *
     * @param file
     * @return
     */
    @PostMapping("addSubject")
    public R addSubject(MultipartFile file) {
        //上传过来的excel文件
        subjectService.addSubject(file, subjectService);
        return R.ok();
    }

    /**
     * 课程分类列表  树形
     * @return
     */
    @GetMapping("getAllSubject")
    public R getAllSubject() {

        List<OneSubject> list = subjectService.getAllOneTwoSubject();
        return R.ok().data("list",list);
    }
}


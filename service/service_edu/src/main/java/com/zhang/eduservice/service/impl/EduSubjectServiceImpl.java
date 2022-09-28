package com.zhang.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhang.eduservice.entity.EduSubject;
import com.zhang.eduservice.entity.excel.SubjectData;
import com.zhang.eduservice.entity.subject.OneSubject;
import com.zhang.eduservice.entity.subject.TwoSubject;
import com.zhang.eduservice.listener.SubjectExcelListener;
import com.zhang.eduservice.mapper.EduSubjectMapper;
import com.zhang.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author zhangying
 * @since 2022-03-16
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {
    //添加课程分类
    @Override
    public void addSubject(MultipartFile file,EduSubjectService subjectService) {
        try{
            //文件输入流
            InputStream in = file.getInputStream();
            //方法进行获取
            EasyExcel.read(in,SubjectData.class,new SubjectExcelListener(subjectService)).sheet().doRead();
        }catch (Exception e){

        }
    }

    @Override
    public List<OneSubject> getAllOneTwoSubject() {
        //1、查询所有一级分类 parentid = 0;
        QueryWrapper<EduSubject> wrapperOne = new QueryWrapper<>();
        wrapperOne.eq("parent_id","0");
        /**
         * ServiceImpl  里面有baseMapper
         */
        List<EduSubject> oneSubjectList = baseMapper.selectList(wrapperOne);
        //2、查询所有二级分类
        QueryWrapper<EduSubject> wrapperTwo = new QueryWrapper<>();
        wrapperOne.ne("parent_id","0");
        List<EduSubject> twoSubjectList = baseMapper.selectList(wrapperTwo);
        //创建list集合，用户存储最终封装的数据

        List<OneSubject> finalSubjectList = new ArrayList<>();
        //3、封装一级分类

        for (int i = 0; i < oneSubjectList.size(); i++) {
            //得到OneSubjectList每个eduSubject对象
            EduSubject eduSubject = oneSubjectList.get(i);
            //将eduSubject里面的值取出来 放到OneSubjectList对象里面
            OneSubject oneSubject = new OneSubject();
            //将eduSubject里和oneSubject对应的值进行复制
            BeanUtils.copyProperties(eduSubject,oneSubject);
            //多个OneSubject梵高finalSubjectList里面
            finalSubjectList.add(oneSubject);

            //在一级分类查询里面查询所有的二级分类
            //创建list集合封装每一个一级分类的二级分类
            List<TwoSubject> twoFinalSubjectList = new ArrayList<>();
            //遍历二级分类list集合
            for (int m = 0; m < twoSubjectList.size(); m++) {
                //获取每个二级分类
                EduSubject tSubject = twoSubjectList.get(m);
                //判断二级分类的parentid和一级分类是否一样
                if (tSubject.getParentId().equals(eduSubject.getId())){
                    //把tSubject的值复制到TwoSubject里面
                    TwoSubject twoSubject = new TwoSubject();
                    BeanUtils.copyProperties(tSubject,twoSubject);
                    twoFinalSubjectList.add(twoSubject);
                }
            }

            //把一级分类下面所有的二级分类放到一级分类里面
            oneSubject.setChildren(twoFinalSubjectList);
        }
        //4、封装二级分类

        return finalSubjectList;
    }
}

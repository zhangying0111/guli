package com.zhang.eduservice.service;

import com.zhang.eduservice.entity.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhang.eduservice.entity.chapter.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author zhangying
 * @since 2022-03-17
 */
public interface EduChapterService extends IService<EduChapter> {

    List<ChapterVo> getChapterVideoByCourseId(String courseId);

    boolean deleteChapter(String chapterId);

    void removeChapterByCourseId(String courseId);
}

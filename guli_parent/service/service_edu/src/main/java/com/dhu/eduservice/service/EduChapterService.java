package com.dhu.eduservice.service;

import com.dhu.eduservice.entity.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dhu.eduservice.entity.chapter.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author Yushui
 * @since 2021-05-18
 */
public interface EduChapterService extends IService<EduChapter> {

    List<ChapterVo> getChapterVideoByCourseId(String courseId);

    boolean deleteChapter(String chapterId);

    void removeChapterByCourseId(String courseId);
}

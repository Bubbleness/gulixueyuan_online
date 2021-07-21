package com.dhu.eduservice.service;

import com.dhu.eduservice.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dhu.eduservice.entity.subject.OneSubject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author Yushui
 * @since 2021-05-17
 */
public interface EduSubjectService extends IService<EduSubject> {

    //添加课程分类
    void saveSubject(MultipartFile file,EduSubjectService eduSubjectService);

    //课程分类列表树形结构
    List<OneSubject> getAllOneTwoSubject();
}

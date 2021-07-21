package com.dhu.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dhu.eduservice.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author Yushui
 * @since 2021-05-15
 */
public interface EduTeacherService extends IService<EduTeacher> {

    Map<String, Object> getTeacherFrontList(Page<EduTeacher> pageTeacher);
}

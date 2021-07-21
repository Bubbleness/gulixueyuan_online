package com.dhu.eduservice.controller;


import com.dhu.commonutils.R;
import com.dhu.eduservice.entity.subject.OneSubject;
import com.dhu.eduservice.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author Yushui
 * @since 2021-05-17
 */
@RestController
@RequestMapping("/eduservice/edusubject")
@CrossOrigin
public class EduSubjectController {

    @Autowired
    private EduSubjectService eduSubjectService;


    /**
     * 添加课程分类
     * 通过Excel表格来添加
     * @param file
     * @return
     */
    @PostMapping("/addSubject")
    public R addSubject(MultipartFile file){

        //上传的excel文件
        eduSubjectService.saveSubject(file,eduSubjectService);
        return R.ok();
    }


    /**
     * 获取课程分类信息
     * 课程分类列表  树形结构
     * @return
     */
    @GetMapping("/getAllSubject")
    public R getAllSubject(){

        List<OneSubject> list = eduSubjectService.getAllOneTwoSubject();
        return R.ok().data("list",list);
    }


}


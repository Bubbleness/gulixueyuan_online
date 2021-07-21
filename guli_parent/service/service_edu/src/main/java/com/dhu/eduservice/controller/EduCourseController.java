package com.dhu.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dhu.commonutils.R;
import com.dhu.eduservice.entity.EduCourse;
import com.dhu.eduservice.entity.vo.CourseInfoVo;
import com.dhu.eduservice.entity.vo.CoursePublishVo;
import com.dhu.eduservice.entity.vo.CourseQuery;
import com.dhu.eduservice.service.EduCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.EditorKit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author Yushui
 * @since 2021-05-18
 */
@Api(description = "课程管理")
@RestController
@RequestMapping("/eduservice/educourse")
@CrossOrigin
public class EduCourseController {

    @Autowired
    private EduCourseService courseService;


    /**
     * 添加课程的基本信息
     * @param courseInfoVo
     * @return
     */
    @PostMapping("/addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        //返回添加之后的课程id，为后面添加大纲做准备
        String id =  courseService.saveCourseInfo(courseInfoVo);
        //courseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId",id);
    }


    /**
     * 根据传入的课程id进行查询课程信息
     * 用于数据的回显
     * @param courseId
     * @return
     */
    @GetMapping("/getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable String courseId){
        CourseInfoVo courseInfoVo = courseService.getCourseInfo(courseId);
        return R.ok().data("courseInfoVo",courseInfoVo);
    }

    /**
     * 根据传入的信息更新课程信息
     * @param courseInfoVo
     * @return
     */
    @PostMapping("/updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        courseService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }


    /**
     * 课程最终发布前确认信息
     * @param id
     * @return
     */
    @GetMapping("/getPublishCourseInfo/{id}")
    public R getPublishCourseInfo(@PathVariable String id){
        CoursePublishVo coursePublishVo = courseService.publishCourseInfo(id);
        return R.ok().data("publishCourse",coursePublishVo);
    }


    /**
     * 课程发布确认
     * 修改发布的状态
     * @param id
     * @return
     */
    @PostMapping("/publishCourse/{id}")
    public R publishCourse(@PathVariable String id){

        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        //已发布
        eduCourse.setStatus("Normal");
        courseService.updateById(eduCourse);
        return R.ok();
    }


    /**
     * 查询出课程信息
     * @return
     */
    @GetMapping("/getCourseList")
    public R getCourseList(){
        List<EduCourse> list = courseService.list(null);
        return R.ok().data("list",list);
    }


    /**
     * 根据课程id进行删除课程
     * @param courseId
     * @return
     */
    @DeleteMapping("/{courseId}")
    public R deleteCourse(@PathVariable String courseId){
        courseService.removeCourse(courseId);
        return R.ok();
    }


    /**
     * 多条件组合分页查询
     * @param current
     * @param limit
     * @param courseQuery
     * @return
     */
    @PostMapping("/pageCourseCondition/{current}/{limit}")
    public R pageCourseCondition(@PathVariable long current,
                                 @PathVariable long limit,
                                 @RequestBody(required = false) CourseQuery courseQuery){
        //创建Page对象 用来分页
        Page<EduCourse> pageCourse = new Page<>(current,limit);
        //构造条件
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        //多条件组合
        String title = courseQuery.getTitle();
        String status = courseQuery.getStatus();
        String begin = courseQuery.getBegin();
        String end = courseQuery.getEnd();

        if(!StringUtils.isEmpty(title))
            wrapper.like("title",title);
        if(!StringUtils.isEmpty(status))
            wrapper.eq("status",status);
        if(!StringUtils.isEmpty(begin))
            wrapper.ge("gmt_create",begin);
        if(!StringUtils.isEmpty(end))
            wrapper.le("gmt_create",end);

        //排序
        wrapper.orderByDesc("gmt_create");

        courseService.page(pageCourse,wrapper);
        long total = pageCourse.getTotal();
        List<EduCourse> records = pageCourse.getRecords();
        return R.ok().data("total",total).data("rows",records);
    }


    /**
     * 实现分页查询课程列表
     * @param current
     * @param limit
     * @return
     */
    @PostMapping("/pageCourse/{current}/{limit}")
    public R pageListCourse(@PathVariable long current,
                             @PathVariable long limit){

        Page<EduCourse> pageCourse = new Page<>(current,limit);
        //QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        courseService.page(pageCourse,null);

        long total = pageCourse.getTotal();
        List<EduCourse> records = pageCourse.getRecords();
        Map map = new HashMap();
        map.put("total",total);
        map.put("rows",records);
        return R.ok().data(map);
        //return R.ok().data("total",total).data("records",records);
    }

}


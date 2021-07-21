package com.dhu.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dhu.commonutils.R;
import com.dhu.eduservice.entity.EduTeacher;
import com.dhu.eduservice.entity.vo.TeacherQuery;
import com.dhu.eduservice.service.EduTeacherService;
import com.dhu.servicebase.exceptionhandler.GuliException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author Yushui
 * @since 2021-05-15
 */
@Api(description = "讲师管理")
@RestController
@RequestMapping("/eduservice/teacher")
@CrossOrigin
public class EduTeacherController {

    //注入service
    @Autowired
    private EduTeacherService teacherService;

    /**
     * 查询所有教师接口
     * 接口遵循rest风格
     * 访问网址：http://localhost:8001/eduservice/teacher/findAll
     * 使用 R 数据类型进行返回
     * @return
     */
    @ApiOperation(value = "所有讲师列表")
    @GetMapping("/findAll")
    public R findAllTeacher(){

        //调用service方法实现查询所有教师
        List<EduTeacher> list = teacherService.list(null);
        return R.ok().data("items",list);
    }


    /**
     * 逻辑删除讲师接口
     * 根据id进行逻辑删除
     * @PathVariable 表示获取到路径中的id参数
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    public R removeTeacher(
            @ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable String id){

        boolean flag = teacherService.removeById(id);
        if(flag)
            return R.ok();
        else
            return R.error();
    }


    /**
     * 实现分页查询接口
     * @param current 当前页
     * @param limit 每页记录数
     * @return
     */
    @GetMapping("/pageTeacher/{current}/{limit}")
    public R pageListTeacher(@PathVariable long current,
                             @PathVariable long limit){

        //创建Page对象
        Page<EduTeacher> page = new Page<>(current,limit);
        //调用方法实现分页
        //底层将所有数据封装到page对象中
        teacherService.page(page,null);

        long total = page.getTotal();
        List<EduTeacher> records = page.getRecords();

        Map map = new HashMap();
        map.put("total",total);
        map.put("rows",records);
        return R.ok().data(map);

        //或者
        //return R.ok().data("total",total).data("rows",records);
    }



    /**
     * 多条件组合带分页查询
     * @param current
     * @param limit
     * @param teacherQuery 多组合条件查询的条件
     * @return
     */
//    @GetMapping("/pageTeacherCondition/{current}/{limit}")
//    public R pageTeacherCondition(@PathVariable long current,
//                                  @PathVariable long limit,
//                                  TeacherQuery teacherQuery){
//
//        Page<EduTeacher> page = new Page(current,limit);
//        //构建查询条件
//        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
//        //多条件组合查询
//        //判断条件是否为空，不为空就拼接查询条件
//        String name = teacherQuery.getName();
//        Integer level = teacherQuery.getLevel();
//        String begin = teacherQuery.getBegin();
//        String end = teacherQuery.getEnd();
//
//        if(!StringUtils.isEmpty(name))
//            wrapper.like("name",name);
//
//        if(!StringUtils.isEmpty(level))
//            wrapper.eq("level",level);
//
//        //大于等于begin
//        if(!StringUtils.isEmpty(begin))
//            wrapper.gt("gmt_create",begin);
//
//        //小于等于end
//        if(!StringUtils.isEmpty(end))
//            wrapper.le("gmt_create",end);
//
//        teacherService.page(page,wrapper);
//        long total = page.getTotal();
//        List<EduTeacher> records = page.getRecords();
//        return R.ok().data("total",total).data("rows",records);
//    }


    /**
     * 多条件组合带分页查询
     * @param current
     * @param limit
     * @param teacherQuery 多组合条件查询的条件
     * @return
     */
    @PostMapping("/pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(@PathVariable long current,@PathVariable long limit,
                                  @RequestBody(required = false)  TeacherQuery teacherQuery) {
        //创建page对象
        Page<EduTeacher> pageTeacher = new Page<>(current,limit);

        //构建条件
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        // 多条件组合查询
        // mybatis学过 动态sql
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        //判断条件值是否为空，如果不为空拼接条件
        if(!StringUtils.isEmpty(name)) {
            //构建条件
            wrapper.like("name",name);
        }
        if(!StringUtils.isEmpty(level)) {
            wrapper.eq("level",level);
        }
        if(!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create",begin);
        }
        if(!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create",end);
        }

        //排序
        wrapper.orderByDesc("gmt_create");

        //调用方法实现条件查询分页
        teacherService.page(pageTeacher,wrapper);

        long total = pageTeacher.getTotal();//总记录数
        List<EduTeacher> records = pageTeacher.getRecords(); //数据list集合
        return R.ok().data("total",total).data("rows",records);
    }


    /**
     * 添加教师接口
     * 当使用@RequestBody注解时 只能使用post提交
     * @RequestBody 将前端的数据以json格式封装到参数对象中
     * @param eduTeacher
     * @return
     */
    @PostMapping("/addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher){

        boolean save = teacherService.save(eduTeacher);
        if(save)
            return R.ok();
        else
            return R.error();
    }


    /**
     * 根据id查询教师接口
     * @param id
     * @return
     */
    @GetMapping("/getTeacher/{id}")
    public R getTeacher(@PathVariable String id){

        EduTeacher eduTeacher = teacherService.getById(id);
        return R.ok().data("teacher",eduTeacher);
    }


    /**
     * 根据id更新教师接口
     * @param eduTeacher
     * @return
     */
    @PostMapping("/updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher){

        //测试自定义异常处理
//        try {
//            int i = 1/0;
//        }catch (Exception e){
//            throw new GuliException(20001,"自定义异常处理");
//        }

        boolean flag = teacherService.updateById(eduTeacher);
        if(flag)
            return R.ok();
        else
            return R.error();
    }
}


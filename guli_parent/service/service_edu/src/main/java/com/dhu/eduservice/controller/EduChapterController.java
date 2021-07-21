package com.dhu.eduservice.controller;


import com.dhu.commonutils.R;
import com.dhu.eduservice.entity.EduChapter;
import com.dhu.eduservice.entity.chapter.ChapterVo;
import com.dhu.eduservice.service.EduChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author Yushui
 * @since 2021-05-18
 */
@RestController
@RequestMapping("/eduservice/educhapter")
@CrossOrigin
public class EduChapterController {

    @Autowired
    private EduChapterService chapterService;


    /**
     * 根据传入的课程号id 来查询出所属于的课程的章节信息
     * @param courseId
     * @return
     */
    @GetMapping("/getChapterVideo/{courseId}")
    public R getChapterVideo(@PathVariable String courseId){

        List<ChapterVo> list = chapterService.getChapterVideoByCourseId(courseId);

        return R.ok().data("allChapterVideo",list);
    }


    /**
     * 添加章节信息
     * @param eduChapter
     * @return
     */
    @PostMapping("/addChapter")
    public R addChapter(@RequestBody EduChapter eduChapter){

        chapterService.save(eduChapter);
        return R.ok();
    }

    /**
     * 根据章节id查询
     * @param chapterId
     * @return
     */
    @GetMapping("/getChapterInfo/{chapterId}")
    public R getChapterInfo(@PathVariable String chapterId){

        EduChapter eduChapter = chapterService.getById(chapterId);
        return R.ok().data("chapter",eduChapter);
    }


    /**
     * 根据id更新章节信息
     * @param eduChapter
     * @return
     */
    @PostMapping("/updateChapter")
    public R updateChapter(@RequestBody EduChapter eduChapter){

        chapterService.updateById(eduChapter);
        return R.ok();
    }


    @DeleteMapping("/deleteChapter/{chapterId}")
    public R deleteChapter(@PathVariable String chapterId){
        boolean flag = chapterService.deleteChapter(chapterId);
        if(flag)
            return R.ok();
        else
            return R.error();
    }


}


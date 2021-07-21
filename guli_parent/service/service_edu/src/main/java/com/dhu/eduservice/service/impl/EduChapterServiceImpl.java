package com.dhu.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dhu.eduservice.entity.EduChapter;
import com.dhu.eduservice.entity.EduVideo;
import com.dhu.eduservice.entity.chapter.ChapterVo;
import com.dhu.eduservice.entity.chapter.VideoVo;
import com.dhu.eduservice.mapper.EduChapterMapper;
import com.dhu.eduservice.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dhu.eduservice.service.EduVideoService;
import com.dhu.servicebase.exceptionhandler.GuliException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author Yushui
 * @since 2021-05-18
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService videoService;

    /**
     * 课程大纲列表  根据课程id进行查询
     * @param courseId
     * @return
     */
    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {

        //1 根据课程id查询课程里面所有的章节
        QueryWrapper<EduChapter> wrapperChapter = new QueryWrapper<>();
        wrapperChapter.eq("course_id",courseId);
        List<EduChapter> eduChapterList = baseMapper.selectList(wrapperChapter);

        //2 根据课程id查询课程里面所有的小节
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();
        wrapperChapter.eq("course_id",courseId);
        List<EduVideo> eduVideoList = videoService.list(wrapperVideo);

        //创建List集合用于最终封装数据
        List<ChapterVo> finalList = new ArrayList<>();

        //3 遍历查询章节list集合 进行封装数据
        for(EduChapter item : eduChapterList){
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(item,chapterVo);
            finalList.add(chapterVo);

            //创建list用于封装章节中的小节
            List<VideoVo> videoList = new ArrayList<>();
            //4 遍历查询小节list集合，进行封装数据
            for(EduVideo elem : eduVideoList){

                if(elem.getChapterId().equals(chapterVo.getId())){
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(elem,videoVo);
                    videoList.add(videoVo);
                }
            }

            chapterVo.setChildren(videoList);
        }

        return finalList;
    }

    /**
     * 删除章节信息
     * @param chapterId
     * @return
     */
    @Override
    public boolean deleteChapter(String chapterId) {
        //根据chapterId去查小节表  有小节就不能删章节
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id",chapterId);
        //判断是否包含
        int count = videoService.count(wrapper);
        if(count > 0){
            //不进行删除
            throw new GuliException(20001,"当前章节包含小节，不能删除！");
        }
        else{
            //进行删除
            int i = baseMapper.deleteById(chapterId);
            return i>0;

        }
    }

    /**
     * 根据课程id删除章节信息
     * @param courseId
     */
    @Override
    public void removeChapterByCourseId(String courseId) {

        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);

    }
}

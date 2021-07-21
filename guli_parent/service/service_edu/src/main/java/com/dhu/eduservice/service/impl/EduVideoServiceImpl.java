package com.dhu.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dhu.eduservice.client.VodClient;
import com.dhu.eduservice.entity.EduVideo;
import com.dhu.eduservice.mapper.EduVideoMapper;
import com.dhu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author Yushui
 * @since 2021-05-18
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    @Autowired
    private VodClient vodClient;

    /**
     * 根据课程id删除小节信息
     * @param courseId
     * @TODO 删除小节的时候还得删除对应的视频文件
     */
    @Override
    public void removeVideoByCourseId(String courseId) {

        //根据课程id查询出所有视频的id
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();
        wrapperVideo.eq("course_id",courseId);
        wrapperVideo.select("video_source_id");
        List<EduVideo> eduVideoList = baseMapper.selectList(wrapperVideo);

        List<String> videoIds = new ArrayList<>();
        for(EduVideo elem : eduVideoList){
            if(!StringUtils.isEmpty(elem.getVideoSourceId()))
                videoIds.add(elem.getVideoSourceId());
        }

        //删除多个视频
        if(videoIds.size() > 0)
            vodClient.deleteBatch(videoIds);

        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);
    }
}

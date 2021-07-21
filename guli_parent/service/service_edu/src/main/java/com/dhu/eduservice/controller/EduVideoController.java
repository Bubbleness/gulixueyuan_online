package com.dhu.eduservice.controller;


import com.dhu.commonutils.R;
import com.dhu.eduservice.client.VodClient;
import com.dhu.eduservice.entity.EduVideo;
import com.dhu.eduservice.service.EduVideoService;
import com.dhu.servicebase.exceptionhandler.GuliException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author Yushui
 * @since 2021-05-18
 */
@RestController
@RequestMapping("/eduservice/eduvideo")
@CrossOrigin
public class EduVideoController {

    @Autowired
    private EduVideoService videoService;

    @Autowired
    private VodClient vodClient;


    /**
     * 添加小节
     * @param eduVideo
     * @return
     */
    @PostMapping("/addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo){
        videoService.save(eduVideo);
        return R.ok();
    }


    /**
     * 根据id删除课程信息
     * TODO 后面需要完善  删除小节  同时也要删除里面的视频
     * @param id
     * @return
     */
    @DeleteMapping("/deleteVideo/{id}")
    public R deleteVideo(@PathVariable String id){

        //根据小节id获取视频id
        EduVideo eduVideo = videoService.getById(id);
        String videoSourceId = eduVideo.getVideoSourceId();
        if(!StringUtils.isEmpty(videoSourceId)) {
            R result = vodClient.removeAlyVideo(videoSourceId);
            if(result.getCode() == 20001){
                throw new GuliException(20001,"删除视频失败！执行熔断器");
            }
        }

        videoService.removeById(id);
        return R.ok();
    }

    /**
     * 根据id查询video
     * @param id
     * @return
     */
    @GetMapping("/getVideo/{id}")
    public R getVideo(@PathVariable String id){
        EduVideo eduVideo = videoService.getById(id);
        return R.ok().data("video",eduVideo);
    }


    /**
     * 更新视频信息
     * @param eduVideo
     * @return
     */
    @PostMapping("/updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo){

        videoService.updateById(eduVideo);
        return R.ok();
    }

}


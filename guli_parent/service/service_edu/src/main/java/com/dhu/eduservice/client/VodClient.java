package com.dhu.eduservice.client;

import com.dhu.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
//service-edu 要调用 service-vod 中的方法
@FeignClient(name = "service-vod",fallback = VodFileDegradeFeignClient.class)
public interface VodClient {

    @DeleteMapping("/eduvod/video/removeAlyVideo/{id}")
    R removeAlyVideo(@PathVariable("id") String id);


    /**
     * 删除多个视频的方法
     * @param videoList
     * @return
     */
    @DeleteMapping("/eduvod/video/delete-batch")
    R deleteBatch(@RequestParam("videoList") List<String> videoList);
}

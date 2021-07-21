package com.dhu.eduservice.client;

import com.dhu.commonutils.R;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 熔断器
 * 只有在VodVideo接口中的方法执行错误时才会执行
 */
@Component
public class VodFileDegradeFeignClient implements VodClient{


    @Override
    public R removeAlyVideo(String id) {
        return R.error().message("删除视频出错了！");
    }

    @Override
    public R deleteBatch(List<String> videoList) {
        return R.error().message("删除多个视频出错了！");
    }
}

package com.dhu.eduservice.entity.chapter;

import jdk.internal.dynalink.linker.LinkerServices;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

//章节
@Data
public class ChapterVo {

    private String id;
    private String title;

    //表示小节 一对多
    private List<VideoVo> children = new ArrayList<>();
}

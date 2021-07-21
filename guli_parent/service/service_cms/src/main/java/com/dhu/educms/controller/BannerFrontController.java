package com.dhu.educms.controller;

import com.dhu.commonutils.R;
import com.dhu.educms.entity.CrmBanner;
import com.dhu.educms.service.CrmBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 前台显示banner信息
 */
@RestController
@RequestMapping("/educms/bannerfront")
public class BannerFrontController {

    @Autowired
    private CrmBannerService bannerService;


    /**
     * 查询出所有的banner
     * @return
     */
    @GetMapping("/getAllBanner")
    public R getAllBanner(){

        List<CrmBanner> list = bannerService.selectAllBanner();
        for(CrmBanner item : list){
            System.out.println(item.getImageUrl());
            System.out.println(item.getLinkUrl());
            System.out.println(item.getId());
        }
        return R.ok().data("list",list);
    }
}

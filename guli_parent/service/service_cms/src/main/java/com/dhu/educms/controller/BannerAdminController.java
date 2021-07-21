package com.dhu.educms.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dhu.commonutils.R;
import com.dhu.educms.entity.CrmBanner;
import com.dhu.educms.service.CrmBannerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author Yushui
 * @since 2021-06-07
 */
@RestController
@RequestMapping("/educms/banneradmin")
public class BannerAdminController {


    @Autowired
    private CrmBannerService bannerService;


    /**
     * 分页查询bannner
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("/pageBanner/{page}/{limit}")
    public R pageBanner(@PathVariable long page,@PathVariable long limit){

        Page<CrmBanner> pageBanner = new Page<>(page,limit);
        bannerService.page(pageBanner,null);
        return R.ok().data("items",pageBanner.getRecords()).data("total",pageBanner.getTotal());
    }


    /**
     * 根据id查询banner
     * @param id
     * @return
     */
    @ApiOperation(value = "获取Banner")
    @GetMapping("getBanner/{id}")
    public R get(@PathVariable String id) {
        CrmBanner banner = bannerService.getById(id);
        return R.ok().data("item", banner);
    }


    /**
     * 新增banner
     * @param banner
     * @return
     */
    @ApiOperation(value = "新增Banner")
    @PostMapping("/addBanner")
    public R addBanner(@RequestBody CrmBanner banner) {
        bannerService.save(banner);
        return R.ok();
    }


    /**
     * 修改banner
     * @param banner
     * @return
     */
    @ApiOperation(value = "修改Banner")
    @PutMapping("/updateBanner")
    public R updateById(@RequestBody CrmBanner banner) {
        bannerService.updateById(banner);
        return R.ok();
    }


    /**
     * 删除banner
     * @param id
     * @return
     */
    @ApiOperation(value = "删除Banner")
    @DeleteMapping("removeBanner/{id}")
    public R remove(@PathVariable String id) {
        bannerService.removeById(id);
        return R.ok();
    }
}


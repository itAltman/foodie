package me.atm.foodie.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import me.atm.common.enums.YesOrNoEnum;
import me.atm.common.utils.JsonResult;
import me.atm.pojo.Carousel;
import me.atm.pojo.Category;
import me.atm.pojo.vo.CategoryVO;
import me.atm.pojo.vo.NewItemsVO;
import me.atm.service.CarouselService;
import me.atm.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Altman
 * @date 2019/11/07
 **/
@Api(value = "首页模块value", tags = {"首页模块tags"})
@RestController
@RequestMapping("/index")
public class IndexController {

    @Resource
    private CarouselService carouselService;

    @Resource
    private CategoryService categoryService;

    @ApiOperation(value = "获取首页轮播图列表", notes = "获取首页轮播图列表", httpMethod = "GET")
    @GetMapping("/carousel")
    public JsonResult carousel() {
        List<Carousel> list = carouselService.queryAll(YesOrNoEnum.YES.type);
        return JsonResult.ok(list);
    }

    @ApiOperation(value = "获取商品分类(一级分类)", notes = "获取商品分类(一级分类)", httpMethod = "GET")
    @GetMapping("/cats")
    public JsonResult cats() {
        List<Category> list = categoryService.queryAllRootLevelCat();
        return JsonResult.ok(list);
    }

    @ApiOperation(value = "获取商品子分类", notes = "获取商品子分类", httpMethod = "GET")
    @GetMapping("/subCat/{rootCatId}")
    public JsonResult subCat(@ApiParam(value = "一级分类id", required = true) @PathVariable Integer rootCatId) {
        if (rootCatId == null) {
            JsonResult.errorMsg("一级分类id不能为空");
        }
        List<CategoryVO> subCatList = categoryService.getSubCatList(rootCatId);
        return JsonResult.ok(subCatList);
    }

    @ApiOperation(value = "查询每个一级分类下的最新6条商品数据", notes = "查询每个一级分类下的最新6条商品数据", httpMethod = "GET")
    @GetMapping("/sixNewItems/{rootCatId}")
    public JsonResult sixNewItems(
            @ApiParam(name = "rootCatId", value = "一级分类id", required = true)
            @PathVariable Integer rootCatId) {

        if (rootCatId == null) {
            return JsonResult.errorMsg("分类不存在");
        }

        List<NewItemsVO> list = categoryService.getSixNewItemsLazy(rootCatId);
        return JsonResult.ok(list);
    }
}

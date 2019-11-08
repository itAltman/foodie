package me.atm.foodie.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import me.atm.common.enums.YesOrNoEnum;
import me.atm.common.utils.JsonResult;
import me.atm.pojo.*;
import me.atm.pojo.vo.CategoryVO;
import me.atm.pojo.vo.CommentLevelCountsVO;
import me.atm.pojo.vo.ItemInfoVO;
import me.atm.pojo.vo.NewItemsVO;
import me.atm.service.CarouselService;
import me.atm.service.CategoryService;
import me.atm.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Altman
 * @date 2019/11/08
 **/
@Api(value = "商品模块", tags = {"商品模块"})
@RestController
@RequestMapping("/items")
public class ItemsController {

    @Resource
    private ItemService itemService;

    /**
     * value - 接口名词
     * notes - 接口说明
     */
    @ApiOperation(value = "获取商品信息", notes = "获取商品的图片、价格、产品参数、详情细节", httpMethod = "GET")
    @GetMapping("/info/{itemId}")
    public JsonResult info(@ApiParam(value = "商品id", required = true) @PathVariable String itemId) {
        if (StringUtils.isBlank(itemId)) {
            return JsonResult.errorMsg("商品id不能为空");
        }
        Items items = itemService.queryItemById(itemId);
        List<ItemsImg> itemsImgs = itemService.queryItemImgList(itemId);
        ItemsParam itemsParam = itemService.queryItemParam(itemId);
        List<ItemsSpec> itemsSpecs = itemService.queryItemSpecList(itemId);
        ItemInfoVO itemInfoVO = ItemInfoVO.builder()
                .item(items)
                .itemImgList(itemsImgs)
                .itemParams(itemsParam)
                .itemSpecList(itemsSpecs)
                .build();
        return JsonResult.ok(itemInfoVO);
    }

    @ApiOperation(value = "商品评价等级数量", notes = "获取评价的总数、好评、中评、差评", httpMethod = "GET")
    @GetMapping("/commentLevel/{itemId}")
    public JsonResult commentLevel(@ApiParam(value = "商品id", required = true) @PathVariable String itemId) {
        if (StringUtils.isBlank(itemId)) {
            return JsonResult.errorMsg("商品id不能为空");
        }
        CommentLevelCountsVO commentLevelCountsVO = itemService.queryCommentCounts(itemId);
        return JsonResult.ok(commentLevelCountsVO);
    }
}

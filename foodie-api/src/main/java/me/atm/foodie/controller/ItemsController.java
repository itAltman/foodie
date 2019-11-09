package me.atm.foodie.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import me.atm.common.utils.JsonResult;
import me.atm.common.utils.PagedGridResult;
import me.atm.pojo.Items;
import me.atm.pojo.ItemsImg;
import me.atm.pojo.ItemsParam;
import me.atm.pojo.ItemsSpec;
import me.atm.pojo.vo.CommentLevelCountsVO;
import me.atm.pojo.vo.ItemInfoVO;
import me.atm.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Altman
 * @date 2019/11/08
 **/
@Api(value = "商品模块", tags = {"商品模块"})
@RestController
@RequestMapping("/items")
public class ItemsController extends BaseController {

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
    @GetMapping("/commentLevel")
    public JsonResult commentLevel(@ApiParam(value = "商品id", required = true) @RequestParam String itemId) {
        if (StringUtils.isBlank(itemId)) {
            return JsonResult.errorMsg("商品id不能为空");
        }
        CommentLevelCountsVO countsVO = itemService.queryCommentCounts(itemId);
        return JsonResult.ok(countsVO);
    }

    @ApiOperation(value = "查询商品评论", notes = "查询商品评论", httpMethod = "GET")
    @GetMapping("/comments")
    public JsonResult comments(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @RequestParam String itemId,
            @ApiParam(name = "level", value = "评价等级", required = false)
            @RequestParam Integer level,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
            @RequestParam Integer pageSize) {

        if (StringUtils.isBlank(itemId)) {
            return JsonResult.errorMsg(null);
        }

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult grid = itemService.queryItemComments(itemId, level, page, pageSize);

        return JsonResult.ok(grid);
    }

    @ApiOperation(value = "搜索商品列表", notes = "搜索商品列表", httpMethod = "GET")
    @GetMapping("/search")
    public JsonResult search(
            @ApiParam(name = "keywords", value = "关键字", required = true)
            @RequestParam String keywords,
            @ApiParam(name = "sort", value = "排序。默认不传、c -> 销量、p -> 价格", required = false)
            @RequestParam String sort,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
            @RequestParam Integer pageSize) {

        if (StringUtils.isBlank(keywords)) {
            return JsonResult.errorMsg(null);
        }

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }

        PagedGridResult grid = itemService.searchItemsByKeywords(keywords, sort, page, pageSize);

        return JsonResult.ok(grid);
    }

    @ApiOperation(value = "通过分类id搜索商品列表", notes = "通过分类id搜索商品列表", httpMethod = "GET")
    @GetMapping("/catItems")
    public JsonResult catItems(
            @ApiParam(name = "catId", value = "三级分类id", required = true)
            @RequestParam Integer catId,
            @ApiParam(name = "sort", value = "排序", required = false)
            @RequestParam String sort,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
            @RequestParam Integer pageSize) {

        if (catId == null) {
            return JsonResult.errorMsg(null);
        }

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }

        PagedGridResult grid = itemService.searchItemsByThirdCat(catId, sort, page, pageSize);

        return JsonResult.ok(grid);
    }
}

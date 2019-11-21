package me.atm.foodie.controller.center;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import me.atm.common.enums.YesOrNoEnum;
import me.atm.common.utils.JsonResult;
import me.atm.common.utils.PagedGridResult;
import me.atm.foodie.controller.BaseController;
import me.atm.pojo.OrderItems;
import me.atm.pojo.Orders;
import me.atm.pojo.bo.center.OrderItemsCommentBO;
import me.atm.service.center.MyCommentsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(value = "用户中心-我的评价", tags = {"用户中心-我的评价相关接口"})
@RestController
@RequestMapping("mycomments")
public class MyCommentsController extends BaseController {

    @Resource
    private MyCommentsService myCommentsService;

    @ApiOperation(value = "渲染评论页内容", notes = "查询用户的某个订单下每个商品的信息", httpMethod = "POST")
    @PostMapping("/pending")
    public JsonResult pending(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId) {

        // 判断用户和订单是否关联
        JsonResult jsonResult = checkUserOrder(userId, orderId);
        if (jsonResult.getStatus() != HttpStatus.OK.value()) {
            return jsonResult;
        }

        // 判断该笔订单是否已经评价过，评价过了就不再继续
        Orders orders = (Orders) jsonResult.getData();
        if (orders.getIsComment() == YesOrNoEnum.YES.type) {
            return JsonResult.errorMsg("该笔订单已经评价");
        }
        List<OrderItems> orderItemsList = myCommentsService.getPendingComment(orderId);
        return JsonResult.ok(orderItemsList);
    }


    @ApiOperation(value = "保存评论", notes = "保存用户的某个订单下每个子订单的评价信息", httpMethod = "POST")
    @PostMapping("/saveList")
    public JsonResult saveList(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId,
            @RequestBody List<OrderItemsCommentBO> commentList) {

        // 判断用户和订单是否关联
        JsonResult jsonResult = checkUserOrder(userId, orderId);
        if (jsonResult.getStatus() != HttpStatus.OK.value()) {
            return jsonResult;
        }

        // 判断该笔订单是否已经评价过，评价过了就不再继续
        Orders orders = (Orders) jsonResult.getData();
        if (orders.getIsComment() == YesOrNoEnum.YES.type) {
            return JsonResult.errorMsg("该笔订单已经评价");
        }

        if (commentList == null || commentList.isEmpty()) {
            return JsonResult.errorMsg("评价信息不能为空");
        }
        myCommentsService.saveComments(orderId, userId, commentList);
        return JsonResult.ok();
    }

    @ApiOperation(value = "查询评价列表", notes = "查询评价列表", httpMethod = "POST")
    @PostMapping("/query")
    public JsonResult query(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
            @RequestParam Integer pageSize) {

        if (StringUtils.isBlank(userId)) {
            return JsonResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult grid = myCommentsService.queryMyComments(userId, page, pageSize);

        return JsonResult.ok(grid);
    }

}

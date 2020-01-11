package me.atm.foodie.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.atm.common.utils.JsonResult;
import me.atm.common.utils.JsonUtils;
import me.atm.common.utils.RedisOperator;
import me.atm.pojo.bo.ShopcartBO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Altman
 * @date 2019/11/11
 **/
@Api(value = "购物车", tags = {"购物车模块"})
@RestController
@RequestMapping("/shopcart")
public class ShopcartController extends BaseController {

    @Resource
    private RedisOperator redisOperator;

    /**
     * @RequestParam 代表接收前端 ?a=1的值
     * @RequestBody 代表接收前端 json 格式的值
     */
    @ApiOperation(value = "添加商品到购物车", notes = "当用户已经登录时，添加商品到购物车需要将信息同步至缓存中", httpMethod = "POST")
    @PostMapping("/add")
    public JsonResult add(
            @RequestParam String userId,
            @RequestBody ShopcartBO shopcartBO,
            HttpServletRequest request,
            HttpServletResponse response) {

        if (StringUtils.isBlank(userId)) {
            return JsonResult.errorMsg("");
        }

        System.out.println(shopcartBO);

        // 前端用户在登录的情况下，添加商品到购物车，会同时在后端同步购物车到redis缓存
        // 需要判断当前购物车中包含已经存在的商品，如果存在则累加购买数量
        String shopcartJson = redisOperator.get(FOODIE_SHOPCART + ":" + userId);
        List<ShopcartBO> shopcartList;
        if (StringUtils.isNotBlank(shopcartJson)) {
            // redis中已经有购物车了
            shopcartList = JsonUtils.jsonToList(shopcartJson, ShopcartBO.class);
            // 判断购物车中是否存在已有商品，如果有的话counts累加
            boolean isHaving = false;
            for (ShopcartBO sc: shopcartList) {
                String tmpSpecId = sc.getSpecId();
                if (tmpSpecId.equals(shopcartBO.getSpecId())) {
                    sc.setBuyCounts(sc.getBuyCounts() + shopcartBO.getBuyCounts());
                    isHaving = true;
                }
            }
            if (!isHaving) {
                shopcartList.add(shopcartBO);
            }
        } else {
            // redis中没有购物车
            shopcartList = new ArrayList<>();
            // 直接添加到购物车中
            shopcartList.add(shopcartBO);
        }

        // 覆盖现有redis中的购物车
        redisOperator.set(FOODIE_SHOPCART + ":" + userId, JsonUtils.objectToJson(shopcartList));

        return JsonResult.ok();
    }

    @ApiOperation(value = "从购物车中删除商品", notes = "从购物车中删除商品", httpMethod = "POST")
    @PostMapping("/del")
    public JsonResult del(
            @RequestParam String userId,
            @RequestParam String itemSpecId,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)) {
            return JsonResult.errorMsg("参数不能为空");
        }

        // 用户在页面删除购物车中的商品数据，如果此时用户已经登录，则需要同步删除redis购物车中的商品
        String shopcartJson = redisOperator.get(FOODIE_SHOPCART + ":" + userId);
        if (StringUtils.isNotBlank(shopcartJson)) {
            // redis中已经有购物车了
            List<ShopcartBO> shopcartList = JsonUtils.jsonToList(shopcartJson, ShopcartBO.class);
            // 判断购物车中是否存在已有商品，如果有的话则删除
            for (ShopcartBO sc: shopcartList) {
                String tmpSpecId = sc.getSpecId();
                if (tmpSpecId.equals(itemSpecId)) {
                    shopcartList.remove(sc);
                    break;
                }
            }
            // 覆盖现有redis中的购物车
            redisOperator.set(FOODIE_SHOPCART + ":" + userId, JsonUtils.objectToJson(shopcartList));
        }

        return JsonResult.ok();
    }
}

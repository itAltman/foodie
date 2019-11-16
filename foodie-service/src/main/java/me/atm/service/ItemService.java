package me.atm.service;

import me.atm.common.utils.PagedGridResult;
import me.atm.pojo.Items;
import me.atm.pojo.ItemsImg;
import me.atm.pojo.ItemsParam;
import me.atm.pojo.ItemsSpec;
import me.atm.pojo.vo.CommentLevelCountsVO;
import me.atm.pojo.vo.ItemCommentVO;
import me.atm.pojo.vo.ShopcartVO;

import java.util.List;

/**
 * @author Altman
 * @date 2019/11/08
 **/
public interface ItemService {
    /**
     * 根据商品ID查询详情
     *
     * @param itemId
     * @return
     */
    public Items queryItemById(String itemId);

    /**
     * 根据商品id查询商品图片列表
     *
     * @param itemId
     * @return
     */
    public List<ItemsImg> queryItemImgList(String itemId);

    /**
     * 根据商品id查询商品规格
     *
     * @param itemId
     * @return
     */
    public List<ItemsSpec> queryItemSpecList(String itemId);

    /**
     * 根据商品id查询商品参数
     *
     * @param itemId
     * @return
     */
    public ItemsParam queryItemParam(String itemId);

    /**
     * 根据商品id查询商品的评价等级数量
     *
     * @param itemId
     */
    public CommentLevelCountsVO queryCommentCounts(String itemId);

    /**
     * 根据商品id/评价等级查询商品的评价信息
     *
     * @param itemId
     */
    PagedGridResult queryItemComments(String itemId, Integer commentLevel, Integer page, Integer pageSize);

    /**
     * 根据关键词搜索商品列表
     *
     * @param keywords
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult searchItemsByKeywords(String keywords, String sort, Integer page, Integer pageSize);

    /**
     * 根据三级类目id搜索商品列表
     *
     * @param catId
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult searchItemsByThirdCat(Integer catId, String sort, Integer page, Integer pageSize);

    /**
     * 根据商品规格ids查找最新的商品数据
     *
     * @param itemSpecIds
     * @return
     */
    List<ShopcartVO> queryItemsBySpecIds(String itemSpecIds);

    /**
     * 根据商品规格id获取规格对象的具体信息
     * @param specId
     * @return
     */
    ItemsSpec queryItemSpecById(String specId);

    /**
     * 根据商品id获得商品图片主图url
     * @param itemId
     * @return
     */
    String queryItemMainImgById(String itemId);

    /**
     * 减少库存
     * @param specId
     * @param buyCounts
     */
    void decreaseItemSpecStock(String specId, int buyCounts);
}

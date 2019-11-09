package me.atm.service;

import me.atm.common.utils.PagedGridResult;
import me.atm.pojo.Items;
import me.atm.pojo.ItemsImg;
import me.atm.pojo.ItemsParam;
import me.atm.pojo.ItemsSpec;
import me.atm.pojo.vo.CommentLevelCountsVO;
import me.atm.pojo.vo.ItemCommentVO;

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
}
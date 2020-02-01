package me.atm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Splitter;
import me.atm.common.enums.CommentLevelEnum;
import me.atm.common.enums.YesOrNoEnum;
import me.atm.common.utils.DesensitizationUtil;
import me.atm.common.utils.PagedGridResult;
import me.atm.mapper.*;
import me.atm.pojo.*;
import me.atm.pojo.doc.ItemsDoc;
import me.atm.pojo.vo.CommentLevelCountsVO;
import me.atm.pojo.vo.ItemCommentVO;
import me.atm.pojo.vo.SearchItemsVO;
import me.atm.pojo.vo.ShopcartVO;
import me.atm.service.ItemService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Altman
 * @date 2019/11/05
 **/
@Service
public class ItemServiceImpl implements ItemService {

    // ES 模板类
    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    @Resource
    private ItemsMapper itemsMapper;

    @Resource
    private ItemsMapperCustom itemsMapperCustom;

    @Resource
    private ItemsImgMapper itemsImgMapper;

    @Resource
    private ItemsSpecMapper itemsSpecMapper;

    @Resource
    private ItemsParamMapper itemsParamMapper;

    @Resource
    private ItemsCommentsMapper itemsCommentsMapper;

    @Resource
    private ItemsCommentsMapperCustom itemsCommentsMapperCustom;

    @Override
    public Items queryItemById(String itemId) {
        return itemsMapper.selectByPrimaryKey(itemId);
    }

    @Override
    public List<ItemsImg> queryItemImgList(String itemId) {
        Example example = new Example(ItemsImg.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        return itemsImgMapper.selectByExample(example);
    }

    @Override
    public List<ItemsSpec> queryItemSpecList(String itemId) {
        Example example = new Example(ItemsSpec.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        return itemsSpecMapper.selectByExample(example);
    }

    @Override
    public ItemsParam queryItemParam(String itemId) {
        Example example = new Example(ItemsParam.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId", itemId);

        return itemsParamMapper.selectOneByExample(example);
    }

    @Override
    public CommentLevelCountsVO queryCommentCounts(String itemId) {
        Integer goodCounts = getCommentCounts(itemId, CommentLevelEnum.GOOD.type);
        Integer normalCounts = getCommentCounts(itemId, CommentLevelEnum.NORMAL.type);
        Integer badCounts = getCommentCounts(itemId, CommentLevelEnum.BAD.type);
        Integer totalCounts = goodCounts + normalCounts + badCounts;

        CommentLevelCountsVO countsVO = CommentLevelCountsVO.builder()
                .totalCounts(totalCounts)
                .goodCounts(goodCounts)
                .normalCounts(normalCounts)
                .badCounts(badCounts)
                .build();

        return countsVO;
    }

    @Override
    public PagedGridResult queryItemComments(String itemId, Integer commentLevel, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("itemId", itemId);
        map.put("level", commentLevel);
        /**
         * page: 第几页
         * pageSize: 每页显示条数
         */
        PageHelper.startPage(page, pageSize);
        List<ItemCommentVO> itemCommentVOS = itemsCommentsMapperCustom.queryItemComments(map);
        itemCommentVOS.forEach(vo -> DesensitizationUtil.commonDisplay(vo.getNickname()));
        return setterPagedGrid(itemCommentVOS, page);
    }

    @Override
    public PagedGridResult searchItemsByKeywords(String keywords, String sort, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("keywords", keywords);
        map.put("sort", sort);
        /**
         * page: 第几页
         * pageSize: 每页显示条数
         */
        PageHelper.startPage(page, pageSize);
        List<SearchItemsVO> searchItemsVOS = itemsMapperCustom.searchItemsByKeywords(map);
        return setterPagedGrid(searchItemsVOS, page);
    }

    @Override
    public PagedGridResult searchItemsByThirdCat(Integer catId, String sort, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("catId", catId);
        map.put("sort", sort);

        PageHelper.startPage(page, pageSize);
        List<SearchItemsVO> searchItemsVOS = itemsMapperCustom.searchItemsByThirdCat(map);
        return setterPagedGrid(searchItemsVOS, page);
    }

    @Override
    public List<ShopcartVO> queryItemsBySpecIds(String itemSpecIds) {
        List<String> itemSpecIdList = Splitter.on(",").splitToList(itemSpecIds);
        return itemsMapperCustom.queryItemsBySpecIds(itemSpecIdList);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsSpec queryItemSpecById(String specId) {
        return itemsSpecMapper.selectByPrimaryKey(specId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public String queryItemMainImgById(String itemId) {
        ItemsImg itemsImg = new ItemsImg();
        itemsImg.setItemId(itemId);
        itemsImg.setIsMain(YesOrNoEnum.YES.type);
        ItemsImg result = itemsImgMapper.selectOne(itemsImg);
        return result != null ? result.getUrl() : "";
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void decreaseItemSpecStock(String specId, int buyCounts) {

        // synchronized 不推荐使用，集群下无用，性能低下
        // 锁数据库: 不推荐，导致数据库性能低下
        // 分布式锁 zookeeper redis

        // lockUtil.getLock(); -- 加锁

        // 1. 查询库存
//        int stock = 10;

        // 2. 判断库存，是否能够减少到0以下
//        if (stock - buyCounts < 0) {
        // 提示用户库存不够
//            10 - 3 -3 - 5 = -1
//        }

        // lockUtil.unLock(); -- 解锁


        int result = itemsMapperCustom.decreaseItemSpecStock(specId, buyCounts);
        if (result != 1) {
            throw new RuntimeException("订单创建失败，原因：库存不足!");
        }
    }

    @Override
    public PagedGridResult searchItemsFromES(String keywords, String sort, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of((page-1), pageSize);
        String itemNameFiled = "itemName";

        SortBuilder sortBuilder = null;
        if (sort.equals("c")) {
            sortBuilder = new FieldSortBuilder("sellCounts")
                    .order(SortOrder.DESC);
        } else if (sort.equals("p")) {
            sortBuilder = new FieldSortBuilder("price")
                    .order(SortOrder.ASC);
        } else {
            sortBuilder = new FieldSortBuilder("itemName.keyword")
                    .order(SortOrder.ASC);
        }

        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery(itemNameFiled, keywords))
                .withHighlightFields(new HighlightBuilder.Field(itemNameFiled))
                .withPageable(pageable)
                .withSort(sortBuilder)
                .build();
        AggregatedPage<ItemsDoc> itemsDocs = elasticsearchTemplate.queryForPage(query, ItemsDoc.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                List<ItemsDoc> itemHighLightList = new ArrayList<>();
                SearchHits hits = searchResponse.getHits();
                for (SearchHit hit : hits) {
                    HighlightField highlightField = hit.getHighlightFields().get(itemNameFiled);
                    String itemName = highlightField.getFragments()[0].toString();

                    String itemId = (String) hit.getSourceAsMap().get("itemId");
                    String imgUrl = (String) hit.getSourceAsMap().get("imgUrl");
                    Integer price = (Integer) hit.getSourceAsMap().get("price");
                    Integer sellCounts = (Integer) hit.getSourceAsMap().get("sellCounts");

                    itemHighLightList.add(
                            ItemsDoc.builder()
                                    .itemId(itemId).itemName(itemName)
                                    .imgUrl(imgUrl).price(price)
                                    .sellCounts(sellCounts).build()
                    );
                }
                return new AggregatedPageImpl<>(
                        (List<T>) itemHighLightList,
                        pageable,
                        searchResponse.getHits().totalHits);
            }
        });

        PagedGridResult gridResult = new PagedGridResult();
        gridResult.setRows(itemsDocs.getContent());
        gridResult.setPage(page);
        gridResult.setTotal(itemsDocs.getTotalPages());
        gridResult.setRecords(itemsDocs.getTotalElements());

        return gridResult;
    }

    private PagedGridResult setterPagedGrid(List<?> list, Integer page) {
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult grid = new PagedGridResult();
        grid.setPage(page);
        grid.setRows(list);
        grid.setTotal(pageList.getPages());
        grid.setRecords(pageList.getTotal());
        return grid;
    }

    Integer getCommentCounts(String itemId, Integer level) {
        ItemsComments condition = new ItemsComments();
        condition.setItemId(itemId);
        if (level != null) {
            condition.setCommentLevel(level);
        }
        return itemsCommentsMapper.selectCount(condition);
    }
}

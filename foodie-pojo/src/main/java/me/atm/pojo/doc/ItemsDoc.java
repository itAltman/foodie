package me.atm.pojo.doc;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 商品 ES 文档实体
 *
 * @author Altman
 * @date 2020/02/01
 **/
// 此 type 切记不能写成 _doc。 否则结果不正确
@Document(indexName = "foodie-items-ik", type = "doc", createIndex = false)
@Data
@Builder
public class ItemsDoc {
    // 商品id
    @Id
    @Field(store = true, type = FieldType.Text, index = false)
    private String itemId;
    // 商品名称 需要倒排索引 用于搜索
    @Field(store = true, type = FieldType.Text, index = true)
    private String itemName;

    // 商品主图
    @Field(store = true, type = FieldType.Text, index = false)
    private String imgUrl;

    // 商品价格
    @Field(store = true, type = FieldType.Integer, index = false)
    private Integer price;

     // 商品销量
    @Field(store = true, type = FieldType.Integer, index = false)
    private Integer sellCounts;

    @Tolerate
    public ItemsDoc(){
    }
}

package me.atm.common.enums;

/**
 * 评价 枚举
 *
 * @author Altman
 * @create 2019/11/05
 **/
public enum CommentLevelEnum {
    GOOD(1, "好评"),

    NORMAL(2, "中评"),

    BAD(3, "差评");

    public final Integer type;

    public final String value;

    CommentLevelEnum(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
package me.atm.common.enums;

/**
 * 性别 枚举
 *
 * @author Altman
 * @create 2019/11/05
 **/
public enum SexEnum {
    woman(0, "女"),

    man(1, "男"),

    secret(2, "保密");

    public final Integer type;

    public final String value;

    SexEnum(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
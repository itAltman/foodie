package me.atm.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户新增或修改地址的BO
 *
 * @author Altman
 * @date 2019/11/11
 **/
@ApiModel(value = "地址BO", description = "用户新增或修改地址的前端请求对象")
@Data
public class AddressBO {
    @ApiModelProperty(value = "地址id")
    private String addressId;
    @ApiModelProperty(value = "用戶id")
    private String userId;
    @ApiModelProperty(value = "收件人")
    private String receiver;
    @ApiModelProperty(value = "联系人电话")
    private String mobile;
    @ApiModelProperty(value = "份")
    private String province;
    @ApiModelProperty(value = "市")
    private String city;
    @ApiModelProperty(value = "区")
    private String district;
    @ApiModelProperty(value = "详细地址")
    private String detail;
}

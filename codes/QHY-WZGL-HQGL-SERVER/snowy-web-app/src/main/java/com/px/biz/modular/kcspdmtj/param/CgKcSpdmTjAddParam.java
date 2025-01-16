/*
 * Copyright [2022] [https://www.xiaonuo.vip]
 *
 * Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Snowy源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.xiaonuo.vip
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队xiaonuobase@qq.com商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package com.px.biz.modular.kcspdmtj.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * cg_kc_spdm_tj添加参数
 *
 * @author 品讯科技
 * @date 2024-08
 **/
@Getter
@Setter
public class CgKcSpdmTjAddParam {

    /** 商品代码 */
    @ApiModelProperty(value = "商品代码", position = 1)
    private String spdm;

    /** 商品名称 */
    @ApiModelProperty(value = "商品名称", position = 2)
    private String spmc;

    /** 商品规格 */
    @ApiModelProperty(value = "商品规格", position = 3)
    private String spgg;

    /** 商品大类代码（01） */
    @ApiModelProperty(value = "商品大类代码（01）", position = 4)
    private String ppcd;

    /** 包装率 */
    @ApiModelProperty(value = "包装率", position = 5)
    private String bzl;

    /** 商品大类代码（01） */
    @ApiModelProperty(value = "商品大类代码（01）", position = 6)
    private String dldm;

    /** 商品大类名称（食品类、维修材料类、园林绿化类） */
    @ApiModelProperty(value = "商品大类名称（食品类、维修材料类、园林绿化类）", position = 7)
    private String dlmc;

    /** 商品类别 */
    @ApiModelProperty(value = "商品类别", position = 8)
    private String lbdm;

    /** 类别名称 */
    @ApiModelProperty(value = "类别名称", position = 9)
    private String lbmc;

    /** 计量单位 */
    @ApiModelProperty(value = "计量单位", position = 10)
    private String jldw;

    /** 前期进价 */
    @ApiModelProperty(value = "前期进价", position = 11)
    private BigDecimal oldjj;

    /** 最新调价 */
    @ApiModelProperty(value = "最新调价", position = 12)
    private BigDecimal zxtj;

    /** 供应单价 */
    @ApiModelProperty(value = "供应单价", position = 13)
    private BigDecimal gydj;

    /** 调价日期 */
    @ApiModelProperty(value = "调价日期", position = 14)
    private Date tjrq;

    /** 调价人 */
    @ApiModelProperty(value = "调价人", position = 15)
    private String tjr;

    /** 调价审核日期 */
    @ApiModelProperty(value = "调价审核日期", position = 16)
    private Date tjshrq;

    /** 调价审核人 */
    @ApiModelProperty(value = "调价审核人", position = 17)
    private String tjshr;

}

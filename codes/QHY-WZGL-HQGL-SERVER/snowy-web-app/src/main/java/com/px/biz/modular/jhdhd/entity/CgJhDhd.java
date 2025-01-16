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
package com.px.biz.modular.jhdhd.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.Date;

/**
 * cg_jh_dhd实体
 *
 * @author 品讯科技
 * @date 2024-08
 **/
@Getter
@Setter
@TableName("cg_jh_dhd")
public class CgJhDhd {

    /** 采购单号(4位年+2位月+2位日+4位流水号) */
    @ApiModelProperty(value = "采购单号(4位年+2位月+2位日+4位流水号)", position = 1)
    private String cgdh;

    /** 采购日期（送货日期） */
    @ApiModelProperty(value = "采购日期（送货日期）", position = 2)
    private Date cgrq;

    /** 订货人 */
    @ApiModelProperty(value = "订货人", position = 3)
    private String dhr;

    /** 订货日期 */
    @ApiModelProperty(value = "订货日期", position = 4)
    private Date dhrq;

    /** 审核人 */
    @ApiModelProperty(value = "审核人", position = 5)
    private String shry;

    /** 审核日期 */
    @ApiModelProperty(value = "审核日期", position = 6)
    private Date shrq;

    /** GYSQRRQ */
    @ApiModelProperty(value = "GYSQRRQ", position = 7)
    private Date gysqrrq;

    /** 供应商代码 */
    @ApiModelProperty(value = "供应商代码", position = 8)
    private String gysdm;

    /** 供应商名称 */
    @ApiModelProperty(value = "供应商名称", position = 9)
    private String gysmc;

    /** 商品金额 */
    @ApiModelProperty(value = "商品金额", position = 10)
    private BigDecimal spje;

    /** 状态（订货中、已订货、已送货） */
    @ApiModelProperty(value = "状态（订货中、已订货、已送货）", position = 11)
    private String workstate;

    /** BZ */
    @ApiModelProperty(value = "BZ", position = 12)
    private String bz;

    /** CGLX */
    @ApiModelProperty(value = "CGLX", position = 13)
    private String cglx;

    /** ID */
    @TableId
    @ApiModelProperty(value = "ID", position = 14)
    private String id;

    /** 联系电话 */
    @ApiModelProperty(value = "联系电话", position = 22)
    @TableField(exist = false)
    private String dh;

    /** 联系电话 */
    @ApiModelProperty(value = "联系人", position = 23)
    @TableField(exist = false)
    private String lxr;
}

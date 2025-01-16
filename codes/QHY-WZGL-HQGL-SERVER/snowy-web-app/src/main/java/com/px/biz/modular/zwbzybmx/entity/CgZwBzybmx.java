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
package com.px.biz.modular.zwbzybmx.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.Date;

/**
 * cg_zw_bzybmx实体
 *
 * @author 品讯科技
 * @date 2024-08
 **/
@Getter
@Setter
@TableName("cg_zw_bzybmx")
public class CgZwBzybmx {

    /** YBBH */
    @ApiModelProperty(value = "YBBH", position = 1)
    private String ybbh;

    /** RQ */
    @ApiModelProperty(value = "RQ", position = 2)
    private Date rq;

    /** CZY */
    @ApiModelProperty(value = "CZY", position = 3)
    private String czy;

    /** YJBMDM */
    @ApiModelProperty(value = "YJBMDM", position = 4)
    private String yjbmdm;

    /** YJBMMC */
    @ApiModelProperty(value = "YJBMMC", position = 5)
    private String yjbmmc;

    /** BMDM */
    @ApiModelProperty(value = "BMDM", position = 6)
    private String bmdm;

    /** BMMC */
    @ApiModelProperty(value = "BMMC", position = 7)
    private String bmmc;

    /** BZDM */
    @ApiModelProperty(value = "BZDM", position = 8)
    private String bzdm;

    /** BZMC */
    @ApiModelProperty(value = "BZMC", position = 9)
    private String bzmc;

    /** TJLB */
    @ApiModelProperty(value = "TJLB", position = 10)
    private String tjlb;

    /** LBDM */
    @ApiModelProperty(value = "LBDM", position = 11)
    private String lbdm;

    /** LBMC */
    @ApiModelProperty(value = "LBMC", position = 12)
    private String lbmc;

    /** LBLX */
    @ApiModelProperty(value = "LBLX", position = 13)
    private String lblx;

    /** LBXH */
    @ApiModelProperty(value = "LBXH", position = 14)
    private String lbxh;

    /** OUTJE */
    @ApiModelProperty(value = "OUTJE", position = 15)
    private BigDecimal outje;

    /** INJE */
    @ApiModelProperty(value = "INJE", position = 16)
    private BigDecimal inje;

    /** BZ */
    @ApiModelProperty(value = "BZ", position = 17)
    private String bz;

    /** ID */
    @TableId
    @ApiModelProperty(value = "ID", position = 18)
    private String id;
}

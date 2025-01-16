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
package com.px.biz.modular.zwbmybmx.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.Date;

/**
 * cg_zw_bmybmx实体
 *
 * @author 品讯科技
 * @date 2024-08
 **/
@Getter
@Setter
@TableName("cg_zw_bmybmx")
public class CgZwBmybmx {

    /** 日报编号（如：20100406） */
    @ApiModelProperty(value = "日报编号（如：20100406）", position = 1)
    private String ybbh;

    /** 生成日期 */
    @ApiModelProperty(value = "生成日期", position = 2)
    private Date rq;

    /** 操作员 */
    @ApiModelProperty(value = "操作员", position = 3)
    private String czy;

    /** 一级部门代码（如饮服中心） */
    @ApiModelProperty(value = "一级部门代码（如饮服中心）", position = 4)
    private String yjbmdm;

    /** 一级部门名称 */
    @ApiModelProperty(value = "一级部门名称", position = 5)
    private String yjbmmc;

    /** 部门代码（食堂代码）：如0101 */
    @ApiModelProperty(value = "部门代码（食堂代码）：如0101", position = 6)
    private String bmdm;

    /** 部门名称（东苑食堂、学生食堂、圆中苑、西校区等） */
    @ApiModelProperty(value = "部门名称（东苑食堂、学生食堂、圆中苑、西校区等）", position = 7)
    private String bmmc;

    /** 统计类别（0表示自动计算统计的种类，1表示手工输入的类别） */
    @ApiModelProperty(value = "统计类别（0表示自动计算统计的种类，1表示手工输入的类别）", position = 8)
    private String tjlb;

    /** 商品类别 */
    @ApiModelProperty(value = "商品类别", position = 9)
    private String lbdm;

    /** 类别名称 */
    @ApiModelProperty(value = "类别名称", position = 10)
    private String lbmc;

    /** 类别类型（荤菜类、素菜类、调料类、主食类、水电气类、低耗类、酬金类） */
    @ApiModelProperty(value = "类别类型（荤菜类、素菜类、调料类、主食类、水电气类、低耗类、酬金类）", position = 11)
    private String lblx;

    /** 显示顺序 */
    @ApiModelProperty(value = "显示顺序", position = 12)
    private String lbxh;

    /** 支出金额 */
    @ApiModelProperty(value = "支出金额", position = 13)
    private BigDecimal outje;

    /** 收入金额 */
    @ApiModelProperty(value = "收入金额", position = 14)
    private BigDecimal inje;

    /** BZ */
    @ApiModelProperty(value = "BZ", position = 15)
    private String bz;

    /** ID */
    @TableId
    @ApiModelProperty(value = "ID", position = 16)
    private String id;
}

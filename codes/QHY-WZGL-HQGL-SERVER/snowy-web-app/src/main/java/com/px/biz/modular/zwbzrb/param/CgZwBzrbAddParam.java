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
package com.px.biz.modular.zwbzrb.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * cg_zw_bzrb添加参数
 *
 * @author 品讯科技
 * @date 2024-08
 **/
@Getter
@Setter
public class CgZwBzrbAddParam {

    /** 日报编号（如：20100406） */
    @ApiModelProperty(value = "日报编号（如：20100406）", position = 1)
    private String rbbh;

    /** 生成日期 */
    @ApiModelProperty(value = "生成日期", position = 2)
    private Date rq;

    @ApiModelProperty(value = "编号", position = 2)
    private String bh;

    /** 操作员 */
    @ApiModelProperty(value = "操作员", position = 3)
    private String czy;

    /** 审核日期 */
    @ApiModelProperty(value = "审核日期", position = 4)
    private Date shrq;

    /** 审核人 */
    @ApiModelProperty(value = "审核人", position = 5)
    private String shry;

    /** 一级部门代码（如饮服中心） */
    @ApiModelProperty(value = "一级部门代码（如饮服中心）", position = 6)
    private String yjbmdm;

    /** 一级部门名称 */
    @ApiModelProperty(value = "一级部门名称", position = 7)
    private String yjbmmc;

    /** 部门代码（食堂代码）：如0101 */
    @ApiModelProperty(value = "部门代码（食堂代码）：如0101", position = 8)
    private String bmdm;

    /** 部门名称（东苑食堂、学生食堂、圆中苑、西校区等） */
    @ApiModelProperty(value = "部门名称（东苑食堂、学生食堂、圆中苑、西校区等）", position = 9)
    private String bmmc;

    /** 班组代码（部门代码+位流水号：如010101） */
    @ApiModelProperty(value = "班组代码（部门代码+位流水号：如010101）", position = 10)
    private String bzdm;

    /** 班组名称（煲仔、粥馆、小炒、麻辣等） */
    @ApiModelProperty(value = "班组名称（煲仔、粥馆、小炒、麻辣等）", position = 11)
    private String bzmc;

    /** 荤菜类金额 */
    @ApiModelProperty(value = "荤菜类金额", position = 12)
    private BigDecimal hclje;

    /** 素菜类金额 */
    @ApiModelProperty(value = "素菜类金额", position = 13)
    private BigDecimal sclje;

    /** 调料类金额 */
    @ApiModelProperty(value = "调料类金额", position = 14)
    private BigDecimal tllje;

    /** 主食类金额 */
    @ApiModelProperty(value = "主食类金额", position = 15)
    private BigDecimal zslje;

    /** 饮品类金额 */
    @ApiModelProperty(value = "饮品类金额", position = 16)
    private BigDecimal yplje;

    /** DHCLJE */
    @ApiModelProperty(value = "DHCLJE", position = 17)
    private BigDecimal dhclje;

    /** WXCLJE */
    @ApiModelProperty(value = "WXCLJE", position = 18)
    private BigDecimal wxclje;

    /** 其他支出金额 */
    @ApiModelProperty(value = "其他支出金额", position = 19)
    private BigDecimal qtlje;

    /** 水电气类金额 */
    @ApiModelProperty(value = "水电气类金额", position = 20)
    private BigDecimal sdqlje;

    /** 低耗类金额 */
    @ApiModelProperty(value = "低耗类金额", position = 21)
    private BigDecimal dhlje;

    /** 酬金类金额 */
    @ApiModelProperty(value = "酬金类金额", position = 22)
    private BigDecimal cjlje;

    /** 其他支出金额 */
    @ApiModelProperty(value = "其他支出金额", position = 23)
    private BigDecimal qtzcje;

    /** CPDBJE */
    @ApiModelProperty(value = "CPDBJE", position = 24)
    private BigDecimal cpdbje;

    /** 营业收入金额 */
    @ApiModelProperty(value = "营业收入金额", position = 25)
    private BigDecimal yysrje;

    /** 其他收入金额 */
    @ApiModelProperty(value = "其他收入金额", position = 26)
    private BigDecimal qtsrje;

    /** 盈亏金额（自动计算：收入-支出） */
    @ApiModelProperty(value = "盈亏金额（自动计算：收入-支出）", position = 27)
    private BigDecimal ykje;

    /** 日报状态（登记：已审核） */
    @ApiModelProperty(value = "日报状态（登记：已审核）", position = 28)
    private String workstate;

}

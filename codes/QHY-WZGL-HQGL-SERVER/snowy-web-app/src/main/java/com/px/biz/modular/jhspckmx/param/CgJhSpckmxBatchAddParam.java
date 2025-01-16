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
package com.px.biz.modular.jhspckmx.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * cg_jh_spckmx添加参数
 *
 * @author 品讯科技
 * @date 2024-08
 **/
@Getter
@Setter
public class CgJhSpckmxBatchAddParam {

    /** 商品进货日期+1位识别码（1为班组订货、2为部门备货，3为部门调拨，4为采购退货） */
    @ApiModelProperty(value = "商品进货日期+1位识别码（1为班组订货、2为部门备货，3为部门调拨，4为采购退货）", position = 1)
    private String spjhrq;

    /** 需货日期 */
    @ApiModelProperty(value = "需货日期", position = 2)
    private Date xhrq;

    /** 申请日期 */
    @ApiModelProperty(value = "申请日期", position = 3)
    private Date sqrq;

    /** 申请人 */
    @ApiModelProperty(value = "申请人", position = 4)
    private String sqr;

    /** 出库类型（班组订货、库存领用、班组退库、部门调拨、采购退货） */
    @ApiModelProperty(value = "出库类型（班组订货、库存领用、班组退库、部门调拨、采购退货）", position = 5)
    private String cklx;

    /** -一级部门代码（如饮服中心） */
    @ApiModelProperty(value = "-一级部门代码（如饮服中心）", position = 6)
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

    /** 班组代码（部门代码+位流水号：如010101）(注：部门调拨为调入部门代码，采购退货为供应商代码) */
    @ApiModelProperty(value = "班组代码（部门代码+位流水号：如010101）(注：部门调拨为调入部门代码，采购退货为供应商代码)", position = 10)
    private String bzdm;

    /** 班组名称（煲仔、粥馆、小炒、麻辣等）(注：部门调拨为调入部门名称，采购退货为供应商名称 */
    @ApiModelProperty(value = "班组名称（煲仔、粥馆、小炒、麻辣等）(注：部门调拨为调入部门名称，采购退货为供应商名称", position = 11)
    private String bzmc;

    /** 商品代码 */
    @ApiModelProperty(value = "商品代码", position = 12)
    private String spdm;

    /** 商品名称 */
    @ApiModelProperty(value = "商品名称", position = 13)
    private String spmc;

    /** 商品规格 */
    @ApiModelProperty(value = "商品规格", position = 14)
    private String spgg;

    /** 品牌产地 */
    @ApiModelProperty(value = "品牌产地", position = 15)
    private String ppcd;

    /** 包装率 */
    @ApiModelProperty(value = "包装率", position = 16)
    private String bzl;

    /** 商品分类（荤菜类、素菜类、调料类、主食类），统计用 */
    @ApiModelProperty(value = "商品分类（荤菜类、素菜类、调料类、主食类），统计用", position = 17)
    private String spfl;

    /** 商品大类代码（01） */
    @ApiModelProperty(value = "商品大类代码（01）", position = 18)
    private String dldm;

    /** 商品大类名称（食品类、维修材料类、园林绿化类） */
    @ApiModelProperty(value = "商品大类名称（食品类、维修材料类、园林绿化类）", position = 19)
    private String dlmc;

    /** 商品类别 */
    @ApiModelProperty(value = "商品类别", position = 20)
    private String lbdm;

    /** 类别名称 */
    @ApiModelProperty(value = "类别名称", position = 21)
    private String lbmc;

    /** 供应商代码 */
    @ApiModelProperty(value = "供应商代码", position = 22)
    private String gysdm;

    /** 供应商名称 */
    @ApiModelProperty(value = "供应商名称", position = 23)
    private String gysmc;

    /** 商品计量单位 */
    @ApiModelProperty(value = "商品计量单位", position = 24)
    private String jldw;

    /** 出库日期 */
    @ApiModelProperty(value = "出库日期", position = 25)
    private Date ckrq;

    /** 出库人 */
    @ApiModelProperty(value = "出库人", position = 26)
    private String ckr;

    /** 申请数量 */
    @ApiModelProperty(value = "申请数量", position = 27)
    private BigDecimal sqsl;

    /** 出库数量 */
    @ApiModelProperty(value = "出库数量", position = 28)
    private BigDecimal cksl;

    /** 参考单价 */
    @ApiModelProperty(value = "参考单价", position = 29)
    private BigDecimal ckdj;

    /** NOWJJ */
    @ApiModelProperty(value = "NOWJJ", position = 30)
    private BigDecimal nowjj;

    /** 进价 */
    @ApiModelProperty(value = "进价", position = 39)
    private BigDecimal jhdj;

    /** 供应单价 */
    @ApiModelProperty(value = "供应单价", position = 31)
    private BigDecimal gydj;

    /** 金额 */
    @ApiModelProperty(value = "金额", position = 32)
    private BigDecimal je;

    /** 领用人 */
    @ApiModelProperty(value = "领用人", position = 33)
    private String bzlyr;

    /** 状态（申请、申请提交、发货中、已发货） */
    @ApiModelProperty(value = "状态（申请、申请提交、发货中、已发货）", position = 34)
    private String workstate;

    /** DJBH */
    @ApiModelProperty(value = "DJBH", position = 35)
    private String djbh;

    /** BZ */
    @ApiModelProperty(value = "BZ", position = 36)
    private String bz;

    /** TKBZ */
    @ApiModelProperty(value = "TKBZ", position = 37)
    private String tkbz;

    /** sqdh */
    @ApiModelProperty(value = "申请单号", position = 39)
    private String sqdh;
    /** YTSM */
    @ApiModelProperty(value = "YTSM", position = 38)
    private String ytsm;

    /** sl */
    @ApiModelProperty(value = "sl", position = 38)
    private BigDecimal sl;
    /** YTSM */
    @ApiModelProperty(value = "spmxList")
    private List<CgJhSpckmxAddParam> spmxList;
}

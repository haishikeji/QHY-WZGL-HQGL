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
package com.px.biz.modular.jhspmx.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * cg_jh_spmx编辑参数
 *
 * @author 品讯科技
 * @date 2024-08
 **/
@Getter
@Setter
public class CgJhSpmxEditParam {

    /** 申请单号(4位年+2位月+2位日+4位食堂号+2位流水号) */
    @ApiModelProperty(value = "申请单号(4位年+2位月+2位日+4位食堂号+2位流水号)", position = 1)
    private String sqdh;

    /** 采购单号(4位年+2位月+2位日+4位流水号) */
    @ApiModelProperty(value = "采购单号(4位年+2位月+2位日+4位流水号)", position = 2)
    private String cgdh;

    /** 送货单号(4位年+2位月+2位日+4位食堂号+4位流水号) */
    @ApiModelProperty(value = "送货单号(4位年+2位月+2位日+4位食堂号+4位流水号)", position = 3)
    private String shdh;

    /** 需货日期 */
    @ApiModelProperty(value = "需货日期", position = 4)
    private Date xhrq;

    /** 申请日期 */
    @ApiModelProperty(value = "申请日期", position = 5)
    private Date sqrq;

    /** 申请人 */
    @ApiModelProperty(value = "申请人", position = 6)
    private String sqr;

    /** 商品进货日期+1位识别码（1为班组订货、部门备货，2为部门调拨，3为采购退货，4成品调拨）（如201004121）此字段为商品关联用字段 */
    @ApiModelProperty(value = "商品进货日期+1位识别码（1为班组订货、部门备货，2为部门调拨，3为采购退货，4成品调拨）（如201004121）此字段为商品关联用字段", position = 7)
    private String spjhrq;

    /** 采购类型（班组订货、部门备货、部门调拨、采购退货、成品调拨） */
    @ApiModelProperty(value = "采购类型（班组订货、部门备货、部门调拨、采购退货、成品调拨）", position = 8)
    private String cglx;

    /** 一级部门代码（如饮服中心） */
    @ApiModelProperty(value = "一级部门代码（如饮服中心）", position = 9)
    private String yjbmdm;

    /** 一级部门名称 */
    @ApiModelProperty(value = "一级部门名称", position = 10)
    private String yjbmmc;

    /** 部门代码（食堂代码）：如0101 */
    @ApiModelProperty(value = "部门代码（食堂代码）：如0101", position = 11)
    private String bmdm;

    /** 部门名称（东苑食堂、学生食堂、圆中苑、西校区等） */
    @ApiModelProperty(value = "部门名称（东苑食堂、学生食堂、圆中苑、西校区等）", position = 12)
    private String bmmc;

    /** 商品代码 */
    @ApiModelProperty(value = "商品代码", position = 13)
    private String spdm;

    /** 商品名称 */
    @ApiModelProperty(value = "商品名称", position = 14)
    private String spmc;

    /** 商品规格 */
    @ApiModelProperty(value = "商品规格", position = 15)
    private String spgg;

    /** 品牌产地 */
    @ApiModelProperty(value = "品牌产地", position = 16)
    private String ppcd;

    /** 包装率 */
    @ApiModelProperty(value = "包装率", position = 17)
    private String bzl;

    /** 商品分类（荤菜类、素菜类、调料类、主食类） */
    @ApiModelProperty(value = "商品分类（荤菜类、素菜类、调料类、主食类）", position = 18)
    private String spfl;

    /** 保质期天数 */
    @ApiModelProperty(value = "保质期天数", position = 19)
    private BigDecimal bzts;

    /** 保质日期 */
    @ApiModelProperty(value = "保质日期", position = 20)
    private Date bzrq;

    /** 商品大类代码（01） */
    @ApiModelProperty(value = "商品大类代码（01）", position = 21)
    private String dldm;

    /** 商品大类名称（食品类、维修材料类、园林绿化类），统计用 */
    @ApiModelProperty(value = "商品大类名称（食品类、维修材料类、园林绿化类），统计用", position = 22)
    private String dlmc;

    /** 商品类别 */
    @ApiModelProperty(value = "商品类别", position = 23)
    private String lbdm;

    /** 类别名称 */
    @ApiModelProperty(value = "类别名称", position = 24)
    private String lbmc;

    /** 供应商代码 */
    @ApiModelProperty(value = "供应商代码", position = 25)
    private String gysdm;

    /** 供应商名称 */
    @ApiModelProperty(value = "供应商名称", position = 26)
    private String gysmc;

    /** 商品计量单位 */
    @ApiModelProperty(value = "商品计量单位", position = 27)
    private String jldw;

    /** 采购日期 */
    @ApiModelProperty(value = "采购日期", position = 28)
    private Date cgrq;

    /** 验货日期 */
    @ApiModelProperty(value = "验货日期", position = 29)
    private Date yhrq;

    /** 验货人 */
    @ApiModelProperty(value = "验货人", position = 30)
    private String yhr;

    /** 审核人 */
    @ApiModelProperty(value = "审核人", position = 31)
    private String shr;

    /** 审核日期（即入库日期） */
    @ApiModelProperty(value = "审核日期（即入库日期）", position = 32)
    private Date shrq;

    /** 申请数量 */
    @ApiModelProperty(value = "申请数量", position = 33)
    private BigDecimal sqsl;

    /** 收货数量 */
    @ApiModelProperty(value = "收货数量", position = 34)
    private BigDecimal shsl;

    /** 出库数量(商品出库时更新此项) */
    @ApiModelProperty(value = "出库数量(商品出库时更新此项)", position = 35)
    private BigDecimal cksl;

    /** 库存数量(商品出库时更新此项) */
    @ApiModelProperty(value = "库存数量(商品出库时更新此项)", position = 36)
    private BigDecimal kcsl;

    /** 参考单价 */
    @ApiModelProperty(value = "参考单价", position = 37)
    private BigDecimal ckdj;

    /** 进货单价 */
    @ApiModelProperty(value = "进货单价", position = 38)
    private BigDecimal jhdj;

    /** 供应单价 */
    @ApiModelProperty(value = "供应单价", position = 39)
    private BigDecimal gydj;

    /** 进货金额 */
    @ApiModelProperty(value = "进货金额", position = 40)
    private BigDecimal jhje;

    /** 供应金额 */
    @ApiModelProperty(value = "供应金额", position = 41)
    private BigDecimal gyje;

    /** 业务状态（申请、审批退回、采购退回、待审批、待采购、采购中、待收货、收货中、已收货） */
    @ApiModelProperty(value = "业务状态（申请、审批退回、采购退回、待审批、待采购、采购中、待收货、收货中、已收货）", position = 42)
    private String workstate;

    /** 审批状态（未审批、审批退回、待一审、待二审、已审批、不需审批） */
    @ApiModelProperty(value = "审批状态（未审批、审批退回、待一审、待二审、已审批、不需审批）", position = 43)
    private String spstate;

    /** 原始批次（如：部门调拨用） */
    @ApiModelProperty(value = "原始批次（如：部门调拨用）", position = 44)
    private String yspc;

    /** 商品来源（如：直接采购、东苑食堂） */
    @ApiModelProperty(value = "商品来源（如：直接采购、东苑食堂）", position = 45)
    private String sply;

    /** 商品来源班组（如：米饭组），成品调拨用 */
    @ApiModelProperty(value = "商品来源班组（如：米饭组），成品调拨用", position = 46)
    private String splybz;

    /** 分发班组 */
    @ApiModelProperty(value = "分发班组", position = 47)
    private String ffbz;

    /** 备注 */
    @ApiModelProperty(value = "备注", position = 48)
    private String bz;

    /** YTSM */
    @ApiModelProperty(value = "YTSM", position = 49)
    private String ytsm;

    /** ID */
    @ApiModelProperty(value = "ID", required = true, position = 50)
    @NotNull(message = "id不能为空")
    private String id;

}

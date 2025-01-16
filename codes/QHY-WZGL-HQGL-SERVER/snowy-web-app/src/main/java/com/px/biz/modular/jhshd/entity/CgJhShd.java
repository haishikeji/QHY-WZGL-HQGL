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
package com.px.biz.modular.jhshd.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import com.fhs.core.trans.vo.TransPojo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import com.px.biz.modular.org.entity.BizOrg;
import com.px.sys.modular.org.entity.SysOrg;

import java.math.BigDecimal;
import java.util.Date;

/**
 * cg_jh_shd实体
 *
 * @author 品讯科技
 * @date 2024-08
 **/
@Getter
@Setter
@TableName("cg_jh_shd")
public class CgJhShd implements TransPojo {

    /** 单据编号(4位年+2位月+2位日+4位部门号+4位流水号) */
    @ApiModelProperty(value = "单据编号(4位年+2位月+2位日+4位部门号+4位流水号)", position = 1)
    private String shdh;

    /** 验货日期 */
    @ApiModelProperty(value = "验货日期", position = 2)
    private Date yhrq;

    /** 验货人 */
    @ApiModelProperty(value = "验货人", position = 3)
    private String yhr;

    /** 审核人 */
    @ApiModelProperty(value = "审核人", position = 4)
    private String shry;

    /** 审核日期 */
    @ApiModelProperty(value = "审核日期", position = 5)
    private Date shrq;

    /** 一级部门代码（如饮服中心） */
    @ApiModelProperty(value = "一级部门代码（如饮服中心）", position = 6)
    private String yjbmdm;

    /** 一级部门名称 */
    @ApiModelProperty(value = "一级部门名称", position = 7)
    private String yjbmmc;

    /** 部门代码（食堂代码）：如0101 */
    @ApiModelProperty(value = "部门代码（食堂代码）：如0101", position = 8)
    @Trans(type = TransType.SIMPLE, target = BizOrg.class, fields = "name", alias = "bm", ref = "bmName")
    private String bmdm;

    /** 部门名称（东苑食堂、学生食堂、圆中苑、西校区等） */
    @ApiModelProperty(value = "部门名称（东苑食堂、学生食堂、圆中苑、西校区等）", position = 9)
    private String bmmc;

    /** 供应商代码 */
    @ApiModelProperty(value = "供应商代码", position = 10)
    @Trans(type = TransType.SIMPLE, target = SysOrg.class, fields = "name",alias = "gys",ref = "gysName")
    private String gysdm;

    /** 供应商名称 */
    @ApiModelProperty(value = "供应商名称", position = 11)
    private String gysmc;

    /** 商品金额（进货金额） */
    @ApiModelProperty(value = "商品金额（进货金额）", position = 12)
    private BigDecimal spje;

    /** 折扣系数（默认为1） */
    @ApiModelProperty(value = "折扣系数（默认为1）", position = 13)
    private BigDecimal zkxs;

    /** 实际结算金额（默认等于商品金额，在出现商品折扣等情况下可修改次项） */
    @ApiModelProperty(value = "实际结算金额（默认等于商品金额，在出现商品折扣等情况下可修改次项）", position = 14)
    private BigDecimal jsje;

    /** 供应出库金额（采购中心结算利润用） */
    @ApiModelProperty(value = "供应出库金额（采购中心结算利润用）", position = 15)
    private BigDecimal gyje;

    /** 结算单号（4位年+2位月+4位流水如：2010040001） */
    @ApiModelProperty(value = "结算单号（4位年+2位月+4位流水如：2010040001）", position = 16)
    private String jsdh;

    /** 状态（已收货、提交结算） */
    @ApiModelProperty(value = "状态（已收货、提交结算）", position = 17)
    private String workstate;

    /** BZ */
    @ApiModelProperty(value = "BZ", position = 18)
    private String bz;

    /** CGLX */
    @ApiModelProperty(value = "CGLX", position = 19)
    private String cglx;

    /** PRINTYES */
    @ApiModelProperty(value = "PRINTYES", position = 20)
    private String printyes;

    /** ID */
    @TableId
    @ApiModelProperty(value = "ID", position = 21)
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

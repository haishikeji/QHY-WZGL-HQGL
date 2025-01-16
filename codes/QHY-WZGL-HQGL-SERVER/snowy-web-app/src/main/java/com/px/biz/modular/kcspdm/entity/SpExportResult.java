package com.px.biz.modular.kcspdm.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import com.alibaba.excel.enums.poi.FillPatternTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SpExportResult {

    /** 商品代码 */
    @ApiModelProperty(value = "商品代码", position = 1)
    @HeadStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 40)
    @ExcelProperty({"商品信息", "商品代码"})
    private String spdm;

    /** 商品名称 */
    @ApiModelProperty(value = "商品名称", position = 2)
    @HeadStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 40)
    @ExcelProperty({"商品信息", "商品名称"})
    private String spmc;

    /** 商品规格 */
    @ApiModelProperty(value = "商品名称", position = 3)
    @HeadStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 40)
    @ExcelProperty({"商品信息", "商品名称"})
    private String spgg;

    /** 计量单位 */
    @ApiModelProperty(value = "计量单位", position = 13)
    @HeadStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 40)
    @ExcelProperty({"商品信息", "计量单位"})
    private String jldw;

    /** 商品显示顺序 */
    @ApiModelProperty(value = "商品显示顺序", position = 7)
    @HeadStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 40)
    @ExcelProperty({"商品信息", "商品显示顺序"})
    private String spxh;

    /** 拼音简码 */
    @ApiModelProperty(value = "拼音简码", position = 8)
    @HeadStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 40)
    @ExcelProperty({"商品信息", "拼音简码"})
    private String pyjm;

    /** 供应单价 */
    @ApiModelProperty(value = "供应单价", position = 24)
    @HeadStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 40)
    @ExcelProperty({"商品信息", "供应单价"})
    private BigDecimal gydj;

    /** 品牌产地 */
    @ApiModelProperty(value = "品牌产地", position = 4)
    @HeadStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 40)
    @ExcelProperty({"商品信息", "品牌产地"})
    private String ppcd;

    /** 包装率 */
    @ApiModelProperty(value = "包装率", position = 5)
    @HeadStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 40)
    @ExcelProperty({"商品信息", "包装率"})
    private String bzl;

    /** 启用标志（是、否） */
    @ApiModelProperty(value = "启用标志", position = 28)
    @HeadStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 40)
    @ExcelProperty({"商品信息", "启用标志"})
    private String qybz;

    /** 商品分类（荤菜类、素菜类、调料类、主食类），统计用 */
    @ApiModelProperty(value = "商品分类", position = 6)
    @HeadStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 40)
    @ExcelProperty({"商品信息", "商品分类"})
    private String spfl;

}

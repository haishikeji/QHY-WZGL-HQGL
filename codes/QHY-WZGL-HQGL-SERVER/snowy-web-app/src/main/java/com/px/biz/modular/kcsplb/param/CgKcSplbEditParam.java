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
package com.px.biz.modular.kcsplb.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * cg_kc_splb编辑参数
 *
 * @author 品讯科技
 * @date 2024-08
 **/
@Getter
@Setter
public class CgKcSplbEditParam {

    /** 商品大类代码（01） */
    @ApiModelProperty(value = "商品大类代码（01）", position = 1)
    private String dldm;

    /** 商品大类名称（食品类、维修材料类、园林绿化类） */
    @ApiModelProperty(value = "商品大类名称（食品类、维修材料类、园林绿化类）", position = 2)
    private String dlmc;

    /** 类别代码（0101） */
    @ApiModelProperty(value = "类别代码（0101）", position = 3)
    private String lbdm;

    /** 类别名称（鲜猪肉、冻制品、水产等） */
    @ApiModelProperty(value = "类别名称（鲜猪肉、冻制品、水产等）", position = 4)
    private String lbmc;

    /** 类别显示顺序 */
    @ApiModelProperty(value = "类别显示顺序", position = 5)
    private String lbxh;

    /** 拼音简码 */
    @ApiModelProperty(value = "拼音简码", position = 6)
    private String pyjm;

    /** 启用标志（是、否） */
    @ApiModelProperty(value = "启用标志（是、否）", position = 7)
    private String qybz;

    /** BZ */
    @ApiModelProperty(value = "BZ", position = 8)
    private String bz;

    /** ID */
    @ApiModelProperty(value = "ID", required = true, position = 9)
    @NotNull(message = "id不能为空")
    private String id;

}

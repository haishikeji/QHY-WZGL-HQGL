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
package com.px.biz.modular.codebm.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * cg_code_bm编辑参数
 *
 * @author 品讯科技
 * @date 2024-08
 **/
@Getter
@Setter
public class CgCodeBmEditParam {

    /** 一级部门代码 */
    @ApiModelProperty(value = "一级部门代码", position = 1)
    private String yjbmdm;

    /** 一级部门名称 */
    @ApiModelProperty(value = "一级部门名称", position = 2)
    private String yjbmmc;

    /** 部门代码（食堂代码） */
    @ApiModelProperty(value = "部门代码（食堂代码）", position = 3)
    private String bmdm;

    /** 部门名称（东苑食堂、学生食堂、圆中苑、西校区等） */
    @ApiModelProperty(value = "部门名称（东苑食堂、学生食堂、圆中苑、西校区等）", position = 4)
    private String bmmc;

    /** 部门显示顺序 */
    @ApiModelProperty(value = "部门显示顺序", position = 5)
    private String bmxh;

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
    @NotBlank(message = "id不能为空")
    private String id;

}

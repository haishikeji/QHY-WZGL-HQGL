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
package com.px.biz.modular.codebzgl.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * cg_code_bzgl实体
 *
 * @author 品讯科技
 * @date 2024-08
 **/
@Getter
@Setter
@TableName("cg_code_bzgl")
public class CgCodeBzgl {

    /** 一级部门代码 */
    @ApiModelProperty(value = "一级部门代码", position = 1)
    private String yjbmdm;

    /** 一级部门名称 */
    @ApiModelProperty(value = "一级部门名称", position = 2)
    private String yjbmmc;

    /** 部门代码 */
    @ApiModelProperty(value = "部门代码", position = 3)
    private String bmdm;

    /** 部门名称 */
    @ApiModelProperty(value = "部门名称", position = 4)
    private String bmmc;

    /** 班组代码 */
    @ApiModelProperty(value = "班组代码", position = 5)
    private String bzdm;

    /** 班组名称 */
    @ApiModelProperty(value = "班组名称", position = 6)
    private String bzmc;

    /** 班组序号 */
    @ApiModelProperty(value = "班组序号", position = 7)
    private String bzxh;

    /** 拼音简码 */
    @ApiModelProperty(value = "拼音简码", position = 8)
    private String pyjm;

    /** 启用标志 */
    @ApiModelProperty(value = "启用标志", position = 9)
    private String qybz;

    /** BZ */
    @ApiModelProperty(value = "BZ", position = 10)
    private String bz;

    /** 父id */
    @ApiModelProperty(value = "父id", position = 11)
    private String parentId;

    /** ID */
    @TableId
    @ApiModelProperty(value = "ID", position = 12)
    private Long id;
}

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
package com.px.biz.modular.jhdhd.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import com.px.common.pojo.CommonValidList;

import java.util.Date;

/**
 * cg_jh_dhd添加参数
 *
 * @author 品讯科技
 * @date 2024-08
 **/
@Getter
@Setter
public class CgJhDhdAddParam {


    @ApiModelProperty(value = "idsList", position = 1)
    private CommonValidList<CgJhDhdIdParam> idsList;

    /** CGLX */
    @ApiModelProperty(value = "bmdm", position = 2)
    private String bmdm;

    /** CGLX */
    @ApiModelProperty(value = "spdm", position = 2)
    private String spdm;

    /** CGLX */
    @ApiModelProperty(value = "gysdm", position = 2)
    private String gysdm;
    /** CGLX */
    @ApiModelProperty(value = "gysmc", position = 2)
    private String gysmc;

    /** CGLX */
    @ApiModelProperty(value = "cglx", position = 2)
    private String cglx;

    /** CGLX */
    @ApiModelProperty(value = "cgrq", position = 2)
    private Date cgrq;

    /** 当前页 */
    @ApiModelProperty(value = "当前页码")
    private Integer current;

    /** 每页条数 */
    @ApiModelProperty(value = "每页条数")
    private Integer size;

    /** 排序字段 */
    @ApiModelProperty(value = "排序字段，字段驼峰名称，如：userName")
    private String sortField;

    /** 排序方式 */
    @ApiModelProperty(value = "排序方式，升序：ASCEND；降序：DESCEND")
    private String sortOrder;
}

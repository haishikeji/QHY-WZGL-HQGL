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

/**
 * cg_zw_bzrb查询参数
 *
 * @author 品讯科技
 * @date 2024-08
 **/
@Getter
@Setter
public class CgZwBzrbPageParam {

    /** 月报状态（登记：已审核） */
    @ApiModelProperty(value = "月报状态（登记：已审核）", position = 28)
    private String workstate;

    @ApiModelProperty(value = "月报状态（登记：已审核）", position = 28)
    private String bh;

    @ApiModelProperty(value = "开始编号", position = 28)
    private String startBh;

    @ApiModelProperty(value = "结束编号", position = 28)
    private String endBh;
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

    /** 关键词 */
    @ApiModelProperty(value = "关键词")
    private String searchKey;

    /** 日报编号（如：20100406） */
    @ApiModelProperty(value = "日报编号（如：20100406）")
    private String rbbh;

    /** 部门名称（东苑食堂、学生食堂、圆中苑、西校区等） */
    @ApiModelProperty(value = "部门名称（东苑食堂、学生食堂、圆中苑、西校区等）")
    private String bmdm;

    /** 班组名称（煲仔、粥馆、小炒、麻辣等） */
    @ApiModelProperty(value = "班组名称（煲仔、粥馆、小炒、麻辣等）")
    private String bzdm;

    @ApiModelProperty(value = "字典值")
    private String dictValue;

    /** 班组名称（煲仔、粥馆、小炒、麻辣等） */
    @ApiModelProperty(value = "班组名称（煲仔、粥馆、小炒、麻辣等）")
    private String rq;

}

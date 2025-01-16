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
package com.px.biz.modular.sptrkd.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.px.common.annotation.CommonLog;
import com.px.common.pojo.CommonResult;
import com.px.common.pojo.CommonValidList;
import com.px.biz.modular.sptrkd.entity.BizSptRkd;
import com.px.biz.modular.sptrkd.param.BizSptRkdAddParam;
import com.px.biz.modular.sptrkd.param.BizSptRkdEditParam;
import com.px.biz.modular.sptrkd.param.BizSptRkdIdParam;
import com.px.biz.modular.sptrkd.param.BizSptRkdPageParam;
import com.px.biz.modular.sptrkd.service.BizSptRkdService;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * biz_spt_rkd控制器
 *
 * @author 品讯科技
 * @date 2024-08
 */
@Api(tags = "biz_spt_rkd控制器")
@ApiSupport(author = "SNOWY_TEAM", order = 1)
@RestController
@Validated
public class BizSptRkdController {

    @Resource
    private BizSptRkdService bizSptRkdService;

    /**
     * 获取biz_spt_rkd分页
     *
     * @author 品讯科技
     * @date 2024-08
     */
    @ApiOperationSupport(order = 1)
    @ApiOperation("获取biz_spt_rkd分页")
    @SaCheckPermission("/biz/sptrkd/page")
    @GetMapping("/biz/sptrkd/page")
    public CommonResult<Page<BizSptRkd>> page(BizSptRkdPageParam bizSptRkdPageParam) {
        return CommonResult.data(bizSptRkdService.page(bizSptRkdPageParam));
    }

    /**
     * 添加biz_spt_rkd
     *
     * @author 品讯科技
     * @date 2024-08
     */
    @ApiOperationSupport(order = 2)
    @ApiOperation("添加biz_spt_rkd")
    @CommonLog("添加biz_spt_rkd")
    @SaCheckPermission("/biz/sptrkd/add")
    @PostMapping("/biz/sptrkd/add")
    public CommonResult<String> add(@RequestBody @Valid BizSptRkdAddParam bizSptRkdAddParam) {
        bizSptRkdService.add(bizSptRkdAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑biz_spt_rkd
     *
     * @author 品讯科技
     * @date 2024-08
     */
    @ApiOperationSupport(order = 3)
    @ApiOperation("编辑biz_spt_rkd")
    @CommonLog("编辑biz_spt_rkd")
    @SaCheckPermission("/biz/sptrkd/edit")
    @PostMapping("/biz/sptrkd/edit")
    public CommonResult<String> edit(@RequestBody @Valid BizSptRkdEditParam bizSptRkdEditParam) {
        bizSptRkdService.edit(bizSptRkdEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除biz_spt_rkd
     *
     * @author 品讯科技
     * @date 2024-08
     */
    @ApiOperationSupport(order = 4)
    @ApiOperation("删除biz_spt_rkd")
    @CommonLog("删除biz_spt_rkd")
    @SaCheckPermission("/biz/sptrkd/delete")
    @PostMapping("/biz/sptrkd/delete")
    public CommonResult<String> delete(@RequestBody @Valid @NotEmpty(message = "集合不能为空")
                                                   CommonValidList<BizSptRkdIdParam> bizSptRkdIdParamList) {
        bizSptRkdService.delete(bizSptRkdIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取biz_spt_rkd详情
     *
     * @author 品讯科技
     * @date 2024-08
     */
    @ApiOperationSupport(order = 5)
    @ApiOperation("获取biz_spt_rkd详情")
    @SaCheckPermission("/biz/sptrkd/detail")
    @GetMapping("/biz/sptrkd/detail")
    public CommonResult<BizSptRkd> detail(@Valid BizSptRkdIdParam bizSptRkdIdParam) {
        return CommonResult.data(bizSptRkdService.detail(bizSptRkdIdParam));
    }
}

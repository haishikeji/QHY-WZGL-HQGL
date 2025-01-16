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
package com.px.biz.modular.zwbmybmx.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import com.px.biz.modular.zwbmybmx.entity.CgZwBmybmx;
import com.px.biz.modular.zwbmybmx.param.CgZwBmybmxAddParam;
import com.px.biz.modular.zwbmybmx.param.CgZwBmybmxEditParam;
import com.px.biz.modular.zwbmybmx.param.CgZwBmybmxIdParam;
import com.px.biz.modular.zwbmybmx.param.CgZwBmybmxPageParam;
import com.px.biz.modular.zwbmybmx.service.CgZwBmybmxService;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * cg_zw_bmybmx控制器
 *
 * @author 品讯科技
 * @date 2024-08
 */
@Api(tags = "cg_zw_bmybmx控制器")
@ApiSupport(author = "SNOWY_TEAM", order = 1)
@RestController
@Validated
public class CgZwBmybmxController {

    @Resource
    private CgZwBmybmxService cgZwBmybmxService;

    @ApiOperationSupport(order = 4)
    @ApiOperation("查询明细金额")
    @CommonLog("查询明细金额")
    @GetMapping("/biz/zwbmybmx/queryInJe")
    public CommonResult<CgZwBmybmx> queryInJe(CgZwBmybmxPageParam cgZwBmybmxPageParam) {
        QueryWrapper<CgZwBmybmx> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("lbdm", cgZwBmybmxPageParam.getDictValue());
        queryWrapper.eq("bmdm", cgZwBmybmxPageParam.getBmdm());
        queryWrapper.eq("ybbh", cgZwBmybmxPageParam.getYbbh());
        return CommonResult.data(cgZwBmybmxService.getOne(queryWrapper));
    }

    /**
     * 获取cg_zw_bmybmx分页
     *
     * @author 品讯科技
     * @date 2024-08
     */
    @ApiOperationSupport(order = 1)
    @ApiOperation("获取cg_zw_bmybmx分页")
    @SaCheckPermission("/biz/zwbmybmx/page")
    @GetMapping("/biz/zwbmybmx/page")
    public CommonResult<Page<CgZwBmybmx>> page(CgZwBmybmxPageParam cgZwBmybmxPageParam) {
        return CommonResult.data(cgZwBmybmxService.page(cgZwBmybmxPageParam));
    }

    /**
     * 添加cg_zw_bmybmx
     *
     * @author 品讯科技
     * @date 2024-08
     */
    @ApiOperationSupport(order = 2)
    @ApiOperation("新增")
    @CommonLog("新增")
    @SaCheckPermission("/biz/zwbmybmx/add")
    @PostMapping("/biz/zwbmybmx/add")
    public CommonResult<String> add(@RequestBody @Valid CgZwBmybmxAddParam cgZwBmybmxAddParam) {
        cgZwBmybmxService.add(cgZwBmybmxAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑cg_zw_bmybmx
     *
     * @author 品讯科技
     * @date 2024-08
     */
    @ApiOperationSupport(order = 3)
    @ApiOperation("登记")
    @CommonLog("登记")
    @SaCheckPermission("/biz/zwbmybmx/edit")
    @PostMapping("/biz/zwbmybmx/edit")
    public CommonResult<String> edit(@RequestBody @Valid CgZwBmybmxEditParam cgZwBmybmxEditParam) {
        cgZwBmybmxService.edit(cgZwBmybmxEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除cg_zw_bmybmx
     *
     * @author 品讯科技
     * @date 2024-08
     */
    @ApiOperationSupport(order = 4)
    @ApiOperation("删除cg_zw_bmybmx")
    @CommonLog("删除cg_zw_bmybmx")
    @SaCheckPermission("/biz/zwbmybmx/delete")
    @PostMapping("/biz/zwbmybmx/delete")
    public CommonResult<String> delete(@RequestBody @Valid @NotEmpty(message = "集合不能为空")
                                                   CommonValidList<CgZwBmybmxIdParam> cgZwBmybmxIdParamList) {
        cgZwBmybmxService.delete(cgZwBmybmxIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取cg_zw_bmybmx详情
     *
     * @author 品讯科技
     * @date 2024-08
     */
    @ApiOperationSupport(order = 5)
    @ApiOperation("获取cg_zw_bmybmx详情")
    @SaCheckPermission("/biz/zwbmybmx/detail")
    @GetMapping("/biz/zwbmybmx/detail")
    public CommonResult<CgZwBmybmx> detail(@Valid CgZwBmybmxIdParam cgZwBmybmxIdParam) {
        return CommonResult.data(cgZwBmybmxService.detail(cgZwBmybmxIdParam));
    }
}

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
package com.px.biz.modular.bztree.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.px.common.annotation.CommonLog;
import com.px.common.pojo.CommonResult;
import com.px.common.pojo.CommonValidList;
import com.px.biz.modular.bztree.entity.BizBzTree;
import com.px.biz.modular.bztree.param.BizBzTreeAddParam;
import com.px.biz.modular.bztree.param.BizBzTreeEditParam;
import com.px.biz.modular.bztree.param.BizBzTreeIdParam;
import com.px.biz.modular.bztree.service.BizBzTreeService;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 组织控制器
 *
 * @author 品讯科技
 * @date 2024-08
 */
@Api(tags = "组织控制器")
@ApiSupport(author = "SNOWY_TEAM", order = 1)
@RestController
@Validated
public class BizBzTreeController {

    @Resource
    private BizBzTreeService bizBzTreeService;

    @ApiOperationSupport(order = 2)
    @ApiOperation("获取班组树")
    //@SaCheckPermission("/biz/splbtree/tree")
    @GetMapping("/biz/bztree/findBz")
    public CommonResult<List<BizBzTree>> findBz(@RequestParam String id) {
        return CommonResult.data(bizBzTreeService.findBz(id));

    }

    /**
     * 获取班组类别树
     *
     * @author 品讯科技
     * @date 2024-08
     */
    @ApiOperationSupport(order = 2)
    @ApiOperation("获取班组树")
    //@SaCheckPermission("/biz/splbtree/tree")
    @GetMapping("/biz/bztree/tree")
    public CommonResult<List<Tree<String>>> tree() {
        return CommonResult.data(bizBzTreeService.tree());
    }

    /**
     * 获取组织分页
     *
     * @author 品讯科技
     * @date 2024-08
     */
    @ApiOperationSupport(order = 1)
    @ApiOperation("获取班组分页")
    @SaCheckPermission("/biz/bztree/page")
    @GetMapping("/biz/bztree/page")
    public CommonResult<Page<BizBzTree>> page(String bmdm) {
        return CommonResult.data(bizBzTreeService.page(bmdm));
    }
//    public CommonResult<Page<BizBzTree>> page(BizBzTreePageParam bizBzTreePageParam) {
//        return CommonResult.data(bizBzTreeService.page(bizBzTreePageParam));
//    }

    /**
     * 添加组织
     *
     * @author 品讯科技
     * @date 2024-08
     */
    @ApiOperationSupport(order = 2)
    @ApiOperation("添加班组")
    @CommonLog("添加班组")
    @SaCheckPermission("/biz/bztree/add")
    @PostMapping("/biz/bztree/add")
    public CommonResult<String> add(@RequestBody @Valid BizBzTreeAddParam bizBzTreeAddParam) {
        bizBzTreeService.add(bizBzTreeAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑组织
     *
     * @author 品讯科技
     * @date 2024-08
     */
    @ApiOperationSupport(order = 3)
    @ApiOperation("编辑班组")
    @CommonLog("编辑班组")
    @SaCheckPermission("/biz/bztree/edit")
    @PostMapping("/biz/bztree/edit")
    public CommonResult<String> edit(@RequestBody @Valid BizBzTreeEditParam bizBzTreeEditParam) {
        bizBzTreeService.edit(bizBzTreeEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除组织
     *
     * @author 品讯科技
     * @date 2024-08
     */
    @ApiOperationSupport(order = 4)
    @ApiOperation("删除班组")
    @CommonLog("删除班组")
    @SaCheckPermission("/biz/bztree/delete")
    @PostMapping("/biz/bztree/delete")
    public CommonResult<String> delete(@RequestBody @Valid @NotEmpty(message = "集合不能为空")
                                               CommonValidList<BizBzTreeIdParam> bizBzTreeIdParamList) {
        bizBzTreeService.delete(bizBzTreeIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取组织详情
     *
     * @author 品讯科技
     * @date 2024-08
     */
    @ApiOperationSupport(order = 5)
    @ApiOperation("获取班组详情")
    @SaCheckPermission("/biz/bztree/detail")
    @GetMapping("/biz/bztree/detail")
    public CommonResult<BizBzTree> detail(@Valid BizBzTreeIdParam bizBzTreeIdParam) {
        return CommonResult.data(bizBzTreeService.detail(bizBzTreeIdParam));
    }

}

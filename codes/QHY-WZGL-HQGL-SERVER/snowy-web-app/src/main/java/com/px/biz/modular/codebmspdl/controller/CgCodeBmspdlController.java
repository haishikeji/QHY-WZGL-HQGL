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
package com.px.biz.modular.codebmspdl.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.px.biz.modular.codebmspdl.param.BizOrgGysParam;
import com.px.common.annotation.CommonLog;
import com.px.common.pojo.CommonResult;
import com.px.common.pojo.CommonValidList;
import com.px.biz.modular.codebmspdl.entity.CgCodeBmspdl;
import com.px.biz.modular.codebmspdl.param.CgCodeBmspdlAddParam;
import com.px.biz.modular.codebmspdl.param.CgCodeBmspdlEditParam;
import com.px.biz.modular.codebmspdl.param.CgCodeBmspdlIdParam;
import com.px.biz.modular.codebmspdl.param.CgCodeBmspdlPageParam;
import com.px.biz.modular.codebmspdl.service.CgCodeBmspdlService;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

/**
 * cg_code_bmspdl控制器
 *
 * @author 品讯科技
 * @date 2024-08
 */
@Api(tags = "cg_code_bmspdl控制器")
@ApiSupport(author = "SNOWY_TEAM", order = 1)
@RestController
@Validated
public class CgCodeBmspdlController {

    @Resource
    private CgCodeBmspdlService cgCodeBmspdlService;

    @ApiOperationSupport(order = 2)
    @ApiOperation("查询已选择商品关联列表")
    @GetMapping("/biz/codebmspdl/selectedBmspdl")
    public CommonResult<List<CgCodeBmspdl>> selectedBmspdl(@RequestParam String bmdm) {
        List<CgCodeBmspdl> selectedValues = cgCodeBmspdlService.selectedValues(bmdm);
        return CommonResult.data(selectedValues);
    }

    @ApiOperationSupport(order = 2)
    @ApiOperation("保存商品关联列表")
    //@SaCheckPermission("/biz/splbtree/tree")
    @PostMapping("/biz/codebmspdl/saveBmspdl")
    public CommonResult<String> saveBmspdl(@RequestBody Map map) {
        String id = (String) map.get("id");
        List<String> selectValues = (List<String>) map.get("selectValues");
        if(cgCodeBmspdlService.saveBmspdl(id, selectValues).equals("Success")){
            return CommonResult.ok("操作成功");
        }else{
            return CommonResult.error(cgCodeBmspdlService.saveBmspdl(id, selectValues));
        }

    }

    /**
     * 获取cg_code_bmspdl分页
     *
     * @author 品讯科技
     * @date 2024-08
     */
    @ApiOperationSupport(order = 1)
    @ApiOperation("获取cg_code_bmspdl分页")
    @SaCheckPermission("/biz/codebmspdl/page")
    @GetMapping("/biz/codebmspdl/page")
    public CommonResult<Page<CgCodeBmspdl>> page(CgCodeBmspdlPageParam cgCodeBmspdlPageParam) {
        return CommonResult.data(cgCodeBmspdlService.page(cgCodeBmspdlPageParam));
    }

    /**
     * 添加cg_code_bmspdl
     *
     * @author 品讯科技
     * @date 2024-08
     */
    @ApiOperationSupport(order = 2)
    @ApiOperation("添加cg_code_bmspdl")
    @CommonLog("添加cg_code_bmspdl")
    @SaCheckPermission("/biz/codebmspdl/add")
    @PostMapping("/biz/codebmspdl/add")
    public CommonResult<String> add(@RequestBody @Valid CgCodeBmspdlAddParam cgCodeBmspdlAddParam) {
        cgCodeBmspdlService.add(cgCodeBmspdlAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑cg_code_bmspdl
     *
     * @author 品讯科技
     * @date 2024-08
     */
    @ApiOperationSupport(order = 3)
    @ApiOperation("编辑cg_code_bmspdl")
    @CommonLog("编辑cg_code_bmspdl")
    @SaCheckPermission("/biz/codebmspdl/edit")
    @PostMapping("/biz/codebmspdl/edit")
    public CommonResult<String> edit(@RequestBody @Valid CgCodeBmspdlEditParam cgCodeBmspdlEditParam) {
        cgCodeBmspdlService.edit(cgCodeBmspdlEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除cg_code_bmspdl
     *
     * @author 品讯科技
     * @date 2024-08
     */
    @ApiOperationSupport(order = 4)
    @ApiOperation("删除cg_code_bmspdl")
    @CommonLog("删除cg_code_bmspdl")
    @SaCheckPermission("/biz/codebmspdl/delete")
    @PostMapping("/biz/codebmspdl/delete")
    public CommonResult<String> delete(@RequestBody @Valid @NotEmpty(message = "集合不能为空")
                                               CommonValidList<CgCodeBmspdlIdParam> cgCodeBmspdlIdParamList) {
        cgCodeBmspdlService.delete(cgCodeBmspdlIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取cg_code_bmspdl详情
     *
     * @author 品讯科技
     * @date 2024-08
     */
    @ApiOperationSupport(order = 5)
    @ApiOperation("获取cg_code_bmspdl详情")
    @SaCheckPermission("/biz/codebmspdl/detail")
    @GetMapping("/biz/codebmspdl/detail")
    public CommonResult<CgCodeBmspdl> detail(@Valid CgCodeBmspdlIdParam cgCodeBmspdlIdParam) {
        return CommonResult.data(cgCodeBmspdlService.detail(cgCodeBmspdlIdParam));
    }

    /**
     * 获取人员选择器
     *
     * @author 品讯科技
     * @date 2024-08
     */
    @ApiOperationSupport(order = 8)
    @ApiOperation("获取人员选择器")
    @PostMapping("/biz/codebmspdl/setGys")
    public CommonResult<String> setGys(@RequestBody BizOrgGysParam bizOrgGysParam) {
        cgCodeBmspdlService.setGys(bizOrgGysParam);
        return CommonResult.ok();
    }
}

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
package com.px.biz.modular.kcspdm.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.px.auth.core.pojo.SaBaseLoginUser;
import com.px.auth.core.util.StpLoginUserUtil;
import com.px.biz.modular.kckczb.entity.CgKcKczb;
import com.px.biz.modular.kckczb.service.CgKcKczbService;
import com.px.biz.modular.kcspdm.entity.SpExportParam;
import com.px.biz.modular.kcspdm.param.*;
import com.px.biz.modular.kcspdmtj.entity.CgKcSpdmTj;
import com.px.biz.modular.kcspdmtj.service.CgKcSpdmTjService;
import com.px.common.annotation.CommonLog;
import com.px.common.exception.CommonException;
import com.px.common.pojo.CommonResult;
import com.px.common.pojo.CommonValidList;
import com.px.biz.modular.kcspdm.entity.CgKcSpdm;
import com.px.biz.modular.kcspdm.service.CgKcSpdmService;
import com.px.common.timer.CommonTimerTaskRunner;
import com.px.common.util.CommonDHUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * cg_kc_spdm控制器
 *
 * @author 品讯科技
 * @date 2024-08
 */
@Api(tags = "cg_kc_spdm控制器")
@ApiSupport(author = "SNOWY_TEAM", order = 1)
@RestController
@Validated
public class CgKcSpdmController {

    @Resource
    private CgKcSpdmService cgKcSpdmService;

    @Resource
    private CgKcSpdmTjService cgKcSpdmTjService;

    @Resource
    private CgKcKczbService cgKcKczbService;

    /**
     * 商品导出
     *
     * @author 品讯科技
     * @date 2024-08
     */
    @ApiOperationSupport(order = 11)
    @ApiOperation("商品导出")
    @CommonLog("商品导出")
    @SaCheckPermission("/biz/kcspdm/export")
    @GetMapping(value = "/biz/kcspdm/export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void exportSp(SpExportParam spExportParam, HttpServletResponse response) throws IOException {
        cgKcSpdmService.exportSp(spExportParam, response);
    }
    /**
     * 获取cg_kc_spdm分页
     *
     * @author 品讯科技
     * @date 2024-08
     */
    @ApiOperationSupport(order = 1)
    @ApiOperation("获取cg_kc_spdm分页")
    @SaCheckPermission("/biz/kcspdm/page")
    @GetMapping("/biz/kcspdm/page")
    public CommonResult<Page<CgKcSpdm>> page(CgKcSpdmPageParam cgKcSpdmPageParam) {
        return CommonResult.data(cgKcSpdmService.page(cgKcSpdmPageParam));
    }

    /**
     * 添加cg_kc_spdm
     *
     * @author 品讯科技
     * @date 2024-08
     */
    @ApiOperationSupport(order = 2)
    @ApiOperation("添加cg_kc_spdm")
    @CommonLog("添加cg_kc_spdm")
    @SaCheckPermission("/biz/kcspdm/add")
    @PostMapping("/biz/kcspdm/add")
    public CommonResult<String> add(@RequestBody @Valid CgKcSpdmAddParam cgKcSpdmAddParam) {
        cgKcSpdmService.add(cgKcSpdmAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑cg_kc_spdm
     *
     * @author 品讯科技
     * @date 2024-08
     */
    @ApiOperationSupport(order = 3)
    @ApiOperation("编辑cg_kc_spdm")
    @CommonLog("编辑cg_kc_spdm")
    @SaCheckPermission("/biz/kcspdm/edit")
    @PostMapping("/biz/kcspdm/edit")
    public CommonResult<String> edit(@RequestBody @Valid CgKcSpdmEditParam cgKcSpdmEditParam) {
        cgKcSpdmService.edit(cgKcSpdmEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除cg_kc_spdm
     *
     * @author 品讯科技
     * @date 2024-08
     */
    @ApiOperationSupport(order = 4)
    @ApiOperation("删除cg_kc_spdm")
    @CommonLog("删除cg_kc_spdm")
    @SaCheckPermission("/biz/kcspdm/delete")
    @PostMapping("/biz/kcspdm/delete")
    public CommonResult<String> delete(@RequestBody @Valid @NotEmpty(message = "集合不能为空")
                                                   CommonValidList<CgKcSpdmIdParam> cgKcSpdmIdParamList) {
        cgKcSpdmService.delete(cgKcSpdmIdParamList);
        return CommonResult.ok();
    }
    /**
     * 删除cg_kc_spdm
     *
     * @author 品讯科技
     * @date 2024-08
     */
    @ApiOperationSupport(order = 4)
    @ApiOperation("保存调价cg_kc_spdm")
    @CommonLog("保存调价cg_kc_spdm")
    @PostMapping("/biz/kcspdm/sptj")
    public CommonResult<String> sptj(@RequestBody @Valid @NotEmpty(message = "集合不能为空")
                                               CommonValidList<CgKcSpdm> cgKcSpdmList) {
        Date tjrq= DateUtil.date();
        SaBaseLoginUser loginUser = StpLoginUserUtil.getLoginUser();
        cgKcSpdmList.forEach(cgKcSpdm -> {
                UpdateWrapper updateWrapper = new UpdateWrapper();
                updateWrapper.set("zxtj",cgKcSpdm.getNowjj());
                updateWrapper.set("nowgydj",cgKcSpdm.getGydj());
                updateWrapper.set("tjrq",tjrq);
                updateWrapper.set("tjr",loginUser.getName());
                updateWrapper.set("tjshzt","否");
                updateWrapper.eq("id",cgKcSpdm.getId());
                cgKcSpdmService.update(updateWrapper);
            }

        );

        return CommonResult.ok();
    }
    /**
     * 删除cg_kc_spdm
     *
     * @author 品讯科技
     * @date 2024-08
     */
    @ApiOperationSupport(order = 4)
    @ApiOperation("审核调价cg_kc_spdm")
    @CommonLog("审核调价cg_kc_spdm")
    @PostMapping("/biz/kcspdm/auditSptj")
    public CommonResult<String> auditSptj(@RequestBody @Valid
                                                  CgKcSpdmTjParam cgKcSpdmList) {
        SaBaseLoginUser loginUser = StpLoginUserUtil.getLoginUser();
        if(cgKcSpdmList.getTjshrq()!=null) {
            cgKcSpdmList.getCgKcSpdmList().forEach(cgKcSpdm -> {
                        UpdateWrapper updateWrapper = new UpdateWrapper();

                        updateWrapper.set("tjshrq",cgKcSpdmList.getTjshrq());
                        updateWrapper.set("tjshr",loginUser.getName());
                        updateWrapper.set("tjshzt","待");
                        updateWrapper.eq("id",cgKcSpdm.getId());
                        cgKcSpdmService.update(updateWrapper);

                        CgKcSpdmTj cgKcSpdmTj = new CgKcSpdmTj();
                        BeanUtil.copyProperties(cgKcSpdm,cgKcSpdmTj);
                        cgKcSpdmTj.setOldjj(cgKcSpdm.getZxtj());
                        cgKcSpdmTj.setGydj(cgKcSpdm.getNowgydj());
                        cgKcSpdmTj.setTjshr(loginUser.getName());
                        cgKcSpdmTj.setTjshrq(cgKcSpdmList.getTjshrq());
                        cgKcSpdmTj.setId(null);
                        cgKcSpdmTjService.save(cgKcSpdmTj);


                    }

            );
            LocalDateTime time = LocalDateTime.parse(DateUtil.format(cgKcSpdmList.getTjshrq(),("yyyy-MM-dd HH:mm:ss")), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            String cronStr = time.format(DateTimeFormatter.ofPattern("ss mm HH dd MM ? yyyy"));

            CronUtil.schedule(CommonDHUtil.randomOrderCode("TJ"), cronStr, () -> {
                try {
                    // 运行定时任务
                    ((CommonTimerTaskRunner) SpringUtil.getBean(Class.forName("com.px.biz.modular.kcspdm.task.SptjTaskRunner"))).action();
                } catch (ClassNotFoundException e) {
                    throw new CommonException("定时任务找不到对应的类");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }else{

            Date tjshrq= DateUtil.date();
            cgKcSpdmList.setTjshrq(tjshrq);
            cgKcSpdmList.getCgKcSpdmList().forEach(cgKcSpdm -> {
                UpdateWrapper updateWrapper = new UpdateWrapper();
                updateWrapper.set("zxtj",cgKcSpdm.getZxtj());
                updateWrapper.set("nowjj",cgKcSpdm.getZxtj());
                updateWrapper.set("gydj",cgKcSpdm.getNowgydj());
                updateWrapper.set("nowgydj",cgKcSpdm.getNowgydj());
                updateWrapper.set("tjshrq",cgKcSpdmList.getTjshrq());
                updateWrapper.set("tjshr",loginUser.getName());
                updateWrapper.set("tjshzt","是");
                updateWrapper.eq("id",cgKcSpdm.getId());
                cgKcSpdmService.update(updateWrapper);

                UpdateWrapper<CgKcKczb> cgKcKczbUpdateWrapper = new UpdateWrapper();
                cgKcKczbUpdateWrapper.set("nowjj",cgKcSpdm.getZxtj());
                cgKcKczbUpdateWrapper.set("gydj",cgKcSpdm.getNowgydj());
                cgKcKczbUpdateWrapper.eq("spdm",cgKcSpdm.getSpdm());
                cgKcKczbService.update(cgKcKczbUpdateWrapper);

                CgKcSpdmTj cgKcSpdmTj = new CgKcSpdmTj();
                BeanUtil.copyProperties(cgKcSpdm,cgKcSpdmTj);
                cgKcSpdmTj.setOldjj(cgKcSpdm.getNowjj());
                cgKcSpdmTj.setGydj(cgKcSpdm.getNowgydj());
                cgKcSpdmTj.setTjshr(loginUser.getName());
                cgKcSpdmTj.setTjshrq(cgKcSpdmList.getTjshrq());
                cgKcSpdmTj.setId(null);
                cgKcSpdmTjService.save(cgKcSpdmTj);


                    }

            );
        }




        return CommonResult.ok();
    }
    /**
     * 获取cg_kc_spdm详情
     *
     * @author 品讯科技
     * @date 2024-08
     */
    @ApiOperationSupport(order = 5)
    @ApiOperation("获取cg_kc_spdm详情")
    @SaCheckPermission("/biz/kcspdm/detail")
    @GetMapping("/biz/kcspdm/detail")
    public CommonResult<CgKcSpdm> detail(@Valid CgKcSpdmIdParam cgKcSpdmIdParam) {
        return CommonResult.data(cgKcSpdmService.detail(cgKcSpdmIdParam));
    }

    /**
     * 获取cg_kc_spdm详情
     *
     * @author 品讯科技
     * @date 2024-08
     */
    @ApiOperationSupport(order = 5)
    @ApiOperation("获取cg_kc_spdm最大值")
    @GetMapping("/biz/kcspdm/findNewCode")
    public CommonResult<CgKcSpdm> findNewCode( CgKcSpdm cgKcSpdm) {
        return CommonResult.data(cgKcSpdmService.findNewCode(cgKcSpdm));
    }
    @ApiOperationSupport(order = 5)
    @ApiOperation("获取cg_kc_spdm是否重复值")
    @GetMapping("/biz/kcspdm/checkHasSpdm")
    public CommonResult<Boolean> checkHasSpdm( String spdm) {
        return CommonResult.data(cgKcSpdmService.checkHasSpdm(spdm));
    }
    @ApiOperationSupport(order = 5)
    @ApiOperation("获取cg_kc_spdm是否重复值")
    @GetMapping("/biz/kcspdm/checkHasSpmc")
    public CommonResult<Boolean> checkHasSpmc( String spmc) {
        return CommonResult.data(cgKcSpdmService.checkHasSpmc(spmc));
    }
}

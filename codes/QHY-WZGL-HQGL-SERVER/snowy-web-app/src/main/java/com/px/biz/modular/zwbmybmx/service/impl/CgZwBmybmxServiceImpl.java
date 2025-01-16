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
package com.px.biz.modular.zwbmybmx.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.px.auth.core.util.StpLoginUserUtil;
import com.px.biz.modular.zwbmyb.entity.CgZwBmyb;
import com.px.biz.modular.zwbmyb.service.CgZwBmybService;
import com.px.common.enums.CommonSortOrderEnum;
import com.px.common.exception.CommonException;
import com.px.common.page.CommonPageRequest;
import com.px.biz.modular.zwbmybmx.entity.CgZwBmybmx;
import com.px.biz.modular.zwbmybmx.mapper.CgZwBmybmxMapper;
import com.px.biz.modular.zwbmybmx.param.CgZwBmybmxAddParam;
import com.px.biz.modular.zwbmybmx.param.CgZwBmybmxEditParam;
import com.px.biz.modular.zwbmybmx.param.CgZwBmybmxIdParam;
import com.px.biz.modular.zwbmybmx.param.CgZwBmybmxPageParam;
import com.px.biz.modular.zwbmybmx.service.CgZwBmybmxService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * cg_zw_bmybmxService接口实现类
 *
 * @author 品讯科技
 * @date 2024-08
 **/
@Service
public class CgZwBmybmxServiceImpl extends ServiceImpl<CgZwBmybmxMapper, CgZwBmybmx> implements CgZwBmybmxService {

    @Resource
    private CgZwBmybService bmybService;

    @Override
    public Page<CgZwBmybmx> page(CgZwBmybmxPageParam cgZwBmybmxPageParam) {
        QueryWrapper<CgZwBmybmx> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isAllNotEmpty(cgZwBmybmxPageParam.getSortField(), cgZwBmybmxPageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(cgZwBmybmxPageParam.getSortOrder());
            queryWrapper.orderBy(true, cgZwBmybmxPageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()),
                    StrUtil.toUnderlineCase(cgZwBmybmxPageParam.getSortField()));
        } else {
            queryWrapper.lambda().orderByAsc(CgZwBmybmx::getId);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(CgZwBmybmxAddParam cgZwBmybmxAddParam) {
        //删除明细表原有数据
        QueryWrapper<CgZwBmybmx> ybmxQW = new QueryWrapper<>();
        ybmxQW.eq("bmdm", cgZwBmybmxAddParam.getBmdm());
        ybmxQW.eq("ybbh", cgZwBmybmxAddParam.getYbbh());
        ybmxQW.eq("lbdm", cgZwBmybmxAddParam.getLbdm());
        this.remove(ybmxQW);
        //更新明细表
        CgZwBmybmx cgZwBmybmx = BeanUtil.toBean(cgZwBmybmxAddParam, CgZwBmybmx.class);
        cgZwBmybmx.setRq(DateUtil.date());
        cgZwBmybmx.setCzy(StpLoginUserUtil.getLoginUser().getName());
        //cgZwBmybmx.setYbbh(cgZwBmybmxAddParam.getYbbh());
        this.save(cgZwBmybmx);
        //更新月报
        //String dldm = cgZwBmybmxAddParam.getDldm();
        //final BigDecimal[] injeSum = {BigDecimal.valueOf(0)};
        UpdateWrapper<CgZwBmyb> updateWrapper = new UpdateWrapper<>();
        //updateWrapper.set(dldm, injeSum[0]);
        updateWrapper.eq("bmdm", cgZwBmybmxAddParam.getBmdm());
        updateWrapper.eq("ybbh", cgZwBmybmxAddParam.getYbbh());
        bmybService.update(updateWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(CgZwBmybmxEditParam cgZwBmybmxEditParam) {
        //删除明细表原有数据
        if(ObjectUtil.isNotEmpty(cgZwBmybmxEditParam)) {
            List<CgZwBmybmxEditParam> bmybmxList = cgZwBmybmxEditParam.getBmybmxList();
            bmybmxList.forEach(bmybmx -> {
                QueryWrapper<CgZwBmybmx> ybmxQW = new QueryWrapper<>();
                ybmxQW.eq("bmdm", cgZwBmybmxEditParam.getBmdm());
                ybmxQW.eq("ybbh", cgZwBmybmxEditParam.getYbbh());
//        ybmxQW.eq("lbdm", cgZwBmybmxEditParam.getLbdm());
                this.remove(ybmxQW);
                //更新明细表
//                CgZwBmybmx cgZwBmybmx = BeanUtil.toBean(bmybmx, CgZwBmybmx.class);
//                cgZwBmybmx.setRq(DateUtil.date());
//                cgZwBmybmx.setYbbh(bmybmx.getYbbh());
//                cgZwBmybmx.setCzy(StpLoginUserUtil.getLoginUser().getName());
//                this.save(cgZwBmybmx);
            });
        }
        //更新月报
        String dldm = cgZwBmybmxEditParam.getLbdm();
        final BigDecimal[] injeSum = {BigDecimal.valueOf(0)};
        UpdateWrapper<CgZwBmyb> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set(dldm, injeSum[0]);
        updateWrapper.eq("bmdm", cgZwBmybmxEditParam.getBmdm());
        updateWrapper.eq("ybbh", cgZwBmybmxEditParam.getYbbh());
        bmybService.update(updateWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<CgZwBmybmxIdParam> cgZwBmybmxIdParamList) {
        // 执行删除
        this.removeByIds(CollStreamUtil.toList(cgZwBmybmxIdParamList, CgZwBmybmxIdParam::getId));
    }

    @Override
    public CgZwBmybmx detail(CgZwBmybmxIdParam cgZwBmybmxIdParam) {
        return this.queryEntity(cgZwBmybmxIdParam.getId());
    }

    @Override
    public CgZwBmybmx queryEntity(String id) {
        CgZwBmybmx cgZwBmybmx = this.getById(id);
        if (ObjectUtil.isEmpty(cgZwBmybmx)) {
            throw new CommonException("cg_zw_bmybmx不存在，id值为：{}", id);
        }
        return cgZwBmybmx;
    }
}

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
package com.px.biz.modular.gysprc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.px.common.enums.CommonSortOrderEnum;
import com.px.common.exception.CommonException;
import com.px.common.page.CommonPageRequest;
import com.px.biz.modular.gysprc.entity.CgGysPrc;
import com.px.biz.modular.gysprc.mapper.CgGysPrcMapper;
import com.px.biz.modular.gysprc.param.CgGysPrcAddParam;
import com.px.biz.modular.gysprc.param.CgGysPrcEditParam;
import com.px.biz.modular.gysprc.param.CgGysPrcIdParam;
import com.px.biz.modular.gysprc.param.CgGysPrcPageParam;
import com.px.biz.modular.gysprc.service.CgGysPrcService;

import java.util.List;

/**
 * cg_gys_prcService接口实现类
 *
 * @author 品讯科技
 * @date 2024-08
 **/
@Service
public class CgGysPrcServiceImpl extends ServiceImpl<CgGysPrcMapper, CgGysPrc> implements CgGysPrcService {

    @Override
    public Page<CgGysPrc> page(CgGysPrcPageParam cgGysPrcPageParam) {
        QueryWrapper<CgGysPrc> queryWrapper = new QueryWrapper<>();
        if(ObjectUtil.isAllNotEmpty(cgGysPrcPageParam.getGysdm())) {
            queryWrapper.lambda().eq(CgGysPrc::getPrcId, cgGysPrcPageParam.getGysdm());
        }
        if(ObjectUtil.isAllNotEmpty(cgGysPrcPageParam.getSortField(), cgGysPrcPageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(cgGysPrcPageParam.getSortOrder());
            queryWrapper.orderBy(true, cgGysPrcPageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()),
                    StrUtil.toUnderlineCase(cgGysPrcPageParam.getSortField()));
        } else {
            queryWrapper.lambda().orderByAsc(CgGysPrc::getId);
        }
        queryWrapper.select("id,prc_id,file_name,file_expired,file_path,bz,(case when now()>file_expired then 'true' else 'false' end)isfileexpired,status");
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(CgGysPrcAddParam cgGysPrcAddParam) {
        CgGysPrc cgGysPrc = BeanUtil.toBean(cgGysPrcAddParam, CgGysPrc.class);
        cgGysPrc.setStatus("未审核");
        this.save(cgGysPrc);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(CgGysPrcEditParam cgGysPrcEditParam) {
        CgGysPrc cgGysPrc = this.queryEntity(String.valueOf(cgGysPrcEditParam.getId()));
        BeanUtil.copyProperties(cgGysPrcEditParam, cgGysPrc);
        cgGysPrc.setStatus("未审核");
        this.updateById(cgGysPrc);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<CgGysPrcIdParam> cgGysPrcIdParamList) {
        // 执行删除
        this.removeByIds(CollStreamUtil.toList(cgGysPrcIdParamList, CgGysPrcIdParam::getId));
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void audit(List<CgGysPrcIdParam> cgGysPrcIdParamList) {
        // 执行删除
        cgGysPrcIdParamList.forEach(param->{
            UpdateWrapper<CgGysPrc> cgGysPrcUpdateWrapper = new UpdateWrapper<>();
            cgGysPrcUpdateWrapper.eq("id",param.getId());
            cgGysPrcUpdateWrapper.set("status","已审核");
            this.update(cgGysPrcUpdateWrapper);
        });

        //this.removeByIds(CollStreamUtil.toList(cgGysPrcIdParamList, CgGysPrcIdParam::getId));
    }
    @Override
    public CgGysPrc detail(CgGysPrcIdParam cgGysPrcIdParam) {
        return this.queryEntity(String.valueOf(cgGysPrcIdParam.getId()));
    }

    @Override
    public CgGysPrc queryEntity(String id) {
        CgGysPrc cgGysPrc = this.getById(id);
        if(ObjectUtil.isEmpty(cgGysPrc)) {
            throw new CommonException("cg_gys_prc不存在，id值为：{}", id);
        }
        return cgGysPrc;
    }
}

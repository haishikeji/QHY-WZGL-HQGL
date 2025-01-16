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
package com.px.biz.modular.jhsqd.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.fhs.trans.service.impl.TransService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.px.auth.core.util.StpLoginUserUtil;
import com.px.biz.modular.jhspckmx.service.CgJhSpckmxService;
import com.px.biz.modular.jhspmx.entity.CgJhSpmx;
import com.px.biz.modular.jhspmx.service.CgJhSpmxService;
import com.px.common.enums.CommonSortOrderEnum;
import com.px.common.exception.CommonException;
import com.px.common.page.CommonPageRequest;
import com.px.biz.modular.jhsqd.entity.CgJhSqd;
import com.px.biz.modular.jhsqd.mapper.CgJhSqdMapper;
import com.px.biz.modular.jhsqd.param.CgJhSqdAddParam;
import com.px.biz.modular.jhsqd.param.CgJhSqdEditParam;
import com.px.biz.modular.jhsqd.param.CgJhSqdIdParam;
import com.px.biz.modular.jhsqd.param.CgJhSqdPageParam;
import com.px.biz.modular.jhsqd.service.CgJhSqdService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * cg_jh_sqdService接口实现类
 *
 * @author 品讯科技
 * @date 2024-08
 **/
@Service
public class CgJhSqdServiceImpl extends ServiceImpl<CgJhSqdMapper, CgJhSqd> implements CgJhSqdService {

    @Resource
    private CgJhSpmxService spmxService;

    @Resource
    private CgJhSpckmxService spckmxService;

    @Resource
    private TransService transService;

    @Override
    public Page<CgJhSqd> page(CgJhSqdPageParam cgJhSqdPageParam) {
        List<String> l=StpLoginUserUtil.getLoginUserDataScope();
        QueryWrapper<CgJhSqd> queryWrapper = new QueryWrapper<>();
        if(l.size()>0){

        }else{
            queryWrapper.lambda().eq(CgJhSqd::getSqr, StpLoginUserUtil.getLoginUser().getName());
        }
        if(ObjectUtil.isNotEmpty(cgJhSqdPageParam.getSqdh())) {
            queryWrapper.lambda().like(CgJhSqd::getSqdh, cgJhSqdPageParam.getSqdh());
        }
        if(ObjectUtil.isNotEmpty(cgJhSqdPageParam.getStartSqrq()) && ObjectUtil.isNotEmpty(cgJhSqdPageParam.getEndSqrq())) {
            queryWrapper.between("date_format(sqrq, '%Y-%m-%d')", cgJhSqdPageParam.getStartSqrq(), cgJhSqdPageParam.getEndSqrq());
        }
        if(ObjectUtil.isNotEmpty(cgJhSqdPageParam.getStartXhrq()) && ObjectUtil.isNotEmpty(cgJhSqdPageParam.getEndXhrq())) {
            queryWrapper.between("date_format(xhrq, '%Y-%m-%d')", cgJhSqdPageParam.getStartXhrq(), cgJhSqdPageParam.getEndXhrq());
        }
        if(ObjectUtil.isNotEmpty(cgJhSqdPageParam.getCglx())) {
            queryWrapper.lambda().eq(CgJhSqd::getCglx, cgJhSqdPageParam.getCglx());
        }
        queryWrapper.lambda().in(CgJhSqd::getCglx, "班组订货","部门备货");
        if(ObjectUtil.isNotEmpty(cgJhSqdPageParam.getBmdm())) {
            queryWrapper.lambda().eq(CgJhSqd::getBmdm, cgJhSqdPageParam.getBmdm());
        }
        if(ObjectUtil.isNotEmpty(cgJhSqdPageParam.getWorkstate())) {
            if("部分收货".equals(cgJhSqdPageParam.getWorkstate())){
                queryWrapper.lambda().eq(CgJhSqd::getWorkstate, cgJhSqdPageParam.getWorkstate());
            }else if("已收货".equals(cgJhSqdPageParam.getWorkstate())){
                queryWrapper.lambda().eq(CgJhSqd::getWorkstate, cgJhSqdPageParam.getWorkstate());
            }else{
                queryWrapper.lambda().eq(CgJhSqd::getWorkstate, cgJhSqdPageParam.getWorkstate());
            }


        }
        if(ObjectUtil.isAllNotEmpty(cgJhSqdPageParam.getSortField(), cgJhSqdPageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(cgJhSqdPageParam.getSortOrder());
            queryWrapper.orderBy(true, cgJhSqdPageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()),
                    StrUtil.toUnderlineCase(cgJhSqdPageParam.getSortField()));
        } else {
            queryWrapper.lambda().orderByDesc(CgJhSqd::getId);
        }

        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }
    @Override
    public Page<CgJhSqd> cpdbpage(CgJhSqdPageParam cgJhSqdPageParam) {
        QueryWrapper<CgJhSqd> queryWrapper = new QueryWrapper<>();
        if(ObjectUtil.isNotEmpty(cgJhSqdPageParam.getSqdh())) {
            queryWrapper.lambda().like(CgJhSqd::getSqdh, cgJhSqdPageParam.getSqdh());
        }
        if(ObjectUtil.isNotEmpty(cgJhSqdPageParam.getStartSqrq()) && ObjectUtil.isNotEmpty(cgJhSqdPageParam.getEndSqrq())) {
            queryWrapper.between("date_format(sqrq, '%Y-%m-%d')", cgJhSqdPageParam.getStartSqrq(), cgJhSqdPageParam.getEndSqrq());
        }
        if(ObjectUtil.isNotEmpty(cgJhSqdPageParam.getStartXhrq()) && ObjectUtil.isNotEmpty(cgJhSqdPageParam.getEndXhrq())) {
            queryWrapper.between("date_format(xhrq, '%Y-%m-%d')", cgJhSqdPageParam.getStartXhrq(), cgJhSqdPageParam.getEndXhrq());
        }
        if(ObjectUtil.isNotEmpty(cgJhSqdPageParam.getCglx())) {
            queryWrapper.lambda().eq(CgJhSqd::getCglx, cgJhSqdPageParam.getCglx());
        }
        if(ObjectUtil.isNotEmpty(cgJhSqdPageParam.getBmdm())) {
            queryWrapper.lambda().eq(CgJhSqd::getBmdm, cgJhSqdPageParam.getBmdm());
        }

        queryWrapper.lambda().eq(CgJhSqd::getBz,"班组");
        //cqueryWrapper.lambda().in(CgJhSqd::getBmdm,StpLoginUserUtil.getLoginUserDataScope());
        if(ObjectUtil.isNotEmpty(cgJhSqdPageParam.getWorkstate())) {
            queryWrapper.lambda().eq(CgJhSqd::getWorkstate, cgJhSqdPageParam.getWorkstate());
        }
        if(ObjectUtil.isAllNotEmpty(cgJhSqdPageParam.getSortField(), cgJhSqdPageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(cgJhSqdPageParam.getSortOrder());
            queryWrapper.orderBy(true, cgJhSqdPageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()),
                    StrUtil.toUnderlineCase(cgJhSqdPageParam.getSortField()));
        } else {
            queryWrapper.lambda().orderByDesc(CgJhSqd::getId);
        }
        queryWrapper.select("id,sqdh,sqrq,xhrq,cglx,sqr,bmdm,bmmc,shr1,shrq1,shbz1,shr2,shrq2,shbz2,hjje,dyf,workstate,spstate,shjb,cgthly,bz,cgthr,cgthrq,bzdm,(SELECT group_concat(spmc,'(',sqsl,')') from cg_jh_spmx where sqdh=cg_jh_sqd.sqdh)xhbz");
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }
    @Override
    public Page<CgJhSqd> cpdbbmpage(CgJhSqdPageParam cgJhSqdPageParam) {
        QueryWrapper<CgJhSqd> queryWrapper = new QueryWrapper<>();
        if(ObjectUtil.isNotEmpty(cgJhSqdPageParam.getSqdh())) {
            queryWrapper.lambda().like(CgJhSqd::getSqdh, cgJhSqdPageParam.getSqdh());
        }
        if(ObjectUtil.isNotEmpty(cgJhSqdPageParam.getStartSqrq()) && ObjectUtil.isNotEmpty(cgJhSqdPageParam.getEndSqrq())) {
            queryWrapper.between("date_format(sqrq, '%Y-%m-%d')", cgJhSqdPageParam.getStartSqrq(), cgJhSqdPageParam.getEndSqrq());
        }
        if(ObjectUtil.isNotEmpty(cgJhSqdPageParam.getStartXhrq()) && ObjectUtil.isNotEmpty(cgJhSqdPageParam.getEndXhrq())) {
            queryWrapper.between("date_format(xhrq, '%Y-%m-%d')", cgJhSqdPageParam.getStartXhrq(), cgJhSqdPageParam.getEndXhrq());
        }
        if(ObjectUtil.isNotEmpty(cgJhSqdPageParam.getCglx())) {
            queryWrapper.lambda().eq(CgJhSqd::getCglx, cgJhSqdPageParam.getCglx());
        }
        if(ObjectUtil.isNotEmpty(cgJhSqdPageParam.getBmdm())) {
            queryWrapper.lambda().eq(CgJhSqd::getBmdm, cgJhSqdPageParam.getBmdm());
        }
        //cqueryWrapper.lambda().in(CgJhSqd::getBmdm,StpLoginUserUtil.getLoginUserDataScope());
        if(ObjectUtil.isNotEmpty(cgJhSqdPageParam.getWorkstate())) {
            queryWrapper.lambda().eq(CgJhSqd::getWorkstate, cgJhSqdPageParam.getWorkstate());
        }
        queryWrapper.lambda().eq(CgJhSqd::getBz,"部门");
        if(ObjectUtil.isAllNotEmpty(cgJhSqdPageParam.getSortField(), cgJhSqdPageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(cgJhSqdPageParam.getSortOrder());
            queryWrapper.orderBy(true, cgJhSqdPageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()),
                    StrUtil.toUnderlineCase(cgJhSqdPageParam.getSortField()));
        } else {
            queryWrapper.lambda().orderByDesc(CgJhSqd::getId);
        }
        queryWrapper.select("id,sqdh,sqrq,xhrq,cglx,sqr,bmdm,bmmc,shr1,shrq1,shbz1,shr2,shrq2,shbz2,hjje,dyf,workstate,spstate,shjb,cgthly,bz,cgthr,cgthrq,bzdm,(SELECT group_concat(spmc,'(',sqsl,')') from cg_jh_spmx where sqdh=cg_jh_sqd.sqdh)xhbz");

        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }
    @Override
    public Page<CgJhSqd> spdbpage(CgJhSqdPageParam cgJhSqdPageParam) {
        QueryWrapper<CgJhSqd> queryWrapper = new QueryWrapper<>();
        if(ObjectUtil.isNotEmpty(cgJhSqdPageParam.getSqdh())) {
            queryWrapper.lambda().like(CgJhSqd::getSqdh, cgJhSqdPageParam.getSqdh());
        }
        if(ObjectUtil.isNotEmpty(cgJhSqdPageParam.getStartSqrq()) && ObjectUtil.isNotEmpty(cgJhSqdPageParam.getEndSqrq())) {
            queryWrapper.between("date_format(sqrq, '%Y-%m-%d')", cgJhSqdPageParam.getStartSqrq(), cgJhSqdPageParam.getEndSqrq());
        }
        if(ObjectUtil.isNotEmpty(cgJhSqdPageParam.getStartXhrq()) && ObjectUtil.isNotEmpty(cgJhSqdPageParam.getEndXhrq())) {
            queryWrapper.between("date_format(xhrq, '%Y-%m-%d')", cgJhSqdPageParam.getStartXhrq(), cgJhSqdPageParam.getEndXhrq());
        }
        if(ObjectUtil.isNotEmpty(cgJhSqdPageParam.getCglx())) {
            queryWrapper.lambda().eq(CgJhSqd::getCglx, cgJhSqdPageParam.getCglx());
        }
        if(ObjectUtil.isNotEmpty(cgJhSqdPageParam.getBmdm())) {
            queryWrapper.lambda().eq(CgJhSqd::getBmdm, cgJhSqdPageParam.getBmdm());
        }
        //queryWrapper.lambda().in(CgJhSqd::getBmdm,StpLoginUserUtil.getLoginUserDataScope());
        if(ObjectUtil.isNotEmpty(cgJhSqdPageParam.getWorkstate())) {
            queryWrapper.lambda().eq(CgJhSqd::getWorkstate, cgJhSqdPageParam.getWorkstate());
        }
        if(ObjectUtil.isAllNotEmpty(cgJhSqdPageParam.getSortField(), cgJhSqdPageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(cgJhSqdPageParam.getSortOrder());
            queryWrapper.orderBy(true, cgJhSqdPageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()),
                    StrUtil.toUnderlineCase(cgJhSqdPageParam.getSortField()));
        } else {
            queryWrapper.lambda().orderByDesc(CgJhSqd::getId);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(CgJhSqdAddParam cgJhSqdAddParam) {
        CgJhSqd cgJhSqd = BeanUtil.toBean(cgJhSqdAddParam, CgJhSqd.class);
        this.save(cgJhSqd);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addBatch(List<CgJhSqdAddParam> CgJhSqdAddParamList) {
        CgJhSqdAddParamList.forEach(cgJhSqdAddParam -> {
            CgJhSqd cgJhSqd = BeanUtil.toBean(cgJhSqdAddParam, CgJhSqd.class);
            this.save(cgJhSqd);
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(CgJhSqdEditParam cgJhSqdEditParam) {
        Date xhrq = cgJhSqdEditParam.getXhrq();
        Date sqrq = new Date();
        String xhbz = cgJhSqdEditParam.getXhbz();
        System.out.println(System.currentTimeMillis());
        if (cgJhSqdEditParam.getCgJhSqdEditParamList() != null) {
            List<CgJhSqdEditParam> cgJhSqdEditParamList = cgJhSqdEditParam.getCgJhSqdEditParamList();
            cgJhSqdEditParamList.forEach(cgJhSqdEdit -> {
                CgJhSqd cgJhSqd = this.queryEntity(cgJhSqdEdit.getId());
                cgJhSqd.setXhrq(xhrq);
                cgJhSqd.setSqrq(sqrq);
                cgJhSqd.setXhbz(xhbz);
                cgJhSqd.setWorkstate("已提交");
                System.out.println(System.currentTimeMillis());
                this.updateById(cgJhSqd);
                System.out.println(System.currentTimeMillis());
                UpdateWrapper<CgJhSpmx> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("sqdh", cgJhSqd.getSqdh());
                updateWrapper.set("xhrq", xhrq);
                updateWrapper.set("sqrq",sqrq);
                updateWrapper.set("workstate", "已提交");
                System.out.println(System.currentTimeMillis());
                spmxService.update(updateWrapper);
                System.out.println(System.currentTimeMillis());
                CgJhSpmx cgJhSpmx = new CgJhSpmx();
                cgJhSpmx.setXhrq(xhrq);
                cgJhSpmx.setSqrq(sqrq);
                cgJhSpmx.setWorkstate("已提交");
                cgJhSpmx.setSqdh(cgJhSqd.getSqdh());
                System.out.println(System.currentTimeMillis());
                this.baseMapper.updateCgJhSpckmx(cgJhSpmx);
                System.out.println(System.currentTimeMillis());
            });
        } else {
            System.out.println(System.currentTimeMillis());
            CgJhSqd cgJhSqd = this.queryEntity(cgJhSqdEditParam.getId());
            System.out.println(System.currentTimeMillis());
            cgJhSqd.setWorkstate("已提交");
            cgJhSqd.setXhrq(xhrq);
            cgJhSqd.setSqrq(sqrq);
            cgJhSqd.setXhbz(xhbz);
            System.out.println(System.currentTimeMillis());
            this.updateById(cgJhSqd);
            System.out.println(System.currentTimeMillis());
            UpdateWrapper<CgJhSpmx> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("sqdh", cgJhSqd.getSqdh());
            updateWrapper.set("xhrq", xhrq);
            updateWrapper.set("sqrq",sqrq);
            updateWrapper.set("workstate", "已提交");
            System.out.println(System.currentTimeMillis());
            spmxService.update(updateWrapper);
            System.out.println(System.currentTimeMillis());
//            UpdateWrapper<CgJhSpckmx> spckUW = new UpdateWrapper<>();
//            spckUW.inSql("spjhrq", "select spjhrq from cg_jh_spmx where sqdh = '" + cgJhSqd.getSqdh() + "' ");
//            spckUW.set("xhrq", xhrq);
//            spckUW.set("workstate", "已提交");
            CgJhSpmx cgJhSpmx = new CgJhSpmx();
            cgJhSpmx.setXhrq(xhrq);
            cgJhSpmx.setSqrq(sqrq);
            cgJhSpmx.setWorkstate("已提交");
            cgJhSpmx.setSqdh(cgJhSqd.getSqdh());
            this.baseMapper.updateCgJhSpckmx(cgJhSpmx);
            //SqlRunner.db().update(" UPDATE cg_jh_spckmx a inner join cg_jh_spmx b on a.spjhrq = b.spjhrq SET a.xhrq={0},a.workstate={1} WHERE b.sqdh = {2} ",xhrq,"已提交",cgJhSqd.getSqdh());
//spckmxService.update(spckUW);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<CgJhSqdIdParam> cgJhSqdIdParamList) {
        // 执行删除
        this.removeByIds(CollStreamUtil.toList(cgJhSqdIdParamList, CgJhSqdIdParam::getId));
    }

    @Override
    public CgJhSqd detail(CgJhSqdIdParam cgJhSqdIdParam) {
        return this.queryEntity(cgJhSqdIdParam.getId());
    }

    @Override
    public CgJhSqd queryEntity(String id) {
        CgJhSqd cgJhSqd = this.getById(id);
        if(ObjectUtil.isEmpty(cgJhSqd)) {
            throw new CommonException("cg_jh_sqd不存在，id值为：{}", id);
        }
        return cgJhSqd;
    }

    @Override
    public void editAudit(CgJhSqdEditParam cgJhSqdEditParam) {
        CgJhSqd cgJhSqd = this.queryEntity(cgJhSqdEditParam.getId());
        BeanUtil.copyProperties(cgJhSqdEditParam, cgJhSqd);
        this.updateById(cgJhSqd);
    }
}

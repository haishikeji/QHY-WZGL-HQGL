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
package com.px.biz.modular.zwbzrbmx.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.px.auth.core.util.StpLoginUserUtil;
import com.px.biz.modular.zwbzrb.entity.CgZwBzrb;
import com.px.biz.modular.zwbzrb.mapper.CgZwBzrbMapper;
import com.px.biz.modular.zwbzrb.service.CgZwBzrbService;
import com.px.common.enums.CommonSortOrderEnum;
import com.px.common.exception.CommonException;
import com.px.common.page.CommonPageRequest;
import com.px.biz.modular.zwbzrbmx.entity.CgZwBzrbmx;
import com.px.biz.modular.zwbzrbmx.mapper.CgZwBzrbmxMapper;
import com.px.biz.modular.zwbzrbmx.param.CgZwBzrbmxAddParam;
import com.px.biz.modular.zwbzrbmx.param.CgZwBzrbmxEditParam;
import com.px.biz.modular.zwbzrbmx.param.CgZwBzrbmxIdParam;
import com.px.biz.modular.zwbzrbmx.param.CgZwBzrbmxPageParam;
import com.px.biz.modular.zwbzrbmx.service.CgZwBzrbmxService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * cg_zw_bzrbmxService接口实现类
 *
 * @author 品讯科技
 * @date 2024-08
 **/
@Service
public class CgZwBzrbmxServiceImpl extends ServiceImpl<CgZwBzrbmxMapper, CgZwBzrbmx> implements CgZwBzrbmxService {

    @Resource
    private CgZwBzrbMapper bzrbMapper;

    @Resource
    private CgZwBzrbService bzrbService;

    @Override
    public Page<CgZwBzrbmx> page(CgZwBzrbmxPageParam cgZwBzrbmxPageParam) {
        QueryWrapper<CgZwBzrbmx> queryWrapper = new QueryWrapper<>();
        if(ObjectUtil.isNotEmpty(cgZwBzrbmxPageParam.getRbbh())) {
            queryWrapper.lambda().eq(CgZwBzrbmx::getRbbh, cgZwBzrbmxPageParam.getRbbh());
        }
        if(ObjectUtil.isNotEmpty(cgZwBzrbmxPageParam.getBmdm())) {
            queryWrapper.lambda().eq(CgZwBzrbmx::getBmdm, cgZwBzrbmxPageParam.getBmdm());
        }
        if(ObjectUtil.isNotEmpty(cgZwBzrbmxPageParam.getBzdm())) {
            queryWrapper.lambda().eq(CgZwBzrbmx::getBzdm, cgZwBzrbmxPageParam.getBzdm());
        }
        if (ObjectUtil.isAllNotEmpty(cgZwBzrbmxPageParam.getSortField(), cgZwBzrbmxPageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(cgZwBzrbmxPageParam.getSortOrder());
            queryWrapper.orderBy(true, cgZwBzrbmxPageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()),
                    StrUtil.toUnderlineCase(cgZwBzrbmxPageParam.getSortField()));
        } else {
            queryWrapper.lambda().orderByAsc(CgZwBzrbmx::getId);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(CgZwBzrbmxAddParam cgZwBzrbmxAddParam) {
        if (ObjectUtil.isNotEmpty(cgZwBzrbmxAddParam)) {
            List<CgZwBzrbmxAddParam> zwBzrbmxAddParamList = cgZwBzrbmxAddParam.getCgZwBzrbmxAddParamList();
            zwBzrbmxAddParamList.forEach(zwBzrbmxAddParam -> {
                //删除原有数据
                QueryWrapper<CgZwBzrbmx> rbmxQW = new QueryWrapper<>();
                rbmxQW.eq("bzdm", cgZwBzrbmxAddParam.getBzdm());
                rbmxQW.eq("bmdm", cgZwBzrbmxAddParam.getBmdm());
                rbmxQW.eq("rbbh", cgZwBzrbmxAddParam.getRbbh());
                rbmxQW.eq("lbdm", zwBzrbmxAddParam.getLbdm());
                this.remove(rbmxQW);
                //更新明细表
                CgZwBzrbmx cgZwBzrbmx = BeanUtil.toBean(zwBzrbmxAddParam, CgZwBzrbmx.class);
                cgZwBzrbmx.setRq(DateUtil.date());
                cgZwBzrbmx.setCzy(StpLoginUserUtil.getLoginUser().getName());
                cgZwBzrbmx.setRbbh(zwBzrbmxAddParam.getRbbh());
                this.save(cgZwBzrbmx);
            });
            //更新日报
            String dldm = cgZwBzrbmxAddParam.getDldm();
            final BigDecimal[] injeSum = {BigDecimal.valueOf(0)};
            QueryWrapper<CgZwBzrbmx> cgZwBzrbmxQueryWrapper= new QueryWrapper<>();
            cgZwBzrbmxQueryWrapper.eq("lblx",cgZwBzrbmxAddParam.getLblx());
            cgZwBzrbmxQueryWrapper.eq("bzdm", cgZwBzrbmxAddParam.getBzdm());
            cgZwBzrbmxQueryWrapper.eq("bmdm", cgZwBzrbmxAddParam.getBmdm());
            cgZwBzrbmxQueryWrapper.eq("rbbh", cgZwBzrbmxAddParam.getRbbh());
            cgZwBzrbmxQueryWrapper.select("ifnull(sum(inje),0)inje");
            List<CgZwBzrbmx> cgZwBzrbmxList=this.list(cgZwBzrbmxQueryWrapper);
            if(cgZwBzrbmxList.size()>0){
                injeSum[0] =cgZwBzrbmxList.get(0).getInje();
            }else{
                injeSum[0] = BigDecimal.valueOf(0);
            }

            UpdateWrapper<CgZwBzrb> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set(dldm, injeSum[0]);
            updateWrapper.eq("bzdm", cgZwBzrbmxAddParam.getBzdm());
            updateWrapper.eq("bmdm", cgZwBzrbmxAddParam.getBmdm());
            updateWrapper.eq("rbbh", cgZwBzrbmxAddParam.getRbbh());
            bzrbService.update(updateWrapper);
            bzrbService.updateYkje(cgZwBzrbmxAddParam.getBmdm(), cgZwBzrbmxAddParam.getBzdm(), cgZwBzrbmxAddParam.getRbbh());
        }
    }

    //计算盈亏金额
    private BigDecimal getYkje(CgZwBzrb cgZwBzrb) {
        //收入类
        BigDecimal yysrje = cgZwBzrb.getYysrje();//营业收入
        BigDecimal cpdbje = cgZwBzrb.getCpdbje();//成品调拨收入
        BigDecimal qtsrje = cgZwBzrb.getQtsrje();//其他收入
        BigDecimal shouru = NumberUtil.add(yysrje, cpdbje, qtsrje);
        //支出类
        BigDecimal hclje = cgZwBzrb.getHclje();//荤菜支出
        BigDecimal sclje = cgZwBzrb.getSclje();//素菜支出
        BigDecimal tllje = cgZwBzrb.getTllje();//调料类支出
        BigDecimal zslje = cgZwBzrb.getZslje();//主食类支出
        BigDecimal yplje = cgZwBzrb.getYplje();//饮品类支出
        BigDecimal dhclje = cgZwBzrb.getDhclje();//低耗类支出
        BigDecimal wxclje = cgZwBzrb.getWxclje();//维修材料支出
        BigDecimal qtlje = cgZwBzrb.getQtlje();//其它类支出
        BigDecimal sdqlje = cgZwBzrb.getSdqlje();//水电类支出
        BigDecimal dhlje = cgZwBzrb.getDhlje();//维护费用类支出
        BigDecimal cjlje = cgZwBzrb.getCjlje();//酬金类支出
        BigDecimal qtzcje = cgZwBzrb.getQtzcje();//其他支出
        BigDecimal zhichu = NumberUtil.add(hclje, sclje, tllje, zslje, yplje, dhclje, wxclje, qtlje, sdqlje, dhlje, cjlje, qtzcje);
        //计算盈亏金额
        BigDecimal ykje = NumberUtil.sub(shouru, zhichu);
        return ykje;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(CgZwBzrbmxEditParam cgZwBzrbmxEditParam) {
        CgZwBzrbmx cgZwBzrbmx = this.queryEntity(cgZwBzrbmxEditParam.getId());
        BeanUtil.copyProperties(cgZwBzrbmxEditParam, cgZwBzrbmx);
        this.updateById(cgZwBzrbmx);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<CgZwBzrbmxIdParam> cgZwBzrbmxIdParamList) {
        // 执行删除
        this.removeByIds(CollStreamUtil.toList(cgZwBzrbmxIdParamList, CgZwBzrbmxIdParam::getId));
    }

    @Override
    public CgZwBzrbmx detail(CgZwBzrbmxIdParam cgZwBzrbmxIdParam) {
        return this.queryEntity(cgZwBzrbmxIdParam.getId());
    }

    @Override
    public CgZwBzrbmx queryEntity(String id) {
        CgZwBzrbmx cgZwBzrbmx = this.getById(id);
        if (ObjectUtil.isEmpty(cgZwBzrbmx)) {
            throw new CommonException("cg_zw_bzrbmx不存在，id值为：{}", id);
        }
        return cgZwBzrbmx;
    }
}

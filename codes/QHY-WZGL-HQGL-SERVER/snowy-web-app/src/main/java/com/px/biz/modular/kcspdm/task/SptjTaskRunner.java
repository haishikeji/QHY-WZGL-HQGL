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
package com.px.biz.modular.kcspdm.task;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.px.biz.modular.kckczb.entity.CgKcKczb;
import com.px.biz.modular.kckczb.service.CgKcKczbService;
import com.px.biz.modular.kcspdm.entity.CgKcSpdm;
import com.px.biz.modular.kcspdm.service.CgKcSpdmService;
import com.px.common.timer.CommonTimerTaskRunner;

import javax.annotation.Resource;

/**
 * 定时器的一个示例
 *
 * @author 品讯科技
 * @date 2024-08
 **/
@Slf4j
@Component
public class SptjTaskRunner implements CommonTimerTaskRunner {

    @Resource
    private CgKcSpdmService cgKcSpdmService;

    @Resource
    private CgKcKczbService cgKcKczbService;
    @Override
    public void action() {
        QueryWrapper<CgKcSpdm> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tjshzt","待");
        queryWrapper.le("tjshrq", DateUtil.format(DateUtil.date(),"yyyy-MM-dd HH:mm:ss"));
        cgKcSpdmService.list(queryWrapper).forEach(cgKcSpdm -> {
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.set("zxtj",cgKcSpdm.getZxtj());
            updateWrapper.set("nowjj",cgKcSpdm.getZxtj());
            updateWrapper.set("gydj",cgKcSpdm.getNowgydj());
            updateWrapper.set("nowgydj",cgKcSpdm.getNowgydj());
            updateWrapper.set("tjshzt","是");
            updateWrapper.eq("id",cgKcSpdm.getId());
            cgKcSpdmService.update(updateWrapper);

            UpdateWrapper<CgKcKczb> cgKcKczbUpdateWrapper = new UpdateWrapper();
            cgKcKczbUpdateWrapper.set("nowjj",cgKcSpdm.getZxtj());
            cgKcKczbUpdateWrapper.set("gydj",cgKcSpdm.getNowgydj());
            cgKcKczbUpdateWrapper.eq("spdm",cgKcSpdm.getSpdm());
            cgKcKczbService.update(cgKcKczbUpdateWrapper);
        });


    }
}

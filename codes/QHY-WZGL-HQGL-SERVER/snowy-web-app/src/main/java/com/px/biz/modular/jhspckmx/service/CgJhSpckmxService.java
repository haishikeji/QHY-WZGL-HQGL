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
package com.px.biz.modular.jhspckmx.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.px.biz.modular.jhspckmx.entity.CgJhSpckmx;
import com.px.biz.modular.jhspckmx.param.CgJhSpckmxAddParam;
import com.px.biz.modular.jhspckmx.param.CgJhSpckmxEditParam;
import com.px.biz.modular.jhspckmx.param.CgJhSpckmxIdParam;
import com.px.biz.modular.jhspckmx.param.CgJhSpckmxPageParam;

import java.util.List;

/**
 * cg_jh_spckmxService接口
 *
 * @author 品讯科技
 * @date 2024-08
 **/
public interface CgJhSpckmxService extends IService<CgJhSpckmx> {

    /**
     * 获取cg_jh_spckmx分页
     *
     * @author 品讯科技
     * @date 2024-08
     */
    Page<CgJhSpckmx> page(CgJhSpckmxPageParam cgJhSpckmxPageParam);

    /**
     * 添加cg_jh_spckmx
     *
     * @author 品讯科技
     * @date 2024-08
     */
    void add(CgJhSpckmxAddParam cgJhSpckmxAddParam);

    /**
     * 编辑cg_jh_spckmx
     *
     * @author 品讯科技
     * @date 2024-08
     */
    void edit(CgJhSpckmxEditParam cgJhSpckmxEditParam);

    /**
     * 删除cg_jh_spckmx
     *
     * @author 品讯科技
     * @date 2024-08
     */
    void delete(List<CgJhSpckmxIdParam> cgJhSpckmxIdParamList);

    /**
     * 获取cg_jh_spckmx详情
     *
     * @author 品讯科技
     * @date 2024-08
     */
    CgJhSpckmx detail(CgJhSpckmxIdParam cgJhSpckmxIdParam);

    /**
     * 获取cg_jh_spckmx详情
     *
     * @author 品讯科技
     * @date 2024-08
     **/
    CgJhSpckmx queryEntity(String id);

    Page<CgJhSpckmx> rblbmxPage(CgJhSpckmxPageParam cgJhSpckmxPageParam);

    Page<CgJhSpckmx> rbspmxPage(CgJhSpckmxPageParam cgJhSpckmxPageParam);

    Page<CgJhSpckmx> bmybmxPage(CgJhSpckmxPageParam cgJhSpckmxPageParam);
}

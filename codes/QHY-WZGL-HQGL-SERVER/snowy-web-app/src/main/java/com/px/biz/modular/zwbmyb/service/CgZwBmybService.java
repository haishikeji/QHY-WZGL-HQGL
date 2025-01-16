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
package com.px.biz.modular.zwbmyb.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.px.biz.modular.jhspckmx.entity.CgJhSpckmx;
import com.px.biz.modular.jhspmx.entity.CgJhSpmx;
import com.px.biz.modular.zwbmyb.entity.CgZwBmyb;
import com.px.biz.modular.zwbmyb.param.CgZwBmybAddParam;
import com.px.biz.modular.zwbmyb.param.CgZwBmybEditParam;
import com.px.biz.modular.zwbmyb.param.CgZwBmybIdParam;
import com.px.biz.modular.zwbmyb.param.CgZwBmybPageParam;

import java.util.List;

/**
 * cg_zw_bmybService接口
 *
 * @author 品讯科技
 * @date 2024-08
 **/
public interface CgZwBmybService extends IService<CgZwBmyb> {

    /**
     * 获取cg_zw_bmyb分页
     *
     * @author 品讯科技
     * @date 2024-08
     */
    Page<CgZwBmyb> page(CgZwBmybPageParam cgZwBmybPageParam);

    /**
     * 添加cg_zw_bmyb
     *
     * @author 品讯科技
     * @date 2024-08
     */
    void add(CgZwBmybAddParam cgZwBmybAddParam);

    /**
     * 编辑cg_zw_bmyb
     *
     * @author 品讯科技
     * @date 2024-08
     */
    void edit(CgZwBmybEditParam cgZwBmybEditParam);

    /**
     * 删除cg_zw_bmyb
     *
     * @author 品讯科技
     * @date 2024-08
     */
    void delete(List<CgZwBmybIdParam> cgZwBmybIdParamList);

    /**
     * 获取cg_zw_bmyb详情
     *
     * @author 品讯科技
     * @date 2024-08
     */
    CgZwBmyb detail(CgZwBmybIdParam cgZwBmybIdParam);

    /**
     * 获取cg_zw_bmyb详情
     *
     * @author 品讯科技
     * @date 2024-08
     **/
    CgZwBmyb queryEntity(String id);

    void genBmyb(CgZwBmybAddParam cgZwBmybAddParam);

    Page<CgJhSpmx> bqcgPage(CgZwBmybPageParam cgZwBmybPageParam);

    Page<CgJhSpckmx> kcbsPage(CgZwBmybPageParam cgZwBmybPageParam);


    Page<CgJhSpckmx> ckjePage(CgZwBmybPageParam cgZwBmybPageParam);

    Page<CgJhSpmx> dbrkPage(CgZwBmybPageParam cgZwBmybPageParam);

    List<CgJhSpmx> dbckjePage(CgZwBmybPageParam cgZwBmybPageParam);

    Page<CgJhSpmx> cpdcPage(CgZwBmybPageParam cgZwBmybPageParam);

    List<CgJhSpmx> dbckPage(CgZwBmybPageParam cgZwBmybPageParam);
}

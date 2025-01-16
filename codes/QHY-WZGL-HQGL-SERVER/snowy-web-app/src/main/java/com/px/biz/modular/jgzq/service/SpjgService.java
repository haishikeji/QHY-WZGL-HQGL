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
package com.px.biz.modular.jgzq.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.px.biz.modular.jgzq.entity.Spjg;
import com.px.biz.modular.jgzq.param.SpjgAddParam;
import com.px.biz.modular.jgzq.param.SpjgEditParam;
import com.px.biz.modular.jgzq.param.SpjgIdParam;
import com.px.biz.modular.jgzq.param.SpjgPageParam;

import java.util.List;

/**
 * spjgService接口
 *
 * @author 品讯科技
 * @date 2024-08
 **/
public interface SpjgService extends IService<Spjg> {

    /**
     * 获取spjg分页
     *
     * @author 品讯科技
     * @date 2024-08
     */
    Page<Spjg> page(SpjgPageParam spjgPageParam);

    /**
     * 添加spjg
     *
     * @author 品讯科技
     * @date 2024-08
     */
    void add(SpjgAddParam spjgAddParam);

    /**
     * 编辑spjg
     *
     * @author 品讯科技
     * @date 2024-08
     */
    void edit(SpjgEditParam spjgEditParam);

    /**
     * 删除spjg
     *
     * @author 品讯科技
     * @date 2024-08
     */
    void delete(List<SpjgIdParam> spjgIdParamList);

    /**
     * 获取spjg详情
     *
     * @author 品讯科技
     * @date 2024-08
     */
    Spjg detail(SpjgIdParam spjgIdParam);

    /**
     * 获取spjg详情
     *
     * @author 品讯科技
     * @date 2024-08
     **/
    Spjg queryEntity(String id);
}

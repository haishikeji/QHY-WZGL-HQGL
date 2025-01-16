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
package com.px.biz.modular.gyscontract.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.px.biz.modular.gyscontract.entity.CgGysContract;
import com.px.biz.modular.gyscontract.param.CgGysContractAddParam;
import com.px.biz.modular.gyscontract.param.CgGysContractEditParam;
import com.px.biz.modular.gyscontract.param.CgGysContractIdParam;
import com.px.biz.modular.gyscontract.param.CgGysContractPageParam;

import java.util.List;

/**
 * 供应商合同Service接口
 *
 * @author 品讯科技
 * @date 2024-08
 **/
public interface CgGysContractService extends IService<CgGysContract> {

    /**
     * 获取供应商合同分页
     *
     * @author 品讯科技
     * @date 2024-08
     */
    Page<CgGysContract> page(CgGysContractPageParam cgGysContractPageParam);

    /**
     * 添加供应商合同
     *
     * @author 品讯科技
     * @date 2024-08
     */
    void add(CgGysContractAddParam cgGysContractAddParam);

    /**
     * 编辑供应商合同
     *
     * @author 品讯科技
     * @date 2024-08
     */
    void edit(CgGysContractEditParam cgGysContractEditParam);

    /**
     * 删除供应商合同
     *
     * @author 品讯科技
     * @date 2024-08
     */
    void delete(List<CgGysContractIdParam> cgGysContractIdParamList);

    /**
     * 获取供应商合同详情
     *
     * @author 品讯科技
     * @date 2024-08
     */
    CgGysContract detail(CgGysContractIdParam cgGysContractIdParam);

    /**
     * 获取供应商合同详情
     *
     * @author 品讯科技
     * @date 2024-08
     **/
    CgGysContract queryEntity(String id);
}

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
package com.px.biz.modular.jhspmx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.px.biz.modular.jhspckmx.entity.CgJhSpckmx;
import com.px.biz.modular.jhspmx.entity.CgJhSpmx;
import com.px.biz.modular.jhspmx.param.CgJhSpmxPageParam;

import java.util.List;

/**
 * cg_jh_spmxMapper接口
 *
 * @author 品讯科技
 * @date 2024-08
 **/
public interface CgJhSpmxMapper extends BaseMapper<CgJhSpmx> {

    String getDzc(CgJhSpmx cgJhSpmx);

    void updateJgBz(@Param("sqdhs")List<String> sqdhs);

    void updateJgBm(@Param("sqdhs")List<String> sqdhs);

    List<CgJhSpckmx> spckmxpagehz(@Param("cgJhSpmx") CgJhSpmxPageParam cgJhSpmx);

    // 部门成品调拨统计 2024
    List<CgJhSpmx> pageTj(@Param("cgJhSpmx") CgJhSpmxPageParam cgJhSpmxPageParam);

    List<CgJhSpmx> pagedrTj(@Param("cgJhSpmx") CgJhSpmxPageParam cgJhSpmxPageParam);
    List<CgJhSpmx> pagedcTj(@Param("cgJhSpmx") CgJhSpmxPageParam cgJhSpmxPageParam);

}

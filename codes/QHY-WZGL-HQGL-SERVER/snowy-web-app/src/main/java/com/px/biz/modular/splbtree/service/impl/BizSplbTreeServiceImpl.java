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
package com.px.biz.modular.splbtree.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.px.biz.core.enums.BizDataTypeEnum;
import com.px.common.enums.CommonSortOrderEnum;
import com.px.common.exception.CommonException;
import com.px.common.listener.CommonDataChangeEventCenter;
import com.px.common.page.CommonPageRequest;
import com.px.biz.modular.splbtree.entity.BizSplbTree;
import com.px.biz.modular.splbtree.mapper.BizSplbTreeMapper;
import com.px.biz.modular.splbtree.param.BizSplbTreeAddParam;
import com.px.biz.modular.splbtree.param.BizSplbTreeEditParam;
import com.px.biz.modular.splbtree.param.BizSplbTreeIdParam;
import com.px.biz.modular.splbtree.param.BizSplbTreePageParam;
import com.px.biz.modular.splbtree.service.BizSplbTreeService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 组织Service接口实现类
 *
 * @author 品讯科技
 * @date 2024-08
 **/
@Service
public class BizSplbTreeServiceImpl extends ServiceImpl<BizSplbTreeMapper, BizSplbTree> implements BizSplbTreeService {

    @Override
    public Page<BizSplbTree> page(BizSplbTreePageParam bizSplbTreePageParam) {
        QueryWrapper<BizSplbTree> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isAllNotEmpty(bizSplbTreePageParam.getSortField(), bizSplbTreePageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(bizSplbTreePageParam.getSortOrder());
            queryWrapper.orderBy(true, bizSplbTreePageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()),
                    StrUtil.toUnderlineCase(bizSplbTreePageParam.getSortField()));
        } else {
            queryWrapper.lambda().orderByAsc(BizSplbTree::getSortCode);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(BizSplbTreeAddParam bizSplbTreeAddParam) {
        BizSplbTree bizSplbTree = BeanUtil.toBean(bizSplbTreeAddParam, BizSplbTree.class);
        // 重复名称
        boolean repeatName = this.count(new LambdaQueryWrapper<BizSplbTree>().eq(BizSplbTree::getParentId, bizSplbTree.getParentId())
                .eq(BizSplbTree::getName, bizSplbTree.getName())) > 0;
        if(repeatName) {
            throw new CommonException("存在重复的同级商品类别，名称为：{}", bizSplbTree.getName());
        }
        bizSplbTree.setCode(RandomUtil.randomString(10));
        this.save(bizSplbTree);
        // 发布增加事件
        CommonDataChangeEventCenter.doAddWithData(BizDataTypeEnum.SPLB.getValue(), JSONUtil.createArray().put(bizSplbTree));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(BizSplbTreeEditParam bizSplbTreeEditParam) {
        BizSplbTree bizSplbTree = this.queryEntity(bizSplbTreeEditParam.getId());
        BeanUtil.copyProperties(bizSplbTreeEditParam, bizSplbTree);
        this.updateById(bizSplbTree);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<BizSplbTreeIdParam> bizSplbTreeIdParamList) {
        // 执行删除
        this.removeByIds(CollStreamUtil.toList(bizSplbTreeIdParamList, BizSplbTreeIdParam::getId));
    }

    @Override
    public BizSplbTree detail(BizSplbTreeIdParam bizSplbTreeIdParam) {
        return this.queryEntity(bizSplbTreeIdParam.getId());
    }

    @Override
    public BizSplbTree queryEntity(String id) {
        BizSplbTree bizSplbTree = this.getById(id);
        if (ObjectUtil.isEmpty(bizSplbTree)) {
            throw new CommonException("组织不存在，id值为：{}", id);
        }
        return bizSplbTree;
    }

    @Override
    public List<Tree<String>> tree() {
        // 获取所有机构
        List<BizSplbTree> allSplbList = this.list();
        // 定义机构集合
        Set<BizSplbTree> bizSplbSet = CollectionUtil.newHashSet();

        bizSplbSet.addAll(allSplbList);
//        // 校验数据范围
//        List<String> loginUserDataScope = StpLoginUserUtil.getLoginUserDataScope();
//        if(ObjectUtil.isNotEmpty(loginUserDataScope)) {
//            loginUserDataScope.forEach(orgId -> bizFoodSet.addAll(this.getParentListById(allFoodList, orgId, true)));
//        } else {
//            return CollectionUtil.newArrayList();
//        }
        List<TreeNode<String>> treeNodeList = bizSplbSet.stream().map(BizSplbTree ->
                        new TreeNode<>(BizSplbTree.getId(), BizSplbTree.getParentId(),
                                BizSplbTree.getName(), BizSplbTree.getSortCode()).setExtra(JSONUtil.parseObj(BizSplbTree)))
                .collect(Collectors.toList());
        return TreeUtil.build(treeNodeList, "0");
    }
}

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
package com.px.biz.modular.codegys.service.impl;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.px.auth.core.util.StpGysUtil;
import com.px.auth.modular.login.enums.AuthDeviceTypeEnum;
import com.px.auth.modular.login.enums.AuthExceptionEnum;
import com.px.auth.modular.login.param.AuthAccountPasswordLoginParam;
import com.px.biz.modular.kckczb.entity.CgKcKczb;
import com.px.biz.modular.kckczb.service.CgKcKczbService;
import com.px.common.cache.CommonCacheOperator;
import com.px.common.enums.CommonSortOrderEnum;
import com.px.common.exception.CommonException;
import com.px.common.page.CommonPageRequest;
import com.px.biz.modular.codegys.entity.CgCodeGys;
import com.px.biz.modular.codegys.mapper.CgCodeGysMapper;
import com.px.biz.modular.codegys.param.CgCodeGysAddParam;
import com.px.biz.modular.codegys.param.CgCodeGysEditParam;
import com.px.biz.modular.codegys.param.CgCodeGysIdParam;
import com.px.biz.modular.codegys.param.CgCodeGysPageParam;
import com.px.biz.modular.codegys.service.CgCodeGysService;
import com.px.common.util.CommonCryptogramUtil;
import com.px.dev.api.DevConfigApi;
import com.px.sys.modular.user.param.SysUserUpdatePwdParam;

import javax.annotation.Resource;
import java.util.List;

/**
 * cg_code_gysService接口实现类
 *
 * @author 品讯科技
 * @date 2024-08
 **/
@Service
public class CgCodeGysServiceImpl extends ServiceImpl<CgCodeGysMapper, CgCodeGys> implements CgCodeGysService {
    @Resource
    private DevConfigApi devConfigApi;

    @Resource
    private CommonCacheOperator commonCacheOperator;

    @Resource
    private CgKcKczbService cgKcKczbService;

    @Override
    public Page<CgCodeGys> page(CgCodeGysPageParam cgCodeGysPageParam) {
        QueryWrapper<CgCodeGys> queryWrapper = new QueryWrapper<>();
        if(ObjectUtil.isNotEmpty(cgCodeGysPageParam.getGysmc())) {
            queryWrapper.lambda().like(CgCodeGys::getGysmc, cgCodeGysPageParam.getGysmc());
        }
        if(ObjectUtil.isNotEmpty(cgCodeGysPageParam.getPyjm())) {
            queryWrapper.lambda().like(CgCodeGys::getPyjm, cgCodeGysPageParam.getPyjm());
        }
        if(ObjectUtil.isNotEmpty(cgCodeGysPageParam.getGyslb())) {
            queryWrapper.lambda().like(CgCodeGys::getGyslb, cgCodeGysPageParam.getGyslb());
        }
        if(ObjectUtil.isNotEmpty(cgCodeGysPageParam.getXyd())) {
            queryWrapper.lambda().like(CgCodeGys::getXyd, cgCodeGysPageParam.getXyd());
        }
        if(ObjectUtil.isNotEmpty(cgCodeGysPageParam.getJyfw())) {
            queryWrapper.lambda().like(CgCodeGys::getJyfw, cgCodeGysPageParam.getJyfw());
        }
        if(ObjectUtil.isNotEmpty(cgCodeGysPageParam.getGhzt())) {
            queryWrapper.lambda().like(CgCodeGys::getGhzt, cgCodeGysPageParam.getGhzt());
        }
        if(ObjectUtil.isAllNotEmpty(cgCodeGysPageParam.getSortField(), cgCodeGysPageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(cgCodeGysPageParam.getSortOrder());
            queryWrapper.orderBy(true, cgCodeGysPageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()),
                    StrUtil.toUnderlineCase(cgCodeGysPageParam.getSortField()));
        } else {
            queryWrapper.lambda().orderByAsc(CgCodeGys::getId);
        }
        queryWrapper.select("id, gysdm, gysmc, pyjm, gysxh, gyslb, xyd, frdb, zczb, dz, yb, khyh, yhzh, lxr, dh, cz, email, www, jyfw, ghzt, szrq, gyspasswd, bz,(select count(*) from cg_gys_prc where prc_id=cg_code_gys.gysdm and now()>file_expired)count");
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(CgCodeGysAddParam cgCodeGysAddParam) {
        CgCodeGys cgCodeGys = BeanUtil.toBean(cgCodeGysAddParam, CgCodeGys.class);
        this.save(cgCodeGys);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(CgCodeGysEditParam cgCodeGysEditParam) {
        CgCodeGys cgCodeGys = this.queryEntity(cgCodeGysEditParam.getId());
        BeanUtil.copyProperties(cgCodeGysEditParam, cgCodeGys);
        if(cgCodeGys.getGhzt().equals("停供")){
            UpdateWrapper<CgKcKczb> cgKcKczbUpdateWrapper= new UpdateWrapper<>();
            cgKcKczbUpdateWrapper.eq("gysdm",cgCodeGys.getGysdm());
            cgKcKczbUpdateWrapper.set("gysdm",null);
            cgKcKczbUpdateWrapper.set("gysmc",null);
            cgKcKczbService.update(cgKcKczbUpdateWrapper);
        }
        this.updateById(cgCodeGys);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<CgCodeGysIdParam> cgCodeGysIdParamList) {
        // 执行删除
        this.removeByIds(CollStreamUtil.toList(cgCodeGysIdParamList, CgCodeGysIdParam::getId));
    }

    @Override
    public CgCodeGys detail(CgCodeGysIdParam cgCodeGysIdParam) {
        return this.queryEntity(cgCodeGysIdParam.getId());
    }

    @Override
    public CgCodeGys queryEntity(String id) {
        CgCodeGys cgCodeGys = this.getById(id);
        if(ObjectUtil.isEmpty(cgCodeGys)) {
            throw new CommonException("cg_code_gys不存在，id值为：{}", id);
        }
        return cgCodeGys;
    }
    private static final String SNOWY_SYS_DEFAULT_CAPTCHA_OPEN_KEY = "SNOWY_SYS_DEFAULT_CAPTCHA_OPEN";
    private static final String GYS_CACHE_KEY = "auth-validCode:";
    @Override
    public String doGysLogin(AuthAccountPasswordLoginParam authAccountPasswordLoginParam, String type) {
        // 获取账号
        String account = authAccountPasswordLoginParam.getAccount();
        // 获取密码
        String password = authAccountPasswordLoginParam.getPassword();
        // 获取设备
        String device = authAccountPasswordLoginParam.getDevice();
        // 默认指定为PC，如在小程序跟移动端的情况下，自行指定即可
        if(ObjectUtil.isEmpty(device)) {
            device = AuthDeviceTypeEnum.PC.getValue();
        } else {
            AuthDeviceTypeEnum.validate(device);
        }
        // 校验验证码
        String defaultCaptchaOpen = devConfigApi.getValueByKey(SNOWY_SYS_DEFAULT_CAPTCHA_OPEN_KEY);
        if(ObjectUtil.isNotEmpty(defaultCaptchaOpen)) {
            if(Convert.toBool(defaultCaptchaOpen)) {
                // 获取验证码
                String validCode = authAccountPasswordLoginParam.getValidCode();
                // 获取验证码请求号
                String validCodeReqNo = authAccountPasswordLoginParam.getValidCodeReqNo();
                // 开启验证码则必须传入验证码
                if(ObjectUtil.isEmpty(validCode)) {
                    throw new CommonException(AuthExceptionEnum.VALID_CODE_EMPTY.getValue());
                }
                // 开启验证码则必须传入验证码请求号
                if(ObjectUtil.isEmpty(validCodeReqNo)) {
                    throw new CommonException(AuthExceptionEnum.VALID_CODE_REQ_NO_EMPTY.getValue());
                }
                // 执行校验验证码
                validValidCode(validCode, validCodeReqNo);
            }
        }
        // SM2解密并获得前端传来的密码哈希值
        String passwordHash;
        try {
            // 解密，并做哈希值
            passwordHash = CommonCryptogramUtil.doHashValue(CommonCryptogramUtil.doSm2Decrypt(password));
        } catch (Exception e) {
            throw new CommonException(AuthExceptionEnum.PWD_DECRYPT_ERROR.getValue());
        }
        // 根据账号获取用户信息，根据B端或C端判断

            QueryWrapper<CgCodeGys> cgCodeGysQueryWrapper = new QueryWrapper<>();
            cgCodeGysQueryWrapper.eq("gysdm",account);
            CgCodeGys saBaseLoginUser = this.getOne(cgCodeGysQueryWrapper);
            if(ObjectUtil.isEmpty(saBaseLoginUser)) {
                throw new CommonException(AuthExceptionEnum.ACCOUNT_ERROR.getValue());
            }
            if (!saBaseLoginUser.getGyspasswd().equals(passwordHash)) {
                throw new CommonException(AuthExceptionEnum.PWD_ERROR.getValue());
            }
            // 执行端登录
            return execGysLogin(saBaseLoginUser, device);

    }
    /**
     * 执行登录
     *
     * @author 品讯科技
     * @date 2024-08
     **/
    private String execGysLogin(CgCodeGys saBaseLoginUser, String device) {
        // 校验状态
        if(!saBaseLoginUser.getGhzt().equals("正常")) {
            throw new CommonException(AuthExceptionEnum.ACCOUNT_DISABLED.getValue());
        }

        // 执行登录
        StpGysUtil.login(saBaseLoginUser.getGysdm(), new SaLoginModel().setDevice(device).setExtra("name", saBaseLoginUser.getGysmc()));
//        // 角色集合
//        List<JSONObject> roleList = loginUserApi.getRoleListByUserId(saBaseLoginUser.getId());
//        // 角色id集合
//        List<String> roleIdList = roleList.stream().map(jsonObject -> jsonObject.getStr("id")).collect(Collectors.toList());
//        // 角色码集合
//        List<String> roleCodeList = roleList.stream().map(jsonObject -> jsonObject.getStr("code")).collect(Collectors.toList());
//        // 角色id和用户id集合
//        List<String> userAndRoleIdList = CollectionUtil.unionAll(roleIdList, CollectionUtil.newArrayList(saBaseLoginUser.getId()));
//        // 获取按钮码
//        saBaseLoginUser.setButtonCodeList(loginUserApi.getButtonCodeListListByUserAndRoleIdList(userAndRoleIdList));
//        // 获取移动端按钮码
//        saBaseLoginUser.setMobileButtonCodeList(loginUserApi.getMobileButtonCodeListListByUserIdAndRoleIdList(userAndRoleIdList));
//        // 获取数据范围
//        saBaseLoginUser.setDataScopeList(Convert.toList(SaBaseLoginUser.DataScope.class,
//                loginUserApi.getPermissionListByUserIdAndRoleIdList(userAndRoleIdList, saBaseLoginUser.getOrgId())));
//        // 获取权限码
//        saBaseLoginUser.setPermissionCodeList(saBaseLoginUser.getDataScopeList().stream()
//                .map(SaBaseLoginUser.DataScope::getApiUrl).collect(Collectors.toList()));
//        // 获取角色码
//        saBaseLoginUser.setRoleCodeList(roleCodeList);
        // 缓存用户信息，此处使用TokenSession为了指定时间内无操作则自动下线
        StpGysUtil.getTokenSession().set("loginUser", saBaseLoginUser);
        String token=StpGysUtil.getTokenInfo().tokenValue;
        // 返回token
        return token;
    }
    /**
     * 校验验证码方法
     *
     * @author 品讯科技
     * @date 2024-08
     **/
    private void validValidCode(String validCode, String validCodeReqNo) {
        // 依据请求号，取出缓存中的验证码进行校验
        Object existValidCode = commonCacheOperator.get(GYS_CACHE_KEY + validCodeReqNo);
        // 为空则直接验证码错误
        if(ObjectUtil.isEmpty(existValidCode)) {
            throw new CommonException(AuthExceptionEnum.VALID_CODE_ERROR.getValue());
        }
        // 不一致则直接验证码错误
        if(!validCode.equals(Convert.toStr(existValidCode).toLowerCase())) {
            // 移除该验证码
            commonCacheOperator.remove(GYS_CACHE_KEY + validCodeReqNo);
            throw new CommonException(AuthExceptionEnum.VALID_CODE_ERROR.getValue());
        }
        // 移除该验证码
        commonCacheOperator.remove(GYS_CACHE_KEY + validCodeReqNo);
    }


    @Override
    public void updatePassword(SysUserUpdatePwdParam sysUserUpdatePwdParam) {
        QueryWrapper<CgCodeGys> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("gysdm",StpGysUtil.getLoginIdAsString());
        CgCodeGys sysUser = this.getOne(queryWrapper);
        String password = sysUserUpdatePwdParam.getPassword();
        String newPassword = sysUserUpdatePwdParam.getNewPassword();
        if (!CommonCryptogramUtil.doHashValue(password).equals(sysUser.getGyspasswd())) {
            throw new CommonException("原密码错误");
        }
        this.update(new LambdaUpdateWrapper<CgCodeGys>().eq(CgCodeGys::getGysdm,
                sysUser.getGysdm()).set(CgCodeGys::getGyspasswd,
                CommonCryptogramUtil.doHashValue(newPassword)));
    }
}

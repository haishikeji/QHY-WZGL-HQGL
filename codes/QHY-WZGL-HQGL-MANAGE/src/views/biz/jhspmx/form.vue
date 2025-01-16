<template>
    <a-modal
        :title="formData.id ? '编辑cg_jh_spmx' : '增加cg_jh_spmx'"
        :width="600"
        :visible="visible"
        :destroy-on-close="true"
        :footer-style="{ textAlign: 'right' }"
		@cancel="onClose"
		:maskClosable="false"
    >
        <a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
            <a-row :gutter="16">
                <a-col :span="12">
                    <a-form-item label="申请单号" name="sqdh">
                        <a-input v-model:value="formData.sqdh" placeholder="请输入申请单号" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="采购单号" name="cgdh">
                        <a-input v-model:value="formData.cgdh" placeholder="请输入采购单号" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="送货单号：" name="shdh">
                        <a-input v-model:value="formData.shdh" placeholder="请输入送货单号" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="需货日期：" name="xhrq">
                        <a-date-picker v-model:value="formData.xhrq" value-format="YYYY-MM-DD HH:mm:ss" show-time placeholder="请选择需货日期" style="width: 100%" />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="申请日期：" name="sqrq">
                        <a-date-picker v-model:value="formData.sqrq" value-format="YYYY-MM-DD HH:mm:ss" show-time placeholder="请选择申请日期" style="width: 100%" />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="申请人：" name="sqr">
                        <a-input v-model:value="formData.sqr" placeholder="请输入申请人" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="商品进货日期：" name="spjhrq">
                        <a-input v-model:value="formData.spjhrq" placeholder="请输入商品进货日期" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="采购类型：" name="cglx">
                        <a-input v-model:value="formData.cglx" placeholder="请输入采购类型" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="一级部门代码：" name="yjbmdm">
                        <a-input v-model:value="formData.yjbmdm" placeholder="请输入一级部门代码" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="一级部门名称：" name="yjbmmc">
                        <a-input v-model:value="formData.yjbmmc" placeholder="请输入一级部门名称" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="部门代码：" name="bmdm">
                        <a-input v-model:value="formData.bmdm" placeholder="请输入部门代码" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="部门名称：" name="bmmc">
                        <a-input v-model:value="formData.bmmc" placeholder="请输入部门名称" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="商品代码：" name="spdm">
                        <a-input v-model:value="formData.spdm" placeholder="请输入商品代码" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="商品名称：" name="spmc">
                        <a-input v-model:value="formData.spmc" placeholder="请输入商品名称" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="商品规格：" name="spgg">
                        <a-input v-model:value="formData.spgg" placeholder="请输入商品规格" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="品牌产地：" name="ppcd">
                        <a-input v-model:value="formData.ppcd" placeholder="请输入品牌产地" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="包装率：" name="bzl">
                        <a-input v-model:value="formData.bzl" placeholder="请输入包装率" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="成本分类：" name="spfl">
                        <a-input v-model:value="formData.spfl" placeholder="请输入成本分类" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="保质期天数：" name="bzts">
                        <a-input v-model:value="formData.bzts" placeholder="请输入保质期天数" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="保质日期：" name="bzrq">
                        <a-date-picker v-model:value="formData.bzrq" value-format="YYYY-MM-DD HH:mm:ss" show-time placeholder="请选择保质日期" style="width: 100%" />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="商品大类代码（01）：" name="dldm">
                        <a-input v-model:value="formData.dldm" placeholder="请输入商品大类代码" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="商品大类名称：" name="dlmc">
                        <a-input v-model:value="formData.dlmc" placeholder="请输入商品大类名称" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="商品类别：" name="lbdm">
                        <a-input v-model:value="formData.lbdm" placeholder="请输入商品类别" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="类别名称：" name="lbmc">
                        <a-input v-model:value="formData.lbmc" placeholder="请输入类别名称" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="供应商代码：" name="gysdm">
                        <a-input v-model:value="formData.gysdm" placeholder="请输入供应商代码" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="供应商名称：" name="gysmc">
                        <a-input v-model:value="formData.gysmc" placeholder="请输入供应商名称" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="商品计量单位：" name="jldw">
                        <a-input v-model:value="formData.jldw" placeholder="请输入商品计量单位" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="采购日期：" name="cgrq">
                        <a-date-picker v-model:value="formData.cgrq" value-format="YYYY-MM-DD HH:mm:ss" show-time placeholder="请选择采购日期" style="width: 100%" />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="验货日期：" name="yhrq">
                        <a-date-picker v-model:value="formData.yhrq" value-format="YYYY-MM-DD HH:mm:ss" show-time placeholder="请选择验货日期" style="width: 100%" />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="验货人：" name="yhr">
                        <a-input v-model:value="formData.yhr" placeholder="请输入验货人" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="审核人：" name="shr">
                        <a-input v-model:value="formData.shr" placeholder="请输入审核人" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="审核日期（即入库日期）：" name="shrq">
                        <a-date-picker v-model:value="formData.shrq" value-format="YYYY-MM-DD HH:mm:ss" show-time placeholder="请选择审核日期（即入库日期）" style="width: 100%" />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="申请数量：" name="sqsl">
                        <a-input v-model:value="formData.sqsl" placeholder="请输入申请数量" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="收货数量：" name="shsl">
                        <a-input v-model:value="formData.shsl" placeholder="请输入收货数量" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="出库数量：" name="cksl">
                        <a-input v-model:value="formData.cksl" placeholder="请输入出库数量" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="库存数量：" name="kcsl">
                        <a-input v-model:value="formData.kcsl" placeholder="请输入库存数量" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="参考单价：" name="ckdj">
                        <a-input v-model:value="formData.ckdj" placeholder="请输入参考单价" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="进货单价：" name="jhdj">
                        <a-input v-model:value="formData.jhdj" placeholder="请输入进货单价" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="供应单价：" name="gydj">
                        <a-input v-model:value="formData.gydj" placeholder="请输入供应单价" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="进货金额：" name="jhje">
                        <a-input v-model:value="formData.jhje" placeholder="请输入进货金额" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="供应金额：" name="gyje">
                        <a-input v-model:value="formData.gyje" placeholder="请输入供应金额" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="业务状态：" name="workstate">
                        <a-input v-model:value="formData.workstate" placeholder="请输入业务状态" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="审批状态：" name="spstate">
                        <a-input v-model:value="formData.spstate" placeholder="请输入审批状态" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="原始批次：" name="yspc">
                        <a-input v-model:value="formData.yspc" placeholder="请输入原始批次" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="商品来源：" name="sply">
                        <a-input v-model:value="formData.sply" placeholder="请输入商品来源" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="商品来源班组：" name="splybz">
                        <a-input v-model:value="formData.splybz" placeholder="请输入商品来源班组" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="分发班组：" name="ffbz">
                        <a-input v-model:value="formData.ffbz" placeholder="请输入分发班组" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="备注：" name="bz">
                        <a-input v-model:value="formData.bz" placeholder="请输入备注" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="YTSM：" name="ytsm">
                        <a-input v-model:value="formData.ytsm" placeholder="请输入YTSM" allow-clear />
                    </a-form-item>
                </a-col>
            </a-row>
        </a-form>
        <template #footer>
            <a-button style="margin-right: 8px" @click="onClose">关闭</a-button>
            <a-button type="primary" @click="onSubmit" :loading="submitLoading">保存</a-button>
        </template>
    </a-modal>
</template>

<script setup name="cgJhSpmxForm">
    import { cloneDeep } from 'lodash-es'
    import { required } from '@/utils/formRules'
    import cgJhSpmxApi from '@/api/biz/cgJhSpmxApi'
    // 抽屉状态
    const visible = ref(false)
    const emit = defineEmits({ successful: null })
    const formRef = ref()
    // 表单数据
    const formData = ref({})
    const submitLoading = ref(false)

    // 打开抽屉
    const onOpen = (record) => {
        visible.value = true
        if (record) {
            let recordData = cloneDeep(record)
            formData.value = Object.assign({}, recordData)
        }
    }
    // 关闭抽屉
    const onClose = () => {
        formRef.value.resetFields()
        formData.value = {}
        visible.value = false
    }
    // 默认要校验的
    const formRules = {
    }
    // 验证并提交数据
    const onSubmit = () => {
        formRef.value.validate().then(() => {
            submitLoading.value = true
            const formDataParam = cloneDeep(formData.value)
            cgJhSpmxApi
                .cgJhSpmxSubmitForm(formDataParam, !formDataParam.id)
                .then(() => {
                    onClose()
                    emit('successful')
                })
                .finally(() => {
                    submitLoading.value = false
                })
        })
    }
    // 抛出函数
    defineExpose({
        onOpen
    })
</script>

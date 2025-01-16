<template>
    <a-drawer
        :title="formData.id ? '编辑cg_jh_sqd' : '增加cg_jh_sqd'"
        :width="600"
        :visible="visible"
        :destroy-on-close="true"
        :footer-style="{ textAlign: 'right' }"
        @close="onClose"
    >
        <a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
            <a-form-item label="单据编号(4位年+2位月+2位日+4位部门号+2位流水号)：" name="sqdh">
                <a-input v-model:value="formData.sqdh" placeholder="请输入单据编号(4位年+2位月+2位日+4位部门号+2位流水号)" allow-clear />
            </a-form-item>
            <a-form-item label="申请日期：" name="sqrq">
                <a-date-picker v-model:value="formData.sqrq" value-format="YYYY-MM-DD HH:mm:ss" show-time placeholder="请选择申请日期" style="width: 100%" />
            </a-form-item>
            <a-form-item label="需货日期：" name="xhrq">
                <a-date-picker v-model:value="formData.xhrq" value-format="YYYY-MM-DD HH:mm:ss" show-time placeholder="请选择需货日期" style="width: 100%" />
            </a-form-item>
            <a-form-item label="需货备注（如上午，下午等）：" name="xhbz">
                <a-input v-model:value="formData.xhbz" placeholder="请输入需货备注（如上午，下午等）" allow-clear />
            </a-form-item>
            <a-form-item label="采购类型（班组订货、部门备货、部门调拨、采购退货）：" name="cglx">
                <a-input v-model:value="formData.cglx" placeholder="请输入采购类型（班组订货、部门备货、部门调拨、采购退货）" allow-clear />
            </a-form-item>
            <a-form-item label="申请人姓名：" name="sqr">
                <a-input v-model:value="formData.sqr" placeholder="请输入申请人姓名" allow-clear />
            </a-form-item>
            <a-form-item label="部门代码（食堂代码）：" name="bmdm">
                <a-input v-model:value="formData.bmdm" placeholder="请输入部门代码（食堂代码）" allow-clear />
            </a-form-item>
            <a-form-item label="部门名称（食堂名称）：" name="bmmc">
                <a-input v-model:value="formData.bmmc" placeholder="请输入部门名称（食堂名称）" allow-clear />
            </a-form-item>
            <a-form-item label="审核人1：" name="shr1">
                <a-input v-model:value="formData.shr1" placeholder="请输入审核人1" allow-clear />
            </a-form-item>
            <a-form-item label="审核日期1：" name="shrq1">
                <a-date-picker v-model:value="formData.shrq1" value-format="YYYY-MM-DD HH:mm:ss" show-time placeholder="请选择审核日期1" style="width: 100%" />
            </a-form-item>
            <a-form-item label="审核备注1：" name="shbz1">
                <a-input v-model:value="formData.shbz1" placeholder="请输入审核备注1" allow-clear />
            </a-form-item>
            <a-form-item label="审核人2：" name="shr2">
                <a-input v-model:value="formData.shr2" placeholder="请输入审核人2" allow-clear />
            </a-form-item>
            <a-form-item label="审核日期2：" name="shrq2">
                <a-date-picker v-model:value="formData.shrq2" value-format="YYYY-MM-DD HH:mm:ss" show-time placeholder="请选择审核日期2" style="width: 100%" />
            </a-form-item>
            <a-form-item label="审核备注2：" name="shbz2">
                <a-input v-model:value="formData.shbz2" placeholder="请输入审核备注2" allow-clear />
            </a-form-item>
            <a-form-item label="单据的合计金额：" name="hjje">
                <a-input v-model:value="formData.hjje" placeholder="请输入单据的合计金额" allow-clear />
            </a-form-item>
            <a-form-item label="打印否：" name="dyf">
                <a-input v-model:value="formData.dyf" placeholder="请输入打印否" allow-clear />
            </a-form-item>
            <a-form-item label="业务状态（申请、审批退回、采购退回、待审批、待采购、采购中、待收货、收货中、已收货）：" name="workstate">
                <a-input v-model:value="formData.workstate" placeholder="请输入业务状态（申请、审批退回、采购退回、待审批、待采购、采购中、待收货、收货中、已收货）" allow-clear />
            </a-form-item>
            <a-form-item label="审批状态（未审批、审批退回、待一审、待二审、已审批、不需审批）：" name="spstate">
                <a-input v-model:value="formData.spstate" placeholder="请输入审批状态（未审批、审批退回、待一审、待二审、已审批、不需审批）" allow-clear />
            </a-form-item>
            <a-form-item label="审核级别：" name="shjb">
                <a-input v-model:value="formData.shjb" placeholder="请输入审核级别" allow-clear />
            </a-form-item>
            <a-form-item label="采购退回理由：" name="cgthly">
                <a-input v-model:value="formData.cgthly" placeholder="请输入采购退回理由" allow-clear />
            </a-form-item>
            <a-form-item label="退回人：" name="bz">
                <a-input v-model:value="formData.bz" placeholder="请输入退回人" allow-clear />
            </a-form-item>
            <a-form-item label="退回日期：" name="cgthr">
                <a-input v-model:value="formData.cgthr" placeholder="请输入退回日期" allow-clear />
            </a-form-item>
            <a-form-item label="备注：" name="cgthrq">
                <a-date-picker v-model:value="formData.cgthrq" value-format="YYYY-MM-DD HH:mm:ss" show-time placeholder="请选择备注" style="width: 100%" />
            </a-form-item>
        </a-form>
        <template #footer>
            <a-button style="margin-right: 8px" @click="onClose">关闭</a-button>
            <a-button type="primary" @click="onSubmit" :loading="submitLoading">保存</a-button>
        </template>
    </a-drawer>
</template>

<script setup name="cgJhSqdForm">
    import { cloneDeep } from 'lodash-es'
    import { required } from '@/utils/formRules'
    import cgJhSqdApi from '@/api/biz/cgJhSqdApi'
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
            cgJhSqdApi
                .cgJhSqdSubmitForm(formDataParam, !formDataParam.id)
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

<template>
    <a-drawer
        :title="formData.id ? '编辑cg_jh_dhd' : '增加cg_jh_dhd'"
        :width="600"
        :visible="visible"
        :destroy-on-close="true"
        :footer-style="{ textAlign: 'right' }"
        @close="onClose"
    >
        <a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
            <a-form-item label="采购单号(4位年+2位月+2位日+4位流水号)：" name="cgdh">
                <a-input v-model:value="formData.cgdh" placeholder="请输入采购单号(4位年+2位月+2位日+4位流水号)" allow-clear />
            </a-form-item>
            <a-form-item label="采购日期（送货日期）：" name="cgrq">
                <a-date-picker v-model:value="formData.cgrq" value-format="YYYY-MM-DD HH:mm:ss" show-time placeholder="请选择采购日期（送货日期）" style="width: 100%" />
            </a-form-item>
            <a-form-item label="订货人：" name="dhr">
                <a-input v-model:value="formData.dhr" placeholder="请输入订货人" allow-clear />
            </a-form-item>
            <a-form-item label="订货日期：" name="dhrq">
                <a-date-picker v-model:value="formData.dhrq" value-format="YYYY-MM-DD HH:mm:ss" show-time placeholder="请选择订货日期" style="width: 100%" />
            </a-form-item>
            <a-form-item label="审核人：" name="shr">
                <a-input v-model:value="formData.shr" placeholder="请输入审核人" allow-clear />
            </a-form-item>
            <a-form-item label="审核日期：" name="shrq">
                <a-date-picker v-model:value="formData.shrq" value-format="YYYY-MM-DD HH:mm:ss" show-time placeholder="请选择审核日期" style="width: 100%" />
            </a-form-item>
            <a-form-item label="GYSQRRQ：" name="gysqrrq">
                <a-date-picker v-model:value="formData.gysqrrq" value-format="YYYY-MM-DD HH:mm:ss" show-time placeholder="请选择GYSQRRQ" style="width: 100%" />
            </a-form-item>
            <a-form-item label="供应商代码：" name="gysdm">
                <a-input v-model:value="formData.gysdm" placeholder="请输入供应商代码" allow-clear />
            </a-form-item>
            <a-form-item label="供应商名称：" name="gysmc">
                <a-input v-model:value="formData.gysmc" placeholder="请输入供应商名称" allow-clear />
            </a-form-item>
            <a-form-item label="商品金额：" name="spje">
                <a-input v-model:value="formData.spje" placeholder="请输入商品金额" allow-clear />
            </a-form-item>
            <a-form-item label="状态（订货中、已订货、已送货）：" name="workstate">
                <a-input v-model:value="formData.workstate" placeholder="请输入状态（订货中、已订货、已送货）" allow-clear />
            </a-form-item>
            <a-form-item label="BZ：" name="bz">
                <a-input v-model:value="formData.bz" placeholder="请输入BZ" allow-clear />
            </a-form-item>
            <a-form-item label="CGLX：" name="cglx">
                <a-input v-model:value="formData.cglx" placeholder="请输入CGLX" allow-clear />
            </a-form-item>
        </a-form>
        <template #footer>
            <a-button style="margin-right: 8px" @click="onClose">关闭</a-button>
            <a-button type="primary" @click="onSubmit" :loading="submitLoading">保存</a-button>
        </template>
    </a-drawer>
</template>

<script setup name="cgJhDhdForm">
    import { cloneDeep } from 'lodash-es'
    import { required } from '@/utils/formRules'
    import cgJhDhdApi from '@/api/biz/cgJhDhdApi'
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
            cgJhDhdApi
                .cgJhDhdSubmitForm(formDataParam, !formDataParam.id)
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

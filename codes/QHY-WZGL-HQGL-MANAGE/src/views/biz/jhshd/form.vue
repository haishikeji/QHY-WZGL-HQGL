<template>
    <a-drawer
        :title="formData.id ? '编辑cg_jh_shd' : '增加cg_jh_shd'"
        :width="600"
        :visible="visible"
        :destroy-on-close="true"
        :footer-style="{ textAlign: 'right' }"
        @close="onClose"
    >
        <a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
            <a-form-item label="单据编号(4位年+2位月+2位日+4位部门号+4位流水号)：" name="shdh">
                <a-input v-model:value="formData.shdh" placeholder="请输入单据编号(4位年+2位月+2位日+4位部门号+4位流水号)" allow-clear />
            </a-form-item>
            <a-form-item label="审核人：" name="shry">
                <a-input v-model:value="formData.shry" placeholder="请输入审核人" allow-clear />
            </a-form-item>
            <a-form-item label="审核日期：" name="shrq">
                <a-date-picker v-model:value="formData.shrq" value-format="YYYY-MM-DD HH:mm:ss" show-time placeholder="请选择审核日期" style="width: 100%" />
            </a-form-item>
        </a-form>
        <template #footer>
            <a-button style="margin-right: 8px" @click="onClose">关闭</a-button>
            <a-button type="primary" @click="onSubmit" :loading="submitLoading">保存</a-button>
        </template>
    </a-drawer>
</template>

<script setup name="cgJhShdForm">
    import { cloneDeep } from 'lodash-es'
    import { required } from '@/utils/formRules'
    import cgJhShdApi from '@/api/biz/cgJhShdApi'
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
            cgJhShdApi
                .cgJhShdSubmitForm(formDataParam, !formDataParam.id)
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

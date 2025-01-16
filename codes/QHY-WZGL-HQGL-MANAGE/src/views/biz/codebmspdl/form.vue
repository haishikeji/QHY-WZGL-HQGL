<template>
    <a-drawer
        :title="formData.id ? '编辑cg_code_bmspdl' : '增加cg_code_bmspdl'"
        :width="600"
        :visible="visible"
        :destroy-on-close="true"
        :footer-style="{ textAlign: 'right' }"
        @close="onClose"
    >
        <a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
            <a-form-item label="部门代码" name="bmdm">
                <a-input v-model:value="formData.bmdm" placeholder="请输入部门代码" allow-clear />
            </a-form-item>
            <a-form-item label="部门名称：" name="bmmc">
                <a-input v-model:value="formData.bmmc" placeholder="请输入部门名称" allow-clear />
            </a-form-item>
            <a-form-item label="商品大类代码：" name="dldm">
                <a-input v-model:value="formData.dldm" placeholder="请输入商品大类代码" allow-clear />
            </a-form-item>
            <a-form-item label="商品大类名称：" name="dlmc">
                <a-input v-model:value="formData.dlmc" placeholder="请输入商品大类名称" allow-clear />
            </a-form-item>
        </a-form>
        <template #footer>
            <a-button style="margin-right: 8px" @click="onClose">关闭</a-button>
            <a-button type="primary" @click="onSubmit" :loading="submitLoading">保存</a-button>
        </template>
    </a-drawer>
</template>

<script setup name="cgCodeBmspdlForm">
    import { cloneDeep } from 'lodash-es'
    import { required } from '@/utils/formRules'
    import cgCodeBmspdlApi from '@/api/biz/cgCodeBmspdlApi'
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
            cgCodeBmspdlApi
                .cgCodeBmspdlSubmitForm(formDataParam, !formDataParam.id)
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

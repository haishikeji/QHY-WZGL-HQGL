<template>
    <a-drawer
        :title="formData.id ? '编辑spjg' : '增加spjg'"
        :width="600"
        :visible="visible"
        :destroy-on-close="true"
        :footer-style="{ textAlign: 'right' }"
        @close="onClose"
    >
        <a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
            <a-form-item label="商品名称：" name="spmc">
                <a-input v-model:value="formData.spmc" placeholder="请输入商品名称" allow-clear />
            </a-form-item>
            <a-form-item label="价格：" name="jg">
                <a-input v-model:value="formData.jg" placeholder="请输入价格" allow-clear />
            </a-form-item>
            <a-form-item label="数据来源：" name="sply">
                <a-input v-model:value="formData.sply" placeholder="请输入数据来源" allow-clear />
            </a-form-item>
            <a-form-item label="抓取时间：" name="zqsj">
                <a-date-picker v-model:value="formData.zqsj" value-format="YYYY-MM-DD HH:mm:ss" show-time placeholder="请选择抓取时间" style="width: 100%" />
            </a-form-item>
            <a-form-item label="抓取批次：" name="zqpc">
                <a-input v-model:value="formData.zqpc" placeholder="请输入抓取批次" allow-clear />
            </a-form-item>
        </a-form>
        <template #footer>
            <a-button style="margin-right: 8px" @click="onClose">关闭</a-button>
            <a-button type="primary" @click="onSubmit" :loading="submitLoading">保存</a-button>
        </template>
    </a-drawer>
</template>

<script setup name="spjgForm">
    import { cloneDeep } from 'lodash-es'
    import { required } from '@/utils/formRules'
    import spjgApi from '@/api/biz/spjgApi'
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
            spjgApi
                .spjgSubmitForm(formDataParam, !formDataParam.id)
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

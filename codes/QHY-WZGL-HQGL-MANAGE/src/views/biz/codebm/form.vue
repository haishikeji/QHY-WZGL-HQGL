<template>
    <a-drawer
        :title="formData.id ? '编辑部门名称' : '增加部门名称'"
        :width="600"
        :visible="visible"
        :destroy-on-close="true"
        :footer-style="{ textAlign: 'right' }"
        @close="onClose"
    >
        <a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
            <a-form-item label="一级部门代码：" name="yjbmdm">
                <a-input v-model:value="formData.yjbmdm" placeholder="请输入一级部门代码" allow-clear />
            </a-form-item>
            <a-form-item label="一级部门名称：" name="yjbmmc">
                <a-input v-model:value="formData.yjbmmc" placeholder="请输入一级部门名称" allow-clear />
            </a-form-item>
            <a-form-item label="部门代码：" name="bmdm">
                <a-input v-model:value="formData.bmdm" placeholder="请输入部门代码" allow-clear />
            </a-form-item>
            <a-form-item label="部门名称：" name="bmmc">
                <a-input v-model:value="formData.bmmc" placeholder="请输入部门名称" allow-clear />
            </a-form-item>
            <a-form-item label="部门显示顺序：" name="bmxh">
                <a-input v-model:value="formData.bmxh" placeholder="请输入部门显示顺序" allow-clear />
            </a-form-item>
            <a-form-item label="拼音简码：" name="pyjm">
                <a-input v-model:value="formData.pyjm" placeholder="请输入拼音简码" allow-clear />
            </a-form-item>
            <a-form-item label="启用标志：" name="qybz">
                <a-input v-model:value="formData.qybz" placeholder="请输入启用标志" allow-clear />
            </a-form-item>
            <a-form-item label="BZ：" name="bz">
                <a-input v-model:value="formData.bz" placeholder="请输入BZ" allow-clear />
            </a-form-item>
        </a-form>
        <template #footer>
            <a-button style="margin-right: 8px" @click="onClose">关闭</a-button>
            <a-button type="primary" @click="onSubmit" :loading="submitLoading">保存</a-button>
        </template>
    </a-drawer>

</template>

<script setup name="cgCodeBmForm">
    import { cloneDeep } from 'lodash-es'
    import { required } from '@/utils/formRules'
    import cgCodeBmApi from '@/api/biz/cgCodeBmApi'
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
            cgCodeBmApi
                .cgCodeBmSubmitForm(formDataParam, !formDataParam.id)
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

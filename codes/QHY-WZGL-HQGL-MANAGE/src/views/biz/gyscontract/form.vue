<template>
    <a-drawer
        :title="formData.id ? '编辑供应商合同' : '增加供应商合同'"
        :width="600"
        :visible="visible"
        :destroy-on-close="true"
        :footer-style="{ textAlign: 'right' }"
        @close="onClose"
    >
        <a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
            <a-form-item label="供应商代码：" name="gysdm">
                <a-select v-model:value="formData.gysdm" placeholder="请选择供应商代码" :options="gysdmOptions" />
            </a-form-item>
            <a-form-item label="合同名称：" name="contractName">
                <a-input v-model:value="formData.contractName" placeholder="请输入合同名称" allow-clear />
            </a-form-item>
            <a-form-item label="合同有效期：" name="contractExpired">
                <a-date-picker v-model:value="formData.contractExpired" value-format="YYYY-MM-DD HH:mm:ss" show-time placeholder="请选择合同有效期" style="width: 100%" />
            </a-form-item>
            <a-form-item label="合同范围：" name="contractRange">
                <a-input v-model:value="formData.contractRange" placeholder="请输入合同范围" allow-clear />
            </a-form-item>
            <a-form-item label="是否禁用：" name="isDisable">
                <a-select v-model:value="formData.isDisable" placeholder="请选择是否禁用" :options="isDisableOptions" />
            </a-form-item>
            <a-form-item label="合同文件：" name="filePath">
                <a-input v-model:value="formData.filePath" placeholder="请输入合同文件" allow-clear />
            </a-form-item>
            <a-form-item label="合同状态：" name="status">
                <a-select v-model:value="formData.status" placeholder="请选择合同状态" :options="statusOptions" />
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

<script setup name="cgGysContractForm">
    import tool from '@/utils/tool'
    import { cloneDeep } from 'lodash-es'
    import { required } from '@/utils/formRules'
    import cgGysContractApi from '@/api/biz/cgGysContractApi'
    // 抽屉状态
    const visible = ref(false)
    const emit = defineEmits({ successful: null })
    const formRef = ref()
    // 表单数据
    const formData = ref({})
    const submitLoading = ref(false)
    const gysdmOptions = ref([])
    const isDisableOptions = ref([])
    const statusOptions = ref([])

    // 打开抽屉
    const onOpen = (record) => {
        visible.value = true
        if (record) {
            let recordData = cloneDeep(record)
            formData.value = Object.assign({}, recordData)
        }
        gysdmOptions.value = tool.dictList('COMMON_SWITCH')
        isDisableOptions.value = tool.dictList('启用标志')
        statusOptions.value = tool.dictList('COMMON_STATUS')
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
            cgGysContractApi
                .cgGysContractSubmitForm(formDataParam, !formDataParam.id)
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

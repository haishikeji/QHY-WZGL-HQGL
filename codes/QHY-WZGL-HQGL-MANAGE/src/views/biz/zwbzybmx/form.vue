<template>
    <a-drawer
        :title="formData.id ? '编辑cg_zw_bzybmx' : '增加cg_zw_bzybmx'"
        :width="600"
        :visible="visible"
        :destroy-on-close="true"
        :footer-style="{ textAlign: 'right' }"
        @close="onClose"
    >
        <a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
            <a-form-item label="YBBH：" name="ybbh">
                <a-input v-model:value="formData.ybbh" placeholder="请输入YBBH" allow-clear />
            </a-form-item>
            <a-form-item label="RQ：" name="rq">
                <a-date-picker v-model:value="formData.rq" value-format="YYYY-MM-DD HH:mm:ss" show-time placeholder="请选择RQ" style="width: 100%" />
            </a-form-item>
            <a-form-item label="CZY：" name="czy">
                <a-input v-model:value="formData.czy" placeholder="请输入CZY" allow-clear />
            </a-form-item>
            <a-form-item label="YJBMDM：" name="yjbmdm">
                <a-input v-model:value="formData.yjbmdm" placeholder="请输入YJBMDM" allow-clear />
            </a-form-item>
            <a-form-item label="YJBMMC：" name="yjbmmc">
                <a-input v-model:value="formData.yjbmmc" placeholder="请输入YJBMMC" allow-clear />
            </a-form-item>
            <a-form-item label="BMDM：" name="bmdm">
                <a-input v-model:value="formData.bmdm" placeholder="请输入BMDM" allow-clear />
            </a-form-item>
            <a-form-item label="BMMC：" name="bmmc">
                <a-input v-model:value="formData.bmmc" placeholder="请输入BMMC" allow-clear />
            </a-form-item>
            <a-form-item label="BZDM：" name="bzdm">
                <a-input v-model:value="formData.bzdm" placeholder="请输入BZDM" allow-clear />
            </a-form-item>
            <a-form-item label="BZMC：" name="bzmc">
                <a-input v-model:value="formData.bzmc" placeholder="请输入BZMC" allow-clear />
            </a-form-item>
            <a-form-item label="TJLB：" name="tjlb">
                <a-input v-model:value="formData.tjlb" placeholder="请输入TJLB" allow-clear />
            </a-form-item>
            <a-form-item label="LBDM：" name="lbdm">
                <a-input v-model:value="formData.lbdm" placeholder="请输入LBDM" allow-clear />
            </a-form-item>
            <a-form-item label="LBMC：" name="lbmc">
                <a-input v-model:value="formData.lbmc" placeholder="请输入LBMC" allow-clear />
            </a-form-item>
            <a-form-item label="LBLX：" name="lblx">
                <a-input v-model:value="formData.lblx" placeholder="请输入LBLX" allow-clear />
            </a-form-item>
            <a-form-item label="LBXH：" name="lbxh">
                <a-input v-model:value="formData.lbxh" placeholder="请输入LBXH" allow-clear />
            </a-form-item>
            <a-form-item label="OUTJE：" name="outje">
                <a-input v-model:value="formData.outje" placeholder="请输入OUTJE" allow-clear />
            </a-form-item>
            <a-form-item label="INJE：" name="inje">
                <a-input v-model:value="formData.inje" placeholder="请输入INJE" allow-clear />
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

<script setup name="cgZwBzybmxForm">
    import { cloneDeep } from 'lodash-es'
    import { required } from '@/utils/formRules'
    import cgZwBzybmxApi from '@/api/biz/cgZwBzybmxApi'
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
            cgZwBzybmxApi
                .cgZwBzybmxSubmitForm(formDataParam, !formDataParam.id)
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

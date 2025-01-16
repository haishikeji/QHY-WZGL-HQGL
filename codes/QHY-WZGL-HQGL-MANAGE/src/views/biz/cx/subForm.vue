<template>
    <a-modal
        :title="formData.id ? '提交' : '提交'"
        :width="600"
        :visible="visible"
        :destroy-on-close="true"
        :footer-style="{ textAlign: 'right' }"
		@cancel="onClose"
		:maskClosable="false"
    >
        <a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
			<a-row :gutter="16">
                <a-col :span="24">
                    <a-form-item label="需货日期：" name="xhrq">
                        <a-date-picker v-model:value="formData.xhrq" value-format="YYYY-MM-DD HH:mm:ss" show-time placeholder="请选择需货日期" style="width: 100%" />
                    </a-form-item>
                </a-col>
			</a-row>
			<a-row :gutter="16">
                <a-col :span="24">
                    <a-form-item label="需货备注：" name="bz">
                        <a-textarea v-model:value="formData.bz" placeholder="请输入备注" allow-clear />
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
	import dayjs from "dayjs";
	import cgJhSqdApi from "@/api/biz/cgJhSqdApi";
	import bizOrgApi from "@/api/biz/bizOrgApi";
    // 抽屉状态
    const visible = ref(false)
    const emit = defineEmits({ successful: null })
    const formRef = ref()
    // 表单数据
    const formData = ref({})
    const submitLoading = ref(false)
	const treeData = ref([])

    // 打开抽屉
    const onOpen = (record) => {
        visible.value = true
		if (Array.isArray(record)) {
			let recordData = cloneDeep(record);
			formData.value.xhrq = dayjs().hour(0).minute(0).second(0).add(1, "day").add(6, "hour").add(30, "minute").format("YYYY-MM-DD HH:mm:ss")
			formData.value.cgJhSqdEditParamList = record;
		} else {
			if (record) {
				let recordData = cloneDeep(record);
				formData.value = Object.assign({}, recordData);
				formData.value.xhrq = dayjs(new Date().getTime() + 24 * 60 * 60 * 1000).format("YYYY-MM-DD HH:mm:ss")

			}
		}
    }
	const initOrg = () => {
		bizOrgApi.orgTree().then((res) => {
			treeData.value = res
		})
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
                .cgJhSqdSubmitForm(formDataParam, false)
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

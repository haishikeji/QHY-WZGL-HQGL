<template>
    <a-modal
        :title="formData.id ? '编辑' : '增加'"
        :width="600"
        :visible="visible"
        :destroy-on-close="true"
        :footer-style="{ textAlign: 'right' }"
        @close="onClose"
		@cancel="onClose"
    >
        <a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
            <a-row :gutter="16">
                <a-col :span="12">
                    <a-form-item label="材料名称：" name="fileName">
                        <a-input v-model:value="formData.fileName" placeholder="请输入文件名" allow-clear />
                    </a-form-item>
                </a-col>
				<a-col :span="12">
					<a-form-item label="材料有效期：" name="fileExpired">
						<a-date-picker v-model:value="formData.fileExpired" value-format="YYYY-MM-DD HH:mm:ss" show-time style="width: 100%" />
					</a-form-item>
				</a-col>
                <a-col :span="24">
                    <a-form-item label="材料图片：" name="filePath">
						<a-upload
							v-model:file-list="formData.filePath"
							:action="action"
							:headers="headers"
							list-type="picture-card"
							@preview="handlePreview"
						>
							<div v-if="formData.filePath?formData.filePath.length:[] < 8">
								<plus-outlined />
								<div style="margin-top: 8px">上传</div>
							</div>
						</a-upload>
<!--                        <a-input v-model:value="formData.filePath" placeholder="请输入文件路径" allow-clear />-->
                    </a-form-item>
					<a-modal :visible="previewVisible" :title="previewTitle" :footer="null" @cancel="handleCancel">
						<img alt="example" style="width: 100%" :src="previewImage" />
					</a-modal>
                </a-col>
                <a-col :span="24">
                    <a-form-item label="备注：" name="bz">
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

<script setup name="cgGysPrcForm">
    import { cloneDeep } from 'lodash-es'
    import { required } from '@/utils/formRules'
    import cgGysPrcApi from '@/api/biz/cgGysPrcApi'
	import sysConfig from "@/config";
	import tool from "@/utils/tool";
	import gysApi from "@/api/biz/gysApi";
    // 抽屉状态
    const visible = ref(false)
    const emit = defineEmits({ successful: null })
    const formRef = ref()
    // 表单数据
    const formData = ref({})
    const submitLoading = ref(false)
	const uploadRef = ref()
	const max = ref(5)
	const action = ref(sysConfig.API_URL + '/dev/file/uploadDynamicReturnUrl')
	const headers = ref({
		token: tool.data.get('TOKEN')
	})
	const previewVisible = ref(false);
	const previewImage = ref('');
	const previewTitle = ref('');
	// 回调
	const uploadImgDone = (value) => {
		// 返回的文件列表数据
		console.log( value)
	}
    // 打开抽屉
    const onOpen = (record) => {
        visible.value = true
        if (record) {
            let recordData = cloneDeep(record)
			if(recordData.filePath){
				recordData.filePath = JSON.parse(recordData.filePath)
			}

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
			formDataParam.filePath=JSON.stringify(formDataParam.filePath)
            gysApi
                .cgGysPrcSubmitForm(formDataParam, !formDataParam.id)
                .then(() => {
                    onClose()
                    emit('successful')
                })
                .finally(() => {
                    submitLoading.value = false
                })
        })
    }
	function getBase64(file) {
		return new Promise((resolve, reject) => {
			const reader = new FileReader();
			reader.readAsDataURL(file);
			reader.onload = () => resolve(reader.result);
			reader.onerror = error => reject(error);
		});
	}
	const handlePreview = async file => {
		if (!file.url && !file.preview) {
			file.preview = await getBase64(file.originFileObj);
		}
		previewImage.value = file.url || file.preview;
		previewVisible.value = true;
		previewTitle.value = file.name || file.url.substring(file.url.lastIndexOf('/') + 1);
	}
	const handleCancel = () => {
		previewVisible.value = false
		previewTitle.value = ''
	}
    // 抛出函数
    defineExpose({
        onOpen
    })
</script>

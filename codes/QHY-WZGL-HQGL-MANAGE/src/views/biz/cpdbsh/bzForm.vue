<template>
	<a-modal
		:title="'班组收货'"
		:width="600"
		:visible="visible"
		:destroy-on-close="true"
		:footer-style="{ textAlign: 'right' }"
		@cancel="onClose"
		:maskClosable="false"
	>
		<a-form ref="formRef" :model="formData" :rules="formRules" >
			<a-row :gutter="24">
				<a-space>
					<a-form-item label="商品名称：" name="spmc">
						<span>{{ formData.spmc }}</span>
					</a-form-item>

				</a-space>
			</a-row>
			<a-row :gutter="24">
				<a-col :span="12" v-for="(bzdm,index) in formData.spckmxList">
					<a-form-item :label="bzdm.bzName+'：'" name="bzmc" style="margin-bottom: 8px;">
						<a-input-number v-model:value="bzdm.cksl" placeholder="请输入数量" allow-clear :min="0"
										style="width: 100%" @pressEnter="onSave" :loading="submitLoading"	/>
					</a-form-item>
				</a-col>

			</a-row>
		</a-form>


		<template #footer>
			<a-button style="margin-right: 8px" @click="onClose">关闭</a-button>
			<a-button type="primary" @click="onSave" :loading="submitLoading" >保存</a-button>
		</template>
	</a-modal>
</template>

<script setup name="cgKcKczbForm">
import { cloneDeep } from "lodash-es";
import { required } from "@/utils/formRules";
import cgKcKczbApi from "@/api/biz/cgKcKczbApi";
import fileApi from "@/api/dev/fileApi";
import bizSplbTreeApi from "@/api/biz/bizSplbTreeApi";
// 抽屉状态
const visible = ref(false);
const emit = defineEmits({ successful: null });
const formRef = ref();
// 表单数据
const formData = ref({});
const submitLoading = ref(false);
const activeKey = ref("Local");
const uploadLoading = ref(false);
const treeData = ref([]);
const bzInfo = ref([]);
const extJson = ref([]);
const previewVisible = ref(false);
const previewImage = ref("");
const fileList = ref([]);
const recordOld = ref()
// 打开抽屉
const onOpen = (record) => {

	visible.value = true;
	extJson.value = ref([]);
	if (record) {
		let recordData = cloneDeep(record);
		formData.value = Object.assign({}, recordData);
	}
	bzInfo.value=record.spckmxList
	recordOld.value=record

};
// 关闭抽屉
const onClose = () => {
	formRef.value.resetFields();
	formData.value = {};
	visible.value = false;
};

// 保存抽屉
const onSave = () => {
	cgJhSpmxApi.acceptBatchCgJhSpckmxNoSub(formData.value.spckmxList).then(() => {

	});
	formData.value.shsl=0;
	(formData.value.spckmxList).forEach(spckmx=>{
		formData.value.shsl=NP.plus(formData.value.shsl,spckmx.cksl)
	})
	visible.value = false;
	recordOld.value.shsl=formData.value.shsl
	recordOld.value.spckmxList=formData.value.spckmxList
	emit('successful')
};
import { PlusOutlined } from "@ant-design/icons-vue";
import { defineComponent, ref } from "vue";
import bizBzTreeApi from "@/api/biz/bizBzTreeApi";
import { message } from "ant-design-vue";
import cgJhSpmxApi from "@/api/biz/cgJhSpmxApi";
import NP from "number-precision";

function getBase64(file) {
	return new Promise((resolve, reject) => {
		const reader = new FileReader();
		reader.readAsDataURL(file);
		reader.onload = () => resolve(reader.result);
		reader.onerror = error => reject(error);
	});
}

const handleCancel = () => {
	previewVisible.value = false;
};
const handlePreview = async file => {
	if (!file.url && !file.preview) {
		file.preview = await getBase64(file.originFileObj);
	}
	previewImage.value = file.url || file.preview;
	previewVisible.value = true;
};
const handleChange = ({ fileList: newFileList }) => {
	fileList.value = newFileList;
};


// 上传本地文件
const customRequestLocal = (data) => {
	uploadLoading.value = true;
	const fileData = new FormData();
	fileData.append("file", data.file);
	fileApi
		.fileUploadLocalReturnUrl(fileData)
		.then(() => {
			emit("successful");
		})
		.finally(() => {
			uploadLoading.value = false;
		});
};
// 默认要校验的
const formRules = {};
// 验证并提交数据
const onSubmit = () => {
	formRef.value.validate().then(() => {
		submitLoading.value = true;
		let sjkc = formData.value.sjkc;
		for (const i in bzInfo.value) {
			const bz = bzInfo.value[i];
			if (bz.sqsl) {
				sjkc = sjkc - bz.sqsl;
			}
			if (sjkc < 0) {
				message.error("您填入的数量超过可申请数，请重新填写！");
				submitLoading.value = false;
				return;
			}

		}

		formData.value.spmxList = bzInfo.value.filter(bz => {
			if (bz.sqsl && bz.sqsl > 0) {
				return bz;
			}
		}).map(bz => {
			return { bzdm: bz.id, sqsl: bz.sqsl };
		});
		const formDataParam = cloneDeep(formData.value);
		cgJhSpmxApi
			.cgJhSpckmxApplyForm(formDataParam, !formDataParam.id)
			.then(() => {
				onClose();
				emit("successful");
			})
			.finally(() => {
				submitLoading.value = false;
			});
	});
};

// 抛出函数
defineExpose({
	onOpen
});
</script>
<style>
.ant-upload-select-picture-card i {
	font-size: 32px;
	color: #999;
}

.ant-upload-select-picture-card .ant-upload-text {
	margin-top: 8px;
	color: #666;
}
</style>

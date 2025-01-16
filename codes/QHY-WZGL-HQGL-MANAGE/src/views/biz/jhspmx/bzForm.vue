<template>
	<a-modal
		:title="'班组提交信息'"
		:width="600"
		:visible="visible"
		:destroy-on-close="true"
		:footer-style="{ textAlign: 'right' }"
		@cancel="onClose"
		:maskClosable="false"
	>
		<template #footer>
			{{null}}
		</template>
		<a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
			<a-row :gutter="24">
				<a-col :span="12">
					<a-form-item label="已提交班组：" name="xhrq">
						<a-list size="small" bordered :data-source="ytj">
							<template #renderItem="{ item }">
								<a-list-item>{{ item.name }}</a-list-item>
							</template>
						</a-list>
					</a-form-item>
				</a-col>

				<a-col :span="12">
					<a-form-item label="未提交班组：" name="bz">
						<a-list size="small" bordered :data-source="wtj">
							<template #renderItem="{ item }">
								<a-list-item>{{ item.name }}</a-list-item>
							</template>
						</a-list>
					</a-form-item>
				</a-col>

			</a-row>
		</a-form>

	</a-modal>
</template>

<script setup name="cgJhSpmxForm">
import { cloneDeep } from "lodash-es";
import { required } from "@/utils/formRules";
import cgJhSpmxApi from "@/api/biz/cgJhSpmxApi";
import addBatch from "@/api/biz/cgJhSqdApi";
import dayjs from "dayjs";
import cgJhSqdApi from "@/api/biz/cgJhSqdApi";
// 抽屉状态
const visible = ref(false);
const emit = defineEmits({ successful: null });
const formRef = ref();
// 表单数据
const formData = ref({});
const submitLoading = ref(false);
const ytj = ref([]);
const wtj = ref([]);
// 打开抽屉
const onOpen = (record) => {
	visible.value = true;
	cgJhSpmxApi.getYtjBz(record).then((res)=>{
		ytj.value=res
	})
	cgJhSpmxApi.getWtjBz(record).then((res)=>{
		wtj.value=res
	})
};
// 关闭抽屉
const onClose = () => {
	formRef.value.resetFields();
	formData.value = {};
	visible.value = false;

};
// 默认要校验的
const formRules = {};
// 验证并提交数据
const onSubmit = () => {
	formRef.value.validate().then(() => {
		submitLoading.value = true;
		const formDataParam = cloneDeep(formData.value);
		cgJhSqdApi
			.cgJhSqdSubmitForm(formDataParam, false)
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

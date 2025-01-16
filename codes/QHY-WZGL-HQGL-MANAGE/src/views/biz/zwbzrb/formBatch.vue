<template>
	<a-modal
		:title="'批量登记'"
		:width="400"
		:visible="visible"
		:destroy-on-close="true"
		:footer-style="{ textAlign: 'right' }"
		@cancel="onClose"
		:maskClosable="false"
	>

		<a-form ref="formRef" :model="formData" :rules="formRules" style="width: 100%;">
			<a-row :gutter="24" v-for="(cbdm, index) in cbdmList" :key="index" class="form-row"
				   style="padding-bottom: 5px">
				<a-col :span="8" style="text-align: right">
					{{ `${cbdm.dictLabel}：` }}
				</a-col>
				<a-col :span="8">
					<a-input-group compact>
						<a-input v-model:value="cbdm.inje" style="width: 100%" readonly="readonly"/>
					</a-input-group>
				</a-col>
				<a-col :span="8">
					<a-button type="primary" ghost @click="formRef.onOpen(cbdm, formData)">登记与修改</a-button>
				</a-col>
			</a-row>
		</a-form>

<!--		<a-card>-->
<!--			<template #bodyCell="{ column, record }">-->
<!--				<template v-if="column.dataIndex === 'action'">-->
<!--					<a-space>-->
<!--						<a @click="formRef.onOpen(record)">登记</a>-->
<!--					</a-space>-->
<!--				</template>-->
<!--			</template>-->
<!--		</a-card>-->

		<Form ref="formRef" @successful="onOpen(record)" />

		<template #footer>
			<a-button style="margin-right: 8px" @click="onClose">关闭</a-button>
		</template>
	</a-modal>
</template>

<script setup name="cgZwBzrbForm">
import dictApi from "@/api/dev/dictApi";
import { defineComponent, ref } from "vue";
import { cloneDeep } from "lodash-es";
import cgZwBzrbApi from "@/api/biz/cgZwBzrbApi";
import Form from "./dj_formBatch.vue";
// 抽屉状态
const visible = ref(false);
const emit = defineEmits({ successful: null });
const formRef = ref();
// 表单数据
const formData = ref({});
const submitLoading = ref(false);
const cbdmList = ref([]);

// 打开抽屉
const onOpen = (record) => {
	visible.value = true;
	if (record) {
		let recordData = cloneDeep(record);
		formData.value = Object.assign({}, recordData);

	}
	init();
};

const init = () => {
	dictApi.dictList({ extJson: "cbdm" }).then(res => {
		cbdmList.value = res;
		cbdmList.value.forEach(cbdm => {
			let param = {
				dictValue: cbdm.dictValue,
				bzdm: formData.value.bzdm,
				bmdm: formData.value.bmdm,
				rbbh: formData.value.rbbh
			};
			cgZwBzrbApi.queryJe(param).then(res => {
				cbdm.inje = res[cbdm.dictValue];
			});
		});
	});
};

// 关闭抽屉
const onClose = () => {
	//formRef.value.resetFields();
	formData.value = {};
	visible.value = false;
	emit("successful");

};
// 默认要校验的
const formRules = {};
// 验证并提交数据
const onSubmit = () => {
	formRef.value.validate().then(() => {
		submitLoading.value = true;
		const formDataParam = cloneDeep(formData.value);
		cgZwBzrbApi
			.cgZwBzrbSubmitForm(formDataParam, !formDataParam.id)
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
.form-row {
	display: flex;
	align-items: center;
	justify-content: center;
}

</style>



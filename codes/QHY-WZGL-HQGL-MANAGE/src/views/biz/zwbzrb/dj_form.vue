<template>
	<a-modal
		:title="'手动登记'"
		:width="400"
		:visible="visible"
		:destroy-on-close="true"
		:footer-style="{ textAlign: 'right' }"
		@cancel="onClose"
		:maskClosable="false"
	>

		<a-form ref="formRef"
				:model="formData"
				:rules="formRules"
				style="width: 100%;"
		>

			<a-row :gutter="24" :model="formData" v-for="(cbdm, index) in cbdmList" :key="index" class="form-row"
				   style="padding-bottom: 5px">
				<a-col :span="8" style="text-align: right">
					{{ `${cbdm.dictLabel}：` }}
				</a-col>
				<a-col :span="8">
					<a-input-group compact>
						<a-input-number v-model:value="cbdm.inje" style="width: 100%" :min="0"/>
					</a-input-group>
				</a-col>

			</a-row>
		</a-form>


		<template #footer>
			<a-button style="margin-right: 8px" @click="onClose">关闭</a-button>
			<a-button type="primary" @click="onSubmit" :loading="submitLoading">提交</a-button>
		</template>
	</a-modal>
</template>

<script setup name="cgZwBzrbForm">
import dictApi from "@/api/dev/dictApi";
import { defineComponent, ref } from "vue";
import { cloneDeep } from "lodash-es";
import cgZwBzrbApi from "@/api/biz/cgZwBzrbApi";
import Form from "./dj_form.vue";
import cgZwBzrbmxApi from "@/api/biz/cgZwBzrbmxApi";
import tool from "@/utils/tool";
// 抽屉状态
const visible = ref(false);
const emit = defineEmits({ successful: null });
const formRef = ref();
// 表单数据
const formData = ref({});
const submitLoading = ref(false);
const cbdmList = ref([]);
const rbData = ref({});
const cbdmValue = ref({});

// 打开抽屉
const onOpen = (record, recordformData) => {
	cbdmValue.value = record;
	visible.value = true;
	if (recordformData) {
		rbData.value = recordformData;
	}
	dictApi.dictList({ parentId: record.id }).then(res => {
		cbdmList.value = res;
		cbdmList.value.forEach(cbdm => {
			let param = {
				dictValue: cbdm.dictValue,
				bzdm: rbData.value.bzdm,
				bmdm: rbData.value.bmdm,
				rbbh: rbData.value.rbbh
			};
			cgZwBzrbmxApi.queryInJe(param).then(res => {
				cbdm.inje = res.inje;
			});
		});
	});
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
	let dldm = cbdmValue.value.dictValue;
	let bzdm = rbData.value.bzdm;
	let bmdm = rbData.value.bmdm;
	let rbbh = rbData.value.rbbh;
	let lblx = cbdmValue.value.dictLabel;
	console.log(rbbh);
	let cgZwBzrbmxAddParamList = cbdmList.value.map((item) => {
			item.lbdm = item.dictValue;
			item.lbmc = item.dictLabel;
			item.bzdm = rbData.value.bzdm;
			item.bmmc = rbData.value.bmName;
			item.bzmc = rbData.value.bzName;
			item.rbbh = rbData.value.rbbh;
			item.bmdm = rbData.value.bmdm;
			item.lblx = lblx;
			return item;
		}
	);
	formData.value.dldm = dldm;
	formData.value.bzdm = bzdm;
	formData.value.bmdm = bmdm;
	formData.value.rbbh = rbbh;
	formData.value.lblx = lblx;
	formData.value.cgZwBzrbmxAddParamList = cgZwBzrbmxAddParamList;
	submitLoading.value = true;
	const formDataParam = cloneDeep(formData.value);
	cgZwBzrbmxApi
		.cgZwBzrbmxSubmitForm(formDataParam, !formDataParam.id)
		.then(() => {
			onClose();
			emit("successful", rbData.value);
		})
		.finally(() => {
			submitLoading.value = false;
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



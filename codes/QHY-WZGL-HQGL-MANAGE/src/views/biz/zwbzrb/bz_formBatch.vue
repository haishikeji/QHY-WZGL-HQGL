<template>
	<a-modal
		:title="'班组批量登记'"
		:width="400"
		:visible="visible"
		:destroy-on-close="true"
		:footer-style="{ textAlign: 'right' }"
		@cancel="onClose"
		:maskClosable="false"
	>
		<a-form ref="formRef" :model="formData" :rules="formRules" style="width: 100%;">
			<a-row :gutter="24" v-for="(cbdm, index) in bzInfo" :key="index" class="form-row" style="padding-bottom: 5px">
				<a-col :span="8" style="text-align: right">
					{{ `${cbdm.name}：` }}
				</a-col>
				<a-col :span="8">
					<a-input-group compact>
						<a-input-number v-model:value="cbdm.inje" style="width: 100%" :min="0" />
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
import { ref } from 'vue';
import { cloneDeep } from 'lodash-es';
import bizBzTreeApi from "@/api/biz/bizBzTreeApi";
import cgZwBzrbmxApi from "@/api/biz/cgZwBzrbmxApi";

const visible = ref(false);
const emit = defineEmits(['successful']);
const formRef = ref();
const formData = ref({});
const submitLoading = ref(false);
const bzInfo = ref([]);
const cbdmValue = ref({});

const onOpen = (data) => {
	cbdmValue.value = data;
	visible.value = true;

	bizBzTreeApi.bizBzList({ id: data.bmdm }).then((res) => {
		bzInfo.value = res.map(item => ({ ...item, inje: 0 }));  // Initialize `inje` to 0 or any default value

		// Fetch `inje` for each item in `bzInfo`
		bzInfo.value.forEach((item, index) => {
			const param = {
				dictValue: data.lbdm,
				bzdm: item.id,
				bmdm: data.bmdm,
				rbbh: data.rbbh
			};

			cgZwBzrbmxApi.queryInJe(param).then(response => {
				bzInfo.value[index].inje = response.inje;
			});
		});
	});
};

const onClose = () => {
	formData.value = {};
	visible.value = false;
};

const formRules = {};

const onSubmit = () => {
	bzInfo.value.forEach(bz => {
		const dldm = cbdmValue.value.dldm;
		const bzdm = bz.id;
		const bmdm = cbdmValue.value.bmdm;
		const rbbh = cbdmValue.value.rbbh.split('-').join('');
		const lblx = cbdmValue.value.lblx;

		const cgZwBzrbmxAddParamList = [{
			lbdm: cbdmValue.value.lbdm,
			lbmc: cbdmValue.value.lbmc,
			bzdm: bz.id,
			bmmc: bz.orgName,
			bzmc: bz.name,
			rbbh: rbbh,
			bmdm: cbdmValue.value.bmdm,
			lblx: lblx,
			inje: bz.inje
		}];

		formData.value = {
			dldm: dldm,
			bzdm: bzdm,
			bmdm: bmdm,
			rbbh: rbbh,
			lblx: lblx,
			cgZwBzrbmxAddParamList: cgZwBzrbmxAddParamList
		};

		submitLoading.value = true;
		const formDataParam = cloneDeep(formData.value);

		cgZwBzrbmxApi.cgZwBzrbmxBatchSubmitForm(formDataParam, !formDataParam.id).then(() => {
			onClose();
			emit("successful", {});
		}).finally(() => {
			submitLoading.value = false;
		});
	});
};

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

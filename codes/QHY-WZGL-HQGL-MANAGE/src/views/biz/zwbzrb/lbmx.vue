<template>
	<a-modal
		v-model:visible="visible"
		title="类别明细"
		width="100%"
		:mask-closable="false"
		wrap-class-name="full-modal"
		:destroy-on-close="true"
	>
		<template #footer>
			{{ null }}
		</template>
		<a-card>
			<s-table
				ref="table"
				:columns="columns"
				:data="loadData"
				bordered
				:row-key="(record) => record.id"
				:scroll="{ x: 1000 }"
			>

				<template #bodyCell="{ column, record }">
					<template v-if="column.dataIndex === 'action'">
						<a-space>
							<a @click="formRef.onOpen(record, spflData, data)">明细</a>
						</a-space>
					</template>
				</template>

			</s-table>
		</a-card>
	</a-modal>
	<Form ref="formRef" @successful="table.refresh(true)" />
</template>

<script setup name="zwbzrbmx">
import cgZwBzrbmxApi from "@/api/biz/cgZwBzrbmxApi";
import cgZwBzrbApi from "@/api/biz/cgZwBzrbApi";
import { cloneDeep } from "lodash-es";
import Form from "./spmx.vue";

const visible = ref(false);
const formData = ref({});
const table = ref();
const formRef = ref();
const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false };
// 查询区域显示更多控制
const advanced = ref(false);
const toggleAdvanced = () => {
	advanced.value = !advanced.value;
};

const columns = [
	{
		title: "类别名称",
		dataIndex: "lbmc"
	},
	{
		title: "类别类型",
		dataIndex: "spfl"
	},
	{
		title: "金额",
		dataIndex: "je"
	}
];
const selectedRowKeys = ref([]);

columns.push({
	title: "操作",
	dataIndex: "action",
	align: "center",
	width: "150px"
});

// 列表选择配置
const options = {
	// columns数字类型字段加入 needTotal: true 可以勾选自动算账
	alert: {
		show: true,
		clear: () => {
			selectedRowKeys.value = ref([]);
		}
	},
	rowSelection: {
		onChange: (selectedRowKey, selectedRows) => {
			selectedRowKeys.value = selectedRowKey;
		}
	}
};

const data = ref({});

const spflData = ref();

// 打开抽屉
const onOpen = (record, spfl) => {
	visible.value = true;
	spflData.value = spfl;
	console.log(record)
	data.value = record;
	if (record) {
		let recordData = cloneDeep(record);
		formData.value = Object.assign({}, recordData);
	}
};

// 关闭抽屉
const onClose = () => {
	formData.value = {};
	visible.value = false;
};

const loadData = (parameter) => {
	parameter.bzdm = data.value.bzdm;
	parameter.bmdm = data.value.bmdm;
	parameter.bh = data.value.rbbh;
	parameter.spfl = spflData.value;
	return cgZwBzrbApi.rblbmxPage(parameter).then((data) => {
		return data;
	});
};
// 重置
const reset = () => {
	searchFormRef.value.resetFields();
	table.value.refresh(true);
};

// 抛出函数
defineExpose({
	onOpen
});
</script>
<style lang="less">
.full-modal {
	.ant-modal {
		max-width: 100%;
		top: 0;
		padding-bottom: 0;
		margin: 0;
	}

	.ant-modal-content {
		display: flex;
		flex-direction: column;
		height: calc(100vh);
	}

	.ant-modal-body {
		flex: 1;
	}
}

</style>

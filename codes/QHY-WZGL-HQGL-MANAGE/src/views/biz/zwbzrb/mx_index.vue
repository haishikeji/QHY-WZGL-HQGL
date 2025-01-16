<template>
	<a-modal
		v-model:visible="visible"
		title="明细"
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

			</s-table>
		</a-card>
	</a-modal>
</template>

<script setup name="zwbzrbmx">
import cgZwBzrbmxApi from "@/api/biz/cgZwBzrbmxApi";
import { cloneDeep } from "lodash-es";
const visible = ref(false)
const formData = ref({})
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
		dataIndex: "lblx"
	},
	{
		title: "生成日期",
		dataIndex: "rq"
	},
	{
		title: "金额",
		dataIndex: "inje"
	}
];
const selectedRowKeys = ref([]);

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

const data = ref({})

// 打开抽屉
const onOpen = (record) => {
	visible.value = true
	data.value = record
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

const loadData = (parameter) => {
	parameter.bzdm = data.value.bzdm
	parameter.bmdm = data.value.bmdm
	parameter.rbbh = data.value.rbbh
	console.log(parameter)
	return cgZwBzrbmxApi.cgZwBzrbmxPage(parameter).then((data) => {
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
})
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

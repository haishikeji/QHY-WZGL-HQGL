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
				:scroll="{ x: 1300 }"
			>

			</s-table>
		</a-card>

	</a-modal>
</template>

<script setup name="jhsqd">
import cgJhSqdApi from "@/api/biz/cgJhSqdApi";
import cgJhSpmxApi from "@/api/biz/cgJhSpmxApi";
import gyshzApi from "@/api/biz/gyshzApi";

let searchFormState = reactive({});
const searchFormRef = ref();
const table = ref();
const visible = ref(false);
const formRef = ref();
const cglx = ref(["班组订货", "部门备货"]);
const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false };
// 查询区域显示更多控制
const advanced = ref(false);
const toggleAdvanced = () => {
	advanced.value = !advanced.value;
};
const columns = [
	{
		title: "类别",
		dataIndex: "lbmc"
	},
	{
		title: "商品名称",
		dataIndex: "spmc"
	},
	{
		title: "商品规格",
		dataIndex: "spgg"
	},
	{
		title: "计量单位",
		dataIndex: "jldw"
	},
	{
		title: "进货单价",
		dataIndex: "jhdj"
	},
	{
		title: "供应单价",
		dataIndex: "gydj"
	},
	{
		title: "收货数量",
		dataIndex: "shsl"
	},
	{
		title: "进货金额",
		dataIndex: "jhje"
	},
	{
		title: "供应金额",
		dataIndex: "gyje"
	},
	{
		title: "采购类型",
		dataIndex: "cglx"
	},
	{
		title: "收货日期",
		dataIndex: "shrq"
	},
	{
		title: "保质日期",
		dataIndex: "bzrq"
	},
	{
		title: "状态",
		dataIndex: "workstate"
	},
	{
		title: "收货人",
		dataIndex: "shry"
	},
];

const selectedRowKeys = ref([]);

const onOpen = (record,searchParam) => {
	visible.value = true;
	searchFormState.gysdm = record.gysdm;
	searchFormState.bmdm = searchParam.bmdm;
	if (searchParam.shrq) {
		searchFormState.startShrq = searchParam.shrq[0]
		searchFormState.endShrq = searchParam.shrq[1]
	}
	// console.log(record)
};
// 关闭抽屉
const onClose = () => {
	formData.value = {};
	visible.value = false;
};
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
const loadData = (parameter) => {
	console.log(searchFormState.gysmc)
	const searchFormParam = JSON.parse(JSON.stringify(searchFormState));
	// return cgJhSpmxApi.cgJhSpmxPage(Object.assign(parameter, searchFormParam)).then((data) => {
	// 	return data;
	// });
	return gyshzApi.mxPage(Object.assign(parameter, searchFormParam)).then((data) => {
		return data
	})
};
// 重置
const reset = () => {
	searchFormRef.value.resetFields();
	table.value.refresh(true);
};
// 删除
const auditCgJhSqd = (record) => {
	let params = [
		{
			id: record.id
		}
	];
	cgJhSqdApi.cgJhSqdAudit(params).then(() => {
		table.value.refresh(true);
	});
};
// 批量删除
const auditBatchCgJhSqd = (params) => {
	cgJhSqdApi.cgJhSqdAudit(params).then(() => {
		table.value.clearRefreshSelected();
	});
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

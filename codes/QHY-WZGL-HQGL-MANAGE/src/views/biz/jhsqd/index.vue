<template>
	<a-card :bordered="false">
		<a-form ref="searchFormRef" name="advanced_search" :model="searchFormState" class="ant-advanced-search-form">
			<a-row :gutter="24">
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
					<a-form-item label="部门名称" name="bmmc">
						<a-tree-select
							v-model:value="searchFormState.bmdm"
							show-search
							tree-node-filter-prop="name"
							style="width: 100%"
							:dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
							placeholder="请选择部门名称"
							allow-clear
							tree-default-expand-all
							:tree-data="treeData"
							:field-names="{
						children: 'children',
						label: 'name',
						value: 'id'
					}"
							selectable="false"
							tree-line
						>
						</a-tree-select>
					</a-form-item>
				</a-col>

				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
					<a-form-item label="申请日期" name="sqrq">
						<a-range-picker v-model:value="searchFormState.sqrq" value-format="YYYY-MM-DD"
										/>
					</a-form-item>
				</a-col>
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
					<a-form-item label="需货日期" name="xhrq">
						<a-range-picker v-model:value="searchFormState.xhrq" value-format="YYYY-MM-DD"
										 />
					</a-form-item>
				</a-col>
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24" v-show="advanced">
					<a-form-item label="申请单号" name="sqdh">
						<a-input v-model:value="searchFormState.sqdh" placeholder="请输入单据编号" />
					</a-form-item>
				</a-col>
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24" v-show="advanced">
					<a-form-item label="采购类型" name="cglx">
						<a-select v-model:value="searchFormState.cglx" allow-clear>
							<a-select-option
								v-for="(item, index) in cglx"
								:key="index"
								:label="item"
								:value="item"
							></a-select-option>
						</a-select>
					</a-form-item>
				</a-col>
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
					<a-form-item>
						<a-button type="primary" @click="table.refresh(true)">查询</a-button>
						<a-button style="margin: 0 8px" @click="reset">重置</a-button>
						<a @click="toggleAdvanced" style="margin-left: 8px">
							{{ advanced ? "收起" : "展开" }}
							<component :is="advanced ? 'up-outlined' : 'down-outlined'" />
						</a>
					</a-form-item>
				</a-col>
			</a-row>
		</a-form>

		<s-table
			ref="table"
			:columns="columns"
			:data="loadData"
			:alert="options.alert.show"
			bordered
			:row-key="(record) => record.id"
			:tool-config="toolConfig"
			:row-selection="options.rowSelection"
		>
			<template #operator class="table-operator">
				<a-space>
					<xn-batch-operation
						:buttonName="'批量审核'"
						:title="'审核此信息?'"
						:selectedRowKeys="selectedRowKeys"
						@batchOperation="auditBatchCgJhSqd"
					/>
				</a-space>
			</template>
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'action'">
					<a-space>
						<a @click="formRef.onOpen(record)">明细</a>
						<a-divider type="vertical" v-if="record.workstate=='已提交'" />
						<a-popconfirm title="确定要审核吗？" @confirm="auditCgJhSqd(record)" v-if="record.workstate=='已提交'">
							<a-button type="link" danger size="small">审核</a-button>
						</a-popconfirm>
						<a-popconfirm title="确定要退回吗？" @confirm="backCgJhSqd(record)" v-if="record.workstate=='已提交'">
							<a-button type="link" danger size="small">退回</a-button>
						</a-popconfirm>
					</a-space>
				</template>
				<template v-if="column.dataIndex === 'bmName'">
					<a-space>
						{{record.bmName+'/'+record.bzName}}
					</a-space>
				</template>
			</template>
		</s-table>
	</a-card>
	<mxIndex ref="formRef" @successful="table.refresh(true)" />
</template>

<script setup name="jhsqd">
import Form from "./form.vue";
import mxIndex from "./mx_index.vue";
import cgJhSqdApi from "@/api/biz/cgJhSqdApi";
import bizOrgApi from "@/api/biz/bizOrgApi";
import tool from "@/utils/tool";
import dayjs from "dayjs";

let searchFormState = reactive({workstate:'已提交',sqrq:[dayjs().format("YYYY-MM-DD"),dayjs().format("YYYY-MM-DD")],});
const searchFormRef = ref();
const table = ref();
const formRef = ref();
const treeData = ref([])
const cglx = ref(["班组订货", "部门备货"]);
const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false };
// 查询区域显示更多控制
const advanced = ref(false);
const toggleAdvanced = () => {
	advanced.value = !advanced.value;
};
const columns = [
	{
		title: "申请单号",
		dataIndex: "sqdh"
	},
	{
		title: "申请日期",
		dataIndex: "sqrq"
	},
	{
		title: "申请部门/班组",
		dataIndex: "bmName"
	},
	{
		title: "需货日期",
		dataIndex: "xhrq"
	},
	{
		title: "采购类型",
		dataIndex: "cglx"
	},
	{
		title: "申请人",
		dataIndex: "sqr"
	},
	{
		title: "合计金额",
		dataIndex: "hjje"
	},
	{
		title: "状态",
		dataIndex: "workstate"
	}
];
// 操作栏通过权限判断是否显示
if (hasPerm(["cgJhSqdEdit", "cgJhSqdDelete"])) {
	columns.push({
		title: "操作",
		dataIndex: "action",
		align: "center",
		width: "200px"
	});
}
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
const loadData = (parameter) => {
	const searchFormParam = JSON.parse(JSON.stringify(searchFormState));
	// sqrq范围查询条件重载
	if (searchFormParam.sqrq) {
		searchFormParam.startSqrq = searchFormParam.sqrq[0];
		searchFormParam.endSqrq = searchFormParam.sqrq[1];
		delete searchFormParam.sqrq;
	}
	// xhrq范围查询条件重载
	if (searchFormParam.xhrq) {
		searchFormParam.startXhrq = searchFormParam.xhrq[0];
		searchFormParam.endXhrq = searchFormParam.xhrq[1];
		delete searchFormParam.xhrq;
	}
	return cgJhSqdApi.cgJhSqdPage(Object.assign(parameter, searchFormParam)).then((data) => {
		return data;
	});
};
// 重置
const reset = () => {
	searchFormRef.value.resetFields();
	table.value.refresh(true);
};
// 审核
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
const backCgJhSqd = (record) => {
	let params = [
		{
			id: record.id
		}
	];
	cgJhSqdApi.backCgJhSqd(params).then(() => {
		table.value.refresh(true);
	});
};
// 批量提交
const auditBatchCgJhSqd = (params) => {
	cgJhSqdApi.cgJhSqdAudit(params).then(() => {
		table.value.clearRefreshSelected();
	});
};
const userInfo = ref(tool.data.get('USER_INFO'))
const initOrg = () => {
	bizOrgApi.orgTree().then((res) => {
		treeData.value = res
	})
	searchFormState.bmdm=userInfo.value.orgId
}
initOrg()
</script>

<template>
	<a-card :bordered="false">
		<a-form ref="searchFormRef" name="advanced_search" :model="searchFormState" class="ant-advanced-search-form">
			<a-row :gutter="24">
				<a-col :xxl="6" :xl="8" :lg="8" :md="12" :sm="24">
					<a-form-item label="日报编号" name="rbbh">
						<a-date-picker picker="date" v-model:value="searchFormState.bh" value-format="YYYY-MM-DD" />
					</a-form-item>
				</a-col>
				<a-col :xxl="6" :xl="8" :lg="8" :md="12" :sm="24">
					<a-form-item label="部门名称" name="bmmc">
						<!--						<a-input v-model:value="searchFormState.bmmc" placeholder="请输入部门名称" />-->
						<a-tree-select
							v-model:value="searchFormState.parentId"
							show-search
							tree-node-filter-prop="name"
							style="width: 100%"
							:dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
							placeholder="请选择部门名称"
							allow-clear
							@change="bmChange"
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
				<a-col :xxl="6" :xl="8" :lg="8" :md="12" :sm="24">
					<a-form-item label="班组名称" name="ffbz">
						<a-select v-model:value="searchFormState.bzdm" placeholder="请输入班组"
								  :options="bzInfo" allow-clear>

						</a-select>
					</a-form-item>
				</a-col>
				<a-col :xxl="6" :xl="8" :lg="8" :md="12" :sm="24">
					<a-button type="primary" @click="table.refresh(true)">查询</a-button>
					<a-button style="margin: 0 8px" @click="reset">重置</a-button>
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
			:scroll="{ x: 2000 }"
		>
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'action'">
					<a-space>
						<a-popconfirm title="确定要审核吗？" @confirm="auditRb(record)" v-if="record.workstate=='申请中'">
							<a-button type="link" danger size="small">审核</a-button>
						</a-popconfirm>
					</a-space>
				</template>

				<template v-if="spflList.indexOf(column.dataIndex) > -1">
					<a-space>
						<a @click="lbformRef.onOpen(record, column.dataIndex)">{{ record[column.dataIndex] }}</a>
					</a-space>
				</template>

				<template v-if="column.dataIndex === 'llxj'">
					<a-typography-text>
						{{ record.llxj }}
					</a-typography-text>
				</template>
			</template>
		</s-table>
	</a-card>
	<Form ref="formRef" @successful="table.refresh(true)" />
	<lbmxFormRef ref="lbformRef" @successful="table.refresh(true)" />
</template>

<script setup name="zwbzrb">
import lbmxFormRef from "./lbmx.vue";
import Form from "./form.vue";
import cgZwBzrbApi from "@/api/biz/cgZwBzrbApi";
import cgJhSqdApi from "@/api/biz/cgJhSqdApi";
import bizOrgApi from "@/api/biz/bizOrgApi";
import bizBzTreeApi from "@/api/biz/bizBzTreeApi";
import tool from "@/utils/tool";
import NP from "number-precision";

let searchFormState = reactive({
	workstate: "申请中"
});
let spflList = ref([])
const searchFormRef = ref();
const table = ref();
const treeData = ref([]);
const bzInfo = ref([]);
const formRef = ref();
const lbformRef = ref()
const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false };
spflList.value = ['hclje', 'sclje', 'tllje', 'zslje', 'yplje', 'dhclje', 'wxclje', 'qtlje']
const columns = [
	{
		title: "日报编号",
		dataIndex: "rbbh"
	},
	{
		title: "班组名称",
		dataIndex: "bzName"
	},
	{
		title: "荤菜类",
		dataIndex: "hclje"
	},
	{
		title: "素菜类",
		dataIndex: "sclje"
	},
	{
		title: "调料类",
		dataIndex: "tllje"
	},
	{
		title: "主食类",
		dataIndex: "zslje"
	},
	{
		title: "饮品类",
		dataIndex: "yplje"
	},
	{
		title: "低耗类",
		dataIndex: "dhclje"
	},
	{
		title: "维修材料类",
		dataIndex: "wxclje"
	},
	{
		title: "其它类",
		dataIndex: "qtlje"
	},
	{
		title: "领料小计",
		dataIndex: "llxj"
	},
	{
		title: "水电气类",
		dataIndex: "sdqlje"
	},
	{
		title: "维护费用类",
		dataIndex: "dhlje"
	},
	{
		title: "酬金类",
		dataIndex: "cjlje"
	},
	{
		title: "其他支出",
		dataIndex: "qtzcje"
	},
	{
		title: "营业收入",
		dataIndex: "yysrje"
	},
	{
		title: "成品调拨收入",
		dataIndex: "cpdbje"
	},

	{
		title: "其他收入",
		dataIndex: "qtsrje"
	},
	{
		title: "盈亏金额",
		dataIndex: "ykje"
	},

	{
		title: "登记人",
		dataIndex: "czy"
	},
	{
		title: "登记日期",
		dataIndex: "rq"
	},
	{
		title: "审核人",
		dataIndex: "shry"
	},
	{
		title: "审核日期",
		dataIndex: "shrq"
	},

	{
		title: "日报状态",
		dataIndex: "workstate"
	}
];
// 操作栏通过权限判断是否显示
if (hasPerm(["cgZwBzrbEdit", "cgZwBzrbDelete"])) {
	columns.push({
		title: "操作",
		dataIndex: "action",
		align: "center",
		width: "150px"
	});
}

// 审核
const auditRb = (record) => {
	let params = [
		{
			id: record.id
		}
	];
	cgZwBzrbApi.rbAudit(params).then(() => {
		table.value.refresh(true);
	});
};

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
	return cgZwBzrbApi.cgZwBzrbPage(Object.assign(parameter, searchFormParam)).then((data) => {
		data.records.forEach(item => {
			console.log(item.dhlje)
			item.llxj =NP.plus(item.hclje, item.sclje, item.tllje, item.zslje, item.yplje, item.dhclje, item.wxclje, item.qtlje);
		})
		return data;
	});
};
// 重置
const reset = () => {
	searchFormRef.value.resetFields();
	table.value.refresh(true);
};
// 删除
const deleteCgZwBzrb = (record) => {
	let params = [
		{
			id: record.id
		}
	];
	cgZwBzrbApi.cgZwBzrbDelete(params).then(() => {
		table.value.refresh(true);
	});
};
// 批量删除
const deleteBatchCgZwBzrb = (params) => {
	cgZwBzrbApi.cgZwBzrbDelete(params).then(() => {
		table.value.clearRefreshSelected();
	});
};

const userInfo = ref(tool.data.get("USER_INFO"));
const initOrg = () => {
	bizOrgApi.orgTree().then((res) => {
		treeData.value = res;
	});
	searchFormState.parentId = userInfo.value.orgId;
	bmChange(searchFormState.parentId);
};
const bmChange = (e) => {
	const param = { "id": e };
	searchFormState.bmdm = e;
	bizBzTreeApi.bizBzList(param).then((res) => {
		searchFormState.bzdm = null;
		bzInfo.value = res.map(item => ({ value: item.id, label: item.name }));
		//searchFormState.bzdm = res[0].id;
	});
};

initOrg();
</script>

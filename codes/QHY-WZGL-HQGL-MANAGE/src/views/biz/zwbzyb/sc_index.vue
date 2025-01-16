<template>
	<a-card :bordered="false">
		<a-form ref="searchFormRef" name="advanced_search" :model="searchFormState" class="ant-advanced-search-form">
			<a-row :gutter="24">
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
					<a-form-item label="月报编号" name="bh">
						<a-date-picker picker="month" v-model:value="searchFormState.bh" value-format="YYYY-MM" />
					</a-form-item>
				</a-col>
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
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
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
					<a-form-item label="班组名称" name="ffbz">
						<a-select v-model:value="searchFormState.bzdm" placeholder="请输入班组"
								  :options="bzInfo" allow-clear>

						</a-select>
					</a-form-item>
				</a-col>
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
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
			:scroll="{ x: 2200,y: 300 }"
		>
			<template #operator class="table-operator">
				<a-space>
					<a-button type="primary" @click="genBzyb()">
						<template #icon>
							<plus-outlined />
						</template>
						生成月报
					</a-button>
					<xn-batch-delete
						v-if="hasPerm('cgZwBzrbBatchDelete')"
						:selectedRowKeys="selectedRowKeys"
						@batchDelete="deleteBatchCgZwBzyb"
					/>
				</a-space>
			</template>
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'action'">
					<a-space>
						<a @click="mxformRef.onOpen(record)" v-if="record.workstate=='已审核'">明细</a>
						<a @click="formRef.onOpen(record)" v-if="record.workstate=='申请中'">登记</a>
						<a-divider type="vertical" v-if="record.workstate=='申请中'" />
						<a-popconfirm title="确定要删除吗？" @confirm="deleteCgZwBzyb(record)">
							<a-button type="link" danger size="small" v-if="record.workstate=='申请中'">删除</a-button>
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
			<template #summary>
				<a-table-summary fixed>
					<a-table-summary-row>
						<a-table-summary-cell :index="0"></a-table-summary-cell>
						<a-table-summary-cell v-for="(item, index) in columns" :key="index" :index="1"
											  :style="{'text-align':'left'}">
							<template v-if="item?.summary">
								<a-typography-text>
									{{ (tableData&&tableData.length>0)?combinedNums["total"][item.dataIndex]:0 }}
								</a-typography-text>
							</template>
							<template v-if="item?.summaryTitle">
								<a-typography-text>
									{{ "合计" }}
								</a-typography-text>
							</template>
						</a-table-summary-cell>
					</a-table-summary-row>
				</a-table-summary>
			</template>
		</s-table>
	</a-card>
	<Form ref="formRef" @successful="table.refresh(true)" />
	<mxForm ref="mxformRef" @successful="table.refresh(true)" />
	<lbmxFormRef ref="lbformRef" @successful="table.refresh(true)" />
</template>

<script setup name="zwbzyb">
import Form from "./form.vue";
import mxForm from "./mx_index.vue";
import lbmxFormRef from './lbmx.vue'
import cgZwBzybApi from "@/api/biz/cgZwBzybApi";
import bizOrgApi from "@/api/biz/bizOrgApi";
import bizBzTreeApi from "@/api/biz/bizBzTreeApi";
import tool from "@/utils/tool";
import dayjs from "dayjs";
import { message } from "ant-design-vue";
import NP from "number-precision";

let searchFormState = reactive({bh:dayjs().add(-1,'month').format("YYYY-MM")});
const searchFormRef = ref();
const table = ref();
const formRef = ref();
const mxformRef = ref();
let spflList = ref([])
const lbformRef = ref()
const treeData = ref([]);
const bzInfo = ref([]);
const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false };
// 查询区域显示更多控制
const advanced = ref(false);
const toggleAdvanced = () => {
	advanced.value = !advanced.value;
};
spflList.value = ['hclje', 'sclje', 'tllje', 'zslje', 'yplje', 'dhclje', 'wxclje', 'qtlje']

const columns = [
	{
		title: '月报编号',
		dataIndex: 'ybbh',
		summaryTitle: true,
		fixed: 'left',
		width:'60px'
	},
	{
		title: "班组名称",
		dataIndex: "bzName",
		fixed: 'left',
		width:'100px'
	},
	{
		title: "荤菜类",
		dataIndex: "hclje",
		summary: true,
		width:'60px'
	},
	{
		title: "素菜类",
		dataIndex: "sclje",
		summary: true,
		width:'60px'
	},
	{
		title: "调料类",
		dataIndex: "tllje",
		summary: true,
		width:'60px'
	},
	{
		title: "主食类",
		dataIndex: "zslje",
		summary: true,
		width:'60px'
	},
	{
		title: "饮品类",
		dataIndex: "yplje",
		summary: true,
		width:'60px'
	},
	{
		title: "低耗类",
		dataIndex: "dhclje",
		summary: true,
		width:'60px'
	},
	{
		title: "维修材料类",
		dataIndex: "wxclje",
		summary: true,
		width:'70px'
	},
	{
		title: "其它类",
		dataIndex: "qtlje",
		summary: true,
		width:'60px'
	},
	{
		title: "领料小计",
		dataIndex: "llxj",
		summary: true,
		width:'60px'
	},
	{
		title: "水电气类",
		dataIndex: "sdqlje",
		summary: true,
		width:'60px'
	},
	{
		title: "维护费用类",
		dataIndex: "dhlje",
		summary: true,
		width:'70px'
	},
	{
		title: "酬金类",
		dataIndex: "cjlje",
		summary: true,
		width:'60px'
	},
	{
		title: "其他支出",
		dataIndex: "qtzcje",
		summary: true,
		width:'60px'
	},
	{
		title: "营业收入",
		dataIndex: "yysrje",
		summary: true,
		width:'60px'
	},
	{
		title: "成品调拨收入",
		dataIndex: "cpdbje",
		summary: true,
		width:'80px'
	},

	{
		title: "其他收入",
		dataIndex: "qtsrje",
		summary: true,
		width:'60px'
	},
	{
		title: "盈亏金额",
		dataIndex: "ykje",
		summary: true,
		width:'60px'
	},

	{
		title: "登记人",
		dataIndex: "czy",
		width:'60px'
	},
	{
		title: "登记日期",
		dataIndex: "rq",
		width:'110px'
	},
	{
		title: "审核人",
		dataIndex: "shry",
		width:'60px'
	},
	{
		title: "审核日期",
		dataIndex: "shrq",
		width:'110px'
	},

	{
		title: '月报状态',
		dataIndex: 'workstate',
		width:'60px'
	},
]
// 操作栏通过权限判断是否显示
if (hasPerm(['cgZwBzybEdit', 'cgZwBzybDelete'])) {
	columns.push({
		title: '操作',
		dataIndex: 'action',
		align: 'center',
		fixed: 'right',
		width: '150px'
	})
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
const tableData = ref();
const loadData = (parameter) => {
	const searchFormParam = JSON.parse(JSON.stringify(searchFormState));
	// rq范围查询条件重载
	if (searchFormParam.rq) {
		searchFormParam.startRq = searchFormParam.rq[0];
		searchFormParam.endRq = searchFormParam.rq[1];
		delete searchFormParam.rq;
	}
	return cgZwBzybApi.cgZwBzybPage(Object.assign(parameter, searchFormParam)).then((data) => {
		if(data.records&&data.records.length>0){
			data.records.forEach(item => {
				tableData.value = data.records;
				item.llxj = NP.plus(item.hclje, item.sclje, item.tllje, item.zslje, item.yplje, item.dhclje, item.wxclje, item.qtlje);
			});
		}else{
			tableData.value=[]
		}
		return data;
	});
};
// 重置
const reset = () => {
	searchFormRef.value.resetFields();
	table.value.refresh(true);
};
// 删除
const deleteCgZwBzyb = (record) => {
	let params = [
		{
			id: record.id
		}
	];
	cgZwBzybApi.cgZwBzybDelete(params).then(() => {
		table.value.refresh(true);
	});
};
// 删除
const submitCgZwBzyb = (record) => {

	cgZwBzybApi.cgZwBzybSubmit(record).then(() => {
		table.value.refresh(true);
	});
};
// 批量删除
const deleteBatchCgZwBzyb = (params) => {
	cgZwBzybApi.cgZwBzybDelete(params).then(() => {
		table.value.clearRefreshSelected();
	});
};
//生成
const genBzyb = () => {
	let current = dayjs(new Date()).format("YYYY-MM");
	console.log(current)
	console.log("----------")
	console.log(searchFormState.bh)
	if(searchFormState.bh >= current) {
		message.info("请选择当月之前的月份")
		return
	}
	cgZwBzybApi.genBzyb(searchFormState).then(()=>{
		table.value.refresh(true)
	});
};
const userInfo = ref(tool.data.get('USER_INFO'))
const initOrg = () => {
	bizOrgApi.orgTree().then((res) => {
		treeData.value = res
	})
	searchFormState.parentId=userInfo.value.orgId
	bmChange(searchFormState.parentId)
}
const bmChange = (e) => {
	const param = { "id": e };
	searchFormState.bmdm = e;
	bizBzTreeApi.bizBzList(param).then((res) => {
		searchFormState.bzdm = null;
		bzInfo.value = res.map(item => ({ value: item.id, label: item.name }));
		searchFormState.bzdm = res[0].id;
	});
};

const combinedNums = computed(() => {
	let totalhclje = 0;
	let totalgyje = 0;
	let totalylje = 0;
	let total = {};
	if (tableData.value) {
		tableData.value.forEach((spmxItem, index) => {

			for (let item in spmxItem) {
				columns.forEach(column => {
					if (column.dataIndex == item && column.summary) {
						if (!total[item]) {
							total[item] = 0;
						}
						total[item] = NP.plus(total[item], spmxItem[item]);
					}
				});

			}


		});

	}
	return {
		total
	};
});
initOrg();
</script>

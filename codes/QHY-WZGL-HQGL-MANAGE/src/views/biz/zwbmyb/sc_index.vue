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
							v-model:value="searchFormState.bmdm"
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
					<a-form-item label="生成日期" name="rq">
						<a-range-picker v-model:value="searchFormState.rq" value-format="YYYY-MM-DD" />
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
			:scroll="{ x: 2300,y: 300 }"
		>
			<template #operator class="table-operator">
				<a-space>
					<a-button type="primary" @click="genBmyb()">
						<template #icon>
							<plus-outlined />
						</template>
						生成月报
					</a-button>
					<xn-batch-delete
						v-if="hasPerm('cgZwBzrbBatchDelete')"
						:selectedRowKeys="selectedRowKeys"
						@batchDelete="deleteBatchCgZwBmyb"
					/>
				</a-space>
			</template>
			<template #headerCell="{title, column}">
				<template v-if="column.dataIndex === 'qqkcje'">
					{{ "前期库存" }}
					<a-popover>
						<template #content>
							前期库存=上月库存结余+该月新录入的历史库存
						</template>
						<exclamation-circle-outlined style="color: gray" />
					</a-popover>
				</template>
				<template v-if="column.dataIndex === 'kcje'">
					{{ "库存结余" }}
					<a-popover>
						<template #content>
							库存结余=本期采购+调拨入库+库存盘盈-库存报损-出库金额-库存调出+前期库存
						</template>
						<exclamation-circle-outlined style="color: gray" />
					</a-popover>
				</template>
				<template v-if="column.dataIndex === 'ykje'">
					{{ "盈亏金额" }}
					<a-popover>
						<template #content>
							盈亏金额=营业收入+其他收入+成品调出+库存盘盈-库存报损-出库金额-水电气类-低耗类-酬金类-其他支出
						</template>
						<exclamation-circle-outlined style="color: gray" />
					</a-popover>
				</template>
			</template>
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'dbck'">
					<a-space>
						<a @click="dbckformRef.onOpen(record, column.dataIndex)">{{ NP.plus(record.dbckje + record.cpdbje) }}</a>
					</a-space>
				</template>
				<template v-if="column.dataIndex === 'action'">
					<a-space>
						<a @click="formRef.onOpen(record)">登记</a>
						<a-divider type="vertical" />
						<a-popconfirm title="确定要删除吗？" @confirm="deleteCgZwBmyb(record)">
							<a-button type="link" danger size="small">删除</a-button>
						</a-popconfirm>
					</a-space>
				</template>

				<!--本期采购-->
				<template v-if="column.dataIndex === 'cgjhje'">
					<a-space>
						<a @click="cgjhjeformRef.onOpen(record, column.dataIndex)">{{ record[column.dataIndex] }}</a>
					</a-space>
				</template>

				<!--库存报损金额-->
				<!--				<template v-if="column.dataIndex === 'kcbsje'">-->
				<!--					<a-space>-->
				<!--						<a @click="kcbsjeformRef.onOpen(record, column.dataIndex)">{{ record[column.dataIndex] }}</a>-->
				<!--					</a-space>-->
				<!--				</template>-->

				<!--调拨入库金额-->
				<template v-if="column.dataIndex === 'dbrkje'">
					<a-space>
						<a @click="dbrkjeformRef.onOpen(record, column.dataIndex)">{{ record[column.dataIndex] }}</a>
					</a-space>
				</template>

				<!--出库金额-->
				<template v-if="column.dataIndex === 'ckje'">
					<a-space>
						<a @click="ckjeformRef.onOpen(record, column.dataIndex)">{{ record[column.dataIndex] }}</a>
					</a-space>
				</template>

				<!--库存调出-->
				<template v-if="column.dataIndex === 'dbckje'">
					<a-space>
						<a @click="dbckjeformRef.onOpen(record, column.dataIndex)">{{ record[column.dataIndex] }}</a>
					</a-space>
				</template>

				<!--成品调出-->
				<template v-if="column.dataIndex === 'cpdbje'">
					<a-space>
						<a @click="cpdbjeformRef.onOpen(record, column.dataIndex)">{{ record[column.dataIndex] }}</a>
					</a-space>
				</template>

				<!--调拨出库-->
				<!--				<template v-if="column.dataIndex === 'dbck'">-->
				<!--					<a-space>-->
				<!--						<a @click="dbckformRef.onOpen(record, column.dataIndex)">{{ record[column.dataIndex] }}</a>-->
				<!--					</a-space>-->
				<!--				</template>-->

			</template>
		</s-table>
	</a-card>
	<Form ref="formRef" @successful="table.refresh(true);table.refresh(true)" />
	<cgjhjeFormRef ref="cgjhjeformRef" @successful="table.refresh(true)" />
	<kcbsjeFormRef ref="kcbsjeformRef" @successful="table.refresh(true)" />
	<dbrkjeFormRef ref="dbrkjeformRef" @successful="table.refresh(true)" />
	<ckjeFormRef ref="ckjeformRef" @successful="table.refresh(true)" />
	<kcdcFormRef ref="kcdcformRef" @successful="table.refresh(true)" />
	<cpdbjeFormRef ref="cpdbjeformRef" @successful="table.refresh(true)" />
	<dbckjeFormRef ref="dbckjeformRef" @successful="table.refresh(true)" />
	<dbckFormRef ref="dbckformRef" @successful="table.refresh(true)" />

</template>

<script setup name="zwbmyb">
import Form from "./form.vue";
import cgjhjeFormRef from "./cgjhjemx.vue";
import ckjeFormRef from "./ckjemx.vue";
import cpdbjeFormRef from "./cpdbjemx.vue";
import dbrkjeFormRef from "./dbrkmx.vue";
import kcbsjeFormRef from "./kcbsmx.vue";
import kcdcFormRef from "./kcdcmx.vue";
import dbckjeFormRef from "./dbckjemx.vue";
import dbckFormRef from "./dbckmx.vue";
import NP from "number-precision";
import cgZwBmybApi from "@/api/biz/cgZwBmybApi";
import bizOrgApi from "@/api/biz/bizOrgApi";
import bizBzTreeApi from "@/api/biz/bizBzTreeApi";
import tool from "@/utils/tool";
import dayjs from "dayjs";

let searchFormState = reactive({bh:dayjs().add(-1,'month').format("YYYY-MM")});
const searchFormRef = ref();
const table = ref();
const formRef = ref();
const cgjhjeformRef = ref();
const ckjeformRef = ref();
const kcbsjeformRef = ref();
const dbrkjeformRef = ref();
const cpdbjeformRef = ref();
const dbckjeformRef = ref();
const dbckformRef = ref();
const treeData = ref([]);
const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false };
const columns = [
	{
		title: '月报编号',
		dataIndex: 'ybbh',
		summaryTitle: true,
		fixed: 'left',
		width:'50px'
	},
	{
		title: "部门名称",
		dataIndex: "bmName",
		fixed: 'left',
		width:'90px'
	},
	{
		title: "前期库存",
		dataIndex: "qqkcje",
		width:'60px'
	},
	{
		title: "本期采购",
		dataIndex: "cgjhje",
		summary: true,
		width:'60px'
	},
	{
		title: "库存报损",
		dataIndex: "kcbsje",
		summary: true,
		width:'70px'
	},
	{
		title: "调拨入库",
		dataIndex: "dbrkje",
		summary: true,
		width:'70px'
	},
	{
		title: "出库金额",
		dataIndex: "ckje",
		summary: true,
		width:'70px'
	},
	{
		title: "库存调出",
		dataIndex: "dbckje",
		summary: true,
		width:'70px'
	},
	{
		title: "成品调出",
		dataIndex: "cpdbje",
		summary: true,
		width:'70px'
	},
	{
		title: "调拨出库",
		dataIndex: "dbck",
		summary: true,
		width:'70px'
	},
	{
		title: "库存结余",
		dataIndex: "kcje",
		width:'70px'
	},
	{
		title: "实际库存",
		dataIndex: "kcsjje",
		width:'60px'
	},
	{
		title: "水电气类",
		dataIndex: "sdqlje",
		summary: true,
		width:'70px'
	},
	{
		title: "维修费",
		dataIndex: "dhlje",
		summary: true,
		width:'60px'
	},
	{
		title: "酬金类",
		dataIndex: "cjlje",
		summary: true,
		width:'70px'
	},
	{
		title: "其他支出",
		dataIndex: "qtzcje",
		summary: true,
		width:'70px'
	},
	{
		title: "营业收入",
		dataIndex: "yysrje",
		summary: true,
		width:'80px'
	},
	{
		title: "其他收入",
		dataIndex: "qtsrje",
		summary: true,
		width:'70px'
	},
	{
		title: "盈亏金额",
		dataIndex: "ykje",
		summary: true,
		width:'70px'
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
		title: "状态",
		dataIndex: "workstate",
		width:'60px'
	}
];
// 操作栏通过权限判断是否显示
if (hasPerm(["cgZwBzybEdit", "cgZwBzybDelete"])) {
	columns.push({
		title: "操作",
		dataIndex: "action",
		fixed: 'right',
		align: "center",
		width: "120px"
	});
}

//生成
const genBmyb = () => {
	cgZwBmybApi.genBmyb(searchFormState).then(() => {
		table.value.refresh(true);
	});
};

const userInfo = ref(tool.data.get("USER_INFO"));
const initOrg = () => {
	bizOrgApi.orgTree().then((res) => {
		treeData.value = res;
	});
	searchFormState.bmdm = userInfo.value.orgId;
};

initOrg();

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

let qqkc = ref();

const loadData = (parameter) => {
	const searchFormParam = JSON.parse(JSON.stringify(searchFormState));
	// rq范围查询条件重载
	if (searchFormParam.rq) {
		searchFormParam.startRq = searchFormParam.rq[0];
		searchFormParam.endRq = searchFormParam.rq[1];
		delete searchFormParam.rq;
	}
	searchFormParam.ybbh = searchFormParam.bh
	return cgZwBmybApi.cgZwBmybPage(Object.assign(parameter, searchFormParam)).then((data) => {
		return data;
	});
};
// 重置
const reset = () => {
	searchFormRef.value.resetFields();
	table.value.refresh(true);
};
// 删除
const deleteCgZwBmyb = (record) => {
	let params = [
		{
			id: record.id
		}
	];
	cgZwBmybApi.cgZwBmybDelete(params).then(() => {
		table.value.refresh(true);
	});
};

// 批量删除
const deleteBatchCgZwBmyb = (params) => {
	cgZwBmybApi.cgZwBmybDelete(params).then(() => {
		table.value.clearRefreshSelected();
	});
};


</script>

<template>
	<a-row :gutter="16">
		<a-col :span="15" class="gutter-row">
			<a-spin :spinning="spinning" tip="数据处理中...">
				<a-card :bordered="false" style="margin-bottom: 10px">

					<div>
<!--						<a-row :gutter="24" style="height: 85vh;overflow:auto;">-->
<!--							<a-empty v-if="!spList||spList.length==0" style="padding-top:20%;width:100%;"/>-->
<!--							<a-col v-for="sp in spList" :xxl="12" :xl="12" :lg="12" :md="12" :sm="12"-->
<!--								   style="padding-left: 2px;padding-right: 2px;padding-bottom: 5px;">-->
<!--								<a-card class="card" :class="sp.active" style="" @click="cz(sp)">-->
<!--									<div class="shuxin spmc">{{ sp.spmc }}</div>-->
<!--									<div class="shuxin">订货数量:{{ sp.sqsl + sp.jldw }}</div>-->
<!--									<div class="shuxin">供应单价:{{ sp.gydj + "元" }}</div>-->
<!--									<div class="detail" @click.stop="showDetail(sp)">详情</div>-->
<!--								</a-card>-->
<!--							</a-col>-->
<!--						</a-row>-->
							<a-row  type="flex" wrap>
							<a-empty v-if="!spList||spList.length==0" style="padding-top:20%;width:100%;"/>
							<a-col v-for="sp in spList" :xxl="12" :xl="12" :lg="12" :md="12" :sm="12" :span="12	"
								   style="padding-left: 2px;padding-right: 2px;padding-bottom: 5px;">
								<a-card class="card" :class="sp.active" style="" @click="cz(sp)">
									<div class="shuxin spmc">{{ sp.spmc }}</div>
									<div class="shuxin">订货数量:{{ sp.sqsl + sp.jldw }}</div>
									<div class="shuxin">供应单价:{{ sp.gydj + "元" }}</div>
									<div class="detail" @click.stop="showDetail(sp)">详情</div>
								</a-card>
							</a-col>
						</a-row>

					</div>
					<!--			<s-table-->
					<!--				ref="table"-->
					<!--				:columns="columns"-->
					<!--				:data="loadData"-->
					<!--				:alert="options.alert.show"-->
					<!--				bordered-->
					<!--				:row-key="(record) => record.id"-->
					<!--				:tool-config="toolConfig"-->
					<!--				:row-selection="options.rowSelection"-->
					<!--				:pagination="{ pageSize: 100 }"-->
					<!--			>-->
					<!--				<template #operator class="table-operator">-->
					<!--					<a-space>-->
					<!--						<xn-batch-operation-->
					<!--							:buttonName="'批量收货'"-->
					<!--							:title="'确认此信息?'"-->
					<!--							:selectedRowKeys="selectedRowKeys"-->
					<!--							@batchOperation="acceptBatchCgJhSpmx"-->
					<!--						/>-->
					<!--					</a-space>-->
					<!--				</template>-->

					<!--				<template #bodyCell="{ column, record }">-->
					<!--					<template v-if="column.dataIndex === 'bzrq'">-->
					<!--						<div>-->
					<!--							<a-date-picker v-model:value="record.bzrq" value-format="YYYY-MM-DD HH:mm:ss"-->
					<!--							/>-->
					<!--						</div>-->
					<!--					</template>-->
					<!--					<template v-if="column.dataIndex === 'gysmc'">-->
					<!--						<div>-->
					<!--							{{ record.gysmc }} <a-popover>-->
					<!--							<template #content>-->
					<!--								<p>联系电话：{{ record.gysDh }}</p>-->
					<!--								<p>联系人：{{ record.gysLxr }}</p>-->
					<!--							</template>-->
					<!--							<exclamation-circle-outlined  style="color: gray"/>-->
					<!--						</a-popover>-->
					<!--						</div>-->
					<!--					</template>-->
					<!--					<template v-if="column.dataIndex === 'shsl'">-->
					<!--						<div>-->
					<!--							<a-input-number-->
					<!--								v-model:value="record.shsl"-->
					<!--								:min="0"-->
					<!--							/>-->
					<!--						</div>-->
					<!--					</template>-->
					<!--				</template>-->
					<!--			</s-table>-->
				</a-card>
				<Form ref="formRef" @successful="table.refresh(true)" />
				<menu-selector-plus
					ref="MenuSelectorPlus"
					:role-global="false"
					@onBack="roleBack"
				/>
				<sub-form
					ref="SubForm"
					:role-global="false"
					@successful="table.refresh(true)"
				/>
			</a-spin>
		</a-col>
		<a-col :span="9" class="gutter-row">
			<a-form ref="searchFormRef" name="advanced_search" :model="searchFormState"
					class="ant-advanced-search-form">
				<a-row :gutter="24">
					<!--				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">-->
					<!--					<a-form-item label="部门名称" name="yjbmmc">-->
					<!--						<a-input v-model:value="searchFormState.yjbmmc" placeholder="请输入一级部门名称" />-->
					<!--					</a-form-item>-->
					<!--				</a-col>-->
					<a-col :xxl="24" :xl="24" :lg="24" :md="24" :sm="24">
						<a-form-item name="bmmc">
							<template v-slot:label>
								<cluster-outlined style="font-size: 25px;color:#02A7F0" />
							</template>
							<a-tree-select
								v-model:value="formData.parentId"
								show-search
								tree-node-filter-prop="name"
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
								:bordered="false"
							>

								<template #suffixIcon></template>
							</a-tree-select>
						</a-form-item>
					</a-col>
					<a-col :xxl="24" :xl="24" :lg="24" :md="24" :sm="24">
						<a-form-item name="gysmc">
							<template v-slot:label>
								<shopping-cart-outlined style="font-size: 25px;color:#02A7F0" />
							</template>
							<!--						<a-input v-model:value="searchFormState.bmmc" placeholder="请输入部门名称" />-->
							<a-select v-model:value="searchFormState.gysdm" placeholder="请输入供应商名称" show-search
									  optionFilterProp="label" allow-clear :bordered="false">
								<template #suffixIcon></template>
								<a-select-option
									:key="item.gysdm"
									v-for="item in gysInfo"
									:value="item.gysdm"
									:label="item.gysmc"
								>{{ item.gysmc }}
								</a-select-option>

							</a-select>
						</a-form-item>
					</a-col>
					<a-col :xxl="24" :xl="24" :lg="24" :md="24" :sm="24">
						<a-form-item name="xhrq">
							<template v-slot:label>
								<calendar-outlined style="font-size: 25px;color:#02A7F0" />
							</template>

							<a-range-picker v-model:value="searchFormState.xhrq" value-format="YYYY-MM-DD"
											:bordered="false" />
						</a-form-item>
					</a-col>
					<a-col :xxl="24" :xl="24" :lg="24" :md="24" :sm="24">
						<div class="div-switch">
							<div class="switch">
								班组部门切换
							</div>
							<a-switch v-model:checked="bzbm" checked-children="班组" un-checked-children="部门"
									  class="switch" />
						</div>
					</a-col>
					<a-col :xxl="24" :xl="24" :lg="24" :md="24" :sm="24">
						<div class="div-switch">
							<div class="switch">
								返秤切换
							</div>
							<a-switch v-model:checked="dsys" checked-children="待收" un-checked-children="已收"
									  class="switch" />
						</div>
					</a-col>

					<!--					<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24" v-show="advanced">-->
					<!--						<a-form-item name="workstate">-->
					<!--							<a-select v-model:value="searchFormState.workstate">-->
					<!--								<a-select-option-->
					<!--									v-for="(item, index) in workstate"-->
					<!--									:key="index"-->
					<!--									:label="item"-->
					<!--									:value="item"-->
					<!--								></a-select-option>-->
					<!--							</a-select>-->
					<!--						</a-form-item>-->
					<!--					</a-col>-->

					<a-col :xxl="24" :xl="24" :lg="24" :md="24" :sm="24" style="margin-bottom: 10px">

							<a-button type="primary" @click="loadData"
									  style="background: #02A7F0;width:30%;margin-left:10%;margin-right:10%;height: 48px;font-size: 20px;padding: 5px">
								查询
							</a-button>

							<a-button type="primary" class="save-btn"
									  style="background: #02A7F0;width:30%;margin-left:10%;margin-right:10%;height: 48px;font-size: 20px;padding: 5px"
									  @click="save">称重保存
							</a-button>


					</a-col>
					<a-col :xxl="24" :xl="24" :lg="24" :md="24" :sm="24">
						<a-form-item style="border-bottom : 0px solid #FFFFFF">
							<a-button type="primary" @click="refresh"
									  style="background: #02A7F0;width:23%;margin-left:5%;margin-right:5%;height: 40px;font-size: 16px;padding: 5px">
								页面刷新
							</a-button>

							<a-button type="primary" class="save-btn"
									  style="background: #02A7F0;width:23%;margin-left:5%;margin-right:5%;height: 40px;font-size: 16px;padding: 5px"
									  @click="recheck">设备自检
							</a-button>
							<a-button type="primary" class="save-btn"
									  style="background: #02A7F0;width:23%;margin-left:5%;margin-right:5%;height: 40px;font-size: 16px;padding: 5px"
									  @click="relogin">切换用户
							</a-button>
						</a-form-item>

					</a-col>
				</a-row>
			</a-form>
		</a-col>
	</a-row>
	<a-modal v-model:visible="detailVisible" title="商品详情" :footer="null">
		<div style="padding: 20px;">
			<div class="shuxin">商品名称：{{ spInfo.spmc }}</div>
			<div class="shuxin">商品规格：{{ spInfo.spgg }}</div>
			<div class="shuxin">品牌产地：{{ spInfo.ppcd }}</div>
			<div class="shuxin">包装率：{{ spInfo.bzl }}</div>
			<div class="shuxin">订货数量：{{ spInfo.sqsl + spInfo.jldw }}</div>
			<div class="shuxin" v-if="spInfo.shsl">收货数量：{{ spInfo.shsl + spInfo.jldw }}</div>
			<div class="shuxin">订货人：{{ spInfo.bmName + "-" + spInfo.sqr }}</div>
			<div class="shuxin">订货时间：{{ spInfo.sqrq }}</div>
			<div class="shuxin">需货时间：{{ spInfo.xhrq }}</div>
			<div class="shuxin">供应商：{{ spInfo.gysmc }}</div>
		</div>
	</a-modal>
	<a-modal v-model:visible="recheckVisible" :title="null" :footer="null" style="text-align: center;">
		<a-spin tip="自检中" style="margin:50px;" :span="spin">

		</a-spin>
	</a-modal>
</template>

<script setup name="bmbh">
import Form from "./form.vue";
import cgJhSpmxApi from "@/api/biz/cgJhSpmxApi";
import bizBzTreeApi from "@/api/biz/bizBzTreeApi";
import bizOrgApi from "@/api/biz/bizOrgApi";
import bizUserApi from "@/api/biz/bizUserApi";
import menuSelectorPlus from "@/components/Selector/menuSelectorPlus.vue";
import { notification } from "ant-design-vue";
import cgJhSqdApi from "@/api/biz/cgJhSqdApi";
import subForm from "./subForm.vue";
import cgCodeGysApi from "@/api/biz/cgCodeGysApi";
import dayjs from "dayjs";
import tool from "@/utils/tool";
import { message } from "ant-design-vue";
import { io } from "socket.io-client";
import { watch } from "vue";
import store from "@/store";
import VueTouch from "v-touch";

let searchFormState = reactive({
	cglx: "部门备货",
	size: 12,
	current: 1,
	workstate: "待收货",
	xhrq: [dayjs().startOf("day").format("YYYY-MM-DD"), dayjs().endOf("day").format("YYYY-MM-DD")]

});
let MenuSelectorPlus = ref();
let SubForm = ref();
const searchFormRef = ref(

);
const detailVisible = ref(false);
const recheckVisible = ref(false)
const spInfo = ref();
const table = ref();
const visible = ref(false);
const formData = ref({});
const submitLoading = ref(false);
const formRef = ref();
const treeData = ref([]);
const extJson = ref([]);
const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false };
const bzInfo = ref([]);
const gysInfo = ref([]);
const workstate = ref(["待收货", "收货中"]);
const selectedRecord = ref([]);
const bzbm = ref(true);
const dsys = ref(true);
const networkPercent = ref(0)
const scalePercent = ref(0)
const cameraPercent = ref(0)
// 查询区域显示更多控制
const advanced = ref(false);
const spin = ref(false)


const toggleAdvanced = () => {
	advanced.value = !advanced.value;
};
const columns = [
	{
		title: "商品类别",
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
		title: "品牌产地",
		dataIndex: "ppcd"
	},
	{
		title: "包装率",
		dataIndex: "bzl"
	},
	{
		title: "单位",
		dataIndex: "jldw"
	},


	{
		title: "订货数量",
		dataIndex: "sqsl"
	},
	{
		title: "收货数量",
		dataIndex: "shsl"
	},
	{
		title: "保质期",
		dataIndex: "bzrq"
	},
	{
		title: "供应商名称",
		dataIndex: "gysmc"
	},
	{
		title: "申请人",
		dataIndex: "sqr"
	},
	{
		title: "状态",
		dataIndex: "workstate"
	}
];
// 操作栏通过权限判断是否显示
const selectedRowKeys = ref([]);
const spList = ref();
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
			selectedRowKeys.value = selectedRows;
		}
	}
};
const loadData = (parameter) => {

	const searchFormParam = JSON.parse(JSON.stringify(searchFormState));
	// sqrq范围查询条件重载
	if (searchFormParam.xhrq) {
		searchFormParam.startXhrq = searchFormParam.xhrq[0];
		searchFormParam.endXhrq = searchFormParam.xhrq[1];
		delete searchFormParam.xhrq;
	}
	if(bzbm.value){
		searchFormParam.cglx='班组订货'
	}else{
		searchFormParam.cglx='部门备货'
	}
	if(dsys.value){
		searchFormParam.workstate='待收货'
	}else{
		searchFormParam.workstate='收货中'
	}
	if (!parameter) {
		parameter = {};
	}
	return cgJhSpmxApi.cgJhSpshmxPage(Object.assign(parameter, searchFormParam)).then((data) => {

		spList.value = data.records;
		return data;
	});
};

//选择商品
const selectSp = (record) => {
	selectedRecord.value = record;
	if (!searchFormState.bmdm) {
		if (!record.sqdh) {
			notification.error({
				message: "提示",
				description: "请先选择部门!"
			});
			return;
		}
	}
	// 查询到已有角色，并转为ids的格式，给角色选择器
	const param = {
		id: 1,
		bmdm: searchFormState.bmdm,
		sqdh: record.sqdh
	};
	MenuSelectorPlus.value.showMenuModal(param);
};
//提交
const sub = (record) => {
	SubForm.value.onOpen(record);
};
//选择完成后回调
const roleBack = (value) => {
	let params = {
		spdm: selectedRecord.value.spdm,
		spmxList: []
	};
	if (value.list.length > 0) {
		value.list.forEach((item) => {
			params.spmxList.push(item);
		});
	}
	cgJhSpmxApi.cgJhSpmxSubmitForm(params, value.isEdit).then((res) => {
		table.value.refresh(true);
	});

};
// 重置
const reset = () => {
	searchFormRef.value.resetFields();
	table.value.refresh(true);
};
// 删除
const deleteCgJhSpmx = (record) => {
	let params = [
		{
			id: record.id
		}
	];
	cgJhSqdApi.cgJhSqdDelete(params).then(() => {
		table.value.refresh(true);
	});
};
const spinning = ref(false);
// 批量收货
const acceptBatchCgJhSpmx = (params) => {
	// for(let spmx in params){
	// 	if (params[spmx].bzrq == '' || params[spmx].bzrq == null) {
	// 		message.warn("请填写保质期")
	// 		return
	// 	}
	// }

	spinning.value = true;

	cgJhSpmxApi.acceptBatchCgJhSpmx(params).then(() => {
		spinning.value = false;
		table.value.clearRefreshSelected();
	});
};

const userInfo = ref(tool.data.get("USER_INFO"));
const initOrg = () => {
	bizOrgApi.orgTree().then((res) => {
		treeData.value = res;
	});
	cgCodeGysApi.cgCodeGysList().then((res) => {
		gysInfo.value = res;
	});
	formData.value.parentId = userInfo.value.orgId;
	bmChange(formData.value.parentId);
};
const bmChange = (e) => {
	const param = { "id": e };
	searchFormState.bmdm = e;
};
const emit = defineEmits();
const cz = (record) => {
	spList.value.forEach(sp => {
		sp.active = "";
	});
	record.active = "card-active";
	emit("childEvent", record);

};
const showDetail = (record) => {
	detailVisible.value = true;
	spInfo.value = record;
};
const save = () => {
	emit("saveEvent");
};
const props = defineProps({ hasSave: { type: String, default: () => "0" } });
watch(() => props.hasSave, (newValue) => {
	if (newValue == "1") {
		loadData();
	}
});
const filterOption = (input, option) => {
	return option.value.toLowerCase().indexOf(input.toLowerCase()) >= 0;
};
const refresh = () =>{
	location.reload();
}
const recheck = () =>{
	recheckVisible.value=true;
	networkPercent.value=0
	scalePercent.value=0
	networkPercent.value=0
console.log(networkPercent)
	while(networkPercent.value<100){

			networkPercent.value=networkPercent.value+1

		console.log(networkPercent)
	}

}
const relogin = () =>{
	location.reload();
}
initOrg();
</script>

<style>
.select {
	padding-left: 1100px;
}

.shuxin {
	text-overflow: ellipsis;
	overflow: hidden;
	white-space: nowrap;
	color: black;
	font-size: 20px;
}

.ant-card-body {
	padding: 4px;
}

.spmc {
	font-weight: bold;
	font-size: 25px;
	margin: 5px 5px;
}

.card {
	padding-left: 10px;
	border-radius: 5px;
	border: 1px dashed #02A7F0
}

.card-active {
	background: #CAF982;
	border: 1px solid gray;
}

.card-active:before {
	content: "";
	width: 5px;
	height: 100%;
	position: absolute;
	top: 0;
	left: 0;
	background-color: red;
	border-radius: 5px;

}

.detail {
	position: absolute;
	top: 8px;
	right: 8px;
	font-size: 20px;
}

.ant-form-item {
	font-size: 20px;
	margin-bottom: 5px;
}

.ant-form-item-label > label {
	font-size: 20px;
}

.ant-select {
	font-size: 20px;
}

.ant-picker-input > input {
	font-size: 20px;
}

.ant-form-item-control-input {
	border-bottom: 1px solid gray;
}

.ant-switch-inner {
	font-size: 20px;
}

.div-switch {
	display: flex;
	margin-bottom: 5px;
	margin-top: 10px;
}

.switch {
	flex: 1;
	font-size: 20px;
}
</style>

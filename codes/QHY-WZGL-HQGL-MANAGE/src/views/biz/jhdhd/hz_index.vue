<template>

	<a-modal
		v-model:visible="visible"
		title="明细"
		width="100%"
		:mask-closable="false"
		wrap-class-name="full-modal"
		:destroy-on-close="true"
		@ok="submit"
		:ok-text="'确认生成并下达'"
		:okButtonProps="{loading: spining}"
	>
		<a-card>
			<s-table
				ref="table"
				:columns="columns"
				:data="loadData"
				bordered
				:row-key="(record) => record.id"
				:tool-config="toolConfig"
			>
				<template #operator class="table-operator">

				</template>
				<template #bodyCell="{ column, record }">
					<template v-if="column.dataIndex === 'gysmc'">
						<a-select v-model:value="record.gysdm" placeholder="请输入供应商名称" show-search
								  optionFilterProp="label"  @change="updateGys(record)" >
							<a-select-option
								:key="item.gysdm"
								v-for="item in gysInfo"
								:value="item.gysdm"
								:label="item.gysmc"
							>{{ item.gysmc }}
							</a-select-option>

						</a-select>
					</template>
				</template>
			</s-table>
		</a-card>
		<Form ref="formRef" @successful="table.refresh(true)" />

	</a-modal>

</template>

<script setup name="jhsqd">
import Form from "./form.vue";
import cgJhSqdApi from "@/api/biz/cgJhSqdApi";
import cgJhSpmxApi from "@/api/biz/cgJhSpmxApi";
import cgCodeGysApi from "@/api/biz/cgCodeGysApi";
import cgJhDhdApi from "@/api/biz/cgJhDhdApi";

let searchFormState = reactive({});
const searchFormRef = ref();
const table = ref();
const visible = ref(false);
const formRef = ref();
const gysInfo = ref([])
const emit = defineEmits({ successful: null })
const cglx = ref(["班组订货", "部门备货"]);
const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false };
// 查询区域显示更多控制
const advanced = ref(false);
const gys = ref({})
const toggleAdvanced = () => {
	advanced.value = !advanced.value;
};
const columns = [
	{
		title: "部门名称",
		dataIndex: "bmName"
	},
	{
		title: "供应商名称",
		dataIndex: "gysmc"
	},
	{
		title: "商品名称",
		dataIndex: "spmc"
	},
	{
		title: "合计金额",
		dataIndex: "gyje"
	},

	{
		title: "采购类型",
		dataIndex: "cglx"
	}
];

const selectedRowKeys = ref([]);

const onOpen = (record) => {
	visible.value = true;
	cgCodeGysApi.cgCodeGysList().then((res) => {
		gysInfo.value = res
	})
	 searchFormState = record;
	// searchFormState.bmName = record.bmName;
	// searchFormState.sqr = record.sqr;
	// searchFormState.sqrq = record.sqrq;
	// searchFormState.cglx = record.cglx;
	// console.log(record)
	console.log(record)
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
			selectedRowKeys.value = selectedRows;
		}
	}
};
const loadData = (parameter) => {
	const searchFormParam = JSON.parse(JSON.stringify(searchFormState));
	return cgJhSpmxApi.cghzPage(Object.assign(parameter, searchFormParam)).then((data) => {
		return data;
	});
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
const updateGys = (record) => {
	gysInfo.value.forEach(gys=>{
		if(gys.gysdm==record.gysdm){
			record.gysmc=gys.gysmc
		}
	})
	let param=searchFormState;
	param.bmdm=record.bmdm
	param.spdm=record.spdm
	param.gysdm=record.gysdm
	param.gysmc=record.gysmc
	console.log(param)
	//
	cgJhSpmxApi.updateGysHz( param).then((data) => {
		return data;
	});
}
const editGys = (record) => {
	record.forEach(gysdm=>{
		gysdm.gysdm=gys.value.gysdm
		updateGys(gysdm)
	})
}
const spining = ref(false)
const submit =() =>{
	spining.value=true
	if(spining.value){
		cgJhDhdApi.cgJhDhdSubmitForm(searchFormState).then(() => {
			spining.value=false
			emit('successful')
			handleClose()
		})
	}

}
const handleClose = () => {
	visible.value = false
}
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

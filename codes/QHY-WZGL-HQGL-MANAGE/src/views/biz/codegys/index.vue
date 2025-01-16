<template>

	<a-card :bordered="false">
		<a-form ref="searchFormRef" name="advanced_search" :model="searchFormState" class="ant-advanced-search-form">
			<a-row :gutter="24">
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
					<a-form-item label="供应商名称" name="gysmc">
						<a-input v-model:value="searchFormState.gysmc" placeholder="请输入供应商名称" />
					</a-form-item>
				</a-col>
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
					<a-form-item label="拼音简码" name="pyjm">
						<a-input v-model:value="searchFormState.pyjm" placeholder="请输入拼音简码" />
					</a-form-item>
				</a-col>
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
					<a-form-item label="供应商类别" name="gyslb">
						<a-select v-model:value="searchFormState.gyslb" placeholder="请选择供应商类别" :options="gyslbOptions" />
					</a-form-item>
				</a-col>
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24" v-show="advanced">
					<a-form-item label="信誉度" name="xyd">
						<a-select v-model:value="searchFormState.xyd" placeholder="请选择信誉度" :options="xydOptions" />
					</a-form-item>
				</a-col>
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24" v-show="advanced">
					<a-form-item label="经营范围" name="jyfw">
						<a-input v-model:value="searchFormState.jyfw" placeholder="请输入经营范围" />
					</a-form-item>
				</a-col>
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24" v-show="advanced">
					<a-form-item label="供货状态" name="ghzt">
						<a-select v-model:value="searchFormState.ghzt" placeholder="请选择供货状态" :options="ghztOptions" />
					</a-form-item>
				</a-col>
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
					<a-form-item >
					<a-button type="primary" @click="table.refresh(true)">查询</a-button>
					<a-button style="margin: 0 8px" @click="reset">重置</a-button>
					<a @click="toggleAdvanced" style="margin-left: 8px">
						{{ advanced ? '收起' : '展开' }}
						<component :is="advanced ? 'up-outlined' : 'down-outlined'"/>
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
					<a-button type="primary" @click="formRef.onOpen()" v-if="hasPerm('cgCodeGysAdd')">
						<template #icon><plus-outlined /></template>
						新增
					</a-button>
					<xn-batch-delete
						v-if="hasPerm('cgCodeGysBatchDelete')"
						:selectedRowKeys="selectedRowKeys"
						@batchDelete="deleteBatchCgCodeGys"
					/>
				</a-space>
			</template>
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'gyslb'">
					{{ $TOOL.dictTypeData('供应商类别', record.gyslb) }}
				</template>
				<template v-if="column.dataIndex === 'xyd'">
					{{ $TOOL.dictTypeData('信誉度', record.xyd) }}
				</template>
				<template v-if="column.dataIndex === 'ghzt'">
					{{ $TOOL.dictTypeData('供货状态', record.ghzt) }}
				</template>
				<template v-if="column.dataIndex === 'action'">

						<a @click="genQrcode(record)" >二维码</a>
						<a-divider type="vertical" v-if="hasPerm(['cgCodeGysEdit', 'cgCodeGysDelete'], 'and')" />
						<a @click="formRef.onOpen(record)" v-if="hasPerm('cgCodeGysEdit')">编辑</a>
						<a-divider type="vertical" v-if="hasPerm(['cgCodeGysEdit', 'cgCodeGysDelete'], 'and')" />
						<a-popconfirm title="确定要删除吗？" @confirm="deleteCgCodeGys(record)">
							<a type="link" danger size="small" v-if="hasPerm('cgCodeGysDelete')">删除</a>
						</a-popconfirm>
						<a-divider type="vertical"  />
						<a-badge :count="record.count" :overflow-count="9">
							<a @click="prcForm.onOpen(record)" v-if="hasPerm('cgCodeGysEdit')">电子档案</a>
						</a-badge>
					<a-divider type="vertical"></a-divider>
					<a-badge :count="record.count" :overflow-count="9">
						<a @click="conForm.onOpen(record)" v-if="hasPerm('cgCodeGysEdit')">合同</a>
					</a-badge>
<!--						<a-badge :count="record.count" :overflow-count="9">-->
<!--							<a @click="conForm.onOpen(record)" v-if="hasPerm('cgCodeGysEdit')">合同信息</a>-->
<!--						</a-badge>-->

				</template>
			</template>
		</s-table>
	</a-card>
	<Form ref="formRef" @successful="table.refresh(true)" />
	<prc-form ref="prcForm" @successful="table.refresh(true)" />
	<con-form ref="conForm" @successful="table.refresh(true)" />
	<a-modal v-model:visible="visible" title="二维码">
		<template #footer>
			{{null}}
		</template>
		<vue-qrcode :value="url" :options="{ width: 300 }"></vue-qrcode>
	</a-modal>
</template>

<script setup name="codegys">
import tool from '@/utils/tool'
import Form from './form.vue'
import PrcForm from '@/views/biz/gysprc/index.vue'
import ConForm from '@/views/biz/gyscontract/index.vue'
import cgCodeGysApi from '@/api/biz/cgCodeGysApi'
let searchFormState = reactive({})
const searchFormRef = ref()
const table = ref()
const formRef = ref()
const prcForm = ref()
const conForm = ref()
const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }
// 查询区域显示更多控制
const advanced = ref(false)
const visible = ref(false)
const toggleAdvanced = () => {
	advanced.value = !advanced.value
}
import VueQrcode from '@chenfengyuan/vue-qrcode'
const app = createApp({})
app.component(VueQrcode.name, VueQrcode)
const columns = [
	{
		title: '供应商代码',
		dataIndex: 'gysdm',
		width:80,
		ellipsis:true
	},
	{
		title: '供应商名称',
		dataIndex: 'gysmc'
	},
	{
		title: '联系人',
		dataIndex: 'lxr',
		width:80,
		ellipsis:true
	},
	{
		title: '联系电话',
		dataIndex: 'dh',
		width:120,
		ellipsis:true
	},
	{
		title: '传真',
		dataIndex: 'cz',
		width:100,
		ellipsis:true
	},
	{
		title: '拼音',
		dataIndex: 'pyjm',
		width:50,
		ellipsis:true
	},
	{
		title: '类别',
		dataIndex: 'gyslb',
		width:50,
		ellipsis:true
	},
	{
		title: '状态',
		dataIndex: 'ghzt',
		width:50,
		ellipsis:true
	},
	// {
	// 	title: '供应商显示顺序',
	// 	dataIndex: 'gysxh'
	// },
	//
	// {
	// 	title: '信誉度（A、B、C）',
	// 	dataIndex: 'xyd'
	// },
	// {
	// 	title: '法人代表',
	// 	dataIndex: 'frdb'
	// },
	// {
	// 	title: '注册资本',
	// 	dataIndex: 'zczb'
	// },
	// {
	// 	title: '地址',
	// 	dataIndex: 'dz'
	// },
	// {
	// 	title: '邮编',
	// 	dataIndex: 'yb'
	// },
	// {
	// 	title: '开户银行',
	// 	dataIndex: 'khyh'
	// },
	// {
	// 	title: '银行帐号',
	// 	dataIndex: 'yhzh'
	// },
	//
	//
	// {
	// 	title: 'Email',
	// 	dataIndex: 'email'
	// },
	// {
	// 	title: '网址',
	// 	dataIndex: 'www'
	// },
	// {
	// 	title: '经营范围',
	// 	dataIndex: 'jyfw'
	// },
	//
	// {
	// 	title: '设置日期',
	// 	dataIndex: 'szrq'
	// },
	// {
	// 	title: 'GYSPASSWD',
	// 	dataIndex: 'gyspasswd'
	// },
	// {
	// 	title: 'BZ',
	// 	dataIndex: 'bz'
	// },
]
// 操作栏通过权限判断是否显示

	columns.push({
		title: '操作',
		dataIndex: 'action',
		align: 'center',
		width: '280px'
	})

const selectedRowKeys = ref([])
// 列表选择配置
const options = {
	// columns数字类型字段加入 needTotal: true 可以勾选自动算账
	alert: {
		show: true,
		clear: () => {
			selectedRowKeys.value = ref([])
		}
	},
	rowSelection: {
		onChange: (selectedRowKey, selectedRows) => {
			selectedRowKeys.value = selectedRowKey
		}
	}
}
const loadData = (parameter) => {
	const searchFormParam = JSON.parse(JSON.stringify(searchFormState))
	return cgCodeGysApi.cgCodeGysPage(Object.assign(parameter, searchFormParam)).then((data) => {
		return data
	})
}
// 重置
const reset = () => {
	searchFormRef.value.resetFields()
	table.value.refresh(true)
}
// 删除
const deleteCgCodeGys = (record) => {
	let params = [
		{
			id: record.id
		}
	]
	cgCodeGysApi.cgCodeGysDelete(params).then(() => {
		table.value.refresh(true)
	})
}
// 批量删除
const deleteBatchCgCodeGys = (params) => {
	cgCodeGysApi.cgCodeGysDelete(params).then(() => {
		table.value.clearRefreshSelected()
	})
}
const url = ref('')
const genQrcode = (record) =>{

	url.value=import.meta.env.VITE_FRONT_BASEURL+'/supplierInfo?id='+record.id
	visible.value=true

}
const gyslbOptions = tool.dictList('供应商类别')
const xydOptions = tool.dictList('信誉度')
const ghztOptions = tool.dictList('供货状态')
</script>

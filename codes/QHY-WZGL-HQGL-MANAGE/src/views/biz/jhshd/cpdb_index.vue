<template>
	<a-card :bordered="false">
		<a-form ref="searchFormRef" name="advanced_search" :model="searchFormState" class="ant-advanced-search-form">
			<a-row :gutter="24">
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
					<a-form-item label="需货部门" name="bmdm">
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
						</a-tree-select
					></a-form-item>
				</a-col>
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
					<a-form-item label="调拨类型" name="bz">
						<a-select v-model:value="searchFormState.bz" placeholder="请输入出库类型">
							<a-select-option
								v-for="(item, index) in cklxOptions"
								:key="index"
								:label="item"
								:value="item"
							></a-select-option>
						</a-select>
					</a-form-item>
				</a-col>
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
					<a-form-item label="审核日期" name="shrq">
						<a-range-picker v-model:value="searchFormState.shrq" value-format="YYYY-MM-DD" />
					</a-form-item>
				</a-col>
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24" v-show="advanced">
					<a-form-item label="收货单号" name="shdh">
						<a-input
							v-model:value="searchFormState.shdh"
							placeholder="请输入单据编号(4位年+2位月+2位日+4位部门号+4位流水号)"
						/>
					</a-form-item>
				</a-col>
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24" v-show="advanced">
					<a-form-item label="审核人" name="shry">
						<a-input v-model:value="searchFormState.shry" placeholder="请输入审核人" />
					</a-form-item>
				</a-col>
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24" v-show="advanced">
					<a-form-item label="供货部门" name="gysmc">
						<a-tree-select
							v-model:value="searchFormState.gysdm"
							show-search
							tree-node-filter-prop="name"
							style="width: 100%"
							:dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
							placeholder="请选择部门名称"
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
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24" v-show="advanced">
					<a-form-item label="状态" name="workstate">
						<a-input v-model:value="searchFormState.workstate" placeholder="请输入状态（已收货、提交结算）" />
					</a-form-item>
				</a-col>
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
					<a-form-item>
						<a-button type="primary" @click="table.refresh(true)">查询</a-button>
						<a-button style="margin: 0 8px" @click="reset">重置</a-button>
						<a @click="toggleAdvanced" style="margin-left: 8px">
							{{ advanced ? '收起' : '展开' }}
							<component :is="advanced ? 'up-outlined' : 'down-outlined'" />
						</a>
					</a-form-item>
				</a-col>
			</a-row>
		</a-form>
		<s-table ref="table" :columns="columns" :data="loadData" bordered :row-key="(record) => record.id">
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'action'">
					<a-space>
						<a @click="formRef.onOpen(record)">明细</a>
						<!-- <a-divider type="vertical" />
						<a @click="print(record)">打印</a> -->
					</a-space>
				</template>
			</template>
		</s-table>
	</a-card>
	<mxIndex ref="formRef" @successful="table.refresh(true)" />
	<a-modal v-model:visible="visible" title="打印" width="100%" wrap-class-name="full-modal" :footer="null">
		<iframe :src="src" width="100%" class="print-iframe" frameborder="0"></iframe>
	</a-modal>
</template>

<script setup name="jhshd">
	import Form from './form.vue'
	import mxIndex from './cpdbmx_index.vue'
	import cgJhShdApi from '@/api/biz/cgJhShdApi'
	import bizOrgApi from '@/api/biz/bizOrgApi'
	import tool from '@/utils/tool'
	let searchFormState = reactive({})
	const visible = ref(false)
	const src = ref()
	const searchFormRef = ref()
	const table = ref()
	const treeData = ref()
	const formRef = ref()
	const cklxOptions = ref(['显示全部', '班组', '部门'])
	const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }
	// 查询区域显示更多控制
	const advanced = ref(false)
	const toggleAdvanced = () => {
		advanced.value = !advanced.value
	}
	const columns = [
		{
			title: '收货单号',
			dataIndex: 'shdh'
		},
		{
			title: '采购类型',
			dataIndex: 'cglx'
		},
		{
			title: '需货部门',
			dataIndex: 'bmName'
		},
		{
			title: '审核日期',
			dataIndex: 'shrq'
		},
		{
			title: '供货部门',
			dataIndex: 'gysmc'
		},
		{
			title: '商品金额',
			dataIndex: 'spje'
		},

		{
			title: '审核人',
			dataIndex: 'shry'
		},
		{
			title: '验货人',
			dataIndex: 'yhr'
		},
		{
			title: '状态',
			dataIndex: 'workstate'
		}
	]
	// 操作栏通过权限判断是否显示

	columns.push({
		title: '操作',
		dataIndex: 'action',
		align: 'center',
		width: '150px'
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
		// shrq范围查询条件重载
		if (searchFormParam.shrq) {
			searchFormParam.startShrq = searchFormParam.shrq[0]
			searchFormParam.endShrq = searchFormParam.shrq[1]
			delete searchFormParam.shrq
		}
		return cgJhShdApi.cgJhCpdbPage(Object.assign(parameter, searchFormParam)).then((data) => {
			return data
		})
	}
	// 重置
	const reset = () => {
		searchFormRef.value.resetFields()
		table.value.refresh(true)
	}
	// 删除
	const deleteCgJhShd = (record) => {
		let params = [
			{
				id: record.id
			}
		]
		cgJhShdApi.cgJhShdDelete(params).then(() => {
			table.value.refresh(true)
		})
	}
	// 批量删除
	const deleteBatchCgJhShd = (params) => {
		cgJhShdApi.cgJhShdDelete(params).then(() => {
			table.value.clearRefreshSelected()
		})
	}
	const userInfo = ref(tool.data.get('USER_INFO'))

	const initOrg = () => {
		bizOrgApi.orgTree().then((res) => {
			treeData.value = res
		})
		searchFormState.bmdm = userInfo.value.orgId
	}

	initOrg()
	const print = (record) => {
		visible.value = true

		const userInfo = tool.data.get('USER_INFO')

		if (userInfo.orgName.includes('幼儿园')) {
			src.value =
				'https://hqncg.just.edu.cn/decision/view/report?viewlet=cgjkd%252Fdjdy%252Fcgcpdbda4.cpt&id=' + record.id
		} else {
			src.value =
				'https://hqncg.just.edu.cn/decision/view/report?viewlet=cgjkd%252Fdjdy%252Fcgcpdbd.cpt&id=' + record.id
		}
	}
</script>

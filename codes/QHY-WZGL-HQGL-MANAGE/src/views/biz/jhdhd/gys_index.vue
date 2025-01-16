<template>
	<a-card :bordered="false">
		<a-form ref="searchFormRef" name="advanced_search" :model="searchFormState" class="ant-advanced-search-form">
			<a-row :gutter="24">
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
					<a-form-item label="采购单号" name="cgdh">
						<a-input v-model:value="searchFormState.cgdh" placeholder="请输入采购单号(4位年+2位月+2位日+4位流水号)" />
					</a-form-item>
				</a-col>
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
					<a-form-item label="送货日期" name="cgrq">
						<a-range-picker v-model:value="searchFormState.cgrq" value-format="YYYY-MM-DD" />
					</a-form-item>
				</a-col>
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
					<a-form-item label="订货人" name="dhr">
						<a-input v-model:value="searchFormState.dhr" placeholder="请输入订货人" />
					</a-form-item>
				</a-col>
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24" v-show="advanced">
					<a-form-item label="订货日期" name="dhrq">
						<a-range-picker v-model:value="searchFormState.dhrq" value-format="YYYY-MM-DD" />
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
		<s-table
			ref="table"
			:columns="columns"
			:data="loadData"
			bordered
			:row-key="(record) => record.id"
			:scroll="{ x: 1400 }"
		>
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'action'">
					<a-space>
						<a v-if="record.cglx === '部门备货'" @click="formRef.onOpen(record)">明细</a>
						<a v-if="record.cglx === '班组订货'" @click="bzformRef.onOpen(record)">明细</a>
						<!-- <a-divider type="vertical" />
						<a @click="print(record)">打印</a>
						<a-divider type="vertical" />
						<a v-if="record.cglx === '部门备货'" @click="hzprint(record)">汇总打印</a>
						<a v-if="record.cglx === '班组订货'" @click="bzhzprint(record)">汇总打印</a> -->
					</a-space>
				</template>
			</template>
		</s-table>
	</a-card>
	<bmmxIndex ref="formRef" @successful="table.refresh(true)" />
	<bzmxIndex ref="bzformRef" @successful="table.refresh(true)" />
	<a-modal v-model:visible="visible" title="打印" width="100%" wrap-class-name="full-modal" :footer="null">
		<iframe :src="src" width="100%" class="print-iframe" frameborder="0"></iframe>
	</a-modal>
	<a-modal v-model:visible="hzvisible" title="打印" width="100%" wrap-class-name="full-modal" :footer="null">
		<iframe :src="src" width="100%" class="print-iframe" frameborder="0"></iframe>
	</a-modal>
</template>

<script setup name="jhdhd">
	import cgJhDhdApi from '@/api/biz/cgJhDhdApi'
	import bmmxIndex from './gys_bmmx_index.vue'
	import bzmxIndex from './gys_bzmx_index.vue'
	import gysApi from '@/api/biz/gysApi'
	import tool from '@/utils/tool'
	let searchFormState = reactive({})
	const searchFormRef = ref()
	const table = ref()
	const formRef = ref()
	const bzformRef = ref()
	const visible = ref(false)
	const hzvisible = ref(false)
	const src = ref()
	const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }
	// 查询区域显示更多控制
	const advanced = ref(false)
	const userInfo = tool.data.get('USER_INFO')
	searchFormState.gysdm = userInfo.gysdm
	const toggleAdvanced = () => {
		advanced.value = !advanced.value
	}
	const columns = [
		{
			title: '采购单号',
			dataIndex: 'cgdh'
		},
		{
			title: '送货日期',
			dataIndex: 'cgrq'
		},
		{
			title: '订货人',
			dataIndex: 'dhr'
		},
		{
			title: '订货日期',
			dataIndex: 'dhrq'
		},
		{
			title: '供应商查看时间',
			dataIndex: 'gysqrrq'
		},

		{
			title: '商品金额（元）',
			dataIndex: 'spje'
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
		width: '250px'
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
		// cgrq范围查询条件重载
		if (searchFormParam.cgrq) {
			searchFormParam.startCgrq = searchFormParam.cgrq[0]
			searchFormParam.endCgrq = searchFormParam.cgrq[1]
			delete searchFormParam.cgrq
		}
		// dhrq范围查询条件重载
		if (searchFormParam.dhrq) {
			searchFormParam.startDhrq = searchFormParam.dhrq[0]
			searchFormParam.endDhrq = searchFormParam.dhrq[1]
			delete searchFormParam.dhrq
		}
		return gysApi.cgGysDhdPage(Object.assign(parameter, searchFormParam)).then((data) => {
			return data
		})
	}
	// 重置
	const reset = () => {
		searchFormRef.value.resetFields()
		table.value.refresh(true)
	}
	// 删除
	const deleteCgJhDhd = (record) => {
		let params = [
			{
				id: record.id
			}
		]
		cgJhDhdApi.cgJhDhdDelete(params).then(() => {
			table.value.refresh(true)
		})
	}
	// 批量删除
	const deleteBatchCgJhDhd = (params) => {
		cgJhDhdApi.cgJhDhdDelete(params).then(() => {
			table.value.clearRefreshSelected()
		})
	}
	//更新接受时间
	const updateTime = (params) => {
		gysApi.updateTime(params).then(() => {
			table.value.clearRefreshSelected()
		})
	}

	const print = (record) => {
		visible.value = true
		src.value = 'https://hqncg.just.edu.cn/decision/view/report?viewlet=cgjkd%252Fdjdy%252Fcgdhd.cpt&id=' + record.id
	}
	const hzprint = (record) => {
		hzvisible.value = true
		src.value = 'https://hqncg.just.edu.cn/decision/view/report?viewlet=cgjkd%252Fdjdy%252Fdhdbm.cpt&id=' + record.id
	}
	const bzhzprint = (record) => {
		hzvisible.value = true
		src.value = 'https://hqncg.just.edu.cn/decision/view/report?viewlet=cgjkd%252Fdjdy%252Fdhdhz.cpt&id=' + record.id
	}
</script>

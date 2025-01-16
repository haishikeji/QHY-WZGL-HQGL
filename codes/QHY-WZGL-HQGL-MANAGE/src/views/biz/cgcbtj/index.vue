<template>
	<a-card :bordered="false">
		<a-form ref="searchFormRef" name="advanced_search" :model="searchFormState" class="ant-advanced-search-form">
			<a-row :gutter="24">
				<a-col :span="9">
					<a-form-item label="月份范围" name="sqrq">
						<a-range-picker v-model:value="searchFormState.sqrq" picker="month" value-format="YYYY-MM" />
					</a-form-item>
				</a-col>

				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
					<a-form-item>
						<a-button type="primary" @click="table.refresh(true)">查询</a-button>
						<a-button style="margin: 0 8px" @click="reset">重置</a-button>
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
		>
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'action'">
					<a-space>
						<a @click="formRef.onOpen(record)">明细</a>
						<a-divider type="vertical" />
						<a @click="formBmRef.onOpen(record)">按部门统计</a>
						<!-- <a-divider type="vertical" />
						<a @click="print(record)">打印</a> -->
					</a-space>
				</template>
				<template v-if="column.dataIndex === 'ylje'">
					{{ NP.minus(record.gyje, record.jhje) }}
				</template>
				<template v-if="column.dataIndex === 'shrq'">
					{{ record.shrq.substring(0, 7) }}
				</template>
			</template>
			<template #summary>
				<a-table-summary fixed>
					<a-table-summary-row>
						<a-table-summary-cell
							v-for="(item, index) in columns"
							:key="index"
							:index="1"
							:style="{ 'text-align': 'right' }"
						>
							<template v-if="item?.summary">
								<a-typography-text>
									{{ combinedNums['total' + item.dataIndex] }}
								</a-typography-text>
							</template>
							<template v-if="item?.summaryTitle">
								<a-typography-text>
									{{ '合计' }}
								</a-typography-text>
							</template>
						</a-table-summary-cell>
					</a-table-summary-row>
				</a-table-summary>
			</template>
		</s-table>
	</a-card>
	<mxIndex ref="formRef" @successful="table.refresh(true)" />
	<bmmxIndex ref="formBmRef" @successful="table.refresh(true)" />
	<a-modal v-model:visible="visible" title="打印" width="100%" wrap-class-name="full-modal" :footer="null">
		<iframe :src="src" width="100%" class="print-iframe" frameborder="0"></iframe>
	</a-modal>
</template>

<script setup name="jhsqd">
	import Form from './form.vue'
	import mxIndex from './mx_index.vue'
	import bmmxIndex from './bmmx_index.vue'
	import NP from 'number-precision'
	import cgJhSqdApi from '@/api/biz/cgJhSqdApi'
	import cgJhSpmxApi from '@/api/biz/cgJhSpmxApi'
	import tool from '@/utils/tool'
	import sysConfig from '@/config'

	let searchFormState = reactive({})
	const searchFormRef = ref()
	const table = ref()
	const formRef = ref()
	const formBmRef = ref()
	const cglx = ref(['班组订货', '部门备货'])
	const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }
	// 查询区域显示更多控制
	const advanced = ref(false)
	const toggleAdvanced = () => {
		advanced.value = !advanced.value
	}
	const columns = [
		{
			title: '月份',
			dataIndex: 'shrq',
			align: 'right',
			summaryTitle: true
		},
		{
			title: '采购金额',
			dataIndex: 'jhje',
			align: 'right',
			summary: true
		},
		{
			title: '供应金额',
			dataIndex: 'gyje',
			align: 'right',
			summary: true
		},
		{
			title: '盈利金额',
			dataIndex: 'ylje',
			align: 'right',
			summary: true
		}
	]
	// 操作栏通过权限判断是否显示

	columns.push({
		title: '操作',
		dataIndex: 'action',
		align: 'center'
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
	const tableData = ref()
	const loadData = (parameter) => {
		const searchFormParam = JSON.parse(JSON.stringify(searchFormState))
		// sqrq范围查询条件重载
		if (searchFormParam.sqrq) {
			searchFormParam.startSqrq = searchFormParam.sqrq[0]
			searchFormParam.endSqrq = searchFormParam.sqrq[1]
			delete searchFormParam.sqrq
		}
		return cgJhSpmxApi.cgCbTjPage(Object.assign(parameter, searchFormParam)).then((data) => {
			tableData.value = data.records
			return data
		})
	}
	// 重置
	const reset = () => {
		searchFormRef.value.resetFields()
		table.value.refresh(true)
	}
	// 删除
	const auditCgJhSqd = (record) => {
		let params = [
			{
				id: record.id
			}
		]
		cgJhSqdApi.cgJhSqdAudit(params).then(() => {
			table.value.refresh(true)
		})
	}
	// 批量删除
	const auditBatchCgJhSqd = (params) => {
		cgJhSqdApi.cgJhSqdAudit(params).then(() => {
			table.value.clearRefreshSelected()
		})
	}
	const visible = ref(false)
	const src = ref()
	const print = (record) => {
		visible.value = true

		src.value =
			sysConfig.PRINT_URL +
			'/view/report?viewlet=cgjkd%252Fcwjs%252Fabmtj.cpt&rq1=' +
			record.shrq.substring(0, 7) +
			'&rq2=' +
			record.shrq.substring(0, 7)
	}

	const combinedNums = computed(() => {
		let totaljhje = 0
		let totalgyje = 0
		let totalylje = 0
		if (tableData.value) {
			tableData.value.forEach((spmxItem) => {
				totaljhje = NP.plus(totaljhje, spmxItem.jhje)
				totalgyje = NP.plus(totalgyje, spmxItem.gyje)
				totalylje = NP.minus(totalgyje, totaljhje)
			})
		}
		return {
			totaljhje,
			totalgyje,
			totalylje
		}
	})
</script>

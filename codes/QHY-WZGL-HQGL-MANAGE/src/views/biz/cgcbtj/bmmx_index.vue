<template>
	<a-modal
		v-model:visible="visible"
		title="明细"
		width="100%"
		:mask-closable="false"
		wrap-class-name="full-modal"
		:destroy-on-close="true"
	>
		<template #footer>
			{{ null }}
		</template>

		<s-table
			ref="table"
			:columns="columns"
			:data="loadData"
			bordered
			:row-key="(record) => record.id"
			:scroll="{ x: 1300 }"
		>
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'ylje'">
					{{ NP.minus(record.gyje, record.jhje) }}
				</template>
				<!-- <template v-if="column.dataIndex === 'action'">
					<a-space>
						<a @click="print(record)">打印</a>
					</a-space>
				</template> -->
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

		<Form ref="formRef" @successful="table.refresh(true)" />
		<a-modal v-model:visible="printVisible" title="打印" width="100%" wrap-class-name="full-modal" :footer="null">
			<iframe :src="src" width="100%" class="print-iframe" frameborder="0"></iframe>
		</a-modal>
	</a-modal>
</template>

<script setup name="jhsqd">
	import Form from './form.vue'
	import cgJhSqdApi from '@/api/biz/cgJhSqdApi'
	import cgJhSpmxApi from '@/api/biz/cgJhSpmxApi'
	import NP from 'number-precision'
	import sysConfig from '@/config'

	let searchFormState = reactive({})
	const searchFormRef = ref()
	const table = ref()
	const visible = ref(false)
	const printVisible = ref(false)
	const formRef = ref()
	const cglx = ref(['班组订货', '部门备货'])
	const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }
	// 查询区域显示更多控制
	const advanced = ref(false)
	const toggleAdvanced = () => {
		advanced.value = !advanced.value
	}
	const columns = [
		{
			title: '部门',
			dataIndex: 'bmmc',
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
	columns.push({
		title: '操作',
		dataIndex: 'action',
		align: 'left',
		width: '150px'
	})
	const selectedRowKeys = ref([])
	const src = ref()
	const onOpen = (record) => {
		visible.value = true
		searchFormState.shrq = record.shrq
	}
	// 关闭抽屉
	const onClose = () => {
		formData.value = {}
		visible.value = false
	}
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
		return cgJhSpmxApi.cgJhSpmxAbmPage(Object.assign(parameter, searchFormParam)).then((data) => {
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
	// 抛出函数
	defineExpose({
		onOpen
	})
	const combinedNums = computed(() => {
		let totaljhje = 0
		let totalgyje = 0
		let totalylje = 0
		if (tableData.value) {
			tableData.value.forEach((spmxItem) => {
				totaljhje = NP.plus(totaljhje, spmxItem.jhje)

				totalgyje = NP.plus(totalgyje, spmxItem.gyje)

				totalylje = NP.plus(totalylje, NP.minus(spmxItem.gyje, spmxItem.jhje))
			})
		}
		return {
			totaljhje,
			totalgyje,
			totalylje
		}
	})
	const print = (record) => {
		printVisible.value = true
		//src.value=sysConfig.PRINT_URL+'/view/report?viewlet=cgjkd%252Fdjdy%252Fcgshd.cpt&id='+record.id
		src.value =
			sysConfig.PRINT_URL +
			'/view/report?viewlet=cgjkd%252Fcwtj%252Fbmcgqd.cpt&bmdm=' +
			record.bmdm +
			'&yf=' +
			searchFormState.shrq.substring(0, 7)
	}
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
		}

		.ant-modal-body {
			flex: 1;
		}
	}
</style>

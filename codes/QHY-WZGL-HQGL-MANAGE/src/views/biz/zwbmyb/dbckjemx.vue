<template>
	<a-modal
		v-model:visible="visible"
		title="库存调出明细"
		width="100%"
		:mask-closable="false"
		wrap-class-name="full-modal"
		:destroy-on-close="true"
	>
		<template #footer>
			{{ null }}
		</template>
		<a-card>
			<s-table
				ref="table"
				:columns="columns"
				:data="loadData"
				bordered
				:row-key="(record) => record.id"
				:scroll="{ x: 1000 }"
			>
				<!-- <template #bodyCell="{ column, record }">
					<template v-if="column.dataIndex === 'action'">
						<a-space>
							<a @click="cpdbPrint(record)" v-if="record.cglx==='成品调拨'">打印</a>
							<a @click="bmdbPrint(record)" v-if="record.cglx==='部门调拨'">打印</a>
						</a-space>
					</template>
				</template> -->
			</s-table>
		</a-card>
		<a-modal v-model:visible="printVisible" title="打印" width="100%" wrap-class-name="full-modal" :footer="null">
			<iframe :src="src" width="100%" class="print-iframe" frameborder="0"></iframe>
		</a-modal>
	</a-modal>
</template>

<script setup name="zwbzybmx">
	import cgZwBmybApi from '@/api/biz/cgZwBmybApi'
	import { cloneDeep } from 'lodash-es'

	const visible = ref(false)
	const formData = ref({})
	const table = ref()
	const formRef = ref()
	const printVisible = ref(false)
	const src = ref()
	const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }
	// 查询区域显示更多控制
	const advanced = ref(false)
	const toggleAdvanced = () => {
		advanced.value = !advanced.value
	}
	const columns = [
		{
			title: '收货部门',
			dataIndex: 'bmName'
		},
		{
			title: '调出金额',
			dataIndex: 'gyje'
		},
		{
			title: '调拨类型',
			dataIndex: 'cglx'
		}
	]
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

	columns.push({
		title: '操作',
		dataIndex: 'action',
		align: 'left',
		width: '150px'
	})

	const data = ref({})

	const lxData = ref()

	// 打开抽屉
	const onOpen = (record, lx) => {
		lxData.value = lx
		visible.value = true
		data.value = record
		if (record) {
			let recordData = cloneDeep(record)
			formData.value = Object.assign({}, recordData)
		}
	}

	// 关闭抽屉
	const onClose = () => {
		formRef.value.resetFields()
		formData.value = {}
		visible.value = false
	}

	const loadData = (parameter) => {
		parameter.bmdm = data.value.bmdm
		parameter.ybbh = data.value.ybbh
		parameter.yblx = lxData.value
		if (parameter.yblx === 'dbckje') {
			let data = {
				records: [],
				total: 0,
				size: 10,
				current: 1,
				orders: [],
				optimizeCountSql: true,
				searchCount: true,
				maxLimit: null,
				countId: null,
				pages: 1
			}
			//库存调出
			return cgZwBmybApi.dbckjePage(parameter).then((list) => {
				if (list) {
					data.records = list
					data.total = 1
					return Promise.resolve(data)
				} else {
					return Promise.resolve(data)
				}
			})
		}
	}
	const cpdbPrint = (record) => {
		printVisible.value = true
		src.value =
			'https://hqncg.just.edu.cn/decision/view/report?viewlet=cgjkd%252Fdjdy%252Fcpdbrk.cpt&rkbm=' +
			record.bmdm +
			'&ckbm=' +
			formData.value.bmdm +
			'&yf=' +
			formData.value.ybbh
		console.log(src.value)
	}

	const bmdbPrint = (record) => {
		printVisible.value = true
		src.value =
			'https://hqncg.just.edu.cn/decision/view/report?viewlet=cgjkd%252Fdjdy%252Fkcdbrk.cpt&rkbm=' +
			record.bmdm +
			'&ckbm=' +
			formData.value.bmdm +
			'&yf=' +
			formData.value.ybbh
		console.log(src.value)
	}

	// 重置
	const reset = () => {
		searchFormRef.value.resetFields()
		table.value.refresh(true)
	}

	// 抛出函数
	defineExpose({
		onOpen
	})
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

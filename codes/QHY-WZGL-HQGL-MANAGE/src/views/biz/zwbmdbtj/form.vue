<template>
	<a-modal
		v-model:visible="visible"
		title="按调入统计"
		width="100%"
		:mask-closable="false"
		wrap-class-name="full-modal"
		:destroy-on-close="true"
	>
		<template #footer>
			{{ null }}
		</template>
		<a-card>
			<a-table
				ref="table"
				:columns="columns"
				:data-source="tableData"
				bordered
				:row-key="(record) => record.id"
				:scroll="{ x: 1000 }"
			>
				<!-- <template #bodyCell="{ column, record }">
					<template v-if="column.dataIndex === 'action'">
						<a-space>
							<a @click="cpdbPrint(record)" v-if="record.cglx === '成品调拨'">打印</a>
							<a @click="bmdbPrint(record)" v-if="record.cglx === '部门调拨'">打印</a>
						</a-space>
					</template>
				</template> -->
			</a-table>
		</a-card>

		<a-modal v-model:visible="printVisible" title="打印" width="100%" wrap-class-name="full-modal" :footer="null">
			<iframe :src="src" width="100%" class="print-iframe" frameborder="0"></iframe>
		</a-modal>
	</a-modal>
</template>

<script setup name="zwbzybmx">
	import cgZwBmybApi from '@/api/biz/cgZwBmybApi'
	import { cloneDeep } from 'lodash-es'
	import cgJhSqdApi from '@/api/biz/cgJhSqdApi'
	import dayjs, { Dayjs } from 'dayjs'
	const printVisible = ref(false)
	const src = ref()

	const visible = ref(false)
	const formData = ref({})
	const table = ref()
	const formRef = ref()
	const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }
	// 查询区域显示更多控制
	const advanced = ref(false)
	const toggleAdvanced = () => {
		advanced.value = !advanced.value
	}
	const tableData = ref([])
	const columns = [
		{
			title: '供货部门',
			dataIndex: 'gysmc'
		},
		{
			title: '调入金额',
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

	const data = ref({})

	const lxData = ref()
	let searchFormState = reactive({})

	// 打开抽屉
	const onOpen = (record) => {
		visible.value = true

		loadData(record)
	}

	// 关闭抽屉
	const onClose = () => {
		formRef.value.resetFields()
		formData.value = {}
		visible.value = false
	}

	columns.push({
		title: '操作',
		dataIndex: 'action',
		align: 'center',
		width: '200px'
	})

	const loadData = (data) => {
		let parameter = {}
		parameter.bmdm = data.bmdm
		parameter.gysdm = data.gysdm
		if (data.shrq) {
			parameter.shrq = data.shrq.substring(0, 7)
		}

		return cgJhSqdApi.cgJhSqdCpdbPagedrtj(parameter).then((data) => {
			console.log(data)
			tableData.value = data
		})
	}

	const print = (record) => {
		printVisible.value = true

		// 假设 record.shrq 是一个格式为 'YYYY-MM-DD' 的字符串
		const time = dayjs(record.shrq).format('YYYY-MM-DD')
		console.log('******************time*************')
		console.log(time)

		// 将 record.shrq 转换为 Day.js 对象
		const date = dayjs(record.shrq)

		// 获取本月的最后一天
		const lastDayOfMonth = date.endOf('month').format('YYYY-MM-DD')
		console.log('******************lastDayOfMonth*')
		console.log(lastDayOfMonth)

		src.value =
			'https://hqncg.just.edu.cn/decision/view/report?viewlet=cgjkd%252Fcwtj%252Fcpdbbmhz.cpt&' +
			'rq1=' +
			time +
			'&rq2=' +
			lastDayOfMonth +
			'&rkbm=' +
			record.bmdm +
			'&ckbm=' +
			record.gysdm
	}

	const cpdbPrint = (record) => {
		printVisible.value = true
		const time = dayjs(record.shrq).format('YYYYMM')

		const date = dayjs(record.shrq)

		const lastDayOfMonth = date.endOf('month').format('YYYYMM')
		src.value =
			'https://hqncg.just.edu.cn/decision/view/report?viewlet=cgjkd%252Fdjdy%252Fcpdbrk.cpt&rkbm=' +
			record.bmdm +
			'&ckbm=' +
			record.gysdm +
			'&yf=' +
			time
		console.log(src.value)
	}

	const bmdbPrint = (record) => {
		printVisible.value = true
		const time = dayjs(record.shrq).format('YYYYMM')

		const date = dayjs(record.shrq)

		const lastDayOfMonth = date.endOf('month').format('YYYYMM')
		src.value =
			'https://hqncg.just.edu.cn/decision/view/report?viewlet=cgjkd%252Fdjdy%252Fkcdbrk.cpt&rkbm=' +
			record.bmdm +
			'&ckbm=' +
			record.gysdm +
			'&yf=' +
			time
		console.log(src.value)
	}
	// 重置
	const reset = () => {
		searchFormRef.value.resetFields()
		loadData(data)
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

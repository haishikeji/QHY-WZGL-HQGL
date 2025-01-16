<template>
	<a-card :bordered="false">
		<a-form ref="searchFormRef" name="advanced_search" :model="searchFormState" class="ant-advanced-search-form">
			<a-row :gutter="24">
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
					<a-form-item label="月报编号" name="rq">
						<a-range-picker picker="month" v-model:value="searchFormState.bh" value-format="YYYY-MM" />
					</a-form-item>
				</a-col>
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
					<a-form-item label="部门名称" name="bmmc">
						<!--						<a-input v-model:value="searchFormState.bmmc" placeholder="请输入部门名称" />-->
						<a-tree-select
							v-model:value="searchFormState.parentId"
							show-search
							tree-node-filter-prop="name"
							style="width: 100%"
							:dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
							placeholder="请选择部门名称"
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
					<a-form-item label="班组名称" name="ffbz">
						<a-select v-model:value="searchFormState.bzdm" placeholder="请输入班组" :options="bzInfo" allow-clear>
						</a-select>
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
			:scroll="{ x: 2200, y: 300 }"
		>
			<!-- <template #operator class="table-operator">
				<a-space>
					<a-button type="primary" @click="printBm">
						<template #icon>
							<printer-outlined />
						</template>
						部门打印
					</a-button>
					<a-button type="primary" @click="printFd">
						<template #icon>
							<printer-outlined />
						</template>
						上报财务打印
					</a-button>
				</a-space>
			</template> -->
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'action'">
					<a-space>
						<a @click="formRef.onOpen(record)">明细</a>
					</a-space>
				</template>

				<template v-if="spflList.indexOf(column.dataIndex) > -1">
					<a-space>
						<a @click="formRef.onOpen(record, column.dataIndex)">{{
							record[column.dataIndex] ? NP.round(record[column.dataIndex], 2) : 0
						}}</a>
					</a-space>
				</template>

				<template v-if="column.dataIndex === 'cpdbje'">
					<div @click="printCpdbsr(record)">
						<a-typography-text style="color: var(--primary-color); cursor: pointer">
							{{ record.cpdbje }}
						</a-typography-text>
					</div>
				</template>
				<template v-if="column.dataIndex === 'llxj'">
					<div @click="printLlxj(record)">
						<a-typography-text style="color: var(--primary-color); cursor: pointer">
							{{ record.llxj }}
						</a-typography-text>
					</div>
				</template>
			</template>
			<template #summary>
				<a-table-summary fixed>
					<a-table-summary-row>
						<a-table-summary-cell :index="0"></a-table-summary-cell>
						<a-table-summary-cell
							v-for="(item, index) in columns"
							:key="index"
							:index="1"
							:style="{ 'text-align': 'left' }"
						>
							<template v-if="item?.summary">
								<a-typography-text>
									{{ tableData && tableData.length > 0 ? NP.round(combinedNums['total'][item.dataIndex], 2) : 0 }}
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
	<Form ref="formRef" @successful="table.refresh(true)" />
	<lbmxFormRef ref="formRef" @successful="table.refresh(true)" />
	<a-modal v-model:visible="visible" title="打印" width="100%" wrap-class-name="full-modal" :footer="null">
		<iframe :src="src" width="100%" class="print-iframe" frameborder="0"></iframe>
	</a-modal>
</template>

<script setup name="zwbzyb">
	import Form from './mx_index.vue'
	import lbmxFormRef from './lbmx.vue'
	import cgZwBzybApi from '@/api/biz/cgZwBzybApi'
	import bizOrgApi from '@/api/biz/bizOrgApi'
	import cgZwBzrbApi from '@/api/biz/cgZwBzrbApi'
	import bizBzTreeApi from '@/api/biz/bizBzTreeApi'
	import tool from '@/utils/tool'
	import NP from 'number-precision'
	import dayjs from 'dayjs'
	let searchFormState = reactive({
		bh: [dayjs().add(-1, 'month').format('YYYY-MM'), dayjs().add(-1, 'month').format('YYYY-MM')]
	})
	const searchFormRef = ref()
	const table = ref()
	const formRef = ref()
	const visible = ref(false)
	const src = ref()
	let spflList = ref([])
	const treeData = ref([])
	const bzInfo = ref([])
	const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }
	// 查询区域显示更多控制
	const advanced = ref(false)
	const toggleAdvanced = () => {
		advanced.value = !advanced.value
	}

	spflList.value = ['hclje', 'sclje', 'tllje', 'zslje', 'yplje', 'dhclje', 'wxclje', 'qtlje']

	const columns = [
		{
			title: '月报编号',
			dataIndex: 'ybbh',
			summaryTitle: true,
			fixed: 'left',
			width: '60px'
		},
		{
			title: '班组名称',
			dataIndex: 'bzName',
			fixed: 'left',
			width: '100px'
		},
		{
			title: '荤菜类',
			dataIndex: 'hclje',
			summary: true,
			width: '60px'
		},
		{
			title: '素菜类',
			dataIndex: 'sclje',
			summary: true,
			width: '60px'
		},
		{
			title: '调料类',
			dataIndex: 'tllje',
			summary: true,
			width: '60px'
		},
		{
			title: '主食类',
			dataIndex: 'zslje',
			summary: true,
			width: '60px'
		},
		{
			title: '饮品类',
			dataIndex: 'yplje',
			summary: true,
			width: '60px'
		},
		{
			title: '低耗类',
			dataIndex: 'dhclje',
			summary: true,
			width: '60px'
		},
		{
			title: '维修材料类',
			dataIndex: 'wxclje',
			summary: true,
			width: '70px'
		},
		{
			title: '其它类',
			dataIndex: 'qtlje',
			summary: true,
			width: '60px'
		},
		{
			title: '领料小计',
			dataIndex: 'llxj',
			summary: true,
			width: '60px'
		},
		{
			title: '水电气类',
			dataIndex: 'sdqlje',
			summary: true,
			width: '60px'
		},
		{
			title: '维护费用类',
			dataIndex: 'dhlje',
			summary: true,
			width: '70px'
		},
		{
			title: '酬金类',
			dataIndex: 'cjlje',
			summary: true,
			width: '60px'
		},
		{
			title: '其他支出',
			dataIndex: 'qtzcje',
			summary: true,
			width: '60px'
		},
		{
			title: '营业收入',
			dataIndex: 'yysrje',
			summary: true,
			width: '60px'
		},
		{
			title: '成品调拨收入',
			dataIndex: 'cpdbje',
			summary: true,
			width: '80px'
		},

		{
			title: '其他收入',
			dataIndex: 'qtsrje',
			summary: true,
			width: '60px'
		},
		{
			title: '盈亏金额',
			dataIndex: 'ykje',
			summary: true,
			width: '60px'
		},

		{
			title: '登记人',
			dataIndex: 'czy',
			width: '60px'
		},
		{
			title: '登记日期',
			dataIndex: 'rq',
			width: '110px'
		},
		{
			title: '审核人',
			dataIndex: 'shry',
			width: '60px'
		},
		{
			title: '审核日期',
			dataIndex: 'shrq',
			width: '110px'
		},

		{
			title: '月报状态',
			dataIndex: 'workstate',
			width: '60px'
		}
	]
	// 操作栏通过权限判断是否显示
	if (hasPerm(['cgZwBzybEdit', 'cgZwBzybDelete'])) {
		columns.push({
			title: '操作',
			dataIndex: 'action',
			align: 'center',
			width: '150px'
		})
	}
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
		// rq范围查询条件重载
		if (searchFormParam.bh) {
			searchFormParam.startRq = searchFormParam.bh[0]
			searchFormParam.endRq = searchFormParam.bh[1]
			delete searchFormParam.bh
		}
		return cgZwBzybApi.cgZwBzybPage(Object.assign(parameter, searchFormParam)).then((data) => {
			if (data.records && data.records.length > 0) {
				data.records.forEach((item) => {
					tableData.value = data.records
					item.llxj = NP.plus(
						NP.round(item.hclje ? item.hclje : 0, 2),
						NP.round(item.sclje ? item.sclje : 0, 2),
						NP.round(item.tllje ? item.tllje : 0, 2),
						NP.round(item.zslje ? item.zslje : 0, 2),
						NP.round(item.yplje ? item.yplje : 0, 2),
						NP.round(item.dhclje ? item.dhclje : 0, 2),
						NP.round(item.wxclje ? item.wxclje : 0, 2),
						NP.round(item.qtlje ? item.qtlje : 0, 2)
					)
				})
			} else {
				tableData.value = []
			}

			return data
		})
	}
	// 重置
	const reset = () => {
		searchFormRef.value.resetFields()
		table.value.refresh(true)
	}
	// 删除
	const deleteCgZwBzyb = (record) => {
		let params = [
			{
				id: record.id
			}
		]
		cgZwBzybApi.cgZwBzybDelete(params).then(() => {
			table.value.refresh(true)
		})
	}
	// 删除
	const submitCgZwBzyb = (record) => {
		cgZwBzybApi.cgZwBzybSubmit(record).then(() => {
			table.value.refresh(true)
		})
	}
	// 批量删除
	const deleteBatchCgZwBzyb = (params) => {
		cgZwBzybApi.cgZwBzybDelete(params).then(() => {
			table.value.clearRefreshSelected()
		})
	}
	//生成
	const genBzyb = () => {
		console.log(searchFormState)
		cgZwBzybApi.genBzyb(searchFormState)
	}
	const userInfo = ref(tool.data.get('USER_INFO'))
	const initOrg = () => {
		bizOrgApi.orgTree().then((res) => {
			treeData.value = res
		})
		searchFormState.parentId = userInfo.value.orgId
		bmChange(searchFormState.parentId)
	}
	const bmChange = (e) => {
		const param = { id: e }
		searchFormState.bmdm = e
		bizBzTreeApi.bizBzList(param).then((res) => {
			searchFormState.bzdm = null
			bzInfo.value = [{ value: '', label: '全部班组' }]
			bzInfo.value = bzInfo.value.concat(res.map((item) => ({ value: item.id, label: item.name })))

			searchFormState.bzdm = res[0].id
		})
	}
	const printBm = () => {
		visible.value = true
		src.value =
			'https://hqncg.just.edu.cn/decision/view/report?viewlet=cgjkd%252Fcbtj%252Fbzcbyb_bz.cpt&rq1=' +
			searchFormState.bh[0] +
			'&rq2=' +
			searchFormState.bh[1] +
			'&bmdm=' +
			searchFormState.bmdm +
			'&bzdm=' +
			searchFormState.bzdm
	}
	const printFd = () => {
		visible.value = true
		src.value =
			'https://hqncg.just.edu.cn/decision/view/report?viewlet=cgjkd%252Fcbtj%252Fbzyb_cw.cpt&rq1=' +
			searchFormState.bh[0] +
			'&rq2=' +
			searchFormState.bh[1] +
			'&bmdm=' +
			searchFormState.bmdm +
			'&bzdm=' +
			searchFormState.bzdm
	}
	const printCpdbsr = (record) => {
		visible.value = true
		src.value =
			'https://hqncg.just.edu.cn/decision/view/report?viewlet=cgjkd%252Fdjdy%252Fcgcpdbdhz.cpt&bzdm=' +
			record.bzdm +
			'&rq1=' +
			convertDateStart(record.ybbh) +
			'&rq2=' +
			convertDateEnd(record.ybbh)
	}

	const printLlxj = (record) => {
		const userInfo = tool.data.get('USER_INFO')
		visible.value = true
		if (userInfo.orgName.includes('幼儿园')) {
			src.value =
				'https://hqncg.just.edu.cn/decision/view/report?viewlet=cgjkd%252Fdjdy%252Fbzcb_llmxra4.cpt&bmdm=' +
				record.bmdm +
				'&bzdm=' +
				record.bzdm +
				'&rq1=' +
				convertDateStart(record.ybbh) +
				'&rq2=' +
				convertDateEnd(record.ybbh)
		} else {
			src.value =
				'https://hqncg.just.edu.cn/decision/view/report?viewlet=cgjkd%252Fdjdy%252Fbzcb_llmxr.cpt&bmdm=' +
				record.bmdm +
				'&bzdm=' +
				record.bzdm +
				'&rq1=' +
				convertDateStart(record.ybbh) +
				'&rq2=' +
				convertDateEnd(record.ybbh)
		}
	}
	const convertDateStart = (dateString) => {
		return `${dateString.slice(0, 4)}-${dateString.slice(4, 6)}-01`
	}
	const convertDateEnd = (dateString) => {
		return dayjs(`${dateString.slice(0, 4)}-${dateString.slice(4, 6)}-01`)
			.endOf('month')
			.format('YYYY-MM-DD')
	}
	initOrg()

	const combinedNums = computed(() => {
		let totalhclje = 0
		let totalgyje = 0
		let totalylje = 0
		let total = {}
		if (tableData.value) {
			tableData.value.forEach((spmxItem, index) => {
				for (let item in spmxItem) {
					columns.forEach((column) => {
						if (column.dataIndex == item && column.summary) {
							if (!total[item]) {
								total[item] = 0
							}
							total[item] = NP.plus(total[item], spmxItem[item])
						}
					})
				}
			})
		}
		return {
			total
		}
	})
</script>
<style lang="less" scoped></style>

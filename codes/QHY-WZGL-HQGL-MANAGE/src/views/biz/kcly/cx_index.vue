<template>
	<a-card :bordered="false">
		<a-form ref="searchFormRef" name="advanced_search" :model="searchFormState" class="ant-advanced-search-form">
			<a-row :gutter="24">
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
					<a-form-item label="部门名称" name="bmmc">
						<a-tree-select
							v-model:value="searchFormState.bmdm"
							show-search
							tree-node-filter-prop="name"
							style="width: 100%"
							:dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
							placeholder="请选择部门名称"
							allow-clear
							tree-default-expand-all
							@change="bmChange"
							:tree-data="bmtreeData"
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
						<a-select v-model:value="searchFormState.ffbz" placeholder="请输入分发班组" :options="bzInfo"> </a-select>
					</a-form-item>
				</a-col>
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
					<a-form-item label="出库日期" name="ckrq">
						<a-range-picker v-model:value="searchFormState.ckrq" value-format="YYYY-MM-DD" />
					</a-form-item>
				</a-col>
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24" v-show="advanced">
					<a-form-item label="商品类别" name="lbdm">
						<!--						<a-input v-model:value="searchFormState.lbdm" placeholder="请输入商品类别" />-->
						<a-tree-select
							v-model:value="searchFormState.lbdm"
							show-search
							tree-node-filter-prop="name"
							style="width: 100%"
							:dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
							placeholder="请选择上级类别名称"
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
						></a-tree-select>
					</a-form-item>
				</a-col>
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24" v-show="advanced">
					<a-form-item label="出库类型" name="cklx">
						<a-select v-model:value="searchFormState.cklx" placeholder="请输入出库类型">
							<a-select-option
								v-for="(item, index) in cklxOptions"
								:key="index"
								:label="item"
								:value="item"
							></a-select-option>
						</a-select>
					</a-form-item>
				</a-col>
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24" v-show="advanced">
					<a-form-item label="商品名称" name="spmc">
						<a-input v-model:value="searchFormState.spmc" placeholder="请输入商品名称" />
					</a-form-item>
				</a-col>
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24" v-show="advanced">
					<a-form-item label="商品代码" name="spdm">
						<a-input v-model:value="searchFormState.spdm" placeholder="请输入商品代码" />
					</a-form-item>
				</a-col>

				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
					<a-form-item>
						<a-button type="primary" @click="table.refresh(true)">查询</a-button>
						<!-- <a-button style="margin: 0 8px" @click="print">打印</a-button> -->
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
			:scroll="{ x: 1500 }"
		>
			<template #operator class="table-operator">
				<a-space>
					<a-button type="primary" @click="formRef.onOpen()" v-if="hasPerm('cgKcKczbAdd')">
						<template #icon><plus-outlined /></template>
						新增
					</a-button>
					<xn-batch-delete
						v-if="hasPerm('cgKcKczbBatchDelete')"
						:selectedRowKeys="selectedRowKeys"
						@batchDelete="deleteBatchCgKcKczb"
					/>
				</a-space>
			</template>
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'action'">
					<a-space>
						<a @click="formRef.onOpen(record)">领用</a>
					</a-space>
				</template>
			</template>
		</s-table>
	</a-card>
	<Form ref="formRef" @successful="table.refresh(true)" />
	<a-modal v-model:visible="visible" title="打印" width="100%" wrap-class-name="full-modal" :footer="null">
		<iframe :src="src" width="100%" class="print-iframe" frameborder="0"></iframe>
	</a-modal>
</template>

<script setup name="kckczb">
	import Form from './form.vue'
	import cgKcKczbApi from '@/api/biz/cgKcKczbApi'
	import bizOrgApi from '@/api/biz/bizOrgApi'
	import cgCodeGysApi from '@/api/biz/cgCodeGysApi'
	import bizSplbTreeApi from '@/api/biz/bizSplbTreeApi'
	import cgJhSpmxApi from '@/api/biz/cgJhSpmxApi'
	import bizBzTreeApi from '@/api/biz/bizBzTreeApi'
	import tool from '@/utils/tool'
	import { message } from 'ant-design-vue'
	import dayjs from 'dayjs'
	let searchFormState = reactive({
		cklx: '显示全部',
		ckrq: [dayjs().format('YYYY-MM-DD'), dayjs().format('YYYY-MM-DD')]
	})
	const visible = ref(false)
	const src = ref()
	const searchFormRef = ref()
	const table = ref()
	const formRef = ref()
	const treeData = ref([])
	const bmtreeData = ref([])
	const cklxOptions = ref(['显示全部', '班组订货', '库存领用', '部门调拨', '成品调拨', '库存退库', '商品退货'])
	const bzInfo = ref([])
	const formData = ref({})
	const submitLoading = ref(false)
	const extJson = ref([])
	const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }
	// 查询区域显示更多控制
	const advanced = ref(false)
	const toggleAdvanced = () => {
		advanced.value = !advanced.value
	}
	const columns = [
		// {
		//     title: '一级部门代码（如饮服中心）',
		//     dataIndex: 'yjbmdm',
		//     ellipsis: true
		// },
		// {
		//     title: '一级部门名称',
		//     dataIndex: 'yjbmmc',
		//     ellipsis: true
		// },
		// {
		//     title: '部门代码（食堂代码）：如0101',
		//     dataIndex: 'bmdm'
		// },
		// {
		//     title: '部门名称（东苑食堂、学生食堂、圆中苑、西校区等）',
		//     dataIndex: 'bmmc',
		//     ellipsis: true
		// },
		// {
		//     title: '供应商代码',
		//     dataIndex: 'gysdm',
		//     ellipsis: true
		// },
		// {
		//     title: '供应商名称',
		//     dataIndex: 'gysmc',
		//     ellipsis: true
		// },

		{
			title: '商品类别',
			dataIndex: 'lbmc'
		},

		{
			title: '商品批次',
			dataIndex: 'spjhrq'
		},

		{
			title: '商品名称',
			dataIndex: 'spmc'
		},
		{
			title: '规格',
			dataIndex: 'spgg'
		},
		{
			title: '品牌产地',
			dataIndex: 'ppcd'
		},
		{
			title: '包装率',
			dataIndex: 'bzl'
		},
		{
			title: '单位',
			dataIndex: 'jldw'
		},

		{
			title: '出库数量',
			dataIndex: 'cksl'
		},
		{
			title: '进货单价',
			dataIndex: 'nowjj'
		},
		{
			title: '金额',
			dataIndex: 'je'
		},

		{
			title: '领用班组',
			dataIndex: 'bzmc'
		},
		{
			title: '出库类型',
			dataIndex: 'cklx'
		},
		{
			title: '状态',
			dataIndex: 'workstate'
		},
		{
			title: '领用人',
			dataIndex: 'sqr'
		},
		{
			title: '登记人',
			dataIndex: 'sqr'
		},
		{
			title: '出库审核人',
			dataIndex: 'ckr'
		},
		{
			title: '领用时间',
			dataIndex: 'ckrq'
		}

		// {
		//     title: 'LDJJHBZ',
		//     dataIndex: 'ldjjhbz',
		//     ellipsis: true
		// },
	]

	// columns.push({
	// 	title: '操作',
	// 	dataIndex: 'action',
	// 	align: 'left',
	// 	width: '100px'
	// })

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
		if (searchFormParam.ckrq) {
			searchFormParam.startShrq = searchFormParam.ckrq[0]
			searchFormParam.endShrq = searchFormParam.ckrq[1]
			delete searchFormParam.ckrq
		}
		return cgJhSpmxApi.cgJhSplymxPage(Object.assign(parameter, searchFormParam)).then((data) => {
			return data
		})
	}
	// 重置
	const reset = () => {
		searchFormRef.value.resetFields()
		table.value.refresh(true)
	}
	// 删除
	const deleteCgKcKczb = (record) => {
		let params = [
			{
				id: record.id
			}
		]
		cgKcKczbApi.cgKcKczbDelete(params).then(() => {
			table.value.refresh(true)
		})
	}
	// 批量删除
	const deleteBatchCgKcKczb = (params) => {
		cgKcKczbApi.cgKcKczbDelete(params).then(() => {
			table.value.clearRefreshSelected()
		})
	}
	const print = () => {
		if (!searchFormState.bmdm) {
			message.warn('请先选择部门')
		} else if (!searchFormState.ffbz) {
			message.warn('请先选择班组')
		} else if (!searchFormState.ckrq) {
			message.warn('请先选择日期')
		} else {
			let rq1 = searchFormState.ckrq ? searchFormState.ckrq[0] : ''
			let rq2 = searchFormState.ckrq ? searchFormState.ckrq[1] : ''
			let bmdm = searchFormState.bmdm ? searchFormState.bmdm : ''
			let bzdm = searchFormState.ffbz ? searchFormState.ffbz : ''
			visible.value = true

			src.value =
				'https://hqncg.just.edu.cn/decision/view/report?viewlet=cgjkd%252Fcwtj%252Fbzllspmx.cpt&yf1=' +
				rq1 +
				'&yf2=' +
				rq2 +
				'&bmdm=' +
				bmdm +
				'&bzdm=' +
				bzdm
			console.log(src)
		}
	}
	const userInfo = ref(tool.data.get('USER_INFO'))
	const initOrg = () => {
		bizOrgApi.orgTree().then((res) => {
			bmtreeData.value = res
		})
		bizSplbTreeApi.bizSplbTree().then((res) => {
			treeData.value = res
		})
		searchFormState.bmdm = userInfo.value.orgId
		bmChange(userInfo.value.orgId)
	}
	const bmChange = (e) => {
		const param = { id: e }
		bizBzTreeApi.bizBzList(param).then((res) => {
			searchFormState.ffbz = null
			bzInfo.value = [{ value: '', label: '全部班组' }]
			bzInfo.value = bzInfo.value.concat(res.map((item) => ({ value: item.id, label: item.name })))
			searchFormState.ffbz = bzInfo.value[0].value
		})
	}
	initOrg()
</script>

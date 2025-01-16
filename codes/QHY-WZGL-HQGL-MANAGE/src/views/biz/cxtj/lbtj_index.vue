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
					<a-form-item label="商品类别" name="lbdm">
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

				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
					<a-form-item label="收货日期" name="shrq">
						<a-range-picker v-model:value="searchFormState.shrq" value-format="YYYY-MM-DD" />
					</a-form-item>
				</a-col>

				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24" v-show="advanced">
					<a-form-item label="商品名称" name="spmc">
						<a-input v-model:value="searchFormState.spmc" placeholder="请输入商品名称" />
					</a-form-item>
				</a-col>

				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24" v-show="advanced">
					<a-form-item label="供应商名称" name="gysmc">
						<!--						<a-input v-model:value="searchFormState.bmmc" placeholder="请输入部门名称" />-->
						<a-select
							v-model:value="searchFormState.gysdm"
							placeholder="请输入供应商名称"
							show-search
							optionFilterProp="label"
							allow-clear
						>
							<a-select-option :key="item.gysdm" v-for="item in gysInfo" :value="item.gysdm" :label="item.gysmc"
								>{{ item.gysmc }}
							</a-select-option>
						</a-select>
					</a-form-item>
				</a-col>
				<!--1.打印-->
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
					<a-form-item>
						<a-button type="primary" @click="table.refresh(true)">查询</a-button>
						<!-- <a-button style="margin: 0 8px"> <a @click="print(record)">打印</a></a-button> -->
						<a @click="toggleAdvanced" style="margin-left: 8px">
							{{ advanced ? '收起' : '展开' }}
							<component :is="advanced ? 'up-outlined' : 'down-outlined'" />
						</a>
					</a-form-item>
				</a-col>

				<!--                <a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24" v-show="advanced">-->
				<!--					<a-form-item label="商品代码" name="spdm">-->
				<!--						<a-input v-model:value="searchFormState.spdm" placeholder="请输入商品代码" />-->
				<!--					</a-form-item>-->
				<!--				</a-col>-->
			</a-row>
		</a-form>
		<s-table ref="table" :columns="columns" :data="loadData" bordered :row-key="(record) => record.id">
			<template #operator class="table-operator">
				<a-space>
					<a-button type="primary" @click="formRef.onOpen()" v-if="hasPerm('cgKcKczbAdd')">
						<template #icon>
							<plus-outlined />
						</template>
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
	<!--打印 第3步-->
	<a-modal v-model:visible="visible" title="打印" width="100%" wrap-class-name="full-modal" :footer="null">
		<iframe :src="src" width="100%" class="print-iframe" frameborder="0"></iframe>
	</a-modal>
</template>

<script setup name="kckczb">
	import cgKcKczbApi from '@/api/biz/cgKcKczbApi'
	import bizOrgApi from '@/api/biz/bizOrgApi'
	import cgCodeGysApi from '@/api/biz/cgCodeGysApi'
	import bizSplbTreeApi from '@/api/biz/bizSplbTreeApi'
	import cgJhSpmxApi from '@/api/biz/cgJhSpmxApi'
	import bizBzTreeApi from '@/api/biz/bizBzTreeApi'
	import tool from '@/utils/tool'
	import { message } from 'ant-design-vue'

	let searchFormState = reactive({})
	const visible = ref(false)
	const src = ref()
	const searchFormRef = ref()
	const table = ref()
	const formRef = ref()
	const treeData = ref([])
	const bmtreeData = ref([])
	const gysInfo = ref([])
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
		{
			title: '类别代码',
			dataIndex: 'lbdm'
		},
		{
			title: '商品类别',
			dataIndex: 'lbmc'
		},
		{
			title: '购入数量',
			dataIndex: 'shsl'
		},
		{
			title: '购入金额',
			dataIndex: 'jhje'
		},
		{
			title: '供应金额',
			dataIndex: 'gyje'
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

	// 获取输入日期
	const loadData = (parameter) => {
		const searchFormParam = JSON.parse(JSON.stringify(searchFormState))

		if (searchFormParam.shrq) {
			searchFormParam.startShrq = searchFormParam.shrq[0]
			searchFormParam.endShrq = searchFormParam.shrq[1]
			delete searchFormParam.shrq
		}
		return cgJhSpmxApi.lbtjPage(Object.assign(parameter, searchFormParam)).then((data) => {
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
	const userInfo = ref(tool.data.get('USER_INFO'))

	// 打印 第2步
	const print = () => {
		if (!searchFormState.shrq) {
			message.warn('请先选择收货日期')
		} else {
			let rq1 = searchFormState.shrq ? searchFormState.shrq[0] : ''
			let rq2 = searchFormState.shrq ? searchFormState.shrq[1] : ''
			let lbdm = searchFormState.lbdm ? searchFormState.lbdm : ''
			let bmdm = searchFormState.bmdm ? searchFormState.bmdm : ''
			visible.value = true
			// TODO 打印地址
			src.value =
				'https://hqncg.just.edu.cn/decision/view/report?viewlet=cgjkd%252Fcwtj%252Fsplbtj.cpt&bmdm=' +
				bmdm +
				'&lbdm=' +
				lbdm +
				'&rq1=' +
				rq1 +
				'&rq2=' +
				rq2
			//console.log(src.value)
		}
	}
	const initOrg = () => {
		bizOrgApi.orgTree().then((res) => {
			bmtreeData.value = res
		})
		bizSplbTreeApi.bizSplbTree().then((res) => {
			treeData.value = res
		})
		cgCodeGysApi.cgCodeGysList().then((res) => {
			gysInfo.value = res
		})
		searchFormState.bmdm = userInfo.value.orgId
	}

	initOrg()
</script>

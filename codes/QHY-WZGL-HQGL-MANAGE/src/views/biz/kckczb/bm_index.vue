<template>
	<a-modal
		v-model:visible="visible"
		title="明细"
		width="100%"
		:mask-closable="false"
		wrap-class-name="full-modal"
		:destroy-on-close="true"
	>
		<a-form ref="searchFormRef" name="advanced_search" :model="searchFormState" class="ant-advanced-search-form">
			<a-row :gutter="24">
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
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
					<a-form-item label="商品名称" name="spmc">
						<a-input v-model:value="searchFormState.spmc" placeholder="请输入商品名称" />
					</a-form-item>
				</a-col>
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24" v-show="advanced">
					<a-form-item label="商品代码" name="spdm">
						<a-input v-model:value="searchFormState.spdm" placeholder="请输入商品代码" />
					</a-form-item>
				</a-col>
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24" v-show="advanced">
					<a-form-item label="启用标志" name="qybz">
						<a-input v-model:value="searchFormState.qybz" placeholder="请输入启用标志" />
					</a-form-item>
				</a-col>
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24" v-show="advanced">
					<a-form-item label="显示设置" name="xssz">
						<a-select v-model:value="searchFormState.xssz" placeholder="请输入显示设置">
							<a-select-option
								v-for="(item, index) in xsszOptions"
								:key="index"
								:label="item"
								:value="item"
							></a-select-option>
						</a-select>
					</a-form-item>
				</a-col>
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
					<a-form-item>
						<a-button type="primary" @click="table.refresh(true)">查询</a-button>
						<!-- <a-button style="margin: 0 8px" @click="reset"><a @click="print(record)">打印</a></a-button> -->
						<a @click="toggleAdvanced" style="margin-left: 8px">
							{{ advanced ? '收起' : '展开' }}
							<component :is="advanced ? 'up-outlined' : 'down-outlined'" />
						</a>
					</a-form-item>
				</a-col>
			</a-row>
		</a-form>
		<a-card>
			<s-table
				ref="table"
				:columns="columns"
				:data="loadData"
				bordered
				:row-key="(record) => record.id"
				:scroll="{ x: 900 }"
			>
				<template #bodyCell="{ column, record }">
					<template v-if="column.dataIndex === 'action'">
						<a-space>
							<a @click="rkformRef.onOpen(record)">入库明细</a>
							<a-divider type="vertical" />
							<a @click="ckformRef.onOpen(record)">出库明细</a>
						</a-space>
					</template>

					<template v-if="column.dataIndex === 'kcxx'">
						<div>
							<a-input-number @blur="kcxxChange(record)" v-model:value="record.kcxx" />
						</div>
					</template>

					<template v-if="column.dataIndex === 'sjkc'">
						<div v-if="record.sjkc <= record.kcxx" style="color: red">
							{{ record.sjkc }}
						</div>
						<div v-if="record.sjkc > record.kcxx" style="color: green">
							{{ record.sjkc }}
						</div>
					</template>
				</template>
			</s-table>
		</a-card>
		<template #footer>
			{{ null }}
		</template>
	</a-modal>
	<rkmxIndex ref="rkformRef" @successful="table.refresh(true)" />
	<ckmxIndex ref="ckformRef" @successful="table.refresh(true)" />
	<a-modal v-model:visible="visible" title="打印" width="100%" wrap-class-name="full-modal" :footer="null">
		<iframe :src="src" width="100%" class="print-iframe" frameborder="0"></iframe>
	</a-modal>
</template>

<script setup name="kckczb">
	import Form from './form.vue'
	import rkmxIndex from './rkmx_index.vue'
	import ckmxIndex from './ckmx_index.vue'
	import cgKcKczbApi from '@/api/biz/cgKcKczbApi'
	import saveKcxx from '@/api/biz/cgKcKczbApi'
	import bizOrgApi from '@/api/biz/bizOrgApi'
	import cgCodeGysApi from '@/api/biz/cgCodeGysApi'
	import bizSplbTreeApi from '@/api/biz/bizSplbTreeApi'
	import { message, Modal } from 'ant-design-vue'
	import tool from '@/utils/tool'
	import router from '@/router'

	let searchFormState = reactive({ xssz: '只显示有库存' })
	const searchFormRef = ref()
	const table = ref()
	const formRef = ref()
	const rkformRef = ref()
	const ckformRef = ref()
	const visible = ref(false)
	const src = ref()
	const treeData = ref([])
	const bmtreeData = ref([])
	const formData = ref({})
	const xsszOptions = ref(['显示全部', '只显示有库存', '只显示无库存', '显示下限不足', '显示临期或过期'])
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
		// 	title: "商品代码",
		// 	dataIndex: "spdm"
		// },
		{
			title: '部门名称',
			dataIndex: 'bmmc'
		},
		{
			title: '类别',
			dataIndex: 'lbName'
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
			title: '显示顺序',
			dataIndex: 'spxh'
		},
		{
			title: '拼音简码',
			dataIndex: 'pyjm'
		},
		{
			title: '库存数量',
			dataIndex: 'sjkc'
		},
		{
			title: '库存报警下限',
			dataIndex: 'kcxx'
		},
		{
			title: '启用标志',
			dataIndex: 'qybz'
		}
	]
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
		return cgKcKczbApi.cgKcKczbPage(Object.assign(parameter, searchFormParam)).then((data) => {
			data.records.forEach((record) => {
				record.kcxxOld = record.kcxx
			})
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
		} else {
			visible.value = true
			src.value =
				'https://hqncg.just.edu.cn/decision/view/report?viewlet=cgjkd%252Fcwtj%252Fkchzb.cpt&bmdm=' +
				searchFormState.bmdm
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
		if (router.currentRoute.value.query.xssz) {
			searchFormState.xssz = router.currentRoute.value.query.xssz
		}
	}

	const kcxxChange = (record) => {
		if (record.kcxxOld != record.kcxx) {
			Modal.confirm({
				title: '是否确认修改库存下限',
				onOk() {
					if (!record.kcxx || record.kcxx == null) {
						record.kcxx = 0
					}
					if (record.kcxx < 0) {
						message.warning('请输入大于0的预警下限')
						return
					}
					formData.value = record
					console.log(formData.value)
					cgKcKczbApi.saveKcxx(formData.value).then(() => {
						table.value.refresh(true)
					})
				},
				// eslint-disable-next-line @typescript-eslint/no-empty-function
				onCancel() {
					record.kcxx = record.kcxxOld
				}
			})
		}
	}
	const onOpen = (record) => {
		visible.value = true
		searchFormState.bmdm = record.bmdm
		searchFormState.spdm = record.spdm
		searchFormState.spmc = record.spmc
		searchFormState.bmmc = record.bmmc
		searchFormState.yjbmmc = record.yjbmmc
		//searchFormState.bmdm=userInfo.value.orgId
		// const currentDate = dayjs().startOf('day');
		// const futureDate = currentDate.subtract(1, 'week').startOf('day');
		// searchFormState.xhrq = [futureDate, currentDate];
	}
	// 抛出函数
	defineExpose({
		onOpen
	})
	initOrg()
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

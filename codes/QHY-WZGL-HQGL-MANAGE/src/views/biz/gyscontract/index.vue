<template>
	<!-- <a-modal
		:title="'供应商合同'"
		:width="900"
		:visible="visible"
		:destroy-on-close="true"
		:footer-style="{ textAlign: 'right' }"
		@cancel="onClose"
		:maskClosable="false"
	> -->
	<a-card :bordered="false">
		<a-form ref="searchFormRef" name="advanced_search" :model="searchFormState" class="ant-advanced-search-form">
			<a-row :gutter="24">
				<a-col :span="6">
					<a-form-item label="供应商代码" name="gysdm">
						<a-select v-model:value="searchFormState.gysdm" placeholder="请选择供应商代码" :options="gysdmOptions" />
					</a-form-item>
				</a-col>
				<a-col :span="6">
					<a-form-item label="合同名称" name="contractName">
						<a-input v-model:value="searchFormState.contractName" placeholder="请输入合同名称" />
					</a-form-item>
				</a-col>
				<a-col :span="6">
					<a-form-item label="合同有效期" name="contractExpired">
						<a-range-picker
							v-model:value="searchFormState.contractExpired"
							value-format="YYYY-MM-DD HH:mm:ss"
							show-time
						/>
					</a-form-item>
				</a-col>
				<a-col :span="6" v-show="advanced">
					<a-form-item label="合同范围" name="contractRange">
						<a-input v-model:value="searchFormState.contractRange" placeholder="请输入合同范围" />
					</a-form-item>
				</a-col>
				<a-col :span="6" v-show="advanced">
					<a-form-item label="是否禁用" name="isDisable">
						<a-select
							v-model:value="searchFormState.isDisable"
							placeholder="请选择是否禁用"
							:options="isDisableOptions"
						/>
					</a-form-item>
				</a-col>
				<a-col :span="6" v-show="advanced">
					<a-form-item label="合同状态" name="status">
						<a-select v-model:value="searchFormState.status" placeholder="请选择合同状态" :options="statusOptions" />
					</a-form-item>
				</a-col>
				<a-col :span="6">
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
			:alert="options.alert.show"
			bordered
			:row-key="(record) => record.id"
			:tool-config="toolConfig"
			:row-selection="options.rowSelection"
		>
			<template #operator class="table-operator">
				<a-space>
					<a-button type="primary" @click="formRef.onOpen()" v-if="hasPerm('cgGysContractAdd')">
						<template #icon><plus-outlined /></template>
						新增
					</a-button>
					<xn-batch-delete
						v-if="hasPerm('cgGysContractBatchDelete')"
						:selectedRowKeys="selectedRowKeys"
						@batchDelete="deleteBatchCgGysContract"
					/>
				</a-space>
			</template>
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'action'">
					<a-space>
						<a @click="formRef.onOpen(record)" v-if="hasPerm('cgGysContractEdit')">编辑</a>
						<a-divider type="vertical" v-if="hasPerm(['cgGysContractEdit', 'cgGysContractDelete'], 'and')" />
						<a-popconfirm title="确定要删除吗？" @confirm="deleteCgGysContract(record)">
							<a-button type="link" danger size="small" v-if="hasPerm('cgGysContractDelete')">删除</a-button>
						</a-popconfirm>
					</a-space>
				</template>
			</template>
		</s-table>
	</a-card>
	<Form ref="formRef" @successful="table.refresh(true)" />
	<!-- </a-modal> -->
</template>

<script setup name="gyscontract">
	import tool from '@/utils/tool'
	import Form from './form.vue'
	import cgGysContractApi from '@/api/biz/cgGysContractApi'
	import { cloneDeep } from 'lodash-es'
	let searchFormState = reactive({})
	const visible = ref(false)
	const searchFormRef = ref()
	const table = ref()
	const formRef = ref()
	const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }
	// 查询区域显示更多控制
	const advanced = ref(false)
	const toggleAdvanced = () => {
		advanced.value = !advanced.value
	}
	const columns = [
		{
			title: '供应商代码',
			dataIndex: 'gysName'
		},
		{
			title: '合同名称',
			dataIndex: 'contractName'
		},
		{
			title: '合同有效期',
			dataIndex: 'contractExpired'
		},
		{
			title: '合同范围',
			dataIndex: 'contractRange'
		},
		{
			title: '是否禁用',
			dataIndex: 'isDisable'
		},

		{
			title: '合同状态',
			dataIndex: 'status'
		},
		{
			title: 'BZ',
			dataIndex: 'bz'
		}
	]
	// 操作栏通过权限判断是否显示
	if (hasPerm(['cgGysContractEdit', 'cgGysContractDelete'])) {
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
	const loadData = (parameter) => {
		const searchFormParam = JSON.parse(JSON.stringify(searchFormState))
		// contractExpired范围查询条件重载
		if (searchFormParam.contractExpired) {
			searchFormParam.startContractExpired = searchFormParam.contractExpired[0]
			searchFormParam.endContractExpired = searchFormParam.contractExpired[1]
			delete searchFormParam.contractExpired
		}
		return cgGysContractApi.cgGysContractPage(Object.assign(parameter, searchFormParam)).then((data) => {
			return data
		})
	}
	// 重置
	const reset = () => {
		searchFormRef.value.resetFields()
		table.value.refresh(true)
	}
	// 删除
	const deleteCgGysContract = (record) => {
		let params = [
			{
				id: record.id
			}
		]
		cgGysContractApi.cgGysContractDelete(params).then(() => {
			table.value.refresh(true)
		})
	}
	// 批量删除
	const deleteBatchCgGysContract = (params) => {
		cgGysContractApi.cgGysContractDelete(params).then(() => {
			table.value.clearRefreshSelected()
		})
	}
	const gysdmOptions = tool.dictList('COMMON_SWITCH')
	const isDisableOptions = tool.dictList('启用标志')
	const statusOptions = tool.dictList('COMMON_STATUS')

	// 打开抽屉
	const onOpen = (record) => {
		visible.value = true
		return
		if (record) {
			let recordData = cloneDeep(record)
			recordData.prcId = recordData.gysdm
			// formData.value = Object.assign({}, recordData)
			// addFormData.value = Object.assign({}, recordData)
			// delete addFormData.value.id
		}
	}
	// 关闭抽屉
	const onClose = () => {
		visible.value = false
	}
	// 抛出函数
	defineExpose({
		onOpen
	})
</script>

<template>
	<a-modal
		v-model:visible="visible"
		title="入库明细"
		width="100%"
		:mask-closable="false"
		wrap-class-name="full-modal"
		:destroy-on-close="true"
	>

		<a-form ref="searchFormRef" name="advanced_search" :model="searchFormState" class="ant-advanced-search-form">
			<a-row :gutter="24">
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
					<a-form-item label="商品名称" name="spmc">
						<a-input v-model:value="searchFormState.spmc" placeholder="请输入申请人" readonly="readonly"/>
					</a-form-item>
				</a-col>

				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
					<a-form-item label="入库审核日期：" name="shrq">
						<a-range-picker v-model:value="searchFormState.shrq" value-format="YYYY-MM-DD" show-time :default-value="[dayjs().subtract(1, 'week').startOf('day'), dayjs().startOf('day')]"/>
					</a-form-item>
				</a-col>
<!--				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">-->
<!--					<a-form-item label="一级部门" name="yjbmmc">-->
<!--						<a-input v-model:value="searchFormState.yjbmmc" placeholder="请输入申请人"/>-->
<!--					</a-form-item>-->
<!--				</a-col>-->
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
					<a-form-item label="部门名称" name="bmmc">
<!--						<a-input v-model:value="searchFormState.bmmc" placeholder="请输入申请人"/>-->
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
					<a-button type="primary" @click="table.refresh(true)">查询</a-button>
				</a-col>
			</a-row>
		</a-form>

		<template #footer>
			{{null}}
		</template>
	<a-card>
        <s-table
            ref="table"
            :columns="columns"
            :data="loadData"
            bordered
            :row-key="(record) => record.id"
			:scroll="{ x: 1300 }"
        >
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'action'">
					<a-space>
						<a @click="formRef.onOpen(record)">报损</a>
					</a-space>
				</template>
			</template>
        </s-table>
    </a-card>
    <Form ref="formRef" @successful="table.refresh(true)" />
	</a-modal>
</template>

<script setup name="jhshd">
    import Form from './form.vue'
    import cgJhSqdApi from '@/api/biz/cgJhSqdApi'
	import cgJhSpmxApi from "@/api/biz/cgJhSpmxApi";
	import dayjs from 'dayjs';
	import bizOrgApi from "@/api/biz/bizOrgApi";
    let searchFormState = reactive({workstate:'已收货',xssz:'只显示有库存',isZero:'否'})
    const searchFormRef = ref()
	const bmtreeData = ref([])
    const table = ref()
	const xsszOptions = ref(['显示全部','只显示有库存','只显示无库存'])
	const visible = ref(false)
    const formRef = ref()
	const cglx = ref(['班组订货','部门备货'])
    const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }
    // 查询区域显示更多控制
    const advanced = ref(false)
    const toggleAdvanced = () => {
        advanced.value = !advanced.value
    }
    const columns = [
		{
			title: '商品批次',
			dataIndex: 'spjhrq'
		},
		{
			title: '商品名称',
			dataIndex: 'spmc'
		},
		{
			title: '商品规格',
			dataIndex: 'spgg'
		},
		{
			title: '计量单位',
			dataIndex: 'jldw'
		},
		{
			title: '库存数量',
			dataIndex: 'kcsl'
		},
		{
			title: '供应单价',
			dataIndex: 'gydj'
		},
		{
			title: '合计金额',
			dataIndex: 'gyje'
		},
		{
			title: '入库日期',
			dataIndex: 'shrq'
		},
        {
            title: '入库类型',
            dataIndex: 'cglx'
        },
		{
			title: '入库人',
			dataIndex: 'shry'
		},

    ]
	columns.push({
		title: '操作',
		dataIndex: 'action',
		align: 'center',
		width: '100px'
	})
    const selectedRowKeys = ref([])

	const onOpen = (record)=>{
    	console.log(record)
		visible.value = true
		searchFormState.bmdm=record.bmdm
		searchFormState.spdm=record.spdm
		searchFormState.spmc=record.spmc
		searchFormState.bmmc = record.bmmc;
		searchFormState.yjbmmc = record.yjbmmc;
		// const currentDate = dayjs().startOf('day');
		// const futureDate = currentDate.subtract(1, 'week').startOf('day');
		// searchFormState.shrq = [futureDate, currentDate];

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
    const loadData = (parameter) => {
        const searchFormParam = JSON.parse(JSON.stringify(searchFormState))
        return cgJhSpmxApi.cgJhSpmxPage(Object.assign(parameter, searchFormParam)).then((data) => {
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

	const initOrg = () => {
		bizOrgApi.orgTree().then((res) => {
			bmtreeData.value = res
		})
	}

	initOrg()
</script>
<style  lang="less">
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

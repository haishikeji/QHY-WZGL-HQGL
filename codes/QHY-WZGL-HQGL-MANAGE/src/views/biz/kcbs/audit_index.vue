<template>
    <a-card :bordered="false">
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
                <a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
					<a-form-item>
                    <a-button type="primary" @click="table.refresh(true)">查询</a-button>
                    <a-button style="margin: 0 8px" @click="reset">重置</a-button>
                    <a @click="toggleAdvanced" style="margin-left: 8px">
                        {{ advanced ? '收起' : '展开' }}
                        <component :is="advanced ? 'up-outlined' : 'down-outlined'"/>
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
			:scroll="{ x: 900 }"
        >
            <template #operator class="table-operator">
                <a-space>
                    <xn-batch-operation
						:buttonName="'批量审核'"
						:title="'审核此信息?'"
						:selectedRowKeys="selectedRowKeys"
						@batchOperation="auditBatchCgLyMx"
                    />
                </a-space>
            </template>
            <template #bodyCell="{ column, record }">
                <template v-if="column.dataIndex === 'action'">
                    <a-space>
						<a-popconfirm title="确定要审核吗？" @confirm="auditCgBsMx(record)" v-if="record.workstate=='申请中'">
							<a-button type="link" danger size="small">审核</a-button>
						</a-popconfirm>
                    </a-space>
                </template>
            </template>
        </s-table>
    </a-card>
    <Form ref="formRef" @successful="table.refresh(true)" />
</template>

<script setup name="kckczb">
    import Form from './form.vue'
    import cgKcKczbApi from '@/api/biz/cgKcKczbApi'
	import bizOrgApi from "@/api/biz/bizOrgApi";
	import cgCodeGysApi from "@/api/biz/cgCodeGysApi";
	import bizSplbTreeApi from "@/api/biz/bizSplbTreeApi";
	import cgJhSpmxApi from "@/api/biz/cgJhSpmxApi";
	import cgJhSqdApi from "@/api/biz/cgJhSqdApi";
	import tool from "@/utils/tool";
    let searchFormState = reactive({isZero:"false",cklx:"库存报损",workstate:"申请中"})
    const searchFormRef = ref()
    const table = ref()
    const formRef = ref()
	const treeData = ref([])
	const bmtreeData = ref([])
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
			title: '出库类型',
			dataIndex: 'cklx',
		},
        {
            title: '商品代码',
            dataIndex: 'spdm',
        },
        {
            title: '商品名称',
            dataIndex: 'spmc',
        },
        {
            title: '规格',
            dataIndex: 'spgg'
        },
		{
			title: '单位',
			dataIndex: 'jldw'
		},
		{
			title: '部门名称',
			dataIndex: 'bmmc'
		},
		{
			title: '申请日期',
			dataIndex: 'sqrq'
		},
		{
			title: '申请数量',
			dataIndex: 'sqsl'
		},

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
                selectedRowKeys.value = selectedRows
            }
        }
    }
    const loadData = (parameter) => {
        const searchFormParam = JSON.parse(JSON.stringify(searchFormState))
        return cgJhSpmxApi.cgJhSplymxPage(Object.assign(parameter, searchFormParam)).then((data) => {
            return data
        })
    }
    // 重置
    const reset = () => {
        searchFormRef.value.resetFields()
        table.value.refresh(true)
    }
	// 审核
	const auditCgBsMx = (record) => {
		let params = [
			{
				id: record.id
			}
		]
		cgJhSpmxApi.auditCgBsMx(params).then(() => {
			table.value.refresh(true)
		})
	}
	// 批量审核
	const auditBatchCgLyMx = (params) => {
		cgJhSpmxApi.auditCgBsMx(params).then(() => {
			table.value.clearRefreshSelected()
		})
	}
	const userInfo = ref(tool.data.get('USER_INFO'))
	const initOrg = () => {
		bizOrgApi.orgTree().then((res) => {
			bmtreeData.value = res
		})
		bizSplbTreeApi.bizSplbTree().then((res) => {
			treeData.value = res
		})
		searchFormState.bmdm=userInfo.value.orgId
	}
	initOrg()
</script>

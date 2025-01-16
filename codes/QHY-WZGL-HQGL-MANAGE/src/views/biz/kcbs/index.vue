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
            bordered
            :row-key="(record) => record.id"
			:scroll="{ x: 900 }"
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
                        <a @click="formRef.onOpen(record)">报损</a>
                    </a-space>
                </template>
            </template>
        </s-table>
    </a-card>
    <Form ref="formRef" @successful="table.refresh(true)" />
</template>

<script setup name="kckczb">
    import Form from './rkmx_index.vue'
    import cgKcKczbApi from '@/api/biz/cgKcKczbApi'
	import bizOrgApi from "@/api/biz/bizOrgApi";
	import cgCodeGysApi from "@/api/biz/cgCodeGysApi";
	import bizSplbTreeApi from "@/api/biz/bizSplbTreeApi";
	import tool from "@/utils/tool";
    let searchFormState = reactive({isZero:"false"})
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

        // {
        //     title: '成本分类',
        //     dataIndex: 'spfl',
        //     ellipsis: true
        // },
        // {
        //     title: '商品大类代码（01）',
        //     dataIndex: 'dldm',
        //     ellipsis: true
        // },
        // {
        //     title: '商品大类名称（食品类、维修材料类、园林绿化类）',
        //     dataIndex: 'dlmc',
        //     ellipsis: true
        // },
        // {
        //     title: '商品类别',
        //     dataIndex: 'lbdm'
        // },
        // {
        //     title: '类别名称',
        //     dataIndex: 'lbmc',
        //     ellipsis: true
        // },
        // {
        //     title: '当前进价',
        //     dataIndex: 'nowjj',
        //     ellipsis: true
        // },
		//
        // {
        //     title: '库存报警下限',
        //     dataIndex: 'kcxx',
        //     ellipsis: true
        // },
        // {
        //     title: '审批标志（是、否）(用于进货时确定是否需要审批',
        //     dataIndex: 'spbz',
        //     ellipsis: true
        // },
        // {
        //     title: '审批数量(进货时超过该数量需审批',
        //     dataIndex: 'spsl',
        //     ellipsis: true
        // },
        // {
        //     title: '入库数量(累计）',
        //     dataIndex: 'rklj',
        //     ellipsis: true
        // },
        // {
        //     title: '调拨入库数量(累计）',
        //     dataIndex: 'dbrklj',
        //     ellipsis: true
        // },
        // {
        //     title: '销售出库数量(累计)',
        //     dataIndex: 'cklj',
        //     ellipsis: true
        // },
        // {
        //     title: '调拨出库数量(累计)',
        //     dataIndex: 'dbcklj',
        //     ellipsis: true
        // },
        // {
        //     title: '实际库存(合计)',
        //     dataIndex: 'sjkc',
        //     ellipsis: true
        // },
        {
            title: '启用标志',
            dataIndex: 'qybz'
        },
        // {
        //     title: 'LDJJHBZ',
        //     dataIndex: 'ldjjhbz',
        //     ellipsis: true
        // },
    ]

		columns.push({
			title: '操作',
			dataIndex: 'action',
			align: 'center',
			width: '100px'
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

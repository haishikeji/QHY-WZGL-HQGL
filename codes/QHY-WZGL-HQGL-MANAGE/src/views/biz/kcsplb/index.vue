<template>
    <a-card :bordered="false">
        <a-form ref="searchFormRef" name="advanced_search" :model="searchFormState" class="ant-advanced-search-form">
            <a-row :gutter="24">
                <a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
                    <a-form-item label="关键字名称" name="lbmc">
                        <a-input v-model:value="searchFormState.lbmc" placeholder="请输入关键字名称" />
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
        >
            <template #operator class="table-operator">
                <a-space>
                    <a-button type="primary" @click="formRef.onOpen()" v-if="hasPerm('cgKcSplbAdd')">
                        <template #icon><plus-outlined /></template>
                        新增类别
                    </a-button>
                    <xn-batch-delete
                        v-if="hasPerm('cgKcSplbBatchDelete')"
                        :selectedRowKeys="selectedRowKeys"
                        @batchDelete="deleteBatchCgKcSplb"
                    />
                </a-space>
            </template>
            <template #bodyCell="{ column, record }">
                <template v-if="column.dataIndex === 'action'">
                    <a-space>
                        <a @click="formRef.onOpen(record)" v-if="hasPerm('cgKcSplbEdit')">编辑</a>
                        <a-divider type="vertical" v-if="hasPerm(['cgKcSplbEdit', 'cgKcSplbDelete'], 'and')" />
                        <a-popconfirm title="确定要删除吗？" @confirm="deleteCgKcSplb(record)">
                            <a-button type="link" danger size="small" v-if="hasPerm('cgKcSplbDelete')">删除</a-button>
                        </a-popconfirm>
                    </a-space>
                </template>
            </template>
        </s-table>
    </a-card>
    <Form ref="formRef" @successful="table.refresh(true)" />
</template>

<script setup name="kcsplb">
    import Form from './form.vue'
    import cgKcSplbApi from '@/api/biz/cgKcSplbApi'
    let searchFormState = reactive({})
    const searchFormRef = ref()
    const table = ref()
    const formRef = ref()
    const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }
    const columns = [
		{
			title: '类别代码',
			dataIndex: 'lbdm'
		},
		{
			title: '类别名称',
			dataIndex: 'lbmc'
		},
        {
            title: '商品大类',
            dataIndex: 'dlmc'
        },
        {
            title: '显示顺序',
            dataIndex: 'lbxh'
        },
        {
            title: '拼音简码',
            dataIndex: 'pyjm'
        },
        {
            title: '启用标志',
            dataIndex: 'qybz'
        },
    ]
    // 操作栏通过权限判断是否显示

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
        return cgKcSplbApi.cgKcSplbPage(Object.assign(parameter, searchFormParam)).then((data) => {
            return data
        })
    }
    // 重置
    const reset = () => {
        searchFormRef.value.resetFields()
        table.value.refresh(true)
    }
    // 删除
    const deleteCgKcSplb = (record) => {
        let params = [
            {
                id: record.id
            }
        ]
        cgKcSplbApi.cgKcSplbDelete(params).then(() => {
            table.value.refresh(true)
        })
    }
    // 批量删除
    const deleteBatchCgKcSplb = (params) => {
        cgKcSplbApi.cgKcSplbDelete(params).then(() => {
            table.value.clearRefreshSelected()
        })
    }
</script>

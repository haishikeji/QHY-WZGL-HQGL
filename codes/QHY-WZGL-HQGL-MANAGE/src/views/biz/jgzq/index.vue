<template>
    <a-card :bordered="false">
        <a-form ref="searchFormRef" name="advanced_search" :model="searchFormState" class="ant-advanced-search-form">
            <a-row :gutter="24">
                <a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
                    <a-form-item label="商品名称" name="spmc">
                        <a-input v-model:value="searchFormState.spmc" placeholder="请输入商品名称" />
                    </a-form-item>
                </a-col>
                <a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
                    <a-form-item label="数据来源" name="sply">
                        <a-input v-model:value="searchFormState.sply" placeholder="请输入数据来源" />
                    </a-form-item>
                </a-col>
                <a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
                    <a-form-item label="抓取时间" name="zqsj">
                        <a-range-picker v-model:value="searchFormState.zqsj" value-format="YYYY-MM-DD HH:mm:ss" show-time />
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
            bordered
            :row-key="(record) => record.id"
            :tool-config="toolConfig"
        >
            <template #operator class="table-operator">
                <a-space>
                    <a-button type="primary" @click="grab()">
                        <template #icon><plus-outlined /></template>
                        抓取
                    </a-button>
                </a-space>
            </template>
            <template #bodyCell="{ column, record }">
                <template v-if="column.dataIndex === 'action'">
                    <a-space>
                        <a @click="formRef.onOpen(record)" v-if="hasPerm('spjgEdit')">编辑</a>
                        <a-divider type="vertical" v-if="hasPerm(['spjgEdit', 'spjgDelete'], 'and')" />
                        <a-popconfirm title="确定要删除吗？" @confirm="deleteSpjg(record)">
                            <a-button type="link" danger size="small" v-if="hasPerm('spjgDelete')">删除</a-button>
                        </a-popconfirm>
                    </a-space>
                </template>
            </template>
        </s-table>
    </a-card>
    <Form ref="formRef" @successful="table.refresh(true)" />
</template>

<script setup name="价格抓取">
    import Form from './form.vue'
    import spjgApi from '@/api/biz/spjgApi'
    let searchFormState = reactive({})
    const searchFormRef = ref()
    const table = ref()
    const formRef = ref()
    const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }
    const columns = [
        {
            title: '商品名称',
            dataIndex: 'spmc'
        },
        {
            title: '价格',
            dataIndex: 'jg'
        },
        {
            title: '数据来源',
            dataIndex: 'sply'
        },
        {
            title: '抓取时间',
            dataIndex: 'zqsj'
        },
        {
            title: '抓取批次',
            dataIndex: 'zqpc'
        },
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
    const loadData = (parameter) => {
        const searchFormParam = JSON.parse(JSON.stringify(searchFormState))
        // zqsj范围查询条件重载
        if (searchFormParam.zqsj) {
            searchFormParam.startZqsj = searchFormParam.zqsj[0]
            searchFormParam.endZqsj = searchFormParam.zqsj[1]
            delete searchFormParam.zqsj
        }
        return spjgApi.spjgPage(Object.assign(parameter, searchFormParam)).then((data) => {
            return data
        })
    }
    // 重置
    const reset = () => {
        searchFormRef.value.resetFields()
        table.value.refresh(true)
    }
    // 删除
    const deleteSpjg = (record) => {
        let params = [
            {
                id: record.id
            }
        ]
        spjgApi.spjgDelete(params).then(() => {
            table.value.refresh(true)
        })
    }
    // 批量删除
    const deleteBatchSpjg = (params) => {
        spjgApi.spjgDelete(params).then(() => {
            table.value.clearRefreshSelected()
        })
    }
	//抓取
	const grab = () => {
		spjgApi.spjgGrab().then(() => {
			table.value.clearRefreshSelected()
		})
	}
</script>

<template>
    <a-card :bordered="false">
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
                    <a-button type="primary" @click="formRef.onOpen()" v-if="hasPerm('cgZwBzrbmxAdd')">
                        <template #icon><plus-outlined /></template>
                        新增
                    </a-button>
                    <xn-batch-delete
                        v-if="hasPerm('cgZwBzrbmxBatchDelete')"
                        :selectedRowKeys="selectedRowKeys"
                        @batchDelete="deleteBatchCgZwBzrbmx"
                    />
                </a-space>
            </template>
            <template #bodyCell="{ column, record }">
                <template v-if="column.dataIndex === 'action'">
                    <a-space>
                        <a @click="formRef.onOpen(record)" v-if="hasPerm('cgZwBzrbmxEdit')">编辑</a>
                        <a-divider type="vertical" v-if="hasPerm(['cgZwBzrbmxEdit', 'cgZwBzrbmxDelete'], 'and')" />
                        <a-popconfirm title="确定要删除吗？" @confirm="deleteCgZwBzrbmx(record)">
                            <a-button type="link" danger size="small" v-if="hasPerm('cgZwBzrbmxDelete')">删除</a-button>
                        </a-popconfirm>
                    </a-space>
                </template>
            </template>
        </s-table>
    </a-card>
    <Form ref="formRef" @successful="table.refresh(true)" />
</template>

<script setup name="zwbzrbmx">
    import Form from './form.vue'
    import cgZwBzrbmxApi from '@/api/biz/cgZwBzrbmxApi'
    const table = ref()
    const formRef = ref()
    const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }
    const columns = [
        {
            title: '生成日期',
            dataIndex: 'rq'
        },
        {
            title: '操作员',
            dataIndex: 'czy'
        },
        {
            title: '一级部门名称',
            dataIndex: 'yjbmdm'
        },
        {
            title: '一级部门代码（如饮服中心）',
            dataIndex: 'yjbmmc'
        },
        {
            title: '部门代码（食堂代码）：如0101',
            dataIndex: 'bmdm'
        },
        {
            title: '部门名称（东苑食堂、学生食堂、圆中苑、西校区等）',
            dataIndex: 'bmmc'
        },
        {
            title: '日报编号（如：20100406）',
            dataIndex: 'rbbh'
        },
        {
            title: '班组代码（部门代码+位流水号：如010101）',
            dataIndex: 'bzdm'
        },
        {
            title: '商品类别',
            dataIndex: 'lbdm'
        },
        {
            title: '类别名称',
            dataIndex: 'lbmc'
        },
        {
            title: '类别类型（荤菜类、素菜类、调料类、主食类、水电气类、低耗类、酬金类）',
            dataIndex: 'lblx'
        },
        {
            title: '班组名称（煲仔、粥馆、小炒、麻辣等）',
            dataIndex: 'bzmc'
        },
        {
            title: '统计类别（0表示自动计算统计的种类，1表示手工输入的类别）',
            dataIndex: 'tjlb'
        },
        {
            title: '显示顺序',
            dataIndex: 'lbxh'
        },
        {
            title: '支出金额',
            dataIndex: 'outje'
        },
        {
            title: '收入金额',
            dataIndex: 'inje'
        },
        {
            title: 'BZ',
            dataIndex: 'bz'
        },
    ]
    // 操作栏通过权限判断是否显示
    if (hasPerm(['cgZwBzrbmxEdit', 'cgZwBzrbmxDelete'])) {
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
        return cgZwBzrbmxApi.cgZwBzrbmxPage(parameter).then((data) => {
            return data
        })
    }
    // 重置
    const reset = () => {
        searchFormRef.value.resetFields()
        table.value.refresh(true)
    }
    // 删除
    const deleteCgZwBzrbmx = (record) => {
        let params = [
            {
                id: record.id
            }
        ]
        cgZwBzrbmxApi.cgZwBzrbmxDelete(params).then(() => {
            table.value.refresh(true)
        })
    }
    // 批量删除
    const deleteBatchCgZwBzrbmx = (params) => {
        cgZwBzrbmxApi.cgZwBzrbmxDelete(params).then(() => {
            table.value.clearRefreshSelected()
        })
    }
</script>

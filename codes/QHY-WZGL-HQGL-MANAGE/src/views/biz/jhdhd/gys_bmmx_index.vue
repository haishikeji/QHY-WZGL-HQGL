<template>
	<a-modal
		v-model:visible="visible"
		title="明细"
		width="100%"
		:mask-closable="false"
		wrap-class-name="full-modal"
		:destroy-on-close="true"
	>
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
        >

        </s-table>
    </a-card>
    <Form ref="formRef" @successful="table.refresh(true)" />
	</a-modal>
</template>

<script setup name="jhsqd">
    import Form from './form.vue'
    import cgJhSqdApi from '@/api/biz/cgJhSqdApi'
	import cgJhSpmxApi from "@/api/biz/cgJhSpmxApi";
	import gysApi from "@/api/biz/gysApi";

    let searchFormState = reactive({})
    const searchFormRef = ref()
    const table = ref()
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
			title: '部门名称',
			dataIndex: 'bmName'
		},
		{
			title: '类别',
			dataIndex: 'lbmc'
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
			title: '品牌产地',
			dataIndex: 'ppcd'
		},
		{
			title: '包装率',
			dataIndex: 'bzl'
		},
		{
			title: '计量单位',
			dataIndex: 'jldw'
		},
		{
			title: '订货数量',
			dataIndex: 'sqsl'
		},
		{
			title: '进货单价（元）',
			dataIndex: 'jhdj'
		},
		{
			title: '合计金额（元）',
			dataIndex: 'jhje'
		},
		{
			title: '申请人',
			dataIndex: 'sqr'
		},
        {
            title: '申请日期',
            dataIndex: 'sqrq'
        },
        {
            title: '采购类型',
            dataIndex: 'cglx'
        },
		{
			title: '状态',
			dataIndex: 'workstate'
		},


    ]

    const selectedRowKeys = ref([])

	const onOpen = (record)=>{
		visible.value = true
		searchFormState.cgdh=record.cgdh

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
        return gysApi.cgGysJhSpmxPage(Object.assign(parameter, searchFormParam)).then((data) => {
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

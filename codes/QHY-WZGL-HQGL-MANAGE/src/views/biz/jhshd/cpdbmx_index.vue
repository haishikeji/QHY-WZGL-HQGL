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
<!--			<a-row :gutter="24">-->
<!--				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">-->
<!--					<a-form-item label="供应商名称" name="gysmc">-->
<!--						<a-input v-model:value="searchFormState.gysmc" placeholder="请输入供应商名称" disabled/>-->
<!--					</a-form-item>-->
<!--				</a-col>-->

<!--				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">-->
<!--					<a-form-item label="收货日期：" name="shrq">-->
<!--						<a-date-picker v-model:value="searchFormState.shrq" disabled value-format="YYYY-MM-DD HH:mm:ss" show-time placeholder="请选择需货日期" style="width: 100%" />-->
<!--					</a-form-item>-->
<!--				</a-col>-->
<!--			</a-row>-->
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
			:scroll="{ x: 1800 }"
        >

        </s-table>
    </a-card>
    <Form ref="formRef" @successful="table.refresh(true)" />
	</a-modal>
</template>

<script setup name="jhshd">
    import Form from './form.vue'
    import cgJhSqdApi from '@/api/biz/cgJhSqdApi'
	import cgJhSpmxApi from "@/api/biz/cgJhSpmxApi";
	import tool from "@/utils/tool";
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
            title: '收货单号',
            dataIndex: 'shdh'
        },
		{
			title: '部门名称',
			dataIndex: 'bmName'
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
			title: '供货部门',
			dataIndex: 'gysmc'
		},

		{
			title: '计量单位',
			dataIndex: 'jldw'
		},
		{
			title: '申请数量',
			dataIndex: 'sqsl'
		},
		{
			title: '实收数量',
			dataIndex: 'shsl'
		},
		{
			title: '进货单价',
			dataIndex: 'jhdj'
		},
		{
			title: '供应单价',
			dataIndex: 'gydj'
		},
		{
			title: '进货金额',
			dataIndex: 'jhje'
		},
		{
			title: '供应金额',
			dataIndex: 'gyje'
		},
        {
            title: '申请日期',
            dataIndex: 'sqrq'
        },
		{
			title: '收货日期',
			dataIndex: 'shrq'
		},
        {
            title: '采购类型',
            dataIndex: 'cglx'
        },
        {
            title: '申请人',
            dataIndex: 'sqr'
        },
		{
			title: '收货人',
			dataIndex: 'shry'
		},
		{
			title: '状态',
			dataIndex: 'workstate'
		},

    ]

    const selectedRowKeys = ref([])

	const onOpen = (record)=>{

		const userInfo = tool.data.get('USER_INFO')
		console.log(record)
		visible.value = true
		searchFormState.shdh=record.shdh
		searchFormState.shrq = record.shrq;
		searchFormState.gysmc=record.gysmc
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

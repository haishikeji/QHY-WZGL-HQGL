<template>
	<a-modal
		v-model:visible="visible"
		title="明细"
		width="100%"
		:mask-closable="false"
		wrap-class-name="full-modal"
		:destroy-on-close="true"
	>
    <a-card :bordered="false">
        <a-form ref="searchFormRef" name="advanced_search" :model="searchFormState" class="ant-advanced-search-form">
            <a-row :gutter="24">

                <a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
                    <a-form-item label="商品代码" name="spdm">
                        <a-input v-model:value="searchFormState.spdm" placeholder="请输入商品代码" />
                    </a-form-item>
                </a-col>
                <a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
                    <a-form-item label="商品名称" name="spmc">
                        <a-input v-model:value="searchFormState.spmc" placeholder="请输入商品名称" />
                    </a-form-item>
                </a-col>
                <a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
                    <a-form-item label="调价日期" name="tjrq">
                        <a-range-picker v-model:value="searchFormState.tjrq" value-format="YYYY-MM-DD" />
                    </a-form-item>
                </a-col>
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24" v-show="advanced">
					<a-form-item label="商品类别" name="lbdm">
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
					<a-form-item >
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
        >
			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'action'">
					<a-space>
						<a @click="chart(record)">价格曲线</a>
					</a-space>
				</template>
			</template>
        </s-table>
    </a-card>
    <Form ref="formRef" @successful="table.refresh(true)" />
		<a-modal v-model:visible="chartVisible" title="价格曲线" width="100%"
				 wrap-class-name="full-modal" :footer="null">
			<iframe :src="src" width="100%" class="print-iframe" frameborder="0"></iframe>
		</a-modal>
	</a-modal>
</template>

<script setup name="kcspdmtj">
    import Form from './form.vue'
    import cgKcSpdmTjApi from '@/api/biz/cgKcSpdmTjApi'
	import cgKcSpdmApi from "@/api/biz/cgKcSpdmApi";
	import { message } from "ant-design-vue";
	import cgJhDhdApi from "@/api/biz/cgJhDhdApi";
	import tool from "@/utils/tool";
	import { ref } from "vue";
	import bizSplbTreeApi from "@/api/biz/bizSplbTreeApi";
    let searchFormState = reactive({tjshzt:"是"})
    const searchFormRef = ref()
    const table = ref()
    const formRef = ref()
	const visible = ref()
	const chartVisible = ref()
	const src = ref()
    const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }
    const columns = [
        {
            title: '商品代码',
            dataIndex: 'spdm'
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
            title: '前期进价',
            dataIndex: 'oldjj'
        },
        {
            title: '最新调价',
            dataIndex: 'zxtj'
        },
		{
			title: '最新供价',
			dataIndex: 'gydj'
		},
        {
            title: '调价日期',
            dataIndex: 'tjrq'
        },
		{
			title: '审核日期',
			dataIndex: 'tjshrq'
		},
        {
            title: '调价人',
            dataIndex: 'tjr'
        },
		{
			title: '审核人',
			dataIndex: 'tjshr'
		},
    ]
    // 操作栏通过权限判断是否显示

        columns.push({
            title: '操作',
            dataIndex: 'action',
            align: 'center',
            width: '150px'
        })
	const treeData = ref([])
	bizSplbTreeApi.bizSplbTree().then((res) => {
		treeData.value = res
	})
	const advanced = ref(false)
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
        // tjrq范围查询条件重载
        if (searchFormParam.tjrq) {
            searchFormParam.startTjrq = searchFormParam.tjrq[0]
            searchFormParam.endTjrq = searchFormParam.tjrq[1]
            delete searchFormParam.tjrq
        }
        return cgKcSpdmTjApi.cgKcSpdmTjPage(Object.assign(parameter, searchFormParam)).then((data) => {
            return data
        })
    }
    // 重置
    const reset = () => {
        searchFormRef.value.resetFields()
        table.value.refresh(true)
    }
    // 删除
    const deleteCgKcSpdmTj = (record) => {
        let params = [
            {
                id: record.id
            }
        ]
        cgKcSpdmTjApi.cgKcSpdmTjDelete(params).then(() => {
            table.value.refresh(true)
        })
    }
	// 批量审核
	const auditSptj = (params) => {
		const data={}
		data['tjshrq']=searchFormState.tjshrq
		data['cgKcSpdmList']=params
		cgKcSpdmApi.auditSptj(data).then(() => {
			table.value.clearRefreshSelected()
		})
	}
	const onOpen = (record)=>{
		visible.value = true

	}
	// 关闭抽屉
	const onClose = () => {
		visible.value = false
	}
	const chart = (record)=>{
		chartVisible.value=true
		src.value='https://hqncg.just.edu.cn/decision/view/report?viewlet=cgjkd%252Fcwtj%252Fjgzs.cpt&spdm='+record.spdm
		//src.value='http://bb.zjhtsoft.com/report/decision//view/report?viewlet=CG%252Fcgjkd%252Fbzycbhsb.cpt'
	}
	const toggleAdvanced = () => {
		advanced.value = !advanced.value
	}
	// 抛出函数
	defineExpose({
		onOpen
	})
</script>

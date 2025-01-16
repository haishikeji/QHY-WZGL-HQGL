<template>
    <a-card :bordered="false">
        <a-form ref="searchFormRef" name="advanced_search" :model="searchFormState" class="ant-advanced-search-form">
            <a-row :gutter="24">
                <a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
                    <a-form-item label="部门名称" name="bmdm">
<!--                        <a-input v-model:value="searchFormState.bmdm" placeholder="请输入部门名称" />-->
						<a-tree-select
							v-model:value="searchFormState.bmdm"
							show-search
							tree-node-filter-prop="name"
							style="width: 100%"
							:dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
							placeholder="请选择部门名称"
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
                    <a-button type="primary" @click="formRef.onOpen()" v-if="hasPerm('cgCodeBzglAdd')">
                        <template #icon><plus-outlined /></template>
                        新增
                    </a-button>
                    <xn-batch-delete
                        v-if="hasPerm('cgCodeBzglBatchDelete')"
                        :selectedRowKeys="selectedRowKeys"
                        @batchDelete="deleteBatchCgCodeBzgl"
                    />
                </a-space>
            </template>
            <template #bodyCell="{ column, record }">
                <template v-if="column.dataIndex === 'action'">
                    <a-space>
                        <a @click="formRef.onOpen(record)" v-if="hasPerm('cgCodeBzglEdit')">编辑</a>
                        <a-divider type="vertical" v-if="hasPerm(['cgCodeBzglEdit', 'cgCodeBzglDelete'], 'and')" />
                        <a-popconfirm title="确定要删除吗？" @confirm="deleteCgCodeBzgl(record)">
                            <a-button type="link" danger size="small" v-if="hasPerm('cgCodeBzglDelete')">删除</a-button>
                        </a-popconfirm>
                    </a-space>
                </template>
            </template>
        </s-table>
    </a-card>
    <Form ref="formRef" @successful="table.refresh(true)" />
</template>

<script setup name="codebzgl">
    import Form from './form.vue'
    import cgCodeBzglApi from '@/api/biz/cgCodeBzglApi'
	import bizBmTreeApi from "@/api/biz/bizBmTreeApi";
	import { cloneDeep } from "lodash-es";
	import cgKcSplbApi from "@/api/biz/cgKcSplbApi";
	import bizOrgApi from "@/api/biz/bizOrgApi";
	import bizBzTreeApi from "@/api/biz/bizBzTreeApi";
	// 抽屉状态
    let searchFormState = reactive({})
    const searchFormRef = ref()
	// 表单数据
	const formData = ref({})
	const submitLoading = ref(false)
	const visible = ref(false)
	const emit = defineEmits({ successful: null })
    const table = ref()
    const formRef = ref()
    const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }

	const treeData = ref([])
	const extJson = ref([])
    const columns = [

        {
            title: '班组代码',
            dataIndex: 'bzdm'
        },
        {
            title: '班组名称',
            dataIndex: 'bzmc'
        },

		{
			title: '部门名称',
			dataIndex: 'bmmc'
		},
        {
            title: '显示顺序',
            dataIndex: 'bzxh'
        },
        {
            title: '拼音简码',
            dataIndex: 'pyjm'
        },
        {
            title: '启用标志',
            dataIndex: 'qybz'
        },
        // {
        //     title: 'BZ',
        //     dataIndex: 'bz'
        // },
        // {
        //     title: '父id',
        //     dataIndex: 'parentId'
        // },
    ]
    // 操作栏通过权限判断是否显示

        columns.push({
            title: '操作',
            dataIndex: 'action',
            align: 'center',
            width: '150px'
        })


	// 打开抽屉
	const onOpen = (record) => {
		visible.value = true
		extJson.value = ref([])
		console.log(record)
		if (record) {
			let recordData = cloneDeep(record)
			formData.value = Object.assign({}, recordData)
		}
		// 获取商品类别树并加入顶级
		bizBzTreeApi.bizBzTree()().then((res) => {
			treeData.value = [
				{
					id: 0,
					parentId: '-1',
					name: '顶级',
					children: res
				}
			]
		})
	}

	const initOrg = () => {
		bizBmTreeApi.bizBmTree().then((res) => {
			treeData.value = [
				{
					id: 0,
					parentId: '-1',
					name: '顶级',
					children: res
				}
			]
		})
	}
	// 关闭抽屉
	const onClose = () => {
		formRef.value.resetFields()
		formData.value = {}
		visible.value = false
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
        return cgCodeBzglApi.cgCodeBzglPage(Object.assign(parameter, searchFormParam)).then((data) => {
            return data
        })
		// return bizBzTreeApi.bizBzTreePage(Object.assign(parameter, searchFormParam)).then((data) => {
		// 	return data
		// })
    }
    // 重置
    const reset = () => {
        searchFormRef.value.resetFields()
        table.value.refresh(true)
    }
    // 删除
    const deleteCgCodeBzgl = (record) => {
        let params = [
            {
                id: record.id
            }
        ]
        cgCodeBzglApi.cgCodeBzglDelete(params).then(() => {
            table.value.refresh(true)
        })
    }
    // 批量删除
    const deleteBatchCgCodeBzgl = (params) => {
        cgCodeBzglApi.cgCodeBzglDelete(params).then(() => {
            table.value.clearRefreshSelected()
        })
    }
	// 验证并提交数据
	const onSubmit = () => {
		formRef.value.validate().then(() => {
			submitLoading.value = true
			const formDataParam = cloneDeep(formData.value)
			cgKcSplbApi
				.cgKcSplbSubmitForm(formDataParam, !formDataParam.id)
				.then(() => {
					onClose()
					emit('successful')
				})
				.finally(() => {
					submitLoading.value = false
				})
		})
	}
	initOrg()
	// 抛出函数
	defineExpose({
		onOpen
	})
</script>

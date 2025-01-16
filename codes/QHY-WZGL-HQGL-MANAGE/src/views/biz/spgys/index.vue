<template>
    <a-card :bordered="false">
        <a-form ref="searchFormRef" name="advanced_search" :model="searchFormState" class="ant-advanced-search-form">
            <a-row :gutter="24">

                <a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
                    <a-form-item label="商品类别" name="id">
<!--                        <a-input v-model:value="searchFormState.lbdm" placeholder="请输入商品类别" />-->
						<a-tree-select
							show-search
							tree-node-filter-prop="name"
							style="width: 100%"
							:dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
							placeholder="请选择上级类别名称"
							allow-clear
							v-model:value="searchFormState.lbdm"
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
					<a-form-item label="商品代码" name="spdm">
						<a-input v-model:value="searchFormState.spdm" placeholder="请输入商品代码" />
					</a-form-item>
				</a-col>
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
					<a-form-item label="商品名称" name="spmc">
						<a-input v-model:value="searchFormState.spmc" placeholder="请输入商品名称" />
					</a-form-item>
				</a-col>
                <a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24" v-show="advanced">
                    <a-form-item label="启用标志（是、否）" name="qybz">
                        <a-select v-model:value="searchFormState.qybz" placeholder="请选择启用标志（是、否）" :options="qybzOptions" />
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
            <template #operator class="table-operator">
                <a-space>
					<xn-batch-operation
						:buttonName="'批量设置'"
						:title="'批量设置？'"
						:selectedRowKeys="selectedRowKeys"
						@batchOperation="batchSetGys"
					/>
					<a-button type="primary" @click="mxformRef.onOpen({lbdm:searchFormState.lbdm})">
						<template #icon><plus-outlined /></template>
						按类别查看
					</a-button>
                </a-space>
            </template>
            <template #bodyCell="{ column, record }">
                <template v-if="column.dataIndex === 'qybz'">
                    {{ $TOOL.dictTypeData('启用标志', record.qybz) }}
                </template>
                <template v-if="column.dataIndex === 'action'">
                    <a-space>
                        <a @click="mxformRef.onOpen(record)" >查看供货关系</a>
                    </a-space>
                </template>
            </template>
        </s-table>
    </a-card>
    <Form ref="formRef" @successful="table.refresh(true)" />
	<spgys ref="mxformRef" @successful="table.refresh(true)" />
</template>

<script setup name="kcspdm">
    import tool from '@/utils/tool'
    import Form from './form.vue'
	import spgys from './spgys_index.vue'
    import cgKcSpdmApi from '@/api/biz/cgKcSpdmApi'
	import { ref } from "vue";
	import bizSplbTreeApi from "@/api/biz/bizSplbTreeApi";
	import { message } from "ant-design-vue";
	import cgJhDhdApi from "@/api/biz/cgJhDhdApi";
    let searchFormState = reactive({})
	const visible = ref(false)
	const emit = defineEmits({ successful: null })
    const searchFormRef = ref()
    const table = ref()
    const formRef = ref()
	const mxformRef = ref()
	// 表单数据
	const formData = ref({})
	const submitLoading = ref(false)
	const uploadLoading = ref(false)
	const treeData = ref([])
	const extJson = ref([])
	const previewVisible = ref(false);
	const previewImage = ref('');
	const fileList = ref([

	]);
    const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }
    // 查询区域显示更多控制
    const advanced = ref(false)
    const toggleAdvanced = () => {
        advanced.value = !advanced.value
    }
    const columns = [
		{
			title: "商品代码",
			dataIndex: "spdm"
		},
		{
			title: "商品名称",
			dataIndex: "spmc"
		},
		{
			title: "规格",
			dataIndex: "spgg"
		},
		{
			title: "单位",
			dataIndex: "jldw",
			width:50
		},

		{
			title: "拼音",
			dataIndex: "pyjm",
			width:50,
			ellipsis: true
		},
		{
			title: "单价",
			dataIndex: "gydj",
			width:50,
		},
		{
			title: "品牌产地",
			dataIndex: "ppcd",
			ellipsis:true
		},
		{
			title: "包装率",
			dataIndex: "bzl"
		},
		{
			title: "启用",
			dataIndex: "qybz",
			width:50,
		},
		{
			title: "成本分类",
			dataIndex: "spfl",
			width:70,
		}

    ]
    // 操作栏通过权限判断是否显示
	columns.push({
		title: "操作",
		dataIndex: "action",
		align: "center",
		width: "150px"
	});
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
        return cgKcSpdmApi.cgKcSpdmPage(Object.assign(parameter, searchFormParam)).then((data) => {
            return data
        })
    }
    // 重置
    const reset = () => {
        searchFormRef.value.resetFields()
        table.value.refresh(true)
    }
    // 删除
    const deleteCgKcSpdm = (record) => {
        let params = [
            {
                id: record.id
            }
        ]
        cgKcSpdmApi.cgKcSpdmDelete(params).then(() => {
            table.value.refresh(true)
        })
    }
    // 批量删除
    const deleteBatchCgKcSpdm = (params) => {
        cgKcSpdmApi.cgKcSpdmDelete(params).then(() => {
            table.value.clearRefreshSelected()
        })
    }
	// 批量生成
	const batchSetGys = (record) => {
		console.log(record)
		formRef.value.onOpen(record)
	}
	const initSplb = () => {
		bizSplbTreeApi.bizSplbTree().then((res) => {
			treeData.value = [
				{
					id: "0",
					parentId: "-1",
					name: "全部",
					children: res
				}
			];
		});
	};

    initSplb()
    const qybzOptions = tool.dictList('启用标志')
</script>

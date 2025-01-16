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
							:tree-data="bmData"
							:field-names="{
						children: 'children',
						label: 'name',
						value: 'id'
					}"
							selectable="false"
							tree-line
						>

						</a-tree-select></a-form-item>
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

			<template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'nowjj'">
					<div>
						<a-input-number
							@change="jgChange(record)"
							v-model:value="record.nowjj"

						/>
					</div>
				</template>
				<template v-if="column.dataIndex === 'gydj'">
					<div>
						<a-input-number
							@change="jgChange(record)"
							v-model:value="record.gydj"

						/>
					</div>
				</template>
			</template>
        </s-table>
    </a-card>
    <Form ref="formRef" @successful="table.refresh(true)" />
	</a-modal>
</template>

<script setup name="kcspdm">
    import tool from '@/utils/tool'
    import Form from './form.vue'
    import cgKcSpdmApi from '@/api/biz/cgKcSpdmApi'
	import { ref } from "vue";
	import bizSplbTreeApi from "@/api/biz/bizSplbTreeApi";
	import { message } from "ant-design-vue";
	import cgKcKczbApi from "@/api/biz/cgKcKczbApi";
	import bizOrgApi from "@/api/biz/bizOrgApi";
    let searchFormState = reactive({})
	const visible = ref(false)
	const emit = defineEmits({ successful: null })
    const searchFormRef = ref()
    const table = ref()
    const formRef = ref()
	// 表单数据
	const formData = ref({})
	const submitLoading = ref(false)
	const uploadLoading = ref(false)
	const treeData = ref([])
	const bmData = ref([])
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
            title: '商品代码',
            dataIndex: 'spdm'
        },
        {
            title: '商品名称',
            dataIndex: 'spmc'
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
            title: '供应商名称',
            dataIndex: 'gysGysmc'
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
                selectedRowKeys.value = selectedRows
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
    // 批量保存
    const submit = (params) => {
		let isZero =false;
		params.forEach(record=>{
			if(record.nowjj<=0||record.gydj<=0){
				isZero=true;

			}
		})
		if(isZero){
			message.warning('请输入大于0的金额')
			return
		}
        cgKcSpdmApi.cgKcSpdmTj(params).then(() => {
            table.value.clearRefreshSelected()
        })
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
	const initBmdm = () => {
		bizOrgApi.orgTree().then((res) => {
			bmData.value = res
		})
	};
	const jgChange = (record) => {
		if((record.nowjj!=record.zxtj)||(record.gydj!=record.nowgydj)){
			record.isChange=true
		}
	}
    initSplb()
	initBmdm()
    const qybzOptions = tool.dictList('启用标志')
	const onOpen = (record)=>{
		visible.value = true
		searchFormState.lbdm=record.lbdm
		searchFormState.spdm=record.spdm
		searchFormState.spmc=record.spmc
	}
	// 关闭抽屉
	const onClose = () => {
		visible.value = false
	}
	// 抛出函数
	defineExpose({
		onOpen
	})
</script>

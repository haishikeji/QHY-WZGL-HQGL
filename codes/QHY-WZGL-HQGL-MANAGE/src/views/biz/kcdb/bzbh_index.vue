<template>
    <a-card :bordered="false">
        <a-form ref="searchFormRef" name="advanced_search" :model="searchFormState" class="ant-advanced-search-form">
            <a-row :gutter="24">
<!--				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">-->
<!--					<a-form-item label="部门名称" name="yjbmmc">-->
<!--						<a-input v-model:value="searchFormState.yjbmmc" placeholder="请输入一级部门名称" />-->
<!--					</a-form-item>-->
<!--				</a-col>-->
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
					<a-form-item label="部门名称" name="bmmc">
<!--						<a-input v-model:value="searchFormState.bmmc" placeholder="请输入部门名称" />-->
						<a-tree-select
							v-model:value="formData.parentId"
							show-search
							tree-node-filter-prop="name"
							style="width: 100%"
							:dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
							placeholder="请选择部门名称"
							allow-clear
							@change="bmChange"
							tree-default-expand-all
							:tree-data="treeData"
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
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24" >
					<a-form-item label="班组名称" name="ffbz">
						<a-select v-model:value="searchFormState.ffbz" placeholder="请输入分发班组"
								  :options="bzInfo">

						</a-select>
					</a-form-item>
				</a-col>
                <a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24" v-show="advanced">
                    <a-form-item label="申请日期" name="sqrq">
                        <a-range-picker v-model:value="searchFormState.sqrq" value-format="YYYY-MM-DD HH:mm:ss" show-time />
                    </a-form-item>
                </a-col>
                <a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24" >
                    <a-form-item label="状态" name="workstate">
						<a-select v-model:value="searchFormState.workstate" :defaultValue="dws">
							<a-select-option
								v-for="(item, index) in workstate"
								:key="index"
								:label="item"
								:value="item"
							></a-select-option>
						</a-select>
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
                    <a-button type="primary" @click="selectSp">
                        <template #icon><plus-outlined /></template>
                        新增
                    </a-button>
                    <xn-batch-delete
                        v-if="hasPerm('cgJhSpmxBatchDelete')"
                        :selectedRowKeys="selectedRowKeys"
                        @batchDelete="deleteBatchCgJhSpmx"
                    />
                </a-space>
            </template>
            <template #bodyCell="{ column, record }">
                <template v-if="column.dataIndex === 'action'">
                    <a-space>
                        <a @click="selectSp(record)" v-if="hasPerm('cgJhSpmxEdit')">明细</a>
                        <a-divider type="vertical" v-if="hasPerm(['cgJhSpmxEdit', 'cgJhSpmxDelete'], 'and')" />
                        <a-popconfirm title="确定要删除吗？" @confirm="deleteCgJhSpmx(record)">
                            <a-button type="link" danger size="small" v-if="hasPerm('cgJhSpmxDelete')">删除</a-button>
                        </a-popconfirm>
						<a-divider type="vertical"/>
						<a @click="sub(record)">提交</a>
                    </a-space>
                </template>
            </template>
        </s-table>
    </a-card>
    <Form ref="formRef" @successful="table.refresh(true)" />
	<menu-selector-plus
		ref="MenuSelectorPlus"
		page-url="/biz/user/roleSelector"
		org-url="/biz/user/orgTreeSelector"
		:role-global="false"
		@onBack="roleBack"
	/>
	<sub-form
		ref="SubForm"
		:role-global="false"
		@successful="table.refresh(true)"
	/>
</template>

<script setup name="bzbh">
    import Form from './form.vue'
    import cgJhSpmxApi from '@/api/biz/cgJhSpmxApi'
	import bizBzTreeApi from "@/api/biz/bizBzTreeApi";
	import bizOrgApi from "@/api/biz/bizOrgApi";
	import bizUserApi from "@/api/biz/bizUserApi";
	import menuSelectorPlus from '@/components/Selector/menuSelectorPlus.vue'
	import { notification } from "ant-design-vue";
	import cgJhSqdApi from "@/api/biz/cgJhSqdApi";
	import subForm from "./subForm.vue"
    let searchFormState = reactive({cglx:'班组订货',workstate:'申请中'})
	let MenuSelectorPlus = ref()
	let SubForm = ref()
    const searchFormRef = ref()
    const table = ref()
	const visible = ref(false)
	const formData = ref({})
	const submitLoading = ref(false)
    const formRef = ref()
	const treeData = ref([])
	const extJson = ref([])
    const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }
	const bzInfo = ref([])
	const workstate = ref(['申请中','已提交'])
	const dws = ref('申请中')
	const selectedRecord=ref([])
    // 查询区域显示更多控制
    const advanced = ref(false)
    const toggleAdvanced = () => {
        advanced.value = !advanced.value
    }
	const columns = [
		{
			title: '申请单号',
			dataIndex: 'sqdh'
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
			title: '申请人',
			dataIndex: 'sqr'
		},
		{
			title: '合计金额',
			dataIndex: 'hjje'
		},
		{
			title: '状态',
			dataIndex: 'workstate'
		},
	]
    // 操作栏通过权限判断是否显示

        columns.push({
            title: '操作',
            dataIndex: 'action',
            align: 'center',
            width: '200px'
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
        // sqrq范围查询条件重载
        if (searchFormParam.sqrq) {
            searchFormParam.startSqrq = searchFormParam.sqrq[0]
            searchFormParam.endSqrq = searchFormParam.sqrq[1]
            delete searchFormParam.sqrq
        }
        return cgJhSqdApi.cgJhSqdPage(Object.assign(parameter, searchFormParam)).then((data) => {
            return data
        })
    }
	//提交
	const sub = (record) => {

		SubForm.value.onOpen(record)
	}

	//选择商品
	const selectSp = (record) => {
		selectedRecord.value = record
		if(!searchFormState.bmdm||!searchFormState.ffbz){
			if(!record.sqdh){
			notification.error({
				message: '提示',
				description: '请先选择部门班组!'
			})
			return
			}
		}
		// 查询到已有角色，并转为ids的格式，给角色选择器
		const param = {
			id: 1,
			bmdm: searchFormState.bmdm,
			bzdm: searchFormState.ffbz,
			sqdh: record.sqdh
		}
		MenuSelectorPlus.value.showMenuModal(param)
	}
	//选择完成后回调
	const roleBack = (value) => {
		let params = {
			spdm: selectedRecord.value.spdm,
			spmxList: []
		}
		if (value.list.length > 0) {
			value.list.forEach((item) => {
				params.spmxList.push(item)
			})
		}
		cgJhSpmxApi.cgJhSpckmxSubmitForm(params,value.isEdit).then((res) => {
			table.value.refresh(true)
		})
	}
    // 重置
    const reset = () => {
        searchFormRef.value.resetFields()
        table.value.refresh(true)
    }
    // 删除
    const deleteCgJhSpmx = (record) => {
        let params = [
            {
                id: record.id
            }
        ]
        cgJhSqdApi.cgJhSqdDelete(params).then(() => {
            table.value.refresh(true)
        })
    }
    // 批量删除
    const deleteBatchCgJhSpmx = (params) => {
		cgJhSqdApi.cgJhSqdDelete(params).then(() => {
			table.value.clearRefreshSelected()
		})

    }

	const initOrg = () => {
		bizOrgApi.orgTree().then((res) => {
			treeData.value = res
		})
	}

    const bmChange =(e) => {
    	const param = {  "id" : e}
		searchFormState.bmdm=e
		bizBzTreeApi.bizBzList(param).then((res) => {
			searchFormState.ffbz = null
			bzInfo.value =res.map(item => ({ value: item.id,label:item.name }))
			searchFormState.ffbz = res[0].id
		})
	}

	initOrg()
</script>

<style>
.select {
	padding-left: 1100px;
}
</style>

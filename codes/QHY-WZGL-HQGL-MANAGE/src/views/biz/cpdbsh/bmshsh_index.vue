<template>
    <a-card :bordered="false" style="margin-bottom: 10px">
        <a-form ref="searchFormRef" name="advanced_search" :model="searchFormState" class="ant-advanced-search-form">
            <a-row :gutter="24">
<!--				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">-->
<!--					<a-form-item label="部门名称" name="yjbmmc">-->
<!--						<a-input v-model:value="searchFormState.yjbmmc" placeholder="请输入一级部门名称" />-->
<!--					</a-form-item>-->
<!--				</a-col>-->
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
					<a-form-item label="发货部门" name="bmmc">
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
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
					<a-form-item label="需货部门" name="gysmc">
						<!--						<a-input v-model:value="searchFormState.bmmc" placeholder="请输入部门名称" />-->

							<a-tree-select
								v-model:value="formData.gysdm"
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
                <a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
                    <a-form-item label="申请日期" name="sqrq">
                        <a-range-picker v-model:value="searchFormState.sqrq" value-format="YYYY-MM-DD HH:mm:ss" show-time />
                    </a-form-item>
                </a-col>
				<a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
					<a-form-item>
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
						:buttonName="'批量审核'"
						:title="'确认此信息?'"
						:selectedRowKeys="selectedRowKeys"
						@batchOperation="auditBatchCgJhSpckmx"
					/>
                </a-space>
            </template>
        </s-table>
    </a-card>

</template>

<script setup name="bzbh">
    import cgJhSpmxApi from '@/api/biz/cgJhSpmxApi'
	import bizBzTreeApi from "@/api/biz/bizBzTreeApi";
	import bizOrgApi from "@/api/biz/bizOrgApi";
	import bizUserApi from "@/api/biz/bizUserApi";
	import menuSelectorPlus from '@/components/Selector/menuSelectorPlus.vue'
	import { notification } from "ant-design-vue";
	import cgJhSqdApi from "@/api/biz/cgJhSqdApi";
	import cgCodeGysApi from "@/api/biz/cgCodeGysApi";
	import tool from "@/utils/tool";
    let searchFormState = reactive({cglx:'成品调拨',workstate:'收货中',ytsm:'部门'})
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
	const gysInfo = ref([])
	const workstate = ref(['申请中','收货中'])
	const dws = ref('申请中')
	const selectedRecord=ref([])
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
			title: '商品规格',
			dataIndex: 'spgg'
		},
		{
			title: '申请人',
			dataIndex: 'sqr'
		},
		{
			title: '申请时间',
			dataIndex: 'sqrq'
		},
		{
			title: '发货班组',
			dataIndex: 'gysmc'
		},
		{
			title: '收货数量',
			dataIndex: 'shsl',
			width:'100px'
		},
		{
			title: '申请人',
			dataIndex: 'sqr'
		},
		{
			title: '状态',
			dataIndex: 'workstate'
		},
	]
    // 操作栏通过权限判断是否显示

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

	const userInfo = ref(tool.data.get("USER_INFO"));
	const initOrg = () => {
		bizOrgApi.orgTree().then((res) => {
			treeData.value = res
		})
		bizOrgApi.orgTree().then((res) => {
			gysInfo.value = res
		})
		formData.value.parentId = userInfo.value.orgId;
		searchFormState.bmdm= userInfo.value.orgId;
	}
	initOrg()
    const loadData = (parameter) => {
        const searchFormParam = JSON.parse(JSON.stringify(searchFormState))
        // sqrq范围查询条件重载
        if (searchFormParam.sqrq) {
            searchFormParam.startSqrq = searchFormParam.sqrq[0]
            searchFormParam.endSqrq = searchFormParam.sqrq[1]
            delete searchFormParam.sqrq
        }
        return cgJhSpmxApi.cgJhSpmxPage(Object.assign(parameter, searchFormParam)).then((data) => {
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
    // 批量收货
    const auditBatchCgJhSpckmx = (params) => {
		cgJhSpmxApi.auditBatchCgJhSpmxCpdb(params).then(() => {
			table.value.clearRefreshSelected()
		})

    }



    const bmChange =(e) => {
    	const param = {  "id" : e}
		searchFormState.bmdm=e

	}


</script>

<style>
.select {
	padding-left: 1100px;
}
</style>

<template>
    <a-card :bordered="false">
        <a-form ref="searchFormRef" name="advanced_search" :model="searchFormState" class="ant-advanced-search-form">
            <a-row :gutter="24">
                <a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
					<a-form-item label="采购类型" name="cglx" >
						<a-radio-group v-model:value="searchFormState.cglx" @change="table.refresh(true)">

							<a-radio-button
								v-for="(item, index) in cglx"
								:key="index"
								:label="item.value"
								:value="item.value"
							>{{item.value}}</a-radio-button>

						</a-radio-group>
					</a-form-item>
                </a-col>
                <a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
                    <a-form-item label="申请日期" name="sqrq">
                        <a-range-picker v-model:value="searchFormState.sqrq" value-format="YYYY-MM-DD" />
                    </a-form-item>
                </a-col>
                <a-col :xxl="6" :xl="6" :lg="8" :md="12" :sm="24">
                    <a-form-item label="需货日期" name="xhrq">
                        <a-range-picker v-model:value="searchFormState.xhrq" value-format="YYYY-MM-DD" />
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
            :row-key="(record) => record.sqdh"
            :tool-config="toolConfig"
            :row-selection="options.rowSelection"
			:scroll="{ x: 1100 }"
			:pagination="{ pageSize: 100 }"
        >
            <template #operator class="table-operator">
				<a-space>
					<a-form-item label="送货日期" name="cgrq" style="margin-bottom: 0">
						<a-date-picker v-model:value="searchFormState.cgrq" value-format="YYYY-MM-DD HH:mm:ss" show-time />
					</a-form-item>
				</a-space>
				<a-space>
					<xn-batch-operation
						:buttonName="'生成并下单'"
						:title="'生成并下达此信息?'"
						:selectedRowKeys="selectedRowKeys"
						@batchOperation="generateBatchCgJhSqd"
					/>
				</a-space>
            </template>
            <template #bodyCell="{ column, record }">
				<template v-if="column.dataIndex === 'bmName'">
					{{record.bmName}}/{{record.bzName}}
				</template>

                <template v-if="column.dataIndex === 'action'">
                    <a-space>
                        <a @click="formRef.onOpen(record)">明细</a>
                        <a-divider type="vertical" v-if="record.workstate=='已审核'" />
                        <a-popconfirm title="确定要退回吗？" @confirm="backCgJhSqd(record)" v-if="record.workstate=='已审核'">
                            <a-button type="link" danger size="small">退回</a-button>
                        </a-popconfirm>
                    </a-space>
                </template>
            </template>
        </s-table>
    </a-card>
	<mxIndex ref="formRef" @successful="table.refresh(true);" />
	<mx ref="mxformRef" @successful="table.clearRefreshSelected();" />
</template>

<script setup name="jhsqd">
    import Form from './form.vue'
	import mxIndex from '@/views/biz/jhdhd/sqdmx_index.vue'
	import mx from '@/views/biz/jhdhd/hz_index.vue'
    import cgJhSqdApi from '@/api/biz/cgJhSqdApi'
	import cgJhDhdApi from "@/api/biz/cgJhDhdApi";
	import { message } from "ant-design-vue";
	import dayjs from "dayjs";
    let searchFormState = reactive({workstate:'已审核',cglx:'班组订货',xhrq:[dayjs().add(1,"day").format("YYYY-MM-DD"),dayjs().add(1,"day").format("YYYY-MM-DD")],cgrq:dayjs().hour(0).minute(0).second(0).add(1, "day").add(6, "hour").add(30, "minute").format("YYYY-MM-DD HH:mm:ss")})
    // console.log(dayjs(new Date().getTime() + 24 * 60 * 60 * 1000,"YYYY-MM-DD HH:mm:ss"))
	const searchFormRef = ref()
    const table = ref()
    const formRef = ref()
	const mxformRef = ref()

	const cglx = ref([{value:'班组订货',count:1},{value:'部门备货',count:0}])
    const toolConfig = { refresh: true, height: true, columnSetting: true, striped: false }
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
            title: '需货日期',
            dataIndex: 'xhrq'
        },
        {
            title: '采购类型',
            dataIndex: 'cglx'
        },
		{
			title: '申请部门(班组)',
			dataIndex: 'bmName'
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
			title: '需货备注',
			dataIndex: 'bz'
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
            width: '150px'
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
        // xhrq范围查询条件重载
        if (searchFormParam.xhrq) {
            searchFormParam.startXhrq = searchFormParam.xhrq[0]
            searchFormParam.endXhrq = searchFormParam.xhrq[1]
            delete searchFormParam.xhrq
        }


		let pageData={}
        return cgJhSqdApi.cgJhSqdPage(Object.assign(parameter, searchFormParam)).then((data) => {
			return data
        })
    }
    // 重置
    const reset = () => {
        searchFormRef.value.resetFields()
        table.value.refresh(true)
    }
    // 删除
    const backCgJhSqd = (record) => {
		let params = [
			{
				id: record.id
			}
		]
		cgJhSqdApi.backCgJhSqd(params).then(() => {
			table.value.refresh(true)
		})
    }
    // 批量生成
    const generateBatchCgJhSqd = (params) => {

		if(!searchFormState.cgrq||searchFormState.cgrq==''){
			message.warning('请选择送货日期！')
			return
		}
		const data={}
		data['cgrq']=searchFormState.cgrq
		data['cglx']=searchFormState.cglx
		data['idsList']=params
		mxformRef.value.onOpen(data);

    }
</script>
<style>
.ant-radio-button-wrapper{
	padding: 0 6px;
}
</style>

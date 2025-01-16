<template>
    <a-modal
        :title="formData.id ? '编辑班组' : '增加班组'"
        :width="600"
        :visible="visible"
        :destroy-on-close="true"
        :footer-style="{ textAlign: 'right' }"
		@cancel="onClose"
		:maskClosable="false"
    >
        <a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
            <a-row :gutter="16">
<!--                <a-col :span="12">-->
<!--                    <a-form-item label="一级部门代码：" name="yjbmdm">-->
<!--                        <a-input v-model:value="formData.yjbmdm" placeholder="请输入一级部门代码" allow-clear />-->
<!--                    </a-form-item>-->
<!--                </a-col>-->
<!--                <a-col :span="12">-->
<!--                    <a-form-item label="一级部门名称：" name="yjbmmc">-->
<!--                        <a-input v-model:value="formData.yjbmmc" placeholder="请输入一级部门名称" allow-clear />-->
<!--                    </a-form-item>-->
<!--                </a-col>-->
<!--                <a-col :span="12">-->
<!--                    <a-form-item label="部门代码：" name="bmdm">-->
<!--                        <a-input v-model:value="formData.bmdm" placeholder="请输入部门代码" allow-clear />-->
<!--                    </a-form-item>-->
<!--                </a-col>-->
                <a-col :span="12">
                    <a-form-item label="部门名称：" name="bmmc">
<!--                        <a-input v-model:value="formData.bmmc" placeholder="请输入部门名称" allow-clear />-->
						<a-tree-select
							v-model:value="formData.bmmc"
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
                <a-col :span="12">
                    <a-form-item label="班组代码：" name="bzdm">
                        <a-input v-model:value="formData.bzdm" placeholder="请输入班组代码" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="班组名称：" name="bzmc">
                        <a-input v-model:value="formData.bzmc" placeholder="请输入班组名称" allow-clear />
                    </a-form-item>
                </a-col>
<!--                <a-col :span="12">-->
<!--                    <a-form-item label="班组序号：" name="bzxh">-->
<!--                        <a-input v-model:value="formData.bzxh" placeholder="请输入班组序号" allow-clear />-->
<!--                    </a-form-item>-->
<!--                </a-col>-->
                <a-col :span="12">
                    <a-form-item label="拼音简码：" name="pyjm">
                        <a-input v-model:value="formData.pyjm" placeholder="请输入拼音简码" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="启用标志：" name="qybz">
<!--                        <a-input v-model:value="formData.qybz" placeholder="请输入启用标志" allow-clear />-->
						<a-radio-group name="qybz" v-model:value="formData.qybz">
							<a-radio value="是">是</a-radio>
							<a-radio value="否">否</a-radio>
						</a-radio-group>
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="备注：" name="bz">
<!--                        <a-input v-model:value="formData.bz" placeholder="请输入BZ" allow-clear />-->
						<a-textarea v-model:value="formData.bz" placeholder="请输入备注" :rows="4" />
                    </a-form-item>
                </a-col>
<!--                <a-col :span="12">-->
<!--                    <a-form-item label="父id：" name="parentId">-->
<!--                        <a-input v-model:value="formData.parentId" placeholder="请输入父id" allow-clear />-->
<!--                    </a-form-item>-->
<!--                </a-col>-->
            </a-row>
        </a-form>
        <template #footer>
            <a-button style="margin-right: 8px" @click="onClose">关闭</a-button>
            <a-button type="primary" @click="onSubmit" :loading="submitLoading">保存</a-button>
        </template>
    </a-modal>
</template>

<script setup name="cgCodeBzglForm">
    import { cloneDeep } from 'lodash-es'
    import { required } from '@/utils/formRules'
	import bizBmTreeApi from "@/api/biz/bizBmTreeApi";
    import cgCodeBzglApi from '@/api/biz/cgCodeBzglApi'
    // 抽屉状态
	let searchFormState = reactive({})
	const searchFormRef = ref()
    const visible = ref(false)
    const emit = defineEmits({ successful: null })
    const formRef = ref()
    // 表单数据
    const formData = ref({})
    const submitLoading = ref(false)
	const treeData = ref([])
	const extJson = ref([])

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

    // 打开抽屉
    const onOpen = (record) => {
        visible.value = true
        if (record) {
            let recordData = cloneDeep(record)
            formData.value = Object.assign({}, recordData)
        }
    }
    // 关闭抽屉
    const onClose = () => {
        formRef.value.resetFields()
        formData.value = {}
        visible.value = false
    }
    // 默认要校验的
    const formRules = {
    }
    // 验证并提交数据
    const onSubmit = () => {
        formRef.value.validate().then(() => {
            submitLoading.value = true
            const formDataParam = cloneDeep(formData.value)
            cgCodeBzglApi
                .cgCodeBzglSubmitForm(formDataParam, !formDataParam.id)
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

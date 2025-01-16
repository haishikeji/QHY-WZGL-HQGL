<template>
    <a-drawer
        :title="formData.id ? '编辑组织' : '增加组织'"
        :width="600"
        :visible="visible"
        :destroy-on-close="true"
        :footer-style="{ textAlign: 'right' }"
        @close="onClose"
    >
        <a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
            <a-row :gutter="16">
                <a-col :span="12">
                    <a-form-item label="部门名称：" name="bmdm">
						<a-tree-select
							v-model:value="formData.parentId"
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
						>

						</a-tree-select>
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="名称：" name="name">
                        <a-input v-model:value="formData.name" placeholder="请输入名称" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="编码：" name="code">
                        <a-input v-model:value="formData.code" placeholder="请输入编码" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="分类：" name="category">
                        <a-input v-model:value="formData.category" placeholder="请输入分类" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="排序码：" name="sortCode">
                        <a-input v-model:value="formData.sortCode" placeholder="请输入排序码" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="扩展信息：" name="extJson">
                        <a-input v-model:value="formData.extJson" placeholder="请输入扩展信息" allow-clear />
                    </a-form-item>
                </a-col>
            </a-row>
        </a-form>
        <template #footer>
            <a-button style="margin-right: 8px" @click="onClose">关闭</a-button>
            <a-button type="primary" @click="onSubmit" :loading="submitLoading">保存</a-button>
        </template>
    </a-drawer>
</template>

<script setup name="bizBzTreeForm">
    import { cloneDeep } from 'lodash-es'
    import { required } from '@/utils/formRules'
    import bizBzTreeApi from '@/api/biz/bizBzTreeApi'
	import bizOrgApi from "@/api/biz/bizOrgApi";
    // 抽屉状态
    const visible = ref(false)
    const emit = defineEmits({ successful: null })
    const formRef = ref()
	const treeData = ref([])
    // 表单数据
    const formData = ref({})
    const submitLoading = ref(false)

    // 打开抽屉
    const onOpen = (record) => {
		bizOrgApi.orgTree().then((res) => {
			treeData.value = res
		})
        visible.value = true
        if (record) {
            let recordData = cloneDeep(record)
            formData.value = Object.assign({}, recordData)
			formData.value.parentId=formData.value.bmdm
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
            bizBzTreeApi
                .bizBzTreeSubmitForm(formDataParam, !formDataParam.id)
                .then(() => {
                    onClose()
                    emit('successful')
                })
                .finally(() => {
                    submitLoading.value = false
                })
        })
    }
    // 抛出函数
    defineExpose({
        onOpen
    })
</script>

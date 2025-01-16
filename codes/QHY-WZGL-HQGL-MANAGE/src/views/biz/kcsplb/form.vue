<template>
    <a-modal
        :title="formData.id ? '编辑商品类别' : '增加商品类别'"
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
<!--                    <a-form-item label="商品大类代码（01）：" name="dldm">-->
<!--                        <a-input v-model:value="formData.dldm" placeholder="请输入商品大类代码（01）" allow-clear />-->
<!--                    </a-form-item>-->
<!--                </a-col>-->
                <a-col :span="12">
                    <a-form-item label="上级类别名称：" name="dlmc">
<!--                        <a-input v-model:value="formData.dlmc" placeholder="请输入上级类别名称" allow-clear />-->
							<a-tree-select
								v-model:value="formData.dlmc"
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
                <a-col :span="12">
                    <a-form-item label="类别代码：" name="lbdm">
                        <a-input v-model:value="formData.lbdm" placeholder="请输入类别代码" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="类别名称：" name="lbmc">
                        <a-input v-model:value="formData.lbmc" placeholder="请输入类别名称" allow-clear />
                    </a-form-item>
                </a-col>
<!--                <a-col :span="12">-->
<!--                    <a-form-item label="类别显示顺序：" name="lbxh">-->
<!--                        <a-input v-model:value="formData.lbxh" placeholder="请输入类别显示顺序" allow-clear />-->
<!--                    </a-form-item>-->
<!--                </a-col>-->
                <a-col :span="12">
                    <a-form-item label="拼音简码：" name="pyjm">
                        <a-input v-model:value="formData.pyjm" placeholder="请输入拼音简码" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="启用标志：" name="qybz">
<!--                        <a-input v-model:value="formData.qybz" placeholder="请输入启用标志（是、否）" allow-clear />-->

						<a-radio-group name="qybz" v-model:value="formData.qybz">
							<a-radio value="是">是</a-radio>
							<a-radio value="否">否</a-radio>
						</a-radio-group>

                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="备注：" name="bz">
<!--                        <a-input v-model:value="formData.bz" placeholder="请输入备注" allow-clear />-->
						<a-textarea v-model:value="formData.bz" placeholder="请输入备注" :rows="4" />
                    </a-form-item>
                </a-col>
            </a-row>
        </a-form>
        <template #footer>
            <a-button style="margin-right: 8px" @click="onClose">关闭</a-button>
            <a-button type="primary" @click="onSubmit" :loading="submitLoading">保存</a-button>
        </template>
    </a-modal>
</template>

<script setup name="cgKcSplbForm">
    import { cloneDeep } from 'lodash-es'
    import { required } from '@/utils/formRules'
    import cgKcSplbApi from '@/api/biz/cgKcSplbApi'
	import bizSplbTreeApi from "@/api/biz/bizSplbTreeApi";
	import bizBzTreeApi from "@/api/biz/bizBzTreeApi";
    // 抽屉状态
    const visible = ref(false)
    const emit = defineEmits({ successful: null })
    const formRef = ref()
    // 表单数据
    const formData = ref({})
    const submitLoading = ref(false)

	const treeData = ref([])
	const extJson = ref([])

    // 打开抽屉
    const onOpen = (record) => {
        visible.value = true
		extJson.value = ref([])
        if (record) {
            let recordData = cloneDeep(record)
            formData.value = Object.assign({}, recordData)
        }
		// 获取商品类别树并加入顶级
		bizSplbTreeApi.bizSplbTree().then((res) => {
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
    // 默认要校验的
    const formRules = {
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
    // 抛出函数
    defineExpose({
        onOpen
    })
</script>

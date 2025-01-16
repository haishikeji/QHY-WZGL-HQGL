<template>
    <a-card
        :title="'供应商信息'"
        :width="600"
        :footer-style="{ textAlign: 'right' }"
		style="overflow-y:scroll;"
    >
        <a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
            <a-row :gutter="16">
<!--                <a-col :span="24">-->
<!--                    <a-form-item label="供应商代码：" name="gysdm">-->
<!--                        <a-input v-model:value="formData.gysdm" placeholder="请输入供应商代码(主索引)" allow-clear />-->
<!--                    </a-form-item>-->
<!--                </a-col>-->
                <a-col :span="24">
                    <a-form-item label="供应商名称：" name="gysmc">
                        <a-input v-model:value="formData.gysmc" placeholder="请输入供应商名称" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="24">
                    <a-form-item label="拼音简码：" name="pyjm">
                        <a-input v-model:value="formData.pyjm" placeholder="请输入拼音简码" allow-clear />
                    </a-form-item>
                </a-col>
<!--                <a-col :span="24">-->
<!--                    <a-form-item label="供应商显示顺序：" name="gysxh">-->
<!--                        <a-input v-model:value="formData.gysxh" placeholder="请输入供应商显示顺序" allow-clear />-->
<!--                    </a-form-item>-->
<!--                </a-col>-->
                <a-col :span="24">
                    <a-form-item label="供应商类别：" name="gyslb">
                        <a-select v-model:value="formData.gyslb" placeholder="请选择供应商类别（个体或公司）" :options="gyslbOptions" />
                    </a-form-item>
                </a-col>
                <a-col :span="24">
                    <a-form-item label="信誉度：" name="xyd">
                        <a-select v-model:value="formData.xyd" placeholder="请选择信誉度（A、B、C）" :options="xydOptions" />
                    </a-form-item>
                </a-col>
                <a-col :span="24">
                    <a-form-item label="法人代表：" name="frdb">
                        <a-input v-model:value="formData.frdb" placeholder="请输入法人代表" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="24">
                    <a-form-item label="注册资本：" name="zczb">
                        <a-input v-model:value="formData.zczb" placeholder="请输入注册资本" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="24">
                    <a-form-item label="地址：" name="dz">
                        <a-input v-model:value="formData.dz" placeholder="请输入地址" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="24">
                    <a-form-item label="邮编：" name="yb">
                        <a-input v-model:value="formData.yb" placeholder="请输入邮编" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="24">
                    <a-form-item label="开户银行：" name="khyh">
                        <a-input v-model:value="formData.khyh" placeholder="请输入开户银行" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="24">
                    <a-form-item label="银行帐号：" name="yhzh">
                        <a-input v-model:value="formData.yhzh" placeholder="请输入银行帐号" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="24">
                    <a-form-item label="联系人：" name="lxr">
                        <a-input v-model:value="formData.lxr" placeholder="请输入联系人" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="24">
                    <a-form-item label="联系电话：" name="dh">
                        <a-input v-model:value="formData.dh" placeholder="请输入联系电话" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="24">
                    <a-form-item label="传真：" name="cz">
                        <a-input v-model:value="formData.cz" placeholder="请输入传真" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="24">
                    <a-form-item label="Email：" name="email">
                        <a-input v-model:value="formData.email" placeholder="请输入Email" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="24">
                    <a-form-item label="网址：" name="www">
                        <a-input v-model:value="formData.www" placeholder="请输入网址" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="24">
                    <a-form-item label="经营范围：" name="jyfw">
                        <a-textarea  v-model:value="formData.jyfw" placeholder="请输入经营范围" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="24">
                    <a-form-item label="设置日期：" name="szrq">
                        <a-date-picker v-model:value="formData.szrq" value-format="YYYY-MM-DD HH:mm:ss" show-time placeholder="请选择设置日期" style="width: 100%" />
                    </a-form-item>
                </a-col>
                <a-col :span="24">
                    <a-form-item label="备注：" name="bz">
						<a-textarea v-model:value="formData.bz" placeholder="请输入备注" :rows="4" />
                    </a-form-item>
                </a-col>
            </a-row>

				<a-col :span="24">
					<a-form-item >
						<a-button type="primary" @click="prcForm.onOpen(formData)">材料上传</a-button>
						<a-button type="primary" @click="onSubmit" :loading="submitLoading">保存</a-button>
					</a-form-item>
				</a-col>


        </a-form>
    </a-card>
	<prc-form ref="prcForm" @successful="table.refresh(true)" />
</template>

<script setup name="cgCodeGysForm">
    import tool from '@/utils/tool'
    import { cloneDeep } from 'lodash-es'
    import { required } from '@/utils/formRules'
    import cgCodeGysApi from '@/api/biz/cgCodeGysApi'
	import gysApi from "@/api/biz/gysApi";
	import PrcForm from '@/views/biz/gysprc/gys_index.vue'
    // 抽屉状态
    const visible = ref(false)
    const emit = defineEmits({ successful: null })
    const formRef = ref()
	const prcForm = ref()
	const table = ref()
    // 表单数据
    const formData = ref({})
    const submitLoading = ref(false)
    const gyslbOptions = ref([])
    const xydOptions = ref([])
    const ghztOptions = ref([])

    // 打开抽屉
    const onOpen = () => {
		let geturl = window.location.href
		let getqyinfo = geturl.split('?')[1]   //qycode=1001&qyname=%E4%BC%81%E4%B8%9A%E5%BF%99   截取到参数部分
		let getqys = new URLSearchParams('?'+getqyinfo)  //将参数放在URLSearchParams函数中
		let gysid = getqys.get('id')   //1001
		visible.value = true
		gysApi.getGysInfo({id:gysid}).then((res)=>{
			if (res) {
				let recordData = cloneDeep(res)
				formData.value = Object.assign({}, recordData)
			}
			gyslbOptions.value = tool.dictList('供应商类别')
			xydOptions.value = tool.dictList('信誉度')
			ghztOptions.value = tool.dictList('供货状态')
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
            cgCodeGysApi
                .cgCodeGysSubmitForm(formDataParam, !formDataParam.id)
                .then(() => {
                    emit('successful')
					location.reload()
                })
                .finally(() => {
                    submitLoading.value = false
                })
        })
    }
	// 上传
	const onUpload = () => {
		formRef.value.validate().then(() => {
			submitLoading.value = true
			const formDataParam = cloneDeep(formData.value)
			cgCodeGysApi
				.cgCodeGysSubmitForm(formDataParam, !formDataParam.id)
				.then(() => {
					emit('successful')
					location.reload()
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
	onOpen()
</script>

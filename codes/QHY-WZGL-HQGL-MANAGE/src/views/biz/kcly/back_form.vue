<template>
    <a-modal
        :title="'退库申请'"
        :width="600"
        :visible="visible"
        :destroy-on-close="true"
        :footer-style="{ textAlign: 'right' }"
        @cancel="onClose"
		:maskClosable="false"
    >
        <a-form ref="formRef" :model="formData" :rules="formRules" >
			<a-row :gutter="24">
				<a-form-item label="可申请数量：" name="ksqsl">
					{{formData.ksqsl}}
				</a-form-item>
				<a-form-item label="收货数量：" name="ksqsl">
					<span style="color:blue">{{ formData.cksl }}</span>
				</a-form-item>
				<a-form-item label="已退库数量：" name="ksqsl">
					<span style="color: red">{{ formData.ytksl }}</span>
				</a-form-item>
				<a-form-item label="申请中数量：" name="ksqsl">
					<span style="color: red">{{ formData.ysqsl }}</span>
				</a-form-item>
			</a-row>
            <a-row :gutter="24" >
				<a-col :span="16" >
					<a-form-item label="退库数量" name="sqsl">
						<a-input-number v-model:value="formData.sqsl" placeholder="请输入数量" allow-clear style="width:100%" />
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

<script setup name="cgKcKczbForm">
    import { cloneDeep } from 'lodash-es'
    import { required } from '@/utils/formRules'
    import cgKcKczbApi from '@/api/biz/cgKcKczbApi'
	import fileApi from "@/api/dev/fileApi";
	import bizSplbTreeApi from "@/api/biz/bizSplbTreeApi";
    // 抽屉状态
    const visible = ref(false)
    const emit = defineEmits({ successful: null })
    const formRef = ref()
    // 表单数据
    const formData = ref({})
    const submitLoading = ref(false)
	const activeKey = ref('Local')
	const uploadLoading = ref(false)
	const treeData = ref([])
	const bzInfo = ref([])
	const extJson = ref([])
	const previewVisible = ref(false);
	const previewImage = ref('');
	const fileList = ref([

	]);

    // 打开抽屉
    const onOpen = (record) => {
        visible.value = true
		extJson.value = ref([])
        if (record) {
            let recordData = cloneDeep(record)
            formData.value = Object.assign({}, recordData)
			delete formData.value.sqsl
			delete record.sqrq
			delete record.xhrq
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
		bizBzTreeApi.bizBzList({id:record.bmdm}).then((res) => {
			bzInfo.value = res
		})


		// 获取商品类别树并加入顶级
		cgJhSpmxApi.hasTkApply(record).then((res) => {
			if(!res){
				formData.value.ysqsl = 0;
			}else{
				formData.value.ysqsl = res.sqsl;
			}
			formData.value.ksqsl = NP.minus(formData.value.cksl, formData.value.ysqsl?formData.value.ysqsl:0, formData.value.ytksl?formData.value.ytksl:0);
		});

		// 获取商品类别树并加入顶级
		cgJhSpmxApi.hasTk(record).then((res) => {
			if(!res){
				formData.value.ytksl = 0;
			}else{
				formData.value.ytksl = res.cksl;
			}

			formData.value.ksqsl = NP.minus(formData.value.cksl, formData.value.ysqsl?formData.value.ysqsl:0, formData.value.ytksl?formData.value.ytksl:0);

		});
		bizBzTreeApi.bizBzList({ id: record.bmdm }).then((res) => {
			bzInfo.value = res;
		});
    }
    // 关闭抽屉
    const onClose = () => {
        formRef.value.resetFields()
        formData.value = {}
        visible.value = false
    }



	import { PlusOutlined } from '@ant-design/icons-vue';
	import { defineComponent, ref } from 'vue';
	import bizBzTreeApi from "@/api/biz/bizBzTreeApi";
	import { message } from "ant-design-vue";
	import cgJhSpmxApi from "@/api/biz/cgJhSpmxApi";
	import NP from "number-precision";
	function getBase64(file) {
		return new Promise((resolve, reject) => {
			const reader = new FileReader();
			reader.readAsDataURL(file);
			reader.onload = () => resolve(reader.result);
			reader.onerror = error => reject(error);
		});
	}
	const handleCancel = () => {
		previewVisible.value = false;
	};
	const handlePreview = async file => {
		if (!file.url && !file.preview) {
			file.preview = await getBase64(file.originFileObj);
		}
		previewImage.value = file.url || file.preview;
		previewVisible.value = true;
	};
	const handleChange = ({ fileList: newFileList }) => {
		fileList.value = newFileList;
	};



	// 上传本地文件
	const customRequestLocal = (data) => {
		uploadLoading.value = true
		const fileData = new FormData()
		fileData.append('file', data.file)
		fileApi
			.fileUploadLocalReturnUrl(fileData)
			.then(() => {
				emit('successful')
			})
			.finally(() => {
				uploadLoading.value = false
			})
	}
    // 默认要校验的
    const formRules = {
		sqsl: [required('请填入数量')],
    }
    // 验证并提交数据
    const onSubmit = () => {
        formRef.value.validate().then(() => {
            submitLoading.value = true
			if(formData.value.sqsl<0){
				message.error('请输入大于0的数量！')
				submitLoading.value = false
				return
			}
			if(formData.value.sqsl>formData.value.ksqsl){
				message.error('您填入的数量超过可申请数，请重新填写！')
				submitLoading.value = false
				return
			}

			formData.value.spmxList=bzInfo.value.filter(bz=>{
				if(bz.sqsl&&bz.sqsl>0){
					return bz
				}
			}).map(bz=>{
				return {bzdm:bz.id,sqsl:bz.sqsl}
			})
            const formDataParam = cloneDeep(formData.value)
            cgJhSpmxApi
                .cgJhSpckmxTkForm(formDataParam, !formDataParam.id)
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
<style>
.ant-upload-select-picture-card i {
	font-size: 32px;
	color: #999;
}

.ant-upload-select-picture-card .ant-upload-text {
	margin-top: 8px;
	color: #666;
}
</style>

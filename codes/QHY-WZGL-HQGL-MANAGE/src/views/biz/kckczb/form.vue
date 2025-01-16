<template>
    <a-modal
        :title="formData.id ? '编辑商品名称1' : '增加商品名称'"
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
<!--                    <a-form-item label="一级部门代码（如饮服中心）：" name="yjbmdm">-->
<!--                        <a-input v-model:value="formData.yjbmdm" placeholder="请输入一级部门代码（如饮服中心）" allow-clear />-->
<!--                    </a-form-item>-->
<!--                </a-col>-->
<!--                <a-col :span="12">-->
<!--                    <a-form-item label="一级部门名称：" name="yjbmmc">-->
<!--                        <a-input v-model:value="formData.yjbmmc" placeholder="请输入一级部门名称" allow-clear />-->
<!--                    </a-form-item>-->
<!--                </a-col>-->
<!--                <a-col :span="12">-->
<!--                    <a-form-item label="部门代码（食堂代码）：如0101：" name="bmdm">-->
<!--                        <a-input v-model:value="formData.bmdm" placeholder="请输入部门代码（食堂代码）：如0101" allow-clear />-->
<!--                    </a-form-item>-->
<!--                </a-col>-->
<!--                <a-col :span="12">-->
<!--                    <a-form-item label="部门名称（东苑食堂、学生食堂、圆中苑、西校区等）：" name="bmmc">-->
<!--                        <a-input v-model:value="formData.bmmc" placeholder="请输入部门名称（东苑食堂、学生食堂、圆中苑、西校区等）" allow-clear />-->
<!--                    </a-form-item>-->
<!--                </a-col>-->
<!--                <a-col :span="12">-->
<!--                    <a-form-item label="供应商代码：" name="gysdm">-->
<!--                        <a-input v-model:value="formData.gysdm" placeholder="请输入供应商代码" allow-clear />-->
<!--                    </a-form-item>-->
<!--                </a-col>-->
<!--                <a-col :span="12">-->
<!--                    <a-form-item label="供应商名称：" name="gysmc">-->
<!--                        <a-input v-model:value="formData.gysmc" placeholder="请输入供应商名称" allow-clear />-->
<!--                    </a-form-item>-->
<!--                </a-col>-->
				<a-col :span="12">
					<a-form-item label="类别名称：" name="lbmc">
<!--						<a-input v-model:value="formData.lbmc" placeholder="请输入类别名称" allow-clear />-->
						<a-tree-select
							v-model:value="formData.parentId"
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
                    <a-form-item label="商品代码：" name="spdm">
                        <a-input v-model:value="formData.spdm" placeholder="请输入商品代码" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="商品名称：" name="spmc">
                        <a-input v-model:value="formData.spmc" placeholder="请输入商品名称" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="商品规格：" name="spgg">
                        <a-input v-model:value="formData.spgg" placeholder="请输入商品规格" allow-clear />
                    </a-form-item>
                </a-col>
				<a-col :span="12">
					<a-form-item label="拼音简码：" name="pyjm">
						<a-input v-model:value="formData.pyjm" placeholder="请输入拼音简码" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="计量单位：" name="jldw">
						<a-input v-model:value="formData.jldw" placeholder="请输入计量单位" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="供应单价：" name="gydj">
						<a-input v-model:value="formData.gydj" placeholder="请输入供应单价" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="当前进价：" name="nowjj">
						<a-input v-model:value="formData.nowjj" placeholder="请输入当前进价" allow-clear />
					</a-form-item>
				</a-col>
                <a-col :span="12">
                    <a-form-item label="品牌产地：" name="ppcd">
                        <a-input v-model:value="formData.ppcd" placeholder="请输入品牌产地" allow-clear />
                    </a-form-item>
                </a-col>
                <a-col :span="12">
                    <a-form-item label="包装率：" name="bzl">
                        <a-input v-model:value="formData.bzl" placeholder="请输入包装率" allow-clear />
                    </a-form-item>
                </a-col>
				<a-col :span="12">
					<a-form-item label="审批标志：" name="spbz">
						<a-radio-group name="spbz" v-model:value="formData.spbz">
							<a-radio value="是">是</a-radio>
							<a-radio value="否">否</a-radio>
						</a-radio-group>
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="启用标志：" name="qybz">
						<a-radio-group name="qybz" v-model:value="formData.qybz">
							<a-radio value="是">是</a-radio>
							<a-radio value="否">否</a-radio>
						</a-radio-group>
					</a-form-item>
				</a-col>
<!--                <a-col :span="12">-->
<!--                    <a-form-item label="成本分类（荤菜类、素菜类、调料类、主食类），统计用：" name="spfl">-->
<!--                        <a-input v-model:value="formData.spfl" placeholder="请输入成本分类（荤菜类、素菜类、调料类、主食类），统计用" allow-clear />-->
<!--                    </a-form-item>-->
<!--                </a-col>-->
<!--                <a-col :span="12">-->
<!--                    <a-form-item label="商品显示顺序：" name="spxh">-->
<!--                        <a-input v-model:value="formData.spxh" placeholder="请输入商品显示顺序" allow-clear />-->
<!--                    </a-form-item>-->
<!--                </a-col>-->

<!--                <a-col :span="12">-->
<!--                    <a-form-item label="商品大类代码（01）：" name="dldm">-->
<!--                        <a-input v-model:value="formData.dldm" placeholder="请输入商品大类代码（01）" allow-clear />-->
<!--                    </a-form-item>-->
<!--                </a-col>-->
<!--                <a-col :span="12">-->
<!--                    <a-form-item label="商品大类名称（食品类、维修材料类、园林绿化类）：" name="dlmc">-->
<!--                        <a-input v-model:value="formData.dlmc" placeholder="请输入商品大类名称（食品类、维修材料类、园林绿化类）" allow-clear />-->
<!--                    </a-form-item>-->
<!--                </a-col>-->
<!--                <a-col :span="12">-->
<!--                    <a-form-item label="商品类别：" name="lbdm">-->
<!--                        <a-input v-model:value="formData.lbdm" placeholder="请输入商品类别" allow-clear />-->
<!--                    </a-form-item>-->
<!--                </a-col>-->



<!--                <a-col :span="12">-->
<!--                    <a-form-item label="库存报警下限：" name="kcxx">-->
<!--                        <a-input v-model:value="formData.kcxx" placeholder="请输入库存报警下限" allow-clear />-->
<!--                    </a-form-item>-->
<!--                </a-col>-->

<!--                <a-col :span="12">-->
<!--                    <a-form-item label="审批数量(进货时超过该数量需审批：" name="spsl">-->
<!--                        <a-input v-model:value="formData.spsl" placeholder="请输入审批数量(进货时超过该数量需审批" allow-clear />-->
<!--                    </a-form-item>-->
<!--                </a-col>-->
<!--                <a-col :span="12">-->
<!--                    <a-form-item label="入库数量(累计）：" name="rklj">-->
<!--                        <a-input v-model:value="formData.rklj" placeholder="请输入入库数量(累计）" allow-clear />-->
<!--                    </a-form-item>-->
<!--                </a-col>-->
<!--                <a-col :span="12">-->
<!--                    <a-form-item label="调拨入库数量(累计）：" name="dbrklj">-->
<!--                        <a-input v-model:value="formData.dbrklj" placeholder="请输入调拨入库数量(累计）" allow-clear />-->
<!--                    </a-form-item>-->
<!--                </a-col>-->
<!--                <a-col :span="12">-->
<!--                    <a-form-item label="销售出库数量(累计)：" name="cklj">-->
<!--                        <a-input v-model:value="formData.cklj" placeholder="请输入销售出库数量(累计)" allow-clear />-->
<!--                    </a-form-item>-->
<!--                </a-col>-->
<!--                <a-col :span="12">-->
<!--                    <a-form-item label="调拨出库数量(累计)：" name="dbcklj">-->
<!--                        <a-input v-model:value="formData.dbcklj" placeholder="请输入调拨出库数量(累计)" allow-clear />-->
<!--                    </a-form-item>-->
<!--                </a-col>-->
<!--                <a-col :span="12">-->
<!--                    <a-form-item label="实际库存(合计)：" name="sjkc">-->
<!--                        <a-input v-model:value="formData.sjkc" placeholder="请输入实际库存(合计)" allow-clear />-->
<!--                    </a-form-item>-->
<!--                </a-col>-->

<!--                <a-col :span="12">-->
<!--                    <a-form-item label="LDJJHBZ：" name="ldjjhbz">-->
<!--                        <a-input v-model:value="formData.ldjjhbz" placeholder="请输入LDJJHBZ" allow-clear />-->
<!--                    </a-form-item>-->
<!--                </a-col>-->
                <a-col :span="12">
                    <a-form-item label="备注：" name="bz">
						<a-textarea v-model:value="formData.bz" placeholder="请输入备注" :rows="4" />
                    </a-form-item>
                </a-col>
            </a-row>
        </a-form>


		<div class="clearfix">
			<a-upload
				list-type="picture-card"
				v-model:file-list="fileList"
				@preview="handlePreview"
			>
				<div v-if="fileList.length < 8">
					<plus-outlined />
					<div class="ant-upload-text">Upload</div>
				</div>
			</a-upload>
			<a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
				<img alt="example" style="width: 100%" :src="previewImage" />
			</a-modal>
		</div>

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



	import { PlusOutlined } from '@ant-design/icons-vue';
	import { defineComponent, ref } from 'vue';
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
    }
    // 验证并提交数据
    const onSubmit = () => {
        formRef.value.validate().then(() => {
            submitLoading.value = true
            const formDataParam = cloneDeep(formData.value)
            cgKcKczbApi
                .cgKcKczbSubmitForm(formDataParam, !formDataParam.id)
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

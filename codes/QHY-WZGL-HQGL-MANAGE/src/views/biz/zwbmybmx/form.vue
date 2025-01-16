<template>
    <a-drawer
        :title="formData.id ? '编辑cg_zw_bmybmx' : '增加cg_zw_bmybmx'"
        :width="600"
        :visible="visible"
        :destroy-on-close="true"
        :footer-style="{ textAlign: 'right' }"
        @close="onClose"
    >
        <a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
            <a-form-item label="日报编号（如：20100406）：" name="ybbh">
                <a-input v-model:value="formData.ybbh" placeholder="请输入日报编号（如：20100406）" allow-clear />
            </a-form-item>
            <a-form-item label="生成日期：" name="rq">
                <a-date-picker v-model:value="formData.rq" value-format="YYYY-MM-DD HH:mm:ss" show-time placeholder="请选择生成日期" style="width: 100%" />
            </a-form-item>
            <a-form-item label="操作员：" name="czy">
                <a-input v-model:value="formData.czy" placeholder="请输入操作员" allow-clear />
            </a-form-item>
            <a-form-item label="一级部门代码（如饮服中心）：" name="yjbmdm">
                <a-input v-model:value="formData.yjbmdm" placeholder="请输入一级部门代码（如饮服中心）" allow-clear />
            </a-form-item>
            <a-form-item label="一级部门名称：" name="yjbmmc">
                <a-input v-model:value="formData.yjbmmc" placeholder="请输入一级部门名称" allow-clear />
            </a-form-item>
            <a-form-item label="部门代码（食堂代码）：如0101：" name="bmdm">
                <a-input v-model:value="formData.bmdm" placeholder="请输入部门代码（食堂代码）：如0101" allow-clear />
            </a-form-item>
            <a-form-item label="部门名称（东苑食堂、学生食堂、圆中苑、西校区等）：" name="bmmc">
                <a-input v-model:value="formData.bmmc" placeholder="请输入部门名称（东苑食堂、学生食堂、圆中苑、西校区等）" allow-clear />
            </a-form-item>
            <a-form-item label="统计类别（0表示自动计算统计的种类，1表示手工输入的类别）：" name="tjlb">
                <a-input v-model:value="formData.tjlb" placeholder="请输入统计类别（0表示自动计算统计的种类，1表示手工输入的类别）" allow-clear />
            </a-form-item>
            <a-form-item label="商品类别：" name="lbdm">
                <a-input v-model:value="formData.lbdm" placeholder="请输入商品类别" allow-clear />
            </a-form-item>
            <a-form-item label="类别名称：" name="lbmc">
                <a-input v-model:value="formData.lbmc" placeholder="请输入类别名称" allow-clear />
            </a-form-item>
            <a-form-item label="类别类型（荤菜类、素菜类、调料类、主食类、水电气类、低耗类、酬金类）：" name="lblx">
                <a-input v-model:value="formData.lblx" placeholder="请输入类别类型（荤菜类、素菜类、调料类、主食类、水电气类、低耗类、酬金类）" allow-clear />
            </a-form-item>
            <a-form-item label="显示顺序：" name="lbxh">
                <a-input v-model:value="formData.lbxh" placeholder="请输入显示顺序" allow-clear />
            </a-form-item>
            <a-form-item label="支出金额：" name="outje">
                <a-input v-model:value="formData.outje" placeholder="请输入支出金额" allow-clear />
            </a-form-item>
            <a-form-item label="收入金额：" name="inje">
                <a-input v-model:value="formData.inje" placeholder="请输入收入金额" allow-clear />
            </a-form-item>
            <a-form-item label="BZ：" name="bz">
                <a-input v-model:value="formData.bz" placeholder="请输入BZ" allow-clear />
            </a-form-item>
        </a-form>
        <template #footer>
            <a-button style="margin-right: 8px" @click="onClose">关闭</a-button>
            <a-button type="primary" @click="onSubmit" :loading="submitLoading">保存</a-button>
        </template>
    </a-drawer>
</template>

<script setup name="cgZwBmybmxForm">
    import { cloneDeep } from 'lodash-es'
    import { required } from '@/utils/formRules'
    import cgZwBmybmxApi from '@/api/biz/cgZwBmybmxApi'
    // 抽屉状态
    const visible = ref(false)
    const emit = defineEmits({ successful: null })
    const formRef = ref()
    // 表单数据
    const formData = ref({})
    const submitLoading = ref(false)

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
            cgZwBmybmxApi
                .cgZwBmybmxSubmitForm(formDataParam, !formDataParam.id)
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

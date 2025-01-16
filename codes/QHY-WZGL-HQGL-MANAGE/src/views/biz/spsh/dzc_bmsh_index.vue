<template>

		<Form ref="formRef"  />

</template>

<script setup name="bmbh">
import Form from "./dzc_bmsh.vue";


const formRef = ref();
setTimeout(() => {
	formRef.value.onOpen({});
}, 1000);



</script>

<style>
.select {
	padding-left: 1100px;
}
</style>

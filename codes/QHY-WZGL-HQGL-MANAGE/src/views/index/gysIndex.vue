<template>
	<a-row :gutter="10">
		<a-col :span="16">
			<gysInfo class="mb-2" />
			<a-row :gutter="10">

			</a-row>
		</a-col>
	</a-row>
</template>

<script setup name="indexHome">
	import shortcutCard from '@/components/ShortcutCard/index.vue'
	import gysInfo from './components/gysInfo.vue'
	import shortcut from './components/shortcut.vue'

</script>

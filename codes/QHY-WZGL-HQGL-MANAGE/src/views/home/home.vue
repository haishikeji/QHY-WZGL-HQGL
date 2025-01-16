<template>
	<a-layout>
		<a-layout>
			<div id="snowyHeader" class="snowy-header">
				<div class="snowy-header-left title">
					{{ formData.title }}
				</div>
				<div class="snowy-header-right">
					<web-header></web-header>
				</div>
			</div>
		</a-layout>
		<a-layout style="padding: 0px 0px 25px 0px;">
			<a-carousel effect="fade" autoplay>
				<div v-for="image in formData.carouselList">
					<a-image :src="image.url" width="100%" height="150px" :preview="false"></a-image>
				</div>
			</a-carousel>
			<div :class="(ismobile?'mobile-':'')+'search'" >
				<a-input-search
					v-model:value="searchValue"
					placeholder="请输入关键字(如：书名、作者、出版社、年代等)"
					enter-button="查询"
					size="large"
					@search="onSearch"
				/>
				<div class="search-tags">
					<template v-for="tag in formData.tags" :key="tag">
						<a-tag
							color="transparent"
						>
							<a :style="'color:'+tag.color" class="tag" @click="handleTags(tag.name)">
								{{ tag.name }}
							</a>
						</a-tag>
					</template>
				</div>

			</div>

			<a-layout :style="{  padding: '24px',borderRadius:'15px'}" :class="(ismobile?'mobile-':'')+'main-content-top'">
				<a-layout-sider width="70%" style="background: transparent">
					<a-layout class="main-content">
						<h4 style="margin-bottom: 16px;" class="main-content-title">热门分类:</h4>
						<div>
							<template v-for="tag in formData.hotTags" :key="tag">
								<a-tag
									color="transparent"
								>
									<a class="tag main-content-content">
										{{ tag.name }}
									</a>
								</a-tag>
							</template>
						</div>
					</a-layout>
					<a-layout class="main-content" style="margin-top: 15px">
						<h4 style="margin-bottom: 16px;" class="main-content-title">特色资源:</h4>
						<div>
							<template v-for="tag in formData.featureTags" :key="tag">
								<a-tag
									color="transparent"
								>
									<a class="tag main-content-content">
										{{ tag.name }}
									</a>
								</a-tag>
							</template>
						</div>
					</a-layout>
					<a-layout class="main-content"  style="margin-top: 15px;height: auto">
						<h4 style="margin-bottom: 16px;" class="main-content-title">朝代直通:</h4>
						<div>
							<template v-for="tag in formData.dynastyTags" :key="tag">
								<a-tag
									color="transparent"
								>
									<a class="tag main-content-content">
										{{ tag.name }}
									</a>
								</a-tag>
							</template>
						</div>
					</a-layout>

				</a-layout-sider>
				<a-layout-content class="main-content">
					<h4 style="margin-bottom: 4px;" class="main-content-title">读者常读:</h4>
					<a-list item-layout="horizontal" :data-source="formData.commonList">
						<template #renderItem="{ item,index }">
							<a-list-item class="main-content-list-item" style="border-bottom:0px;">{{(index+1)+'. '+item.name }}</a-list-item>
						</template>
					</a-list>
				</a-layout-content>
			</a-layout>
		</a-layout>
		<a-layout :style="{ textAlign: 'center',height:'40px'}">
			{{ formData.copyright }}
		</a-layout>
	</a-layout>
</template>
<script setup name="home">
import { ref,computed } from "vue";
import tool from "@/utils/tool";
import store from "@/store";
import WebHeader from "@/layout/components/webHeader.vue";
import * as $store from "@antv/g2plot/lib/adaptor/common";

const formData = ref({
	title: "1",
	copyright: "蜂群科技 ©2023 Created by 蜂群科技",
	menuList: [{ code: "category", name: "分类页面" }, { code: "login", name: "登录" }],
	carouselList: [{ url: "//pic.616pic.com/bg_w1180/00/11/46/CINAlRovIX.jpg!/fw/1120" }, { url: "https://bpic.51yuansu.com/backgd/cover/00/02/71/5b569fc10e8d7.jpg?x-oss-process=image/resize,w_780/sharpen,100" }, { url: "https://images.uiiiuiii.com/wp-content/uploads/2019/06/i-bn20190622-1-08.jpg" }, { url: "https://images.uiiiuiii.com/wp-content/uploads/2019/06/i-bn20190622-1-11.jpg" }, { url: "https://images.uiiiuiii.com/wp-content/uploads/2019/06/i-bn20190622-1-09.jpg" }],
	tags: [{ name: "苏东坡", color: "#BD844B" }, { name: "唐寅", color: "black" }, {
		name: "虎丘塔",
		color: "#BD844B"
	}, { name: "堪舆图", color: "black" }, { name: "良渚文化", color: "black" }],
	hotTags: [{ name: "城建史", color: "black" }, { name: "革命史", color: "black" }, {
		name: "散文",
		color: "black"
	}, { name: "诗歌", color: "black" }, { name: "自传", color: "black" }, { name: "方志", color: "black" },
		{ name: "城建史", color: "black" }, { name: "革命史", color: "black" }, { name: "散文", color: "black" }, {
			name: "诗歌",
			color: "black"
		}],
	featureTags:[{ name: "堪舆图", color: "black" }, { name: "拓印图", color: "black" }, {
		name: "宗族族谱",
		color: "black"
	}, { name: "历史遗迹", color: "black" }],
	dynastyTags:[{ name: "夏朝", color: "black" }, { name: "商超", color: "black" }, {
		name: "西周",
		color: "black"
	}, { name: "东周", color: "black" }],
	commonList:[{id:'1',name:'诗经'},{id:'2',name:'中庸'},{id:'1',name:'齐民要术'},{id:'1',name:'梦溪笔谈'},{id:'1',name:'论语'},{id:'1',name:'聊斋志异'},{id:'1',name:'韩非子'},{id:'1',name:'黄帝内经'},{id:'1',name:'伤寒杂病论'},]
});
const searchValue = ref('')
const base = ref({ sysBaseConfig: tool.data.get("SNOWY_SYS_BASE_CONFIG") || store.state.global.sysBaseConfig });
const onSearch = (e) => {

};
const ismobile = computed(()=>{
	return store.state.global.ismobile
})

const onLayoutResize=()=>{
	const clientWidth = document.body.clientWidth
	if (clientWidth < 992) {
		store.commit('SET_ismobile', true)
	} else {
		store.commit('SET_ismobile', false)
	}
}

const handleTags=(e)=>{
	searchValue.value = e
}
onLayoutResize()
</script>
<style scoped>
.logo {
	color: rgb(24, 144, 255);
	float: left;
	font-size: 20px;
	lineHeight: '34px',
}

.panel-item {
	padding: 0 10px;
	cursor: pointer;
	height: 100%;
	display: flex;
	align-items: center;
}

.ant-layout-header {
	height: 34px;
	line-height: 34px;
}

.search {
	padding: 2% 25%;
}
.mobile-search{
	padding: 2% 0;
}
[data-theme='dark'] .site-layout .site-layout-background {
	background: #141414;
}

.ant-carousel :deep(.slick-slide) {
	text-align: center;
	height: 150px;
	line-height: 150px;
	background: #364d79;
	overflow: hidden;
}

.ant-carousel :deep(.slick-slide h3) {
	color: #fff;
}

.search-tags {
	margin: 3px 20px;
}

.title {
	font-family: 'lixuke';
	font-size: 20px;
	color: var(--text-color-inverse)
}

.tag {
	font-size: 14px;
}

</style>

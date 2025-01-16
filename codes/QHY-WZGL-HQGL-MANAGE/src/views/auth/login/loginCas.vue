<template>
	<a-spin :spinning="true">
		<iframe :src="ifsrc" style="display: none"></iframe>
	</a-spin>


</template>

<script>
	import loginApi from '@/api/auth/loginApi'
	import phoneLoginForm from './phoneLoginForm.vue'
	import threeLogin from './threeLogin.vue'
	import smCrypto from '@/utils/smCrypto'
	import { required } from '@/utils/formRules'
	import { afterGysLogin, afterLogin } from "./util";
	import config from '@/config'
	import configApi from '@/api/dev/configApi'
	import tool from '@/utils/tool'
	import store from '@/store'
	import gysApi from "@/api/auth/gysApi";
	import casApi from "@/api/auth/casApi";

	export default {
		name: 'Login',
		components: {
			phoneLoginForm,
			threeLogin
		},
		data() {
			return {
				activeKey: 'userAccount',
				sysBaseConfig: store.state.global.sysBaseConfig || tool.data.get('SNOWY_SYS_BASE_CONFIG'),
				validCodeBase64: '',
				ruleForm: {

					validCode: '',
					validCodeReqNo: '',
					autologin: false
				},
				rules: {
					account: [required(this.$t('login.accountError'), 'blur')],
					password: [required(this.$t('login.PWError'), 'blur')]
				},
				loading: false,
				gysLoading: false,
				casloading: false,
				config: {
					lang: tool.data.get('APP_LANG') || this.$CONFIG.LANG,
					theme: tool.data.get('APP_THEME') || 'default'
				},
				lang: [
					{
						name: '简体中文',
						value: 'zh-cn'
					},
					{
						name: 'English',
						value: 'en'
					}
				],
				ifsrc:''
			}
		},
		computed: {
			captchaOpen() {
				return this.sysBaseConfig.SNOWY_SYS_DEFAULT_CAPTCHA_OPEN === 'true'
			}
		},
		watch: {
			'config.theme': function (val) {
				document.body.setAttribute('data-theme', val)
			},
			'config.lang': function (val) {
				this.$i18n.locale = val
				this.$TOOL.data.set('APP_LANG', val)
			}
		},
		created() {
			store.commit('clearViewTags')
			store.commit('clearKeepLive')
			store.commit('clearIframeList')
		},
		mounted() {
			let formData = ref(config.SYS_BASE_CONFIG)
			//this.getCas()
			this.getCas()

			// if (JSON.stringify(this.getCas())!="{}"){
			// 	alert(2)
			// 	alert(JSON.stringify(this.getCas()))
			// 	if (JSON.stringify(this.getCas()).includes('ticket')) {
			// 		loginApi.caslogin().then((res)=>{
			//
			// 		})
			// 	} else {
			// 		this.gotoCas()
			// 	}
			// }else{
			// 	alert(3)
			// 	this.gotoCas()
			// }

			//loginApi.caslogin().then((res)=>{
			//
			// 		})
			// configApi.configSysBaseList().then((res)=>
			// {
			// 	alert(JSON.stringify(res))
			// 	if(JSON.stringify(res)!='{}'){
			// 		//afterLogin(res)
			// 	}
			// })


			configApi.configSysBaseList().then((data) => {
				if (data) {
					data.forEach((item) => {
						formData.value[item.configKey] = item.configValue
					})
					tool.data.set('SNOWY_SYS_BASE_CONFIG', formData.value)
					store.commit('SET_sysBaseConfig', formData.value)
					this.refreshSwitch()
					this.captchaOpen = formData.value.SNOWY_SYS_DEFAULT_CAPTCHA_OPEN
				}
			})
		},
		methods: {
			// 通过开关加载内容
			refreshSwitch() {
				// 判断是否开启验证码
				if (this.captchaOpen) {
					// 加载验证码
					this.loginCaptcha()
					// 加入校验
					this.rules.validCode = [required(this.$t('login.validError'), 'blur')]
				}
			},
			// 获取验证码
			loginCaptcha() {
				loginApi.getPicCaptcha().then((data) => {
					this.validCodeBase64 = data.validCodeBase64
					this.ruleForm.validCodeReqNo = data.validCodeReqNo
				})
			},
			// 用户名密码登录
			async login() {
				this.$refs.loginForm.validate().then(async () => {
					this.loading = true
					const loginData = {
						account: this.ruleForm.account,
						// 密码进行SM2加密，传输过程中看到的只有密文，后端存储使用hash
						password: smCrypto.doSm2Encrypt(this.ruleForm.password),
						validCode: this.ruleForm.validCode,
						validCodeReqNo: this.ruleForm.validCodeReqNo
					}
					// 获取token
					try {
						const loginToken = await loginApi.login(loginData)
						afterLogin(loginToken)
					} catch (err) {
						this.loading = false
					}
				})
			},
			// 供应商密码登录
			async gysLogin() {
				this.$refs.loginForm.validate().then(async () => {
					this.gysLoading = true
					const loginData = {
						account: this.ruleForm.account,
						// 密码进行SM2加密，传输过程中看到的只有密文，后端存储使用hash
						password: smCrypto.doSm2Encrypt(this.ruleForm.password),
						validCode: this.ruleForm.validCode,
						validCodeReqNo: this.ruleForm.validCodeReqNo,
						device: "GYS"
					}
					// 获取token
					try {
						const loginToken = await gysApi.gysLogin(loginData).then((res)=>{
							afterGysLogin(res)
						})

					} catch (err) {
						this.gysLoading = false
					} finally {
						this.gysLoading = false
					}
				})
			},
			configLang(key) {
				this.config.lang = key
			},
			checkTicket() {
				const url = location.search
				// 获取url中"?"符后的字串
				const theRequest = {}
				if (url.indexOf('?') !== -1) {
					const str = url.substr(1)
					const strs = str.split('&')
					for (let i = 0; i < strs.length; i++) {
						theRequest[strs[i].split('=')[0]] = unescape(strs[i].split('=')[1])
					}
				}
				return theRequest
			},
			getCas() {
				this.gysLoading = true
				//alert(this.checkTicket().ticket)
				if (this.checkTicket().ticket) {
					this.ifsrc = 'https://ids2.just.edu.cn/cas/login?service=https%3A%2F%2Fhqncg.just.edu.cn%3A8080%2Fcas'
					this.casLogin()
					this.gysLoading = false
					setTimeout(this.casLogin, 500)
				}else{
					setTimeout(this.gotocas, 200)
					this.gysLoading = false
				}
			},
			casLogin(){

				casApi.caslogin().then((data)=>{
					afterLogin(data.token)

				})
			},
			gotocas(){
				window.location.href = "https://ids2.just.edu.cn/cas/login?service=https%3A%2F%2Fhqncg.just.edu.cn%2FloginCas"
			}

		}
	}
</script>

<style lang="less">
	@import 'login';
</style>

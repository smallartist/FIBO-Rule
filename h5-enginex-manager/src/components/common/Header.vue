<template>
	<div class="header">
		<!-- 折叠按钮 -->
		<div class="collapse-btn" @click="collapseChage">
			<i v-if="!collapse" class="el-icon-s-fold"></i>
			<i v-else class="el-icon-s-unfold"></i>
		</div>
		<div class="logo">数字决策平台</div>
		<div class="header-right">
			<!-- {{fielduser}} -->
			<!-- {{ruleList}} -->

			<!-- {{fielduser==''}} -->
			<!-- {{ruleList==null}} -->
			<div class="header-user-con">

				<div style="display: flex;font-size: 14px;">

					<div v-for="value in link.e" class="header_link" @click="go(value)">
						{{value.title}}
					</div>



				</div>


				<!--  -->
				<div class="btn-fullscreen" @click="ReGetStorage">
					<!-- {{fielduser}} -->
					<!-- {{ruleList=='[]'}} -->
					<el-tooltip effect="dark" content="刷新缓存" placement="bottom">

						<i :class="Loading?'el-icon-loading':'el-icon-refresh'"></i>
					</el-tooltip>
				</div>

				<!-- 全屏显示 -->
				<div class="btn-fullscreen" @click="handleFullScreen">
					<el-tooltip effect="dark" :content="fullscreen?`取消全屏`:`全屏`" placement="bottom">
						<i class="el-icon-rank"></i>
					</el-tooltip>
				</div>

				<!-- 用户头像 -->
				<div class="user-avator">
					<img src="../../assets/img/img.jpg" />
				</div>
				<!-- 用户名下拉菜单 -->
				<el-dropdown class="user-name" trigger="click" @command="handleCommand">
					<span class="el-dropdown-link">
						{{username}}
						<i class="el-icon-caret-bottom"></i>
					</span>
					<el-dropdown-menu slot="dropdown">
						<el-dropdown-item divided command="loginout">退出登录</el-dropdown-item>
					</el-dropdown-menu>
				</el-dropdown>
			</div>
		</div>
	</div>
</template>
<script>
	import bus from '../common/bus';
	import {
		getLogout
	} from '../../api/index';
	import linkOr from '@/utils/link.js'
	export default {
		data() {
			return {
				collapse: false,
				fullscreen: false,
				name: '',
				message: 2,
				link: []
			};
		},
		created() {
			this.link = linkOr
			bus.$on('collapseHeader', res => {
				this.busCollapseChage(res)
			})
			bus.$on('regetcache', res => {

				this.$store.dispatch('regetcache', res)
			})

			this.$store.dispatch('getfielduser')
			this.$store.dispatch('getRuleList')


			Object.keys(this.$store.state.cacheList).forEach(value => {
				this.$store.dispatch('getcache', value)
			})


			if (!sessionStorage.getItem('reGet')) {
				this.$store.dispatch('reGetfielduser')
				this.$store.dispatch('regetcache', 'Interface')
				this.$store.dispatch('regetcache', 'Sourcelist')

				sessionStorage.setItem('reGet', 1)
			}




			this.$store.commit('setbarShrink', this.collapse)
		},
		computed: {
			username() {
				if (localStorage.getItem('ms_username')) {
					return localStorage.getItem('ms_username');
				} else {
					return "未命名"
				}
			},
			fielduser() {
				if (this.$store.state.FieldUser) {
					return this.$store.state.FieldUser.data.fieldList
				} else {

					return null
				}
			},
			ruleList() {
				if (this.$store.state.RuleList) {
					return this.$store.state.RuleList

				} else {

					return null
				}
			},
			Loading() {
				if (this.$store.state.FieldUser == null) {
					return true
				}
				if (this.$store.state.RuleList == null) {
					return true
				}
				let is = false
				Object.keys(this.$store.state.cacheList).forEach(value => {
					if (this.$store.state[value] == null) {
						is = true
					}
				})
				if (is) return true

				// if (this.$store.state.SCO == null) {
				// 	return true
				// }
				// if (this.$store.state.Interface == null) {
				// 	return true
				// }
				// if (this.$store.state.decisionTable == null) {
				// 	return true
				// }
				// if (this.$store.state.decisionTree == null) {
				// 	return true
				// }
				// if (this.$store.state.Sourcelist == null) {
				// 	return true
				// }
				// if (this.$store.state.Engine == null) {
				// 	return true
				// }



				return false

			}
		},
		methods: {
			
			go(value) {
				var url = window.location.href

				if (value[url.split('#')[0]]) {
					window.open(value[url.split('#')[0]])
				} else {
					this.$message.error('请检查跳转配置')
				}
			},
			ReGetStorage() {

				if (this.Loading) {
					return
				}

				this.mixinReGetStorage()
			},
			// 用户名下拉菜单选择事件
			handleCommand(command) {
				if (command == 'loginout') {
					// 调用退出登录接口
					getLogout();
					localStorage.removeItem("engineId");
					// localStorage.removeItem("token");
					localStorage.removeItem('ms_username');
					this.$router.push('/login');
				}
			},
			// Bus侧边栏折叠
			busCollapseChage(res) {
				this.collapse = res;
				this.$store.commit('setbarShrink', this.collapse)
				bus.$emit('collapse', this.collapse);
			},
			// 侧边栏折叠
			collapseChage() {
				this.collapse = !this.collapse;
				this.$store.commit('setbarShrink', this.collapse)
				bus.$emit('collapse', this.collapse);
			},
			// 全屏事件
			handleFullScreen() {
				let element = document.documentElement;
				if (this.fullscreen) {
					if (document.exitFullscreen) {
						document.exitFullscreen();
					} else if (document.webkitCancelFullScreen) {
						document.webkitCancelFullScreen();
					} else if (document.mozCancelFullScreen) {
						document.mozCancelFullScreen();
					} else if (document.msExitFullscreen) {
						document.msExitFullscreen();
					}
				} else {
					if (element.requestFullscreen) {
						element.requestFullscreen();
					} else if (element.webkitRequestFullScreen) {
						element.webkitRequestFullScreen();
					} else if (element.mozRequestFullScreen) {
						element.mozRequestFullScreen();
					} else if (element.msRequestFullscreen) {
						// IE11
						element.msRequestFullscreen();
					}
				}
				this.fullscreen = !this.fullscreen;
			}
		},
		mounted() {
			if (document.body.clientWidth < 1500) {
				this.collapseChage();
			}
		}
	};
</script>
<style scoped>
	.header {
		position: relative;
		box-sizing: border-box;
		width: 100%;
		height: 70px;
		font-size: 22px;
		color: #fff;
	}

	.header_link {
		padding: 4px;
		margin-right: 20px;
		color: #fff;
	}

	.header_link:hover {
		cursor: pointer;
	}


	.collapse-btn {
		float: left;
		padding: 0 21px;
		cursor: pointer;
		line-height: 70px;
	}

	.header .logo {
		float: left;
		width: 250px;
		line-height: 70px;
	}

	.header-right {
		float: right;
		padding-right: 50px;
	}

	.header-user-con {
		display: flex;
		height: 70px;
		align-items: center;
	}

	.btn-fullscreen {
		transform: rotate(45deg);
		margin-right: 5px;
		font-size: 24px;
	}

	.btn-bell,
	.btn-fullscreen {
		position: relative;
		width: 30px;
		height: 30px;
		text-align: center;
		border-radius: 15px;
		cursor: pointer;
	}

	.btn-bell-badge {
		position: absolute;
		right: 0;
		top: -2px;
		width: 8px;
		height: 8px;
		border-radius: 4px;
		background: #f56c6c;
		color: #fff;
	}

	.btn-bell .el-icon-bell {
		color: #fff;
	}

	.user-name {
		margin-left: 10px;
	}

	.user-avator {
		margin-left: 20px;
	}

	.user-avator img {
		display: block;
		width: 40px;
		height: 40px;
		border-radius: 50%;
	}

	.el-dropdown-link {
		color: #fff;
		cursor: pointer;
	}

	.el-dropdown-menu__item {
		text-align: center;
	}
</style>

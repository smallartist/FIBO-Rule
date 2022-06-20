<template>
	<div class="sidebar">
		<el-menu class="sidebar-el-menu" :default-active="onRoutes" :collapse="collapse" background-color="#324157"
		 text-color="#bfcbd9" active-text-color="#20a0ff" unique-opened :router="true">
			<template v-for="item in items">
				<template v-if="item.subs">
					<el-submenu :index="item.index" :key="item.index">
						<template slot="title">
							<i :class="item.icon"></i>
							<span slot="title">{{ item.title }}</span>
						</template>
						<template v-for="subItem in item.subs">
							<el-submenu v-if="subItem.subs" :index="subItem.index" :key="subItem.index">
								<template slot="title">{{ subItem.title }}</template>
								<el-menu-item v-for="(threeItem,i) in subItem.subs" :key="i" :index="threeItem.index">{{ threeItem.title }}</el-menu-item>
							</el-submenu>
							<el-menu-item v-else :index="subItem.index" :key="subItem.index">{{ subItem.title }}</el-menu-item>
						</template>
					</el-submenu>
				</template>
				<template v-else>
					<el-menu-item :index="item.index" :key="item.index">
						<i :class="item.icon"></i>
						<span slot="title">{{ item.title }}</span>
					</el-menu-item>
				</template>
			</template>
		</el-menu>
	</div>
</template>

<script>
	import bus from './bus';
	import {
		getMenus
	} from '../../api/index';
	export default {
		data() {
			return {
				collapse: false,
				items: [],
			};
		},
		computed: {
			onRoutes() {
				return this.$route.path
			}
		},
		created() {
			// 通过 Event Bus 进行组件间通信，来折叠侧边栏
			bus.$on('collapse', msg => {
				this.collapse = msg;
				// bus.$emit('setcanvasn',msg)
				bus.$emit('collapse-content', msg);
			});

			// 获取菜单接口
			this.getMenus()
		},

		methods: {
			async getMenus() {
				const data = await getMenus({entity:{resourceSystem:'EngineX'}});
			    // console.log('菜单接口返回的数据',data);
				if (data.status === "0") {
					this.$message.error(data.msg);
					if (data.error === "01000103") {
						this.$router.push('/login')
					}
				}

				this.items = data.data;
				if(this.items.length == 0){
					this.$message.error('您没有导航权限,请联系管理员');
				}
				// console.log("items", this.items);
			}
		}
	};
</script>

<style scoped>
	.sidebar {
		display: block;
		position: absolute;
		left: 0;
		top: 70px;
		bottom: 0;
		overflow-y: scroll;
	}

	.sidebar::-webkit-scrollbar {
		width: 0;
	}

	.sidebar-el-menu:not(.el-menu--collapse) {
		width: 250px;
	}

	.sidebar>ul {
		height: 100%;
	}
</style>

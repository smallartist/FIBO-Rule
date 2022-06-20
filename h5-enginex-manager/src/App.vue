<template>
	<div id="app">

		<!-- <keep-alive include="history"> -->
		<router-view></router-view>
		<!-- </keep-alive> -->
	</div>
</template>
<script>
	// import {
	// 	fieldusing
	// } from '@/api/index.js'
	export default {
		name: 'App',
		created() {
			
			// getV({page:2}).then(res=>{

			// 	console.log(String(res))
			// })
		}
	}
</script>
<style>
	@import "./assets/css/main.css";
	@import "./assets/css/color-dark.css";

	/*深色主题*/
	/*@import "./assets/css/theme-green/color-green.css";   浅绿色主题*/
	.setting-wrapper {
		display: flex;
		align-items: center;
		justify-content: space-between;
	}

	.setting-wrapper .line {
		margin: 0 10px;
	}

	.call-mode-wrapper,
	.setting-wrapper {
		width: 100%;
		margin-top: 20px;
	}

	.conditions-wrapper {
		/* display: flex;  */
		margin: 15px 0;
		/* align-items: center; */
		transition: all 0.3s;
	}

	.type3_submit_home {
		display: flex;
		align-items: center;
		justify-content: flex-end;
		padding-right: 10px;
		/* position: absolute; */
		/* left: 80%; */
		margin-top: 100px;
		/* top: 74.5vh; */
		/* bottom: 20px; */
		z-index: 999;
	}
</style>

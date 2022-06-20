<template>

	<div>
		<cont title="决策表" :getData="getDataFun"></cont>
	</div>



</template>

<script>
	import cont from '@/components/common/decision/decCont.vue'
	import {
		getDecisionTablesList, // 获取列表
		addListRules, //新增文件夹
		getRulesListTree, // 获取列表树
		updataListRules, // 删除更新列表树
		getrulesave,
		getDecisionTablesInfo,
		getRuleUpdata,
		updateDecisionTablesStatus, // 删启停
		fielddownTemplate,
		updateDecisionTables,
		addDecisionTables,
		updateDecisionTablesParent,
		getDecisionTablesVersionInfo,
		addDecVersion,
		copyDecVersion,
		updateDecVersion,
		updateDecVersionStatus
	} from '@/api/index.js'
	export default {
		name:'decisionTable',
		components: {
			cont
		},
		data() {
			return {
				getDataFun: {
					treeType:'3',
					row: [{
						label: '决策代码',
						row: 'code',
					}, {
						label: '决策名称',
						row: 'name'
					}, {
						label: '决策描述',
						row: 'description'
					}, {
						label: '状态',
						row: 'status',
						type: 'State'
					}, {
						label: '创建人',
						row: 'creatorName'
					}, {
						label: '创建时间',
						row: 'createTime',
						type: 'Time'
					}],
					redact: "dataManageRedact",
					async getTree(e){
						return await getRulesListTree(e).then(res => {
							return res
						})
					},
					async getlist(e) { // 获取列表
						return await getDecisionTablesList(e).then(res => {
							return res
						})
					},
					async addlist(e) { //新增文件夹
						return await addListRules(e).then(res => {
							return res
						})
					},
					async updatalist(e) {
						return await updataListRules(e).then(res => {
							return res
						})
					},
					async setsave(e) {
						return await addDecisionTables(e).then(res => {
							return res
						})
					},
					async getVersion(e) { //获取单个规则详细信息
						return await getDecisionTablesInfo(e).then(res => {
							return res
						})
					},
					async getInfo(e) { //获取单个规则详细信息
						return await getDecisionTablesVersionInfo(e).then(res => {
							return res
						})
					},
					async updatafield(e) {
						return await updateDecisionTables(e).then(res => {
							return res
						})
					},
					async fieldusing(e) {
						return await updateDecisionTablesStatus(e).then(res => {
							return res
						})
					},
					async down(e) {
						return await fielddownTemplate(e).then(res => {
							return res
						})
					},
					async updateParent(e) {
						return await updateDecisionTablesParent(e).then(res => {
							return res
						})
					},
					async addVersion(e) { //添加新版本
						return await addDecVersion(e).then(res => {
							return res
						})
					},
					async delectVersion(e) { //删除版本
						return await updateDecVersionStatus(e).then(res => {
							return res
						})
					},
					async copyVersion(e) { //复制新版本
						return await copyDecVersion(e).then(res => {
							return res
						})
					},
					async updateVersion(e) { //更新版本
						return await updateDecVersion(e).then(res => {
							return res
						})
					},
				},
			};
		},
		
	};
</script>

<style>

</style>

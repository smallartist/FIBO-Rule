<template>

	<div>
		<cont title="脚本规则集"  :getData="getDataFun" ></cont>
	</div>



</template>

<script>
	// import bus from '@/components/common/rule/bus.js'
	import cont from '@/components/common/rule/ruleCont.vue'
	import {
		getRulesList, // 获取列表
		addListRules, //新增文件夹
		getRulesListTree, // 获取列表树
		updataListRules, // 删除更新列表树
		getrulesave,
		getScriptRuleInfo, //groovy 获取单个规则
		
		getRuleUpdata,
		Rulesusing, // 删启停
		fielddownTemplate,
		ruleupdata,
		updateRuleParent,  //更新文件夹
		addRuleScriptVersion, //groovy 添加版本
		updateRuleScriptVersionStatus,//groovy 删除版本
		copyRuleScriptVersion,//groovy复制版本
		updateRuleScriptVersion, //groovy 版本重命名
		getRuleScriptVersionInfo // groovy获取具体规则版本
	} from '@/api/index.js'

	export default {
		name:'Knowledge',
		components: {
			cont
		},
		data() {
			return {
				getDataFun: {
					type:3,
					treeType:'7',
					row: [{
						label: '规则代码',
						row: 'code',
					}, {
						label: '规则名称',
						row: 'name'
					}, {
						label: '规则描述',
						row: 'description'
					}, {
						label: '状态',
						row: 'status',
						type: 'State'
					}, {
						label: '创建人',
						row: 'authorName'
					}, {
						label: '创建时间',
						row: 'created',
						type: 'Time'
					}],
					redact: "dataManageRedact",
					async getTree(e){
						return await getRulesListTree(e).then(res => {
							return res
						})
					},
					async getlist(e) { // 获取列表
						e.ruleInfo.difficulty=3
						return await getRulesList(e).then(res => {
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
						return await getrulesave(e).then(res => {
							return res
						})
					},
					async getVersion(e) { //获取单个规则版本信息
						return await getScriptRuleInfo(e).then(res => {
							return res
						})
					},
					async updatafield(e) {
						return await getRuleUpdata(e).then(res => {
							return res
						})
					},
					async fieldusing(e) {
						return await Rulesusing(e).then(res => {
							return res
						})
					},
					async down(e) {
						return await fielddownTemplate(e).then(res => {
							return res
						})
					},
					async updateParent(e) {
						return await updateRuleParent(e).then(res => {
							return res
						})
					},
					async getInfo(e) { //获取单个规则详细信息
						return await getRuleScriptVersionInfo(e).then(res => {
							return res
						})
					},
					async addVersion(e) { //添加新版本
						return await addRuleScriptVersion(e).then(res => {
							return res
						})
					},
					async delectVersion(e) { //删除版本
						return await updateRuleScriptVersionStatus(e).then(res => {
							return res
						})
					},
					async copyVersion(e) { //复制新版本
						return await copyRuleScriptVersion(e).then(res => {
							return res
						})
					},
					async updateVersion(e) { //更新版本
						return await updateRuleScriptVersion(e).then(res => {
							return res
						})
					},
				},
			};
		}
	};
</script>

<style>

</style>

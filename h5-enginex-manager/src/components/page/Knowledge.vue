<template>

	<div>
		<cont title="复杂规则集"  :getData="getDataFun" ></cont>
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
		getRuleInfo,
		getRuleUpdata,
		Rulesusing, // 删启停
		fielddownTemplate,
		ruleupdata,
		updateRuleParent,  //更新文件夹
		addRuleVersion,
		updateRuleVersionStatus,
		copyRuleVersion,
		updateRuleVersion,
		getRuleVersionInfo //获取具体规则
	} from '@/api/index.js'

	export default {
		name:'Knowledge',
		components: {
			cont
		},
		data() {
			return {
				getDataFun: {
					type:2,
					treeType:'5',
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
						e.ruleInfo.difficulty=2
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
					async getVersion(e) { //获取单个规则详细信息
						return await getRuleInfo(e).then(res => {
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
						return await getRuleVersionInfo(e).then(res => {
							return res
						})
					},
					async addVersion(e) { //添加新版本
						return await addRuleVersion(e).then(res => {
							return res
						})
					},
					async delectVersion(e) { //删除版本
						return await updateRuleVersionStatus(e).then(res => {
							return res
						})
					},
					async copyVersion(e) { //复制新版本
						return await copyRuleVersion(e).then(res => {
							return res
						})
					},
					async updateVersion(e) { //更新版本
						return await updateRuleVersion(e).then(res => {
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

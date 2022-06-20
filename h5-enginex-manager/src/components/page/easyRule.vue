<template>

	<div>
		<cont title="基础规则集"  :getData="getDataFun"></cont>
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
		getEasyRulesave,
		getEasyRuleUpdata,
		getEasyRuleInfo,
		ruleupdata,
		updateRuleParent  //更新文件夹
	} from '@/api/index.js'

	export default {
		name:'easyRule',
		components: {
			cont
		},

		data() {
			return {
				getDataFun: {
					type:1,
					treeType:'0',
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
						e.ruleInfo.difficulty=1
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
						return await getEasyRulesave(e).then(res => {
							return res
						})
					},
					
					async getInfo(e) { //获取单个规则详细信息
						return await getEasyRuleInfo({id:e}).then(res => {
							return res
						})
					},
					async updatafield(e) {
						return await getEasyRuleUpdata(e).then(res => {
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
					async fieldsubmit(e) {
						return await ruleupdata(e).then(res => {
							return res
						})
					},
					async updateParent(e) {
						return await updateRuleParent(e).then(res => {
							return res
						})
					}
				},
			};
		}
	};
</script>

<style>

</style>

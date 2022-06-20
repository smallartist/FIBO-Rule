<template>

	<div>
		<cont title="决策树" :getData="getDataFun"></cont>
	</div>



</template>

<script>
	import cont from '@/components/common/decisionTree/decTreeCont.vue'
	import {
		
		getDecisionTreeList, // 获取列表
		addListRules, //新增文件夹
		getRulesListTree, // 获取列表树
		updataListRules, // 删除更新列表树
		getrulesave,
		getDecisionTreeInfo,
		getRuleUpdata,
		updateDecisionTreeStatus, // 删启停
		fielddownTemplate,
		updateDecisionTree, //更新内容
		addDecisionTree,// 添加
		updateDecisionTreeFolder, // 移动文件夹
		getDecisionTreeVersionInfo,
		addDecTreeVersion, // 添加版本
		copyDecTreeVersion, // 复制版本 
		updateDecTreeVersion, // 重命名版本
		updateDecTreeVersionStatus //删除版本 
	} from '@/api/index.js'
	export default {
		name:'decisionTree',
		components: {
			cont
		},
		data() {
			return {
				getDataFun: {
					treeType:'4',
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
						return await getDecisionTreeList(e).then(res => {
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
					async setsave(e) { // 添加
						return await addDecisionTree(e).then(res => {
							return res
						})
					},
					async getVersion(e) { //获取单个规则详细信息
						return await getDecisionTreeInfo(e).then(res => {
							return res
						})
					},
					async getInfo(e) { //获取单个规则详细信息
						return await getDecisionTreeVersionInfo(e).then(res => {
							return res
						})
					},
					async updatafield(e) { //更新内容
						return await updateDecisionTree(e).then(res => {
							return res
						})
					},
					async fieldusing(e) {
						return await updateDecisionTreeStatus(e).then(res => {
							return res
						})
					},
					async down(e) {
						return await fielddownTemplate(e).then(res => {
							return res
						})
					},
					async updateParent(e) { // 移动文件夹
						return await updateDecisionTreeFolder(e).then(res => {
							return res
						})
					},
					async addVersion(e) { //添加新版本
						return await addDecTreeVersion(e).then(res => {
							return res
						})
					},
					async delectVersion(e) { //删除版本
						return await updateDecTreeVersionStatus(e).then(res => {
							return res
						})
					},
					async copyVersion(e) { //复制新版本
						return await copyDecTreeVersion(e).then(res => {
							return res
						})
					},
					async updateVersion(e) { //更新版本
						return await updateDecTreeVersion(e).then(res => {
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

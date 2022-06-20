<template>

	<div>
		<cont title="集合操作" :getData="getDataFun"></cont>
	</div>



</template>

<script>
	import cont from '@/components/common/listOperation/listOperation.vue'
	import {
		getListOperationList, // 获取列表
		addListRules, //新增文件夹
		getRulesListTree, // 获取列表树
		updataListRules, // 删除更新列表树
		getrulesave,
		getListOperation,
		getRuleUpdata,
		updateListOperationStatus, // 删启停
		fielddownTemplate,
		updateListOperation, //更新内容
		addListOperation,// 添加
		updateListOperationFolder, // 移动文件夹
		getDecisionTreeVersionInfo,
		addlistOperationVersion, // 添加版本
		copyListOperationVersion, // 复制版本 
		updatelistOperationVersion, // 重命名版本
		updatelistOperationStatusVersion //删除版本 
	} from '@/api/index.js'
	export default {
		name:'decisionTree',
		components: {
			cont
		},
		data() {
			return {
				getDataFun: {
					treeType:'8',
					row: [{
						label: '集合代码',
						row: 'code',
					}, {
						label: '集合名称',
						row: 'name'
					}, {
						label: '集合描述',
						row: 'description'
					}, {
						label: '类型',
						row: 'opType',
						fn: (res)=>res==1?'集合规则':'集合处理'
					}, {
						label: '状态',
						row: 'status',
						type: 'State'
					}, {
						label: '创建人',
						row: 'createUserName'
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
						return await getListOperationList(e).then(res => {
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
						return await addListOperation(e).then(res => {
							return res
						})
					},
					async getVersion(e) { //获取单个规则详细信息
						return await getListOperation(e).then(res => {
							return res
						})
					},
					async getInfo(e) { //获取单个规则详细信息
						return await getDecisionTreeVersionInfo(e).then(res => {
							return res
						})
					},
					async updatafield(e) { //更新内容
						return await updateListOperation(e).then(res => {
							return res
						})
					},
					async fieldusing(e) {
						return await updateListOperationStatus(e).then(res => {
							return res
						})
					},
					async down(e) {
						return await fielddownTemplate(e).then(res => {
							return res
						})
					},
					async updateParent(e) { // 移动文件夹
						return await updateListOperationFolder(e).then(res => {
							return res
						})
					},
					async addVersion(e) { //添加新版本
						return await addlistOperationVersion(e).then(res => {
							return res
						})
					},
					async delectVersion(e) { //删除版本
						return await updatelistOperationStatusVersion(e).then(res => {
							return res
						})
					},
					async copyVersion(e) { //复制新版本
						return await copyListOperationVersion(e).then(res => {
							return res
						})
					},
					async updateVersion(e) { //更新版本
						return await updatelistOperationVersion(e).then(res => {
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

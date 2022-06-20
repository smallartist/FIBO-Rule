<template>

	<div>
		<cont title="指标管理" :getData="getDataFun"></cont>
	</div>



</template>

<script>
	import cont from '@/components/common/cont.vue'
	import {
		getfieldList,addfieldList,getfieldListTree,updatafieldList,getfieldsave,getfieldInfo,updatafield,fieldusing,fielddownTemplate,fieldupdata
	} from '@/api/index.js'

	export default {
		name:'datamanage',
		components: {
			cont
		},
		data() {
			return {
				getDataFun: {
					type:1,
					row: [ {
						label: '序号',
						row: 'id'
					}, {
						label: '字段名称',
						row: 'fieldEn'
					}, {
						label: '字段中文名',
						row: 'fieldCn'
					}, {
						label: '字段类型',
						row: 'valueType',
						type: 'type'
					},  {
						label: '状态',
						row: 'status',
						type: 'State'
					}, {
						label: '创建人',
						row: 'nickName'
					},{
						label: '创建时间',
						row: 'created',
						type: 'Time'
					} ],
					redact: "dataManageRedact",
					async getTree(e){
						return await getfieldListTree(e).then(res => {
							return res
						})
					},
					async getlist(e) {
						e.fType = 1
						return await getfieldList(e).then(res => {
							return res
						})
					},
					async addlist(e) {
						return await addfieldList(e).then(res => {
							return res
						})
					},
					async updatalist(e) {
					
						return await updatafieldList(e).then(res => {
							return res
						})
					},
					async setsave(e) {
						
						return await getfieldsave(e).then(res => {
							return res
						})
					},
					async getInfo(e) {
						
						console.log(e)
						return await getfieldInfo(e).then(res => {
							return res
						})
					},
					async updatafield(e) {
						
						return await updatafield(e).then(res => {
							return res
						})
					},
					async fieldusing(e) {
						
						return await fieldusing(e).then(res => {
							return res
						})
					},
					async down(e) {
						
						return await fielddownTemplate(e).then(res => {
							return res
						})
					},
					async fieldsubmit(e) {
					
						return await fieldupdata(e).then(res => {
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

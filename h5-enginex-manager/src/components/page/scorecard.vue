<template>

	<div>
		<cont title="评分卡管理" :getData="getDataFun"></cont>
	</div>



</template>

<script>
	import cont from '@/components/common/SCO/SCOCont.vue'
	import {
		getSCOList, // 获取列表
		addListRules, //新增文件夹
		getRulesListTree, // 获取列表树
		updataListRules, // 删除更新列表树
		addSCO, // 新增list
		getSCOVersion, // 获取数据回显
		getSCOUpdata, //更改保存
		updataListSCO, // 删启停
		fielddownTemplate,
		getSCOInfo,
		addScorecardVersion, // 添加新版本
		updateScorecardVersionStatus, //更新版本状态
		copyScorecardVersion, //复制版本
		updateScorecardVersion  //重命名版本
	} from '@/api/index.js'

	export default {
		name:'scorecard',
		components: {
			cont
		},

		data() {
			return {
				getDataFun: {
					treeType:'1',
					row: [{
						label: '评分卡代码',
						row: 'code',
					}, {
						label: '评分卡名称',
						row: 'name'
					}, {
						label: '评分卡描述',
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
						row: 'updated',
						type: 'Time'
					}],
					redact: "dataManageRedact",
					async getTree(e){
						return await getRulesListTree(e).then(res => {
							return res
						})
					},
					async getlist(e) { // 获取列表
						return await getSCOList(e).then(res => {
							return res
						})
					},
					async addlist(e) { //新增文件夹
						return await addListRules(e).then(res => {
							return res
						})
					},
					async updatalist(e) { // 更新列表
						return await updataListRules(e).then(res => {
							return res
						})
					},
					async setsave(e) { // 新增列表
						return await addSCO(e).then(res => {
							return res
						})
					},
					async getVersion(e) { //获取单个规则详细信息
						return await getSCOVersion(e).then(res => {
							return res
						})
					},
					async getInfo(e) { //获取单个规则详细信息
						return await getSCOInfo(e).then(res => {
							return res
						})
					},
					async updatafield(e) { // 更新单个规则详细信息
						return await getSCOUpdata(e).then(res => {
							return res
						})
					},
					async fieldusing(e) { //删启停
						return await updataListSCO(e).then(res => {
							return res
						})
					},
					async down(e) { //下载
						return await fielddownTemplate(e).then(res => {
							return res
						})
					},
					async addVersion(e) { //添加新版本
						return await addScorecardVersion(e).then(res => {
							return res
						})
					},
					async delectVersion(e) { //删除版本
						return await updateScorecardVersionStatus(e).then(res => {
							return res
						})
					},
					async copyVersion(e) { //复制新版本
						return await copyScorecardVersion(e).then(res => {
							return res
						})
					},
					async updateVersion(e) { //更新版本
						return await updateScorecardVersion(e).then(res => {
							return res
						})
					},
				},
			};
		},
		methods: {
			getListTree() { // 获取列表树
				getRulesListTree({
					parentId: 0,
					treeType: '1',
					type: 1,
				}).then(res => {
					console.log(res)
					this.contLeftLoading = false

					this.data = res.data
					this.listTree = []
					this.listTreeDeep(this.data, 1)
					this.opening(99999999)
				})
			},
			listTreeDeep(item, num) {

				// console.log(item)
				item.forEach((value) => {
					// if(value.status!==-1){
					this.$set(this.listTree, this.listTree.length, {
						id: value.id,
						parentId: value.parentId,
						name: value.name,
						ZIndex: num,
						open: false,
						show: num === 1 ? true : false,
						iconshow: value.children.length > 0 ? true : false,
						Rename: false
					})
					if (value.children.length > 0) {
						this.listTreeDeep(value.children, num + 1)
					}

				})
			},
			opening(id) {
				this.showRight = true
				let temp = -1
				let type = null
				this.listTree.forEach((value, index) => {

					if (value.id === id) {
						value.open = !value.open
						temp = value.ZIndex
						if (value.open === true) {
							type = true
						} else if (value.open === false) {
							type = false
						}
					} else {
						if (type === true) {
							if (value.ZIndex == temp + 1) {
								value.show = !value.show
							} else if (value.ZIndex == temp) {
								temp = -1
							}
						} else if (type === false) {
							if (value.ZIndex > temp) {
								value.show = false
								value.open = false
							} else if (value.ZIndex == temp) {
								temp = 99
							}

						}
					}



				})

			}
		}
	};
</script>

<style>

</style>

<style>
	.listOp_top {

		display: flex;
	}

	.listOp_cont {}

	.listOp_table_header {
		width: 350px;
		border-left: 1px solid #ddd;
		border-bottom: 1px solid #ddd;
	}

	.listOp_table_comm {}

	.listOp_score {
		width: 199px;
		height: 80px;
		text-align: center;
		line-height: 80px;
		border-left: 1px solid #ddd;
		border-bottom: 1px solid #ddd;
		box-sizing: border-box;
	}

	.listOp_home {
		border-top: 1px solid #ddd;
		overflow: scroll;
		flex: 1;
	}
</style>
<template>
	<!-- :style="{width:$store.state.barShrink?'95vw':'85vw'}" -->
	<div class="dataManageRedact" v-loading="loading">

		<div :class="smallHeader?'MR_header MR_headerSmall':'MR_header'">
			<div>
				<div>
					<el-button type="primary" icon="el-icon-arrow-left" circle @click="mixinClose"></el-button>
				</div>
				<div>
					<span v-if="id===0">新增数据清洗 :</span>
					<span v-else>编辑数据清洗 :</span>
				</div>
			</div>
			<div>
				<el-button :icon="smallHeader?'el-icon-bottom':'el-icon-top'" circle @click="openHeader">
				</el-button>
				<el-button type="success" icon="el-icon-check" circle @click="submit" :disabled="addVersionStatus">
				</el-button>
			</div>
		</div>
		<div :class="smallHeader?'MR_input MR_inputSmall':'MR_input'">
			<div>
				<p>数据清洗代码: </p>
				<el-input placeholder="请输入数据清洗代码" maxlength="30" v-model="code" clearable :disabled="addVersionStatus">
				</el-input>
			</div>
			<div>
				<p>数据清洗名称:</p>
				<el-input placeholder="请输入数据清洗名称" maxlength="20" v-model="name" clearable :disabled="addVersionStatus">
				</el-input>
			</div>

		</div>
		<div :class="smallHeader?'MR_scope MR_scopeSmall':'MR_scope'">
			<div>
				<p>数据清洗描述: </p>
				<el-input placeholder="请输入决策描述" maxlength="300" v-model="description" clearable
					:disabled="addVersionStatus" style="margin-left: 20px;"></el-input>


			</div>

		</div>

		<version style="margin:5px;" :id="id" :ruleVersionList="versionList" :version="version" @addVersion="addVersion"
			@addVersionClose="addVersionClose" @copyVersion="copyVersion" @delectVersion="delectVersion"
			@updateVersion="updateVersion" @addVersionSure="addVersionSure" :addVersionDialog="addVersionDialog"
			@Dialog="addVersionDialog=$event" @addVersionExamine="addVersionExamine"
			:addVersionLoading="addVersionLoading" @versionChange="versionChange" :addVersionStatus="addVersionStatus"
			@StatusChange="addVersionStatus=$event"></version>
		<!-- 版本部分 -->

		<div class="listOp_home">

			<opType :opType="opType" :data="version" @opFieldChange="opFieldChange"></opType>

		</div>

	</div>
</template>

<script>
	import opType from './opType.vue'
	import mangeRedactMixin from '@/utils/contminxin/MangeRedactMixin.js'
	import '@/assets/css/ManageRedact.css'
	import version from '@/components/common/Version.vue'

	const versionOrigin = {
		inputFieldEn: '',
		originalDataOp: {
			opField: [],
			opType: 'iteration',
			child: {}
		},
		conditionAndOutPutList: [{
			// eachList: '',
			condition: {
				logical: "&&",
				conditionType: 1,
				children: []
			},
			defaultOutput: [],
			resultFilterCondition: {
				conditionType: 1,
				logical: "&&",
				children: [],
				variableType: 1
			},
			inputFilterCondition: {
				conditionType: 1,
				logical: "&&",
				children: [],
				variableType: 1
			},
			failOutput: [],
			successOutput: [],
			dataCleanBlock: {
				name: "",
				opType: 'original',
				groupFields: [],
				handleCollection: []
			}
		}]
	}

	export default {
		mixins: [mangeRedactMixin],
		components: {
			version,
			opType
		},
		props: {
			fieldTypeId: {
				type: Number,
				default: 0,
			},
			getData: {
				type: Object,
				default () {
					return {}
				}
			},
			id: {
				type: Number,
				default: 0
			},
			type: {
				type: Number,
				default: 1
			},
			nameId: {
				type: Number,
				default: 0
			}
		},

		data() {
			return {
				addVersionStatus: false,
				addVersionLoading: false,
				addVersionDialog: false,
				tempadd: {},
				loading: false,
				priority: '',
				code: '',
				name: '',
				description: '',
				parentId: '',
				versionList: [],
				version: {},
				opType: 2,


			}
		},
		created() {

			if (this.id != 0) {
				this.loading = true
				this.getData.getVersion(this.id).then(res => {
					if (res.status == '1') {
						this.code = res.data.code
						this.name = res.data.name

						this.priority = res.data.priority
						this.description = res.data.description
						this.parentId = res.data.folderId



						// 版本部分
						this.versionList = res.data.versionList
						let obj = JSON.parse(JSON.stringify(this.versionList[0]))


						this.Enformat(obj, 'Array')
						console.log(obj)
						this.version = obj
						this.loading = false
					}
				})
			} else {
				// 新增初始化
				this.version = JSON.parse(JSON.stringify(versionOrigin))
				console.log(this.version)
			}
		},

		methods: {
			opFieldChange(e) {
				this.version.conditionAndOutPutList = JSON.parse(JSON.stringify(versionOrigin.conditionAndOutPutList))

				console.log(this.version)


			},
			// opTypeChange(e) {
			// 	if (e == 1) {
			// 		this.version.conditionAndOutPutList = [this.version.conditionAndOutPutList[0]]
			// 	} else if (e == 2) {
			// 		this.version.groupFields = []
			// 	}



			// },
			versionChange(e) {
				let obj = JSON.parse(JSON.stringify(e))
				this.Enformat(obj, 'Array')


				// debugger
				this.version = obj
				this.loading = false
			},
			addVersionExamine() {

				if (this.getIsempty(this.version)) {
					return
				}
				this.addVersionDialog = true
			},
			addVersionSure(tempVersion) {
				this.addVersionLoading = true


				let obj = JSON.parse(JSON.stringify(this.version))
				obj.versionCode = tempVersion.versionCode
				obj.description = tempVersion.description
				obj.dataCleanId = this.id
				
				try {
					if (Array.isArray(JSON.parse(this.mixinGetFieldByEn(this.version.inputFieldEn).jsonValue))) {
						obj.inputFieldType = 'list'
					} else {
						obj.inputFieldType = 'map'
					}
				} catch (e) {
					this.$message.error('源数据选择出错')
					this.addVersionLoading = false
					return
				}
				
				
				
				
				this.Enformat(obj, 'String')





				this.getData.addVersion(obj).then(res => {
					if (res.status == '1') {
						this.$message({
							message: '添加版本成功',
							type: 'success'
						});

						this.versionList = res.data
						this.versionChange(this.versionList[0])

					}
					this.loading = false
					this.addVersionStatus = false
					this.addVersionDialog = false
					this.addVersionLoading = false
				})
			},
			updateVersion(tempVersion) {
				if (this.getIsempty(this.version)) {
					return
				}
				this.addVersionLoading = true
				let obj = JSON.parse(JSON.stringify(this.version))
				obj.versionCode = tempVersion.versionCode
				obj.description = tempVersion.description
				obj.dataCleanId = this.id
				try {
					if (Array.isArray(JSON.parse(this.mixinGetFieldByEn(this.version.inputFieldEn).jsonValue))) {
						obj.inputFieldType = 'list'
					} else {
						obj.inputFieldType = 'map'
					}
				} catch (e) {
					this.$message.error('源数据选择出错')
					this.addVersionLoading = false
					return
				}
				this.Enformat(obj, 'String')

				this.getData.updateVersion(obj).then(res => {
					if (res.status == '1') {
						this.$message({
							message: '版本重命名成功',
							type: 'success'
						});

						this.versionList = res.data
						this.versionChange(this.versionList[0])

					}
					this.addVersionDialog = false
					this.addVersionLoading = false
				})
			},
			delectVersion() {
				this.getData.delectVersion({
					status: -1,
					ids: [this.version.id],
					strategyId: this.id
				}).then(res => {
					if (res.status == "1") {
						this.$message({
							message: '删除成功',
							type: 'success'
						});

						this.versionList = res.data
						this.versionChange(this.versionList[0])
					}
				})
			},
			copyVersion(tempVersion) {
				this.addVersionLoading = true

				this.getData.copyVersion({
					dataCleanId: this.id,
					id: this.version.id,
					versionCode: tempVersion.versionCode,
					description: tempVersion.description
				}).then(res => {
					if (res.status == '1') {
						this.$message({
							message: '复制版本成功',
							type: 'success'
						});

						this.versionList = res.data
						this.versionChange(this.versionList[0])

					}
					this.loading = false
					this.addVersionDialog = false
					this.addVersionLoading = false
				})
			},
			addVersionClose() {
				this.version = this.tempadd
				this.tempadd = {}
			},
			addVersion() {
				this.tempadd = JSON.parse(JSON.stringify(this.version))

				this.reset()
			},

			reset() {
				this.version = JSON.parse(JSON.stringify(versionOrigin))
			},
			Enformat(obj, str) {
				if(obj.originalDataOp.opField){
					obj.originalDataOp.opField = getformat(obj.originalDataOp.opField)
				}



				obj.conditionAndOutPutList.forEach(value => {


					value.dataCleanBlock.handleCollection = getformat(value.dataCleanBlock.handleCollection)

					value.successOutput.forEach(item => {
						if (str == 'String') {
							item.opType = item.outputKey.shift()
						} else {
							item.outputKey = item.opType + '.' + item.outputKey
						}
						item.outputKey = getformat(item.outputKey)
						if (item.variableType == 2) {
							item.outputValue = getformat(item.outputValue)
						}
					})
					value.failOutput.forEach(item => {

						if (str == 'String') {
							item.opType = item.outputKey.shift()
						} else {
							item.outputKey = item.opType + '.' + item.outputKey
						}

						item.outputKey = getformat(item.outputKey)
						if (item.variableType == 2) {
							item.outputValue = getformat(item.outputValue)
						}
					})


					if (str == 'Array') {
						if (value.dataCleanBlock.groupFields) {

							value.dataCleanBlock.groupFields = JSON.parse(value.dataCleanBlock.groupFields)
						} else {
							value.dataCleanBlock.groupFields = []
						}
					}

					value.dataCleanBlock.opType = 'handle_collection'
					value.dataCleanBlock.groupFields = value.dataCleanBlock.groupFields.map(item => {
						if (str == 'String') {
							item.shift()
						} else {
							item = 'handle_collection.' + item
						}
						return getformat(item)
					})
					if (str == 'String') {
						value.dataCleanBlock.groupFields = JSON.stringify(value.dataCleanBlock.groupFields)
					}

					value.resultFilterCondition.children.forEach(item => {



						if (str == 'String') {
							item.opType = item.opKey.shift()
						} else {
							item.opKey = item.opType + '.' + item.opKey
						}

						item.opKey = getformat(item.opKey)
					})
					value.inputFilterCondition.children.forEach(item => {

						if (str == 'String') {
							item.opType = item.opKey.shift()
						} else {
							item.opKey = item.opType + '.' + item.opKey
						}

						item.opKey = getformat(item.opKey)
					})

					deepformat([value.condition])

					// if(value.condition.children.length>0){
					// value.defaultOutput = []
					// }else{
					// 	value.defaultOutput = value.successOutput
					// }

				})

				function deepformat(arr) {
					arr.forEach(value => {
						if (value.children && value.children.length > 0) {
							deepformat(value.children)
						}

						if (value.conditionType == 2) {


							if (str == 'String') {
								value.opType = value.opKey.shift()
							} else {
								value.opKey = value.opType + '.' + value.opKey
							}

							value.opKey = getformat(value.opKey)

							if (value.variableType == 2) {
								value.variableValue = getformat(value.variableValue)
							}

						}


					})
				}


				function getformat(arr) {
					if (str == "String") {
						// debugger
						console.log(arr)
						return arr.join('.')
					} else {
						return arr.split('.')
					}
				}

			},
			examine() {
				let reg = /[\u4e00-\u9fa5]+/g;
				if (this.code.match(reg) != null) {
					this.$message.error('代码不允许出现中文');
					return true
				}
				if (this.verificationCode(this.code) || this.verificationName(this.name)) {
					return true
				}
				if (this.code.trim() == '') {
					this.$message.error('请填入数据清洗代码，并检查空格');
					return true
				}
				if (this.name.trim() == '') {
					this.$message.error('请填入数据清洗名称，并检查空格');
					return true
				}
				if (this.description.trim() == '') {
					this.$message.error('请填入数据清洗描述，并检查空格');
					return true
				}

				let is = {
					is: false,
					msg: '提交失败，请检查是否有未填项'
				}


				if (is.is) {
					this.$message.error(is.msg)
					return true
				}

			},
			getIsempty(obj) {
				let is = {
					is: false,
					msg: ''
				}


				if (!obj.inputFieldEn) {
					is.is = true
					is.msg = '请选择 源数据 '
				}

				// if (!obj.originalDataOp.opField || obj.originalDataOp.opField.length == 0) {
				// 	is.is = true
				// 	is.msg = '请选择 循环数据 '
				// }








				obj.conditionAndOutPutList.forEach(value => {

					if (!value.dataCleanBlock.handleCollection.length) {
						is.is = true
						is.msg = '请选择 集合'
					}

					value.resultFilterCondition.children.forEach(value => {
						if (!value.opKey || value.opKey.length == 0) {
							is.is = true
							is.msg = '请选择 返回过滤的 Key'
						}


						if ((!value.variableValue || value.variableValue.trim() == '')&&value.operator!='min') {
							is.is = true
							is.msg = '请填写 返回过滤的 的值'
						}

					})
					value.inputFilterCondition.children.forEach(value => {
						if (!value.opKey || value.opKey.length == 0) {
							is.is = true
							is.msg = '请选择 进入过滤的 Key'
						}


						if ((!value.variableValue || value.variableValue.trim() == '')&&value.operator!='min') {
							is.is = true
							is.msg = '请填写 进入过滤的 的值'
						}

					})











					value.dataCleanBlock.groupFields.forEach(value => {
						
						if (value.length == 0 || !value[0]) {
							is.is = true
							is.msg = '请选择 分组 '
						}
					})


					if (is.is) {
						return
					}
					
					if((value.successOutput.length||value.failOutput.length)&&!value.condition.children.length){
						is.is=true
						is.msg = '有命中修改或未命中修改后 条件不能为空'
					}
					if (is.is) {
						return
					}
					value.successOutput.forEach(item => {
						forVerify(is, item, '命中')
					})

					if (is.is) {
						return
					}

					value.failOutput.forEach(item => {
						forVerify(is, item, '未命中')
					})

					if (is.is) {
						return
					}

					deepexamine(is, [value.condition])
					if (is.is) {
						return
					}

				})




				if (is.is) {
					this.$message.error(is.msg)
				}
				return is.is



				function forVerify(is, item, str) {


					console.log(item)
					if (!item.outputKey) {
						is.is = true
						is.msg = `请选择${str}修改的 属性`
					}

					if (!item.outputValue || !item.outputValue.trim()) {
						is.is = true
						is.msg = `请输入${str}修改的 值`
					} else if (item.variableType == 3 && JSON.parse(item.outputValue).formula.trim() == '') {
						
						is.is = true
						is.msg = `请输入${str}修改的 值`
					}


				}

				function deepexamine(is, arr, index = 1) {
					arr.forEach(value => {
						if (value.children && value.children.length > 0) {
							deepexamine(is, value.children, index + 1)
						}
						if (value.conditionType == 2) {


							if (!value.opKey) {
								is.is = true
								is.msg = '请选择 条件 的 入Key'
							}
							if (!value.operator) {
								is.is = true
								is.msg = '请选择 条件 的 运算符'
							}
							if (!value.variableValue || !value.variableValue.trim()) {
								is.is = true
								is.msg = '请选择 条件 的 值'
							}


						}
						if (value.conditionType == 1 && value.children.length == 0 && index != 1) {
							is.is = true
							is.msg = '关系符下不允许为空'
						}


					})


				}

			},

			submit() {

				this.loading = true
				if (this.examine()) {
					this.loading = false
					return
				}
				console.log(this.version)
				if (this.getIsempty(this.version)) {
					this.loading = false
					return
				}

				let sobj = JSON.parse(JSON.stringify(this.version))

				try {
					if (Array.isArray(JSON.parse(this.mixinGetFieldByEn(this.version.inputFieldEn).jsonValue))) {
						sobj.inputFieldType = 'list'
					} else {
						sobj.inputFieldType = 'map'
					}
				} catch (e) {
					this.$message.error('源数据选择出错')
					this.loading = false
					return
				}


				this.Enformat(sobj, 'String')
				console.log(sobj)
				// return
				let obj = {
					"code": this.code.trim(),
					"name": this.name.trim(),
					"description": String(this.description).trim(),

					"versionList": [sobj],

				}

				if (this.id == 0) {
					obj.versionList[0].versionCode = 'V:0'
					obj.versionList[0].description = '初始版本'
					obj.folderId = this.nameId == 99999999 ? 0 : this.nameId,
						this.getData.setsave(obj).then(res => {
							this.loading = false
							if (res.status === "1") {
								this.versionList = res.data.versionList
								this.$message({
									message: '添加成功',
									type: 'success'
								});

								this.$emit('Ok')
							}
						}).catch(err => {
							this.loading = false
						})
				} else {
					obj.id = this.id
					obj.folderId = this.parentId
					obj.versionList[0].id = this.version.id
					obj.versionList[0].dataCleanId = this.id

					this.getData.updatafield(obj).then(res => {
						this.loading = false
						if (res.status === "1") {
							this.versionList = res.data.versionList
							this.$message({
								message: '修改成功',
								type: 'success'
							});

						}
					})
				}
			}
		},
		watch: {

		},
		computed: {

		}











	}
</script>

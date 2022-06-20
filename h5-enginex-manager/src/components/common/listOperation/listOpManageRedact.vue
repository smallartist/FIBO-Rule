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
					<span v-if="id===0">新增集合 :</span>
					<span v-else>编辑集合 :</span>
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
				<p>集合代码: </p>
				<el-input placeholder="请输入集合代码" maxlength="30" v-model="code" clearable :disabled="addVersionStatus">
				</el-input>
			</div>
			<div>
				<p>集合名称:</p>
				<el-input placeholder="请输入集合名称" maxlength="20" v-model="name" clearable :disabled="addVersionStatus">
				</el-input>
			</div>
			<!-- <div>
				<p>集合类型:</p>
				<el-select v-model="opType" placeholder="请选择类型" @change="opTypeChange"
					:disabled="addVersionStatus||id!=0">
					<el-option v-for="item in opTypeList" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>
			</div> -->
		</div>
		<div :class="smallHeader?'MR_scope MR_scopeSmall':'MR_scope'">
			<div>
				<p>集合描述: </p>
				<el-input placeholder="请输入决策描述" maxlength="300" v-model="description" clearable
					:disabled="addVersionStatus" style="margin-left: 20px;"></el-input>

				<p style="margin-left: 20px;">有效时间: </p>

				<el-date-picker style="margin:0 20px;width: 500px;" v-model="validTime" type="daterange"
					range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" :disabled="addVersionStatus">
				</el-date-picker>
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

			<opType  :opType="opType" :data="version" @changeInputFieldEn="changeInputFieldEn"></opType>

		</div>

	</div>
</template>

<script>
	import opType from '@/components/common/listOperation/opType.vue'
	import mangeRedactMixin from '@/utils/contminxin/MangeRedactMixin.js'
	import '@/assets/css/ManageRedact.css'
	import version from '@/components/common/Version.vue'

	const versionOrigin = {
		inputFieldEn: [],

		conditionAndOutPutList: [{
			condition: {
				logical: "&&",
				conditionType: 1,
				children: []
			},
			defaultOutput: [],
			filterCondition: {
				conditionType: 1,
				logical: "&&",
				children: [],
				variableType: 1
			},
			failOutput: [],
			successOutput: [],
			listOperationBlock: {
				name: "",
				groupFields: [],
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
				opTypeList: [{
					value: 1,
					label: '集合规则'
				}, {
					value: 2,
					label: '集合处理'
				}, ],
				validTime: [],
			}
		},
		created() {

			if (this.id != 0) {
				this.loading = true
				this.getData.getVersion(this.id).then(res => {
					if (res.status == '1') {
						this.code = res.data.code
						this.name = res.data.name
						this.opType = res.data.opType
						this.priority = res.data.priority
						this.description = res.data.description
						this.parentId = res.data.folderId

						if (res.data.startTime) {
							this.$set(this.validTime, 0, new Date(res.data.startTime))
						}
						if (res.data.endTime) {
							this.$set(this.validTime, 1, new Date(res.data.endTime))
						}

						// 版本部分
						this.versionList = res.data.versionList
						let obj = JSON.parse(JSON.stringify(this.versionList[0]))


						this.Enformat(obj, 'Array')
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
			changeInputFieldEn(e) {
				this.version = JSON.parse(JSON.stringify(versionOrigin))
				this.version.inputFieldEn = e
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
				obj.listOpId = this.id
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
				obj.listOpId = this.id
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
					listOpId: this.id,
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

				obj.inputFieldEn = getformat(obj.inputFieldEn)


				// if (str == 'Array') {
				// 	obj.groupFields = JSON.parse(obj.groupFields)
				// }
				// obj.groupFields = obj.groupFields.map(value => {
				// 	return getformat(value)
				// })
				// if (str == 'String') {
				// 	obj.groupFields = JSON.stringify(obj.groupFields)
				// }

				obj.conditionAndOutPutList.forEach(value => {
					value.successOutput.forEach(item => {
						item.outputOpKey = getformat(item.outputOpKey)
						if (item.variableType == 2) {
							item.outputValue = getformat(item.outputValue)
						}
					})
					value.failOutput.forEach(item => {
						item.outputOpKey = getformat(item.outputOpKey)
						if (item.variableType == 2) {
							item.outputValue = getformat(item.outputValue)
						}
					})


					if (str == 'Array') {
						if(value.listOperationBlock.groupFields){
							
						value.listOperationBlock.groupFields = JSON.parse(value.listOperationBlock.groupFields)
						}else{
							value.listOperationBlock.groupFields = []
						}
					}

					
					value.listOperationBlock.groupFields = value.listOperationBlock.groupFields.map(item => {
						return getformat(item)
					})
					if (str == 'String') {
						value.listOperationBlock.groupFields = JSON.stringify(value.listOperationBlock.groupFields)
					}

					value.filterCondition.children.forEach(item => {
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
					this.$message.error('请填入集合代码，并检查空格');
					return true
				}
				if (this.name.trim() == '') {
					this.$message.error('请填入集合名称，并检查空格');
					return true
				}
				if (this.description.trim() == '') {
					this.$message.error('请填入集合描述，并检查空格');
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


				if (!obj.inputFieldEn || obj.inputFieldEn.length == 0) {
					is.is = true
					is.msg = '请选择 数据 '
				}








				obj.conditionAndOutPutList.forEach(value => {
					value.filterCondition.children.forEach(value => {
						if (!value.opKey || value.opKey.length == 0) {
							is.is = true
							is.msg = '请选择 过滤的 Key'
						}


						if (!value.variableValue || value.variableValue.trim() == '') {
							is.is = true
							is.msg = '请填写 过滤 的值'
						}

					})

					value.listOperationBlock.groupFields.forEach(value => {
						// debugger
						if (value.length == 0 || !value[0]) {
							is.is = true
							is.msg = '请选择 分组 '
						}
					})


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

					this.verify(is, value.successOutput, '命中')
					if (is.is) {
						return
					}
					this.verify(is, value.failOutput, '命中')



				})




				if (is.is) {
					this.$message.error(is.msg)
				}
				return is.is



				function forVerify(is, item, str) {

					console.log(item)
					if (!item.outputKey.trim()) {
						is.is = true
						is.msg = `请输入${str}输出的KEY`
					}
					if (!item.outputOp) {
						is.is = true
						is.msg = `请选择${str}输出的算子`
					}
					if (!item.outputOpKey) {
						is.is = true
						is.msg = `请选择${str}输出的 计算条件`
					}

					if (item.outputOp != 'custom' && item.outputOp != 'list_element' && item.outputOp !=
						'count') { //如果不为自定义
						console.log(item)
						if (!item.outputOpKey || item.outputOpKey.length == 0 || !item.outputOpKey[0]) {
							is.is = true
							is.msg = `请输入${str}输出的 计算依赖（只有算子为集合时可以不选择计算依赖）`
						}
					} else if (item.outputOp == 'custom') {
						// if (item.variableType != 2) {
						if (!item.outputValue || !item.outputValue.trim()) {
							is.is = true
							is.msg = `请输入${str}输出的 值`
						}
				
					}
				}

				function deepexamine(is, arr,index=1) {
					arr.forEach(value => {
						if (value.children && value.children.length > 0) {
							deepexamine(is, value.children,index+1)
						}
						if (value.conditionType == 2) {

							if (!value.opType) {
								is.is = true
								is.msg = '请选择 条件 的 算子'
							}
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
						if (value.conditionType == 1&&value.children.length==0&&index!=1) {
							is.is = true
							is.msg = '关系符下不允许为空'
						}


					})


				}

			},
			verify(is, item, str) {
				let arr = []

				item.forEach(value => {


					if (value.outputOp == 'custom' && value.variableType == 3) {
						console.log(value)

						let obj = JSON.parse(value.outputValue)
						let sarr = obj.formula.match(/@.*?@/)
						if (sarr != null) {
							sarr.forEach(cont => {
								let subStr = cont
								subStr = subStr.substring(1, subStr.length - 1)
								if (!this.FieldUser.find(x => x.fieldCn == subStr) && !arr.find(x => x ==
										subStr)) {
									is.is = true
									is.msg = `自定义中使用输出Key 只能使用同块并且在自身之上的Key 违规Key：${subStr}`
								}
							})
						}

					}
					arr.push(value.outputKey)
				})
			},
			submit() {
				this.loading = true
				if (this.examine()) {
					this.loading = false
					return
				}
				if (this.getIsempty(this.version)) {
					this.loading = false
					return
				}

				let sobj = JSON.parse(JSON.stringify(this.version))
				sobj.inputFieldType = 'list'
				this.Enformat(sobj, 'String')

				let obj = {
					"code": this.code.trim(),
					"name": this.name.trim(),
					"description": String(this.description).trim(),
					"opType": this.opType,
					"versionList": [sobj],
					"startTime": this.validTime && this.validTime[0] ? this.validTime[0] : null,
					"endTime": this.validTime && this.validTime[1] ? this.validTime[1] : null,
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
						})
				} else {
					obj.id = this.id
					obj.folderId = this.parentId
					obj.versionList[0].id = this.version.id
					obj.versionList[0].listOpId = this.id

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

<style>
	.rule_com {
		/* height: 51vh; */
		flex: 1;
		overflow: scroll;
		/* overflow-x: hidden; */
		margin: 0px 0px 0px 10px;
		padding-bottom: 100px;
	}

	.rule_com::-webkit-scrollbar {
		/* display: none; */
		/* Chrome Safari */
	}

	.Rule_version_buttom {
		/* width: 100px; */
		margin-left: 20px !important;
	}

	.rule_outcontent_box {
		border: 1px dotted #00000022;

		margin-top: 20px;
		padding: 10px 0 10px 10px;
	}

	.groovyHint {
		padding: 5px;
	}
</style>
<template>

	<div class="dataManageRedact" v-loading="loading">

		<div :class="smallHeader?'MR_header MR_headerSmall':'MR_header'">
			<div>
				<div>
					<el-button type="primary" icon="el-icon-arrow-left" circle @click="mixinClose"></el-button>
				</div>
				<div>
					<span v-if="id===0">新增规则 :</span>
					<span v-else>编辑规则 :</span>
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
				<p>规则代码: </p>
				<el-input placeholder="请输入规则代码" maxlength="30" v-model="code" clearable :disabled="addVersionStatus">
				</el-input>
			</div>
			<div>
				<p> 规则名称: </p>
				<el-input placeholder="请输入规则名称" maxlength="20" v-model="name" clearable :disabled="addVersionStatus">
				</el-input>
			</div>


			<div>
				<p> 脚本类型: </p>
				<el-select v-model="scriptType" placeholder="请选择" :disabled="id===0?'disabled':''">
					<el-option v-for="item in scriptList" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>
			</div>
		</div>
		<div :class="smallHeader?'MR_scope MR_scopeSmall':'MR_scope'">
			<div>
				<p>规则描述: </p>
				<el-input placeholder="请输入规则描述" maxlength="300" v-model="description" clearable
					:disabled="addVersionStatus"></el-input>
			</div>
		</div>

		<version style="margin:5px;" :id="id" :ruleVersionList="ruleScriptVersionList" :version="version"
			:addVersionStatus="addVersionStatus" :addVersionDialog="addVersionDialog"
			:addVersionLoading="addVersionLoading" @addVersion="addVersion" @addVersionClose="addVersionClose"
			@copyVersion="copyVersion" @delectVersion="delectVersion" @updateVersion="updateVersion"
			@addVersionSure="addVersionSure" @Dialog="addVersionDialog=$event" @addVersionExamine="addVersionExamine"
			@versionChange="versionChange" @StatusChange="addVersionStatus=$event"></version>

		<div class="rule_com">
			<!--  -->

			<teV2 v-model="formula">
				<div v-if="scripHint[scriptType].top" class="groovyHint"
					:style="{backgroundColor:scripHint[scriptType].top.backgroundColor,color:scripHint[scriptType].top.color}"
					v-html="scripHint[scriptType].top.cont">

				</div>
			</teV2>
			<div v-if="scripHint[scriptType].bottom" class="groovyHint"
				:style="{backgroundColor:scripHint[scriptType].bottom.backgroundColor,color:scripHint[scriptType].bottom.color}"
				v-html="scripHint[scriptType].bottom.cont">

			</div>





			<!-- 
			<div class="rule_outcontent_box">
				<p>命中输出：</p>
				<div class="rule_home" style="margin-top: 10px;">
					<div class="rule_fa">
						<el-button icon="el-icon-plus" circle @click="outAdd(0)" disabled></el-button>
						<el-button icon="el-icon-close" circle disabled='disabled' style="margin-right: 10px">
						</el-button>
					</div>
					<el-select v-model="resultFieldEn" filterable placeholder="请选择" style="width: 200px;" clearable>
						<el-option v-for="item in FieldUser" :key="item.id" :label="item.fieldCn" :value="item.fieldEn">
						</el-option>
					</el-select>
					<p style="margin: 10px;">
						=
					</p>
					<el-select filterable value="是否命中" disabled style="width: 255px;">
					</el-select>
				</div>

				<outcontent :outcontent="outcontent" :ruleOut="true" type="complex_rule" :outType="outTypeSuccess">
					<div style="display: flex;align-items: center;">
						<el-select v-model="scoreFieldEn" filterable placeholder="请选择" style="width: 200px;" clearable>
							<el-option v-for="item in FieldUser" :key="item.id" :label="item.fieldCn"
								:value="item.fieldEn">
							</el-option>
						</el-select>
						<p style="margin: 10px;">=</p>
						<div>
							<el-input v-model="SpecialField.score" maxlength="30" style="width: 255px;">
								<template slot="prepend">得分</template>
							</el-input>
						</div>
					</div>
				</outcontent>
			</div>
			<br> -->
			<!-- <div class="rule_outcontent_box">
				<p>未命中输出:</p>
				<outcontent :outcontent="failOutputList" :unone="true" :ruleOut="true" type="complex_rule"
					:outType="outTypeFail">

				</outcontent>
				<div>
				</div>
			</div> -->
		</div>
	</div>
</template>

<script>
	import teV2 from '@/components/common/teV2.vue'
	import mangeRedactMixin from '@/utils/contminxin/MangeRedactMixin.js'
	import '@/assets/css/ManageRedact.css'
	import version from '@/components/common/Version.vue'
	import outcontent from '@/components/models/outcontent.vue'
	export default {
		mixins: [mangeRedactMixin],
		components: {
			teV2,
			outcontent,
			version
		},
		props: {
			fieldTypeId: {
				type: Number,
				default: 0,
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
			},
			getData: {
				type: Object,
				default () {
					return {}
				}
			}
		},

		data() {
			return {
				formula: '',
				scriptType: 'groovy',
				scriptList: [{
					label: 'groovy规则',
					value: 'groovy',
				}],
				scripHint: {
					groovy: {
						top: {
							cont: `def main(_) {\n HashMap<String, Object> result = new HashMap<>();                   //规则执行的返回值<br>int ruleScore = 0;   //规则命中时得分<br> String hitResult = "未命中"; //命中结果，可选类型为：命中、未命中<br>	HashMap<String, Object> updateInputMap = new HashMap<>();           //用于更新入参的map，此map中的所有内容将被更新到入参中,key重复的将被覆盖。<br> ArrayList<HashMap<String, Object>> fieldList = new ArrayList<>();   //用于存放输出字段的值<br> //自定义代码区域，根据需要书写逻辑代码<br>`,
							backgroundColor: '#f4f4f5',
							color: '#999'
						},
						bottom: {
							cont: ` //返回固定格式的结果用于后续执行<br> result.put("hitResult",hitResult);<br> result.put("ruleScore",ruleScore);<br> result.put("fieldList",fieldList);<br> result.put("updateInputMap",updateInputMap);<br>	return result;<br> }`,
							backgroundColor: '#f4f4f5',
							color: '#999'
						}
					}
				},
				topCode: `def main(_) {\n HashMap<String, Object> result = new HashMap<>();\n  int ruleScore = 0;\n   String hitResult = "未命中";\n  HashMap<String, Object> updateInputMap = new HashMap<>();\n  ArrayList<HashMap<String, Object>> fieldList = new ArrayList<>();\n //!#$%^\n`,
				bottomCode: `//!#$%^\n result.put("hitResult",hitResult);\n result.put("ruleScore",ruleScore);\n result.put("fieldList",fieldList);\n result.put("updateInputMap",updateInputMap);\n return result;\n}\n`,
				// 新增部分
				tempVersion: {
					versionCode: '',
					description: ''
				},
				addVersionLoading: false,
				addVersionDialog: false,
				version: {},
				addVersionStatus: false,
				ruleScriptVersionList: [],
				scoreFieldEn: '',
				resultFieldEn: '',
				loading: false,
				valueScope: '',
				priority: 0,
				code: '',
				name: '',
				description: '',
				// ruledata: null,
				// outcontent: [],
				// failOutputList: [],
				SpecialField: {
					score: '1',
					ruleAudit: 5
				},
				tempisEmpty: false,
				parentId: '',
				// outTypeSuccess: {
				// 	outType: 'success'
				// },
				// outTypeFail: {
				// 	outType: 'fail'
				// },
			}
		},
		created() {
			this.$store.dispatch('getfielduser')
		
			if (this.id != 0) {
				this.loading = true
				this.getData.getVersion(this.id).then(res => {
					this.parentId = res.data.parentId
					this.ruleScriptVersionList = res.data.ruleScriptVersionList
					this.version = JSON.parse(JSON.stringify(this.ruleScriptVersionList[0]))
					this.code = res.data.code
					this.name = res.data.name
					this.priority = res.data.priority
					this.description = res.data.description
					this.getRuleCont()
				})
			} else {
				// this.ruledata = {
				// 	"logical": "&&",
				// 	"fieldId": null,
				// 	"operator": null,
				// 	"fieldValue": null,
				// 	"conditionType": 1,
				// 	"children": [],
				// 	loopGroupActions: []
				// }
			}
		},
		computed: {
			FieldUser() {
				if (this.$store.state.FieldUser) {
					// console.log(this.$store.state.FieldUser.data.fieldList)
					return this.$store.state.FieldUser.data.fieldList
				} else {
					return []
				}
			},
			
		},
		mounted() {

		},
		methods: {
			addVersionExamine() { //添加版本验证
				if (this.verification()) {
					return
				}
				this.addVersionDialog = true
			},
			addVersionSure(tempVersion) { //确定添加版本
				this.addVersionLoading = true
				let scriptContent = JSON.stringify(this.sectionSure(this.formula))
				let obj = {
					versionCode: tempVersion.versionCode,
					description: tempVersion.description,
					ruleId: this.id,
					scriptContent: scriptContent,
					scriptType: this.scriptType,


					// score: this.SpecialField.score,
					// ruleAudit: this.SpecialField.ruleAudit,
					// scoreFieldEn: this.scoreFieldEn,
					// resultFieldEn: this.resultFieldEn,

					// strategyOutputList: this.outcontent,
					// failOutputList: this.failOutputList
				}




				this.getData.addVersion(obj).then(res => {
					if (res.status == '1') {
						this.$message({
							message: '添加版本成功',
							type: 'success'
						});
						this.ruleScriptVersionList = res.data
						this.version = JSON.parse(JSON.stringify(this.ruleScriptVersionList[0]))
						this.addVersionDialog = false
						this.addVersionStatus = false
						this.getRuleCont()
						this.$store.dispatch('reGetRuleList')

					}
					this.addVersionLoading = false
				})
			},
			updateVersion(tempVersion) { //版本重命名
				this.addVersionLoading = true


				let obj = {
					ruleId: this.id,
					id: this.version.id,
					versionCode: tempVersion.versionCode,
					description: tempVersion.description,
					scriptContent: JSON.stringify(this.sectionSure(this.formula)),
					scriptType: this.scriptType,
					// score: this.SpecialField.score,
					// ruleAudit: this.SpecialField.ruleAudit,
					// scoreFieldEn: this.scoreFieldEn,
					// resultFieldEn: this.resultFieldEn,
					// strategyOutputList: this.outcontent,
					// failOutputList: this.failOutputList

				}
				this.getData.updateVersion(obj).then(res => {
					if (res.status == '1') {
						this.$message({
							message: '版本重命名成功',
							type: 'success'
						});
						this.ruleScriptVersionList = res.data
						this.version = JSON.parse(JSON.stringify(this.ruleScriptVersionList[0]))
						this.addVersionDialog = false
						this.addVersionStatus = false
						this.getRuleCont()
						this.$store.dispatch('reGetRuleList')
					}
					this.addVersionLoading = false
				})
			},
			copyVersion(tempVersion) { //复制版本
				this.addVersionLoading = true
				let obj = {
					ruleId: this.id,
					id: this.version.id,
					versionCode: tempVersion.versionCode,
					description: tempVersion.description
				}
				this.getData.copyVersion(obj).then(res => {
					if (res.status == '1') {
						this.$message({
							message: '复制版本成功',
							type: 'success'
						});
						this.ruleScriptVersionList = res.data
						this.version = JSON.parse(JSON.stringify(this.ruleScriptVersionList[0]))
						this.addVersionDialog = false
						this.addVersionStatus = false
						this.getRuleCont()
						this.$store.dispatch('reGetRuleList')
					}
					this.addVersionLoading = false
				})
			},
			getRuleCont() { //获取规则
				this.loading = true
				this.getData.getInfo({
					id: this.version.id
				}).then(res => {
					if (res.status == "1") {
						if (res.data.scriptContent) this.formula = JSON.parse(res.data.scriptContent).formula
							.split('//!#$%^\n')[1]
						// console.log(JSON.parse(res.data.scriptContent).formula)
						// console.log(this.formula)
						// this.scoreFieldEn = res.data.scoreFieldEn
						// this.resultFieldEn = res.data.resultFieldEn
						// this.getType(res.data.ruleConditionVo)
						// this.ruledata = res.data.ruleConditionVo ? res.data.ruleConditionVo : {
						// 	"logical": "&&",
						// 	"fieldId": null,
						// 	"operator": null,
						// 	"fieldValue": null,
						// 	"conditionType": 1,
						// 	"children": [],
						// }
						// this.outcontent = res.data.strategyOutputList
						// this.failOutputList = res.data.failOutputList
						// this.outcontent.forEach(value => {
						// 	if (!value.variableType) {
						// 		value.variableType = 1
						// 	}
						// })
						// this.redeepverify(this.ruledata)

						// this.SpecialField.score = res.data.score
						// this.SpecialField.ruleAudit = res.data.ruleAudit
					}
					this.loading = false
				})
			},
			versionChange() { //切换版本
				this.ruleScriptVersionList.forEach(value => {
					if (value.id === this.version.id) {
						this.version = JSON.parse(JSON.stringify(value))
						this.getRuleCont()
					}
				})
			},
			addVersion() { //添加版本
				this.addVersionStatus = true
				this.tempadd = {
					// scoreFieldEn: this.scoreFieldEn,
					// resultFieldEn: this.resultFieldEn,
					formula: this.formula,
					// outcontent: this.outcontent,
					// failOutputList: this.failOutputList,
					// score: this.SpecialField.score,
				}
				this.reset()
			},

			verification() { //验证
				let reg = /[\u4e00-\u9fa5]+/g;
				if (this.code.match(reg) != null) {
					this.$message.error('代码不允许出现中文');
					return true
				}
				if(this.verificationCode(this.code)||this.verificationName(this.name)){
					return true
				}
				if (this.code.trim() === '') {
					this.$message.error('请填入规则代码，并检查空格');
					return true
				}
				if (this.priority === '') {
					this.$message.error('请选择规则优先级');
					return true
				}
				if (this.name.trim() === '') {
					this.$message.error('请填入规则名称，并检查空格');
					return true
				}
				if (this.description === '') {
					this.$message.error('请填入规则描述，并检查空格');
					return true
				}
				// if (this.resultFieldEn === "") {
				// 	this.$message.error('请选择命中时输出变量');
				// 	return true
				// }
				// if (this.scoreFieldEn == "") {
				// 	this.$message.error('请选择得分时输出变量');
				// 	return true
				// }
				if (isNaN(Number(this.SpecialField.score)) || String(this.SpecialField.score).trim() === "") {
					this.$message.error('得分只能是数字');
					return true
				}
				let is = true
				this.tempisEmpty = false
				// this.deepverify(this.ruledata)
				if (this.tempisEmpty) {
					return true
				}
				// this.outcontent.forEach(value => {
				// 	if (value.fieldId === "" || String(value.fieldValue).trim() === "" || value.variableType ===
				// 		"") {
				// 		is = false
				// 	}
				// 	if (value.variableType == 3 && (String(value.fieldValue).trim() === "" || JSON.parse(value
				// 				.fieldValue).formula.trim() ===
				// 			'')) {
				// 		is = false
				// 	}
				// })
				// this.failOutputList.forEach(value => {
				// 	if (value.fieldId === "" || String(value.fieldValue).trim() === "" || value.variableType ===
				// 		"") {
				// 		is = false
				// 	}
				// 	if (value.variableType == 3 && (String(value.fieldValue).trim() === "" || JSON.parse(value
				// 				.fieldValue).formula.trim() ===
				// 			'')) {
				// 		is = false
				// 	}
				// })
				if (is === false) {
					this.$message.error('请检查自定义输出部分是否有未填项');
					return true
				}
			},
			addVersionClose() { //添加版本取消
				this.addVersionStatus = false

				this.formula = this.tempadd.formula

				// this.SpecialField.score = this.tempadd.score
				// this.scoreFieldEn = this.tempadd.scoreFieldEn
				// this.resultFieldEn = this.tempadd.resultFieldEn

				// this.outcontent = this.tempadd.outcontent
				// this.failOutputList = this.tempadd.failOutputList
			},
			reset() { //数据暂时重置
				this.formula = ''
				// this.SpecialField.score = 1
				// this.outcontent = []
				// this.failOutputList = []
				// this.scoreFieldEn = ""
				// this.resultFieldEn = ""
			},
			delectVersion() { //删除版本
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
						this.ruleScriptVersionList = res.data
						this.version = JSON.parse(JSON.stringify(this.ruleScriptVersionList[0]))
						this.getRuleCont()
					}
				})

			},
			// getType(obj) { //获取 规则类型
			// 	if (obj.conditionType == 2 && !obj.valueType) {
			// 		obj.valueType = this.mixinGetvalueType(obj.fieldId)
			// 	}
			// 	if (obj.children.length > 0) {
			// 		obj.children.forEach(value => {
			// 			this.getType(value)
			// 		})
			// 	}
			// },
			// outAdd(index) { //添加自定义输出
			// 	this.outcontent.splice(index, 0, {
			// 		"fieldId": "",
			// 		strategyType: 'base_rule',
			// 		"fieldValue": "",
			// 		variableType: 1
			// 	})
			// },
			submit() { //提交

				this.loading = true
				
				if (this.verification()) {
					this.loading = false
					return
				}
				let is = this.sectionSure(this.formula)
				if (!is) {
					this.loading = false
					return
				}
				let scriptContent = JSON.stringify(is)



				// this.outcontent.forEach(value => {
				// 	value.fieldEn = this.mixinGetvalueEn(value.fieldId)
				// })
				// this.failOutputList.forEach(value => {
				// 	value.fieldEn = this.mixinGetvalueEn(value.fieldId)
				// })
				let obj = {
					"code": this.code.trim(),
					"name": this.name.trim(),
					"priority": this.priority,
					"description": String(this.description).trim(),
					scriptType: this.scriptType,
					difficulty: 3,
					ruleAudit: this.SpecialField.ruleAudit,
					ruleScriptVersionList: [{
						// score: this.SpecialField.score,
						// scoreFieldEn: this.scoreFieldEn,
						// resultFieldEn: this.resultFieldEn,
						// strategyOutputList: this.outcontent,
						// failOutputList: this.failOutputList,
						scriptType: this.scriptType,
						scriptContent: scriptContent
					}],
				}


				if (this.id == 0) {
					obj.ruleScriptVersionList[0].versionCode = 'V:0'
					obj.ruleScriptVersionList[0].description = '初始版本'
					obj.parentId = this.nameId == 99999999 ? 0 : this.nameId,
						this.getData.setsave(obj).then(res => {
							this.loading = false
							if (res.status === "1") {
								this.$message({
									message: '添加成功',
									type: 'success'
								});
								this.$emit('Ok')
								this.$store.dispatch('reGetRuleList')
							}
						}).catch(err => {
							this.loading = false
							this.$message.error('网络出现问题-_-');
						})
				} else {
					obj.id = this.id
					obj.parentId = this.parentId
					obj.ruleScriptVersionList[0].id = this.version.id
					this.getData.updatafield(obj).then(res => {
						this.loading = false
						if (res.status === "1") {
							this.$message({
								message: '修改成功',
								type: 'success'
							});
							// this.$emit('Ok')
							this.$store.dispatch('reGetRuleList')
						}
					}).catch(err => {
						this.loading = false
						this.$message.error('网络出现问题-_-');
					})
				}
				console.log(obj)
			},
			sectionSure(tempValue) {
				tempValue = tempValue ? tempValue.trim() : ''
				
				if(String(tempValue).match(/@/g)&&String(tempValue).match(/@/g).length%2==1){
					
					this.$message.error(`@符号不能为单数个`);
					return false
					
				}
				
				let arr = String(tempValue).match(/@(.|\n)*?@/g) === null ? [] : tempValue.match(/@(.|\n)*?@/g);
				let is = false
				console.log(tempValue)
				arr = Array.from(new Set(arr))
				// console.log(arr)
				let str = ''
				arr = arr.map(value => {
					console.log(value)
					value = value.split('')
					value.pop()
					value.shift()
					value = value.join('')
					if (this.mixinGetValueByCn(value)) {
						return this.mixinGetValueByCn(value)
					} else {
						is = true
						str += value+'、'
						return this.mixinGetValueByCn(value)
					}

				})
				if (is) {
					this.$message.error(`没有找到指标:${str}`);
					return false
				}
				let obj = {
					farr: arr,
					formula: this.topCode + tempValue + this.bottomCode
				}
				return obj

			},
		},
		watch: {

		}
	}
</script>

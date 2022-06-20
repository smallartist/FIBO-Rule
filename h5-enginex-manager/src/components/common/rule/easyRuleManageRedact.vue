<style>

	.easyrule_com{
	flex: 1;
	overflow: scroll;
	margin: 20px 0px 0px 10px;
	padding-bottom: 100px;
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
				<el-button type="success" icon="el-icon-check" circle @click="submit"></el-button>
			</div>
		</div>
		<div :class="smallHeader?'MR_input MR_inputSmall':'MR_input'">
			<div>
				<p>规则代码: </p>
				<el-input placeholder="请输入规则代码" maxlength="30" v-model="code" clearable></el-input>
			</div>
			<div>
				<p> 规则名称: </p>
				<el-input placeholder="请输入规则名称" maxlength="20" v-model="name" clearable></el-input>
			</div>
			<div>
				<p>优 先 级 : </p>
				<el-select v-model="priority" placeholder="请选择">
					<el-option label="0" :value="0" />
					<el-option label="1" :value="1" />
					<el-option label="2" :value="2" />
					<el-option label="3" :value="3" />
					<el-option label="4" :value="4" />
					<el-option label="5" :value="5" />
					<el-option label="6" :value="6" />
					<el-option label="7" :value="7" />
					<el-option label="8" :value="8" />
					<el-option label="9" :value="9" />
				</el-select>
			</div>
		</div>
		<div :class="smallHeader?'MR_scope MR_scopeSmall':'MR_scope'">
			<div>
				<p>规则描述: </p>
				<el-input placeholder="请输入规则描述" maxlength="300" v-model="description" clearable></el-input>
			</div>
		</div>

		<div class="easyrule_com">

			<RuleDown :type="1" :Data="ruledata" @faadd="faadd" @outAdd="outAdd" @outDelect="outDelect" @fadelect="fadelect"
			 @change="change" :SpecialField="SpecialField" :outcontent="outcontent">

				<div class="rule_home">
					<div class="rule_fa">
						<el-button icon="el-icon-plus" circle @click="outAdd(0)" disabled></el-button>
						<el-button icon="el-icon-close" circle disabled='disabled' style="margin-right: 10px"></el-button>
					</div>
					<el-select v-model="resultFieldEn" filterable placeholder="请选择" style="width: 200px;">
						<el-option v-for="item in FieldUser" :key="item.id" :label="item.fieldCn" :value="item.fieldEn">
						</el-option>
					</el-select>
					<p style="margin: 10px;">
						=
					</p>
					<el-select filterable value="是否命中" disabled style="width: 255px;">
					</el-select>
				</div>


				<div class="rule_home">
					<div class="rule_fa">
						<el-button icon="el-icon-plus" circle @click="outAdd(0)"></el-button>
						<el-button icon="el-icon-close" circle disabled='disabled' style="margin-right: 10px"></el-button>
					</div>

					<div style="display: flex;align-items: center;">

						<el-select v-model="scoreFieldEn" filterable placeholder="请选择" style="width: 200px;">
							<el-option v-for="item in FieldUser" :key="item.id" :label="item.fieldCn" :value="item.fieldEn">
							</el-option>
						</el-select>
						<p style="margin: 10px;">=</p>
						<div>
							<el-input v-model="SpecialField.score" maxlength="30" style="width: 255px;">
								<template slot="prepend">得分</template>
							</el-input>
						</div>

					</div>
				</div>

			</RuleDown>
		</div>
	</div>
</template>

<script>
	import mangeRedactMixin from '@/utils/contminxin/MangeRedactMixin.js'
	import '@/assets/css/ManageRedact.css'
	import RuleDown from '@/components/common/rule/ruleRule.vue'
	export default {
		mixins: [mangeRedactMixin],
		components: {
			RuleDown
		},
		props: {
			fieldTypeId: {
				type: Number,
				default: 0,
			},
			setsave: {
				type: Function,
				default: () => {}
			},
			id: {
				type: Number,
				default: 0
			},
			type: {
				type: Number,
				default: 1
			},
			getInfo: {
				type: Function,
				default: () => {}
			},
			updata: {
				type: Function,
				default: () => {}
			},
			nameId: {
				type: Number,
				default: 0
			}
		},

		data() {
			return {
				scoreFieldEn: '',
				resultFieldEn: '',
				loading: false,

				priority: '',
				code: '',
				name: '',
				description: '',
				ruledata: null,
				"outcontent": [],
				SpecialField: {
					score: '1',
					ruleAudit: 5,
					lastLogical: '-1'
				},
				tempisEmpty: false,
				parentId: '',
			}
		},
		created() {
		
	
			if (this.id != 0) {
				this.loading = true
				this.getInfo(this.id).then(res => {
					this.scoreFieldEn = res.data.scoreFieldEn
					this.resultFieldEn = res.data.resultFieldEn
					this.code = res.data.code
					this.name = res.data.name
					this.priority = res.data.priority
					this.description = res.data.description
					this.parentId = res.data.parentId
					this.ruledata = res.data.ruleFieldList ? res.data.ruleFieldList : [{
							"logical": "",
							"fieldId": "",
							"operator": "",
							"fieldValue": ""
						}],
						this.outcontent = res.data.strategyOutputList
					this.outcontent.forEach(value => {
						if (!value.variableType) {
							value.variableType = 1
						}
						if (value.variableType == 2) {
							value.fieldValue = Number(value.fieldValue.split('|')[0])
						}
					})
					this.SpecialField.score = res.data.score
					this.SpecialField.ruleAudit = res.data.ruleAudit
					this.SpecialField.lastLogical = res.data.lastLogical

					console.log(res.data.strategyOutputList)
					this.loading = false
				})
			} else {
				this.ruledata = [{
					"logical": "",
					"fieldId": "",
					"operator": "",
					"fieldValue": ""
				}]

			}
		},
		computed: {

			FieldUser() {
				return this.$store.state.FieldUser.data.fieldList
			},
			
		},
		mounted() {
			
		},
		methods: {
			getType(obj) {
				if (obj.conditionType == 2 && !obj.valueType) {
					obj.valueType = this.mixinGetvalueType(obj.fieldId)
				}
				if (obj.children.length > 0) {
					obj.children.forEach(value => {
						this.getType(value)
					})
				}
			},
			outAdd(index) {
				this.outcontent.splice(index, 0, {
					"fieldId": "",
					strategyType: 'base_rule',
					"fieldValue": "",
					variableType: 1
				})
			},
			outDelect(index) {
				this.outcontent.splice(index, 1)
			},
			submit() {
				let reg = /[\u4e00-\u9fa5]+/g;
				if (this.code.match(reg) != null) {
					this.$message.error('代码不允许出现中文');
					return
				}
				if (this.code.trim() === '') {
					this.$message.error('请填入规则代码，并检查空格');
					return
				}
				if (this.priority === '') {
					this.$message.error('请选择规则优先级');
					return
				}
				if (this.name.trim() === '') {
					this.$message.error('请填入规则名称，并检查空格');
					return
				}
				if (this.description === '') {
					this.$message.error('请填入规则描述，并检查空格');
					return
				}
				if (this.resultFieldEn === "" || this.resultFieldEn == null) {
					this.$message.error('请选择命中时输出变量');
					return
				}
				if (this.scoreFieldEn == "" || this.scoreFieldEn == null) {
					this.$message.error('请选择得分时输出变量');
					return
				}


				if (isNaN(Number(this.SpecialField.score)) || String(this.SpecialField.score).trim() === "") {
					this.$message.error('得分只能是数字');
					return
				}
				let is = {
					is: true,
					msg: '',
				}

				this.ruledata.forEach(value => {
					if (value.logical === "") {
						is.is = false
						is.msg = '请检查是否有连接符未选'
					}
					if (value.fieldId === "") {
						is.is = false
						is.msg = '请检查是否有条件未选'
					}
					if (value.operator === "") {
						is.is = false
						is.msg = '请检查是否有运算符未选'
					}
					if (String(value.fieldValue).trim() === "") {
						is.is = false
						is.msg = '请检查是否有条件对比值未填'
					}



				})


				if (this.SpecialField.score === "" || this.SpecialField.ruleAudit === "") {
					is.is = false
					is.msg = '请检查是否得分未填'
				}


				let str = ""
				this.ruledata.forEach(value => {
					str += value.logical
				})

				str += this.SpecialField.lastLogical
				console.log(str)
				if (!this.is_leagl_brackets(str)) {
					this.$message.error('请检查括号完整性');
					return
				}



				this.outcontent.forEach(value => {
					if (value.fieldId === "" || String(value.fieldValue).trim() === "" || value.variableType === "") {
						is.is = false
						is.msg = '请检查自定义输出部分是否有未填项'
					}
					if (value.variableType == 3 && (String(value.fieldValue).trim() === "" || JSON.parse(value.fieldValue).formula.trim() ===
							'')) {
						is.is = false
						is.msg = '请检查自定义输出部分是否有未填项'
					}
				})

				if (is.is === false) {
					this.$message.error(is.msg);
					return
				}


				this.outcontent.forEach(value => {
					value.fieldEn = this.mixinGetvalueEn(value.fieldId)
					if (value.variableType == 2) {

						value.fieldValue = value.fieldValue + '|' + this.mixinGetvalueEn(value.fieldValue)

					}
				})
				let obj = {
					"code": this.code.trim(),
					"name": this.name.trim(),
					"priority": this.priority,
					"description": String(this.description).trim(),

					score: this.SpecialField.score,
					ruleAudit: this.SpecialField.ruleAudit,
					scoreFieldEn: this.scoreFieldEn,
					resultFieldEn: this.resultFieldEn
				}
				let arr = this.ruledata.map(value => {
					value.valueType = this.getvalueType(value.fieldId)
					console.log(value)
					return value
				})
				obj.difficulty = 1
				obj.ruleFieldList = arr
				obj.strategyOutputList = this.outcontent.length > 0 ? this.outcontent : null
				obj.lastLogical = this.SpecialField.lastLogical

				if (this.id == 0) {
					this.loading = true
					obj.parentId = this.nameId == 99999999 ? 0 : this.nameId,
						this.setsave(obj).then(res => {
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
					this.loading = true
					this.updata(obj).then(res => {
						this.loading = false
						if (res.status === "1") {
							this.$message({
								message: '修改成功',
								type: 'success'
							});
							this.$emit('Ok')
							this.$store.dispatch('reGetRuleList')
						}
					}).catch(err => {
						this.loading = false
						this.$message.error('网络出现问题-_-');
					})
				}
			},
			change(index) {
				this.ruledata[index].operator = ""
				this.ruledata[index].fieldValue = ""
			},
			faadd(index) { //rule父节点添加
				this.ruledata.splice(index + 1, 0, {
					"logical": "",
					"fieldId": "",
					"operator": "",
					"fieldValue": ""
				})

			},
			fadelect(index) {
				this.ruledata.splice(index, 1)
			},
			is_leagl_brackets(string) {
				console.log(1)
				var array = [];
				for (var i = 0; i < string.length; i++) {
					var item = string[i];
					if (item === "(") {
						array.push("(");
					} else if (item === ")") {
						if (array.length === 0) {
							return false;
						} else {
							array.pop();
						}
					} else {
						continue;
					}
				};
				return array.length === 0;
			},
			getvalueType(cont) {
				let num
				this.FieldUser.forEach(value => {
					if (value.id === parseInt(cont)) {
						num = value.valueType
					}
				})
				return num
			},
			getvalueEn(cont) {
				let num
				this.FieldUser.forEach(value => {
					if (value.id === parseInt(cont)) {
						num = value.fieldEn
						console.log(1)
					}
				})
				return num
			}


		},
		watch: {

		}











	}
</script>

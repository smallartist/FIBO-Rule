<style>
	.rule {
		display: flex;
		/* margin-left: 5px; */
		position: relative;
	}

	.Rule_cont {
		width: 30px;
		border-top: 1px dashed #C7C6C8;
		/* border-top: 1px dashed rgb(1,1,1); */
		margin-top: 7px;
		flex-grow: 0;
		height: 1px;
		/* background-color: #C7C6C8; */
		margin: 12px 0px 0 0;
		height: 0;

	}

	.RuleIcon {
		background-color: #EBEBEB;
		border: #DADADA 1px solid;
		border-radius: 4px;
		padding: 4px 6px 4px 2px;
	}

	.RuleSelcet .el-input__inner {
		border-radius: 0 4px 4px 0;
	}



	.RuleCont_for_out {
		border-left: 1px dashed #ccc;
		border-bottom: 1px dashed #ccc;
		border-right: 1px dashed #ccc;
		padding-top: 5px;
		position: relative;
	}
</style>

<template>
	<div class="rule" :style="{marginLeft:ZIndex==1?'20px':'0px'}" v-if="data">
		<div :style="{position: 'absolute',top:'0px', left: '-20px',marginTop: top===0?'0':'8px'}" class="RuleIcon">
			<el-dropdown trigger="click" @command="handleCommand">
				<span class="el-dropdown-link">
					<i class="el-icon-s-operation" :style="{color: color[ZIndex%5] ,fontSize: '16px'}"></i>
				</span>

				<el-dropdown-menu slot="dropdown">
					<el-dropdown-item icon="el-icon-plus" command="addRule" v-if="data.conditionType!=3">添加规则
					</el-dropdown-item>
					<el-dropdown-item icon="el-icon-circle-plus-outline" command="addlogical">添加关系</el-dropdown-item>
					<el-dropdown-item icon="el-icon-video-play" command="addfor" v-if="data.conditionType!=4">添加循环规则
					</el-dropdown-item>
					<el-dropdown-item icon="el-icon-video-play" command="addcond">添加条件组</el-dropdown-item>
					<el-dropdown-item icon="el-icon-close" command="delect" v-if="ZIndex!=1&&data.conditionType!=4">
						删除此节点</el-dropdown-item>
				</el-dropdown-menu>
			</el-dropdown>
		</div>
		<div v-if="data.conditionType==1||data.conditionType==4" class="RuleSelcet">

			<div :style="{width: '80px',marginTop: top===0?'0':'8px'}">
				<el-select v-model="data.logical" placeholder="请选择" size="mini">
					<el-option :key="1" label="AND" value="&&"></el-option>
					<el-option :key="2" label="OR" value="||"></el-option>
				</el-select>
			</div>
		</div>
		<div v-if="data.conditionType==3" class="RuleSelcet">
			<div :style="{width: '80px',marginTop: top===0?'0':'8px'}">
				<el-select v-model="data.logical" placeholder="请选择" size="mini">
					<el-option :key="1" label="FOR" value="for"></el-option>
					<!-- <el-option :key="2" label="包含" value="contains"></el-option> -->
					<!-- <el-option :key="3" label="长度" value="length"></el-option> -->
				</el-select>
			</div>
		</div>

		<div v-if="data.conditionType==5" class="RuleSelcet">

			<div :style="{width: '80px',marginTop: top===0?'0':'8px'}">
				<el-select v-model="data.logical" placeholder="请选择" size="mini">
					<el-option :key="1" label="条件组" value="condGroup"></el-option>
				</el-select>
			</div>
		</div>
		<div style="width: 10px;height: 0;border-top: 1px dashed #C7C6C8;margin-top: 19px;margin-left: 5px;">
		</div>
		<div>
			<div
				:style="{border: '1px dashed '+ color[ZIndex%5],borderRadius: '5px',paddingRight: '5px',paddingBottom: '5px',position:'relative',minWidth:'30px',minHeight:'40px'}">
				<div v-if="data.conditionType==3" style="display: flex;margin-top: 6px;">
					<div class="Rule_cont" :style="{marginTop:'12px'}"></div>
					
					<el-cascader v-model="data.fieldEn" filterable size="mini" :options="fielduserArr" :key="keyValue+(data.random?data.random:0)" @visible-change="randomAdd(data,$event)"
						:props="{ expandTrigger: 'hover' }" @change="EnChange"></el-cascader>
						
					<!-- for 的输入 -->
				</div>
				<div
					:style="{border:data.conditionType==3?'1px dotted #000':'',margin:data.conditionType==3?'5px 10px ':'',padding:data.conditionType==3?'8px':'',paddingLeft:'0px'}">
					{{typeof sdataJson!= 'object'&&typeof sdataJson!= 'array'&&data.conditionType==3?'此处不支持继续遍历请删除此节点':''}}
					<div v-for="(item,index) in data.children" style="display: flex;margin-top: 7px;">
						<div class="Rule_cont"
							:style="{marginTop:item.conditionType===0?'12px':(index===0?'11px':'20px')}">
						</div>
						<!--  conditionType==2  规则部分 -->
						<div v-if="item.conditionType==2" style="display: flex;">
							<!-- {{fieldType}} -->
							<div v-if="fieldType!='for'" style="display: flex;">
								<!-- 普通规则部分 -->

								<!-- 不为输出节点 则拉选 fielduser-->
								
								<div v-if="data.conditionType!=4&&out!='out'" style="display: flex;">
									
									<el-cascader v-model="item.fieldEn" filterable size="mini" :options="fieldUserObj" clearable @change="ruleCascaderChange(item)"
										:key="keyValue+(item.random?item.random:0)" :props="{ expandTrigger: 'hover' }" @visible-change="randomAdd(item,$event)">
									</el-cascader>
									
									<!-- <bigElCascader v-model="item.fieldEn" filterable size="mini" :options="fieldUserObj"
										clearable @change="ruleCascaderChange(item)" :Mykey="keyValue"
										:props="{ expandTrigger: 'hover' }"></bigElCascader> -->
										
									<ruleRelation v-model="item.operator" :value2.sync="item.fieldValue"
										:variableType.sync="item.variableType"
										:valueType="mixinGetValueTypeByJSONEn(item.fieldEn)" size="mini"></ruleRelation>

									<!-- 加入 变量选择 -->
								</div>
								<div v-else style="display: flex;">
									<el-select v-model="item.fieldEn" size="mini" filterable
										@change="selectChange(item)">
										<el-option v-for="cont in suseingfield" :key="cont.id" :label="cont.fieldCn"
											:value="cont.fieldEn">
										</el-option>
									</el-select>
									<ruleRelation v-model="item.operator" :value2.sync="item.fieldValue"
										:variableType.sync="item.variableType"
										:valueType="mixinGetValueTypeByEn(item.fieldEn)" size="mini"></ruleRelation>
								</div>


								<!-- <el-input v-model="item.fieldValue" maxlength="30" size="mini" placeholder="请输入内容,最长30位"
									style="width: 100px;margin-left: 10px;"
									v-if="mixinGetValueTypeByEn(item.fieldEn)!==3&&['is empty','not empty'].indexOf(item.operator)==-1">
								</el-input>
								<el-select v-model="item.fieldValue" placeholder="请选择" size="mini"
									style="width: 100px;margin-left: 10px;"
									v-if="mixinGetValueTypeByEn(item.fieldEn)===3">
									<el-option label="是" value="1"></el-option>
									<el-option label="否" value="0"></el-option>
								</el-select> -->

							</div>
							<div v-else style="display: flex;">
								<!-- {{sEn}} -->
								<!-- for规则部分 -->

								<!-- {{sEn}} -->
								<el-cascader size="mini" filterable v-model="item.fieldEn" :options="getUserObj" @visible-change="randomAdd(item,$event)"
									:key="keyValue+(item.random?item.random:0)" :props="{ expandTrigger: 'hover' }" @change="forChange(item)">
								</el-cascader>
								
								<ruleRelation v-model="item.operator" :value2.sync="item.fieldValue"
									:variableType.sync="item.variableType" :valueType="getvalueTypebyEn(item.fieldEn)"
									size="mini"></ruleRelation>
								<!-- <el-input v-model="item.fieldValue" maxlength="30" size="mini" placeholder="请输入内容,最长30位"
									style="width: 100px;margin-left: 10px;" v-if="getvalueTypebyEn(item.fieldEn)!==3&&['is empty','not empty'].indexOf(item.operator)==-1">
								</el-input>
								<el-select v-model="item.fieldValue" placeholder="请选择" size="mini"
									style="width: 100px;margin-left: 10px;" v-if="getvalueTypebyEn(item.fieldEn)===3">
									<el-option label="是" value="="></el-option>
									<el-option label="否" value="!="></el-option>
								</el-select> -->

							</div>

							<i class="el-icon-circle-close" style="color: #fa4949;margin-left: 5px;"
								@click="deleteSon(index)"></i>
						</div>
						<!--  conditionType==1 关系节点  conditionType==3  for的输出的关系节点  节点部分 -->
						<div v-if="item.conditionType==1||item.conditionType==3||item.conditionType==5">
							<!-- {{fieldType}} -->
							<rule :data="item" :suseingfield="suseingfield" :top="index" :out="out" :ZIndex="ZIndex+1"
								:sEn="En" :index="index" :sdataJson="dataJson" @delectLogical="delectLogical"
								:fieldType="data.conditionType==3||data.loopGroupActions.length>0||fieldType=='for'?'for':''">
							</rule>
						</div>

					</div>

				</div>
				<div v-if="data.conditionType==3" style="margin-left: 25px;margin-top: 10px;">
					<!-- for 的输出 -->

					<rule :suseingfield="useingfield" :out="'out'" :data="data.loopResultCondition" :ZIndex="ZIndex+1"
						@delectLogical="delectLogical"></rule>
				</div>
			</div>
			<div v-if="data.conditionType==5" class="RuleCont_for_out">
				<span style="font-size: 12px;margin-left: 5px;">命中条件：</span>
				[
				<el-input size="mini" v-model="data.condGroupResultCondition.children[0].fieldValue"
					style="width:150px;"></el-input>
				~
				<el-input size="mini" v-model="data.condGroupResultCondition.children[1].fieldValue"
					style="width:150px;"></el-input>
				]

			</div>
			<div v-if="data.loopGroupActions.length>0" class="RuleCont_for_out">
				<div v-for="(item,index) in data.loopGroupActions"
					style="display: flex; align-items: center;margin-bottom: 5px;margin-left: 5px;">
					<el-select v-model="item.actionKey" style="width: 150px;" size="mini" filterable>
						<el-option v-for="cont in fielduser" :key="cont.id" :label="cont.fieldCn" :value="cont.fieldEn"
							v-if="item.actionType!=6||cont.valueType==6">
						</el-option>
					</el-select>
					<p>:</p>
					<el-select v-model="item.actionType" size="mini" filterable @change="GroupselectChange(item)"
						style="width: 100px;">
						<el-option :key="value.value" :label="value.label" :value="value.value"
							v-for="value in actionTypelist"></el-option>
					</el-select>
					<p v-if="item.actionType!=1">:</p>
					<el-input size="mini" style="width: 120px;" v-model="item.actionValue"
						v-if="[2,4,6,7].indexOf(item.actionType)!=-1"></el-input>

					<el-cascader size="mini" style="width: 120px;" filterable v-model="item.actionValue"
						:options="getUserObj" :key="keyValue+(item.random?item.random:0)" v-if="item.actionType==3" @visible-change="randomAdd(item,$event)"
						:props="{ expandTrigger: 'hover' }"></el-cascader>
					
					<i class="el-icon-circle-plus-outline" style="color: #66B1FF;margin-left: 3px;"
						@click="addLoopOut(index)"></i>
					<i class="el-icon-circle-close" style="color: #F56C6C;margin-left: 3px;" v-if="index!=0"
						@click="delectLoopOut(index)"></i>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	import ruleRelation from '@/components/common/ruleRelation.vue'
	import bigElCascader from '@/components/common/bigElCascader.vue'
	export default {
		components: {
			ruleRelation,
			bigElCascader
		},
		name: 'rule',
		data() {
			return {
				color: ['#0D183E', '#409EFF', '#67C23A', '#F56C6C', '#FFCD43'],
				En: '',
				keyValue: 1, //用于给级联选择框重新渲染
				keyValueReady: false,
				actionTypelist: [{
						label: '计数',
						value: 1
					},
					{
						label: '过滤',
						value: 5
					},
					{
						label: '输出变量',
						value: 3
					},
					{
						label: '输出常量',
						value: 4
					},
					{
						label: '添加元素(去重)',
						value: 6
					},
					// {label:'元素添加',value:7},
				]
			}
		},
		created() {

		},
		mounted() {
			if (this.data) {
				// console.log(this.data.fieldEn)
				if (Array.isArray(this.data.fieldEn)) {
					if (this.data.fieldEn.length > 0) {
						this.EnChange(this.data.fieldEn, false)
					}
				}
			}
			this.$nextTick(() => {
				this.keyValueReady = true
				if (this.sEn !== "") {
					this.En = this.sEn
				}
			})

		},
		props: {
			sdataJson: {
				type: Object | String | Number | Boolean,
				default: () => {
					return {}
				}
			},
			fieldType: {
				type: String,
				default: ''
			},
			ZIndex: {
				type: Number,
				default: -1
			},
			sEn: {
				type: String,
				default: ''
			},
			data: {
				type: Object,
				default () {
					return {}
				}
			},
			top: {
				tyep: String,
				default: '8px'
			},
			index: {
				type: Number,
				default: -1
			},
			suseingfield: {
				type: Array,
				default () {
					return []
				}
			},
			out: {
				type: String,
				default: 'unout'
			}
		},
		computed: {
			useingfield() {
				let arr = []
				this.deepGetUseing(this.data, arr)
				// console.log(arr)
				arr = arr.map(value => JSON.stringify(value))
				arr = new Set(arr)
				arr = Array.from(arr)
				arr = arr.map(value => JSON.parse(value))
				return arr
			},
			fieldUserObj() {
				if (this.$store.state.FieldUserObj) {
					return this.$store.state.FieldUserObj.data.fieldList
				} else {
					return []
				}
			},
			dataJson() {
				let obj = {}
				if (this.fieldType == "for") {
					obj = this.sdataJson
				} else if (Array.isArray(this.data.fieldEn)) {
					// console.log(1, this.data.fieldEn)
					obj = JSON.parse(this.mixinGetFieldByEn(this.data.fieldEn[0]).jsonValue)
				}
				if (Array.isArray(this.data.fieldEn)) {
					this.data.fieldEn.forEach((value, index) => {
						if (index != 0) {
							obj = obj[value]
						}
					})
					if (Array.isArray(obj)) {
						obj = obj[0]
					}
				}


				return obj
			},
			getUserObj() {
				let arr = []

				let obj = {
					value: '%' + this.sEn.split('.')[this.sEn.split('.').length - 1] + '%',
					label: '%元素%',
				}
				obj.children = this.getdeepObj(this.sdataJson)

				arr.push(obj)
				let tempTypeArr = this.fielduser.filter((value) => {
					return value.valueType != 6
				})
				arr.push(...this.typeConversion(tempTypeArr))
				return arr
			},
			fielduserArr() {
				let arr = []
				// console.log(11,this.fielduser)
				if (this.fielduser.length > 1) {
					if (this.fieldType != "for") { // 如果元素不为 for 则 用fielduser里的 json 格式指标
						arr = this.fielduser.filter((value) => {
							return value.valueType == 6
						})

						arr = arr.map((value) => {
							let obj = {
								value: value.fieldEn,
								label: value.fieldCn,
							}
							obj.children = this.getdeepArr(JSON.parse(value.jsonValue))
							return obj
						})
					} else { // 如果元素为 for 则 使用元素节点 进行二次遍历
						// console.log(this.sdataJson)
						let obj = {
							value: '%' + this.sEn.split('.')[this.sEn.split('.').length - 1] + '%',
							label: '%元素%'
						}
						let sarr = []

						for (let key in this.sdataJson) {
							if (this.sdataJson.hasOwnProperty(key)) {
								// console.log(key)
								// console.log(this.sdataJson[key])
								if (typeof this.sdataJson[key] != "object" || this.sdataJson[key] == null) {
									continue
								}
								let sobj = {
									value: key,
									label: key
								}
								sobj.children = this.getdeepArr(this.sdataJson[key])
								sarr.push(sobj)
							}
						}
						obj.children = sarr
						arr.push(obj)
					}
					// console.log(arr)
					return arr

				} else {
					return []
				}
			},
			fielduser() {
				if (this.$store.state.FieldUser) {
					return this.$store.state.FieldUser.data.fieldList
				} else {
					return []
				}

			},

		},
		methods: {

			deepGetUseing(obj, arr) {
				if (obj.loopGroupActions.length > 0) {
					obj.loopGroupActions.forEach(value => {
						arr.push({
							fieldEn: value.actionKey,
							fieldCn: this.mixinGetCnByEn(value.actionKey)
						})
					})
				}
				if (obj.children.length > 0) {
					obj.children.forEach(value => {
						this.deepGetUseing(value, arr)
					})
				}

			},
			GroupselectChange(item, e) {
				item.actionValue = ""

				if (this.mixinGetValueTypeByEn(item.actionKey) != 6 && item.actionType == 6) {
					item.actionKey = ''
				}
			},
			forChange(item) {
				item.operator = ""
			},
			getvalueTypebyEn(e) { //通过En 获取 valueType
				if (!Array.isArray(e)) {
					return
				}
				if (e[0][0] !== '%') {
					return this.mixinGetValueTypeByEn(e[0])
				}
				if (e[e.length - 1] == 'length') {
					return 1
				}
				let str = this.sdataJson
				if (JSON.stringify(this.sdataJson) != '{}') {
					e.forEach((value, index) => {
						if (index != 0) {
							str = str[value]
						}
					})
				}
				// console.log(this.sdataJson)
				switch (typeof str) {
					case 'string':
						return 2
						break;
					case 'object':
						return 6
						break;
					case 'boolean':
						return 3
						break;
					default:
						return 1
				}

			},
			EnChange(e, clear = true) {

				this.En = e.join('.')
				if (!clear) return
				this.deepClearEn(this.data)
			},
			deepClearEn(obj) { // 递归清除用到父级的 En
				obj.children.forEach(value => {
					if (Array.isArray(value.fieldEn)) {
						if (value.fieldEn[0][0] == "%") {
							value.fieldEn = ""
						}
					}
					if (value.children.length > 0) {
						this.deepClearEn(value)
					}
				})

			},
			typeConversion(arr) {
				let arr2 = arr.map((value) => {
					return {
						label: value.fieldCn,
						value: value.fieldEn
					}
				})
				return arr2
			},
			getdeepArr(obj) {
				if (Array.isArray(obj)) {
					return false
				} else if (typeof obj == 'object') {
					let arr = []
					for (let key in obj) {
						if (obj.hasOwnProperty(key)) {
							if (Array.isArray(obj[key])) {
								arr.push({
									value: key,
									label: key
								})
							} else if (typeof obj[key] == 'object') {
								arr.push({
									value: key,
									label: key,
									children: this.getdeepArr(obj[key])
								})
							}

						}
					}
					return arr
				} else {
					return []
				}


			},
			getdeepObj(obj) {
				if (typeof obj == 'object') {
					let arr = []
					for (let key in obj) {
						if (obj.hasOwnProperty(key)) {
							if (Array.isArray(obj[key])) {
								arr.push({
									value: key,
									label: key,
									children: [{
										value: 'length()',
										label: '长度'
									}]
								})
							} else if (typeof obj[key] == 'object' && obj[key] != null) {
								arr.push({
									value: key,
									label: key,
									children: this.getdeepObj(obj[key])
								})
							} else {
								arr.push({
									value: key,
									label: key,
								})
							}

						}
					}
					return arr
				}
			},
			getObj(obj) {
				// console.log(obj)
			},
			delectLoopOut(index) {
				this.$confirm('确定删除?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.data.loopGroupActions.splice(index, 1)
					this.$message({
						type: 'success',
						message: '删除成功!'
					});
				}).catch(() => {
					this.$message({
						type: 'info',
						message: '已取消删除'
					});
				});
			},
			addLoopOut(index) {
				this.data.loopGroupActions.splice(index + 1, 0, {
					"id": '',
					"actionType": "",
					"actionKey": "",
					"actionValue": "",
				})
			},
			delectLogical(index) {
				this.data.children.splice(index, 1)
			},
			handleCommand(str) {
				if (str == "addRule") { //添加规则
					// console.log(this.data)
					this.data.children.push({
						"logical": null,
						"fieldId": null,
						"operator": "",
						"fieldValue": "",
						"conditionType": 2,
						"variableType": 1,
						"children": [],
						loopGroupActions: [],
						loopResultCondition: {},
						condGroupResultCondition: {}
					})


				} else if (str == "addlogical") { //添加链接符
					let obj = {
						"logical": '&&',
						"fieldId": "",
						"operator": "",
						"fieldValue": "",
						"conditionType": 1,
						"children": [],
						loopGroupActions: [],
						loopResultCondition: {},
						condGroupResultCondition: {}
					}
					if (this.data.conditionType == 3) {
						obj.loopGroupActions.push({
							"actionType": 1,
							"actionKey": "",
							"actionValue": "",
						})
						obj.children.push({
							"logical": null,
							"fieldId": null,
							"operator": "",
							"fieldValue": "",
							"conditionType": 2,
							"variableType": 1,
							"children": [],
							loopGroupActions: [],
							loopResultCondition: {}
						})
					}
					this.data.children.push(obj)


				} else if (str == "addfor") { // 添加循环规则
					this.data.children.push({
						"logical": 'for',
						"fieldId": "",
						"fieldEn":[],
						"operator": "",
						"fieldValue": "",
						"conditionType": 3,
						"children": [],
						loopGroupActions: [],
						condGroupResultCondition: {},
						loopResultCondition: {
							loopGroupActions: [],
							"id": '',
							"actionType": "",
							"actionKey": "",
							"actionValue": "",
							"logical": '&&',
							"fieldId": "",
							"operator": "",
							"fieldValue": "",
							"conditionType": 4,
							"children": [],
						}
					})


				} else if (str == "delect") {
					this.$confirm('确定删除?', '提示', {
						confirmButtonText: '确定',
						cancelButtonText: '取消',
						type: 'warning'
					}).then(() => {
						this.$emit('delectLogical', this.index)
						this.$message({
							type: 'success',
							message: '删除成功!'
						});
					}).catch(() => {
						this.$message({
							type: 'info',
							message: '已取消删除'
						});
					});


				} else if (str == "addcond") { //添加条件组
					this.data.children.push({
						"logical": 'condGroup',
						"fieldId": "",
						"operator": "",
						"fieldValue": "",
						"conditionType": 5, // 条件组   conditionType 为 5
						"children": [],
						loopGroupActions: [],
						loopResultCondition: {},
						condGroupResultCondition: {
							loopGroupActions: [],
							"id": '',
							"actionType": "",
							"actionKey": "",
							"actionValue": "",
							"logical": '&&',
							"fieldId": "",
							"operator": "",
							"fieldValue": "",
							"conditionType": 6, // 条件组的值 conditionType 为 6
							"children": [{
								operator: ">=",
								fieldValue: "",
								fieldEn: "hitNum",
								conditionType: 2,
								variableType: 1,
								fieldType: 1,
							}, {
								operator: "<=",
								fieldEn: "hitNum",
								fieldValue: "",
								fieldType: 1,
								conditionType: 2,
								variableType: 1,
							}],
						}
					})
				}
			},
			deleteSon(index) {

				this.$confirm('确定删除?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.data.children.splice(index, 1)
					this.$message({
						type: 'success',
						message: '删除成功!'
					});
				}).catch(() => {
					this.$message({
						type: 'info',
						message: '已取消删除'
					});
				});


			},
			ruleCascaderChange(item) {
				item.valueType = this.mixinGetValueTypeByJSONEn(item.fieldEn)
				item.fieldValue = ""
				item.operator = ""
				if (this.data.conditionType != 4 && this.out != 'out') {
					return
				} // 不为for的输出节点 及其后置节点 则退出
				item.fieldType = 1
			},
			selectChange(item) {
				item.valueType = this.mixinGetValueTypeByEn(item.fieldEn)
				item.fieldValue = ""
				item.operator = ""
				if (this.data.conditionType != 4 && this.out != 'out') {
					return
				} // 不为for的输出节点 及其后置节点 则退出
				item.fieldType = 1
			},
			getvalueType(cont) {
				let num
				this.fielduser.forEach(value => {
					if (value.id === cont) {
						num = value.valueType
					}
				})
				return num
			}
		},
		watch: {
			getUserObj() {
				if (this.keyValueReady) {
					this.keyValue++
				}

			},
			fielduserArr() {
				if (this.keyValueReady) {
					this.keyValue++
				}
			}
		}

	}
</script>

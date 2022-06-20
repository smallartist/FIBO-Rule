<style>
	.opType1_subbtitle {
		color: #666;
		margin-top: 10px;
		margin-bottom: 10px;
	}

	.opType2Cont {
		background-color: #fff;
		padding: 5px;
		margin-top: 10px;
		border-radius: 10px;
		border: 1px dotted #aaa
	}

	.opTypeRow {
		display: flex;
		align-items: center;
		margin-top: 10px;
		border-bottom: 1px dotted #eee;
		padding-bottom: 10px;

	}

	.opTypeRow>p:nth-of-type(1) {
		width: 100px;

	}

	.opTypeDelete {
		color: red;
		margin-left: 10px;
		font-size: 24px;
		position: absolute;
		top: 20px;
		right: 20px;
	}

	.opType_ps {
		color: #666;
		font-size: 14px;
	}
</style>

<template>

	<div style="padding: 10px;">

		<div class="opTypeRow">
			<p class="opType1_subbtitle">
				源数据：
			</p>
			<div>
				<el-select v-model="data.inputFieldEn" filterable placeholder="只允许选择JSON类型指标"
					@change="data.originalDataOp.opField=[];opFieldChange()" style="width: 300px;">
					<el-option v-for="item in FieldUser" v-if="item.valueType==6" :key="item.value"
						:label="item.fieldCn" :value="item.fieldEn">
					</el-option>
				</el-select>
			</div>
		</div>





		<div class="opTypeRow" v-if="data.originalDataOp">
			<p class="opType1_subbtitle">
				循环对象：
			</p>
			<div>

				<el-cascader v-model="data.originalDataOp.opField" filterable :options="getdeepArr(FieldUserJSON)"
					:key="(data.random?data.random:0)" @visible-change="randomAdd(data,$event)" style="width: 300px;"
					placeholder="只允许选择'数据'下的数组类型属性,可不选" :props="{ expandTrigger: 'hover' }" @change="opFieldChange">
				</el-cascader>


				<p class="opType_ps">
					*切换 源数据 和 循环对象 会重置下面所选的信息
				</p>
			</div>
		</div>




		<template v-for="(value,index) in data.conditionAndOutPutList">


			<div class="opTypeRow">


				<p class="opType1_subbtitle">
					选择集合：
				</p>
				<div>
					<el-cascader v-model="value.dataCleanBlock.handleCollection" filterable
						:options="getdeepArr(eachObjJSON)" :key="(value.random?value.random:0)"
						@visible-change="randomAdd(value,$event)" placeholder="只允许选择'数据'下的数组类型属性"
						@change="handleCollectionChange(index,$event)" :props="{ expandTrigger: 'hover' }"  style="width: 300px;">
					</el-cascader>
					<!-- {{eachObjJSON}} -->
				</div>



			</div>


			<div :class="opType==2?'opType2Cont':''" v-if="index==0||opType==2" style="position: relative;">

				<!-- <div class="opTypeRow" v-if="opType==2">
				<p class="opType1_subbtitle">
					运算
				</p>
				<el-input v-model="value.dataCleanBlock.name" style="width: 220px;"></el-input>

			</div> -->

				<div class="opTypeRow">

					<p class="opType1_subbtitle">
						进入过滤
					</p>

					<div>
						<div
							v-if="value.inputFilterCondition.children&&value.inputFilterCondition.children.length>0&&ValueObj">

							<div v-for="(item,index) in value.inputFilterCondition.children"
								style="margin-bottom: 10px;display: flex;align-items: center;">
								<el-cascader v-if="item.operator!='black_list'" v-model="item.opKey" filterable
									:options="getcustomObj([],value.dataCleanBlock.handleCollection)"
									:key="cascaKey+(item.random?item.random:0)" @visible-change="randomAdd(item,$event)"
									:props="{ expandTrigger: 'hover' }" style="margin-right: 20px;width: 300px;flex-shrink: 0;"></el-cascader>


								<el-select v-model="item.operator" placeholder="请选择" @change="operatorChange(item)"
									style="margin-right: 20px;">
									<el-option v-for="cont in filterOptions" :key="cont.value" :label="cont.label"
										:value="cont.value">
									</el-option>
								</el-select>

								<el-input v-model="item.variableValue" placeholder="请以逗号分割" v-if="item.operator!='min'">
								</el-input>


								<!-- <varialeSelect v-if="item.operator=='black_list'" :variableType.sync="item.variableType"
									:disabled="[1,2]" :variableCascaderValue="ValueObj" :variableCascader="true"
									size="medium" height="36px" v-model="item.variableValue" :interceptCustom="true"
									@CustomCallback="CustomCallback(item,'variableValue','blackList')"
									style="margin-left: 10px;">
								</varialeSelect> -->



								<i class="el-icon-circle-close" style="color: red;margin-left: 10px;"
									@click="deleteFilterCondition(value.inputFilterCondition.children,index)"></i>
							</div>
						</div>
						<el-button @click="addfilterCondition(value.inputFilterCondition.children)">+</el-button>
					</div>

				</div>

				<div class="opTypeRow">
					<p class="opType1_subbtitle">
						分组
					</p>

					<div>
						<div
							v-if="value.dataCleanBlock.groupFields&&value.dataCleanBlock.groupFields.length>0&&ValueObj">

							<div v-for="(item,index) in value.dataCleanBlock.groupFields" style="margin-bottom: 10px;">
								<el-cascader v-model="value.dataCleanBlock.groupFields[index]" filterable
									:options="getcustomObj([],value.dataCleanBlock.handleCollection)"
									:key="cascaKey+(item.random?item.random:0)" @visible-change="randomAdd(item,$event)"
									:props="{ expandTrigger: 'hover' }" style="width: 300px;">
								</el-cascader>
								<i class="el-icon-circle-close" style="color: red;margin-left: 10px;"
									@click="deleteGroupFields(value.dataCleanBlock.groupFields,index)"></i>
							</div>
						</div>
						<el-button @click="addgroupFields(value.dataCleanBlock.groupFields)">+</el-button>
					</div>

				</div>

				<div class="opTypeRow">
					<p class="opType1_subbtitle">
						修改元素条件
					</p>
					<rule :data="value.condition" :ZIndex="1" :ruleList="opTypeList"
						:customUserObj="getcustomObj(ValueObj,value.dataCleanBlock.handleCollection) "
						@CustomCallback="CustomCallback($event,value.dataCleanBlock.handleCollection,'variableValue')"></rule>


				</div>
				<div class="opTypeRow">
					<p class="opType1_subbtitle">
						命中修改
					</p>
					<div>
						<dataClear :list="value.successOutput"
							:option="getcustomObj([],value.dataCleanBlock.handleCollection)"
							@CustomCallback="CustomCallback($event,value.dataCleanBlock.handleCollection)">
						</dataClear>
					</div>
				</div>
				<div class="opTypeRow">
					<p class="opType1_subbtitle">
						未命中修改
					</p>
					<div>
						<dataClear :list="value.failOutput"
							:option="getcustomObj([],value.dataCleanBlock.handleCollection)"
							 @CustomCallback="CustomCallback($event,value.dataCleanBlock.handleCollection)">
						</dataClear>
					</div>
				</div>

				<div class="opTypeRow">
					<p class="opType1_subbtitle">
						返回过滤
					</p>

					<div>
						<div
							v-if="value.resultFilterCondition.children&&value.resultFilterCondition.children.length>0&&ValueObj">

							<div v-for="(item,index) in value.resultFilterCondition.children"
								style="margin-bottom: 10px;display: flex;align-items: center;">
								<el-cascader v-if="item.operator!='black_list'" v-model="item.opKey" filterable
									:options="getcustomObj([],value.dataCleanBlock.handleCollection)"
									:key="cascaKey+(item.random?item.random:0)" @visible-change="randomAdd(item,$event)"
									:props="{ expandTrigger: 'hover' }" style="margin-right: 20px;width: 300px;flex-shrink: 0;"></el-cascader>


								<el-select v-model="item.operator" placeholder="请选择" @change="operatorChange(item)"
									style="margin-right: 20px;">
									<el-option v-for="cont in filterOptions" :key="cont.value" :label="cont.label"
										:value="cont.value">
									</el-option>
								</el-select>

								<el-input v-model="item.variableValue" placeholder="请以逗号分割"
									v-if="item.operator!='min'">
								</el-input>


								<!-- <varialeSelect v-if="item.operator=='black_list'" :variableType.sync="item.variableType"
									:disabled="[1,2]" :variableCascaderValue="ValueObj" :variableCascader="true"
									size="medium" height="36px" v-model="item.variableValue" :interceptCustom="true"
									@CustomCallback="CustomCallback(item,'variableValue','blackList')"
									style="margin-left: 10px;">
								</varialeSelect> -->



								<i class="el-icon-circle-close" style="color: red;margin-left: 10px;"
									@click="deleteFilterCondition(value.resultFilterCondition.children,index)"></i>
							</div>
						</div>
						<el-button @click="addfilterCondition(value.resultFilterCondition.children)">+</el-button>
					</div>

				</div>



				<p v-if="index>0">
					<i class="el-icon-circle-close opTypeDelete" @click="deleteConditionAndOutPutList(index)"></i>
				</p>
			</div>
		</template>
		<el-button type="primary" v-if="opType==2" style="margin: 10px 0 40px 0;" @click="addConditionAndOutPutList"
			plain>
			添加处理</el-button>
		
		<el-dialog title="值编辑器" v-if="dialogVisible" :visible.sync="dialogVisible" width="70%"
			:close-on-click-modal="false" append-to-body @close="reSetTemp">
			<teParam v-if="dialogVisible" v-model="tempValue" :paramList="tempParam"  :Disabled="teParamdisabled"
				:options="tempOptions">
			</teParam>
			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogVisible=false;reSetTemp()">取 消</el-button>
				<el-button type="primary" @click="valueCompileCallback()">确 定</el-button>
			</span>
		</el-dialog>



	</div>

</template>

<script>
	import listOutPut from '@/components/common/listOutput.vue'
	import varialeSelect from '@/components/models/varialeSelect.vue'
	import teParam from './teParam.vue'
	import rule from './RuleCont.vue'
	import {
		GetdeepObj
	} from '@/utils/GetdeepObj.js'
	import dataClear from '@/components/common/dataRinse/dataClear.vue'
	export default {
		components: {
			rule,
			teParam,
			varialeSelect,

			dataClear
		},
		data() {
			return {
				valueCompileCallback: () => {}, //值编辑器确定回调
				dialogVisible:false,
				tempValue: '',
				tempParam: [],
				tempOptions:[],
				cascaKey: 0, //用于级联选择框重新渲染
				opTypeList: [{ //算子
						label: '个数',
						value: 'count'
					},
					{
						label: '去重个数',
						value: 'distinct_count'
					},
					{
						label: '最大',
						value: 'max'
					},
					{
						label: '最小',
						value: 'min'
					},
					{
						label: '平均',
						value: 'avg'
					},
				],
				filterOptions: [{
					value: '==',
					label: '等于'
				}, {
					value: '!=',
					label: '不等于'
				}, {
					value: 'in',
					label: 'in'
				}, {
					value: 'not in',
					label: 'not in'
				}, {
					value: 'min',
					label: '最小'
				}],
				teParamdisabled: false
			}
		},
		props: {
			data: {
				type: Object,
				default () {
					return {}
				}
			},
			opType: {
				type: Number,
				default: 1
			}
		},
		created() {
			// if (!Array.isArray(this.data.groupFields)) {
			// 	this.data.groupFields = [
			// 		['']
			// 	]
			// }
			console.log(this.data)

		},
		methods: {
			handleCollectionChange(index, e) {
				console.log(1)
				//  this.data.conditionAndOutPutList[index] =  {
				// 	condition: {
				// 		logical: "&&",
				// 		conditionType: 1,
				// 		children: []
				// 	},
				// 	defaultOutput: [],
				// 	resultFilterCondition: {
				// 		conditionType: 1,
				// 		logical: "&&",
				// 		children: [],
				// 		variableType: 1
				// 	},
				// 	inputFilterCondition: {
				// 		conditionType: 1,
				// 		logical: "&&",
				// 		children: [],
				// 		variableType: 1
				// 	},
				// 	failOutput: [],
				// 	successOutput: [],
				// 	dataCleanBlock: {
				// 		name: "",
				// 		groupFields: [],
				// 		handleCollection: e
				// 	}
				// }
				this.$set(this.data.conditionAndOutPutList,index,{
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
						groupFields: [],
						handleCollection: e
					}
				})
				// value.dataCleanBlock.handleCollection = e
			},
			opFieldChange() {
				this.$emit('opFieldChange')
			},
			getCustomField(strarr) {
				let obj = {}
				if (strarr.length) {
					obj = this.FieldUserJSON
					if (this.data.originalDataOp.opField.length) {
						this.data.originalDataOp.opField.forEach((value, index) => {
							obj = obj[value]
							if (index == this.data.originalDataOp.opField.length - 1) {
								obj = obj[0]
							}
						})
					}

					strarr.forEach((value, index) => {
						obj = obj[value]
						if (index == strarr.length - 1) {
							obj = obj[0]
						}
					})

				}
				return Object.keys(obj).map(value => {
					return {
						fieldCn: value,
						fieldEn: value,
						id: 1
					}
				})
			},
			getcustomObj(arr, strarr) {
				let arr2 = JSON.parse(JSON.stringify(arr))
				let obj = {}
				if (strarr.length) {
					obj = this.FieldUserJSON
					if (this.data.originalDataOp.opField.length) {
						this.data.originalDataOp.opField.forEach((value, index) => {
							obj = obj[value]
							if (index == this.data.originalDataOp.opField.length - 1) {
								obj = obj[0]
							}
						})
					}


					strarr.forEach((value, index) => {
						obj = obj[value]
						if (index == strarr.length - 1) {
							obj = obj[0]
						}
					})
				}

				arr2.push({
					label: '集合元素',
					value: 'handle_collection',
					children: GetdeepObj(obj, true, true),
				})


				return arr2

			},
			operatorChange(item) {
				if (item.operator == 'min') {
					
				item.variableValue = ''
				
				}
			},

			deleteFilterCondition(item, index) {

				this.$confirm('确定删除?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					item.splice(index, 1)
					this.$message({
						type: 'success',
						message: '删除成功!'
					});
				})


			},

			addfilterCondition(value) {
				if (!this.ValueObj) {
					this.$message.error('此数据下无可用字段')
					return
				}
				value.push({
					children: [],
					conditionType: 2,
					logical: null,
					variableType: 1,
					variableValue: "",
					opKey: [],
					operator: "in"
				})
			},
			reSetTemp() {
				this.tempValue = '';
				this.valueCompileCallback = () => {};
				this.tempParam = []

				this.teParamdisabled = false

			},
			deleteConditionAndOutPutList(index) {
				this.$confirm('确定删除?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.data.conditionAndOutPutList.splice(index, 1)
					this.$message({
						type: 'success',
						message: '删除成功!'
					});
				})

			},
			addConditionAndOutPutList() {
				this.data.conditionAndOutPutList.push({
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
						groupFields: [],
						handleCollection: []
					}
				})
			},

			CustomCallback(item, strarr,key="outputValue") {
				
				this.tempOptions=this.getcustomObj([],strarr)
				
				
				if (!item[key]) {
					item[key] = JSON.stringify({
						farr: [],
						formula: ''
					})
				}
				this.tempValue = JSON.parse(item[key]).formula
				this.tempParam = JSON.parse(item[key]).farr.map(value=>{
					value.fieldEn = value.opType +'.'+ value.fieldEn
					value.fieldEn = value.fieldEn.split('.')
					return value
				})
				console.log(this.tempParam )

				// if (item.arr) {
				// 	this.tempAddOpkey = item.arr.map(x => {
				// 		return {
				// 			fieldEn: x,
				// 			fieldCn: x,
				// 			id: parseInt(Math.random() * -9999),
				// 			outputKey: true
				// 		}
				// 	})
				// }

				// if (type == 'blackList') {
				// 	console.log(type)
				// 	this.teParamdisabled = true
				// }


				this.dialogVisible = true
				// console.log(this.tempParam)

				// this.tempParam.forEach(value => {
				// 	value.paramList = value.paramList ? value.paramList.map(item => {
				// 		item.value = item.type == 2 ? item.value.split('.') : item.value
				// 		return item
				// 	}) : []
				// })



				this.valueCompileCallback = () => {

					let arr = JSON.parse(JSON.stringify(this.tempParam))

					let obj = {
						farr: arr.map(value => {
							
							value.opType =  value.fieldEn.shift()
							value.fieldEn = value.fieldEn.join('.')
							return {
								fieldCn: value.fieldCn,
								fieldEn: value.fieldEn,
								valueType: value.valueType,
								opType:value.opType
							}
						}),
						formula: this.tempValue
					}

					// if (type == 'blackList') {
					// 	obj.formula = obj.farr[0] ? `@${obj.farr[0].fieldCn}@` : ''
					// }

					if (this.examineVariableValue(obj)) {
						return
					}

					let arr2 = obj.formula.match(/@.*?@/g)

					// if (arr2) {
					// 	arr2 = arr2.filter(value => !obj.farr.find(x => x.fieldCn == value.substring(1, value.length -
					// 		1)))

					// 	arr2 = arr2.map(value => {
					// 		return {
					// 			fieldCn: value.substring(1, value.length - 1),
					// 			fieldEn: value.substring(1, value.length - 1),
					// 			temp: true,
					// 			valueType: 1
					// 		}
					// 	})
					// 	obj.farr.push(...arr2)

					// }

					item[key] = JSON.stringify(obj)
					this.dialogVisible = false
					this.reSetTemp()
					
				}


			},
			examineVariableValue(obj) {
				let is = {
					is: false,
					msg: ''
				}

				if (!obj.formula) {
					is.is = true
					is.msg = '请输入自定义内容'
				}

				obj.farr.forEach(value => {
					if (!value.fieldEn) {
						is.is = true
						is.msg = '请选择入参指标'
					}
					if (!value.fieldCn) {
						is.is = true
						is.msg = '请输入入参别名'
					}

				})
				// 验证是否有重名
				let DuplicatesArr = obj.farr.map(x => x.Cn)
				let DuplicatesArr2 = Array.from(new Set(DuplicatesArr))

				if (DuplicatesArr.length !== DuplicatesArr2.length) {
					is.is = true
					is.msg = '请查看入参是否有重名'
				}


				// console.log(obj)
				let arr = obj.formula.match(/@.*?@/g)
				
				if (arr) {
					arr.forEach(value => {
						if (!obj.farr.find(x => x.fieldCn == value.substring(1, value.length - 1))) {
							is.is = true
							is.msg = '请检查是否有 未入参 但已使用的 字段'
						}
					})
				}


				if (is.is) {
					this.$message.error(is.msg)
				}
				return is.is

			},
			deleteGroupFields(item, index) {
				this.$confirm('确定删除?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					item.splice(index, 1)
					this.$message({
						type: 'success',
						message: '删除成功!'
					});
				})



			},
			addgroupFields(value) { //添加分组
				console.log(value)
				if (!this.ValueObj) {
					this.$message.error('此数据下无可用字段')
					return
				}
				if (Array.isArray(value)) {
					value.push([''])
					// this.$set(value.groupFields,0,[''])
				} else {
					value = [
						['']
					]
					// this.$set(value,'groupFields',[['']])
				}
			},

			getdeepArr(obj) {
				if (Array.isArray(obj)) {
					return [{
						value: '[]',
						label: '元素本身'
					}]
				} else if (typeof obj == 'object') {
					// console.log(obj)
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
			}
		},
		computed: {
			FieldUser() {
				if (!this.$store.state.FieldUser) {
					return []
				} else {
					return this.$store.state.FieldUser.data.fieldList
				}
			},
			FieldUserObj() {
				if (!this.$store.state.FieldUserObj) {
					return []
				} else {
					return this.$store.state.FieldUserObj.data.fieldList
				}
			},

			FieldUserJSON() { //所选数据源的json
				if (this.data.inputFieldEn) {
					let obj = this.FieldUser.find(x => x.fieldEn == this.data.inputFieldEn)

					return obj ? JSON.parse(obj.jsonValue) : {}

				}
				return {}
			},
			eachObjJSON() { //循环对象下层JSON

				if (!this.data.originalDataOp.opField.length) {
					return this.FieldUserJSON
				}
				var arr = this.FieldUserJSON

				this.data.originalDataOp.opField.forEach(value => {
					if (value == '[]') {
						arr = arr[0]
					} else {
						arr = arr[value]
					}
				})
				console.log(this.data.originalDataOp.opField)

				return arr[0]


			},


			ValueObj() { //分组输入字段 的 元素 Obj

				let arr = []
				if (this.data.inputFieldEn) {
					let obj = this.FieldUser.find(x => x.fieldEn == this.data.inputFieldEn)
					let arr2 = GetdeepObj(this.FieldUserJSON, true, true)

					arr.push({
						label: obj.fieldCn,
						value: 'original',
						children: arr2
					})
				}
				if (this.data.originalDataOp.opField.length) {
					let arr2 = this.FieldUserJSON

					this.data.originalDataOp.opField.forEach(value => {

						if (value == '[]') {
							arr2 = arr2[0]
						} else {
							arr2 = arr2[value]
						}

					})
					console.log(arr2)
					arr2 = GetdeepObj(arr2[0], true, true)
					arr.push({
						label: '循环对象元素',
						value: 'data_op',
						children: arr2
					})
				}






				return arr


				// if (this.data.inputFieldEn && this.data.inputFieldEn.length > 0) {
				// 	let obj = this.FieldUserObj.find(x => x.value == this.data.inputFieldEn[0])
				// 	obj = JSON.parse(obj.jsonValue)
				// 	this.data.inputFieldEn.forEach((value, index) => {

				// 		if (index) {
				// 			obj = obj[value]
				// 		}

				// 	})
				// 	obj = obj[0]
				// 	obj = GetdeepObj(obj, false)
				// 	this.cascaKey++

				// 	return obj
				// } else {
				// 	return null
				// }


			}
		},
	}
</script>

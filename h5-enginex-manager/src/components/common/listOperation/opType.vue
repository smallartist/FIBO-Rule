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
				数据：
			</p>
			<div>
				<el-cascader v-model="data.inputFieldEn" filterable :options="FieldUserArr"
					:key="(data.random?data.random:0)" @visible-change="randomAdd(data,$event)"
					:props="{ expandTrigger: 'hover' }" @change="$emit('changeInputFieldEn',data.inputFieldEn)">
				</el-cascader>

				<p class="opType_ps">
					*切换数据源会重置下面所选的信息 请谨慎切换
				</p>
			</div>
		</div>


		<div :class="opType==2?'opType2Cont':''" v-for="(value,index) in data.conditionAndOutPutList"
			v-if="index==0||opType==2" style="position: relative;">

			<div class="opTypeRow" v-if="opType==2">
				<p class="opType1_subbtitle">
					运算
				</p>
				<el-input v-model="value.listOperationBlock.name" style="width: 220px;"></el-input>

			</div>



			<div class="opTypeRow">
				<p class="opType1_subbtitle">
					过滤
				</p>

				<div>
					<div v-if="value.filterCondition.children&&value.filterCondition.children.length>0&&ValueObj">

						<div v-for="(item,index) in value.filterCondition.children"
							style="margin-bottom: 10px;display: flex;align-items: center;">
							<el-cascader v-if="item.operator!='black_list'" v-model="item.opKey" filterable
								:options="ValueObj" :key="cascaKey+(item.random?item.random:0)"
								@visible-change="randomAdd(item,$event)" :props="{ expandTrigger: 'hover' }"
								style="margin-right: 20px;"></el-cascader>


							<el-select v-model="item.operator" placeholder="请选择" @change="operatorChange(item)"
								style="margin-right: 20px;">
								<el-option v-for="cont in filterOptions" :key="cont.value" :label="cont.label"
									:value="cont.value">
								</el-option>
							</el-select>

							<el-input v-model="item.variableValue" placeholder="请以逗号分割"
								v-if="item.operator!='black_list'">
							</el-input>


							<varialeSelect v-if="item.operator=='black_list'" :variableType.sync="item.variableType"
								:disabled="[1,2]" :variableCascaderValue="ValueObj" :variableCascader="true"
								size="medium" height="36px" v-model="item.variableValue" :interceptCustom="true"
								@CustomCallback="CustomCallback(item,'variableValue','blackList')" style="margin-left: 10px;">
							</varialeSelect>



							<i class="el-icon-circle-close" style="color: red;margin-left: 10px;"
								@click="deleteFilterCondition(value.filterCondition.children,index)"></i>
						</div>
					</div>
					<el-button @click="addfilterCondition(value.filterCondition.children)">+</el-button>
				</div>

			</div>


			<div class="opTypeRow">
				<p class="opType1_subbtitle">
					分组
				</p>

				<div>
					<div
						v-if="value.listOperationBlock.groupFields&&value.listOperationBlock.groupFields.length>0&&ValueObj">

						<div v-for="(item,index) in value.listOperationBlock.groupFields" style="margin-bottom: 10px;">
							<el-cascader v-model="value.listOperationBlock.groupFields[index]" filterable :options="ValueObj"
								:key="cascaKey+(item.random?item.random:0)" @visible-change="randomAdd(item,$event)"
								:props="{ expandTrigger: 'hover' }"></el-cascader>
							<i class="el-icon-circle-close" style="color: red;margin-left: 10px;"
								@click="deleteGroupFields(value.listOperationBlock.groupFields,index)"></i>
						</div>
					</div>
					<el-button @click="addgroupFields(value.listOperationBlock.groupFields)">+</el-button>
				</div>

			</div>



			<div class="opTypeRow">
				<p class="opType1_subbtitle">
					条件
				</p>
				<rule :data="value.condition" :ZIndex="1" :ruleList="opTypeList" :customUserObj="ValueObj"
					@CustomCallback="CustomCallback"></rule>

			</div>
			<div class="opTypeRow">
				<p class="opType1_subbtitle">
					命中输出：
				</p>

				<div>
					<listOutPut :list="value.successOutput"
						:option="[...opTypeList,{label:'集合',value:'list_element'},{label:'自定义',value:'custom'}]"
						@CustomCallback="CustomCallback($event,'outputValue')" :ValueObj="ValueObj"></listOutPut>
				</div>

			</div>
			<div class="opTypeRow">
				<p class="opType1_subbtitle">
					未命中输出：
				</p>

				<div>
					<listOutPut :list="value.failOutput"
						:option="[...opTypeList,{label:'集合',value:'list_element'},{label:'自定义',value:'custom'}]"
						@CustomCallback="CustomCallback($event,'outputValue')" :ValueObj="ValueObj"></listOutPut>
				</div>

			</div>
			<p v-if="index>0">
				<i class="el-icon-circle-close opTypeDelete" @click="deleteConditionAndOutPutList(index)"></i>
			</p>
		</div>
		<el-button type="primary" v-if="opType==2" style="margin: 10px 0 40px 0;" @click="addConditionAndOutPutList"
			plain>
			添加处理</el-button>




		<el-dialog title="值编辑器" v-if="dialogVisible" :visible.sync="dialogVisible" width="70%"
			:close-on-click-modal="false" append-to-body @close="reSetTemp">
			<teParam v-if="dialogVisible" v-model="tempValue" :paramList="tempParam" :tempAddOpkey="tempAddOpkey" :Disabled="teParamdisabled"
				:variableCascaderValue="ValueObj">
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
	import teParam from '@/components/common/teParam.vue'
	import rule from './RuleCont.vue'
	import {
		GetdeepObj
	} from '@/utils/GetdeepObj.js'
	export default {
		components: {
			rule,
			teParam,
			varialeSelect,
			listOutPut
		},
		data() {
			return {
				valueCompileCallback: () => {}, //值编辑器确定回调
				dialogVisible: false,
				tempValue: '',
				tempParam: [],
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
				tempAddOpkey: [], //用于命中未命中弹窗中选择 之前的
				filterOptions: [{
					value: 'in',
					label: 'in'
				}, {
					value: 'not in',
					label: 'not in'
				}, {
					value: 'black_list',
					label: '匹配名单库'
				}],
				teParamdisabled:false 
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
			operatorChange(item) {
				if (item.operator == 'black_list') {
					item.opKey = ['']
					item.variableType = 3
				} else {
					item.variableType = 1
				}
				item.variableValue = ''
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
					failOutput: [],
					successOutput: [],
					filterCondition: {
						conditionType: 1,
						logical: "&&",
						children: [],
						variableType: 1
					},
					listOperationBlock: {
						name: "",
						groupFields: [],
					}
				})
			},
			
			CustomCallback(item, key = "variableValue",type="") {
				// console.log(item)
				if (!item[key]) {
					item[key] = JSON.stringify({
						farr: [],
						formula: ''
					})
				}
				this.tempValue = JSON.parse(item[key]).formula
				this.tempParam = JSON.parse(item[key]).farr.filter(x=>!x.temp)
				
				if (item.arr) {
					this.tempAddOpkey = item.arr.map(x => {
						return {
							fieldEn: x,
							fieldCn: x,
							id: parseInt(Math.random() * -9999),
							outputKey: true
						}
					})
				}
				
				if(type =='blackList'){
					console.log(type)
					this.teParamdisabled = true
				}


				this.dialogVisible = true
				// console.log(this.tempParam)

				this.tempParam.forEach(value => {
					value.paramList = value.paramList ? value.paramList.map(item => {
						item.value = item.type == 2 ? item.value.split('.') : item.value
						return item
					}) : []
				})



				this.valueCompileCallback = () => {

					let arr = JSON.parse(JSON.stringify(this.tempParam))

					let obj = {
						farr: arr.map(value => {
							return {
								fieldCn: value.fieldCn,
								fieldEn: value.fieldEn,
								id: value.id,
								valueType: value.valueType,
								sqlStatement: value.sqlStatement,
								jsonValue: value.jsonValue,
								paramList: value.paramList ? value.paramList.map(cont => {
									cont.value = cont.type == 2 ? cont.value.join('.') : cont.value
									return cont
								}) : ''
							}
						}),
						formula: this.tempValue
					}
					
					if(type=='blackList'){
						obj.formula = obj.farr[0]?`@${obj.farr[0].fieldCn}@`:''
					}
					
					if (this.examineVariableValue(obj)) {
						return
					}
					
					let arr2 = obj.formula.match(/@.*?@/g)
					
					if(arr2){
						arr2 = arr2.filter(value=> !obj.farr.find(x=>x.fieldCn==value.substring(1,value.length-1)) )
						
						arr2 = arr2.map(value=>{
							return {
								fieldCn:value.substring(1,value.length-1),
								fieldEn:value.substring(1,value.length-1),
								temp : true,
								valueType:1
							}
						})
						obj.farr.push(...arr2)
						
					}
					
					item[key] = JSON.stringify(obj)
					this.dialogVisible = false
					this.reSetTemp()
					// console.log(item)
					
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
					if (!value.id || !value.fieldEn) {
						is.is = true
						is.msg = '请选择入参指标'
					}

					if (value.paramList && value.paramList.length > 0) {
						value.paramList.forEach(item => {
							if (!item.value) {
								is.is = true
								is.msg = '请输入入参指标的次级参数'
							}
						})
					}
				})
				// 验证是否有重名
				let DuplicatesArr = obj.farr.map(x => x.id)
				let DuplicatesArr2 = Array.from(new Set(DuplicatesArr))

				if (DuplicatesArr.length !== DuplicatesArr2.length) {
					is.is = true
					is.msg = '请查看入参是否有重名'
				}


				// console.log(obj)
				let arr = obj.formula.match(/@.*?@/g)

				if (arr) {
					arr.forEach(value => {
						if (!obj.farr.find(x => x.fieldCn == value.substring(1, value.length - 1)) && !this
							.tempAddOpkey.find(x => x.fieldCn == value.substring(1, value.length - 1))) {
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
			FieldUserArr() {
				if (this.FieldUserObj.length == 0) {
					return []
				} else {
					let arr = this.FieldUser.filter((value) => {
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
					// console.log(arr)
					return arr
				}
			},
			ValueObj() { //分组输入字段 的 元素 Obj


				if (this.data.inputFieldEn && this.data.inputFieldEn.length > 0) {
					let obj = this.FieldUserObj.find(x => x.value == this.data.inputFieldEn[0])
					obj = JSON.parse(obj.jsonValue)
					this.data.inputFieldEn.forEach((value, index) => {

						if (index) {
							obj = obj[value]
						}

					})
					obj = obj[0]
					obj = GetdeepObj(obj, false)
					this.cascaKey++

					return obj
				} else {
					return null
				}


			}
		},
	}
</script>

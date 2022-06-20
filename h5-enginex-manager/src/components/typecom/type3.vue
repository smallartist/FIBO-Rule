<style>
	.type {
		border-top: 1px solid #ddd;
		padding: 5px;
		box-sizing: border-box;
	}

	.type_header {
		font-size: 12px;
		font-weight: bold;
	}

	.type3_fa {
		border: 1px solid #ddd;
		padding: 5px;
		border-radius: 10px;
		font-size: 14px;
		position: relative;
		margin-top: 20px;
	}

	.type3_delect_fa {
		border: 1px solid #F56C6C;
		border-radius: 50%;
		padding: 1px;
		position: absolute;
		top: -10px;
		right: -5px;
		background-color: #f5e7e7;
	}

	.type3 .type3_rule_home {
		height: 80%;
	}

	
</style>

<template>
	<div class="type4 type type3" v-loading="loading" style="width: 550px;height: 100%;">
		<div class="type_header">
			<p>节点类型:分组</p>
		</div>
		<div
			style="background-color: #aaa; height: 30px;line-height: 30px; color: #fff;padding: 10px; box-sizing: border-box; margin: 10px 0;display: flex;align-items: center;justify-content: space-between;">
			<p>分组设置</p>
			<p style="display: flex;align-items: center;">
				<i class="el-icon-plus" @click="faAdd"
					style="font-size: 20px;color: #409EFF;padding: 2px;border-radius: 50px;border: 1px solid #409EFF; background-color:#cfe6fd ;"></i>
			</p>
		</div>
		<div class="type3_rule_home">
			<div v-for="(item,index) in datajson.conditions" class="type3_fa" :key="index">
				分组名称: <el-input v-model="item.group_name" placeholder="请输入分组名" size="mini" style="width: 200px;">
				</el-input>

				<p>分组条件:</p>
				<div v-for="(value,inde) in item.formulas"
					style="display: flex; margin-top: 5px;align-items: center;transition: all 0.3s;" :key="inde">

					<el-input placeholder="请选择指标" :value="value.field_name?value.field_name:value.field_code"
						size="mini" style="width:150px;" @focus='focus(index,inde,value)'>
						<i slot="suffix" class="el-input__icon el-icon-arrow-down"></i>
					</el-input>


					<!-- <el-select v-model="value.field_code" filterable placeholder="请选择指标" size="mini" style="width: 150px;" @change="change(index,inde)">
						<el-option v-for="cont in Fielduser" :key="cont.id" :label="cont.fieldCn" :value="cont.fieldEn">
						</el-option>
					</el-select> -->

					<!-- {{value}} -->
					<ruleRelation v-model="value.operator" :value2.sync="value.value"
						:valueType="value.valueType" size="mini"></ruleRelation>



					<!-- <el-input v-model="value.value" maxlength="30" placeholder="值" style="width: 100px;margin-left: 10px;" v-show="getvalueType(value.field_code)!==3"
					 size="mini">
					</el-input>
					<el-select v-model="value.value" placeholder="请选择" size="mini" style="width: 100px;margin-left: 10px;" v-show="getvalueType(value.field_code)===3">
						<el-option label="是" value="="></el-option>
						<el-option label="否" value="!="></el-option>
					</el-select> -->
					<el-select v-model="value.relative_operator" placeholder="请选择"
						style="width: 100px;margin-left: 10px;" size="mini" v-show="inde!==item.formulas.length-1">
						<el-option label="and" value="&&"></el-option>
						<el-option label="or" value="or"></el-option>
					</el-select>
					<i class="el-icon-plus" style="font-size: 20px;color: #409EFF;" @click="sonAdd(index,inde)"></i>
					<i class="el-icon-close" style="font-size: 20px;color: #F56C6C;" v-show="inde!=0"
						@click="sondelect(index,inde)"></i>
				</div>
				<div class="type3_delect_fa">
					<i class="el-icon-close" style="font-size: 20px;color: #F56C6C;" @click="fadelect(index)"></i>
				</div>
			</div>
		</div>
		<div class="type3_submit_home" v-if="!readOnly">
			<el-button type="primary" round @click="submit" >提交</el-button>
		</div>
		<el-dialog title="请选择参数" :visible.sync="dialogType3" width="60%" :append-to-body="true">
			<div v-loading="selectLoading">
				<div class="type9_header">
					<el-select v-model="tempSelectType" placeholder="请选择类别" @change="SelectChange">
						<el-option v-for="(item,idx) in decisionOptions" :key="idx" :label="item.nodeTypeName"
							:value="item.nodeType"></el-option>
					</el-select> 
					<!-- 选择节点类型 -->

					<el-select v-model="SelectRadio" style="margin-left: 20px;" filterable placeholder="请选择"
						v-show="radioAll.length>0&&tempSelectType!=-2" @change="selectNodeInfo">
						<el-option v-for="item in radioAll" :key="item.nodeId" :label="item.nodeName"
							:value="item.nodeId">
						</el-option>
					</el-select>
					<!-- 选择节点 -->

					<el-select v-model="selRule" style="margin-left: 20px;" filterable placeholder="请选择"
						v-show="ruleList.length>0" @change="selRuleChange">
						<el-option v-for="(item,idx) in ruleList" :key="idx" 
							:label="item.name?item.name:item.listName?item.listName:item.fieldCn" :value="idx">
						</el-option>
					</el-select>
					<!-- 选择 策略-->
					
					
					<el-select v-model="selOutput" style="margin-left: 20px;" filterable placeholder="请选择1"
						v-show="outputList.length>0" >
						<el-option v-for="(item,idx) in outputList" :key="idx"
							:label="item.fieldCn?item.fieldCn:mixinGetCnByEn(item.fieldEn)" :value="item.fieldEn">
						</el-option>
					</el-select>
					<!-- {{outputList}} -->
					<!-- 选择策略的终止 -->


					<el-select v-model="selOutput" style="margin-left: 20px;" filterable placeholder="请选择字段"
						v-show="radioAll.length>0&&tempSelectType==-2">
						<el-option v-for="(item,idx) in radioAll" :key="idx"
							:label="item.fieldCn?item.fieldCn:mixinGetCnByEn(item.fieldEn)" :value="item.fieldEn">
						</el-option>
					</el-select>


					<div v-show="radioAll.length<1" style="color: #999;margin-top: 10px;">
						此选项下内容为空
					</div>
				</div>
			</div>

			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogType3 = false;changeingSelect='';initSelect()">取 消</el-button>
				<el-button type="primary" @click="dialogSure">确 定</el-button>
			</span>
		</el-dialog>
	</div>
</template>

<script>
	import {
		setType4,
		getConditionInputParam
	} from '@/api/index.js'
	import ruleRelation from '@/components/common/ruleRelation.vue'
	export default {
		components: {
			setType4,
			ruleRelation
		},
		data() {
			return {
				loading: false,
				datajson: [],
				dialogType3: false,
				selectLoading: false,
				radioAll: [],
				radioHome: [],
				tempSelectType: '',
				decisionOptions: [],
				selOutput: '',
				outputList: [],
				selRule: null,
				ruleList: [],
				SelectRadio: "",
				currentIndex: null,
				currentIdx: null
			}
		},
		created() {
			this.crea()
		},
		props: {
			data: {
				type: Object,
				default () {
					return {}
				}
			},
			nodeNexts: {
				type: Array,
				default () {
					return []
				}
			},
			readOnly:{
				type: Boolean,
				default :false
			}
		},
		methods: {

			change(index, inde) {
				this.datajson.conditions[index].formulas[inde].operator = ''
				this.datajson.conditions[index].formulas[inde].value = ''
			},
			submit() {



				 // 格式检查
					// 检查是否有重名
				let tempArr = this.datajson.conditions.map(value => value.group_name)
				if (tempArr.length != Array.from(new Set(tempArr)).length) {
					this.$message.error('不允许出现同名出口')
					return
				}
					// 检查是否有空
				let is = false
				this.datajson.conditions.forEach(value => {
					if (String(value.group_name).trim() != '') {
						value.formulas.forEach((item, index) => {
							if (item.field_cod != '' && item.operator != '' && item.value.trim() != '') {
								if (item.relative_operator || index == value.formulas.length - 1) {

								} else {
									// console.log(index, value.formulas.length - 1)
									is = true
								}
							} else {
								is = true
							}
						})
					} else {
						is = true
					}
				})
				if (is) {
					this.$message.error('请检查是否有空字段');
					return
				}
				// 检查是否分组数量不够
				if (this.datajson.conditions.length < 2) {
					this.$message.error('分组不能少于2');
					return
				}



				// 格式化数据
				let arr = []
				this.datajson.conditions.forEach(value => {
					value.formulas.forEach(item => {
						arr.push(item.field_code)
					})
				})
				arr = new Set(arr)
				arr = Array.from(arr)
				arr = arr.map(value => {
					return {
						fieldCode: this.mixinGetFieldByEn(value) ? this.mixinGetFieldByEn(value).fieldEn : "",
						fieldName: this.mixinGetFieldByEn(value) ? this.mixinGetFieldByEn(value).fieldCn : "",
						fieldId: this.mixinGetFieldByEn(value) ? this.mixinGetFieldByEn(value).id : "",
						valueType: this.mixinGetFieldByEn(value) ? this.mixinGetFieldByEn(value).valueType : "",
					}
				})
				this.datajson.fields = arr
				
				
				
				// 给 datajson 添加
				
				this.nodeNexts.forEach((value) => {
					this.datajson.conditions.forEach(item=>{
						if(value.proportion === item.group_name){
							item.nextNode = value.nextNode
						}
					})
				})
				console.log(this.datajson.conditions)
				
				this.loading = true
				this.setType({
					"id": this.data.id,
					"initEngineVersionId": String(this.data.Vid),
					"nodeType": 3,
					"nodeName": this.data.text,
					"nodeCode": this.data.nodeCode,
					"nodeOrder": this.data.nodeOrder,
					"nodeX": this.data.x,
					"nodeY": this.data.y,
					nodeJson: JSON.stringify(this.datajson),
					"snapshot":JSON.stringify(this.datajson),
					params: JSON.stringify({
						"dataId": 3,
						"url": "/Riskmanage/resource/images/decision/createUserGroup.png",
						"type": 3,
					})
				}).then(res => {
					if (res.status == "1") {
						
						let obj = this.datajson.conditions.map((value, index) => {
							this.data.nodeNexts.forEach(item => {
								if (value.group_name == item.proportion) {
									value.nextNode = item.nextNode
								}
							})
							return {
								proportion: value.group_name,
								sandbox: index + 1,
								nextNode: value.nextNode ? value.nextNode : ''
							}
						})
						
						// 设置 外层的 nextNodes
						this.$message({
							message: '提交成功',
							type: 'success'
						});

						this.$emit('setNextNodes', obj)
						this.$emit('callback', JSON.stringify(this.datajson))
					}
					this.loading = false
				})

			},
			faAdd() {
				this.datajson.conditions.push({
					group_name: '临时分组',
					nextNode: '',
					formulas: [{
						field_code: '',
						operator: '',
						value: '',
						relative_operator: '',

					}]
				})

			},
			fadelect(index) {
				if (this.data.nodeNexts[index] && this.data.nodeNexts[index].nextNode != "") {
					this.$message({
						message: '该分支有子节点，请清除后提交',
						type: 'warning'
					});
				} else {
					this.datajson.conditions.splice(index, 1)
				}

			},
			sonAdd(index, inde) {
				this.datajson.conditions[index].formulas.splice(inde + 1, 0, {
					field_code: '',
					operator: '',
					value: '',
					relative_operator: ''
				})
			},
			sondelect(index, inde) {
				this.datajson.conditions[index].formulas.splice(inde, 1)
			},
			crea() {
			
				this.getCondition();
				this.datajson = this.data.nodeJson ? JSON.parse(this.data.nodeJson) : {
					conditions: [{
						group_name: '分组1',
						nextNode: '',
						formulas: [{
							field_code: '',
							operator: '',
							value: '',
							relative_operator: ''
						}]
					}]
				}
			},
			getvalueType(cont) {
				let num
				this.Fielduser.forEach(value => {
					if (value.fieldEn === cont) {
						num = value.valueType
					}
				})
				return num
			},
			async setType(param) {
				const data = await setType4(param);
				return data
			},
			async getConditionParam(param) {
				const data = await getConditionInputParam(param)
				return data
			},
			getCondition() {
				this.selectLoading = true
				this.getConditionParam({
					"nodeId": this.data.id
				}).then((res) => {
					if (res.status == '1') {
						let {
							nodeTypeList
						} = res.data;
						this.decisionOptions = [{
								nodeType: -2,
								nodeTypeName: "字段",
								nodeInfoList: this.FieldUser
							},
							...nodeTypeList
						];
					}
					this.selectLoading = false
				})
			},
			focus(index, idx, value) {
				this.currentIndex = index;
				this.currentIdx = idx;

				this.dialogType3 = true
				this.tempSelectType = value.nodeType
				this.changeingSelect = index
				if (value.nodeType != undefined && this.decisionOptions.findIndex(x => x.nodeType == value.nodeType) == -
					1) {
					this.tempSelectType = -2;
				}
				if (value.nodeType) {
					let {
						nodeInfoList
					} = this.decisionOptions.find(x => x.nodeType == this.tempSelectType);
					this.radioAll = nodeInfoList;
					if (this.tempSelectType != -2) {
						this.selectNodeInfo(value.nodeId)
					}

					this.SelectRadio = value.nodeId;
					this.selOutput = value.field_code;
					if (value.strategyId) {
						this.selRule = this.ruleList.findIndex(x => x.id == value.strategyId)
						const {
							ruleOutputList,
							listDbOutputList,
							fieldEn
						} = this.ruleList[this.selRule];

						if (ruleOutputList) {
							this.selRuleChange(this.selRule)
						} else if (listDbOutputList) {
							this.selRuleChange(this.selRule)
						}
						this.selOutput = value.field_code
					}

				}
			},
			selRuleChange(e) {
				const currentRule = this.ruleList[e];
				const {
					ruleOutputList,
					listDbOutputList,
					fieldEn
				} = currentRule;
				this.selOutput = "";
				if (ruleOutputList) {
					this.outputList = ruleOutputList;
				} else if (listDbOutputList) {
					this.outputList = listDbOutputList;
				} else if (fieldEn) {
					this.outputList = [];
					this.selOutput = fieldEn;
				}
			},
			selectNodeInfo(e) {
				const {
					strategyOutputList,
					ruleOutput,
					listDbOutput
				} = this.radioAll.find(x => x.nodeId == e);
				this.outputList = strategyOutputList;
				this.selRule = null;
				this.selOutput = "";
				if (strategyOutputList == null && listDbOutput == null) {
					let newArr = ruleOutput.statisticsOutputList.map(v => {
						return {
							...v,
							name: v.fieldCn,
							id: v.fieldEn
						}
					})
					this.ruleList = [...newArr, ...ruleOutput.ruleInfoList];
					this.outputList = [];
				} else if (strategyOutputList == null && ruleOutput == null) {
					let newArr = listDbOutput.statisticsOutputList.map(v => {
						return {
							...v,
							listName: v.fieldCn,
							id: v.fieldEn
						}
					})
					this.ruleList = [...newArr, ...listDbOutput.listDbInfoOutput];
					this.outputList = [];
				} else {
					this.ruleList = [];
				}
			},
			SelectChange(e) {
				let {
					nodeInfoList
				} = this.decisionOptions.find(x => x.nodeType == this.tempSelectType);
				this.radioAll = nodeInfoList;
				this.SelectRadio = "";
				this.selRule = null;
				this.selOutput = "";
				this.ruleList = [];
				this.outputList = [];
			},
			initSelect() {
				this.SelectRadio = "";
				this.selRule = null;
				this.selOutput = "";
				this.radioAll = [];
				this.ruleList = [];
				this.outputList = [];
			},
			dialogSure() {
				if (this.selOutput) {
					let field = {};
					let currentNode = this.decisionOptions.find(x => x.nodeType == this.tempSelectType);
					let {
						nodeInfoList
					} = currentNode
					field.asName = currentNode.nodeTypeName;
					if (this.tempSelectType != -2) {
						const currentNodeInfo = nodeInfoList.find(x => x.nodeId == this.SelectRadio);
						let {
							strategyOutputList
						} = currentNodeInfo;
						field.asName += '/' + currentNodeInfo.nodeName;
						console.log(this.radioAll)
						if (this.ruleList.length > 0 && strategyOutputList == null) {
							const currentRule = this.ruleList[this.selRule];
							let {
								ruleOutputList,
								listDbOutputList,
								fieldEn,
								fieldCn,
								valueType
							} = currentRule;
							
							if (ruleOutputList) {
								field.fieldCn = ruleOutputList.find(x => x.fieldEn == this.selOutput).fieldCn;
								field.valueType = ruleOutputList.find(x => x.fieldEn == this.selOutput).valueType
								field.fieldEn = this.selOutput;
								field.asName += '/' + this.ruleList[this.selRule].name;
								field.strategyId = currentRule.id ? currentRule.id : '';
							} else if (listDbOutputList) {
								field.fieldCn = listDbOutputList.find(x => x.fieldEn == this.selOutput).fieldCn;
								field.valueType = listDbOutputList.find(x => x.fieldEn == this.selOutput).valueType
								field.fieldEn = this.selOutput;
								field.asName += '/' + this.ruleList[this.selRule].listName;
								field.strategyId = currentRule.id ? currentRule.id : '';
							} else {
								field.fieldEn = fieldEn;
								field.fieldCn = fieldCn;
								field.strategyId = fieldEn;
								field.valueType = valueType;
							}
							
						} else {
							
							let {
								fieldCn,
								fieldEn,
								valueType
							} = strategyOutputList.find(x => x.fieldEn == this.selOutput);
							field.fieldEn = fieldEn;
							field.fieldCn = fieldCn;
							field.valueType = valueType;
							
						}
					} else {
						let {
							fieldCn,
							fieldEn,
							valueType
						} = nodeInfoList.find(x => x.fieldEn == this.selOutput);
						console.log(nodeInfoList.find(x => x.fieldEn == this.selOutput))
						field.fieldEn = fieldEn;
						field.fieldCn = fieldCn;
						field.valueType = valueType;
					}
					const currentField = this.Fielduser.find(x => x.fieldEn == field.fieldEn);
					field.nodeId = this.SelectRadio;
					field.nodeType = this.tempSelectType;
					field.field_id = currentField ? currentField.id : '';
					// field.valueType = currentField ? currentField.valueType : '';

					this.datajson.conditions[this.currentIndex].formulas[this.currentIdx].operator = '';
					this.datajson.conditions[this.currentIndex].formulas[this.currentIdx].value = '';
					this.datajson.conditions[this.currentIndex].formulas[this.currentIdx].field_code = field.fieldEn;
					this.datajson.conditions[this.currentIndex].formulas[this.currentIdx].strategyId = field.strategyId ? 
						field.strategyId : "";
					this.datajson.conditions[this.currentIndex].formulas[this.currentIdx].nodeType = this.tempSelectType
					this.datajson.conditions[this.currentIndex].formulas[this.currentIdx].nodeId = this.SelectRadio
					this.datajson.conditions[this.currentIndex].formulas[this.currentIdx].field_name = field.fieldCn
					this.datajson.conditions[this.currentIndex].formulas[this.currentIdx].valueType = field.valueType
					
					
					this.initSelect();
					this.changeingSelect = ''
					this.dialogType3 = false
				} else {
					this.$message({
						message: '请选择一个选项再确定',
						type: 'warning'
					});
				}
			}
		},
		computed: {
			Fielduser() {
				if (this.$store.state.FieldUser != null) {
					this.loading = false
					return this.$store.state.FieldUser.data.fieldList
				} else {
					return []
				}
			}
		},
		watch: {
			data() {

				this.crea()

			}
			// nodeNexts: {
			// 	handler: function() {
			// 		// this.datajson.conditions=this.nodeNexts
			// 		console.log(this.datajson.conditions)
			// 	},
			// 	deep: true
			// }

		}



	}
</script>

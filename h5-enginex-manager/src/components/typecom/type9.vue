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

	.type3_rule_home {
		/* height: 80%; */
	}

	/* .type3_submit_home {
		display: flex;
		align-items: center;
		justify-content: center;
		position: absolute;
		left: 80%;
		margin-top: 30px;
		
		bottom: 20px;
		z-index: 999;
	} */
</style>

<template>
	<div class="type4 type" v-loading="loading" style="width: 550px;height: 100%;">

		<div class="type_header">
			<p>节点类型:决策选项</p>
		</div>

		<div
			style="background-color: #aaa; height: 30px;line-height: 30px; color: #fff;padding: 10px; box-sizing: border-box; margin: 10px 0;display: flex;align-items: center;justify-content: space-between;">
			<p>输入变量</p>
			<p style="display: flex;align-items: center;">
				<i class="el-icon-plus" @click="addVariable"
					style="font-size: 20px;color: #409EFF;padding: 2px;border-radius: 50px;border: 1px solid #409EFF; background-color:#cfe6fd ;"></i>
			</p>
		</div>
		<div>

			<div v-for="(value,index) in input" style="display: flex;align-items:center;margin-top: 5px;" :key="index">
				<!-- <el-select :value="value.field_name" filterable placeholder="请选择入参" size="mini" style="width: 150px;" @focus='focus(index,value)'> -->
				<!-- <el-option v-for="cont in Fielduser" :key="cont.id" :label="cont.fieldCn" :value="cont.fieldEn">
						</el-option> -->
				<!-- </el-select> -->

				<el-input placeholder="请选择入参" v-model="value.field_name" size="mini" style="width:150px;"
					@focus='focus(index,value)'>
					<i slot="suffix" class="el-input__icon el-icon-arrow-down"></i>
				</el-input>

				<p style="margin-left: 10px;">别名：</p>
				<el-input size="mini" v-model="value.asName" :placeholder="value.field_name" style="width: 200px;">
				</el-input>
				<i class="el-icon-close" style="font-size: 20px;color: #F56C6C;" @click="delectVariable(index)"></i>
			</div>
		</div>


		<div
			style="background-color: #aaa; height: 30px;line-height: 30px; color: #fff;padding: 10px; box-sizing: border-box; margin: 10px 0;display: flex;align-items: center;justify-content: space-between;">
			<p>决策条件</p>

			<p style="display: flex;align-items: center;">
				<i class="el-icon-plus" @click="faAdd"
					style="font-size: 20px;color: #409EFF;padding: 2px;border-radius: 50px;border: 1px solid #409EFF; background-color:#cfe6fd ;"></i>
			</p>
		</div>
		<div class="type3_rule_home">
			<!-- <span>{{input}}</span> -->

			<div v-for="(item,index) in datajson.conditions" class="type3_fa" :key="index">

				<p>决策条件:</p>
				<div v-for="(value,inde) in item.formula"
					style="display: flex; margin-bottom: 5px;align-items: center;transition: all 0.3s;" :key="inde">
					<!-- {{value}} -->
					<el-select v-model="value.field_code" filterable placeholder="请选择入参" size="mini"
						style="width: 150px;" @change="getname(value)">
						<el-option v-for="cont in input" :label="cont.asName" :value="cont.field_code"
							:key="cont.fieldEn">
						</el-option>
					</el-select>

					<!-- <span>{{mixinGetValueTypeByEn(value.field_code)}}</span> -->
					<ruleRelation v-model="value.operator" :value2.sync="value.result"
						:valueType="mixinGetValueTypeByEn(value.field_code)" size="mini"></ruleRelation>
					<!-- <el-input v-model="value.result" maxlength="30" placeholder="值" style="width: 100px;margin-left: 10px;" v-show="value.type!==3"
					 size="mini">
					</el-input>
					<el-select v-model="value.result" placeholder="请选择" size="mini" style="width: 100px;margin-left: 10px;" v-show="value.type===3">
						<el-option label="是" value="=="></el-option>
						<el-option label="否" value="!="></el-option>
					</el-select> -->
					<el-select v-model="value.sign" placeholder="请选择" style="width: 100px;margin-left: 10px;"
						size="mini" v-show="inde!==item.formula.length-1">
						<el-option label="and" value="&&"></el-option>
						<el-option label="or" value="||"></el-option>
					</el-select>
					<i class="el-icon-plus" style="font-size: 20px;color: #409EFF;" @click="sonAdd(index,inde)"></i>
					<i class="el-icon-close" style="font-size: 20px;color: #F56C6C;" v-show="inde!=0"
						@click="sondelect(index,inde)"></i>
				</div>
				决策结果: <el-input v-model="item.result" placeholder="请输入结果" size="mini"
					style="width: 200px;margin-top: 10px;"></el-input>
				<div class="type3_delect_fa">
					<i class="el-icon-close" style="font-size: 20px;color: #F56C6C;" @click="fadelect(index)"></i>
				</div>
			</div>
			<div class="type3_fa">
				默认结果: <el-input v-model="datajson.output.defaultValue" placeholder="请输入结果" size="mini"
					style="width: 200px;margin-top: 10px;"></el-input>
			</div>
		</div>

		<div
			style="background-color: #aaa; height: 30px;line-height: 30px; color: #fff;padding: 10px; box-sizing: border-box; margin: 10px 0;display: flex;align-items: center;justify-content: space-between;">
			<p>输出变量</p>
		</div>
		<div>
			<el-select v-model="result" filterable placeholder="请选择结果指标" size="mini" style="width: 150px;">
				<el-option v-for="cont in Fielduser" :key="cont.id" :label="cont.fieldCn" :value="cont.id">
				</el-option>
			</el-select>
		</div>

		<div class="type3_submit_home" v-if="!readOnly">
			<el-button type="primary" round @click="submit">提交</el-button>
		</div>
		<el-dialog title="请选择参数" :visible.sync="dialogTyep9" width="60%" :append-to-body="true">
			<div v-loading="selectLoading">
				<div class="type9_header">
					<el-select v-model="tempSelectType" placeholder="请选择类别" @change="SelectChange">
						<el-option v-for="(item,idx) in decisionOptions" :key="idx" :label="item.nodeTypeName"
							:value="item.nodeType"></el-option>
						<!-- <el-option label="评分卡" :value="1"></el-option>
						<el-option label="字段" :value="2"></el-option>
						<el-option label="规则集" :value="3"></el-option>
						<el-option label="决策选项" :value="5"></el-option>
						<el-option label="子引擎" :value="6"></el-option>
						<el-option label="模型" :value="7"></el-option>
						<el-option label="决策表" :value="16"></el-option>
						<el-option label="决策树" :value="17" disabled></el-option> -->
					</el-select>

					<el-select v-model="SelectRadio" style="margin-left: 20px;" filterable placeholder="请选择"
						v-show="radioAll.length>0&&tempSelectType!=-2" @change="selectNodeInfo">
						<!-- <el-option v-for="item in fradioAll" :key="item.id" :label="item.name" :value="item.id">
						</el-option> -->
						<el-option v-for="item in radioAll" :key="item.nodeId" :label="item.nodeName"
							:value="item.nodeId">
						</el-option>
					</el-select>

					<el-select v-model="selRule" style="margin-left: 20px;" filterable placeholder="请选择"
						v-show="ruleList.length>0" @change="selRuleChange">
						<el-option v-for="(item,idx) in ruleList" :key="idx"
							:label="item.name?item.name:item.listName?item.listName:item.fieldCn" :value="idx">
						</el-option>
					</el-select>

					<el-select v-model="selOutput" style="margin-left: 20px;" filterable placeholder="请选择"
						v-show="outputList.length>0">
						<el-option v-for="(item,idx) in outputList" :key="idx"
							:label="item.fieldCn?item.fieldCn:getFieldCn(item.fieldEn)" :value="item.fieldEn">
						</el-option>
					</el-select>

					<el-select v-model="selOutput" style="margin-left: 20px;" filterable placeholder="请选择字段"
						v-show="radioAll.length>0&&tempSelectType==-2">
						<el-option v-for="(item,idx) in radioAll" :key="idx"
							:label="item.fieldCn?item.fieldCn:getFieldCn(item.fieldEn)" :value="item.fieldEn">
						</el-option>
					</el-select>

					<!-- <el-select v-model="SelectRadio" style="margin-left: 20px;" filterable placeholder="请选择" v-show="radioAll.length>0">
						<el-option v-for="item in fradioAll" :key="item.id" :label="item.name" :value="item.id">
						</el-option>
					</el-select> -->
					<div v-show="radioAll.length<1" style="color: #999;margin-top: 10px;">
						此选项下内容为空
					</div>
				</div>
			</div>
			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogTyep9 = false;changeingSelect='';initSelect()">取 消</el-button>
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
				input: [{
					field_name: '',
					asName: '',
					field_code: '',
					field_id: '',
					valyeType: '',
					fieldCn: '',
					fieldEn: ''
				}],
				search: '',
				changeingSelect: '',
				SelectRadio: '',
				selectLoading: false,
				radioAll: [],
				radioHome: [],
				tempSelectType: '',
				dialogTyep9: false,
				result: '',
				loading: false,
				datajson: [],
				decisionOptions: [],
				selOutput: '',
				outputList: [],
				selRule: null,
				ruleList: []
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
			readOnly: {
				type: Boolean,
				default: false
			}
		},
		methods: {
			getFieldCn(fieldEn) {
				const fie = this.Fielduser.find(x => x.fieldEn == fieldEn);
				return fie ? fie.fieldCn : ''
			},
			getname(value) {
				this.input.forEach(item => {
					if (item.field_code == value.field_code) {
						value.name = item.field_name
						value.valueType = item.valueType ? item.valueType : 1
					}
				})
			},
			delectVariable(index) {
				this.input.splice(index, 1)
			},
			addVariable() {
				console.log(this.input)
				this.input.push({
					field_name: '',
					asName: '',
					field_code: '',
					field_id: '',
					valueType: '',
				})
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
				// if (this.radioHome[this.tempSelectType]) {
				// 	this.radioAll = this.radioHome[this.tempSelectType]
				// }else{
				this.SelectRadio = "";
				this.selRule = null;
				this.selOutput = "";
				this.ruleList = [];
				this.outputList = [];
				// if(e==-2){
				// 	this.radioAll = 
				// }
				// }
				//  else {
				// 	this.selectLoading = true
				// 	this.getType({
				// 		"pageNo": 1,
				// 		pageSize: 999,
				// 		"opType": this.tempSelectType,
				// 		"nodeId": this.data.id,
				// 		"isOutput": 0,
				// 	}).then(res => {
				// 		if (res.status == "1") {
				// 			this.radioAll = res.data.list
				// 			this.radioHome[this.tempSelectType] = res.data.list
				// 		}
				// 		this.selectLoading = false
				// 	}).catch(err => this.selectLoading = false)
				// }

			},
			focus(index, value) {
				console.log(index, value);
				this.dialogTyep9 = true
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

				// this.SelectChange()
				// this.changeingSelect.push(inde)
				// if(value.field_id==""){
				// 	this.initSelect();
				// }
			},
			valueCode(id) {
				let str
				this.radioHome.forEach(value => {
					if (!value) {
						return
					}
					value.forEach(item => {
						if (id == item.id) {
							str = item.code
						}
					})
				})
				return str
			},
			valueNodeId(id) {
				let str
				this.radioHome.forEach(value => {
					if (!value) {
						return
					}
					value.forEach(item => {
						if (id == item.id) {
							str = item.nodeId
						}
					})
				})
				return str
			},
			valueNodeType(id) {
				let str
				this.radioHome.forEach((value, index) => {
					if (!value) {
						return
					}
					value.forEach(item => {
						if (id == item.id) {
							str = index
						}
					})
				})
				return str
			},
			submit() {
				let asNameIsSp = false
				this.input.forEach(value => {
					console.log(value)
					if (String(value.asName).trim() === "") {
						asNameIsSp = true
					}
				})
				if (asNameIsSp) {
					this.$message.error('别名不能为空')
					return
				}

				let z = /\{.+?\}/g
				let isasName = false
				this.datajson.conditions.forEach(value => {
					if (String(value.result).match(z)) {
						String(value.result).match(z).forEach(item => {
							let is = true
							this.input.forEach(cont => {
								if (cont.asName == item.substr(1, item.length - 2)) {
									is = false
								}
							})
							if (is) {
								isasName = true
							}
						})
					}
				})
				if (isasName) {
					this.$message.error('请检查模板文字与别名是否匹配')
					return
				}



				let is = true
				this.datajson.conditions.forEach(value => {
					if (String(value.result).trim() !== "") {
						value.formula.forEach((item, index) => {
							if (item.name && item.operator && item.result.trim()) {
								if (item.sign || index == value.formula.length - 1) {} else {
									console.log(index, value.formula.length - 1)

									is = false
								}
							} else {

								is = false
							}
						})
					} else {

						is = false
					}
				})

				// if(String(this.datajson.output.defaultValue).trim()===""){
				// 	is = false
				// }
				if (!this.result) {
					is = false
				}

				if (is) {
					this.datajson.conditions.forEach(value => {
						value.resultKey = value.result
						value.formula.forEach(item => {
							const field = this.FieldUser.find(x => x.id == item.id);
							const node = this.datajson.input.find(x => x.field_id == item.id);
							console.log(field);
							item.resultKey = item.result
							// item.field_code = field?field.fieldEn?field.fieldEn:"":"";

							item.nodeId = node ? node.nodeId ? node.nodeId : "" : "";
							item.nodeType = node ? node.nodeType ? node.nodeType : "" : "";
						})
					})

					let out
					this.Fielduser.forEach(value => {
						if (value.id == this.result) {
							out = value
						}
					})

					let obj = {
						condition_type: 1,
						output: {
							"field_id": out.id,
							"field_code": out.fieldEn,
							"field_name": out.fieldCn,
							"valueType": out.valueType,
							defaultValue: this.datajson.output.defaultValue
						},
						conditions: this.datajson.conditions,
						input: this.input
					}
					this.loading = true
					this.setType({
						"id": this.data.id,
						"initEngineVersionId": String(this.data.Vid),
						"nodeType": 9,
						"nodeName": this.data.text,
						"nodeCode": this.data.nodeCode,
						"nodeOrder": this.data.nodeOrder,
						"nodeX": this.data.x,
						"nodeY": this.data.y,
						nodeJson: JSON.stringify(obj),
						"snapshot": JSON.stringify(obj),
						params: JSON.stringify({
							"dataId": "7",
							"url": "/Riskmanage/resource/images/decision/createDcisionOption.png",
							"type": "9"
						})
					}).then(res => {

						if (res.status == "1") {
							this.$message({
								message: '提交成功',
								type: 'success'
							});

							this.$emit('callback', JSON.stringify(obj))

						}
						this.loading = false
					})
				} else {
					this.$message.error('请检查是否有空字段');
				}
			},
			faAdd() {
				this.datajson.conditions.push({
					"result": 0,
					"resultKey": 0,
					"formula": [{
						"field_code": "",
						"operator": "",
						"result": "",
						"resultKey": "",
						sign: ''
					}]
				})
			},
			fadelect(index) {
				this.datajson.conditions.splice(index, 1)
			},
			sonAdd(index, inde) {
				this.datajson.conditions[index].formula.splice(inde + 1, 0, {
					"field_code": "",
					"operator": "",
					"result": "",
					"resultKey": "",
					sign: ''
				})
			},
			sondelect(index, inde) {
				this.datajson.conditions[index].formula.splice(inde, 1)
			},
			initSelect() {
				this.SelectRadio = "";
				this.selRule = null;
				this.selOutput = "";
				this.radioAll = [];
				this.ruleList = [];
				this.outputList = [];
			},
			// OffsetSign(obj) {
			// 	obj.conditions.forEach(value => {
			// 		value.formula.forEach((item, index) => {

			// 			if (index != value.formula.length - 1) {
			// 				item.sign = value.formula[index + 1].sign
			// 			} else {
			// 				item.sign = ""
			// 			}
			// 		})
			// 	})
			// 	return obj
			// },
			crea() {
				
				this.selectLoading = true
				// 获取决策流条件入参
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
						// this.radioHome[value] = res.data.list
					}
					this.selectLoading = false
				})

				// let arr = [1, 2, 3, 5, 6, 7, 16, 17]
				// arr.forEach(value => {
				// 	this.selectLoading = true
				// 	this.getType({
				// 		"pageNo": 1,
				// 		pageSize: 999,
				// 		"opType": value,
				// 		"nodeId": this.data.id,
				// 		"isOutput": 0,
				// 	}).then(res => {
				// 		if (res.status == "1") {
				// 			this.radioHome[value] = res.data.list
				// 		}
				// 		this.selectLoading = false
				// 	})
				// }) //提前请求 

				this.datajson = this.data.nodeJson ? JSON.parse(this.data.nodeJson) : {
						"conditions": [{
								"result": "",
								"resultKey": "",
								"formula": [{
									"field_code": "",
									"name": '',
									"type": '',
									"operator": "",
									"result": "",
									"resultKey": "",
									"sign": ''
								}]
							}

						],
						"output": {
							"field_id": '',
							"field_code": "",
							"field_name": "",
							"valueType": '',
							"field_scope": "",
							defaultValue: '',
						},
						"input": []
					},
					this.result = this.datajson.output.field_id
				this.input = this.datajson.input
				console.log(this.datajson)

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
			// dialogSure() {
			// 	console.log(this.SelectRadio)
			// 	if (this.SelectRadio) {
			// 		let obj = {}
			// 		this.radioHome.forEach((value,index)=>{
			// 			if(value){
			// 				value.forEach(item=>{
			// 					if(item.id==this.SelectRadio){
			// 						obj = item
			// 						obj.nodeType = index
			// 					}
			// 				})
			// 			}

			// 		})

			// 		this.input[this.changeingSelect].field_name = obj.name
			// 		this.input[this.changeingSelect].field_code =obj.code
			// 		this.input[this.changeingSelect].field_id =obj.id
			// 		this.input[this.changeingSelect].valueType =obj.type
			// 		this.input[this.changeingSelect].nodeType =obj.nodeType
			// 		this.input[this.changeingSelect].asName = this.input[this.changeingSelect].field_name
			// 		this.SelectRadio = null
			// 		this.changeingSelect = ''
			// 		this.dialogTyep9 = false
			// 	} else {
			// 		this.$message({
			// 			message: '请选择一个选项再确定',
			// 			type: 'warning'
			// 		});
			// 	}
			// },
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
								field.fieldEn = this.selOutput;
								field.asName += '/' + this.ruleList[this.selRule].name;
								field.strategyId = currentRule.id ? currentRule.id : '';
								field.valueType = ruleOutputList.find(x => x.fieldEn == this.selOutput).valueType
							} else if (listDbOutputList) {
								field.fieldCn = listDbOutputList.find(x => x.fieldEn == this.selOutput).fieldCn;
								field.fieldEn = this.selOutput;
								field.asName += '/' + this.ruleList[this.selRule].listName;
								field.strategyId = currentRule.id ? currentRule.id : '';
								field.valueType = listDbOutputList.find(x => x.fieldEn == this.selOutput).valueType
							} else {
								field.fieldEn = fieldEn;
								field.fieldCn = fieldCn;
								field.strategyId = fieldEn;
								field.valueType = valueType
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
						field.fieldEn = fieldEn;
						field.fieldCn = fieldCn;
						field.valueType = valueType;
					}
					// this.radioHome.forEach((value,index)=>{
					// 	if(value){
					// 		value.forEach(item=>{
					// 			if(item.id==this.SelectRadio){
					// 				obj = item
					// 				obj.nodeType = index
					// 			}
					// 		})
					// 	}

					// })
					const currentField = this.Fielduser.find(x => x.fieldEn == field.fieldEn);
					// this.input[this.changeingSelect].valueType = currentField ? currentField.valueType : ''
					this.input[this.changeingSelect].field_name = field.fieldCn
					this.input[this.changeingSelect].field_code = field.fieldEn
					this.input[this.changeingSelect].field_id = currentField ? currentField.id : ''
					this.input[this.changeingSelect].strategyId = field.strategyId ? field.strategyId : "";
					this.input[this.changeingSelect].nodeType = this.tempSelectType
					this.input[this.changeingSelect].nodeId = this.SelectRadio
					this.input[this.changeingSelect].asName = field.asName + '/' + field.fieldCn;
					this.input[this.changeingSelect].valueType = field.valueType
					
					console.log(this.input[this.changeingSelect])
					
					// this.SelectRadio = null
					this.initSelect();
					this.changeingSelect = ''
					this.dialogTyep9 = false
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
			},
			fradioAll() {

				let arr = this.radioAll.filter(value => {

					return String(value.name).indexOf(String(this.search)) != -1
				})

				return arr
			}
		},
		watch: {
			data() {
				this.crea()
			}
		}



	}
</script>

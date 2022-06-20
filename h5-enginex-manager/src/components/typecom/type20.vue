<style>
	.type {
        width: 500px;
		border-top: 1px solid #ddd;
		padding: 5px;
		box-sizing: border-box;
	}

	.type_header {
        /* width: 300px; */
		font-size: 12px;
		font-weight: bold;
	}
    .type18_summber_home{
		display: flex;
		align-items: center;
		justify-content: center;
		position: absolute;
		left: 80%;
		margin-top: 30px;
		top: 74.5vh;
		z-index: 999;
	}
    
</style>

<template>
	<div class="type" v-loading="loading">
		<div class="type_header">
			<p>节点类型:聚合</p>
		</div>		
        <!-- <div style="background-color: #aaa; height: 30px;line-height: 30px; color: #fff;padding: 10px; box-sizing: border-box; margin: 10px 0;display: flex;align-items: center;justify-content: space-between;">
			<p>输入变量</p>
			<p style="display: flex;align-items: center;">
				<i class="el-icon-plus" @click="addVariable" style="font-size: 20px;color: #409EFF;padding: 2px;border-radius: 50px;border: 1px solid #409EFF; background-color:#cfe6fd ;"></i>
			</p>
		</div>
		<div>
			<div v-for="(value,index) in input" style="display: flex;align-items:center;margin-top: 5px;" :key="index">
				<el-input
					placeholder="请选择入参"
					v-model="value.fieldName"
					size="mini"
					style="width:150px;"
					@focus='focus(index,value)'>
					<i slot="suffix" class="el-input__icon el-icon-arrow-down"></i>
				</el-input>
				<p style="margin-left: 10px;">别名：</p>
				<el-input size="mini" v-model="value.asName" :placeholder="value.fieldName" style="width: 200px;"></el-input>
				<i class="el-icon-close" style="font-size: 20px;color: #F56C6C;" @click="delectVariable(index)"></i>
			</div>
		</div>
        <div class="type3_rule_home" v-if="input.length>0">
		
            <div style="background-color: #aaa; height: 30px;line-height: 30px; color: #fff;padding: 10px; box-sizing: border-box; margin: 10px 0;display: flex;align-items: center;justify-content: space-between;">
				<p>终止条件</p>
			</div>		
			<div v-for="(condition,index) in nodeJson.terminationInfo.conditions" :key="index" class="conditions-wrapper" style="display: flex;">
				<el-select v-model="condition.fieldCode" placeholder="条数/分数" size="mini" style="width: 110px;">
                    <el-option v-for="cont in input" :label="cont.asName" :value="cont.fieldCode" :key="cont.fieldEn">
					</el-option>
				</el-select>
				<ruleRelation v-model="condition.operator" :value2.sync="condition.value" :valueType="mixinGetValueTypeByEn(condition.fieldCode)" size="mini" style="width:200px;"></ruleRelation>

				<el-select v-model="condition.relativeOperator" placeholder="请选择" style="width: 70px;margin-left: 10px;" size="mini" v-if="index!=nodeJson.terminationInfo.conditions.length-1">
					<el-option label="and" value="&&"></el-option>
					<el-option label="or" value="||"></el-option>
				</el-select>
				<i class="el-icon-plus" style="font-size: 20px;color: #409EFF;" @click="addCondition(index)"></i>
				<i class="el-icon-close" style="font-size: 20px;color: #F56C6C;" v-show="index!=0" @click="delCondtion(index)"></i>
			</div>
            <div>
				<div style="background-color: #aaa; height: 30px;line-height: 30px; color: #fff;padding: 10px; box-sizing: border-box; margin: 10px 0;display: flex;align-items: center;justify-content: space-between;">
					<p>终止结果</p>
				</div>
				<div class="setting-wrapper">
					<el-select v-model="nodeJson.terminationInfo.output.fieldId" placeholder="输出变量" size="mini" style="width:130px;">
						<el-option v-for="(item,index) in Fielduser" :key="index" :label="item.fieldCn" :value="item.id">
						</el-option>
					</el-select> 
					<div class="line">=</div>
					<varialeSelect v-model="nodeJson.terminationInfo.output.fieldValue" :obj="nodeJson.terminationInfo.output" height="28px"></varialeSelect>
				</div>
			</div>	
		</div>
        <div class="type18_summber_home">
			<el-button type="primary" round @click="submit">提交</el-button>
		</div>
        <el-dialog title="请选择参数" :visible.sync="dialogTyep20" width="60%" :append-to-body="true">
			<div v-loading="selectLoading">
				<div class="type9_header">
					<el-select v-model="tempSelectType" placeholder="请选择类别" @change="SelectChange">
						<el-option v-for="(item,idx) in decisionOptions" :key="idx" :label="item.nodeTypeName" :value="item.nodeType"></el-option>
					</el-select>
				
					<el-select v-model="SelectRadio" style="margin-left: 20px;" filterable placeholder="请选择" v-show="radioAll.length>0&&tempSelectType!=-2" @change="selectNodeInfo">
						<el-option v-for="item in radioAll" :key="item.nodeId" :label="item.nodeName" :value="item.nodeId">
						</el-option>
					</el-select>

					<el-select v-model="selRule" style="margin-left: 20px;" filterable placeholder="请选择" v-show="ruleList.length>0" @change="selRuleChange">
						<el-option v-for="(item,idx) in ruleList" :key="idx" :label="item.name?item.name:item.listName?item.listName:item.fieldCn" :value="idx">
						</el-option>
					</el-select>

					<el-select v-model="selOutput" style="margin-left: 20px;" filterable placeholder="请选择" v-show="outputList.length>0">
						<el-option v-for="(item,idx) in outputList" :key="idx" :label="item.fieldCn?item.fieldCn:getFieldCn(item.fieldEn)" :value="item.fieldEn">
						</el-option>
					</el-select>

					<el-select v-model="selOutput" style="margin-left: 20px;" filterable placeholder="请选择字段" v-show="radioAll.length>0&&tempSelectType==-2">
						<el-option v-for="(item,idx) in radioAll" :key="idx" :label="item.fieldCn?item.fieldCn:getFieldCn(item.fieldEn)" :value="item.fieldEn">
						</el-option>
					</el-select>
					<div v-show="radioAll.length<1" style="color: #999;margin-top: 10px;">
						此选项下内容为空
					</div>
				</div>
			</div>
			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogTyep20 = false;changeingSelect='';initSelect()">取 消</el-button>
				<el-button type="primary" @click="dialogSure">确 定</el-button>
			</span>
		</el-dialog> -->
	</div>
</template>

<script>
	import {getConditionInputParam , setType4} from '@/api/index.js'
    import ruleRelation from '@/components/common/ruleRelation.vue'
	import varialeSelect from '@/components/models/varialeSelect.vue'
	export default {
        components: {
			ruleRelation,
			varialeSelect
		},
		data() {
			return {
				loading: false,
                nodeJson:[],
                dialogTyep20:false,
                selectLoading: false,
                changeingSelect: '',
                input: [],   
                SelectRadio: '',  
                decisionOptions:[],
                radioAll: [],
                tempSelectType: '',
                selOutput:'',
				outputList:[],
				selRule:null,
				ruleList:[]           
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
			readOnly:{
				type: Boolean,
				default :false
			}
		},
        computed:{
            Fielduser() {
				if (this.$store.state.FieldUser != null) {
					this.loading = false
					return this.$store.state.FieldUser.data.fieldList
				} else {
					return []
				}
			}
        },
		methods: {
            delCondtion(index){
				this.nodeJson.terminationInfo.conditions.splice(index, 1); 
			},
			addCondition(index){
				this.nodeJson.terminationInfo.conditions.splice(index + 1, 0, {
					"fieldCode": "",
					"fieldName": "",
					"valueType": null,
					"operator": "",
					"value": "",
					"relativeOperator": ""
				})
			},
            getFieldCn(fieldEn){
				const fie = this.Fielduser.find(x=>x.fieldEn==fieldEn);
				return fie?fie.fieldCn:''
			},
			getname(value) {
				this.input.forEach(item => {
					if (item.field_code == value.field_code) {
						value.name = item.field_name
						value.valueType = item.valueType?item.valueType:1
					}
				})
			},
            dialogSure(){
				if (this.selOutput) {
					let field = {};
					let currentNode = this.decisionOptions.find(x=>x.nodeType==this.tempSelectType);
					let {nodeInfoList} = currentNode
					field.asName=currentNode.nodeTypeName;
					if(this.tempSelectType!=-2){	
						const currentNodeInfo = nodeInfoList.find(x=>x.nodeId==this.SelectRadio);
						let {strategyOutputList} = currentNodeInfo;
						field.asName += '/'+currentNodeInfo.nodeName;
						if(this.ruleList.length>0&&strategyOutputList==null){
							const currentRule = this.ruleList[this.selRule];
							let {ruleOutputList,listDbOutputList,fieldEn,fieldCn} = currentRule;
							if(ruleOutputList){							
								field.fieldCn=ruleOutputList.find(x=>x.fieldEn==this.selOutput).fieldCn;
								field.fieldEn = this.selOutput;
								field.asName += '/'+this.ruleList[this.selRule].name;
								field.strategyId=currentRule.id?currentRule.id:'';
							}else if(listDbOutputList){
								field.fieldCn=listDbOutputList.find(x=>x.fieldEn==this.selOutput).fieldCn;
								field.fieldEn = this.selOutput;
								field.asName += '/'+this.ruleList[this.selRule].listName;
								field.strategyId=currentRule.id?currentRule.id:'';
							}else{
								field.fieldEn=fieldEn;
								field.fieldCn = fieldCn;
								field.strategyId=fieldEn;
							}
						}else{
							let {fieldCn,fieldEn} = strategyOutputList.find(x=>x.fieldEn==this.selOutput);
							field.fieldEn=fieldEn;
							field.fieldCn = fieldCn;
						}		
					}else{
						let {fieldCn,fieldEn} = nodeInfoList.find(x=>x.fieldEn==this.selOutput);
						field.fieldEn=fieldEn;
						field.fieldCn = fieldCn;
					}
					const currentField = this.Fielduser.find(x=>x.fieldEn==field.fieldEn);					
					// this.input[this.changeingSelect].field_name = field.fieldCn
					// this.input[this.changeingSelect].field_code = field.fieldEn
					// this.input[this.changeingSelect].field_id =currentField?currentField.id:''
					// this.input[this.changeingSelect].valueType =currentField?currentField.valueType:''
					// this.input[this.changeingSelect].strategyId = field.strategyId?field.strategyId:"";
					// this.input[this.changeingSelect].nodeType =this.tempSelectType
					// this.input[this.changeingSelect].nodeId = this.SelectRadio
					// this.input[this.changeingSelect].asName = field.asName+'/'+field.fieldCn;

                    this.input[this.changeingSelect].fieldName = field.fieldCn?field.fieldCn:field.fieldEn
					this.input[this.changeingSelect].fieldCode = field.fieldEn
					this.input[this.changeingSelect].fieldId =currentField?currentField.id:''
					this.input[this.changeingSelect].valueType =currentField?currentField.valueType:1
					this.input[this.changeingSelect].strategyId = field.strategyId?field.strategyId:"";
					this.input[this.changeingSelect].nodeType =this.tempSelectType
					this.input[this.changeingSelect].nodeId = this.SelectRadio
					this.input[this.changeingSelect].asName = field.asName+'/'+(field.fieldCn?field.fieldCn:field.fieldEn);

					// this.SelectRadio = null
					this.initSelect();
					this.changeingSelect = ''
					this.dialogTyep20 = false
				} else {
					this.$message({
						message: '请选择一个选项再确定',
						type: 'warning'
					});
				}
			},
            selRuleChange(e){
				const currentRule = this.ruleList[e];
				const {ruleOutputList,listDbOutputList,fieldEn} = currentRule;
				this.selOutput="";
				if(ruleOutputList){
					this.outputList = ruleOutputList;
				}else if(listDbOutputList){
					this.outputList = listDbOutputList;
				}else if(fieldEn){
					this.outputList=[];
					this.selOutput=fieldEn;
				}
			},
			selectNodeInfo(e){
				const {strategyOutputList,ruleOutput,listDbOutput} = this.radioAll.find(x=>x.nodeId==e);
				this.outputList=strategyOutputList;
				this.selRule=null;			
				this.selOutput="";
				if(strategyOutputList==null&&listDbOutput==null){
					let newArr = ruleOutput.statisticsOutputList.map(v=>{
						return {...v,name:v.fieldCn,id:v.fieldEn}
					})
					this.ruleList=[...newArr,...ruleOutput.ruleInfoList];
					this.outputList=[];
				}else if(strategyOutputList==null&&ruleOutput==null){
					let newArr = listDbOutput.statisticsOutputList.map(v=>{
						return {...v,listName:v.fieldCn,id:v.fieldEn}
					})
					this.ruleList=[...newArr,...listDbOutput.listDbInfoOutput];
					this.outputList=[];
				}else{
					this.ruleList=[];
				}				
			},
			SelectChange(e) {
				let {nodeInfoList} = this.decisionOptions.find(x=>x.nodeType==this.tempSelectType);
				this.radioAll = nodeInfoList;
				this.SelectRadio="";
				this.selRule=null;
				this.selOutput="";
				this.ruleList=[];
				this.outputList=[];
			},
			focus(index,value) {
				console.log(index,value);
				this.dialogTyep20 = true
				this.tempSelectType = value.nodeType
				this.changeingSelect = index				
				if(value.nodeType != undefined &&this.decisionOptions.findIndex(x=>x.nodeType==value.nodeType)==-1){
					this.tempSelectType=-2;					
				}
				if(value.nodeType){
					let {nodeInfoList} = this.decisionOptions.find(x=>x.nodeType==this.tempSelectType);
					this.radioAll = nodeInfoList;
					if(this.tempSelectType!=-2){
						this.selectNodeInfo(value.nodeId)
					}

					this.SelectRadio = value.nodeId;
					this.selOutput = value.fieldCode;
					if(value.strategyId){
						this.selRule = this.ruleList.findIndex(x=>x.id==value.strategyId)
						const {ruleOutputList,listDbOutputList,fieldEn} = this.ruleList[this.selRule];

						if(ruleOutputList){
							this.selRuleChange(this.selRule)
						}else if(listDbOutputList){
							this.selRuleChange(this.selRule)
						}
						this.selOutput = value.fieldCode
					}
					
				}
			},
            addVariable() {
				this.input.push({
					fieldId: null,
                    fieldCode: "",
                    fieldName: "",
                    asName: "",
                    valueType: null
				})
			},
            delectVariable(index) {
				this.input.splice(index, 1)
                if(this.input.length<=0){
                    this.nodeJson.terminationInfo.conditions=[
						{
							"fieldCode": "",
							"fieldName": "",
							"valueType": null,
							"operator": "",
							"value": "",
							"relativeOperator": ""
						}
					]
                    this.nodeJson.terminationInfo.output=[
						{
							"fieldId": null,
                            "fieldCode": "",
                            "fieldName": "",
                            "valueType": null,
                            "variableType": null,
                            "fieldValue": ""
						}
					]
                }
			},
            submit(){                
                console.log("提交")   	
				if(this.input.length<=0){
					this.$message.error("请选择变量！")
					return
				}	
				let { conditions } = this.nodeJson.terminationInfo;	
				let { output } = this.nodeJson.terminationInfo
				let isT = true;
				if(this.input.length>0){
                    this.input.forEach(item=>{
                        if(item.fieldCode==""){
                            isT = false;
                        }
                    })
					conditions.forEach((item,index)=>{		
                        console.log(item);				
						if (item.fieldCode && item.operator && item.value.trim()) {						
							if(index !==conditions.length-1 && !item.relativeOperator){
								isT = false;
							}
						}else{
							isT = false;
						}
						const field = this.Fielduser.find(x=>x.fieldEn==item.fieldCode)
                        console.log(field);
						item.valueType = field?field.valueType?field.valueType:1:1;
                        item.fieldName = field?field.fieldCn?field.fieldCn:field.fieldEn:"";
					})				
					if( output.fieldId  && output.fieldValue && output.variableType){
						const field = this.Fielduser.find(x=>x.id==output.fieldId);
						output.fieldId = field.id;
						output.fieldName = field.fieldCn;
						output.fieldCode = field.fieldEn;
						output.valueType = field.valueType?field.valueType:1;
					}else{
						isT = false;
					}
				}else{
					conditions= [
						{
							"fieldCode": "",
							"fieldName": "",
							"valueType": null,
							"operator": "",
							"value": "",
							"relativeOperator": ""
						}
					],
					output = {
						"fieldId": null,
						"fieldCode": "",
						"fieldName": "",
						"valueType": null,
						"variableType": null,
						"fieldValue": ""
					}
				}	
                if(isT){
					let nodeJson = this.nodeJson;
					nodeJson.terminationInfo.input= this.input;
					nodeJson.terminationInfo.conditions = conditions;
					nodeJson.terminationInfo.output = output;
					this.loading = true;
                    console.log(nodeJson);
					this.setType({
							"id": this.data.id,
							"initEngineVersionId": String(this.data.Vid),
							"nodeType": 20,
							"nodeName": this.data.text,
							"nodeCode": this.data.nodeCode,
							"nodeOrder": this.data.nodeOrder,
							"nodeX": this.data.x,
							"nodeY": this.data.y,
							"nodeJson":JSON.stringify(nodeJson)
					}).then(res => {
						if (res.status == 1) {
							this.$message({
								message: '提交成功',
								type: 'success'
							});
							this.$emit('callback', nodeJson)
						}
						this.loading = false;
					})
				}else{
					this.$message.error("请检查是否有空字段!");
					return
				}
                
            },
            initSelect(){
				this.SelectRadio="";
				this.selRule=null;
				this.selOutput="";
				this.radioAll=[];
				this.ruleList=[];
				this.outputList=[];
			},
            async setType(param) {
				const data = await setType4(param);
				return data
			},
			crea() {
				
				return 
				
				// 聚合节点 不再拥有功能 成为 空节点
				
				
                this.selectLoading = true
                // 获取决策流条件入参
				this.getConditionParam({
					"nodeId": this.data.id
				}).then((res)=>{
					if(res.status == '1'){
						let {nodeTypeList} = res.data;
						this.decisionOptions =[
							{nodeType:-2,
							nodeTypeName:"字段",
							nodeInfoList:this.FieldUser},
							...nodeTypeList];
					}
					this.selectLoading = false
				})

                this.nodeJson = this.data.nodeJson ? JSON.parse(this.data.nodeJson) : {
                    "terminationInfo": {
                        "input": [],
                        "conditions": [
                            {
                                "fieldCode": "",
                                "fieldName": "",
                                "valueType": null,
                                "operator": "",
                                "value": "",
                                "relativeOperator": ""
                            }
                        ],
                        "output": {
                            "fieldId": null,
                            "fieldCode": "",
                            "fieldName": "",
                            "valueType": 20,
                            "variableType": 1,
                            "fieldValue": ""
                        }
                    }
                }
                this.input = this.nodeJson.terminationInfo.input
			},
            async getConditionParam(param){
				const data = await getConditionInputParam(param)
				return data
			}
		},
		watch: {
			data() {
				this.crea()
			}
		}


	}
</script>

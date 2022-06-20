<template>
	<div v-loading="loading">
		<div style="border-top: 1px solid #ddd;padding: 5px ;">
			<p style="margin-top: 10px; margin-bottom: 10px;font-size: 14px;font-weight: bold;">已选节点：</p>
			<div class="rule-item" v-for="(value,idx) in rulesResult" :key="idx">
				<p @click="showDialog(value)">
					<i class="el-icon-success" v-if="value.ruleResult=='命中'"></i>
					<i class="el-icon-error" v-if="value.ruleResult=='未命中'"></i>
					{{value.ruleName}}</p>
				<span class="version-wrapper" v-if="value.versionCode">版本：{{value.versionCode}}</span>
				<span class="version-wrapper" v-if="value.ruleScore">得分：{{value.ruleScore}}</span>
			</div>
			<el-pagination small layout="prev, pager, next" :total="ruleResultList.length" :page-size="pageSize" @current-change="page=$event" style="margin-top: 10px;">
			</el-pagination>
			<div class="grouptype-wrapper" style="display: flex;margin-top: 10px;border-top: 1px solid #ddd;padding: 10px;">
				<el-radio v-model="nodeJson.groupType" :label="1" border size="mini">串行</el-radio>
				<el-radio v-model="nodeJson.groupType" :label="2" border size="mini">并行</el-radio>
			</div>
		</div>
		<div>
			<p class="type_title">
				终止条件：
			</p>
			<p v-show="nodeJson.terminationInfo.selectedRule.length==0" style="text-align: center;margin:20px 0;">无</p>
			<el-button v-show="nodeJson.terminationInfo.selectedRule.length>0" type="primary" round size="mini" @click="showRuleDialog=true" style="margin-top: 10px;">选择规则({{nodeJson.terminationInfo.selectedRule.length}})</el-button>
			<div v-show="nodeJson.terminationInfo.selectedRule.length>0">
				<div v-for="(condition,index) in nodeJson.terminationInfo.conditions" :key="index" class="conditions-wrapper field-wrapper">
					<el-input v-model="condition.fieldName" disabled style="width: 150px;" size="mini"></el-input>
					<el-input v-model="condition.operator" disabled style="width: 100px;margin-left: 10px;" size="mini"></el-input>
					<el-input v-model="condition.value" maxlength="30" disabled style="width: 60px;margin-left: 10px;" size="mini">
					</el-input>
					<el-input v-model="condition.relativeOperator" disabled style="width: 70px;margin-left: 10px;" size="mini" v-if="index!=nodeJson.terminationInfo.conditions.length-1"></el-input>
				</div>
			</div>
			<p class="type_title" v-show="nodeJson.terminationInfo.selectedRule.length>0">
				终止结果：
			</p>
			<div class="field-wrapper" v-show="nodeJson.terminationInfo.selectedRule.length>0">
				<el-select filterable :value="nodeJson.terminationInfo.output.fieldName" disabled size="mini" style="width:200px;">
				</el-select>
				<p style="margin: 10px;">
					=
				</p>
				<el-input v-model="nodeJson.terminationInfo.output.fieldValue" disabled size="mini" style="width:200px;">
					<template slot="prepend">{{nodeJson.terminationInfo.output.variableType==1?'常量':nodeJson.terminationInfo.output.variableType==2?'变量':'自定义'}}</template>
				</el-input>
			</div>
		</div>
		
		<el-dialog
			:title="currentStrategy.ruleName"
			:visible.sync="dialogVisible"
			width="60%">			
			<div v-if="snapshot" class="strategy-box">
				<!-- 复杂规则集 -->
				<div class="mask-wrapper">
					<rule :data="ruleConditionVo" :ZIndex="1"></rule>
					<div class="rule_home" style="margin-top: 20px;">
						<div class="rule_fa">
							<el-button icon="el-icon-plus" circle disabled></el-button>
							<el-button icon="el-icon-close" circle disabled='disabled' style="margin-right: 10px"></el-button>
						</div>
						<el-select v-model="resultFieldEn" disabled placeholder="请选择" style="width: 200px;">
							<el-option v-for="item in FieldUser" :key="item.id" :label="item.fieldCn" :value="item.fieldEn">
							</el-option>
						</el-select>
						<p style="margin: 10px;">
							=
						</p>
						<el-select filterable value="是否命中" disabled style="width: 255px;">
						</el-select>
					</div>
					<outcontent :outcontent="strategyOutputList" :ruleOut="false" type="complex_rule">
						<div style="display: flex;align-items: center;">
							<el-select v-model="scoreFieldEn" disabled filterable style="width: 200px;">
								<el-option v-for="item in FieldUser" :key="item.id" :label="item.fieldCn" :value="item.fieldEn">
								</el-option>
							</el-select>
							<p style="margin: 10px;">=</p>
							<div>
								<el-input v-model="score" maxlength="30" style="width: 255px;">
									<template slot="prepend">得分</template>
								</el-input>
							</div>
						</div>
					</outcontent>
				</div>
			</div>
		</el-dialog>
		<el-dialog title="已选规则" :visible.sync="showRuleDialog" width="40%">
			<div>
				<el-table :data="nodeJson.terminationInfo.selectedRule" size="mini" height="250">
					<el-table-column prop="id" label="id" width="80">
					</el-table-column>
					<el-table-column prop="code" label="code" width="180">
					</el-table-column>
					<el-table-column prop="name" label="名称">
					</el-table-column>
				</el-table>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	import {getStrategy} from '@/api/index.js'
	import rule from '@/components/models/RuleCont.vue'
	import outcontent from '@/components/models/outcontent.vue'
	export default{
		components: {
			rule,
			outcontent
		},
		props:{
			snapshot:{
				type:Object,
				default:{}
			},
			rowkey:{
				type:String,
				default:""
			},
			ruleResultList:{
				type:Array,
				default:[]
			}
		},
		data() {
			return {
				loading:false,
				dialogVisible: false,
				showRuleDialog:false,
				nodeJson:null,
				rules:[],
				monitorInfo:{},
				ruleConditionVo:[],
				resultFieldEn:"",
				strategyOutputList:[],
				scoreFieldEn:"",
				score:"",
				currentStrategy:{
					name:"详情"
				},
				page:1,
				pageSize:10
			}
		},
		created(){
			
			this.nodeJson = this.snapshot;
			if(this.nodeJson.groupType==2){
				this.rules= this.nodeJson.executeGroup.rules
			}else{
				this.rules= this.nodeJson.mutexGroup.rules
			}
		},
		methods:{
			showDialog(val){
				this.currentStrategy= val;
				let versionId = this.rules.find(x=>x.id==val.ruleId).ruleVersionId;
				if(versionId){
					this.getStrategy({"hbaseRowKey":this.rowkey+versionId},()=>{
						this.dialogVisible = true;	
					});
				}else{
					// 此规则无版本
					this.$message.error('此规则无版本！');
				}			
			},
			async getStrategy(param,fn){
				this.loading = true
				const data = await getStrategy(param);
				this.loading = false;
				if(data.status=='1'){
					// if (data.data.monitorInfo.snapshot.scorecardDimension.length > 0) {
						this.monitorInfo= data.data.monitorInfo
						this.ruleConditionVo = data.data.monitorInfo.snapshot.ruleConditionVo;
						this.strategyOutputList = data.data.monitorInfo.snapshot.strategyOutputList;
						this.resultFieldEn=this.monitorInfo.snapshot.resultFieldEn;
						this.scoreFieldEn = this.monitorInfo.snapshot.scoreFieldEn;
						this.score = this.monitorInfo.snapshot.score;						
						fn&&fn()
					// }
				}
				
			}
		},
		computed:{
			rulesResult() {
				if (this.ruleResultList) {
					let arr = this.ruleResultList.filter((value, index) => {
						return (index >= ((this.page - 1) * this.pageSize) && index < (this.page * this.pageSize))
					})
					return arr
				} else {
					return []
				}
			},
			Fielduser() {
				if (this.$store.state.FieldUser != null) {
					this.loading = false
					return this.$store.state.FieldUser.data.fieldList
				} else {
					return []
				}
			}
		}
	}
</script>

<style>
	.rule-item{
		margin-bottom: 10px;
	}
	.rule-item p{
		color: #409EFF;
		cursor: pointer;
		display: inline-block;
	}
	.rule-item p i{
		margin-right: 5px;
	}
	.rule-item p i.el-icon-success{
		color: #67C23A;
	}
	.rule-item p i.el-icon-error{
		color: #F56C6C;
	}
	/* #F56C6C */
	.rule-item .version-wrapper{
		margin-left: 10px;
		font-size: 12px;
		color: #000;
	}
	.field-wrapper{
		display: flex;
		align-items: center;
		margin: 15px 15px 15px 0;
	}
	.grouptype-wrapper,
	.mask-wrapper{
		position: relative;
		z-index: 3;
	}
	.grouptype-wrapper::before,
	.mask-wrapper::before{
		width: 100%;
		height: 100%;
		content: "";
		position: absolute;
		left: 0;
		top: 0;
		background-color: rgba(0, 0, 0, 0);
		z-index: 4;
	}
	.strategy-box{
		max-height: 500px;
		overflow-y: auto;
	}
</style>

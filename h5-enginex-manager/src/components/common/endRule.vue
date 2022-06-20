<template>

	<div class="stop-condition">

		<!-- 标题 -->
		<div style="background-color: #aaa; height: 30px;line-height: 30px; color: #fff;padding: 10px; box-sizing: border-box; margin: 10px 0;display: flex;align-items: center;justify-content: space-between;">
			<p>终止条件</p>
		</div>

		<!-- 按钮 -->
		<el-button type="primary" round size="mini" @click="selRule(true)" style="margin-top: 10px;">
			选择({{data.selectedRule.length}})</el-button>



		<div v-if="data.selectedRule.length>0">
			<div v-for="(condition,index) in data.conditions" :key="index" class="conditions-wrapper"
				style="display: flex;">
				<el-select v-model="condition.fieldCode"  placeholder="条数/分数" size="mini" style="width: 110px;">
					<slot>
						
					</slot>
				</el-select>
				<ruleRelation v-model="condition.operator" :value2.sync="condition.value" :valueType="valueType" size="mini"
					style="width:200px;"></ruleRelation>

				<el-select v-model="condition.relativeOperator" placeholder="请选择" style="width: 70px;margin-left: 10px;"
					size="mini" v-if="index!=data.conditions.length-1">
					<el-option label="and" value="&&"></el-option>
					<el-option label="or" value="||"></el-option>
				</el-select>
				<i class="el-icon-plus" style="font-size: 20px;color: #409EFF;" @click="addCondition(index)"></i>
				<i class="el-icon-close" style="font-size: 20px;color: #F56C6C;" v-show="index!=0"
					@click="delCondtion(index)"></i>
			</div>
		</div>
		<div v-if="data.selectedRule.length>0">
			<div
				style="background-color: #aaa; height: 30px;line-height: 30px; color: #fff;padding: 10px; box-sizing: border-box; margin: 10px 0;display: flex;align-items: center;justify-content: space-between;">
				<p>终止结果</p>
			</div>
			<div class="setting-wrapper">
				<el-select v-model="data.output.fieldId" filterable placeholder="输出变量"  @change="data.output.fieldCode = mixinGetvalueEn($event)" size="mini" style="width:130px;">
					<el-option v-for="(item,index) in Fielduser" :key="index" :label="item.fieldCn" :value="item.id">
					</el-option>
				</el-select>
				<div class="line">=</div>
				<varialeSelect v-model="data.output.fieldValue" :valueType="mixinGetvalueType(data.output.fieldId)" :variableType.sync="data.output.variableType" height="28px"></varialeSelect>
			</div>
		</div>

		<el-dialog title="选择条件(多选)" :visible="showRuleDialog" width="40%" :append-to-body="true" @close="cancelSelRule">
			<div>
				<el-checkbox-group v-model="currentSelectRule">
					<el-table ref="ruleArr" :data="pageRuleList" size="mini">
						
						<el-table-column width="60">
							<template slot-scope="scope">
								<el-checkbox :label="scope.row.id">&nbsp;</el-checkbox>
							</template>
					
						</el-table-column>
						<el-table-column prop="id" label="id" >
							
						</el-table-column>
						<el-table-column :prop="name" label="名称">
							
						</el-table-column>
						<el-table-column :prop="code" label="code">
						</el-table-column>
						
					</el-table>
					<el-pagination small layout="prev, pager, next" :total="list.length" :page-size="10"
						@current-change="page=$event" style="margin-left: 70%;margin-top: 10px;">
					</el-pagination>
				</el-checkbox-group>
			</div> 
			<span slot="footer" class="dialog-footer">
				<el-button @click="cancelSelRule">取 消</el-button>
				<el-button type="primary" @click="selRuleSure">确 定</el-button>
			</span>
		</el-dialog>
	</div>

</template>

<script>
	import ruleRelation from '@/components/common/ruleRelation.vue'
	import varialeSelect from '@/components/models/varialeSelect.vue'
	export default {
		components:{ruleRelation,varialeSelect},
		props: {
			data: {
				type: Object,
				default: {}
			}, //nodeJson.terminationInfo
			list:{
				type: Array,
				default: []
			},
			onlyOne:{
				type: Boolean,
				default: false
			},
			valueType:{
				type: Number,
				default: 1
			},
			name:{
				type: String,
				default: 'name'
			},
			code:{
				type: String,
				default: 'code'
			},
		},
		data() {
			return {
				showRuleDialog: false,
				page: 1,
				currentSelectRule:[]
			}
		},
		created() {
			
			
		
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
			pageRuleList(){
				return this.list.filter((value,index)=>{
					return index>=(this.page-1)*10&&index<this.page*10
				})
			}
		},
		methods: {
			addCondition(index) {
				this.data.conditions.splice(index + 1, 0, {
					"fieldCode": "",
					"fieldName": "",
					"valueType": this.valueType,
					"operator": "",
					"value": "",
					"relativeOperator": ""
				})
			},
			selRuleSure(){ //确定选择
				this.data.selectedRule = this.list.filter(value=>{
					return  this.currentSelectRule.indexOf(value.id)!=-1
				})
				if(this.data.selectedRule.length==0){
					this.$emit('reSetTerminationInfo')
				}
				this.showRuleDialog = false
				
			}, 
			cancelSelRule(){ //取消选择规则
				this.showRuleDialog = false
			},
			selRule(isShow) {
				if(this.onlyOne){
					console.log(this.data.selectedRule,this.pageRuleList)
					if(!this.pageRuleList[0]){
						this.$message.warning('请先选择上方节点')
						return
					}
					if(!this.data.selectedRule.length){
						this.data.selectedRule = this.pageRuleList
						this.$message.success('已为选中上方节点')
						return
					}else{
						this.$emit('reSetTerminationInfo')
						this.$message.success('已为你取消选中上方节点')
						return
					}
				}
				
				
				console.log(this.currentSelectRule )
				
				if (this.list.length > 0 && isShow) {
					this.showRuleDialog = true;
					this.currentSelectRule = this.data.selectedRule.map(value=>value.id)
				} else if (isShow) {
					this.$message.error('请先选择策略！');
				}

			},
			delCondtion(index){
				this.data.conditions.splice(index, 1); 
			},
		},
		watch:{
			list(newValue){
				
				if(this.data.selectedRule&&this.data.selectedRule.length>0){
					let num = -1
					this.data.selectedRule.forEach((value,index)=>{
						let is = true
						newValue.forEach(item=>{
							if(item.id==value.id){
								console.log(item.id,value.id)
								is = false
							}
						})
						if(is){
							num = index
						}
					})
					
					if(num!=-1){
						
						this.data.selectedRule.splice(num,1)
					}
					
				}
				
				
				
			}
		}
	}
</script>

<style>
</style>

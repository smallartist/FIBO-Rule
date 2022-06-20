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
					<el-dropdown-item icon="el-icon-close" command="delect" v-if="ZIndex!=1&&data.conditionType!=4">
						删除此节点</el-dropdown-item>
				</el-dropdown-menu>
			</el-dropdown>
		</div>
		<div v-if="data.conditionType==1" class="RuleSelcet">

			<div :style="{width: '80px',marginTop: top===0?'0':'8px'}">
				<el-select v-model="data.logical" placeholder="请选择" size="mini">
					<el-option :key="1" label="AND" value="&&"></el-option>
					<el-option :key="2" label="OR" value="||"></el-option>
				</el-select>
			</div>
		</div>
		<div style="width: 10px;height: 0;border-top: 1px dashed #C7C6C8;margin-top: 19px;margin-left: 5px;">
		</div>
		<div>
			<div
				:style="{border: '1px dashed '+ color[ZIndex%5],borderRadius: '5px',paddingRight: '5px',paddingBottom: '5px',position:'relative',minWidth:'30px',minHeight:'40px'}">
				
				<div
					:style="{border:'',margin:'',padding:'',paddingLeft:'0px'}">
				
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
								<div style="display: flex;">

									<el-cascader v-model="item.fieldEn" filterable size="mini" :options="fieldUserObj" clearable @change="ruleCascaderChange(item)"
										:key="keyValue+(item.random?item.random:0)" :props="{ expandTrigger: 'hover' }" @visible-change="randomAdd(item,$event)">
									</el-cascader>

									<ruleRelation v-model="item.operator" :value2.sync="item.fieldValue"
										:variableType.sync="item.variableType"
										:valueType="mixinGetValueTypeByJSONEn(item.fieldEn)" size="mini"></ruleRelation>

								</div>
							</div>
							

							<i class="el-icon-circle-close" style="color: #fa4949;margin-left: 5px;"
								@click="deleteSon(index)"></i>
						</div>
						<!--  conditionType==1 关系节点  conditionType==3  for的输出的关系节点  节点部分 -->
						<div v-if="item.conditionType==1">
							<!-- {{fieldType}} -->
							<rule :data="item"  :top="index"  :ZIndex="ZIndex+1"
								:index="index"  @delectLogical="delectLogical"
								>
							</rule>
						</div>

					</div>

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
			
				keyValue: 1, //用于给级联选择框重新渲染
				keyValueReady: false
			}
		},
		created() {

		},
		mounted() {
			if (this.data) {
				if (Array.isArray(this.data.fieldEn)) {
					if (this.data.fieldEn.length > 0) {
						this.EnChange(this.data.fieldEn, false)
					}
				}
			}
		},
		props: {
			
			fieldType: {
				type: String,
				default: ''
			},
			ZIndex: {
				type: Number,
				default: -1
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
		
		},
		computed: {
			
			fieldUserObj() {
				if (this.$store.state.FieldUserObj) {
					return this.$store.state.FieldUserObj.data.fieldList
				} else {
					return []
				}
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
					
					})


				} else if (str == "addlogical") { //添加链接符
					let obj = {
						"logical": '&&',
						"fieldId": "",
						"operator": "",
						"fieldValue": "",
						"conditionType": 1,
						"children": [],
					}
					this.data.children.push(obj)


				}  else if (str == "delect") {
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
			},

		},
		watch: {
			
			fielduserArr() {
				if (this.keyValueReady) {
					this.keyValue++
				}
			}
		}

	}
</script> 
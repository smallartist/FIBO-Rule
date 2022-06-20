<template>
	<div>
		<TreeChart :json="treeData" :class="{landscape: isVertical}" @add="addStock" @delete="deleteStock" />

		<el-dialog :title="tempStatus=='add'?'添加':tempStatus=='change'?'修改':'错误状态'" v-if="dialogVisible" :visible.sync="dialogVisible"
		 width="50%" :before-close="handleClose" :close-on-click-modal="false">

			<!-- <el-select v-model="tempData.fieldId" filterable @change="tempData.fieldEn = mixinGetvalueEn($event);fieldChange(tempData)">
				<el-option v-for="item in FieldUser" :key="item.id" :label="item.fieldCn" :value="item.id">
				</el-option>
			</el-select> -->
			
			
			<el-cascader v-model="tempData.fieldEn" filterable  :options="fieldUserObj" clearable @change="fieldChange(tempData)"
				:props="{ expandTrigger: 'hover' }" >
			</el-cascader>
			
			<div style="display: flex;margin-top: 10px;">
				<el-select v-model="tempData.logical" placeholder="请选择关系符">
					<el-option :key="1" label="AND" value="&&"></el-option>
					<el-option :key="2" label="OR" value="||"></el-option>
				</el-select>

				<div style="border-left: 1px dashed #ddd;margin-left: 10px;">
					<div v-for="(value,index) in tempData.conditionList" style="width: 500px;display: flex;margin-top: 10px;justify-content: flex-start;">
						<ruleRelation style="width: 120px;" :openValue2="false" v-model="value.operator" :valueType="tempData.valueType"
						 :type="2" size="mini" :andTextInput="true"></ruleRelation>
						<varialeSelect v-model="value.fieldValue" :variableType.sync="value.variableType" :valueType="tempData.valueType" style="margin-left: 20px;" height="28px"></varialeSelect>
						<i class="el-icon-circle-plus-outline" style="font-size: 20px;color: #409EFF;" @click="addRule(index)"></i>
						<i class="el-icon-circle-close" :style="{fontSize: '20px',color:index===0?'#ddd':'#F56C6C'}" @click="delectRule(index)"></i>
					</div>
				</div>
			</div>
			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogVisible = false;currentTreeData = {};tempData = {}">取 消</el-button>
				<el-button type="primary" @click="dialogSule">确 定</el-button>
			</span>
		</el-dialog>

		<el-dialog title="添加结果" v-if="resultDialogVisible" :visible.sync="resultDialogVisible" width="20%" @close="resultDialogClose"
		 :close-on-click-modal="false">
			<varialeSelect v-model="tempResult.resultValue"  :variableType.sync="tempResult.variableType"  style="margin-left: 20px;" height="28px"></varialeSelect>
			<span slot="footer" class="dialog-footer">
				<el-button @click="resultDialogVisible = false;">取 消</el-button>
				<el-button type="primary" @click="resultDialogSule">确 定</el-button>
			</span>

		</el-dialog>

	</div>
</template>

<script>
	import TreeChart from '@/components/common/decisionTree/treeNode.vue'
	import ruleRelation from '@/components/common/ruleRelation.vue'
	import varialeSelect from '@/components/models/varialeSelect.vue'
	export default {
		name: 'tree',
		components: {
			TreeChart,
			ruleRelation,
			varialeSelect
		},
		props: {
			treeData: {
				type: Object,
				default () {
					return {}
				}
			}

		},
		computed:{
			fieldUserObj() {
				if (this.$store.state.FieldUserObj) {
					return this.$store.state.FieldUserObj.data.fieldList
				} else {
					return []
				}
			},
		},
		data() {
			return {
				tempResult: {
					resultValue: '',
					variableType: 1,
				},
				resultDialogVisible: false, //添加结果弹窗
				isVertical: false, // 是否是竖方向,只给最外层的添加
				dialogVisible: false,
				tempData: {},
				currentTreeData: {},
				tempStatus: '', //临时状态 添加为 ‘add’  修改为 ‘change’
				
			}
		},
		methods: {
			resultDialogClose(){
				this.tempResult ={
					resultValue: '',
					variableType: 1,
				}
				this.currentTreeData = {}
			},
			deleteResult(data) {
				this.$confirm('确定删除此结果?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.currentTreeData = data
					const deleteData = (data) => {
						data.some((item, i) => {
							if (item == this.currentTreeData) {
								item.variableType = 1
								item.resultValue = ""
								return
							} else if (item.children) {
								deleteData(item.children)
							}
						})
					}
					let arr = [this.treeData]
					deleteData(arr)
					this.treeData = arr[0] ? arr[0] : {},
						this.currentTreeData = {}

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
			resultDialogSule() {
				if(this.tempResult.resultValue.trim()==""||this.tempResult.resultValue==null||(this.tempResult.variableType==3&&JSON.parse(this.tempResult.resultValue).formula.trim()=="")){
					this.$message.error('请输入值')
					return
				}
				const deleteData = (data) => {
					data.some((item, i) => {
						if (item == this.currentTreeData) {
							Object.assign(item, this.tempResult)
							return
						} else if (item.children) {
							deleteData(item.children)
						}
					})
				}
				let arr = [this.treeData]
				deleteData(arr)
				this.treeData = arr[0] ? arr[0] : {}

					this.resultDialogVisible = false
				this.currentTreeData = {}
				this.tempResult = {
					resultValue: '',
					variableType: 1,
				}

			},
			addResult(data) {
				this.currentTreeData = data
				this.tempResult = {
					resultValue: '',
					variableType: 1,
				}
				this.resultDialogVisible = true

			},
			fieldChange(item) {
				
				
				item.fieldId = this.mixinGetIdByEn(item.fieldEn[0])
				item.valueType = this.mixinGetValueTypeByJSONEn(item.fieldEn)
			
				item.conditionList.forEach(value => {
					value.operator = ""
				})
			},
			dialogSule() {
				let is = false
				this.tempData.conditionList.forEach(value => {
					if (value.operator == "" || String(value.fieldValue).trim() === "") {
						is = true
					}
					if (value.variableType == 3 && (value.fieldValue === "" || JSON.parse(value.fieldValue).formula === "")) {
						is = true
					}
				})
				if (is) {
					this.$message.error('比较符和值不允许为空')
					return
				}

				const deleteData = (data) => {
					data.some((item, i) => {
						if (item == this.currentTreeData) {
							if (this.tempStatus == 'change') {
								Object.assign(item, this.tempData)
							} else if (this.tempStatus == 'add') {
								item.children.push(this.tempData)
								// item.nodeType=1
							}
							return
						} else if (item.children) {
							deleteData(item.children)
						}
					})
				}
				let arr = [this.treeData]
				deleteData(arr)
				this.treeData = arr[0] ? arr[0] : {}
				this.dialogVisible = false
				this.currentTreeData = {}
				this.tempData = {}
				this.tempStatus = ''
			},
			handleClose() {
				this.dialogVisible = false
				this.currentTreeData = {}
				this.tempData = {}
			},
			// 新增编辑,val: 0 新增, 1 编辑 ，2 添加结果 3 删除结果 4更改结果
			addStock(data) {
				if (data.val == 0) {
					this.addNode(data.data)
				} else if (data.val == 1) {
					this.changeNode(data.data)
				} else if (data.val == 2) {
					this.addResult(data.data)
				} else if (data.val == 3) {
					this.deleteResult(data.data)
				}else if (data.val == 4) {
					this.updateResult(data.data)
				}

			},
			updateResult(data){
				console.log(data)
				this.resultDialogVisible = true
				this.currentTreeData = data
				this.tempResult ={
					resultValue: data.resultValue,
					variableType:data.variableType,
				}
				
				
			},
			addNode(data) {
				this.tempStatus = 'add'
				this.currentTreeData = data
				this.tempData = {
					"conditionList": [{
						"operator": "",
						"variableType": 1,
						"fieldValue": ""
					}],
					nodeType:'',
					"fieldId": "",
					"children": [],
					"logical": "&&",
					"fieldEn": [],
					resultValue: "",
					"variableType": 1,
				}
				this.dialogVisible = true
			},
			addRule(index) {
				this.tempData.conditionList.splice(index + 1, 0, {
					operator: '',
					fieldValue: '',
					variableType: 1
				})
			},
			delectRule(index) {
				if (index == 0) {
					return
				}
				this.tempData.conditionList.splice(index, 1)
			},
			changeNode(data) {
				this.currentTreeData = data
				this.tempStatus = 'change'
				this.tempData = JSON.parse(JSON.stringify(data))
				this.dialogVisible = true
			},
			// 删除
			deleteStock(data) {
				if (data == this.treeData) {
					this.$message({
						type: 'warning',
						message: '顶层节点不允许删除'
					});
					return
				}


				this.$confirm('确定删除此节点?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.currentTreeData = data
					this.confimdelete()
				}).catch(() => {
					this.$message({
						type: 'info',
						message: '已取消删除'
					});
				});
			},
			// 确定删除
			confimdelete() {
				// 前端删除 遍历原数据,删除匹配random数据
				const deleteData = (data) => {
					data.some((item, i) => {
						if (item == this.currentTreeData) {
							data.splice(i, 1)
							
							return
						} else if (item.children) {
							deleteData(item.children)
						}
					})
				}
				let arr = [this.treeData]
				deleteData(arr)
				this.treeData = arr[0] ? arr[0] : {}
				console.log(this.treeData)
				this.dialogVisible2 = false
				this.$message({
					type: "success",
					message: "成功"
				});
			}
		}
	}
</script>

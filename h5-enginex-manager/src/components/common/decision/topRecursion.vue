<style>
	.SCO_defen {
		position: absolute;
		right: -200px;
		display: flex;
		align-items: center;
		justify-content: center;
		width: 200px;
		height: 50px;
		border-bottom: 1px solid #ddd;
		border-right: 1px solid #ddd;
		/* margin-top: 1px; */
	}

	.el-dropdown-link {
		cursor: pointer;
		color: #409EFF;
	}

	.el-icon-arrow-down {
		font-size: 12px;
	}

	.demonstration {
		display: block;
		color: #8492a6;
		font-size: 14px;
		margin-bottom: 20px;
	}

	.recursion_cont {
		overflow: hidden;
		padding-left: 5px;
		padding-top: 5px;
		/* box-sizing: border-box; */
		position: relative;

	}

	.recursion_cont:hover {
		overflow: unset;

	}

	.top:hover {
		background-color: #ddd;
		cursor: pointer
	}

	.Toprecusion_for {
		flex-direction: column;
		box-sizing: border-box;
		/* margin-left:-1px; */
		display: flex;
		border: 1px solid #ddd;
		border-bottom: 0;
		border-right: 0;
		min-height: 80px;
		flex-shrink: 1;
		/* borderRight:tier==-1?'1px solid #ddd':'', */
	}
</style>
<template>
	<div :style="{width: '',display:'flex',flexGrow:1}">

		<div v-for="(item,index) in data" :key="item.random" class="Toprecusion_for"
			:style="{borderRight:tier==-1&&index==data.length-1?'1px solid #ddd':'',marginLeft:tier==-1&&index==0?'':'-1px',marginTop:'-1px',width:item.children.length>0?'':'200px'}">

			<div :style="{'borderBottom':'1px solid #ddd',flexGrow:item.children.length>0?0:1,height:item.children.length>=1?'80px':''}"
				class="recursion_cont">
				<div style="margin-left: 20px;width: 175px;"
					:class="item.children.length==0?'top toplast '+Math.random():'top' " @click="openCompile(index)">
					<span
						style="font-size: 12px;">{{mixinGetvalueCn(item.fieldId)?mixinGetvalueCn(item.fieldId):'请选择指标'}}</span>
					<div style="display: flex;">
						<p style="border-right: 1px dashed #ddd;">

							<span style="font-size: 14px;">{{item.logical?mixinGetLogical(item.logical):'AND'}}</span>
						</p>
						<div>

							<div v-for="(value,inde) in item.conditionList" style="display: flex;align-items: center;">
								<span
									style="margin-left: 10px;margin-right: 5px;font-size: 14px;">{{value.operator=='contains'?'包含':value.operator=='not contains'?'不包含':value.operator}}</span>
								<span
									style="font-size: 14px;">{{value.variableType==2?mixinGetCnByEn(value.fieldValue):value.variableType==3?JSON.parse(value.fieldValue).formula:value.fieldValue}}</span>
							</div>
						</div>
					</div>
				</div>
				<div style="position: absolute;left: 0;top: 0;font-size: 12px;color: #409EFF;">
					<el-dropdown @command="handleCommand" trigger="click">
						<span class="el-dropdown-link">
							<i class="el-icon-s-operation"></i>
						</span>
						<el-dropdown-menu slot="dropdown">
							<el-dropdown-item icon="el-icon-bottom" :command="'addSon|'+index"
								v-show="!item.children.length">向下添加</el-dropdown-item>
							<el-dropdown-item icon="el-icon-right" :command="'addBrother|'+index">向右添加
							</el-dropdown-item>
							<el-dropdown-item icon="el-icon-close" :command="'delect|'+index"
								v-show="tier!=-1||index!=0">删除此字段</el-dropdown-item>
							<el-dropdown-item icon="el-icon-document-copy" :command="'copy|'+index">复制此字段
							</el-dropdown-item>
						</el-dropdown-menu>
					</el-dropdown>
				</div>
			</div>
			<recursion v-if="item.children.length>0" :data="item.children" :tier="tier+1" :direction="direction">
			</recursion>
		</div>
		<el-dialog title="编辑" v-if="dialogVisible" :visible.sync="dialogVisible" width="50%" :before-close="handleClose"
			:close-on-click-modal="false">

			<!-- <el-select v-model="tempData.fieldId" filterable @change="tempData.fieldEn = mixinGetvalueEn($event);fieldChange(tempData)">
				<el-option v-for="item in FieldUser" :key="item.id" :label="item.fieldCn" :value="item.id">
				</el-option>
			</el-select> -->
			<el-cascader v-model="tempData.fieldEn" filterable :options="fieldUserObj" clearable
				@change="fieldChange(tempData)" :props="{ expandTrigger: 'hover' }">
			</el-cascader>
			<div style="display: flex;margin-top: 10px;">
				<el-select v-model="tempData.logical" placeholder="请选择关系符">
					<el-option :key="1" label="AND" value="&&"></el-option>
					<el-option :key="2" label="OR" value="||"></el-option>
				</el-select>
				<div style="border-left: 1px dashed #ddd;margin-left: 10px;">
					<div v-for="(value,index) in tempData.conditionList"
						style="width: 500px;display: flex;margin-top: 10px;justify-content: flex-start;">
						<ruleRelation style="width:120px;" :openValue2="false" v-model="value.operator"
							:valueType="tempData.valueType" :type="2" size="mini" :andTextInput="true"></ruleRelation>
						<varialeSelect v-model="value.fieldValue" :valueType="tempData.valueType"
							:variableType.sync="value.variableType" style="margin-left: 20px;" height="28px">
						</varialeSelect>
						<i class="el-icon-circle-plus-outline" style="font-size: 20px;color: #409EFF;"
							@click="addRule(index)"></i>
						<i class="el-icon-circle-close" :style="{fontSize: '20px',color:index===0?'#ddd':'#F56C6C'}"
							@click="delectRule(index)"></i>
					</div>
				</div>
			</div>


			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogRe">清空并关闭</el-button>
				<el-button @click="dialogVisible = false">取 消</el-button>
				<el-button type="primary" @click="dialogSule">确 定</el-button>
			</span>
		</el-dialog>
	</div>
</template>

<script>
	import textInput from '@/components/common/textInput.vue'
	import textSelect from '@/components/common/textSelect.vue'
	import bus from './bus.js'
	import ruleRelation from '@/components/common/ruleRelation.vue'
	import varialeSelect from '@/components/models/varialeSelect.vue'
	export default {
		data() {
			return {
				dialogVisible: false,
				tempData: {},
				tempIndex: ''
			}
		},
		components: {
			textInput,
			textSelect,
			ruleRelation,
			varialeSelect
		},
		name: 'recursion',
		props: {
			direction: {
				type: String,
				default () {
					return 'com'
				}
			},
			data: {
				type: Array,
				default () {
					return []
				}
			},
			tier: {
				type: Number,
				default: -1
			}
		},
		created() {
			this.data.forEach(value => {
				if (value.coefficient == null) {
					value.coefficient = 0
				}
			})
		},
		beforeDestroy() {

		},
		methods: {
			dialogRe() {

				this.$set(this.data, this.tempIndex, {
					"fieldId": '',
					"fieldEn": [],
					"logical": "&&",
					// "type": this.tempData.type,
					"indexValue": this.tempData.indexValue,
					"valueType": null,
					"conditionList": [{
						"operator": "",
						"fieldValue": "",
						variableType: 1
					}],
					"children": this.tempData.children,
					"random": this.tempData.random
				})
				this.dialogVisible = false;
			},
			dialogSule() {
				let is = false
				this.tempData.conditionList.forEach(value => {
					if (value.operator == "" || String(value.fieldValue).trim() === "") {

						is = true
					}
					if (value.variableType == 3 && (value.fieldValue === "" || JSON.parse(value.fieldValue)
							.formula === "")) {
						is = true
					}
				})

				if (is) {
					this.$message.error('比较符和值不允许为空')
					return
				}
				// bus.$emit('lookrodem')
				for (let i in this.tempData) {
					this.data[this.tempIndex][i] = this.tempData[i]
				}
				this.dialogVisible = false;
				// this.data[this.tempIndex] = this.tempData


			},
			handleClose(done) {
				this.$confirm('确认关闭？')
					.then(_ => {
						done();
					})
			},
			openCompile(index) {
				this.tempIndex = index
				for (let i in this.data[index]) {
					if (typeof this.data[index][i] == 'object' && this.data[index][i] != null && i != 'children') {
						this.$set(this.tempData, i, JSON.parse(JSON.stringify(this.data[index][i])))
					} else {
						this.$set(this.tempData, i, this.data[index][i])

					}
				}
				if (this.tempData.conditionList.length == 0) {
					this.tempData.conditionList.push({
						operator: '',
						fieldValue: '',
						variableType: 1
					})
				}
				
				// console.log(this.tempData)
				// debugger
				this.dialogVisible = true
			},

			delectRule(index) {
				if (index == 0) {
					return
				}
				this.tempData.conditionList.splice(index, 1)
			},
			fieldChange(item) {


				item.fieldId = this.mixinGetIdByEn(item.fieldEn[0])
				item.valueType = this.mixinGetValueTypeByJSONEn(item.fieldEn)
				console.log(item)
				item.conditionList.forEach(value => {
					value.operator = ""
				})
			},
			handleCommand(str) {
				switch (str.split('|')[0]) {
					case 'addBrother':
						this.addBrother(str.split('|')[1])
						break;
					case 'delect':
						this.delect(str.split('|')[1])
						break;
					case 'addSon':
						this.addSon(str.split('|')[1])
						break;
					case 'copy':
						this.copy(str.split('|')[1])
						break;
					case 'addRule':
						this.addRule(str.split('|')[1])
						break;
				}
			},
			addRule(index) {
				this.tempData.conditionList.splice(index + 1, 0, {
					operator: '',
					fieldValue: '',
					variableType: 1
				})
			},
			addSon(index) {
				this.data[index].children.push({
					fieldId: '',
					condition: '',
					children: [],
					fieldEn: [],
					logical: '&&',
					id: null,
					"score": 1,
					"coefficient": null,
					"calculateType": 1,
					conditionList: [{
						operator: '',
						variableType: 1,
						fieldValue: '',
					}],
					random: Math.random()
				})
				bus.$emit('reCount', 'addTop')
			},
			delect(index) {
				this.data.splice(index, 1)
				bus.$emit('reCount', 'delectTop')
			},
			addBrother(index) {
				this.data.splice(Number(index) + 1, 0, {
					conditionList: [{
						operator: '',
						variableType: 1,
						fieldValue: '',
					}],
					fieldId: '',
					children: [],
					fieldEn: [],
					id: null,
					logical: '&&',
					parentId: this.data[0].parentId,
					random: Math.random()
				})
				bus.$emit('reCount', 'addTop')
				bus.$emit('look')
			},
			copy(index) {
				console.log(index)
				let obj = JSON.parse(JSON.stringify(this.data[index]))
				obj.id = null
				obj.random = Math.random()
				this.data.splice(Number(index) + 1, 0, obj)
				bus.$emit('reCount', {
					type: 'copyTop',
					data: this.data[index]
				})
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
			width() {
				console.log(this.data)
				return 1
			}
		}
	}
</script>

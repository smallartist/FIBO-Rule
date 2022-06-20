<style>
	.rule_home {
		display: flex;

	}

	.rule_fa {
		display: flex;
		width: 20%;
		height: 30%;
		margin: 10px 10px 0 0;
		flex-shrink: 0;
	}

	.rule_son {
		display: flex;
		margin-top: 10px;
		justify-content: flex-start;
	}

	.rule_dialg_header {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 40px;
	}

	.rule_dialg_cont {
		display: flex;
		flex-wrap: wrap;
		justify-content: flex-start;
		height: 40vh;
		overflow: scroll;
		overflow-x: hidden;
		align-items: flex-start;
		align-content: flex-start;
	}

	.rule_dialg_cont>div {
		width: 30%;
		overflow: hidden;
		margin-top: 10px;
		margin-right: 10px;
		background-color: #fff;
	}

	.rule_dialg_cont>div:hover {
		overflow: unset;
	}
	.ruleOut{
		position: absolute;
		padding-top: 10px;
		height: 100%;
		display: flex;
		align-items: center;
		margin-left: 100px;
		
	}
	.ruleOut>span{
		border-radius: 2px;
	}
	.ruleOut>span:hover{
		/* cursor:hand */
		user-select: none;
		cursor:pointer;
		
	}
	.ruleOut>span:active{
		margin-top: 3px;
	}
	
</style>

<template>
	<div>
		<div class="rule_home">
			<div class="rule_fa">
				<el-button icon="el-icon-plus" circle @click="outAdd(0)"></el-button>
				<el-button icon="el-icon-close" circle @click="outDelect(index)"  v-if="!unone" disabled style="margin-right: 10px"></el-button>
			</div>
			<slot></slot>
		</div>

		<div v-for="(value,index) in outcontent" class="rule_home" style="display: flex;position: relative;">
			<div class="rule_fa">
				<el-button icon="el-icon-plus" circle @click="outAdd(index+1)"></el-button>
				<el-button icon="el-icon-close" circle @click="outDelect(index)"  style="margin-right: 10px"></el-button>
			</div>
			<div v-if="ruleOut" class="ruleOut">
				<span :style="{backgroundColor:value.outCondition?'#afd7ff':'#e8e8e8',border:value.outCondition?'1px solid #409EFF':'1px solid #adadad',color:value.outCondition?'#409EFF':'#adadad',fontSize:'12px',padding:'2px'}"
				 @click="ruleOutDialogOpen(value,index)">条件输出</span>
			</div>

			<div style="display: flex;align-items: center;">
				<el-select :value="getCn(value.fieldId)" placeholder="请选择" style="width: 200px;margin-right: 10px;" @focus="outClick(index)">
				</el-select>
				<p style="margin-right: 10px;">=</p>

				<varialeSelect v-model="value.fieldValue" :valueType="mixinGetvalueType(value.fieldId)" :variableType.sync="value.variableType" width="255px" height="40px" size="—"></varialeSelect>

			</div>
		</div>
		<el-dialog title="输入参数"  :visible.sync="dialogVisible" width="50%" >
			<div class="rule_dialg_header">
				请选择参数：
				<div>
					<el-input v-model="search" maxlength="30" placeholder="快速搜索"></el-input>
				</div>
			</div>
			<div class="rule_dialg_cont">
				<div v-for="value in fradioList">
					<el-radio v-model="radio" :label="value.id" border size="mini">{{value.fieldCn}}</el-radio>
				</div>
			</div>
			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogVisible = false;radio='';search=''">取 消</el-button>
				<el-button type="primary" @click="dialogSure()">确 定</el-button>
			</span>
		</el-dialog>
		<el-dialog title="条件输出" :visible.sync="ruleOutDialog" width="40%" :close-on-click-modal="false" @close="tempOutCondition='';tempIndex=''">
			<el-select v-model="tempOutCondition.logical" placeholder="请选择关系符">
				<el-option :key="1" label="AND" value="&&"></el-option>
				<el-option :key="2" label="OR" value="||"></el-option>
			</el-select>

			<div style="margin-top: 20px;margin-left: 100px;">
				<div v-for="(value,index) in tempOutCondition.conditionList" style="display: flex;align-items: center;margin-top: 5px;">
					<el-select filterable v-model="value.fieldEn" placeholder="请选择" style="width: 200px;margin-right:10px;" @change="value.operator=''"
					 size="mini">
						<el-option v-for="item in FieldUser" :label="item.fieldCn" :value="item.fieldEn"></el-option>
					</el-select>
					<ruleRelation style="width: 200px;" :openValue2="false" v-model="value.operator" :valueType="mixinGetValueTypeByEn(value.fieldEn)"
					 :type="2" size="mini" :andTextInput="true"></ruleRelation>
					<varialeSelect v-model="value.fieldValue" :valueType="mixinGetValueTypeByEn(value.fieldEn)" :variableType.sync="value.variableType" style="margin-left: 20px;" height="28px"></varialeSelect>
					<i class="el-icon-circle-plus-outline" style="font-size: 20px;color: #409EFF;" @click="addRule(index)"></i>
					<i class="el-icon-circle-close" :style="{fontSize: '20px',color:index===0?'#ddd':'#F56C6C'}" @click="delectRule(index)"></i>
				</div>
			</div>


			<span slot="footer" class="dialog-footer">
				<el-button @click="ruleOutDialog = false;">取 消</el-button>
				<el-button type="primary" @click="ruleOutDialogSure()">确 定</el-button>
				<el-button type="primary" @click="delectOutCondition()">关闭条件输出</el-button>
			</span>
		</el-dialog>



	</div>



</template>

<script>
	import ruleRelation from '@/components/common/ruleRelation.vue'
	import varialeSelect from '@/components/models/varialeSelect.vue'
	export default {
		components: {
			ruleRelation,
			varialeSelect
		},
		data() {
			return {
				tempOutCondition: {},
				ruleOutDialog: false,
				dialogVisible: false,
				search: '',
				radioList: [],
				radio: '',
				tempIndex: '',
			}
		},
		
		props: {
			ruleOut: {
				type: Boolean,
				default: false
			},
			outcontent: {
				type: Array,
				default: () => {
					return []
				}
			},
			type: {
				type: String,
				default: ''
			},
			zhezhao: {
				type: Boolean,
				default: true
			},
			unone:{
				type: Boolean,
				default: false
			},
			outType:{
				type: Object|null,
				default: () => {
					return null
				}
			}
		},
		// watch: {
		// 	outcontent: {
		// 		handler(val, oldVal) {
		// 			if(this.unone&&val.length===0){
		// 				this.outAdd(0)
		// 			}
		// 		},
		// 		deep: true
		// 	}
		// },
		computed: {
			FieldUser() {
				if (this.$store.state.FieldUser) {
					
					return this.$store.state.FieldUser.data.fieldList
				} else {
					return []
				}
			},
			outPutUser() {
				if (this.$store.state.Output) {
					return this.$store.state.Output.data.paramMap.fieldList
				} else {
					return []
				}
			},
			fradioList() {
				if (this.search != "") {
					let arr = []

					arr = this.radioList.filter(value => {
						if (value.fieldCn.indexOf(this.search) != -1) {
							return true
						} else {
							return false
						}
					})
					return arr
				} else {
					return this.radioList
				}

			}
		},
		methods: {
			delectOutCondition() {
				delete this.outcontent[this.tempIndex].outCondition
				this.ruleOutDialog = false
			},
			delectRule(index) {
				this.tempOutCondition.conditionList.splice(index, 1)
			},
			addRule(index) {
				this.tempOutCondition.conditionList.splice(index + 1, 0, {
					conditionType: 2,
					fieldEn: '',
					fieldValue: '',
					operator: '',
					variableType: 1,
				})
			},
			ruleOutDialogOpen(e, index) {
				this.tempIndex = index
				this.ruleOutDialog = true
				if (e.outCondition) {
					this.tempOutCondition = JSON.parse(e.outCondition)
				} else {
					this.tempOutCondition = {
						logical: '&&',
						conditionList: [{
							conditionType: 2,
							fieldEn: '',
							fieldValue: '',
							operator: '',
							variableType: 1,
						}]
					}
				}
			},
			ruleOutDialogSure() {
				let is = false
				this.tempOutCondition.conditionList.forEach(value => {
					if (value.fieldEn.trim() === "" || value.fieldValue.trim() === "" || value.operator.trim() === "") {
						is = true
					}
				})
				if (is) {
					this.$message.error('请检查是否有未填项')
					return
				}


				this.outcontent[this.tempIndex].outCondition = JSON.stringify(this.tempOutCondition)

				this.ruleOutDialog = false
			},
			outAdd(index) {
				let obj = {
					"fieldId": "",
					"fieldValue": "",
					variableType: 1,
				}
				if(this.outType){
					obj=Object.assign(obj,this.outType)
				}
				if (this.type !== "") {
					obj.strategyType = this.type
				}
				this.outcontent.splice(index, 0, obj)
			},
			outDelect(index) {
				this.outcontent.splice(index, 1)
			},
			dialogSure() {
				if (this.radio == '') {
					this.$message.error('请选择一个字段，或者选择取消')
				} else {
					if (this.tempcur.split('###')[1] === "Data") {
						this.Data.forEach((value, index) => {
							if (index === parseInt(this.tempcur.split('###')[0])) {
								console.log(value)
								value.fieldId = this.radio + '|' + this.getvalueEn(this.radio)
								value.operator = ""
								value.fieldValue = ""
							}
						})
					} else if (this.tempcur.split('###')[1] === "out") {
						console.log(this.outcontent)
						this.outcontent.forEach((value, index) => {
							if (index === parseInt(this.tempcur.split('###')[0])) {
								value.fieldId = this.radio
								// value.fieldEn = this.mixinGetvalueEn(this.radio)
								this.$set(value,'fieldEn',this.mixinGetvalueEn(this.radio))
								value.fieldValue = ''
							}
						})
					}
					this.dialogVisible = false;
					this.radio = '';
					this.search = '';
				}

			},
			getCn(id, type) {
				let Cn
				if (type != 2) {
					this.FieldUser.forEach(value => {
						if (value.id == parseInt(id)) {
							Cn = value.fieldCn
							return
						}
					})
				} else {
					this.FieldUser.forEach(value => {
						if (value.id == parseInt(id)) {
							Cn = value.fieldCn
							return
						}
					})
				}
				return Cn
			},
			getvalueEn(cont) {
				let num
				console.log(cont)
				this.FieldUser.forEach(value => {
					if (value.id === parseInt(cont)) {
						num = value.fieldEn
						console.log(1)
					}
				})
				return num
			},
			dataClick(index, id) {
				this.tempcur = index + '###Data'
				this.radioList = this.FieldUser
				this.radio = Number(id.split('|')[0])
				console.log(id)
				this.dialogVisible = true
			},
			outClick(index) {
				this.tempcur = index + '###out'
				console.log(this.tempcur)
				this.radioList = this.FieldUser
				this.dialogVisible = true
			},
			getvalueType(cont) {
				let num
				this.FieldUser.forEach(value => {
					if (value.id === parseInt(cont)) {
						num = value.valueType
					}
				})
				return num
			}
		}
	}
</script>

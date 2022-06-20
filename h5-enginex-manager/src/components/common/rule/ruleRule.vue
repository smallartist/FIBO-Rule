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
	.rule_dialg_header{
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 40px;
	}
	.rule_dialg_cont{
		display: flex;
		flex-wrap: wrap;
		justify-content: flex-start;
		height: 40vh;
		overflow: scroll;
		overflow-x: hidden;
		align-items: flex-start;
		align-content: flex-start;
	}
	.rule_dialg_cont>div{
		width: 30%;
		overflow: hidden;
		margin-top: 10px;
		margin-right: 10px;
		background-color: #fff;
	}
	.rule_dialg_cont>div:hover{
		
		overflow: unset;
	}
	
</style>

<template>
	<div style="margin-left: 20px;margin-top: 20px;">
		<div v-if="Data&&FieldUser">
			<div v-if="type==1">
				条件：
				<div v-for="(item,index) in Data" class="rule_home">
					<div class="rule_fa">
						<el-button icon="el-icon-plus" circle @click="$emit('faadd',index)"></el-button>
						<el-button icon="el-icon-close" circle @click="$emit('fadelect',index)" :disabled="index===0?'disabled':false"
						 style="margin-right: 10px;"></el-button>
					</div>
					<div style="display: flex;">


						<el-select v-show="index===0" v-model="item.logical" placeholder="请选择" style="width: 200px;margin-right: 10px;">
							<el-option label="(" value="("></el-option>
							<el-option label="((" value="(("></el-option>
							<el-option label="(((" value="((("></el-option>
							<el-option label="空置" value="-1"></el-option>
						</el-select>
						<el-select v-show="index!=0" v-model="item.logical" placeholder="请选择" style="width: 200px;margin-right: 10px;">
							<el-option v-for="cont in logical" :key="cont.id" :label="cont.label" :value="cont.value">
							</el-option>
						</el-select>
						<el-select :value="getCn(item.fieldId,2)" placeholder="请选择" style="width: 200px;" @change="$emit('change',index)"
						 @focus="dataClick(index,item.fieldId)">
						</el-select>
						

						<ruleRelation v-model="item.operator" :value2.sync="item.fieldValue" :valueType="getvalueType(item.fieldId)"></ruleRelation>
						
						<!-- <el-input v-model="item.fieldValue" maxlength="30" placeholder="请输入内容,最长30位" style="width: 300px;margin-left: 10px;"
						 v-show="getvalueType(item.fieldId)!==3">
						</el-input>
						<el-select v-model="item.fieldValue" placeholder="请选择" style="width: 300px;margin-left: 10px;" v-show="getvalueType(item.fieldId)===3">
							<el-option label="是" value="1"></el-option>
							<el-option label="否" value="0"></el-option>
						</el-select> -->
					</div>

				</div>
				<div class="rule_home">
					<div class="rule_fa">
						<el-button icon="el-icon-plus" circle disabled='disabled'></el-button>
						<el-button icon="el-icon-close" circle disabled='disabled' style="margin-right: 10px"></el-button>
					</div>
					<el-select v-model="SpecialField.lastLogical" placeholder="请选择" style="width: 200px;">
						<el-option label=")" value=")"></el-option>
						<el-option label="))" value="))"></el-option>
						<el-option label=")))" value=")))"></el-option>
						<el-option label="空置" value="-1"></el-option>
					</el-select>
				</div>
			</div>
			
			输出：
			<slot></slot>
			<!-- <div class="rule_home">

				<div class="rule_fa">
					<el-button icon="el-icon-plus" circle disabled='disabled'></el-button>
					<el-button icon="el-icon-close" circle disabled='disabled' style="margin-right: 10px"></el-button>
				</div>


				<div style="display: flex;align-items: center;">
					<el-input value="命中动作:" maxlength="30" style="width: 200px;margin-right: 10px;" disabled></el-input>
					<p style="margin-right: 10px;">=</p>
					<el-select v-model="SpecialField.ruleAudit" placeholder="请选择" style="width: 200px;">
						<el-option label="终止决策流" :value="2"></el-option>
						<el-option label="继续决策流" :value="5"></el-option>
					</el-select>
				</div>

			</div> -->
			
			<div v-for="(value,index) in outcontent" class="rule_home">

				<div class="rule_fa">
					<el-button icon="el-icon-plus" circle @click="$emit('outAdd',index+1)"></el-button>
					<el-button icon="el-icon-close" circle @click="$emit('outDelect',index)" style="margin-right: 10px"></el-button>
				</div>


				<div style="display: flex;align-items: center;">
					<el-select :value="getCn(value.fieldId)" placeholder="请选择" style="width: 200px;margin-right: 10px;" @focus="outClick(index)">
					</el-select>
					<p style="margin-right: 10px;">=</p>
					<div style="display: flex;">
						<div style="font-size: 14px;padding-right: 5px;width: 60px;height: 40px;background-color: #d4d4d4;border-radius: 4px;line-height: 40px;color: #fff;text-align: center;">
							<!-- <el-select v-model="value.variableType" placeholder="请选择" @change="value.fieldValue=''">
								<el-option label="常量" value="1"></el-option>
								<el-option label="变量" value="2"></el-option>
							</el-select> -->




							<el-dropdown trigger="click" @command="value.variableType=$event;value.fieldValue=''">
								<span class="el-dropdown-link" style="color: #fff;">
									{{value.variableType==1?"常量":"变量"}}<i class="el-icon-arrow-down el-icon--right"></i>
								</span>
								<el-dropdown-menu slot="dropdown">
									<el-dropdown-item icon="el-icon-caret-right" :command="1">常量</el-dropdown-item>
									<el-dropdown-item icon="el-icon-caret-right" :command="2">变量</el-dropdown-item>
								</el-dropdown-menu>
							</el-dropdown>




						</div>

						<el-input v-show="value.variableType==1" v-model="value.fieldValue" maxlength="30" style="width: 200px;margin-right:10px;margin-left: -5px;"></el-input>
						<!-- {{Number(value.fieldValue.split('|')[0])}} -->
						<el-select v-show="value.variableType==2" filterable v-model="value.fieldValue" placeholder="请选择" style="width: 200px;margin-right:10px;margin-left: -5px;">
							<el-option v-for="item in FieldUser" :label="item.fieldCn" :value="Number(item.id)"></el-option>
						</el-select>
					</div>
				</div>
			</div>
			<el-dialog title="输入参数" :visible.sync="dialogVisible" width="50%">

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
		</div>
	</div>




</template>

<script>
	import ruleRelation from '@/components/common/ruleRelation.vue'
	export default {
		components: {
			ruleRelation
		},
		data() {
			return {
				search: '',
				tempcur: '',

				radioList: [],
				radio: '',
				dialogVisible: false,
				logical: [{
					value: '&&',
					label: 'and',
				}, {
					value: '||',
					label: 'or',
				}, {
					value: '(',
					label: '(',
				}, {
					value: '((',
					label: '((',
				}, {
					value: '(((',
					label: '(((',
				}, {
					value: ')',
					label: ')',
				}, {
					value: '))',
					label: '))',
				}, {
					value: ')))',
					label: ')))',
				}, {
					value: '&&(',
					label: 'and (',
				}, {
					value: '&&((',
					label: 'and ((',
				}, {
					value: '&&(((',
					label: 'and (((',
				}, {
					value: ')&&',
					label: ') and',
				}, {
					value: '))&&',
					label: '))and',
				}, {
					value: ')))&&',
					label: ')))and',
				}, {
					value: ')&&(',
					label: ')and(',
				}, {
					value: '))&&',
					label: '))and',
				}, {
					value: ')&&((',
					label: ') and ((',
				}, {
					value: ')&&(((',
					label: ') and (((',
				}, {
					value: '))&&(',
					label: ')) and (',
				}, {
					value: '))&&((',
					label: ')) and ((',
				}, {
					value: '))&&(((',
					label: ')) and (((',
				}, {
					value: ')))&&(',
					label: '))) and (',
				}, {
					value: ')))&&((',
					label: '))) and ((',
				}, {
					value: ')))&&(((',
					label: '))) and (((',
				}, {
					value: '||(',
					label: 'or (',
				}, {
					value: '||((',
					label: 'or ((',
				}, {
					value: '||(((',
					label: 'or(((',
				}, {
					value: ')||',
					label: ') or ',
				}, {
					value: '))||',
					label: ')) or ',
				}, {
					value: ')))||',
					label: '))) or ',
				}, {
					value: ')||(',
					label: ' ) or (',
				}, {
					value: ')||((',
					label: ' ) or (( ',
				}, {
					value: '))||(',
					label: ')) or ( ',
				}, {
					value: '))||((',
					label: ')) or (( ',
				}, {
					value: '))||(((',
					label: ')) or (((',
				}, {
					value: ')))||(',
					label: ' ))) or (',
				}, {
					value: ')))||((',
					label: '))) or ((',
				}, {
					value: ')))||(((',
					label: '))) or (((',
				}, {
					value: '-1',
					label: '空置',
				}]
			}
		},
		created() {
			console.log(this.Data)
		},
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
		props: {
			type: {
				type: Number,
				default: 1
			},
			Data: {
				type: Array,
				default () {
					return []
				}
			},
			SpecialField: {
				type: Object,
				default () {
					return {}
				}
			},
			outcontent: {
				type: Array,
				default () {
					return []
				}
			}
		},
		methods: {
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
								value.fieldEn = this.mixinGetvalueEn(this.radio)
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

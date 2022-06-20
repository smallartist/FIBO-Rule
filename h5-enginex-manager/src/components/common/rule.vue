<style>
	.rule_home {
		display: flex;
		
	}

	.rule_fa {
		display: flex;
		width: 20%;
		height: 30%;
		margin: 10px 10px 0 0;
	}
	
	.rule_son {
		display: flex;
		margin-top: 10px;
		justify-content: flex-start;
	}
</style>

<template>
	<div>
		<div v-if="Data&&FieldUser">
			<div v-for="(item,index) in Data" class="rule_home">
				<div class="rule_fa">
					<el-button icon="el-icon-plus" circle @click="$emit('faadd',index)"></el-button>
					<el-button icon="el-icon-close" circle @click="$emit('fadelect',index)" :disabled="index===0?'disabled':false"  style="margin-right: 10px;"></el-button>
					<el-input v-model="item.conditionValue" style="width: 200px;" maxlength="20" placeholder="请输入内容"></el-input>
				</div>
				<div>
					<div v-for="(value,inde) in item.fieldSubCond" class="rule_son">
						<el-button icon="el-icon-plus" circle @click="$emit('sonadd',index,inde)" ></el-button>
						<el-button icon="el-icon-close" circle @click="$emit('sondelect',index,inde)" :disabled="inde===0?'disabled':false"  style="margin-right: 10px;"></el-button>
						<el-select v-model="value.fieldId" placeholder="请选择" filterable  style="width: 200px;" @change="$emit('change',index,inde)">
							<el-option v-for="cont in FieldUser.data.fieldList" :key="cont.id" :label="cont.fieldCn" :value="cont.id">
							</el-option>
						</el-select>
						<ruleRelation  v-model="value.operator" :value2.sync="value.fieldValue" :valueType="getvalueType(value.fieldId)" ></ruleRelation>
						<!-- <el-input v-model="value.fieldValue" maxlength="30" placeholder="请输入内容,最长30位" style="width: 300px;margin-left: 10px;" v-show="getvalueType(value.fieldId)!==3">
						</el-input>
						<el-select v-model="value.fieldValue" placeholder="请选择" style="width: 300px;margin-left: 10px;" v-show="getvalueType(value.fieldId)===3">
							<el-option label="是" value="1"></el-option>
							<el-option label="否" value="0"></el-option>
						</el-select> -->
						<el-select v-model="value.logical" placeholder="请选择" style="width: 100px;margin-left: 10px;" v-show="inde!==item.fieldSubCond.length-1">
							<el-option label="and" value="&&"></el-option>
							<el-option label="or" value="or"></el-option>
						</el-select>
					</div>
				</div>

			</div>
		</div>









	</div>




</template>

<script>
	import ruleRelation from '@/components/common/ruleRelation.vue'
	export default {
		components:{
			ruleRelation
		},
		data() {
			return {
				
			}
		},
		created() {
			console.log(this.Data)
		},
		computed: {
			FieldUser() {
				return this.$store.state.FieldUser
			}
		},
		props: {
			Data: {
				type: Array,
				default () {
					return []
				}
			}
		},
		methods:{
			getvalueType(cont){
				let num 
				this.FieldUser.data.fieldList.forEach(value=>{
					if(value.id===cont){
						num = value.valueType
					}
				})
				return num
			}
		}
	}
</script>

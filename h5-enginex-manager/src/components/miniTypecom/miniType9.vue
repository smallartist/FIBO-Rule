<template>
	<div>
		<div class="mask-wrapper" style="border-top: 1px solid #ddd;">
			<div>
				<p class="type_title">
					输入变量：
				</p>
			</div>
			<div v-for="(value,index) in snapshot.input" style="display: flex;align-items:center;margin-top: 5px;" :key="index">
				<el-input
					placeholder="请选择入参"
					v-model="value.field_name"
					size="mini"
					style="width:150px;">
					<i slot="suffix" class="el-input__icon el-icon-arrow-down"></i>
				</el-input>

				<p style="margin-left: 10px;">别名：</p>
				<el-input size="mini" v-model="value.asName" :placeholder="value.field_name" style="width: 200px;"></el-input>
				<i class="el-icon-close" style="font-size: 20px;color: #F56C6C;"></i>
			</div>
			<div>
				<p class="type_title">
					决策条件：
				</p>
			</div>
			<div class="type3_rule_home">
				<div v-for="(item,index) in snapshot.conditions" class="type3_fa" :key="index">					
					<p>决策条件:</p>
					<div v-for="(value,inde) in item.formula" style="display: flex; margin-bottom: 5px;align-items: center;transition: all 0.3s;" :key="inde">
						<!-- {{value}} -->
						<el-select v-model="value.field_code" filterable placeholder="请选择入参" size="mini" style="width: 150px;">
							<!-- <el-option v-for="cont in input" :label="cont.asName" :value="cont.field_code" :key="cont.fieldEn">
							</el-option> -->
						</el-select>
						<ruleRelation v-model="value.operator" :value2.sync="value.result" :valueType="mixinGetValueTypeByEn(value.field_code)" size="mini"></ruleRelation>

						<!-- <el-input v-model="value.operator" size="mini" style="width: 100px;margin-left: 10px;">
						</el-input> -->

						<!-- <el-input v-model="value.result" maxlength="30" placeholder="值" style="width: 100px;margin-left: 10px;" v-show="value.type!==3"
						size="mini">
						</el-input>
						<el-select v-model="value.result" placeholder="请选择" size="mini" style="width: 100px;margin-left: 10px;" v-show="value.type===3">
							<el-option label="是" value="=="></el-option>
							<el-option label="否" value="!="></el-option>
						</el-select> -->
						<el-select v-model="value.sign" placeholder="请选择" style="width: 100px;margin-left: 10px;" size="mini" v-show="inde!==item.formula.length-1">
							<el-option label="and" value="&&"></el-option>
							<el-option label="or" value="||"></el-option>
						</el-select>
						<i class="el-icon-plus" style="font-size: 20px;color: #409EFF;"></i>
						<i class="el-icon-close" style="font-size: 20px;color: #F56C6C;" v-show="inde!=0"></i>
					</div>
					决策结果: <el-input v-model="item.result" placeholder="请输入结果" size="mini" style="width: 200px;margin-top: 10px;"></el-input>
					<div class="type3_delect_fa">
						<i class="el-icon-close" style="font-size: 20px;color: #F56C6C;"></i>
					</div>
				</div>
				<div  class="type3_fa" >
					默认结果: <el-input v-model="snapshot.output.defaultValue" placeholder="请输入结果" size="mini" style="width: 200px;margin-top: 10px;"></el-input>
				</div>
			</div>
			<div>
				<p class="type_title">
					输出变量：
				</p>
			</div>
			<div>
				<el-select v-model="snapshot.output.field_name" filterable size="mini" style="width: 150px;">
					<!-- <el-option v-for="cont in Fielduser" :key="cont.id" :label="cont.fieldCn" :value="cont.id">
					</el-option> -->
				</el-select>
			</div>
			<!-- <div style="border-top: 1px solid #ddd;padding: 5px 0;font-size: 14px;">
				版本：
				<el-select :value="4" size='mini' disabled>
				</el-select>
			</div> -->
		</div>
	</div>
</template>

<script>
	import ruleRelation from '@/components/common/ruleRelation.vue'
	export default{
		components: {
			ruleRelation
		},
		props:{
			snapshot:{
				type:Object,
				default:{}
			},
			rowkey:{
				type:String,
				default:""
			}
		},
		created(){
			this.init();
		},
		data() {
			return {
				
			}
		},
		methods:{
			init(){
				
			}
		},
		computed:{
		}
	}
</script>

<style>
	.type3_fa{
		border: 1px solid #ddd;
		padding:5px;
		border-radius: 10px;
		font-size: 14px;
		position: relative;
		margin-top: 20px;
	}
	.type3_delect_fa{
		border: 1px solid #F56C6C;
		border-radius: 50%;
		padding: 1px;
		position: absolute;
		top: -10px;
		right: -5px;
		background-color: #f5e7e7;
	}
	.mask-wrapper{
		position: relative;
		z-index: 3;
	}
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
</style>

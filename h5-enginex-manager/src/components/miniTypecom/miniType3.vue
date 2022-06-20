<template>
	<div>
		<div style="border-top: 1px solid #ddd;padding: 5px ;">
			<div>
				<p class="type_title">
					分组设置：
				</p>
			</div>
			<div class="type3_rule_home mask-wrapper">
				<div v-for="(item,index) in snapshot.conditions" class="type3_fa" :key="index">
					分组名称: <el-input v-model="item.group_name" placeholder="请输入分组名" size="mini" style="width: 200px;"></el-input>

					<p>分组条件:</p>
					<div v-for="(value,inde) in item.formulas" style="display: flex; margin-top: 5px;align-items: center;transition: all 0.3s;" :key="inde">

						<el-input
							placeholder="请选择指标"
							:value="value.field_name?value.field_name:value.field_code"
							size="mini"
							style="width:150px;">
							<i slot="suffix" class="el-input__icon el-icon-arrow-down"></i>
						</el-input>
						<ruleRelation v-model="value.operator" :value2.sync="value.value" :valueType="value.valueType?value.valueType:1" size="mini"></ruleRelation>
						<!-- <el-input v-model="value.value" maxlength="30" placeholder="值" style="width: 100px;margin-left: 10px;" v-show="(value.valueType?value.valueType:1)!==3"
						size="mini">
						</el-input>
						<el-select v-model="value.value" placeholder="请选择" size="mini" style="width: 100px;margin-left: 10px;" v-show="(value.valueType?value.valueType:1)===3">
							<el-option label="是" value="="></el-option>
							<el-option label="否" value="!="></el-option>
						</el-select> -->
						<el-select v-model="value.relative_operator" placeholder="请选择" style="width: 100px;margin-left: 10px;" size="mini"
						v-show="inde!==item.formulas.length-1">
							<el-option label="and" value="&&"></el-option>
							<el-option label="or" value="or"></el-option>
						</el-select>
						<i class="el-icon-plus" style="font-size: 20px;color: #409EFF;"></i>
						<i class="el-icon-close" style="font-size: 20px;color: #F56C6C;" v-show="inde!=0"></i>
					</div>
					<div class="type3_delect_fa">
						<i class="el-icon-close" style="font-size: 20px;color: #F56C6C;"></i>
					</div>
				</div>
			</div>
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
		data() {
			return {
			}
		},
		methods:{
			
		},
	}
</script>

<style>
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

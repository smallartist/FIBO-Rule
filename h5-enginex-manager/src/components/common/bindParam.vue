<template>
	<div>
		<p style="margin-top: 20px;padding-top: 10px;display: flex;align-items: center;">
			变量：<i class="el-icon-circle-plus-outline" style="color:#409EFF;font-size: 24px;" @click="addVariable"></i>
		</p>

		<div v-for="(value,index) in Variable" style="display: flex;align-items: center;margin-top: 10px;">
			<el-input v-model="value.key" style="width: 200px;" placeholder="键" :size="size"></el-input>
			<p style="margin:10px;">:</p>
			<el-input v-model="value.value" style="width: 200px;" placeholder="默认值" :size="size"></el-input>
			<i class="el-icon-circle-close" :style="{color:'#F56C6C',fontSize: '24px',marginLeft:'10px'}"
				@click="delectVariable(index)"></i>
		</div>


		<p style="margin-top: 20px;display: flex;align-items: center;">
			字典变量：<i class="el-icon-circle-plus-outline" style="color:#409EFF;font-size: 24px;"
				@click="addDictVariable"></i></p>

		<div v-for="(value,index) in dictVariable" style="display: flex;align-items: center;margin-top: 10px;">
			<el-input v-model="value.key" style="width: 200px;" placeholder="键" :size="size"></el-input>
			<p style="margin:10px;">:</p>
			<el-select v-model="value.type" placeholder="请选择类型" :size="size">
				<el-option v-for="item in dictVariableType" :key="item.value" :label="item.label" :value="item.value">
				</el-option>
			</el-select>
			<el-input v-model="value.value" style="width: 200px;margin-left: 20px;" placeholder="格式" :size="size"></el-input>
			<i class="el-icon-circle-close" :style="{color:'#F56C6C',fontSize: '24px',marginLeft:'10px'}"
				@click="delectDictVariable(index)"></i>
		</div>
	</div>
</template>

<script>
	export default {
		props: {
			Variable: {
				type: Array,
				default () {
					return []
				}
			},
			dictVariable: {
				type: Array,
				default () {
					return []
				}
			},
			size:{
				type: String,
				default:''
			}
		},
		data() {
			return {
				dictVariableType: [{
					label: '时间',
					value: 'date'
				}]
			}
		},
		methods: {
			addVariable() {
				this.Variable.push({
					value: '',
					key: ''
				})
			},
			delectVariable(index) {
				this.Variable.splice(index, 1)
			},
			addDictVariable() {
				this.dictVariable.push({
					key: '',
					type: '',
					value: ''
				})
			},
			delectDictVariable(index) {
				
					this.dictVariable.splice(index, 1)
				
			},
		}





	}
</script>

<style>
</style>

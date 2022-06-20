<template>
	<div>

		<div v-for="(value,index) in list" style="width: 800px;display: flex;align-items: center;margin-bottom: 10px;">
			<el-input v-model="value.outputKey" placeholder="请输入Key" style="width: 160px;margin-right: 10px;">
			</el-input>
			:

			<el-select v-model="value.outputOp" clearable placeholder="请选择" style="width: 120px;margin-left: 10px;"
				@change="Opchange(value)">
				<el-option v-for="item in option" :key="item.value" :label="item.label" :value="item.value">
				</el-option>
			</el-select>

			<el-cascader v-model="value.outputOpKey" filterable :options="ValueObj" clearable placeholder="请选择计算依赖"
				v-if="value.outputOp!='custom'&&value.outputOp!='count'" :key="(value.random?value.random:0)" style="margin-left: 10px;"
				:props="{ expandTrigger: 'hover' }" @visible-change="randomAdd(value,$event)">
			</el-cascader>



			<varialeSelect v-if="value.outputOp=='custom'" :variableType.sync="value.variableType" :disabled="[2]"
				:variableCascaderValue="ValueObj" :variableCascader="true" size="medium" height="36px"
				v-model="value.outputValue" :interceptCustom="true" @CustomCallback="CustomCallback(value,list,index)"
				style="margin-left: 10px;">
			</varialeSelect>


			<i class="el-icon-circle-close" style="color: red;margin-left: 10px;" @click="deleteOutput(index)"></i>
		</div>
		<el-button @click="addOutput">+</el-button>



	</div>
</template>

<script>
	import varialeSelect from '@/components/models/varialeSelect.vue'
	export default {
		components: {
			varialeSelect
		},
		props: {
			ValueObj: {
				type: Array,
				default () {
					return []
				}
			},
			list: {
				type: Array,
				default () {
					return []
				}
			},
			option: {
				type: Array,
				default () {
					return []
				}
			},
		},
		methods: {
			CustomCallback(value,list,index) {
				
				let arr = JSON.parse(JSON.stringify(list))
				
				arr = list.filter((value,index2) =>index2<index)
				arr = arr.map(value => value.outputKey)
				value.arr = []
				value.arr = arr
				this.$emit('CustomCallback', value)
			},

			Opchange(value) {
				
					value.variableType = 1
					value.outputValue = ''
					value.outputOpKey = []

				
			},
			deleteOutput(index) {
				this.$confirm('确定删除?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.list.splice(index, 1)
					this.$message({
						type: 'success',
						message: '删除成功!'
					});
				})
			},
			addOutput() {
				this.list.push({
					outputKey: '',
					outputOp: '',
					variableType: 1,
					outputValue: '',
					outputOpKey: []
				})
			}
		}



	}
</script>

<style>
</style>

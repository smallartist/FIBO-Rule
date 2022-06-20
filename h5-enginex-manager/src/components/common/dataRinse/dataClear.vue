<template>
	<div>

		<div v-for="(value,index) in list" style="width: 800px;display: flex;align-items: center;margin-bottom: 10px;">


			<el-cascader v-model="value.outputKey" filterable :options="option" clearable placeholder="请选择属性"
				:key="(value.random?value.random:0)" style="margin-right: 10px;width: 300px;" :props="{ expandTrigger: 'hover' }"
				@visible-change="randomAdd(value,$event)">
			</el-cascader>


			=
			<varialeSelect :variableType.sync="value.variableType" :disabled="[2]" size="medium" height="36px"
				v-model="value.outputValue" style="margin-left: 10px;"  :interceptCustom="true" @CustomCallback="$emit('CustomCallback',value)">
			</varialeSelect>





			<i class="el-icon-circle-close" style="color: red;margin-left: 10px;" @click="deleteDataClear(index)"></i>
		</div>
		<el-button @click="addDataClear">+</el-button>







	</div>
</template>

<script>
	import varialeSelect from '@/components/models/varialeSelect.vue'
	export default {
		components: {
			varialeSelect
		},
		props: {
			list: {
				type: Array,
				default: []
			},
			option: {
				type: Array,
				default: []
			},
			filterType: {
				type: String,
				default: ''
			},
			
		},
		methods: {
			deleteDataClear(index) {
				this.list.splice(index, 1)
			},
			addDataClear() {
				this.list.push({
					variableType: 3,
					variableValue: '',
					operator: '==',
					filterType: this.filterType

				})
			}
		}
	}
</script>

<style>
</style>

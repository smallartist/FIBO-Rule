<template>
	<div style="display: inline;">
		<div style="display: flex;align-items: center;">
			<el-select v-model="data" :size="size" :filterable="type==2?'filterable':false" placeholder="请选择"
				ref="select" style="width: 100px;margin-left: 10px;" @change="change" @blur="$emit('blur')">
				<el-option label="大于" value=">" v-show="[2,3,5,6].indexOf(valueType)==-1"></el-option>
				<el-option label="大于等于" value=">=" v-show="[2,3,5,6].indexOf(valueType)==-1"></el-option>
				<el-option label="等于" value="==" v-show="[5].indexOf(valueType)==-1"></el-option>
				<el-option label="小于" value="<" v-show="[2,3,5,6].indexOf(valueType)==-1"></el-option>
				<el-option label="小于等于" value="<=" v-show="[2,3,5,6].indexOf(valueType)==-1"></el-option>
				<el-option label="不等于" value="!=" v-show="[5].indexOf(valueType)==-1"></el-option>
				<el-option label="包含" value="contains" v-show="[2,5,6].indexOf(valueType)!=-1"></el-option>
				<el-option label="不包含" value="not contains" v-show="[2,5,6].indexOf(valueType)!=-1"></el-option>
				<el-option label="为空" value="is empty" v-show="[6].indexOf(valueType)!=-1"></el-option>
				<el-option label="不为空" value="not empty" v-show="[6].indexOf(valueType)!=-1"></el-option>
				<el-option label="正则匹配" value="regex" v-show="[2].indexOf(valueType)!=-1"></el-option>
			</el-select>

			<el-input :value="value2" @input="$emit('update:value2',$event)" maxlength="30" :size="size"
				placeholder="请输入内容,最长30位" style="width: 100px;margin-left: 10px;"
				v-if="!variableType&&openValue2&&valueType!==3&&['is empty','not empty'].indexOf(data)==-1">
			</el-input>
			<el-select :value="value2" @input="$emit('update:value2',$event)" placeholder="请选择" :size="size"
				style="width: 100px;margin-left: 10px;" v-if="!variableType&&openValue2&&valueType===3">
				<el-option label="是" value="="></el-option>
				<el-option label="否" value="!="></el-option>
			</el-select>
			<varialeSelect v-if="variableType"  :valueType="valueType"
				:disabled="variableDisList" :variableType="variableType"
				@update:variableType="$emit('update:variableType',$event)" :value="value2"
				@input="$emit('update:value2',$event)" v-bind="$attrs" :interceptCustom="interceptCustom" @CustomCallback="$emit('CustomCallback',$event)" style="margin-left: 10px;"></varialeSelect>
		</div>
	</div>
</template>

<script>
	import varialeSelect from '@/components/models/varialeSelect.vue'
	export default {
		components: {
			varialeSelect
		},
		props: {
			openValue2: { //是否打开后半截输入框 
				type: Boolean,
				default: true
			},
			value2: { //后半截输入框的Key
				type: String,
				default: ''
			},
			type: { //是否打开搜索
				type: Number,
				default: 1
			},
			openSelect: { //是否自动打开下拉选择框
				type: Boolean,
				default: false
			},
			value: { //大于小于等信息
				type: String,
				default: ''
			},
			valueType: { //数字 或者字符串 或者 JSON
				type: Number,
				default: 1
			},
			size: { //大小
				type: String,
				default: ''
			},
			variableType: { //是否打开 常量变量自定义 以及默认为哪个 0为不打开
				type: Number,
				default: 0
			},
			variableDis: { //禁用常量变量自定义
				type: Array || null,
				default: null
			},
			interceptCustom:{
				type:Boolean,
				default: false
			}
		},
		data() {
			return {
				data: ''
			}
		},
		created() {
			this.data = this.value
			// console.log(this.$listeners)
		},
		methods: {
			change() {
				this.$emit('change')
				if (['is empty', 'not empty'].indexOf(this.data) == -1) {
					if (this.data == 'regex') {
						this.$emit('update:value2', '')
					} else {
						this.$emit('update:value2', '0')
					}
				}
				if (this.data == 'regex') { //如果选择正则匹配 则只能为变量
					this.$emit('update:variableType', 1)
				}


				this.$emit('input', this.data)
			}
		},
		computed: {
			variableDisList() {

				let arr = []
				if (this.variableDis) {

					arr.push(...this.variableDis)
				}
				if (this.data == 'regex') {
					arr.push(...[2, 3])
				}
				arr = Array.from(new Set(arr))
				return arr
			}
		},
		watch: {
			value() {
				this.data = this.value
			},
			openSelect() {
				if (this.openSelect) {
					this.$nextTick(() => {
						this.$refs.select.focus()
					})


				}
			}
		}
	}
</script>

<style>
</style>

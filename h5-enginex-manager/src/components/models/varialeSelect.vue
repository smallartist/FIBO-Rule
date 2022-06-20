<template>
	<div class="variateSelect" :style="{width:width,height:height}">
		<div style="width: 35%;" class="variateSelectLeft" :style="{height:height}">
			<el-dropdown trigger="click" @command="em">
				<span class="el-dropdown-link" style="color: #fff;">
					{{variableType==1?"常量":variableType==2?"变量":variableType==3?"自定义":"错误"}}<i
						class="el-icon-arrow-down el-icon--right"></i>
				</span>
				<el-dropdown-menu slot="dropdown">
					<el-dropdown-item icon="el-icon-caret-right" :command="1" :disabled="disabled.indexOf(1)!=-1">常量
					</el-dropdown-item>
					<el-dropdown-item icon="el-icon-caret-right" :command="2" :disabled="disabled.indexOf(2)!=-1">变量
					</el-dropdown-item>
					<el-dropdown-item icon="el-icon-caret-right" :command="3" :disabled="disabled.indexOf(3)!=-1">自定义
					</el-dropdown-item>
				</el-dropdown-menu>
			</el-dropdown>
		</div>

		<!-- 常量 -->
		<el-input style="margin-left: -10%;width: 75%;" :value="value" :size="size" placeholder="请输入内容"
			v-if="variableType===1" @input="$emit('input',$event)"></el-input>


		<!-- 变量 -->
		<el-select style="margin-left: -10%;width: 75%;" :size="size" :value="value" filterable
			@change="$emit('input',$event)" v-else-if="variableType===2&&!variableCascader">
			<el-option v-for="item in MyFieldUser" :key="item.id" :label="item.fieldCn" :value="item.fieldEn">
			</el-option>
		</el-select>

		<!-- 变量级联 -->

		<el-cascader style="margin-left: -10%;width: 75%;" :value="value" :size="size"
			v-else-if="variableType===2&&variableCascader" filterable :options="FieldUserObj"
			:key="(value.random?value.random:0)" @visible-change="randomAdd(value,$event)"
			@change="$emit('input',$event)" :props="{ expandTrigger: 'hover' }"></el-cascader>

		<!-- 自定义按钮 -->
		<el-button plain @click="CustomClick" v-else-if="variableType===3" :style="{height:height}"
			class="varialeType3Button" :size="size">
			{{value==""||JSON.parse(value).formula===""?'请输入自定义内容':JSON.parse(value).formula}}
		</el-button>
		<!-- 自定义的弹框 -->
		<el-dialog title="自定义" v-if="dialogVisible" :visible.sync="dialogVisible" width="70%"
			:close-on-click-modal="false" append-to-body>
			<teV2 v-model="tempValue" ></teV2>
			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogVisible=false;tempValue=''">取 消</el-button>
				<el-button type="primary" @click="sectionSure" :disabled="type3Sure">确 定</el-button>
			</span>
		</el-dialog>
	</div>
</template>

<script>
	import teV2 from '@/components/common/teV2.vue'
	export default {
		components: {
			teV2
		},
		data() {
			return {
				dialogVisible: false,
				tempValue: ''
			}
		},
		props: {
			size: {
				type: String,
				default: 'mini'
			},
			variableType: {
				type: Number,
				default: 1
			},
			value: {
				type: String | Array,
				default: ""
			},
			width: {
				type: String,
				default: '300px'
			},
			height: {
				type: String,
				default: '28px'
			},
			disabled: {
				type: Array,
				default () {
					return []
				}
			},
			valueType: {
				type: Number,
				default: 0
			},
			interceptCustom: { //是否拦截自定义点击
				type: Boolean,
				default: false
			},
			variableCascader: { // 是否使用变量级联
				type: Boolean,
				default: false
			},
			variableCascaderValue: { //变量级联值
				type: Array,
				default: undefined
			},

		},
		created() {
			// console.log(this.variableType)
			if (!this.variableType) {
				this.$emit('update:variableType', 1)
			}
		},
		methods: {

			CustomClick() {
				if (this.interceptCustom) {
					this.$emit('CustomCallback', this.value)
					return
				}
				this.tempValue = this.value === '' ? this.value : JSON.parse(this.value).formula;
				this.dialogVisible = true;
			},
			sectionSure(e) {
				let arr = String(this.tempValue).match(/@(.|\n)*?@/g) === null ? [] : this.tempValue.match(/@(.|\n)*?@/g);
				let is = false
				// console.log(arr,this.tempValue)
				arr = Array.from(new Set(arr))
				// console.log(arr)
				arr = arr.map(value => {
					console.log(value)
					value = value.split('')
					value.pop()
					value.shift()
					value = value.join('')



					let filed = this.mixinGetValueByCn(value)
					if (filed) {
						return filed
					} else {
						is = true
						this.$message.error(`没有找到指标:${value}`);
						return filed
					}


				})
				// console.log(arr)
				// console.log(e)
				if (is) {
					return
				}
				let obj = {
					farr: arr,
					formula: this.tempValue.trim()
				}


				// console.log(this.tempValue)
				this.$emit('input', JSON.stringify(obj))
				this.dialogVisible = false
			},
			em(e) {
				this.$emit('update:variableType', e)
				this.$emit('input', "")
			},
			isodd(text, str) { // 判断某个字符串中 某个字符是否是奇数
				let num = 0
				for (let i of text) {
					if (i === str) {
						num++
					}
				}
				if (num % 2 === 1) {
					return true
				} else {
					return false
				}
			}
		},
		computed: {
			FieldUserObj() {

				if (this.variableCascaderValue === undefined) {

					if (this.$store.state.FieldUserObj) {
						return this.$store.state.FieldUserObj.data.fieldList
					} else {
						return []
					}
				}
				return this.variableCascaderValue

			},
			MyFieldUser(e) {
				if (this.valueType) {
					return this.FieldUser.filter(x => x.valueType == this.valueType)
				} else {
					return this.FieldUser
				}
			},
			type3Sure() {
				return this.isodd(this.tempValue, '@')
			}
		}
	}
</script>

<style>
	.variateSelect {

		display: flex;
		align-items: center;
		justify-content: center;
	}

	.variateSelectLeft {
		background-color: #ddd;
		border-radius: 5px;
		padding-right: 10px;
		display: flex;
		align-items: center;
		padding-left: 6px;
		padding-right: 30px;
		justify-content: center;
	}

	.varialeType3Button {
		flex-shrink: 0;
		flex-grow: 1;
		margin-left: -10%;
		width: 75%;
		overflow: hidden;
	}
</style>

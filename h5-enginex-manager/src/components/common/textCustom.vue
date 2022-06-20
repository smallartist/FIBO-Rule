<template>
	<div style="width: 100%;margin-left: 5px;">
		<div class="textInput" v-if="click=='dbl'" @dblclick="dialogVisibleOpen" :style="{'color':color,textAlign:center?'center':'left'}">{{value.trim()===""||JSON.parse(value).formula.trim()==""?'-':JSON.parse(value).formula}}</div>


		<el-dialog title="自定义" v-if="dialogVisible" :visible.sync="dialogVisible" width="30%" append-to-body>
			<teV2 v-model="tempValue"></teV2>
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
		props: {
			center: {
				type: Boolean,
				default: false
			},
			value: {
				type: String,
				default: ''
			},
			click: {
				type: String,
				default: 'dbl'
			}
		},
		data() {
			return {
				color: '#000',
				dialogVisible: false,
				tempValue: ''
			}
		},
		created() {

		},
		mounted() {

		},


		methods: {
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
			},
			dialogVisibleOpen() {
				// console.log('this.value',this.value)
				this.tempValue = this.value==""?this.value:JSON.parse(this.value).formula
				this.dialogVisible = true
			},
			sectionSure(e) {
				// console.log(this.tempValue)
				let arr = String(this.tempValue).match(/@(.|\n)*?@/g) === null ? [] : this.tempValue.match(/@(.|\n)*?@/g);
				let is = false
				arr = Array.from(new Set(arr))
				// console.log(arr)
				arr = arr.map(value => {
					// console.log(value)
					value = value.split('')
					value.pop()
					value.shift()
					value = value.join('')
					if (this.mixinGetValueByCn(value)) {
						return this.mixinGetValueByCn(value)
					} else {
						is = true
						this.$message.error(`没有找到指标:${value}`);
						return this.mixinGetValueByCn(value)
					}

				})
				if (is) {
					return
				}
				let obj = {
					farr: arr,
					formula: this.tempValue.trim()
				}
				if(this.tempValue.trim()===""){
					this.color = '#ee0000'
				}else{
					
					this.color = '#009500'
				}

				this.$emit('input', JSON.stringify(obj))
				this.dialogVisible = false
			},

		},
		watch: {

		},
		computed: {
			type3Sure() {
				return this.isodd(this.tempValue, '@')
			}
		}
	}
</script>

<style>
	.textInput {

		width: 100% !important;
		table-layout: fixed !important;
		word-wrap: break-all !important;
		word-break: normal !important;
		overflow: hidden !important;
		text-align: left;
		font-size: 14px;
		text-overflow: ellipsis;
		white-space: nowrap;
	}
</style>

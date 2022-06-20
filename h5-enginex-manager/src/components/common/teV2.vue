<style>
	.te_top {
		width: 100%;
		height: 30px;
		background-color: #aaa;
		display: flex;
		justify-content: space-around;

	}

	.te_top>p {
		color: #eee;
		font-weight: bold;
		font-size: 16px;
		line-height: 30px;
	}

	.te_top>p:hover {
		color: #fff;
		cursor: pointer
	}
</style>

<template>
	<div style="width: 100%;">
		
		<p style="font-size:14px;margin-bottom: 5px;color: #aaa;">
			‘@’字符选择变量
		</p>
		<slot></slot>
		<div class="te_top" v-if="hint==true">
			<p @click="textareaAdd('+')">+</p>
			<p @click="textareaAdd('-')">-</p>
			<p @click="textareaAdd('*')">*</p>
			<p @click="textareaAdd('/')">/</p>
			<p @click="textareaAdd('sqrt(,)')">sqrt</p>
			<p @click="textareaAdd('In(,)')">In</p>
			<p @click="textareaAdd('avg(,)')">avg()</p>
			<p @click="textareaAdd('(,)')">()</p>
			<p @click="textareaAdd('abs(,)')">abs</p>
			<p @click="textareaAdd('max(,)')">max</p>
			<p @click="textareaAdd('min(,)')">min</p>
			<p @click="textareaAdd('lg(,)')">lg</p>
			<p @click="textareaAdd('exp(,)')">exp</p>
			<p @click="textareaAdd('ceil(,)')">ceil</p>
			<p @click="textareaAdd('floor(,)')">floor</p>
		</div>
		<el-input type="textarea" rows="9" placeholder="请输入内容" v-model="formula_show" ref="textarea"
			@input="$emit('input',formula_show)">
		</el-input>
		<fieldUserTable :text="text" :fieldUser="FieldUser" refs="t" @dbc="dbclick" :show="isshow"
			@close="isshow=false"></fieldUserTable>
		<!-- <el-dialog :title="'当前编辑'+nowCurr" :visible.sync="dialogVisible" width="30%" append-to-body>
			<el-button @click="delect">删除字段</el-button>
		</el-dialog> -->

	</div>
</template>

<script>
	import fieldUserTable from '@/components/common/fieldUserTable.vue'

	import bus from './SCO/bus.js'
	export default {
		components: {
			fieldUserTable
		},
		props: {
			data: {
				type: Object,
				default () {
					return {}
				}
			},
			value: {
				type: String,
				default: ''
			},
			hint:{
				type:Boolean,
				default:false
			},
			
		},
		data() {
			return {
				loading: false,
				tempsection: [],
				sectionVisible: false, //区间编辑弹窗
				nowCurr: '',
				dialogVisible: false,
				isshow: false,
				text: '',
				tempIndex: '',
				lest: '',
				islest: false,
				tempFormula: '',
				formula_show: '',
				cursorfront: '',
				cursorlest: '',
				lastKeyDown: '',
				// fields: []
			}
		},
		mounted() {
			this.$refs.textarea.$refs.textarea.onkeydown = (e) => {


				this.cursorfront = this.formula_show.substring(0, this.$refs.textarea.$refs.textarea.selectionStart)
				this.cursorlest = this.formula_show.substring(this.$refs.textarea.$refs.textarea.selectionStart)
				if (e.key == "Backspace" || e.key == "Delete") {
					this.lastKeyDown = e.key

					if (e.key == "Backspace" && this.formula_show.substring(this.$refs.textarea.$refs.textarea
							.selectionStart - 1,
							this.$refs.textarea.$refs.textarea.selectionStart) == '@' && (this.isodd(this.formula_show,
							'@') == false)) {
						e.preventDefault()
						this.countCurr()
						this.delect("Backspace")
					}
					if (e.key == "Delete" && this.formula_show.substring(this.$refs.textarea.$refs.textarea
							.selectionStart, this.$refs
							.textarea.$refs.textarea.selectionStart + 1) == '@') {
						e.preventDefault()
						this.countCurr()
						this.delect("Delete")
					}

				}

			}

		},
		computed: {
			FieldUser() {
				return this.$store.state.FieldUser.data.fieldList
			},
		},
		created() {
			this.formula_show = this.value 
			
			// console.log(this.formula_show, this.value)
			bus.$on('getOk', (e) => {
				this.formula_show = e.formula_show
				this.tempFormula = e.formula_show

			})
		},
		methods: {
			setCaretPosition(ctrl, pos) { //设置光标位置函数
				if (ctrl.setSelectionRange) {
					ctrl.focus();
					this.$nextTick(() => {
						ctrl.setSelectionRange(pos, pos);
					})
				} else if (ctrl.createTextRange) {
					var range = ctrl.createTextRange();
					range.collapse(true);
					range.moveEnd('character', pos);
					range.moveStart('character', pos);
					range.select();
				}
			},
			textareaAdd(e) {
				this.cursorfront = this.formula_show.substring(0, this.$refs.textarea.$refs.textarea
					.selectionStart)
				this.cursorlest = this.formula_show.substring(this.$refs.textarea.$refs.textarea.selectionStart)

				if (e.split(',')[1]) {
					this.formula_show = this.cursorfront + e.split(',')[0] + e.split(',')[1] + this.cursorlest
				} else {
					this.formula_show = this.cursorfront + e.split(',')[0] + this.cursorlest
				}
				this.setCaretPosition(this.$refs.textarea.$refs.textarea, (this.cursorfront + e.split(',')[0])
					.length)
			},

			// 获取  字符串中 有 多少个指定字符
			getTempIndex(string, str) {
				let temp = 0
				for (let num = 0;;) {


					if (string.indexOf(str, temp) != -1) {
						temp = string.indexOf(str, temp)
						temp++
						num++
					} else {
						return num
					}
				}
				// console.log(string, str)
			},
			getTempArreyIndex(array, str) {
				let num = 0
				array.forEach((value) => {
					if (value.field_name == "str") {
						num++
					}
				})
				return num


			},
			countCurr() {
				if ((this.isodd(this.cursorfront, '@') == true) && (this.isodd(this.cursorlest, '@') == true)) {} else if (
					this.lastKeyDown == "Backspace") {
					this.cursorfront = this.cursorfront.substring(0, this.cursorfront.length - 1)
					this.cursorlest = '@' + this.cursorlest

				} else if (this.lastKeyDown == "Delete") {
					this.cursorfront = this.cursorfront + '@'
					this.cursorlest = this.cursorlest.substr(1);

				}
				let str = this.cursorfront.substring(this.cursorfront.lastIndexOf('@')) + this.cursorlest
					.substring(0, this.cursorlest
						.indexOf('@') + 1)

				this.nowCurr = str + '|' + this.getTempIndex(this.cursorfront, str)
				// console.log(this.nowCurr)

			},
			delect(type) {
				let str = this.nowCurr.split('|')[0]
				let index = this.nowCurr.split('|')[1]

				// this.cursorfront = this.formula_show.substring(0, this.$refs.textarea.$refs.textarea.selectionStart)
				// this.cursorlest = this.formula_show.substring(this.$refs.textarea.$refs.textarea.selectionStart)

				this.delectShow(str, parseInt(index))
				if(type=='Backspace'){
					
				this.setCaretPosition(this.$refs.textarea.$refs.textarea, (this.cursorfront.length-str.length+1))
				}else if(type=='Delete'){
					this.setCaretPosition(this.$refs.textarea.$refs.textarea, (this.cursorfront.length-1))
				}
				
				
				
				this.cursorfront = ""
				this.cursorlest = ""
				this.nowCurr = ""
				this.lastKeyDown = ""

			},

			delectShow(str, index) { // 回显删除
				
				this.formula_show = this.formula_show.substring(0, this.cursorfront.lastIndexOf('@')) + this
					.formula_show.substring(
						this.cursorfront.lastIndexOf('@') + str.length, )
				this.tempFormula = this.formula_show


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


			},
			dbclick(e) {
				this.cursorfront = this.formula_show.substring(0, this.$refs.textarea.$refs.textarea.selectionStart)
				// console.log(this.cursorfront)
				let T = ""
				T = this.formula_show.split("")
				T.splice(this.tempIndex, this.text.length + 1, '@' + e + '@')
				this.formula_show = T.join("")
				this.setCaretPosition(this.$refs.textarea.$refs.textarea, (this.cursorfront.length+e.length+1-this.text.length))
				this.isshow = false
				// console.log(1)
			},
			deepClone(obj) {
				if (this.getType(obj) === 'Array') {
					var res = []
				} else if (this.getType(obj) === 'Object') {
					var res = {}
				} else {
					return obj
				}
				for (var i in obj) {
					res[i] = this.deepClone(obj[i])
				}
				return res
			},
			getType(obj) {
				var res = Object.prototype.toString.call(obj)
				//截取注意[] 符号，和空格
				return res.slice(8, -1) // 也可以res.length-1
			}
		},
		watch: {
			formula_show() {
				let num = 0

				for (let i of this.formula_show) {
					if (i === "@") {
						num++
					}
				}
				if (num % 2 === 1) {
					this.isshow = true
					for (let i in this.formula_show) {
						if (this.formula_show[i] !== this.tempFormula[i] && this.formula_show[i] == "@") {

							if (this.islest) {
								this.tempIndex = i

								this.lest = this.formula_show.substring(parseInt(this.tempIndex) + 1, this
									.formula_show.length)
								this.islest = false
							}

							break
						}
					}

					let T = this.formula_show.substring(parseInt(this.tempIndex) + 1, this.formula_show.length)
					if (this.lest !== "") {
						// console.log(T)
						T = T.substring(0, T.indexOf(this.lest))

					} else {
						T = T.substring(0, T.length)
					}

					this.text = T
					if (this.text === "@") {
						this.text = ""
					}
					// console.log('lest:' + this.lest, 'index:' + this.tempIndex, "T:" + T)

				} else {
					this.islest = true
					this.text = ""
					this.tempIndex = null
					this.isshow = false
					// console.log(1)
				}
				this.tempFormula = this.formula_show
				this.data.formula_show = this.formula_show
				this.$emit('input', this.formula_show)
			},
			fields: {
				handler: function() {
					this.data.fields = this.fields
				},
				deep: true,
			},
			value(value){
				console.log(value)
				this.formula_show = this.value
			}
		}






	}
</script>

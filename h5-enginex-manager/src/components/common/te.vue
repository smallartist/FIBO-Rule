<style>
	.te_top{
	width: 100%;
	height: 30px;
	background-color: #aaa;
	display: flex;
	justify-content: space-around;
	user-select: none;
}
.te_top>p{
	color: #eee;
	font-weight: bold;
	font-size: 16px;
	line-height: 30px;
}
.te_top>p:hover{
	color: #fff;
	cursor:pointer 
}
</style>

<template>
	<div style="width: 100%;">

		<p style="font-size:14px;margin-bottom: 5px;color: #aaa;">
			‘@’字符选择变量
		</p>
		<div class="te_top">
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
		<el-input type="textarea" rows="9" placeholder="请输入内容" v-model="formula_show" ref="textarea" @input="$emit('input',formula_show)">
		</el-input>
		<fieldUserTable :text="text" :fieldUser="FieldUser" refs="t" @dbc="dbclick" :show="isshow" @close="isshow=false"></fieldUserTable>
		<el-dialog :title="'当前编辑'+nowCurr" :visible.sync="dialogVisible" width="30%" append-to-body>
			<el-button @click="delect">删除字段</el-button>
		</el-dialog>

	</div>
</template>

<script>
	import fieldUserTable from '@/components/common/fieldUserTable.vue'
	import {
		validateSection
	} from '@/api/index.js'
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
			}
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
				fields: []
			}
		},
		mounted() {
			this.$refs.textarea.$refs.textarea.onkeydown = (e) => {

				if (e.key == 'a') {
					if (e.ctrlKey) {
						e.preventDefault()
					}
				}
				this.cursorfront = this.formula_show.substring(0, this.$refs.textarea.$refs.textarea.selectionStart)
				this.cursorlest = this.formula_show.substring(this.$refs.textarea.$refs.textarea.selectionStart)
				if (e.key == "Backspace" || e.key == "Delete") {
					this.lastKeyDown = e.key

					if (e.key == "Backspace" && this.formula_show.substring(this.$refs.textarea.$refs.textarea.selectionStart - 1,
							this.$refs.textarea.$refs.textarea.selectionStart) == '@' && (this.isodd(this.formula_show, '@') == false)) {
						e.preventDefault()
						this.openDialog()
					}
					if (e.key == "Delete" && this.formula_show.substring(this.$refs.textarea.$refs.textarea.selectionStart, this.$refs
							.textarea.$refs.textarea.selectionStart + 1) == '@') {
						e.preventDefault()
						this.openDialog()
					}
				}
				if (((this.isodd(this.cursorfront, '@') == true) && (this.isodd(this.cursorlest, '@') == true)) || (this.dialogVisible ==
						true)) {

					e.preventDefault()
					this.openDialog()
				}
			}
			this.$refs.textarea.$refs.textarea.onclick = (e) => {
				this.cursorfront = this.formula_show.substring(0, this.$refs.textarea.$refs.textarea.selectionStart)
				this.cursorlest = this.formula_show.substring(this.$refs.textarea.$refs.textarea.selectionStart)

				if ((this.isodd(this.cursorfront, '@') == true) && (this.isodd(this.cursorlest, '@') == true) && this.formula_show
					.indexOf(
						'@') != -1) {
					this.openDialog()
				}

			}
			this.$refs.textarea.$refs.textarea.onfocus = () => {
				document.body.onmousemove = (e) => {
					e.preventDefault()
				}
			}
			this.$refs.textarea.$refs.textarea.onblur = () => {
				document.body.onmousemove = (e) => {

				}
			}

		},
		computed: {
			FieldUser() {
				return this.$store.state.FieldUser
			},
		},
		created() {
			this.formula_show = this.value
			console.log(this.formula_show,this.value)
			bus.$on('getOk', (e) => {
				this.formula_show = e.formula_show
				this.tempFormula = e.formula_show
				this.fields = this.deepClone(e.fields)
			})
		},
		methods: {
			setCaretPosition(ctrl, pos) { //设置光标位置函数
				if (ctrl.setSelectionRange) {
					ctrl.focus();
					this.$nextTick(()=>{
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
				this.cursorfront = this.formula_show.substring(0, this.$refs.textarea.$refs.textarea.selectionStart)
				this.cursorlest = this.formula_show.substring(this.$refs.textarea.$refs.textarea.selectionStart)
				
				if(e.split(',')[1]){
				this.formula_show = this.cursorfront + e.split(',')[0] + e.split(',')[1] + this.cursorlest
				}else{
				this.formula_show = this.cursorfront + e.split(',')[0]  + this.cursorlest
				}
				this.setCaretPosition(this.$refs.textarea.$refs.textarea,(this.cursorfront + e.split(',')[0]).length)
				
				// console.log(e, this.formula_show)
			},
			sectionSure() {
				this.loading = true
				let isNot = false
				let Z = /^(\[|\()\d*,\d*(\]|\))$/
				this.tempsection.forEach(value => {
					if (!Z.test(value.segment)) {
						isNot = true
					}


				})
				if (isNot) {
					this.$message.error('请检查区间格式');
					this.loading = false
					return
				}

				let arr = this.tempsection.map(value => {
					return value.segment
				})

				validateSection(arr).then(res => {
					if (res.status == "1") {
						if (res.data.result == "1") {
							this.$message({
								message: res.data.msg,
								type: 'success'
							});
							let isNotValue = false
							this.tempsection.forEach(value => {
								if (value.value == "") {

									isNotValue = true
								}
							})
							if (isNotValue) {

								setTimeout(() => {
									this.$message.error('值不能为空');
								}, 10)
								this.loading = false
								return
							}



							let ind = this.cursorfront.match(/@.*?@/g) === null ? [] : this.cursorfront.match(/@.*?@/g);
							this.fields[ind.length].segments = this.deepClone(this.tempsection)
							this.sectionVisible = false
							this.tempsection = []

						} else {
							this.$message.error(res.data.msg);
						}

					}
					this.loading = false
				}).catch(err => {
					this.loading = false
				})




			},
			delectSegment(index) {
				this.tempsection.splice(index, 1)
			},
			addSegment(index) {
				this.tempsection.splice(index, 0, {
					"segment": "",
					"value": ''
				})
			},
			// 打开区间编辑
			openSection() {
				let ind = this.cursorfront.match(/@.*?@/g) === null ? [] : this.cursorfront.match(/@.*?@/g);
				this.tempsection = this.deepClone(this.fields[ind.length].segments)
				// this.sectionVisible = true
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
				if ((this.isodd(this.cursorfront, '@') == true) && (this.isodd(this.cursorlest, '@') == true)) {
					// console.log(this.cursorfront.substring(this.cursorfront.lastIndexOf('@')))
					// console.log(this.cursorlest.substring(0,this.cursorlest.indexOf('@')+1))
					// console.log(this.cursorfront.lastIndexOf('@'),'上半句最后一个@')



				} else if (this.lastKeyDown == "Backspace") {
					this.cursorfront = this.cursorfront.substring(0, this.cursorfront.length - 1)
					this.cursorlest = '@' + this.cursorlest

				} else if (this.lastKeyDown == "Delete") {
					this.cursorfront = this.cursorfront + '@'
					this.cursorlest = this.cursorlest.substr(1);

				}
				let str = this.cursorfront.substring(this.cursorfront.lastIndexOf('@')) + this.cursorlest.substring(0, this.cursorlest
					.indexOf('@') + 1)
				// console.log(this.cursorfront, str)
				// console.log(this.formula_show.indexOf(str,1),'整句中str的位置（1）')
				this.nowCurr = str + '|' + this.getTempIndex(this.cursorfront, str)


			},
			openDialog() {
				this.countCurr()
				this.dialogVisible = true
				this.$refs.textarea.$refs.textarea.blur()

				// 数组添加
				if (this.getTempIndex(this.formula_show, this.nowCurr.split('|')[0]) > this.getTempArreyIndex(this.fields, this.nowCurr
						.split('|')[0].substring(1, this.nowCurr.split('|')[0].Length - 1))) {


					let ind = this.cursorfront.match(/@.*?@/g) === null ? [] : this.cursorfront.match(/@.*?@/g);
					let ind2 = this.formula_show.match(/@.*?@/g) === null ? [] : this.formula_show.match(/@.*?@/g);


					// console.log(this.fields, ind2, ind, ind2)
					let name = this.nowCurr.split('|')[0].substring(1, this.nowCurr.split('|')[0].length - 1)
					let obj = {}
					this.FieldUser.data.fieldList.forEach(value => {
						if (value.fieldCn === name) {
							obj = value
						}
					})
					if (typeof this.fields[ind.length] === 'object' && this.fields[ind.length].field_type != 0) {
						// console.log(this.fields[ind.length].length)
						if (this.fields.length < ind2.length) {

							// console.log(ind)

							this.fields.splice(ind.length, 0, {
								"field_id": obj.id,
								"field_code": obj.fieldEn,
								"field_name": name,
								"field_type": 1,
								"segments": [{
									"segment": "",
									"value": ''
								}]
							})
						} else {
							// this.fields[ind.length] = {
							// 	n: Math.random()
							// }
							// 开启数据回显
						}


					} else {

						// this.fields[ind.length] = {
						// 	"field_id": obj.id,
						// 	"field_code": obj.fieldEn,
						// 	"field_name": name,
						// 	"field_type": 1,
						// 	"segments": [{
						// 		"segment": "",
						// 		"value": ''
						// 	}]
						// }
						this.$set(this.fields, ind.length, {
							"field_id": obj.id,
							"field_code": obj.fieldEn,
							"field_name": name,
							"field_type": 1,
							"segments": [{
								"segment": "",
								"value": ''
							}]
						})
						for (let i = 0; i < ind.length; i++) {

							if (typeof this.fields[i] !== 'object') {
								// this.fields[i] = {
								// 	"field_id": 0,
								// 	"field_code": "",
								// 	"field_name": "",
								// 	"field_type": 0,
								// 	"segments": [{
								// 		"segment": "",
								// 		"value": ''
								// 	}]
								// }


								this.$set(this.fields, i, {
									"field_id": 0,
									"field_code": "",
									"field_name": "",
									"field_type": 0,
									"segments": [{
										"segment": "",
										"value": ''
									}]
								})
							}


						}
					}
					// console.log(this.fields)
				} else { //数组更新

				}







			},
			delect() {
				let str = this.nowCurr.split('|')[0]
				let index = this.nowCurr.split('|')[1]
				this.delectShow(str, parseInt(index))
				this.delectArray(str, parseInt(index))
				this.dialogVisible = false
				this.cursorfront = ""
				this.cursorlest = ""
				this.nowCurr = ""
				this.lastKeyDown = ""

			},

			delectShow(str, index) { // 回显删除

				// console.log(this.cursorfront.lastIndexOf('@'))
				this.formula_show = this.formula_show.substring(0, this.cursorfront.lastIndexOf('@')) + this.formula_show.substring(
					this.cursorfront.lastIndexOf('@') + str.length, )
				this.tempFormula = this.formula_show


			},
			delectArray(str, index) { // 数组删除
				let ind = this.cursorfront.match(/@.*?@/g) === null ? [] : this.cursorfront.match(/@.*?@/g);

				// console.log(ind)
				this.fields.splice(ind.length, 1)
				// console.log(this.fields)





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
				let T = ""
				T = this.formula_show.split("")
				T.splice(this.tempIndex, this.text.length + 1, '@' + e + '@')
				this.formula_show = T.join("")
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

								this.lest = this.formula_show.substring(parseInt(this.tempIndex) + 1, this.formula_show.length)
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
			}
		}






	}
</script>

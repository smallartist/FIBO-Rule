<template>
	<div style="width: 100%;margin-left: 5px;">
		<div class="textInput" v-if="click=='dbl'" @dblclick="openinput=true" :style="{'color':color,textAlign:center?'center':'left'}"
		 v-show="!openinput">{{mytext.trim()==""?'-':mytext}}</div>
		<div class="textInput" v-if="click=='click'" @click="openinput=true" :style="{'color':color,textAlign:center?'center':'left'}"
		 v-show="!openinput">{{mytext.trim()==""?'-':mytext}}</div>

		<el-input v-show="openinput" ref='input' maxlength="30" v-model="mytext" placeholder="请输入内容" size="mini" @input="input" @blur="openinput=false"></el-input>
	</div>
</template>
<script>
	export default {
		props: {
			center: {
				type: Boolean,
				default: false
			},
			text: {
				type: String,
				default: ''
			},
			type: {
				type: Number,
				default: 1
			},
			examine: {
				type: Number,
				default: 1 // 1为范围检查  2为数字检查
			},
			click: {
				type: String,
				default: 'dbl'
			},
			defcolor:{
				type: String,
				default: '#000'
			}
		},
		data() {
			return {
				color: '#000',
				openinput: false,
				mytext: ''
			}
		},
		created() {
			this.color = this.defcolor
			this.mytext = this.text ? this.text : this.text === 0 ? this.text : '-'
			if (this.text == null) {
				this.mytext = "-"
			}
		},
		mounted() {
			this.$refs.input.$refs.input.onkeydown = (e) => {
				if (e.key == 'Enter') {
					this.$refs.input.$refs.input.blur()
				}
			}
		},


		methods: {
			input(e) {
				this.$emit('input', e)
			},
			verify(type = 1) {
				if (!this.mytext && this.mytext != 0) {
					this.mytext = '-'
					this.color = '#f00'
				} else {
					if (this.examine == 1) {
						if (this.type == 1) {
							// let Z = /^(\[|\()(\d|\.)*,(\d|\.)*(\]|\))$/
							let Z = /^((\[|\()(\d(\d)*(\.(\d)+)?)|\(),((\d(\d)*(\.(\d)+)?(\]|\)))|\))$/
							if (!Z.test(this.mytext)) {
								if (type == 1) {
									this.$message.error('该字段为数值型 请填写范围区间  如:[123,456) 留空请使用 "(" ');
								}
								this.color = "#ee0000"

							} else {
								this.color = "#009500"
							}
						}
					} else if (this.examine == 2) {
						if (typeof Number(this.mytext) == 'number' && !isNaN(Number(this.mytext))) {


							this.color = "#009500"
						} else {
							if (type == 1) {
								this.$message.error('请填写数字');
							}
							this.color = "#ee0000"
						}
					} else if (this.examine == 3) {

						if (String(this.mytext).trim() === "") {
							this.color = "#ee0000"
						} else {

							this.color = "#009500"
						}
					}
				}
			}
		},
		watch: {
			text() {
				this.mytext = this.text
			},
			openinput() {
				if (this.openinput) {
					if (this.mytext == "-") {
						this.mytext = ""
					}
					this.$nextTick(() => {
						this.$refs.input.$refs.input.focus()
					})
				} else {
					this.verify()
				}

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

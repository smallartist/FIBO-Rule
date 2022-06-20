<style>
	.type {
		border-top: 1px solid #ddd;
		padding: 5px;
		box-sizing: border-box;
	}

	.type_header {
		font-size: 12px;
		font-weight: bold;
	}

	input[type=number] {
		-moz-appearance: textfield;
	}

	input[type=number]::-webkit-inner-spin-button,
	input[type=number]::-webkit-outer-spin-button {
		-webkit-appearance: none;
		margin: 0;
	}
</style>

<template>
	<div class="type4 type" style="height: 100%;">
		<div class="type_header">
			<p>节点类型:分流</p>
		</div>
		<div style="height: 80%;overflow: scroll;overflow-x: hidden;">
			<div v-for="(item,index) in shunt" style="display:flex;align-items: center;margin-top: 10px;">
				<p style="font-size: 12px;margin-right: 5px;">分流{{item.sandbox}}:</p>
				<div style="width: 100px;margin-right: 5px;">
					<el-input v-model="item.proportion" type='number' size="mini" placeholder="%"><i slot="suffix"
							style="font-size: 15px;">%</i></el-input>
				</div>
				<i class="el-icon-plus" style="color: #409EFF;font-size: 18px;" v-show="index>0&&index==shunt.length-1"
					@click="add(index)"></i>
				<i class="el-icon-close" style="color: #ff5500;font-size: 18px;" v-show="index>1"
					@click="delect(index)"></i>
			</div>
		</div>
		<div v-if="!readOnly">
			<el-button type="primary" round @click="submit">提交</el-button>
		</div>

	</div>
</template>

<script>
	import {
		setType4
	} from '@/api/index.js'
	export default {
		components: {
			setType4
		},
		data() {
			return {
				num: 0,
				shunt: '',
	
			}
		},
		created() {
			this.crea()
		},
		props: {
			
			data: {
				type: Object,
				default () {
					return {}
				}
			},
			nodeNexts: {
				type: Array,
				default () {
					return []
				}
			},
			readOnly: {
				type: Boolean,
				default: false
			}
		},
		methods: {
			submit() {
				let num = 0
				let havefu = false
				this.shunt.forEach(value => {
					num += parseInt(value.proportion)
					if (parseInt(value.proportion) < 0) {
						havefu = true
						this.$message.error('不允许负值')
					}
				})
				if (havefu) {
					return
				}
				if (num == 100) {
					console.log(this.nodeNexts)
					this.nodeNexts.forEach((value, index) => {
						if (this.shunt[index]&&this.shunt[index].proportion==value.proportion) {
							this.shunt[index].nextNode = value.nextNode
						}
					})
					this.setType({
						"id": this.data.id,
						"initEngineVersionId": String(this.data.Vid),
						"nodeType": 7,
						"nodeName": this.data.text,
						"nodeCode": this.data.nodeCode,
						"nodeOrder": this.data.nodeOrder,
						"nodeX": this.data.x,
						"nodeY": this.data.y,
						"params": JSON.stringify({
							"dataId": "2",
							"url": "/Riskmanage/resource/images/decision/createRiverRate.png",
							"type": "7"
						}),
						"nodeJson": JSON.stringify(this.shunt),
						"snapshot": JSON.stringify(this.shunt)
					}).then(res => {
						if (res.status == "1") {
						
							this.$emit('callback', JSON.stringify(this.shunt))
							console.log(this.shunt)
						
							this.$emit('setNextNodes', this.shunt)
							this.$message({
								message: '提交成功',
								type: 'success'
							});
						}
					})
				} else {
					this.$message({
						message: '请将数值和累加到 100 且 没有空值',
						type: 'warning'
					});
				}
			},
			add(index) {
				this.num += 1
				this.shunt.splice(index + 1, 0, {
					"proportion": "",
					"sandbox": this.num,
					"nextNode": ""
				})
			},
			delect(index) {
				if (this.nodeNexts[index] && this.nodeNexts[index].nextNode != '') {
					this.$message({
						message: '该分支有子节点，请清除后提交',
						type: 'warning'
					});
				} else {
					this.shunt.splice(index, 1)
				}
			},
			crea() {
				if (this.data.nodeJson == null) {
					let obj = [{
						"proportion": "",
						"sandbox": "1",
						"nextNode": ""
					}, {
						"proportion": "",
						"sandbox": "2",
						"nextNode": ""
					}]
					this.$emit('callback',JSON.stringify(obj) )
					this.shunt = obj
				} else {
					this.shunt = JSON.parse(this.data.nodeJson)
				}
				let num = 0
				this.shunt.forEach(value => {
					if (parseInt(value.sandbox) > num) {
						num = parseInt(value.sandbox)
					}
				})
				this.num = num
			},
			async setType(param) {
				const data = await setType4(param);
				return data
			},
		},
		watch: {

			'data.nodeNexts': {
				handler: function() {
					// this.datajson.conditions=this.nodeNexts
					// console.log(this.nodeNexts)
					// console.log(1)
					this.crea()
				},
				deep: true
			}
		}


	}
</script>

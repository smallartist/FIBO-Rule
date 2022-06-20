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
</style>

<template>
	<div class="type4 type" v-loading="loading">
		<div class="type_header">
			<p>节点类型:模型选择</p>
		</div>
		<div>
			<p
				style="font-size: 12px;font-weight: bold;margin-bottom: 10px;border-top: 1px solid #ddd;padding-top:5px ;">
				当前选择的模型:</p>
			<el-select v-model="value" filterable placeholder="请选择模型" clearable>
				<el-option v-for="item in options" :key="item.id" :label="item.modelName" :value="item.id">
				</el-option>
			</el-select>
		</div>
		<endRule :data="terminationInfo" :list="[valueObj]" :onlyOne="true" @reSetTerminationInfo="reSetTerminationInfo"
			:valueType="1">
			<el-option label="模型得分" :value="'15_'+data.id+'_terminal_result'"></el-option>

		</endRule>
		<div class="type3_submit_home"  v-if="!readOnly">
			<el-button type="primary" round @click="submit">提交</el-button>
		</div>

	</div>
</template>

<script>
	import {
		getType15,
		setType4
	} from '@/api/index.js'
	import endRule from '@/components/common/endRule.vue'
	import {
		endRuleVerification
	} from '@/utils/endRule.js'
	const terminarion = {
		output: {
			"fieldId": '',
			"fieldCode": "",
			"fieldName": "",
			"valueType": '',
			"variableType": 1,
			"fieldValue": ""
		},
		selectedRule: [],
		conditions: [{
			"fieldCode": "",
			"fieldName": "",
			"valueType": 1,
			"operator": "",
			"value": "",
			"relativeOperator": ""
		}]
	}
	export default {
		components: {
			getType15,
			setType4,
			endRule
		},
		data() {
			return {
				loading: false,
				value: '',
				options: [],
				terminationInfo: Object.assign({}, terminarion)
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
			readOnly:{
				type: Boolean,
				default :false
			}
		},
		methods: {
			crea() {
				this.loading = true
				this.terminationInfo = Object.assign({}, terminarion)
				this.value = ''
				this.getType({
					"pageNo": 1,
					"pageSize": 9999,
					"nodeId": this.data.id,
					"searchString": ""
				}).then(res => {
					if (res.status == "1") {
						this.options = res.data.modelsList
						if(this.data.nodeJson){
							let  nodeJson = JSON.parse(this.data.nodeJson)
							this.value = nodeJson.modelList[0].modelId
							this.terminationInfo = nodeJson.terminationInfo
						}
					}
					this.loading = false
				})
			},
			reSetTerminationInfo() {
				this.terminationInfo = Object.assign({}, terminarion)
			},
			submit() {
				if (endRuleVerification(this.terminationInfo)) {
					return
				}

				let subobj = {
					"modelList": [{
						"modelId": this.valueObj.id,
						"name":this.valueObj.modelName,
					}],
					"terminationInfo": this.terminationInfo
				}



				let obj = {
					"id": this.data.id,
					"initEngineVersionId": String(this.data.Vid),
					"nodeType": 15,
					"nodeName": this.data.text,
					"nodeCode": this.data.nodeCode,
					"nodeOrder": this.data.nodeOrder,
					"nodeX": this.data.x,
					"nodeY": this.data.y,
					"nodeJson": JSON.stringify(subobj),
					"snapshot": JSON.stringify(subobj),
					
					"params": JSON.stringify({
						"dataId": 12,
						"url": "/Riskmanage/resource/images/decision/models.png",
						"type": 15
					})
				}
				this.setType(obj).then(res => {
					if (res.status == "1") {
						this.$message.success('提交成功')
						this.$emit('callback',JSON.stringify(subobj))
					}
				})
			},
			async getType(param) {
				const data = await getType15(param);
				return data
			},
			async setType(param) {
				const data = await setType4(param);
				return data
			},
		},
		watch: {
			data() {
				this.crea()
			}
		},
		computed: {
			valueObj() {
				return this.options.find(x => x.id == this.value)
			}
		}


	}
</script>

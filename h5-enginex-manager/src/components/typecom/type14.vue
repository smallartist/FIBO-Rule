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
			<p>节点类型:子引擎</p>
		</div>
		<div>
			<p style="font-size: 12px;font-weight: bold;margin-bottom: 10px;border-top: 1px solid #ddd;padding-top:5px ;">当前选择的模型:</p>
			<el-select v-model="value" filterable placeholder="请选择子引擎" @change="change" clearable>
				<el-option v-for="item in options" :key="item.id" :label="item.name" :value="item.id">
				</el-option>
			</el-select>
		</div>

	</div>
</template>

<script>
	import {
		getType14,
		setType4
	} from '@/api/index.js'
	export default {
		components: {
			getType14,
			setType4
		},
		data() {
			return {
				loading: false,
				value: '',
				// options: []
			}
		},
		created() {
			this.crea()
			console.log(this.data)
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
				// this.loading = true
				this.value = ''
				// this.getType({
				// 	"pageNo": 1,
				// 	"pageSize": 9999,
				// 	"nodeId": this.data.id,
				// 	"searchString": ""
				// }).then(res => {
				// 	if (res.status == "1") {
				// 		this.options = res.data.engineList
						this.value = this.data.nodeJson?parseInt(this.data.nodeJson):''
				// 	}
				// 	this.loading = false
				// })
			},
			change() {
				let obj = {
					"id": this.data.id,
					"initEngineVersionId":String(this.data.Vid),
					"nodeType": 14,
					"nodeName": this.data.text,
					"nodeCode": this.data.nodeCode,
					"nodeOrder": this.data.nodeOrder,
					"nodeX":  this.data.x,
					"nodeY": this.data.y,
					"nodeJson": String(this.value),
					"params": JSON.stringify({
						"dataId": 11,
						"url": "/Riskmanage/resource/images/decision/childEngine.png",
						"type": 14
					})
				}
				this.setType(obj).then(res=>{
					if(res.status=="1"){
						this.$emit('callback',this.value)
					}
				})
			},
			async getType(param) {
				const data = await getType14(param);
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
		computed:{
			options(){
				return this.$store.state.Engine?this.$store.state.Engine:[]
			}
		}


	}
</script>

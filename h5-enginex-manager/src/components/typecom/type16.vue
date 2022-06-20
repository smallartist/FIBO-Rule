<style>
	.type {
		border-top: 1px solid #ddd;
		padding: 5px;
		box-sizing: border-box;
	}

	.type_header {
		font-size: 12px;
		font-weight: bold;
		margin-bottom: 5px;
	}
</style>

<template>
	<div class="type4 type" v-loading="loading">
		<div class="type_header">
			<p>节点类型:决策表</p>

		</div>
		<div style="border-top: 1px solid #ddd;padding: 5px ;">
			<p style="margin-top: 10px; margin-bottom: 10px;font-size: 14px;font-weight: bold;">当前选择的决策表：</p>
			<div style="margin-bottom: 10px;">
				<p v-if="checked!=null" style="color: #409EFF;">{{checked.name}}</p>
				<p v-if="checked==null" style="color: #999;">暂未选择决策表</p>
			</div>
			<div style="border-top: 1px solid #ddd;padding: 5px 0;font-size: 14px;" v-if="checked!=null">
				版本：
				<el-select v-model="version" placeholder="请选择版本" size='mini' @change="versionChange">
					<el-option v-for="item in versionList" :key="item.id" :label="item.versionCode" :value="item.id">
					</el-option>
				</el-select>
				<br>
				<span style="color:#999 ;font-size: 14px;">
					描述：{{describe}}
				</span>
			</div>
			<div style="margin-bottom: 10px;border-top: 1px solid #ddd;border-bottom: 1px solid #ddd;padding: 10px 0;">
				<el-button type="primary" round size="mini"
					@click="dialogTableVisible=true;radio=checked?checked.id:''; tempVersion = version">选择决策表
				</el-button>
			</div>
		</div>
		<endRule :data="terminationInfo" :list="[checked]" :onlyOne="true" @reSetTerminationInfo="reSetTerminationInfo"
			:valueType="2">
			<el-option label="决策表结果" :value="'16_'+data.id+'_terminal_result'"></el-option>

		</endRule>
		<div class="type3_submit_home"  v-if="!readOnly">
			<el-button type="primary" round @click="submit">提交</el-button>
		</div>

		<el-dialog title="选择决策表" width="30%" :visible.sync="dialogTableVisible" :modal="true" :append-to-body="true">
			<el-table :data="flist" size="mini">
				<el-table-column label="单选" width="50">
					<template slot-scope="scope">
						<el-radio v-model="radio" :label="scope.row.id" @change="radioChange">&nbsp;</el-radio>
					</template>
				</el-table-column>
				<el-table-column property="name" label="名称" width="150"></el-table-column>
				<el-table-column property="description" label="描述" width="200"></el-table-column>
				<el-table-column property="creatorName" label="创建人"></el-table-column>
			</el-table>
			<el-pagination layout="prev, pager, next" @current-change="currpage" :total="list?list.length:0">
			</el-pagination>

			<el-select v-model="tempVersion" placeholder="请选择版本" size='mini'>
				<el-option v-for="item in tempVersionList" :key="item.id" :label="item.versionCode" :value="item.id">
				</el-option>
			</el-select>
			<span style="color:#999 ;font-size: 14px;">
				描述：{{tempDescribe}}
			</span>
			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogTableVisible = false">取 消</el-button>
				<el-button type="primary" @click="radioSure">确 定</el-button>
			</span>
		</el-dialog>


	</div>
</template>

<script>
	import {
		getDecisionTablesList,
		setType4
	} from '@/api/index.js'
	import endRule from '@/components/common/endRule.vue'
	import {
		endRuleVerification
	} from '@/utils/endRule.js'
	const termination =  {
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
						"valueType": 2,
						"operator": "",
						"value": "",
						"relativeOperator": ""
					}]
				}
	export default {
		data() {
			return {
				version: '',
				versionList: [],
				// describe
				// tempVersionList:[],
				tempVersion: '',
				// tempDescribe:'',
				loading: false,
				radio: null,
				dialogTableVisible: false,
				checked: null,
				// list: null,
				page: 1,
				currentNode: {},
				terminationInfo: Object.assign({},termination)
			}
		},
		components: {
			getDecisionTablesList,
			setType4,
			endRule
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
				this.terminationInfo=Object.assign({},termination)
				this.loading = true
				// this.getType({
				// 	decisionTables: {},
				// 	pageNum: 0,
				// 	status: "0,1",
				// 	pageSize: 0
				// }).then(res => {
					// this.list = res.data.klist

					if (this.data.nodeJson) {
						let dataJson = JSON.parse(this.data.nodeJson)

						this.radio = dataJson.decisionTableList[0].decisionTableId
						this.terminationInfo = dataJson.terminationInfo
						this.list.forEach(value => {
							if (value.id == this.radio) {
								this.checked = value
								this.versionList = value.decisionTablesVersionList
								this.version = dataJson.decisionTableList[0].versionId
							}
						})
					}


					this.loading = false


				// })
			},
			reSetTerminationInfo() {
				this.terminationInfo=Object.assign({},termination)
			},
			versionChange() {
				this.radioSure('e', 2)
			},
			radioChange() {
				this.list.forEach(value => {
					if (value.id == this.radio) {
						this.currentNode = value
						this.tempVersion = value.decisionTablesVersionList[0].id
					}
				})
			},
			radioSure(e, type = 1) {
				this.list.forEach(value => {
					if (value.id == this.radio) {
						if (type == 1) {
							this.checked = value
							this.versionList = this.tempVersionList
							this.version = this.tempVersion
							this.currentNode.decisionTablesVersionList = [this.tempVersionList.find(x => x.id ==
								this.tempVersion)]
						} else if (type == 2) {
							this.currentNode.decisionTablesVersionList = [this.versionList.find(x => x.id == this
								.version)]
						}



					}
				})
				this.dialogTableVisible = false
			},
			submit() {

				if (!this.checked) {
					this.$message.error('请选择一个决策表')
					return
				}

				if(endRuleVerification(this.terminationInfo)){
					return
				}
				let version = this.versionList.find(x => x.id == this.version)

				let subObj = {
					"decisionTableList": [{
						"decisionTableId": this.checked.id,
						"parentId": this.checked.parentId,
						"code": this.checked.code,
						"name": this.checked.name,
						"versionId": version.id,
						"versionCode": version.versionCode,
						"versionDesc": version.description
					}],
					"terminationInfo": this.terminationInfo
				}

				let obj = {
					"id": this.data.id,
					"initEngineVersionId": String(this.data.Vid),
					"nodeType": 16,
					"cardId": this.checked.id,
					"nodeName": this.data.text,
					"nodeCode": "ND_" + this.data.nodeOrder,
					"nodeOrder": this.data.nodeOrder,
					"nodeX": this.data.x,
					"nodeY": this.data.y,
					"nodeJson": JSON.stringify(subObj),
					"snapshot": JSON.stringify(subObj),
				}
				this.setType(obj).then(res => {
					if (res.status == '1') {
						this.$message.success('提交成功')
						this.$emit('callback', JSON.stringify(subObj))
					}
				})
			},
			async getType(param) {

				const data = await getDecisionTablesList(param);
				return data
			},
			async setType(param) {

				this.loading = true
				const data = await setType4(param);
				this.loading = false
				return data
			},
			currpage(e) {
				this.page = e
			}
		},
		computed: {
			list(){
				return  this.$store.state.decisionTable
			},
			describe() {
				let str = ''
				if (this.list) {
					this.list.forEach(value => {
						value.decisionTablesVersionList.forEach(item => {
							if (item.id == this.version) {
								str = item.description
							}
						})
					})
					return str
				} else {
					return ''
				}

			},
			tempDescribe() {
				let str = ''
				if (this.list) {
					this.list.forEach(value => {
						value.decisionTablesVersionList.forEach(item => {
							if (item.id == this.tempVersion) {
								str = item.description
							}
						})
					})
					return str
				} else {
					return ''
				}
			},
			tempVersionList() {
				let arr = []

				// this.tempDescribe =''
				if (this.list) {
					this.list.forEach(value => {
						if (value.id == this.radio) {
							arr = value.decisionTablesVersionList

						}
					})
					return arr
				} else {
					return []
				}
			},
			flist() {
				if (this.list) {
					let arr = []
					let i = 10 * (this.page - 1)
					for (; i < 10 * this.page; i++) {
						if (this.list[i]) {
							arr.push(this.list[i])
						}
					}

					console.log(arr)
					return arr
				} else {
					return []
				}


			}
		},
		watch: {
			data() {
				this.checked = null
				this.radio = null
				this.crea()

			}
		}


	}
</script>

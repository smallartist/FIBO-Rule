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
			<p>节点类型:评分卡</p>

		</div>
		<div style="border-top: 1px solid #ddd;padding: 5px ;">
			<p style="margin-top: 10px; margin-bottom: 10px;font-size: 14px;font-weight: bold;">当前选择的评分卡：</p>
			<div style="margin-bottom: 10px;">
				<p v-if="checked!=null" style="color: #409EFF;">{{checked.name}}</p>
				<p v-if="checked==null" style="color: #999;">暂未选择评分卡</p>
			</div>
			<div style="border-top: 1px solid #ddd;padding: 5px 0;font-size: 14px;" v-if="checked!=null">
				版本：
				<el-select v-model="version" placeholder="请选择版本" size='mini'>
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
					@click="dialogTableVisible=true;tempVersionList=versionList;tempVersion=version;">选择评分卡</el-button>
			</div>
		</div>
		<endRule :data="terminationInfo" :list="[checked]" :onlyOne="true" @reSetTerminationInfo="reSetTerminationInfo">
			<el-option label="评分卡得分" :value="'4_'+data.id+'_terminal_score'"></el-option>

		</endRule>

		<div class="type3_submit_home" v-if="!readOnly">
			<el-button type="primary" round @click="submit">提交</el-button>
		</div>


		<el-dialog title="选择评分卡" width="30%" :visible.sync="dialogTableVisible" :modal="true" :append-to-body="true">
			<div v-loading="dialogLoading">
				<el-table :data="flist" size="mini">
					<el-table-column label="单选" width="50">
						<template slot-scope="scope">
							<el-radio v-model="radio" :label="scope.row.id" @change="radioChange">&nbsp;</el-radio>
						</template>
					</el-table-column>
					<el-table-column property="name" label="名称" width="150"></el-table-column>
					<el-table-column property="description" label="描述" width="200"></el-table-column>
					<el-table-column property="authorName" label="创建人"></el-table-column>
				</el-table>
				<el-pagination layout="prev, pager, next" @current-change="currpage" :total="list?list.length:0">
				</el-pagination>

				<el-select v-model="tempVersion" placeholder="请选择版本" size='mini'>
					<el-option v-for="item in tempVersionList" :key="item.id" :label="item.versionCode"
						:value="item.id">
					</el-option>
				</el-select>
				<span style="color:#999 ;font-size: 14px;">
					描述：{{tempDescribe}}
				</span>
				<div slot="footer" class="dialog-footer" style="margin-top: 20px;margin-left: 60%;">
					<el-button @click="dialogTableVisible = false">取 消</el-button>
					<el-button type="primary" @click="radioSure">确 定</el-button>
				</div>
			</div>
		</el-dialog>




	</div>
</template>

<script>
	import {
		getType4,
		setType4,
		getSCOVersion
	} from '@/api/index.js'
	import endRule from '@/components/common/endRule.vue'
	import {
		endRuleVerification
	} from '@/utils/endRule.js'
	const termination = {
		output: {
			"fieldId": '',
			"fieldCode": "",
			"fieldName": "",
			"valueType": 1,
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
		data() {
			return {
				dialogLoading: false,
				tempVersionList: [],
				versionList: [],
				version: '',
				tempVersion: '',
				loading: false,
				radio: null,
				dialogTableVisible: false,
				checked: null,
				// list: null,
				page: 1,
				currentNode: {},
				terminationInfo: Object.assign({}, termination)
			}
		},
		components: {
			getType4,
			setType4,
			endRule,
			getSCOVersion
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
			readOnly: {
				type: Boolean,
				default: false
			}
		},
		methods: {
			crea() {
				this.terminationInfo = Object.assign({}, termination)
				this.loading = true
				// this.getType({
				// 	"nodeId": this.data.id,
				// 	"status": 1,
				// 	pageSize: 100
				// }).then(res => {
				// this.list = res.data.list

				console.log(this.data)
				if (this.data.nodeJson) {
					let obj = JSON.parse(this.data.nodeJson)
					this.terminationInfo = obj.terminationInfo
	
					let value = this.list.find(x=>x.id==obj.scorecardList[0].scorecardId)
					
					getSCOVersion({id:obj.scorecardList[0].scorecardId}).then(res => {
						if(res.status=='1'){
							this.tempVersionList = res.data.versionList
							this.tempVersion = obj.scorecardList[0].versionId
							this.versionList = this.tempVersionList
							
							this.version = this.tempVersion
							this.checked = value
							this.radio = value.id
						}
						
						this.loading = false
					})




				}else{
					this.loading = false
				}


				//  断点  未完成显示部分

				
				// })
			},
			radioChange(e) {
				this.dialogLoading = true
				getSCOVersion({
					id: e
				}).then(res => {
					if (res.status == '1') {
						this.currentNode = res.data;
						this.tempVersionList = res.data.versionList
						this.tempVersion = JSON.parse(JSON.stringify(this.tempVersionList[0].id))
					}
					this.dialogLoading = false
				})
				console.log(e);
			},
			radioSure() {
				this.currentNode.versionList = [this.tempVersionList.find(x => x.id == this.tempVersion)];
				this.list.forEach(value => {
					if (value.id == this.radio) {
						this.checked = value
						this.versionList = this.tempVersionList
						this.version = this.tempVersion
						this.reSetTerminationInfo()
					}
				})
				this.dialogTableVisible = false
			},
			submit() {
				if (!this.checked) {
					this.$message.error('请选择评分卡')
					return
				}
				if (endRuleVerification(this.terminationInfo)) {
					return
				}

				let version = this.versionList.find(x => x.id == this.version)

				let subobj = {
					"scorecardList": [{
						scorecardId: this.checked.id,
						parentId: this.checked.parentId,
						code: this.checked.code,
						name: this.checked.name,
						versionId: version.id,
						versionCode: version.versionCode,
						versionDesc: version.description,
					}],
					terminationInfo: this.terminationInfo
				}
				console.log(subobj)
				let obj = {
					"id": this.data.id,
					"initEngineVersionId": String(this.data.Vid),
					"nodeType": 4,
					// "cardId": this.checked.id,
					"nodeName": this.data.text,
					"nodeCode": "ND_" + this.data.nodeOrder,
					"nodeOrder": this.data.nodeOrder,
					"nodeX": this.data.x,
					"nodeY": this.data.y,
					"nodeJson": JSON.stringify(subobj),
					"snapshot": JSON.stringify(subobj)
				}
				this.setType(obj).then(res => {
					if (res.status == '1') {
						this.$emit('callback', JSON.stringify(subobj))
					}
				})
			},

			// async getType(param) {
			// 	const data = await getType4(param);
			// 	return data
			// },
			async setType(param) {
				this.loading = true
				const data = await setType4(param);
				this.loading = false
				return data
			},
			currpage(e) {
				this.page = e
			},
			reSetTerminationInfo() {
				this.terminationInfo = Object.assign({}, termination)
			}
		},
		computed: {
			list() {
				return this.$store.state.SCO
			},
			tempDescribe() {
				let str = ''
				this.tempVersionList.forEach(value => {
					if (value.id === this.tempVersion) {
						str = value.description
					}
				})
				return str
			},
			describe() {
				let str = ''
				this.versionList.forEach(value => {
					if (value.id === this.version) {
						str = value.description
					}
				})
				return str
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

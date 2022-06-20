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

	.type2_rule_header {
		height: 50px;
		margin-bottom: 10px;
		display: flex;
		align-items: center;
		white-space: nowrap;
	}

	.currtree {
		color: #409EFF;
	}

	.type2_rule_cont {
		display: flex;
		flex-wrap: wrap;
		/* justify-content: flex-start; */
	}

	.type2_rule_cont>div {
		width: 33%;
		white-space: nowrap;
	}

	.type2_select_rule {
		color: #409EFF;
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
		border-bottom: 1px solid #ddd;
		padding-bottom: 10px;
		font-size: 14px;
	}
</style>

<template>
	<div class="type4 type" v-loading='loading||ruleList==null' style="width: 400px;" :key="dialogkey">
		<div class="type_header">
			<p>节点类型:名单库</p>
		</div>
		<div v-if="ruleList">
			<p style="font-size: 12px;font-weight: bold;">已选节点:</p>
			<div v-if="params.rules.length<1" style="color: #aaa;">
				暂未选择名单
			</div>
			<div v-else class="type2_select_rule">

				<p v-for="item in frules">
					<i class="el-icon-error" @click="delectCurrRule(item)"></i>
					{{item}}
				</p>
				<el-pagination small layout="prev, pager, next" :total="params.rules.length" :page-size="10"
					@current-change="page2=$event" style="margin-top: 10px;">
				</el-pagination>

			</div>
			<el-button type="primary" round size="mini" @click="dialogVisible=true" style="margin-top: 10px;">选择名单
			</el-button>
			<endRule :data="terminationInfo" :list="currRuleArr"  @reSetTerminationInfo="reSetTerminationInfo"
				:valueType="1" name="listName">
				<el-option label="名单库命中数量" :value="'5_'+data.id+'_terminal_size'"></el-option>
			</endRule>
			
		</div>
		


		<el-dialog title="选择名单(多选)" :visible="dialogVisible" width="60%" @close="handleClose" :append-to-body="true">

			<div class="type2_rule_header">
				<div style="float: left;width: 80%;">


					<el-button type="primary" round size="mini" @click="CurrPage" style="margin-left: 20px;"
						:disabled="elswitch">当页全选/清空
					</el-button>
					<!-- <el-button type="primary" round size="mini" @click="CurrClassify" :disabled="elswitch"> 当前分类全选/清空 </el-button> -->
					<el-switch v-model="elswitch" style="margin-left: 20px;" active-text="只显示已选中" inactive-text="正常"
						@change="page=1">
					</el-switch>
				</div>
				<el-input v-model="search" size="mini" placeholder="快速搜索" style="width: 20%;float: right;"></el-input>
			</div>


			<div style="display: flex;align-items: center;justify-content: center;">
				<div style="width: 100%;" v-if="elswitch">
					<el-table :data="fcurrRule" style="width: 100%" size="mini">
						<el-table-column label="id" width="200">
							<template slot-scope="scope">
								{{scope.row.split('/')[0]}}
							</template>
						</el-table-column>
						<el-table-column label="名称" >
							<template slot-scope="scope">
								{{scope.row}}
							</template>
						</el-table-column>
						<!-- <el-table-column prop="listType" label="类型" width="230">
							<template slot-scope="scope">
								{{scope.row.listType==="b"?"黑名单":'白名单'}}
							</template>
						</el-table-column>
						<el-table-column prop="dataSource" width="200" label="数据来源">
							<template slot-scope="scope">
								{{scope.row.dataSource==1?'外部':scope.row.dataSource==2?'内部':'待选'}}
							</template>
						</el-table-column> -->
						<el-table-column label="操作" >
							<template slot-scope="scope">
								<i class="el-icon-error" style="color: #D12449;font-size: 18px;"
									@click="delectcurrRule(scope.row)"></i>
							</template>
						</el-table-column>
					</el-table>
					<el-pagination small layout="prev, pager, next" :total="secrRule.length" :page-size="10"
						@current-change="page=$event" style="margin-left: 70%;margin-top: 10px;">
					</el-pagination>
				</div>
				
				



				<el-checkbox-group v-model="currRule" style="width: 100%;" v-else>
					<el-table :data="pageRuleList" style="width: 100%" size="mini">
						<el-table-column width="80">
							<template slot-scope="scope">
								<el-checkbox :label="scope.row.id+'/'+scope.row.listName">&nbsp;</el-checkbox>
							</template>
						</el-table-column>
						<el-table-column prop="id" label="id" >
						</el-table-column>
						<el-table-column prop="listName" label="名称">
						</el-table-column>
						<!-- <el-table-column prop="listType" label="类型" width="230">
							<template slot-scope="scope">
								{{scope.row.listType==="b"?"黑名单":'白名单'}}
							</template>
						</el-table-column>
						<el-table-column prop="dataSource" width="200" label="数据来源">
							<template slot-scope="scope">
								{{scope.row.dataSource==1?'外部':scope.row.dataSource==2?'内部':'待选'}}
							</template>
						</el-table-column> -->
						<el-table-column prop="nickName" label="创建人" >
						</el-table-column>
					</el-table>
					<el-pagination small layout="prev, pager, next" :total="fruleList.length" :page-size="10"
						@current-change="page=$event" style="margin-left: 70%;margin-top: 10px;">
					</el-pagination>
				</el-checkbox-group>
			</div>
			<!-- <div>
				<p style="margin: 10px;padding: 10px;border-top: 1px solid #ddd; border-bottom: 1px solid #ddd;">已选:</p>
				<div style="display: flex;flex-wrap: wrap;">
					<p v-for="item in currRule" style="width: 33%;white-space: nowrap;"><i class="el-icon-error" style="font-size: 20px;"
						 @click="delect(item)"></i>{{item}}</p>
				</div>
			</div> -->
			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogVisible = false;currRule=[...params.rules];tempSerial=params.isSerial">取 消
				</el-button>
				<el-button type="primary" @click="dialogSure">确 定</el-button>
			</span>
		</el-dialog>

		<div class="type3_submit_home" v-if="!readOnly">
			<el-button type="primary" round @click="submit">提交</el-button>
		</div>








	</div>
</template>

<script>
	import {
		getType56,
	
		setType4
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
		components: {
			getType56,
		
			endRule
		},
		data() {
			return {
				currtreetype: '',
				dialogkey: 1,
				search: '',
				elswitch: false,
				page2: 1,
				page: 1,
				currtree: '',
				activeName: 'first',
				dialogVisible: false,
				params: {
					rules: [],
					isSerial: 0
				},
				tempSerial: 0,
				currRule: [],
				loading: false,
				tree: null,
				
				terminationInfo:Object.assign({}, termination)
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
				this.terminationInfo = Object.assign({}, termination)
				this.loading = true
				console.log(this.data)
				// getType56({
				// 	page: 0,
				// 	"nodeId": this.data.id,
				// 	pageSize: 0
				// }).then(res => {
				// 	if (res.status == "1") {
				// 		this.ruleList = res.data.listDbs


						


				// 	}
				// 	this.loading = false
				// })
				
				if (this.data.nodeJson) {
					let nodeJson = JSON.parse(this.data.nodeJson)
					this.params.rules = nodeJson.listDbList.map(value => value.listDbId +'/'+value.listName)
					this.currRule = [...this.params.rules ]
					this.terminationInfo = nodeJson.terminationInfo
				}


				this.loading = false
			},
			submit(){
				if(this.params.rules.length==0){
					
					this.$message.error('请先选择一个名单库')
					return
				}
				if(endRuleVerification(this.terminationInfo)){
					return
				}
				
				let arr = this.currRule.map(value => {
					let obj
					this.ruleList.forEach(item => {
						if (item.id == value.split('/')[0]) {
							obj = item
						}
					})
					return obj
				})
				console.log(arr)
				// let innerListdbs = arr.filter(value => {
				// 	if (value.dataSource == 2) {
				// 		return true
				// 	} else {
				// 		return false
				// 	}
				// }).map(value => {
				// 	return value.id
				// })
				
				
				// let outerListdbs = arr.filter(value => {
				// 	if (value.dataSource == 1) {
				// 		return true
				// 	} else {
				// 		return false
				// 	}
				// }).map(value => {
				// 	return value.id
				// })
				
				console.log(arr)
				let subobj = {
					listDbList: arr.map(value=>{
						return {
							listDbId : value.id,
							listDesc : value.listDesc,
							listName : value.listName
						}
					}),
					terminationInfo: this.terminationInfo
				}
				
				let obj = {
					"id": this.data.id,
					"nodeId": this.data.id,
					"versionId": this.data.Vid,
					initEngineVersionId: String(this.data.Vid),
					"nodeType": this.data.item.node.type,
					"nodeJson": JSON.stringify(subobj),
					"nodeOrder": this.data.nodeOrder,
					"nodeName": this.data.text,
					"nodeCode": this.data.nodeCode,
					"nodeX": this.data.x,
					"nodeY": this.data.y,
					// "innerListdbs": innerListdbs.join(','),
					// "outerListdbs": outerListdbs.join(','),
					"snapshot": JSON.stringify(subobj)
				}
				
				
				setType4(obj).then(res => {
					if (res.status == "1") {
						
				
				
						this.$emit('callback', JSON.stringify(subobj))
						this.$message({
							message: '提交成功',
							type: 'success'
						});
					}
					
				})
			},
			CurrPage() {
				let is = false
				console.log(this.currRule)
				this.pageRuleList.forEach(value => {
					if (this.currRule.indexOf(value.id + '/' + value.listName) == -1) {
						is = true
					}
				})
				if (is) {
					this.pageRuleList.forEach(value => {
						if (this.currRule.indexOf(value.id + '/' + value.listName) == -1) {
							this.currRule.push(value.id + '/' + value.listName)
						}
					})
				} else {
					this.pageRuleList.forEach(value => {
						if (this.currRule.indexOf(value.id + '/' + value.listName) != -1) {
							this.currRule.splice(this.currRule.indexOf(value.id + '/' + value.listName), 1)
						}
					})
				}
			},
			change() {

				this.dialogSure()
			},
			delect(item) {
				this.currRule.forEach((value, index) => {
					if (value == item) {
						this.currRule.splice(index, 1)
					}
				})
			},
			handleClose() {
				this.dialogVisible = false;
				this.currRule = [...this.params.rules];
				this.tempSerial = this.params.isSerial
			},
			delectCurrRule(item) {
				this.$confirm('确定删除?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {

					let tempindex
					this.currRule.forEach((value, index) => {
						if (value == item) {
							tempindex = index
							return
						}
					})
					this.currRule.splice(tempindex, 1)
					this.dialogSure()

				}).catch(() => {
					this.$message({
						type: 'info',
						message: '已取消删除'
					});
				});




			},
			reSetTerminationInfo() {
				this.terminationInfo = {
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
						"valueType": 2,
						"operator": "",
						"value": "",
						"relativeOperator": ""
					}]
				}
			},
			dialogSure() {
				this.params.rules = [...this.currRule]
				

				this.params.isSerial = this.tempSerial
				this.dialogVisible = false

				// let sobj = {}
				// sobj.rules = this.params.rules.map(value => {
				// 	let o
				// 	this.ruleList.forEach(item => {
				// 		if (item.id == parseInt(value.split('/')[0])) {
				// 			o = item
				// 		}
				// 	})
				// 	return o
				// })

			},
			delectcurrRule(str) {

				this.currRule.forEach((value, index) => {
					if (value === str) {
						this.currRule.splice(index, 1)
					}
				})




			},
			async getType(param) {
				const data = await getType2(param);
				return data
			},
			async getTypetree(param) {
				const data = await getType2tree(param);
				return data
			},
			
		},
		watch: {
			dialogVisible() {
				console.log(this.dialogVisible)
			},
			data() {

				this.search = ''
				this.elswitch = false
				this.page2 = 1
				this.page = 1

				this.params = {
					rules: [],
					isSerial: 0
				}
				this.tempSerial = 0
				this.currRule = []
				this.tree = null
				this.currtreetype = ""
				this.ruleList = null
				this.dialogVisible = false
				this.dialogkey++
				this.currtree = ''
				// this.getparams()
				this.crea()
			},

		},
		computed: {
			ruleList(){
				return this.$store.state.BlackWhiteList
			},
			fruleList() {
				if (this.ruleList) {
					let arr = this.ruleList.filter(value => {
						return value.dataSource != 0
					})
					let arr2 = arr.filter((value, index) => {
						return value.listName.indexOf(this.search) != -1
					})

					return arr2
				} else {
					return []
				}
			},
			pageRuleList() {


				let arr2 = this.fruleList.filter((value, index) => {
					return (index >= ((this.page - 1) * 10) && index < (this.page * 10))
				})
				return arr2
			},
			frules() {
				if (this.params) {



					let arr = this.params.rules.filter((value, index) => {
						return (index >= ((this.page2 - 1) * 10) && index < (this.page2 * 10))
					})
					return arr
				} else {
					return []
				}
			},
			secrRule() {
				let arr2 = this.currRule.filter((value, index) => {
					return value.indexOf(this.search) != -1
				})
				return arr2
			},
			fcurrRule() {
				let arr = this.secrRule.filter((value, index) => {
					return (index >= ((this.page - 1) * 10) && index < (this.page * 10))
				})
				return arr
			},
			currRuleArr(){ //选择的黑名单的 对象格式
				return this.ruleList.filter(value=>{
					return this.params.rules.indexOf(value.id+'/'+value.listName)!=-1
				})
				
			}
		}

	}
</script>

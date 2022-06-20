<style>
	.type2 {
		width: 450px;
		border-top: 1px solid #ddd;
		padding: 5px;
		box-sizing: border-box;
		padding-bottom: 100px;
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
		padding: 5px 0 10px;
		font-size: 14px;
	}

	.type2_select_rule p {
		margin-top: 5px;
		line-height: 1.5em;
	}

	.setting-wrapper {
		display: flex;
		align-items: center;
		justify-content: space-between;
	}

	.line {
		margin: 0 10px;
		/* display: inline-block; */
	}

	.rule-item .page {
		display: none;
		color: #606266;
		/* border: 1px #E4E7ED solid; */
		border-radius: 30px;
		/* padding: 0 5px; */
		margin-left: 5px;
		font-size: 12px;
		box-sizing: border-box;
	}

	.rule-item:hover .page {
		display: inline-block;
	}
</style>

<template>
	<div class="type2" v-loading='loading||!Array.isArray(ruleList)' :key="dialogkey">
		<div class="type_header">
			<p>节点类型:规则集</p>
		</div>
		<div>
			<p style="font-size: 12px;font-weight: bold;">已选节点:<i
					style="color:#F56C6C;">{{tempSerial==1?'（拖动可调整当前页优先级,点击上一页下一页调整所在页）':''}}</i></p>
			<div v-if="params.rules.length<1" style="color: #aaa;">
				暂未选择规则
			</div>
			<div v-else class="type2_select_rule">
				<!-- <vuedraggable class="wrapper" v-model="frules" v-if="tempSerial==1">
					<transition-group tag="p">						
						<p v-for="(item,index) in frules" :key="index" class="rule-item">
							<i class="el-icon-error" @click="delectCurrRule(item)"></i>
							{{item}}
							<span v-if="page2>1" @click="pre(item)">上一页</span><span v-if="page2<Math.ceil(params.rules.length/10)" @click="next(item)">下一页</span>
						</p>						
					</transition-group>
				</vuedraggable> -->

				<vuedraggable class="wrapper" v-model="frules" v-if="tempSerial==1">
					<transition-group tag="p">
						<p v-for="(item,index) in frules" :key="index" class="rule-item">
							<i class="el-icon-error" @click="delectCurrRule(item)"></i>
							{{item.split('/')[0]+'/'+item.split('/')[1]}}
							<span class="page" v-if="page2>1" @click="pre(item)">上一页</span>
							<span class="page" v-if="page2<Math.ceil(params.rules.length/10)"
								@click="next(item)">下一页</span>
							<el-select style="width: 100px;margin-left: 10px;" v-if="item.split('/')[2]"
								:value="Number(item.split('/')[2])" placeholder="请选择" size="mini"
								@change="selsctVersionChange($event,item)">
								<el-option v-for="value in getRuleById(Number(item.split('/')[0])).ruleVersionList"
									:key="value.id" :label="value.versionCode" :value="value.id">
								</el-option>
							</el-select>
							<span style="margin-left: 10px;"
								v-if="item.split('/')[2]">{{getdescription(item.split('/')[0],item.split('/')[2])}}</span>
						</p>
					</transition-group>
				</vuedraggable>

				<div v-if="tempSerial==2">
					<p v-for="(item,index) in frules" :key="index">
						<i class="el-icon-error" @click="delectCurrRule(item)"></i>
						{{item.split('/')[0]+'/'+item.split('/')[1]}}

						<el-select style="width: 100px;margin-left: 10px;" v-if="item.split('/')[2]"
							:value="Number(item.split('/')[2])" placeholder="请选择" size="mini"
							@change="selsctVersionChange($event,item)">
							<el-option v-for="value in getRuleById(Number(item.split('/')[0])).ruleVersionList"
								:key="value.id" :label="value.versionCode" :value="value.id">
							</el-option>
						</el-select>
						<span style="margin-left: 10px;"
							v-if="item.split('/')[2]">{{getdescription(item.split('/')[0],item.split('/')[2])}}</span>

					</p>
				</div>
				<el-pagination small layout="prev, pager, next" :total="params.rules.length" :page-size="10"
					@current-change="page2=$event" style="margin-top: 10px;">
				</el-pagination>

			</div>
			<el-button type="primary" round size="mini" @click="selectRule" style="margin-top: 10px;">选择规则</el-button>
		</div>

		<div style="display: flex;margin-top: 10px;border-top: 1px solid #ddd;padding: 10px;">
			<el-radio v-model="tempSerial" :label="1" border size="mini" @change="change">串行</el-radio>
			<el-radio v-model="tempSerial" :label="2" border size="mini" @change="change">并行</el-radio>
		</div>
		<endRule :data="terminationInfo" :list="paramsRulesObj" @reSetTerminationInfo="reSetTerminationInfo"
			:valueType="1">
			<el-option label="规则命中个数" :value="'2_'+data.id+'_terminal_size'"></el-option>
			<el-option label="规则总得分" :value="'2_'+data.id+'_terminal_score'"></el-option>
		</endRule>

		<div class="type3_submit_home" v-if="!readOnly">
			<el-button type="primary" round @click="submit">提交</el-button>
		</div>
		<el-dialog title="选择规则(多选)" :visible="dialogVisible" width="60%" @close="handleClose" :append-to-body="true">

			<div class="type2_rule_header">
				<div style="float: left;width: 80%;">
					<el-select v-model="currtreetype" placeholder="请选择规则分类" :disabled="elswitch" @change="getTree"
						style="width: 20%;">
						<!-- <el-option :key="1" label="简单规则" :value="0"></el-option> -->
						<el-option :key="2" label="复杂规则" :value="5"></el-option>
						<el-option :key="3" label="脚本规则" :value="7"></el-option>
					</el-select>
					<el-select v-model="currtree" placeholder="请选择规则分类" @change="page=1"
						:disabled="elswitch||currtreetype===''" style="width: 20%;margin-left: 10px;">
						<el-option v-for="item in tree" :key="item.id" :label="item.name" :value="item.id">
						</el-option>
					</el-select>
					<el-button type="primary" round size="mini" @click="CurrPage" style="margin-left: 20px;"
						:disabled="elswitch">当页全选/清空
					</el-button>
					<el-button type="primary" round size="mini" @click="CurrClassify" :disabled="elswitch"> 当前分类全选/清空
					</el-button>
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
						<el-table-column label="名称" width="300">
							<template slot-scope="scope">
								{{scope.row.split('/')[1]}}
							</template>
						</el-table-column>
						<el-table-column label="版本" width="300">
							<template slot-scope="scope">
								<!-- {{scope.row.split('/')[2]}} -->


								<div v-if="getRuleById(scope.row.split('/')[0]).ruleVersionList">
									<el-select style="width: 150px;"
										v-model="getRuleById(scope.row.split('/')[0]).ruleVersionId" placeholder="请选择"
										size="mini"
										@change="versionChange($event,getRuleById(scope.row.split('/')[0]))">
										<el-option v-for="item in getRuleById(scope.row.split('/')[0]).ruleVersionList"
											:key="item.id" :label="item.versionCode" :value="item.id">
										</el-option>
									</el-select>
									{{getdescription(getRuleById(scope.row.split('/')[0]).id,getRuleById(scope.row.split('/')[0]).ruleVersionId)}}
								</div>
								<div v-else>
									此规则无版本
								</div>
							</template>

						</el-table-column>
						<el-table-column label="操作" width="310">
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
								<el-checkbox
									:label="scope.row.ruleVersionId?scope.row.id+'/'+scope.row.name+'/'+scope.row.ruleVersionId:scope.row.id+'/'+scope.row.name">
									&nbsp;</el-checkbox>
							</template>
						</el-table-column>
						<el-table-column prop="id" label="id" width="150">
						</el-table-column>
						<el-table-column prop="code" label="code" width="230">
						</el-table-column>
						<el-table-column prop="name" label="名称" width="200">
						</el-table-column>
						<el-table-column label="版本" width="300">
							<template slot-scope="scope">
								<div v-if="scope.row.ruleVersionList">
									<el-select style="width: 150px;" v-model="scope.row.ruleVersionId" placeholder="请选择"
										size="mini" @change="versionChange($event,scope.row)">
										<el-option v-for="item in scope.row.ruleVersionList" :key="item.id"
											:label="item.versionCode" :value="item.id">
										</el-option>
									</el-select>
									{{getdescription(scope.row.id,scope.row.ruleVersionId)}}
								</div>
								<div v-else>
									此规则无版本
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="authorName" label="创建人" width="150">
						</el-table-column>
					</el-table>
					<el-pagination small layout="prev, pager, next" :total="fruleList.length" :page-size="10"
						@current-change="page=$event" style="margin-left: 70%;margin-top: 10px;">
					</el-pagination>
				</el-checkbox-group>
			</div>

			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogVisible = false;currRule=[...params.rules];tempSerial=params.isSerial">取 消
				</el-button>
				<el-button type="primary" @click="dialogSure">确 定</el-button>
			</span>
		</el-dialog>


	</div>
</template>

<script>
	import {
		getType2,
		getType2tree,
		setType4,
		getRulesListTree
	} from '@/api/index.js'
	import vuedraggable from 'vuedraggable';
	import ruleRelation from '@/components/common/ruleRelation.vue'
	import varialeSelect from '@/components/models/varialeSelect.vue'
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
			endRule,
			getType2,
			getType2tree,
			setType4,
			vuedraggable,
			ruleRelation,
			varialeSelect
		},
		data() {
			return {
				terminationInfo: Object.assign({}, termination),
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
				ruleArr: [],
				currentRule: "",
				showRuleDialog: false,
				multipleSelection: [],
				nodeJson: [],
				page3: 1,
				tempPage3CurrRule: [],
				selectedRule: [],
				currentSelectRule: [],
				RuletypeVersionList: {
					1: null,
					2: 'ruleVersionList',
					3: 'ruleScriptVersionList'
				}
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
			readOnly: {
				type: Boolean,
				default: false
			}
		},
		methods: {
			reSetTerminationInfo() {
				this.terminationInfo = Object.assign({}, termination)
				console.log(1123)
			},
			pre(e) {
				this.params.rules.splice(this.params.rules.indexOf(e), 1);
				this.params.rules.splice((this.page2 - 2) * 10, 0, e)
			},
			next(e) {
				this.params.rules.splice(this.params.rules.indexOf(e), 1);
				this.params.rules.splice((this.page2) * 10 + 1, 0, e)
			},

			getdescription(id, vid) {
				let str = null
				this.ruleList.forEach(value => {
					if (value.id == id) {
						if (value.ruleVersionList) {
							value.ruleVersionList.forEach(item => {
								if (item.id == vid) {
									str = item.description
								}
							})
						}
					}
					if (str != null) {
						return
					}
				})
				return str
			},
			selectRule() {
				this.ruleList.forEach(value => {
					this.params.rules.forEach(item => {
						if (value.id === Number(item.split('/')[0])) {
							if (item.split('/')[2]) {
								value.ruleVersionId = Number(item.split('/')[2])
							}
						}
					})

				})
				this.dialogVisible = true
			},
			selsctVersionChange(e, item) {
				let num
				this.params.rules.forEach((value, index) => {
					if (value == item) {
						num = index
					}
				})
				// this.params.rules[num] = item.split('/')[0]+'/'+item.split('/')[1]+'/'+e
				this.$set(this.params.rules, num, item.split('/')[0] + '/' + item.split('/')[1] + '/' + e)
				this.ruleList.forEach(value => {
					if (value.id === Number(item.split('/')[0])) {
						value.ruleVersionId = e
					}
				})
				this.currRule = [...this.params.rules]
				// debugger
			},
			getRuleById(id) {
				let obj
				this.ruleList.forEach(value => {
					if (value.id === Number(id)) {
						obj = value
					}
				})
				return obj
			},
			versionChange(e, obj) {
				this.currRule.forEach((value, index) => {
					if (Number(value.split('/')[0]) === obj.id) {
						this.currRule[index] = obj.id + '/' + obj.code + '/' + e
					}
				})
			},

			toggleSelection() {
				this.$nextTick(() => {
					this.ruleArr.forEach(row => {
						let isSel = this.nodeJson.terminationInfo.selectedRule.findIndex(x => x.id == row
							.id);
						if (isSel >= 0) {
							this.$refs.ruleArr.toggleRowSelection(row);
						}
					})
				})
			},
			selRuleSure() {
				this.selectedRule = [...this.currentSelectRule];
				this.showRuleDialog = false;
			},
			cancelSelRule() {
				this.showRuleDialog = false;
			},
			handleSelectionChange(val) {
				this.multipleSelection = val;
			},
			selRule(isShow) {

				this.ruleArr = this.params.rules.map((value, idx) => {
					let o
					this.ruleList.forEach(item => {
						if (item.id == parseInt(value.split('/')[0])) {
							o = {
								id: item.id,
								parentId: item.parentId,
								priority: idx,
								code: item.code,
								name: item.name
							}
						}
					})
					return o
				})
				if (this.ruleArr.length > 0 && isShow) {
					this.showRuleDialog = true;
					this.currentSelectRule = [...this.selectedRule];
					this.toggleSelection();
				} else if (isShow) {
					this.$message.error('请选择规则！');
				}

			},
			getTree(e) {
				this.getType({
					// "status": 1,
					parentId : 0,
					"type": 1,
					treeType: String(e),
					// pageSize: 9999999
				}).then(res => {
						this.tree = res
						this.currtree = ""
				})
			},
			submit() {

				let rules = this.params.rules.map((value, idx) => {
					let o
					this.ruleList.forEach(item => {
						if (item.id == parseInt(value.split('/')[0])) {
							o = {
								id: item.id,
								parentId: item.parentId,
								code: item.code,
								name: item.name,
							}
							if (this.tempSerial == 1) {
								o.priority = idx
							}
							if (value.split('/')[2]) {
								o.ruleVersionId = Number(value.split('/')[2])
							}
							if (item.ruleVersionList) {
								item.ruleVersionList.forEach(v => {
									if (v.id == o.ruleVersionId) {
										o.versionCode = v.versionCode;
										o.description = v.description;
										o.difficulty = item.difficulty;
									}
								})
							}

						}
					})
					return o
				})
				if (rules.length <= 0) {
					this.$message.error("请选择规则！")
					return
				}
				if (endRuleVerification(this.terminationInfo)) {
					return
				}




				let nodeJson = this.nodeJson;
				nodeJson.groupType = this.tempSerial;
				nodeJson.mutexGroup.rules = this.tempSerial == 1 ? rules : [];
				nodeJson.executeGroup.rules = this.tempSerial == 2 ? rules : [];
				nodeJson.terminationInfo = this.terminationInfo;
				this.loading = true;
				// debugger
				this.setType({
					"id": this.data.id,
					"initEngineVersionId": String(this.data.Vid),
					"nodeType": 2,
					"nodeName": this.data.text,
					"nodeCode": this.data.nodeCode,
					"nodeOrder": this.data.nodeOrder,
					"nodeX": this.data.x,
					"nodeY": this.data.y,
					"nodeJson": JSON.stringify(nodeJson),
					"snapshot": JSON.stringify(nodeJson)
				}).then(res => {
					if (res.status == 1) {
						this.$message({
							message: '提交成功',
							type: 'success'
						});
						this.$emit('callback', JSON.stringify(nodeJson))
					}
					this.loading = false;
				}).catch(err => {
					this.loading = false;
				})

			},
			crea() {
				this.terminationInfo = Object.assign({}, termination)
				this.loading = true;

				let defaultNode = {
					"groupType": 2,
					"mutexGroup": {
						"rules": []
					},
					"executeGroup": {
						"rules": []
					},

				}
				let nodeJson = this.data.nodeJson;
				if (nodeJson) {
					nodeJson = JSON.parse(nodeJson);
					if (!nodeJson.mutexGroup) {
						nodeJson.mutexGroup = {
							"rules": []
						}
					}
					if (!nodeJson.executeGroup) {
						nodeJson.executeGroup = {
							"rules": []
						}
					}
					if (nodeJson.terminationInfo) {
						this.terminationInfo = nodeJson.terminationInfo
					}
					this.nodeJson = nodeJson;
				} else {
					this.nodeJson = defaultNode;
				}
				this.getparams()
				this.loading = false
			},
			CurrPage() {
				let is = false
				this.pageRuleList.forEach(value => {
					let str = value.ruleVersionId ? value.id + '/' + value.name + '/' + value.ruleVersionId : value
						.id + '/' + value.name
					if (this.currRule.indexOf(str) == -1) {
						is = true
					}
				})
				if (is) {
					this.pageRuleList.forEach(value => {
						let str = value.ruleVersionId ? value.id + '/' + value.name + '/' + value.ruleVersionId :
							value.id + '/' + value.name
						if (this.currRule.indexOf(str) == -1) {
							this.currRule.push(str)
						}
					})
				} else {
					this.pageRuleList.forEach(value => {
						let str = value.ruleVersionId ? value.id + '/' + value.name + '/' + value.ruleVersionId :
							value.id + '/' + value.name
						if (this.currRule.indexOf(str) != -1) {
							this.currRule.splice(this.currRule.indexOf(str), 1)
						}
					})
				}
			},
			CurrClassify() {
				let is = false
				this.fruleList.forEach(value => {
					let str = value.ruleVersionId ? value.id + '/' + value.name + '/' + value.ruleVersionId : value
						.id + '/' + value.name
					if (this.currRule.indexOf(str) == -1) {
						is = true
					}
				})
				if (is) {
					this.fruleList.forEach(value => {
						let str = value.ruleVersionId ? value.id + '/' + value.name + '/' + value.ruleVersionId :
							value.id + '/' + value.name
						if (this.currRule.indexOf(str) == -1) {
							this.currRule.push(str)
						}
					})
				} else {
					this.fruleList.forEach(value => {
						let str = value.ruleVersionId ? value.id + '/' + value.name + '/' + value.ruleVersionId :
							value.id + '/' + value.name
						if (this.currRule.indexOf(str) != -1) {
							this.currRule.splice(this.currRule.indexOf(str), 1)
						}
					})
				}
			},
			change(e) {
				this.tempSerial = e
			},
			delect(item) {
				this.currRule.forEach((value, index) => {
					if (value == item) {
						this.currRule.splice(index, 1)
					}
				})
			},
			getRules() {
				let rules = [];
				if (typeof this.data.nodeJson == "string") {
					rules = JSON.parse(JSON.parse(this.data.nodeJson).deny_rules).rules.map(value => {
						return {
							"id": value.id,
							"parentId": value.parentId,
							"priority": value.priority,
							"code": value.code,
							"name": value.name
						}
					})

				} else {
					if (this.data.nodeJson !== null) {
						rules = this.data.nodeJson.rules.map(value => {
							return {
								"id": value.id,
								"parentId": value.parentId,
								"priority": value.priority,
								"code": value.code,
								"name": value.name
							}
						})
					}
				}
				return rules
			},
			getparams() {
				if (this.nodeJson !== null) {
					if (this.nodeJson.groupType == 1) {
						this.params.rules = this.nodeJson.mutexGroup.rules.map(value => {
							if (value.ruleVersionId) {
								return value.id + '/' + value.name + '/' + value.ruleVersionId
							} else {
								return value.id + '/' + value.name
							}
						})
					} else if (this.nodeJson.groupType == 2) {
						this.params.rules = this.nodeJson.executeGroup.rules.map(value => {
							if (value.ruleVersionId) {
								return value.id + '/' + value.name + '/' + value.ruleVersionId
							} else {
								return value.id + '/' + value.name
							}
						})
					}


					this.params.isSerial = parseInt(this.nodeJson.groupType)
				}
				// this.params.rules = this.params.rules.map(value => {
				// 	if (this.getVersion(value.split('/')[0]) != null) {
				// 		return value + '/' + this.getVersion(value.split('/')[0])
				// 	} else {
				// 		return value
				// 	}

				// })
				this.currRule = [...this.params.rules]

				this.tempSerial = this.params.isSerial;
				this.selRule(false);
			},
			getVersion(id) {
				let idnum = null
				this.ruleList.forEach(value => {
					if (value.id === Number(id) && value.difficulty == 2) {
						idnum = value.ruleVersionList[0].id
					}
				})
				return idnum
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
					// this.tempPage3CurrRule = [...this.nodeJson.terminationInfo.selectedRule];
					// this.tempPage3CurrRule.forEach((value, index) => {
					// 	if (value.id === parseInt(item.split('/')[0])) {
					// 		this.tempPage3CurrRule.splice(index, 1)
					// 	}
					// })
					// this.selectedRule.forEach((value, index) => {
					// 	if (value === item) {
					// 		this.selectedRule.splice(index, 1)
					// 	}
					// })
					this.dialogSure()

				}).catch(() => {
					this.$message({
						type: 'info',
						message: '已取消删除'
					});
				});
			},
			dialogSure() {
				this.params.isSerial = this.tempSerial
				this.params.rules = [...this.currRule]

				this.dialogVisible = false
			},
			delectcurrRule(str) {
				this.currRule.forEach((value, index) => {
					if (value === str) {
						this.currRule.splice(index, 1)
					}
				})

			},
			deepGetType(arr,obj){
				if(obj.children&&obj.children.length>0){
					obj.children.forEach(value=>{
						arr.push({id:value.id,name:value.name})
						this.deepGetType(arr,value)
					})
				}
				
				
				
			},
			async getType(param) {
				
				// param.treeType = String(param.treeType)
				const data = await getRulesListTree(param);
				if(data.status!='1') return
				let arr = []
				this.deepGetType(arr,data.data[0])
				
				return arr
			},
			async getTypetree(param) {
				const data = await getType2tree(param);
				return data
			},
			async setType(param) {
				const data = await setType4(param);
				return data
			},
		},
		watch: {
			dialogVisible() {
				// console.log(this.dialogVisible)
			},
			data() {
				this.search = ''
				this.elswitch = false
				this.page2 = 1
				this.page = 1
				this.activeName = 'first'
				this.params = {
					rules: [],
					isSerial: 1
				}
				this.tempSerial = 1
				this.currRule = []
				this.tree = null
				this.currtreetype = ""
				// this.ruleList = null
				this.dialogVisible = false
				this.dialogkey++
				this.currtree = ''
				this.selectedRule = []
				this.getparams()
				this.crea()
			},

		},
		computed: {
			Fielduser() {
				if (this.$store.state.FieldUser != null) {
					this.loading = false
					return this.$store.state.FieldUser.data.fieldList
				} else {
					return []
				}
			},
			ruleList() {
				if (this.$store.state.RuleList) {
					let arr = this.$store.state.RuleList
					arr.forEach(value => {

						if (value.ruleVersionList) {

							this.$set(value, 'ruleVersionId', value.ruleVersionList[0].id)

						}
					})

					return arr
				} else {
					return []
				}
			},
			fruleList() {
				if (this.ruleList) {
					let arr = this.ruleList.filter(value => {
						return value.parentId === this.currtree
					})
					let arr2 = arr.filter((value, index) => {
						return value.name.indexOf(this.search) != -1
					})

					return arr2
				} else {
					return []
				}
			},
			page3RuleList() {
				let arr = this.ruleArr.filter((value, index) => {
					return (index >= ((this.page3 - 1) * 10) && index < (this.page3 * 10))
				})
				return arr
			},
			pageRuleList() {

				let arr2 = this.fruleList.filter((value, index) => {
					return (index >= ((this.page - 1) * 10) && index < (this.page * 10))
				})

				return arr2
			},
			frules: {
				get() {
					if (this.params) {
						let arr = this.params.rules.filter((value, index) => {
							return (index >= ((this.page2 - 1) * 10) && index < (this.page2 * 10))
						})
						return arr
					} else {
						return []
					}
				},
				set(val) {
					let rulesArr = this.params.rules;
					rulesArr.splice((this.page2 - 1) * 10, val.length, ...val);
					this.params.rules = rulesArr
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
			paramsRulesObj() {
				let arr = this.params.rules.map(value => {
					return this.ruleList.find(x => x.id === parseInt(value.split('/')[0]))

				})

				return arr

			}
		}

	}
</script>

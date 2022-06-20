<style>
	.watchContLeft {
		width: 180px;
		margin-right: 10px;

	}

	.watchContLeft>div {
		width: 180px;
		background-color: #fff;
	}

	.watchContSelect {
		height: 5%;

	}

	.watchCont_nodeHitItem {
		height: 95%;
		padding: 5px;
	}

	.watchCont_param {
		height: 230px;
		justify-content: space-between;

		width: 900px;
		display: flex;

	}

	.watchCont_param>div {
		width: 49.5%;
		background-color: #fff;
		/* border-left: 1px solid #000; */
		overflow: scroll;
		padding: 10px;
		box-sizing: border-box;

	}

	.watchCont_param>div::-webkit-scrollbar {
		display: none;
	}

	.watchCont_right {
		margin-left: 10px;
		width: 40%;
		background-color: #fff;
		padding: 15px;
		overflow: auto;
		height: 86vh;
	}

	.watchCont_left_node {
		border: 1px solid #ddd;
		padding: 5px;
		box-sizing: border-box;
		margin-top: 5px;
		font-size: 12px;
		border-radius: 5px;
		background-color: #fdfdfd;
	}

	.watchCont_left_node:hover {
		background-color: #f1f1f1;
		cursor: pointer;
	}

	.type_header,
	.result-wrapper {
		font-size: 14px;
		font-weight: bold;
	}

	.result-wrapper {
		font-weight: normal;
	}

	.type_title {
		background-color: #aaa;
		height: 30px;
		line-height: 30px;
		color: #fff;
		padding: 10px;
		box-sizing: border-box;
		margin: 10px 0;
		display: flex;
		align-items: center;
		justify-content: space-between;
	}

	.node-content-wrapper {
		padding-left: 10px;
		position: relative;
	}

	.node-content-wrapper-mode {
		position: absolute;
		width: 100%;
		height: 100%;

		z-index: 9999;

	}
</style>

<template>
	<div style="display: flex;overflow: hidden;" v-loading="loading">
		<div class="watchContLeft">
			<div class="watchContSelect">
				<el-select v-model="selectType" placeholder="请选择节点类型" clearable>
					<el-option v-for="item in nodes" :key="item.node.type" :label="item.node.text"
						:value="item.node.type" v-if="item.node.type!=1">
					</el-option>
				</el-select>
			</div>
			<div class="watchCont_nodeHitItem">
				<div v-for="item in fnode" v-if="item.item.node.type!==1" class="watchCont_left_node"
					@dblclick="getcontent(item.nodeId)">
					节点类型:{{item.item.node.text}} <br>
					节点名称:{{item.text}}
				</div>

			</div>
		</div>
		<div>
			<canvas id="canvas" height="600px" width="900px"></canvas>
			<div class="watchCont_param">
				<div>
					<p>
						引擎信息：
						{{baseInfo.engineName}}
					</p>
					<p>
						业务ID:
						{{baseInfo.businessId}}
					</p>
				</div>
				<div style="position: relative;">
					入参：
					<pre style="overflow-x: auto;">{{JSON.stringify(param,null,4)}}</pre>
					<el-button type="text" style="position: absolute;top:10px;right: 10px;" @click="getCnparam">查看指标名
					</el-button>
				</div>
			</div>
		</div>

		<div class="watchCont_right">
			<div>
				<p class="type_header">节点信息：</p>
				<div v-if="monitorInfo!=null">
					<p class="type_header">节点名称:{{currNode.item.node.text}} </p>
					<div v-if="currNode.item">
						<div>
							<p class="type_title">
								节点内容：
							</p>

							<div class="node-content-wrapper">
								<div class="node-content-wrapper-mode">

								</div>


								<type2 :data="monitorInfo.snapshot" :readOnly="true" :rowkey="rowKey"
									v-if="currNode.item.node.type==2"></type2>
								<type3 :data="monitorInfo.snapshot" :readOnly="true" :rowkey="rowKey"
									v-if="currNode.item.node.type==3"></type3>
								<type4 :data="monitorInfo.snapshot" :readOnly="true" :rowkey="rowKey"
									v-if="currNode.item.node.type==4"></type4>
								<type5 :data="monitorInfo.snapshot" :readOnly="true" :rowkey="rowKey"
									v-if="currNode.item.node.type==5"></type5>
								<type7 :data="monitorInfo.snapshot" :readOnly="true" :rowkey="rowKey"
									v-if="currNode.item.node.type==7"></type7>
								<type9 :data="monitorInfo.snapshot" :readOnly="true" :rowkey="rowKey"
									v-if="currNode.item.node.type==9"></type9>
								<type14 :data="monitorInfo.snapshot" :readOnly="true" :rowkey="rowKey"
									v-if="currNode.item.node.type==14"></type14>
								<type15 :data="monitorInfo.snapshot" :readOnly="true" :rowkey="rowKey"
									v-if="currNode.item.node.type==15"></type15>
								<type16 :data="monitorInfo.snapshot" :readOnly="true" :rowkey="rowKey"
									v-if="currNode.item.node.type==16"></type16>
								<type17 :data="monitorInfo.snapshot" :readOnly="true" :rowkey="rowKey"
									v-if="currNode.item.node.type==17"></type17>
								<type18 :data="monitorInfo.snapshot" :readOnly="true" :rowkey="rowKey"
									v-if="currNode.item.node.type==18"></type18>
								<type19 :data="monitorInfo.snapshot" :readOnly="true" :rowkey="rowKey"
									v-if="currNode.item.node.type==19"></type19>
								<type20 :data="monitorInfo.snapshot" :readOnly="true" :rowkey="rowKey"
									v-if="currNode.item.node.type==20"></type20>
								<type21 :data="monitorInfo.snapshot" :readOnly="true" :rowkey="rowKey"
									v-if="currNode.item.node.type==21"></type21>

							</div>

						</div>
						<div>

						</div>
						<div class="result-wrapper">
							<p class="type_title" v-if="currNode.item.node.type!=9">
								最终结果：
							</p>

							<span v-if="currNode.item.node.type==4">得分：{{monitorInfo.result.score}}</span>
							<p v-if="currNode.item.node.type==2">
								得分：{{monitorInfo.result.scoreTotal?monitorInfo.result.scoreTotal:monitorInfo.result['2_'+currentNodeId+'_score']}}
							</p>
							<p v-if="currNode.item.node.type==2">
								命中条数：{{monitorInfo.result.hitNum?monitorInfo.result.hitNum:monitorInfo.result['2_'+currentNodeId+'_size']}}
							</p>


							<p v-if="currNode.item.node.type==5">
								结果：{{monitorInfo.result?monitorInfo.result.resultJson:'无'}}</p>
							<p v-if="currNode.item.node.type==7">
								结果：无
							</p>
							<p v-if="currNode.item.node.type==3">
								结果：无
							</p>
							<p v-if="currNode.item.node.type==16">
								结果：{{monitorInfo.result.result[0].result?monitorInfo.result.result[0].result:"无"}}</p>
							<p v-if="currNode.item.node.type==17">
								结果：{{monitorInfo.result.result[0].result?monitorInfo.result.result[0].result:"无"}}</p>
						</div>

						<div>
							<p class="type_title">
								节点入参：
							</p>
							<pre style="overflow-x: auto;">{{JSON.stringify(monitorInfo.params,null,4)}}</pre>

							<p class="type_title">
								节点出参：
							</p>
							<pre style="overflow-x: auto;">{{JSON.stringify(monitorInfo.result,null,4)}}</pre>
						</div>

						<div v-if="StrategyList.length>0">
							<p class="type_title">
								策略详情：
							</p>
							<div v-for="value in StrategyList" style="padding-left: 20px;">


								<span style="margin-right: 20px;">
									<i class="el-icon-success" style="color: #07C4A8;"
										v-if="value.Result!='未命中'&&value.Result!=''"></i>
									<i class="el-icon-error" style="color: #c22020;" v-else></i>
								</span>
								<el-button type="text" size="medium" @click="getStrategys({...value})">{{value.name}}
								</el-button>

								<span style="margin-left: 20px;font-size: 14px;"
									v-if="value.Score">得分:{{value.Score}}</span>

								<span style="margin-left: 20px;font-size: 14px;"
									v-if="value.Result">结果:{{value.Result}}</span>

							</div>


						</div>


					</div>
				</div>
			</div>
		</div>

		<el-dialog title="中文指标" :visible.sync="dialogVisible" width="30%">
			<pre>{{paramCn}}</pre>
			<span slot="footer" class="dialog-footer">
				<el-button type="primary" @click="dialogVisible = false">确 定</el-button>
			</span>
		</el-dialog>

		<watchStrategy v-if="strategyDialogVisible" :strategyDialogVisible.sync="strategyDialogVisible"
			:dataCont="strategyItem"></watchStrategy>

	</div>
</template>

<script>
	import zoomView from '@/components/common/ZoomView.vue'
	// import miniSCO from '@/components/common/miniSCO.vue'
	import type4 from '@/components/typecom/type4.vue'
	import type3 from '@/components/typecom/type3.vue'
	import type5 from '@/components/typecom/type5.vue'
	import type7 from '@/components/typecom/type7.vue'
	import type9 from '@/components/typecom/type9.vue'
	import type2 from '@/components/typecom/type2.vue'
	import type15 from '@/components/typecom/type15.vue'
	import type14 from '@/components/typecom/type14.vue'
	import type16 from '@/components/typecom/type16.vue'
	import type17 from '@/components/typecom/type17.vue'
	import type18 from '@/components/typecom/type18'
	import type19 from '@/components/typecom/type19'
	import type20 from '@/components/typecom/type20'
	import type21 from '@/components/typecom/type21'
	import {
		getMonitorDecisionFlow,
		getMonitorNode,

		getMonitorDecisionFlowMysql,
		getMonitorNodeMysql,

		getStrategy,
		getStrategyMysql
	} from '@/api/index.js'
	import {
		nodes
	} from '@/utils/nodeList.js'
	import watchStrategy from '@/components/common/watch_strategy.vue'
	export default {
		components: {

			zoomView,
			// miniSCO,
			type17,
			type16,
			type9,
			type3,
			type7,
			type5,
			type14,
			type15,
			type2,
			type4,
			type18,
			type19,
			type20,
			type21,
			watchStrategy
		},
		data() {
			return {
				loading: false,
				currNode: {},
				selectType: '',
				baseInfo: {},
				param: {},
				paramCn: null,
				id: '',
				rowKey: "",
				monitorInfo: null,
				// canvas: null,
				stage: null,
				scene: null,
				path: './img',
				Node: [],
				hitPath: [],
				nodes: nodes,
				currentNodeId: null,
				dialogVisible: false,
				getType: 'Mysql',
				flowId: '',
				StrategyList: [],
				strategyItem: {},
				strategyDialogVisible: false
			}
		},
		created() {
			// this.id = this.$route.params.id
			this.init();

		},
		watch: {

		},
		methods: {
			getCnparam() {
				if (this.paramCn == null) {
					this.paramCn = this.getCn(this.param)
				}
				this.dialogVisible = true
			},
			getCn(param) {
				let str = JSON.stringify(param, null, 4)
				let arr = JSON.stringify(param, null, 4).match(/\".+?\":/g)

				arr.forEach(value => {
					let tempValue = value.substring(1, value.length - 2)
					let cn = this.mixinGetCnByEn(tempValue)

					if (cn) {
						str = str.replace(tempValue, cn)
					}
				})
				return str
			},
			init() {

				this.id = this.$route.params.id.split('|')[1]
				this.rowKey = this.$route.params.id.split('|')[0]
				this.loading = true;

				this.getMonitorDecisionFlows({
					id: this.id,
					rowKey: this.rowKey
				}).then(res => {
					this.loading = false;
					if (res.status === '1') {
						if (res.data.length == 0) {
							return
						}
						console.log(res.data)
						this.hitPath = res.data[0].monitorInfo.process
						this.param = res.data[0].monitorInfo.params
						this.baseInfo = res.data[0].baseInfo
						this.initTopo()
						this.NodeStart(res.data[0].monitorInfo.snapshot)
					}
				})
			},
			getcontent(nodeId) {
				this.currentNodeId = nodeId;
				let Tempitem
				this.Node.forEach(value => {
					if (value.id == nodeId) {
						Tempitem = value
					}
				})
				// this.getStrategys({
				// 	nodeId:nodeId,

				// })
				this.getMonitorNodes({
					hbaseRowKey: this.id + nodeId,
					nodeId: nodeId,

				}).then(res => {
					if (res.status == "1") {

						if (res.data.length > 0) {
							this.currNode = Tempitem
							this.rowKey = res.data[0].rowKey;
							this.monitorInfo = res.data[0].monitorInfo
							this.monitorInfo.snapshot = {
								nodeJson: res.data[0].monitorInfo.snapshot.nodeSnapshot?JSON.stringify(res.data[0].monitorInfo.snapshot.nodeSnapshot):JSON.stringify(res.data[0].monitorInfo.snapshot),
								id: res.data[0].baseInfo.nodeId
							}

							this.setStrategyList(JSON.parse(JSON.stringify(res.data[0])))

						} else {
							this.$message.error('此节点没有快照信息')
						}
					}
				})
			},
			setStrategyList(res) {
				this.StrategyList = []

				console.log(res)
				let obj = {
					'2': {
						result: 'ruleResultList',
						name: 'ruleName',
						ruleId: 'ruleId',
						VersionId: 'ruleVersionId',
						Result: 'ruleResult',
						Score: 'ruleScore',
					},
					'4': {
						result: '',
						name: 'cardName',
						ruleId: 'cardId',
						VersionId: 'cardVersionId',
						// Result: 'score',
						Score: 'score',
					},
					'16': {
						result: 'result',
						name: 'decisionTablesName',
						ruleId: 'decisionTablesId',
						VersionId: 'versionId',
						Result: 'result',
						// Score:'ruleScore',
					},
					'17': {
						result: 'result',
						name: 'decisionTreeName',
						ruleId: 'decisionTreeId',
						VersionId: 'versionId',
						Result: 'result',
						// Score:'ruleScore',
					},
				}

				let subobj = obj[res.baseInfo.nodeType]
				
				if(!subobj) return
				
				let arr
				if (subobj.result) {
					this.StrategyList = res.monitorInfo.result[subobj.result].map(value => {
						return {
							id: res.id,
							name: value[subobj.name],
							ruleId: value[subobj.ruleId],
							VersionId: value[subobj.VersionId],
							Result: value[subobj.Result],
							Score: value[subobj.Score],
							nodeId: res.monitorInfo.snapshot.id,
							hbaseRowKey: this.id + res.monitorInfo.snapshot.id
						}
					})
				} else {
					this.StrategyList = [
						{
							id: res.id,
							name: res.monitorInfo.result[subobj.name],
							ruleId: res.monitorInfo.result[subobj.ruleId],
							VersionId: res.monitorInfo.result[subobj.VersionId],
							Result: res.monitorInfo.result[subobj.Result],
							Score: res.monitorInfo.result[subobj.Score],
							nodeId: res.monitorInfo.snapshot.id,
							hbaseRowKey: this.id + res.monitorInfo.snapshot.id
						}
					]
				}


				if (res.baseInfo.nodeType == 2) { //规则节点数据格式化

					this.StrategyList.forEach(value => {

						let rule = JSON.parse(res.monitorInfo.snapshot.nodeJson).mutexGroup.rules.find(x => x.id ==
							value.ruleId)
						let rule2 = JSON.parse(res.monitorInfo.snapshot.nodeJson).executeGroup.rules.find(x => x
							.id == value.ruleId)
						if (rule) {
							value.difficulty = rule.difficulty
						}
						if (rule2) {
							value.difficulty = rule2.difficulty
						}

					})
				}







			},
			initTopo() {
				let canvas = document.getElementById('canvas');
				this.stage = new JTopo.Stage(canvas);
				this.stage.wheelZoom = null;
				this.scene = new JTopo.Scene(this.stage);
				this.scene.background = "./img/decisionBcg.jpg"
			},
			NodeStart(item) {
				this.Node = []
				this.scene.clear()
				item.forEach(value => {
					var nodes = new JTopo.Node(value.nodeName);
					let nodeitem = ''
					this.nodes.forEach(item => {
						if (item.node.type == value.nodeType) {
							nodeitem = item
						}
					})
					nodes.setLocation(value.nodeX, value.nodeY);
					nodes.showSelected = true; // 不显示选中矩形
					nodes.fontColor = "48,48,48";
					nodes.borderRadius = 22.5;
					nodes.font = "12px";
					nodes.item = nodeitem;
					nodes.is = false;
					nodes.dragable = false

					if (nodes.item.node.url2) {
						nodes.textOffsetY = 4
						nodes.fontColor = '255,255,255'
						nodes.textPosition = 'Middle_Center'
						nodes.setSize(80, 40);
						nodes.setImage(this.path + nodeitem.node.url2)
					} else {
						nodes.textOffsetY = -5
						nodes.setImage(this.path + nodeitem.node.url)
					}
					// 
					nodes.drag = false;
					nodes.nextNode = '';
					nodes.Vid = this.currversions
					nodes.nodeJson = value.nodeJson
					nodes.nodeCode = value.nodeCode
					nodes.nodeOrder = value.nodeOrder
					nodes.parentId = value.parentId;
					nodes.id = value.nodeId
					nodes.dbclick((e) => {
						this.getcontent(nodes.id)
					})
					if (nodes.item.node.type == 7 || nodes.item.node.type == 3) {
						if (nodes.item.node.type == 7) {
							if (typeof value.nodeJson == "string") {
								nodes.nodeNexts = JSON.parse(value.nodeJson)
							} else {
								nodes.nodeNexts = value.nodeJson
							}
						} else {
							if (value.nodeJson) {
								if (typeof value.nodeJson == "string") {
									// console.log(1, nodes)
									nodes.nodeNexts = JSON.parse(value.nodeJson).conditions.map((value, index) => {
										return {
											proportion: value.group_name,
											sandbox: index + 1,
											nextNode: value.nextNode
										}
									})
								} else {
									nodes.nodeNexts = value.nodeJson.conditions.map((value, index) => {
										return {
											proportion: value.group_name,
											sandbox: index + 1,
											nextNode: value.nextNode
										}
									})
								}
							}
						}

					}

					this.Node.push(nodes)
					this.scene.add(nodes);
				})
				console.log(this.Node);
				this.Node.forEach(value => {
					if (value.parentId !== null) {
						this.Node.forEach(item => {
							String(value.parentId).split(',').forEach(cont => {
								if (item.id == cont) {
									let link = new JTopo.Link(item, value)
									if (item.item.node.type == 7 || item.item.node.type == 3) {
										item.nodeNexts.forEach(cont => {
											if (cont.nextNode == value.nodeCode) {
												link.text = cont.proportion
												link.fontColor = '#000'
											}
										})
									}
									if (this.getColor(item.id, value.id, item.item.node.type ==
											1)) {
										link.strokeColor = '0,200,0'
										link.lineWidth = 1.5
									} else {
										link.strokeColor = '170,170,170'
										link.lineWidth = 1
									}
									// link.alpha = 0.1
									item.nextNode = value.id
									link.arrowsRadius = 10

									value.is = !value.is
									this.scene.add(link);
								}
							})
						})
					}
				})
				// this.stage.setCenter(this.Node[0].x+500, this.Node[0].y)
				this.stage.centerAndZoom()
			},
			getColor(id1, id2, is = false) {
				if (is) {
					return true
				}
				let num = null
				this.hitPath.forEach((value, index) => {
					if (value == id1) {
						num = index + 1
					}
				})
				if (this.hitPath[num] == id2) {
					return true
				} else {
					return false
				}
			},
			getStrategys(param) {
				getStrategyMysql({
					strategyId: param.VersionId,
					nodeId: param.nodeId,
					monitorParentId: param.id
				}).then(res => {
					if (res.status == '1') {

						this.strategyDialogVisible = true
						this.strategyItem = res.data[0]
						console.log(this.strategyItem)
						if (param.difficulty) {
							this.strategyItem.difficulty = param.difficulty
						}
					}
				})
			},
			getMonitorNodes(param) {
				return new Promise(async (reslove) => {
					var res

					if (this.getType == 'Mysql') {

						let obj = {
							nodeId: param.nodeId,
							monitorParentId: this.flowId
						}
						res = await getMonitorNodeMysql(obj)

						if (res.status == 1 && res.data.length) {
							res = {
								data: [{
									id: res.data[0].id,
									monitorInfo: {
										params: JSON.parse(res.data[0].input),
										snapshot: JSON.parse(res.data[0].snapshot),
										result: JSON.parse(res.data[0].output)
									},
									baseInfo: {
										"businessId": res.data[0].businessId,
										"nodeId": res.data[0].nodeId,
										"nodeName": res.data[0].nodeName,
										"nodeType": res.data[0].nodeType,
										"engineVersionId": res.data[0].engineVersionId,

									}
								}],
								error: res.status,
								msg: res.status,
								status: res.status,
							}
						}

					} else {
						let obj = {
							hbaseRowKey: param.hbaseRowKey
						}
						res = await getMonitorNode(obj)
					}
					// console.log(res)
					reslove(res)

				})
			},
			getMonitorDecisionFlows(param) {
				return new Promise(async (reslove) => {
					var res
					if (this.getType == 'Mysql') {
						let obj = {
							resultId: param.id
						}
						res = await getMonitorDecisionFlowMysql(obj)
						if (!res.data[0]) {
							this.$message.error('此记录无数据')
							reslove({
								status: 1,
							})
						}
						this.flowId = res.data[0].id
						res = {
							data: [{
								rowKey: param.rowKey,
								monitorInfo: {
									snapshot: JSON.parse(res.data[0].snapshot),
									params: JSON.parse(res.data[0].input),
									process: JSON.parse(res.data[0].process)
								},
								baseInfo: {
									businessId: res.data[0].engineId,

									engineName: res.data[0].engineName,
									engineVersionId: res.data[0].engineVersionId
								}
							}],
							error: res.error,
							msg: res.msg,
							status: res.status,
						}

					} else {
						let obj = {
							hbaseRowKey: param.rowKey
						}
						res = await getMonitorDecisionFlow(obj)
					}

					reslove(res)
				})
			}

		},
		computed: {
			fnode() {
				let arr = this.Node.map(value => {
					return {
						text: value.text,
						item: value.item,
						nodeId: value.id
					}
				})

				if (this.selectType !== "") {
					arr = arr.filter(value => {
						return value.item.node.type == this.selectType
					})
				}
				return arr
			}
		}
	}
</script>

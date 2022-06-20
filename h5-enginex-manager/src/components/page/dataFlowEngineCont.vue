<template>
	<div style="height: 100%;overflow: auto;position: relative;" v-loading="loading">


		<div class="dataFlowCont_left_versions">
			<!-- 预留左侧更改版本相关 -->
		</div>
	<!-- 	<div style="position: absolute;right: 10%;top: 20px;">
			<el-button type="primary" @click="submit">提交</el-button>
		</div> -->
		<div class="dataFlowCont_MqSelect">

			<el-select v-model="sourceId" placeholder="请选择消息队列" @change="sourceIdChange">
				<el-option v-for="item in MqList" :key="item.id" :label="item.name" :value="item.id">
				</el-option>
			</el-select>
			<p style="color: red;font-size: 14px;">
				*修改消息队列将导致所有节点清空 ， 请谨慎修改
			</p>
		</div>
		<div>

		<div class="dataFlowCont_center_cont" :style="{display: drawer?'flex':'block'}">

				<div>
					<div class="dataFlowCont_node dataFlowCont_node_start">
						<div style="height: 16px;">
					
						</div>
						<div>
					
							<div style="color: #81E211;font-size: 20px;font-weight: bold;text-align: center;">
								开始
							</div>
					
						</div>
						<div>
							<el-button icon="el-icon-plus" size="small" circle @click="addNode(-1)"></el-button>
							<el-button type="danger" disabled size="small" icon="el-icon-delete" circle></el-button>
						</div>
						<div class="dataFlowCont_node_next">
							<div class="dataFlowCont_node_next_arrows">
					
							</div>
						</div>
					</div>
					
					
					<draggable v-model="nodeList">
						<transition-group type="transition">
							<div class="dataFlowCont_node" v-for="(value,index) in nodeList" :key="index">
								<div style="font-size: 14px;">
									<!-- <textInput :text="value.nodeName" :examine='0' @input="value.nodeName=$event;"
										defcolor="#fff"></textInput> -->
										{{value.nodeName===''?'未命名':value.nodeName}}
								</div>
								<div @click="setItem(value)" style="flex: 1;">
									<p class="dataFlowCont_node_text">
										<span>{{value.nodeType?value.nodeType:'未选择'}}</span>
										<span>({{value.fixedValue}}</span>
										<span
											v-if="userTimeOperator.indexOf(value.nodeType)!=-1">{{timeUnitList.find(x=>x.value==value.timeUnit).label}}</span>
										<span>)</span>
									</p>
					
								</div>
								<div>
									<el-button icon="el-icon-plus" size="small" circle @click="addNode(index)"></el-button>
									<el-button type="danger" size="small" icon="el-icon-delete" @click="detect(index)"
										circle>
									</el-button>
								</div>
								<div class="dataFlowCont_node_next">
									<div class="dataFlowCont_node_next_arrows">
					
									</div>
								</div>
							</div>
						</transition-group>
					</draggable>
					
					
					
					<div class="dataFlowCont_node dataFlowCont_node_end">
						<div style="height: 16px;">
					
						</div>
						<div>
					
							<div style="color: #AA7520;font-size: 20px;font-weight: bold;text-align: center;">
								结束
							</div>
						</div>
					
					</div>
					
				</div>












			</div>



		</div>
		<!-- {{tempValue}} -->
		<el-drawer title="节点内容" v-if="drawer" :visible.sync="drawer" direction="rtl" :before-close="handleClose"
			size="50%" @close="interiorSubmit=()=>{}">

			<div style="padding: 10px;">
				<div>
					<p style="font-size: 14px;color: #999;">节点名称 :</p>
					<el-input v-model="tempValue.nodeName" style="margin: 5px;">

					</el-input>
				</div>
				<div>
					<p style="font-size: 14px;color: #999;">
						请选择算子 :
					</p>
					<div style="display: flex; flex-wrap: wrap;padding: 10px;">
						<el-radio v-model="tempValue.nodeType" :label="value.label" border
							v-for="value in $store.state.operator" style="margin-top: 10px;" @change="nodeTypeChange">
						</el-radio>
					</div>

				</div>
				<div v-if="operatorValueType==1">
					<p style="font-size: 14px;color: #999;">固定值:</p>
					<div style="padding: 20px;">
						<el-input v-model="tempValue.fixedValue" placeholder="请输入固定值"
							style="width: 200px;margin-right: 10px;"></el-input>
						<el-select v-model="tempValue.timeUnit" placeholder="请选择时间单位"
							v-if="userTimeOperator.indexOf(tempValue.nodeType) != -1">
							<el-option v-for="item in timeUnitList" :key="item.value" :label="item.label"
								:value="item.value">
							</el-option>
						</el-select>
					</div>
				</div>
				<div v-if="operatorValueType==2">
					<p>规则:</p>
					<GeneralRule :ZIndex="1" :data="tempValue.condition[0]" :customUserObj="MqValue"></GeneralRule>
				</div>
				<div v-if="operatorValueType==0" style="color: #999;">
					此算子无需输入值
				</div>
			</div>


			<div
				style="position: absolute;bottom:4%;right: 10%;display: flex;flex-direction: column;align-items: flex-end;">
				<el-button type="primary" @click="interiorSubmit()">保存</el-button>
				<p style="color: red;font-size: 14px;">
					<!-- *此保存仅为临时保存，并不会存储到服务器中，请点击页面中的 提交 按钮进行提交 -->
				</p>
			</div>





		</el-drawer>
	</div>
</template>

<script>
	import {
		getDataEngineVersionList,
		getDataEngineVersionInfo,
		updateDataEngineVersionInfo
	} from '@/api/index.js'
	import draggable from "vuedraggable"
	import GeneralRule from '@/components/common/GeneralRule.vue'
	import {
		deepexamine,
		ruleEnformat
	} from '@/utils/GetdeepObj.js'
	import textInput from '@/components/common/textInput.vue'
	const node = {
		nodeName: '',
		nodeType: '',
		condition: [{
			"logical": '&&',
			"fieldId": "",
			"operator": "",
			"fieldValue": "",
			"conditionType": 1,
			"children": [],
		}],
		fixedValue: '',
		timeUnit: ''
	}

	export default {
		name: 'dataFlowEngineCont',
		components: {
			draggable,
			GeneralRule,
			textInput
		},
		data() {
			return {
				loading: false,
				nodeList: [],
				sourceId: '',
				drawer: false,
				tempValue: {},
				timeUnitList: [{
						label: '天',
						value: 'DAYS'
					},
					{
						label: '小时',
						value: 'HOURS'
					},
					{
						label: '分钟',
						value: 'MINUTES'
					},
					{
						label: '秒',
						value: 'SECONDS'
					},
					{
						label: '毫秒',
						value: 'MILLISECONDS'
					},
					{
						label: '微秒',
						value: 'MICROSECONDS'
					},
					{
						label: '纳秒',
						value: 'NANOSECONDS'
					},
				],
				timeUnit: '',
				interiorSubmit: () => {},
				userTimeOperator: ['within'],
				VersionId: '',
				lastSourceId:''
			}
		},
		created() {
			if (this.$route.params.id) {
				this.getversions(this.$route.params.id)

			} else if (localStorage.getItem('dataFlowEngineId')) {
				this.getversions(Number(localStorage.getItem('dataFlowEngineId')))

			}
		},
		computed: {
			MqList() {
				return this.$store.state.Mqlist
			},
			operator() { //算子
				return this.$store.state.operator
			},
			operatorValueType() {
				let obj = this.$store.state.operator.find(x => x.label == this.tempValue.nodeType)
				return obj ? obj.valueType : 1
			},
			MqValue() { //队列的内容
				let obj = this.MqList.find(x => x.id == this.sourceId)
				return obj && JSON.parse(obj.messageBody)
			},

		},
		methods: {
			sourceIdChange() {
				if(!this.lastSourceId){
					return
				}
				this.$confirm('修改消息队列将导致所有节点清空，确定切换?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.lastSourceId = this.sourceId 
					this.nodeList = []
					this.submit()
				}).catch(() => {
					this.sourceId = this.lastSourceId
				});
			},
			submit() {

				if (this.sourceId == '') {
					this.$message.error('请选择队列')
					return
				}

				this.loading = true
				updateDataEngineVersionInfo({
					engineVersionId: this.VersionId,
					engineContent: JSON.stringify({
						nodeList: this.nodeList,
						sourceId: this.sourceId
					}),
					engineType: "data_flow_engine"
				}).then(res => {
					if (res.status == '1') {
						this.$message.success('提交成功')
					}
					this.loading = false
				})
			},

			interiorVerify() {
				let is = {
					is: false,
					msg: ''
				}

				if (this.tempValue.nodeType == '') {
					is.is = true
					is.msg = '请选择算子'
				}
				if (this.operatorValueType == 1 && this.tempValue.fixedValue.trim() == '') {
					is.is = true
					is.msg = '请填写固定值'
				}
				if (this.userTimeOperator.indexOf(this.tempValue.nodeType) != -1 && this.tempValue.timeUnit == '') {
					is.is = true
					is.msg = '请填写时间单位'
				}


				if (this.operatorValueType == 2) {
					deepexamine(is, this.tempValue.condition)
				}










				if (is.is) {
					this.$message.error(is.msg)

				}

				return is.is

			},
			nodeTypeChange(e) {
				if (this.operatorValueType == 1) {
					this.tempValue.condition = JSON.parse(JSON.stringify(node.condition))
				} else if (this.operatorValueType == 2) {
					this.tempValue.fixedValue = ''

				}

				if (this.userTimeOperator.indexOf(e) == -1) {
					this.tempValue.timeUnit = ''
				}
			},
			setItem(value) {

				this.tempValue = JSON.parse(JSON.stringify(value))
				ruleEnformat(this.tempValue.condition, 'Array')
				this.drawer = true
				this.interiorSubmit = () => {

					if (this.interiorVerify()) {
						return
					}


					value.nodeName = this.tempValue.nodeName
					value.nodeType = this.tempValue.nodeType
					value.condition = JSON.parse(JSON.stringify(this.tempValue.condition))
					value.fixedValue = this.tempValue.fixedValue
					value.timeUnit = this.tempValue.timeUnit
					ruleEnformat(value.condition, 'String')
					this.submit()
					this.$message.success('保存成功')
					this.drawer = false
				}
			},
			handleClose(done) {
				this.$confirm('确认关闭？系统不会保存未提交的内容')
					.then(_ => {
						done();
					})
					.catch(_ => {});
			},
			detect(index) {
				this.$confirm('删除此节点后将无法找回', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {

					this.nodeList.splice(index, 1)
					this.submit()
					this.$message({
						type: 'success',
						message: '删除成功!'
					});
				})
			},
			getversions(id) {
				localStorage.setItem('dataFlowEngineId', id)
				getDataEngineVersionList({
					engineId: id
				}).then(res => {
					if (res.status == '1') {

						this.VersionId = res.data[0].versionId
						this.getCont(res.data[0].versionId)
					}
				})
			},
			addNode(index) {
				if (!this.sourceId) {
					this.$message.warning('请优先选择队列')
					return
				}
				let obj = JSON.parse(JSON.stringify(node))
				obj.nodeName = '节点' + (this.nodeList.length + 1)
				this.nodeList.splice(index + 1, 0, obj)
				this.submit()
			},
			getCont(vid) {
				getDataEngineVersionInfo({
					engineVersionId: vid
				}).then(res => {
					if (res.status == '1') {
						this.nodeList = JSON.parse(res.data.engineContent).nodeList
						this.sourceId = JSON.parse(res.data.engineContent).sourceId ? JSON.parse(res.data
							.engineContent).sourceId : ''
						this.lastSourceId = this.sourceId

					}
				})
			},

		},
		watch: {
			'$route.params.id'() {
				if (this.$route.params.id) {
					this.getversions(this.$route.params.id)

				}
			}
		}
	}
</script>

<style>
	.dataFlowCont_center_cont {
		/* background-color: #0000FF; */
		margin: 0 auto;
		width: 900px;
		
		/* height: 300px; */
		/* border: 1px solid #0000FF; */
	}

	.dataFlowCont_node {
		
		background-color: #fff;
		width: 300px;
		height: 100px;
		border-radius: 10px;
		margin: 50px auto;
		position: relative;

		display: flex;
		flex-direction: column;

	}

	.dataFlowCont_node:hover {
		cursor: pointer;
	}

	.dataFlowCont_node>div:nth-of-type(1) {
		background-color: #409EFF;
		color: #fff;
		padding: 2px;
		border-radius: 10px 10px 0 0;
	}

	.dataFlowCont_node>div:nth-of-type(2) {
		padding-left: 10px;
		padding-top: 5px;
	}

	.dataFlowCont_node>div:nth-of-type(3) {
		position: absolute;
		right: 10px;
		bottom: -10px;
	}

	.dataFlowCont_node_start {
		margin-top: 10px;
		cursor: unset !important;
	}

	.dataFlowCont_node_start>div:nth-of-type(1) {
		background-color: #81e211;

	}

	.dataFlowCont_node_end {
		cursor: unset !important;
	}

	.dataFlowCont_node_end>div:nth-of-type(1) {
		background-color: #aa7520;
	}

	.dataFlowCont_node_next {
		height: 50px;
		width: 2px;
		background-color: #cbd2ff;
		position: absolute;
		left: 0;
		bottom: -45px;
		right: 0;
		margin: auto;

	}

	.dataFlowCont_node_next_arrows {
		width: 10px;
		height: 10px;
		margin-left: -4px;
		/* background-color: #00D1B2; */
		position: absolute;
		bottom: 0;
		border-left: 2px solid #cbd2ff;
		border-bottom: 2px solid #cbd2ff;
		transform: rotateZ(-45deg);
		/* margin: auto; */
	}

	.dataFlowCont_node_text {
		font-size: 18px;
		user-select: none;
	}

	.dataFlowCont_node_text>span {
		color: #2a8f2f;
	}

	.dataFlowCont_MqSelect {
		position: absolute;
		left: 20px;
		top: 20px;
	}
</style>

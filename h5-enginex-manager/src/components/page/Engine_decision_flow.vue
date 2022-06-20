<style>
	.engine {
		margin-bottom: 10px;
	}

	.engine_decision {
		height: 100%;
	}

	#canvas {
		transition: 0.3s;
	}

	.decision_header {
		margin-top: -10px;
		height: 5%;
		display: flex;
		/* justify-content: space-around; */
		align-items: center;
		font-size: 12px;
		min-width: 1100px;

	}

	.decision_header_button {
		width: 350px !important;
		user-select: none;
		/* display: flex; */
		/* justify-content: space-around; */
	}

	.decision_header_button>div {
		width: 100px;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
	}

	.decision_header_button>div>button {
		width: 28px;
		height: 28px;
		padding: 0 !important;
		display: flex;
		align-items: center;
		justify-content: center;
		background-color: #409EFF;
	}

	.decision_header_button>div>button>span>img {
		height: 15px;
	}

	.decision_header>div {
		width: 8.5%;
		display: flex;
		justify-content: center;
		margin-right: 20px;
	}

	.canvas_home {
		height: 100%;
		user-select: none;
	}

	.decision {
		display: flex;
		height: 95%;
		width: 100% !important;

	}

	.decision_left {
		z-index: 9;
		position: fixed;
		/* display: none; */
		background-color: #F0F0F0;
		height: 100%;
		flex-shrink: 0;
		transition: all 0.3s;
	}

	.decision_left_close {
		width: 50px;
	}

	.decision_left_open {
		width: 150px;
	}

	.decision_right {
		position: fixed;
		right: 0;
		height: 100%;
		flex-shrink: 0;
		user-select: none;
		transition: all 0.3s;
		z-index: 30;
	}

	.decision_right .el-tabs {
		/* height: 100%; */
	}

	.decision_right .el-tabs__nav {
		white-space: unset !important;

	}

	.decision_right .el-tabs .el-tabs__item {
		width: 10px;
		height: 100px;
		line-height: 23px !important;
	}

	.closebutton {
		font-size: 14px;
		font-weight: bold;
		color: #222;
		width: 100%;
		background-color: #F0F0F0;
	}

	.nodes {
		display: flex;
		align-items: center;
		padding: 7px;
		/* height: 30px; */
		/* border-bottom: 1px solid #ddd; */
		overflow: hidden;
	}

	.nodes {
		user-select: none;
	}

	.nodes>span {
		margin-left: 10px;
		white-space: nowrap;
	}

	.nodes>img {
		height: 30px;
	}

	.versions>div:nth-of-type(1) {
		height: 500px;
	}

	.versions_title {
		background-color: #eee;
		padding: 3px;
		border-radius: 3px;
	}

	.NodeRename>div {
		display: flex;
		/* margin-top: 5px; */
		justify-content: space-between;
		padding: 5px;
		box-sizing: border-box;
	}

	.decision_right_header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		border-bottom: 1px solid #ddd;
		padding: 5px;
	}

	.type_home::-webkit-scrollbar {
		display: none;
	}

	.iconhover {
		color: #606266;
		font-size: 20px;
	}

	.iconhover:hover {
		color: #409EFF;
	}

	.tempimg {
		position: fixed;
		z-index: 10;
		-webkit-user-select: none;
		cursor: default;
		pointer-events: none;
	}

	.tempimg>img {
		opacity: .5;
	}

	.versionscont {
		overflow: scroll;
		height: 25vh;
	}

	.versionscont::-webkit-scrollbar {
		display: none;

	}

	.decision_shade_close {
		display: flex;
		align-items: center;
		color: #999999;
	}

	.decision_shade_close>div {
		margin-right: 5px;
		background-color: #999999;
		height: 15px;
		width: 15px !important;
		z-index: 29;
		border-radius: 50%;
		transition: all .3s;
	}

	.decision_shade {
		color: #39b153;
		display: flex;
		align-items: center;
	}

	.decision_shade>div {
		-webkit-box-shadow: 0px 0px 12px 3px rgba(85, 255, 127, 0.69);
		-moz-box-shadow: 0px 0px 12px 3px rgba(85, 255, 127, 0.69);
		box-shadow: 0px 0px 12px 3px rgba(85, 255, 127, 0.69);
		margin-right: 5px;
		background-color: #55ff7f;
		height: 15px;
		width: 15px !important;
		z-index: 29;
		border-radius: 50%;
		transition: all .3s;
	}
</style>

<template>
	<div class="engine_decision" v-loading="loading">
		<!-- 顶部 -->
		<div class="decision_header">

			<!-- 打开引擎的下拉框 -->
			<div style="display: flex;align-items: center;width: 200px;">
				<i class="el-icon-cpu" :style="{fontSize: '20px',color:engid?'#409EFF':'#777',marginRight: '5px'}"></i>
				<el-select v-model="engid" placeholder="请打开引擎" size="medium" @change="engChange" filterable>
					<el-option v-for="item in eng" :key="item.id" :label="item.name" :value="item.id">
					</el-option>
				</el-select>
			</div>


			<!-- 设置按钮们 -->
			<div class="decision_header_button" v-if="engid">
				<div v-for="(item,index) in button" :key="index">
					<div v-if="item.text!='|'">
						<el-tooltip class="item" effect="dark" :content="item.text" placement="bottom">
							<!-- <img :src="path+item.url" width="20px" height="20px" draggable="false" @click="headerButtonClick(item)" /> -->
							<i :class="item.url+' iconhover'" style="" @click="headerButtonClick(item)"></i>
						</el-tooltip>
					</div>
					<div v-else style="color: #ccc;">
						|
					</div>
				</div>
			</div>
			<!-- 状态框 -->
			<div :class="DeployIngId==currversions?'decision_shade':'decision_shade_close'" v-if="engid">
				<div>

				</div>
				{{DeployIngId==currversions?'此版本运行中':'此版本未运行'}}
			</div>

		</div>
		<!-- 主体部分 -->
		<div class="decision">
			<!-- 主体左   节点展示部分 -->
			<div :class="leftOpen?'decision_left decision_left_open':'decision_left decision_left_close'" v-if="engid">
				<div class="engine">
					<el-button class="closebutton" @click="leftOpen=!leftOpen"><i
							:class="leftOpen?'el-icon-d-arrow-left':'el-icon-d-arrow-right'"></i></el-button>
				</div>
				<div class="node_home" ref="nodeHome">
					<div v-for="(item,index) in nodes" :key="index" class="nodes" @mousedown="nodesMousedown(item)"
						@mouseup="nodesMouseup()" v-if="item.node.type!=1">
						<img :src="path+item.node.url" alt="" draggable="false" /><span v-show="leftOpen"
							style="color: #555;font-size: 14px;">{{item.node.text}}</span>
					</div>
				</div>
				<div class="decision_button">
				</div>
			</div>

			<!-- 图 canvas 部分 -->
			<div class="decision_center" style="position: relative;flex:1">

				<div class="canvas_home">
					<div v-show="Node.length<1" :style="{height:getHeight,width:getWidth}">

						<p style="font-size: 100px;color: #ccc;text-align: center;line-height: 810px;">
							请选择引擎
						</p>
					</div>
					<canvas v-show="Node.length>0" id="canvas" height="800px" :width="getWidth"></canvas>
					<div v-if="DeployIngId == currversions||getbootStatus(currversions)==2"
						style="position: absolute;top: 0;left: 0;width: 100%;height: 100%;background-color: #ffffff88;">
						<p style="text-align: center;font-size: 30px;">
							<span v-if="DeployIngId == currversions" style="color: #c3e7cb;">
								此版本运行中
							</span>
							<span v-if="getbootStatus(currversions)==2" style="color: #a7acb3;">
								此版本审核中 请联系管理员审核
							</span>
						</p>
					</div>
				</div>
				<div :style="{position: 'absolute',top:'-30px',right:'0px',display:'flex',alignItems:'center'}"
					v-if="engid">
					<!-- <el-button type="primary" round size="mini" @click="rightHide=false">打开工具箱</el-button> -->
					<el-button type="text" @click="openHint=true">操作提示</el-button>
					<el-tooltip class="item" effect="dark" :content="rightHide?'打开工具箱':'隐藏工具箱'" placement="top">
						<i :class="rightHide?'el-icon-s-fold':'el-icon-s-unfold'" style="color:#409EFF;font-size: 26px;"
							@click="rightHide=!rightHide"></i>
					</el-tooltip>
				</div>
			</div>

			<!-- 右侧部分  节点与版本信息 -->
			<div class="decision_right">
				<!-- 标签页 -->
				<el-tabs type="border-card" tab-position="right" v-model="tabsModel"
					:style="{display:rightHide?'none':'block','minWidth':'300px'}">
					<!-- 右侧顶部 主要是删除 和 复制功能 -->
					<div class="decision_right_header">
						<div>
							<el-tooltip class="item" effect="dark" content="复制节点" placement="top"
								v-show="tabsModel=='node'&&tempNode">
								<i class="el-icon-document-copy" style="color: #409EFF;margin-right: 10px;"
									@click="copyNode"></i>
							</el-tooltip>
							<el-tooltip class="item" effect="dark" :content="('删除此'+(tabsModel=='node'?'节点':'版本'))"
								placement="top" v-show="tabsModel=='node'&&tempNode">
								<i class="el-icon-delete" style="color: #BB2739;margin-right: 10px;"
									@click="delectNode(tempNode)"></i>
							</el-tooltip>
						</div>
						<p style="font-size: 12px;color: #666666;">
							节点名称 按确定修改
						</p>
					</div>

					<!-- 节点部分主体 -->
					<el-tab-pane label="节点属性" name="node" style="height: 100%;overflow: scroll;" class="type_home">
						<div v-if="tempNode!==null&&tabsModel=='node'">

							<!--  节点名称编辑 -->

							<div class="NodeRename">
								<div style="align-items: auto;display: flex;">
									<img :src="path+tempNode.item.node.url" width="25px" height="25px"
										style="margin-right: 5px;" alt="">
										
										<!-- {{tempNode}} -->
									<el-input v-model="tempNode.tempText" placeholder="请输入内容,回车键确认" size="mini"
										maxlength="20" ref="ReName" @keyup.enter.native="reNameSure"></el-input>
								</div>
							</div>
							<!-- 节点具体信息 -->
							<div style="height: 700px;">
								<div v-if="tempNode.item.node.type==1">
								</div>
								<div v-if="tempNode.item.node.type==2">
									<type2 :data="tempNode" @callback="callback($event,tempNode)"></type2>
								</div>
								<div v-if="tempNode.item.node.type==3">
									<type3 :data="tempNode" @callback="callback($event,tempNode)"
										@setNextNodes="setNextNodes($event,tempNode)" :nodeNexts="tempNode.nodeNexts">
									</type3>
								</div>
								<div v-if="tempNode.item.node.type==4">
									<type4 :data="tempNode" @callback="callback($event,tempNode)"></type4>
								</div>
								<div v-if="tempNode.item.node.type==5" style="height: 100%;">
									<type5 :data="tempNode" @callback="callback($event,tempNode)"></type5>
								</div>
								<div v-if="tempNode.item.node.type==7" style="height: 100%;">
									<type7 :data="tempNode" @callback="callback($event,tempNode)"
										:nodeNexts="tempNode.nodeNexts" @setNextNodes="setNextNodes($event,tempNode)">
									</type7>
								</div>
								<div v-if="tempNode.item.node.type==9" style="height: 100%;">
									<type9 :data="tempNode" @callback="callback($event,tempNode)"
										@setNextNodes="setNextNodes($event,tempNode)"></type9>
								</div>
								<div v-if="tempNode.item.node.type==14">
									<type14 :data="tempNode" @callback="callback($event,tempNode)"></type14>
								</div>
								<div v-if="tempNode.item.node.type==15">
									<type15 :data="tempNode" @callback="callback($event,tempNode)"></type15>
								</div>
								<div v-if="tempNode.item.node.type==16">
									<type16 :data="tempNode" @callback="callback($event,tempNode)"></type16>
								</div>
								<div v-if="tempNode.item.node.type==17">
									<type17 :data="tempNode" @callback="callback($event,tempNode)"></type17>
								</div>
								<div v-if="tempNode.item.node.type==18">
									<type18 :data="tempNode" @callback="callback($event,tempNode)"></type18>
								</div>
								<div v-if="tempNode.item.node.type==19">
									<type19 :data="tempNode" @callback="callback($event,tempNode)"
										:nodeNexts="tempNode.nodeNexts" @setNextNodes="setNextNodes($event,tempNode)">
									</type19>
								</div>
								<div v-if="tempNode.item.node.type==20">
									<type20 :data="tempNode" @callback="callback($event,tempNode)"></type20>
								</div>
								<div v-if="tempNode.item.node.type==21">
									<type21 :data="tempNode" @callback="callback($event,tempNode)"
										:nodeNexts="tempNode.nodeNexts" @setNextNodes="setNextNodes($event,tempNode)">
									</type21>
								</div>
							</div>
						</div>
						<!-- 未选择节点展示 -->
						<div v-show="tempNode==null"
							style="color: #bbb;font-size: 16px;font-weight: bold;text-align: center;">
							请选择左侧节点
						</div>

					</el-tab-pane>
					<!-- 版本信息部分 -->
					<el-tab-pane label="版本信息" name="versions">
						<!-- 主版本信息 -->
						<div v-if="versions" class="versions">
							<div class="versionscont">
								<p class="versions_title">
									主版本
								</p>
								<div v-for="(item,index) in versions" style="display: flex;align-items: center;"
									:key="item.engineVersion.versionId">

									<el-button type="text" @click="VersionCurr(item.engineVersion.versionId)"
										:style="{color:currversions==item.engineVersion.versionId?'#409EFF':'#a8cde3'}">
										V：{{item.engineVersion.version}}</el-button>
									<p v-show="DeployIngId==item.engineVersion.versionId"
										style="color: #43962A; font-size: 14px;margin-left: 5px;"><i
											class="el-icon-success"></i>运行中</p>
									<p v-show="item.engineVersion.bootState===2"
										style="color: #989898; font-size: 14px;margin-left: 5px;"><i
											class="el-icon-success"></i>审核中</p>
								</div>
							</div>
							<!-- 小版本信息 -->
							<div v-if="tempSubversions">
								<p class="versions_title">
									小版本
								</p>
								<div class="versionscont">
									<div v-for="(item,index) in tempSubversions.subEngineVersionList"
										style="display: flex;align-items: center;" :key="item.versionId">
										<el-button type="text" @click="VersionCurr(item.versionId)"
											:style="{color:currversions==item.versionId?'#409EFF':'#a8cde3'}">
											临时版本：{{item.version+'.'+item.subVersion}}</el-button>
										<p v-show="DeployIngId==item.versionId"
											style="color: #43962A;font-size: 14px;margin-left: 5px;"><i
												class="el-icon-success"></i>运行中</p>
										<p v-show="item.bootState===2"
											style="color: #989898; font-size: 14px;margin-left: 5px;"><i
												class="el-icon-success"></i>审核中</p>
									</div>
								</div>
							</div>
						</div>
						<!-- 引擎未选择 -->
						<div v-else>
							请选择一个引擎
						</div>
					</el-tab-pane>
				</el-tabs>
			</div>
		</div>
		<!-- 选择出口的提示框 -->
		<el-dialog title="请选择出口" :visible.sync="dialogVisible" width="30%" v-if="tempLinkNodeA&&tempLinkNodeA.nodeNexts"
			:close-on-click-modal="false">
			<!-- <el-select v-model="tempSelectNext" placeholder="请选择">
				<el-option v-for="(item,index) in tempLinkNodeA.nodeNexts" :key="index"
					:label="'出口:'+item.sandbox+'|'+item.proportion" :value="item.sandbox"
					:disabled="item.nextNode?'disabled':false">
				</el-option>
			</el-select> -->

			<div style="display: flex;flex-wrap: wrap;">
				<el-button type="primary" round style="margin-left: 20px;margin-top: 20px;"
					v-for="(item,index) in tempLinkNodeA.nodeNexts" :disabled="item.nextNode?'disabled':false"
					@click="tempSelectNext=item.sandbox;dialogVisible = false">
					{{'出口:'+item.sandbox+'|'+item.proportion}}
				</el-button>

			</div>

			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogVisible = false;tempSelectNext='';tempLinkNodeA=null">取 消</el-button>
				<!-- <el-button type="primary" @click="dialogVisible = false">确 定</el-button> -->
			</span>
		</el-dialog>
		<!-- 添加节点时跟随鼠标移动的节点样式 -->
		<div ref='tempimg' class="tempimg">
			<img>
		</div>
		<!-- 测试执行 -->
		<batchTest v-if="upShow" :upShow="upShow" :engid="engid" :versionId="currversions" @upshowf="upShow=false"></batchTest>
		<!-- <batchTest :upShow="upShow" @upshowf="upShow=false" :versionId="DeployIngId"></batchTest>-->
		<flowHint :open.sync="openHint"></flowHint>



	</div>




</template>

<script>
	import bus2 from './bus.js' // 用于接收 跳转信息
	import bus from '../common/bus.js' // 用于 和 sidebar 通讯 导航栏菜单状态
	import batchTest from '@/components/models/batchTest.vue'
	import type4 from '../typecom/type4.vue'
	import type3 from '../typecom/type3.vue'
	import type5 from '../typecom/type5.vue'
	import type7 from '../typecom/type7.vue'
	import type9 from '../typecom/type9.vue'
	import type2 from '../typecom/type2.vue'
	import type15 from '../typecom/type15.vue'
	import type14 from '../typecom/type14.vue'
	import type16 from '../typecom/type16.vue'
	import type17 from '../typecom/type17.vue'
	import type18 from '../typecom/type18'
	import type19 from '../typecom/type19'
	import type20 from '../typecom/type20'
	import type21 from '../typecom/type21'
	import flowHint from '@/components/common/flowHint.vue'
	import {
		getEngineList,
		getversion,
		getNodeList,
		setmoveNode,
		setdelectNode,
		getVersionDeploy,
		getVersionUnDeploy,
		setAddNode,
		setupdateProperty,
		setremoveLink,
		setRenameNode,
		setAddNodeHB,
		setType4,
		getCopyVer,
		getAddVer,
		getClearNode,
		delectVer,
		getCopyNode,
		setdelectNodeList
	} from '../../api/index.js'
	import {
		nodes
	} from '@/utils/nodeList.js'
	export default {
		name: 'engineDecisionFlow',
		components: {
			flowHint,
			type17,
			batchTest,
			getCopyVer,
			setType4,
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
			getEngineList,
			getNodeList,
			getversion,
			setmoveNode,
			setdelectNode,
			getVersionUnDeploy,
			getVersionDeploy,
			setAddNode,
			setupdateProperty,
			setremoveLink,
			setRenameNode,
			setAddNodeHB,
			getAddVer,
			getClearNode,
			delectVer,
			getCopyNode,
			setdelectNodeList
		},
		data() {
			return {
				contentHeight: '', // 窗口高信息
				upShow: false, // 测试执行 是否展示
				scaling: 5, //  jtopo图 的 缩小倍率 用于放大缩小
				rightHide: true, //是否隐藏 右侧的信息 true 为显示  false 为不显示
				tempSelectNext: '', //临时出口选项
				dialogVisible: false, // 选择出口的弹窗是否显示  true为显示 false 为不显示
				maxOrder: 1, //当前最大叠加数  用于叠加nodeCode
				DeployIngId: null, //当前正在运行中的版本的ID
				tempNode: null, //当前选中的节点
				templink: null, //当前选中的连线
				currversions: '', //当前选中的版本
				tabsModel: 'versions', //右侧菜单栏打开节点面板 还是打开版本面板
				tempSubversions: null, //当前选中版本的小版本
				versions: null, //版本列表
				engid: null, //当前打开的引擎的ID
				loading: false, //整个页面的 loading
				eng: null, //  可选引擎的 list
				tempType: null, //临时节点类型  作用暂时未知
				Link: [], //连线列表  暂时未使用
				Node: [], //节点列表
				tempLinkNodeZ: null, //终止节点
				tempLinkNodeA: null, //起始节点
				canvas: null, //canvas
				stage: null, //舞台对象
				scene: null, //场景对象
				tempMouseDown: null, //鼠标拖动左侧节点时的 临时存储
				// sidbarClose: false,
				contentWidth: '', //舞台宽度
				openHint: false, //是否打开提示层

				// tool: [{
				// 	text: '生成png',
				// 	url: '/tool/png(2).png'
				// }],
				button: [{
						text: '部署/取消部署',
						url: 'el-icon-sort'
					},
					{
						text: '|'
					},
					{
						text: '批量测试',
						url: 'el-icon-set-up'
					}, {
						text: '|'
					},
					{
						text: '复制版本',
						url: 'el-icon-document-copy'
					},
					{
						text: '新建版本',
						url: 'el-icon-document'
					},
					{
						text: '|'
					},
					{
						text: '清空节点',
						url: 'el-icon-document-delete'
					}, {
						text: '批量删除',
						url: 'el-icon-delete'
					}, {
						text: '生成png',
						url: 'el-icon-picture-outline-round'
					}, {
						text: '|'
					},
					{
						text: '放大',
						url: 'el-icon-zoom-in'
					}, {
						text: '缩小',
						url: 'el-icon-zoom-out'
					},
					// {
					// 	text: '|'
					// }, {
					// 	text: '连线模式',
					// 	url: 'el-icon-link'
					// },
				],
				leftOpen: true, //左侧节点列表展开 或者关闭
				path: './img', //图片路径
				nodes: nodes
			}
		},
		created() {
			bus.$emit('collapseHeader', true)


			bus2.$on('EngineSwitchover', res => {
				this.engChange(res)
			})

			this.loading = true
			const h = this.$createElement;
			this.$notify({
				title: '提示',
				offset: 100,
				duration: 2000,
				message: h('i', {
					style: 'color: teal'
				}, '按住Ctrl 可以多选节点哦')
			});
			this.getEngineLists({
				pageNo: 1,
				pageSize: 999999
			}).then(res => {
				if (res.status == "1") {
					this.eng = res.data.enginelist
				}
				localStorage.getItem('engineId') ? this.engChange(Number(localStorage.getItem('engineId'))) : ''
				this.loading = false

			}).catch(err => {
				this.loading = false
			})
		},
		mounted() {

			window.onmouseup = () => {
				this.tempMouseDown = null
			}

			window.onresize = (e) => {
				this.contentWidth = document.querySelector('.content').offsetWidth
				// console.log(this.contentWidth)
			}
			this.initTopo()
			window.onkeydown = (e) => {
				if (e.key == "Control") {
					this.scene.mode = 'select'
				}
			}
			window.onkeyup = (e) => {
				if (e.key == "Control") {
					this.scene.mode = 'normal'
				}
			}


			// debugger

		},
		methods: {
			getbootStatus(id) { //获取版本状态是审核还是运行中
				let status = 0
				if (!this.versions) return
				this.versions.forEach(value => {
					if (value.engineVersion.versionId == id) {
						status = value.engineVersion.bootState
					} else {
						value.subEngineVersionList.forEach(item => {
							if (item.versionId == id) {
								status = item.bootState
							}
						})
					}

				})


				return status
			},
			hideRight() { //隐藏右侧
				this.rightHide = true
			},
			setNextNodes(e, temp) { //设置节点的  nextNodes 下级节点

				let value = this.Node.find(value => value.id == temp.id) //拿到节点
				let index = this.Node.findIndex(value => value.id == temp.id) //拿到节点的索引


				

				let arr = []
				value.nodeNexts.forEach((cont, index) => { //当前节点出口循环
					let is = true


					if (e[index].proportion !== cont.proportion) {
						arr.push(cont)
					}

			
				})
				arr.forEach(value => { //被删除的出口遍历
					if (value.nextNode) { //如果此接口上有连线 就删除此条连线

						this.scene.childs.forEach(item => {
							if (item.elementType == "link" && item.nodeZ.nodeCode == value
								.nextNode) {
								item.text = value.proportion
								this.unlink(item)
							}
						})
					}
				})

				let obj = value
				obj.nodeNexts = e;
				obj.tempText = temp.tempText
				console.log(JSON.stringify(e), JSON.stringify(value.nodeNexts))
				this.Node.splice(index, 1, obj)
				this.tempNode = obj
			},
			callback(e, temp) { //设置节点的nodeJson

				this.Node.forEach(value => {
					if (value.id == temp.id) {
						if (typeof e == 'string') {
							value.nodeJson = e
						} else {
							value.nodeJson = JSON.stringify(e)
						}
					}
				})
			},
			reNameSure() { //确定重命名
				if (String(this.tempNode.tempText).trim() === "") {
					this.$message.error('名字不能为空')
					this.tempNode.tempText = this.tempNode.text
					return
				}
				this.tempNode.text = this.tempNode.tempText
				this.loading = true
				this.Node.forEach(value => {
					if (value.id == this.tempNode.id) {
						value.text = this.tempNode.text
						this.setRenameNodes({
							"nodeId": this.tempNode.id,
							"nodeName": this.tempNode.text
						}).then(res => {
							this.loading = false
							this.$message.success('名称修改成功')
						}).catch(err => {
							this.loading = false
						})
					}
					

					//重命名掉接口预留位置
				})
			},
			delectNode(node, type = "") { //删除节点
				if (this.tabsModel == "node" || type == "batchDelect") {

					if (node.item.node.type == 1) {
						this.$message.warning('开始节点禁止删除')
						return
					} // 判断是否开始节点
					this.loading = true // 开始loading
					this.delectNodes({ //对接口
						currentNodeId: node.id,
						preNodeId: node.parentId.length ? node.parentId.join(',') : -1
						// preNodeId:-1 
					}).then(res => {

						if (res.status == '1') { //如果接口返回成功
							let arr = [] //用于获取 子节点

							// console.log(arr)

							this.Node.forEach(value => { //遍历所有节点   


								if (node.item.node.haveChildren) { // 如果被删除的节点本身是多子节点
									arr = node.nodeNexts.map(value => {
										return value.nextNode
									}) //arr 为删除节点的 子节点 们 （如果删除的是多子节点）

									arr.forEach(item => { //遍历删除节点的子节点
										if (value.nodeCode == item) { //清除 子节点的 父节点

											value.parentId.splice(value.parentId.indexOf(node.id),
												1) //删除
										}
									})
								} else {
									if (value.parentId.indexOf(node.id) !== -1) {
										// console.log(value,2)
										value.parentId.splice(value.parentId.indexOf(node.id), 1)
									}
								}





								if (node.parentId.indexOf(value.id) !== -1 && value.item.node
									.haveChildren) { //  如果父节点也是多子节点 清除 父节点 的 子节点 
									value.nodeNexts.forEach(cont => {
										if (cont.nextNode == node.nodeCode) {
											cont.nextNode = ''

										}
									})
								}
								if (value.nextNode == node.id) { //如果父节点不是多子节点
									value.nextNode = null
								}




							})


							this.Node.forEach((value, index) => { //清除此节点 不在上层循环清是因为需要先清干净关联数据
								// if (value.id == node.parentId && value.item.node.haveChildren) {
								// 	// console.log(value)
								// 	value.nodeNexts.forEach(item => {
								// 		if (item.nextNode == node.nodeCode) {
								// 			// console.log(3)
								// 			item.nextNode = ''
								// 		}
								// 	})
								// }
								if (value.id === node.id) {
									// this.Node.forEach(item => {

									// 	if (item.id == value.parentId) {
									// 		item.nextNode = null
									// 	}
									// })
									this.scene.remove(value)
									this.Node.splice(index, 1)
								}
							})
							this.tempNode = null
						}
						this.loading = false
					}).catch(err => this.loading = false)

				} else {
					// console.log(1)
					this.loading = true
					this.delectVers(this.currversions).then(res => {
						if (res.status == "1") {
							this.$message({
								message: '删除成功',
								type: 'success'
							});
							this.engChange(this.engid)

						}
						this.loading = false
					})
				}
			},
			setNodeProperty(nodes) { //设置node 的属性
				nodes.showSelected = true; // 不显示选中矩形

				nodes.fontColor = "48,48,48";
				nodes.borderRadius = 22.5;
				nodes.is = false;
				nodes.font = "12px";
				nodes.drag = false;

				if (!nodes.item.node.haveChildren) {
					nodes.nextNode = ''
				}

				if (nodes.item.node.url2) {
					nodes.textOffsetY = 4
					nodes.fontColor = '255,255,255'
					nodes.textPosition = 'Middle_Center'
					nodes.setSize(80, 40);
					nodes.setImage(this.path + nodes.item.node.url2)
				} else {
					nodes.setImage(this.path + nodes.item.node.url)
				}

				nodes.Vid = this.currversions
				nodes.mouseup((e) => {
					this.nodesclickup(e, nodes)
				})
				nodes.mousedrag((e) => {
					nodes.drag = true
				})
				nodes.dbclick((e) => {
					this.rightHide = false
					this.openNode(nodes)
				})


			},
			addNode(e) { //添加节点
				this.maxOrder += 1
				var nodes = new JTopo.Node(this.tempMouseDown.node.text);
				nodes.setLocation(e.x - 11, e.y - 11);
				nodes.type = this.tempMouseDown.node.type;
				nodes.url = this.tempMouseDown.node.url;
				nodes.parentId = [];
				nodes.item = this.tempMouseDown
				nodes.nodeJson = null
				nodes.nodeCode = 'ND_' + this.maxOrder
				nodes.nodeOrder = this.maxOrder
				if (nodes.item.node.haveChildren) {
					nodes.nodeNexts = []
				}
				this.setNodeProperty(nodes)

				let obj = {

					nodeType: nodes.item.node.type,
					versionId: this.currversions,
					nodeName: nodes.text,
					nodeCode: nodes.nodeCode,
					nodeOrder: nodes.nodeOrder,
					nodeX: e.x - 11,
					nodeY: e.y - 11
				}
				if (nodes.item.node.type != 5) {
					obj.id = 0
				} else {
					obj.nodeId = 0
				}

				this.loading = true
				this.setAddNodes(obj).then(res => {
					if (res.status == '1') {
						nodes.id = res.data.nodeId

						this.Node.push(nodes)
						this.scene.add(nodes);
						this.loading = false
					}

				})
			},
			NodeStart(item) { //节点初始化
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
					nodes.item = nodeitem;
					nodes.nodeJson = value.nodeJson
					nodes.nodeCode = value.nodeCode
					nodes.nodeOrder = value.nodeOrder


					nodes.parentId = value.parentId ? value.parentId.split(',').map(value => +value) :
				[]; //parentId 更改为数组
					nodes.id = value.nodeId

					if (nodes.item.node.haveChildren) {
						if (nodes.item.node.type == 7) {
							nodes.nodeNexts = value.nodeJson ? JSON.parse(value.nodeJson) : []
						} else if (nodes.item.node.type == 21) {
							if (value.nodeJson) {

								nodes.nodeNexts = JSON.parse(value.nodeJson).map((value, index) => {
									return {
										proportion: value.branchName,
										sandbox: index + 1,
										nextNode: value.nextNode,
										champion: value.champion
									}
								})

							} else {
								nodes.nodeNexts = []
							}
						} else if (nodes.item.node.type == 19) {
							if (value.nodeJson) {

								nodes.nodeNexts = JSON.parse(value.nodeJson).map((value, index) => {
									return {
										proportion: value.branchName,
										sandbox: index + 1,
										nextNode: value.nextNode
									}
								})

							} else {
								nodes.nodeNexts = []
							}
						} else {
							if (value.nodeJson) {
								nodes.nodeNexts = JSON.parse(value.nodeJson).conditions.map((value, index) => {
									return {
										proportion: value.group_name,
										sandbox: index + 1,
										nextNode: value.nextNode
									}
								})
							} else {
								nodes.nodeNexts = []
							}
						}

					}
					this.setNodeProperty(nodes)


					this.Node.push(nodes)

					this.scene.add(nodes);
				})
				this.Node.forEach(value => {
					if (value.parentId.length) {

						this.Node.forEach(item => {
							value.parentId.forEach(cont => {
								if (item.id == cont) {

									let link = new JTopo.Link(item, value)
									if (item.item.node.haveChildren) {
										item.nodeNexts.forEach(cont => {
											if (cont.nextNode == value.nodeCode) {
												link.text = cont.proportion
												link.fontColor = '#000'
											}
										})
									} else {
										item.nextNode = value.id
									}
									link.arrowsRadius = 10
									link.lineWidth = 1
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
			openNode(nodes) { //打开节点的内容
				this.tabsModel = 'node';
				this.tempNode = {
					...nodes
				};
				this.$set(this.tempNode, 'tempText', this.tempNode.text)
				// this.$nextTick(() => {
				// 	if (this.$refs.ReName.$refs.input.onkeydown == null) {
				// 		this.$refs.ReName.$refs.input.onkeydown = (e) => {
				// 			if (e.key == "Enter") {
				// 				this.reNameSure()
				// 			} else if (e.key == "Escape") {
				// 				this.tempNode.tempText = this.tempNode.text
				// 			}
				// 		}
				// 	}
				// })
			},
			nodesclickup(e, nodes) { //节点上鼠标弹起
				if (e.button == 2 || (e.button === 0 && this.tempLinkNodeA != null)) {

					this.link(e, nodes)
				} else if (e.button == 0) {
					// console.log(nodes)
					console.log(nodes.id, nodes.parentId, nodes.nodeNexts, nodes.nextNode, nodes.nodeJson)
				}

				let arr = []
				let is = false
				this.Node.forEach(value => {
					if (value.id === nodes.id) {
						is = true
					}
					if (value.selected) {
						arr.push(value)
					}
				})
				if (nodes.drag && is) {

					arr.forEach(value => {
						this.setmoveNodes({
							nodeX: value.x,
							nodeY: value.y,
							nodeId: value.id,
						})
					})


				}

			},
			copyNode() { //复制节点
				this.loading = true
				if (this.tempNode.item.node.type == 1) {
					this.$message.error('开始节点禁止复制')
					this.loading = false
					return
				}

				this.getCopyNodes({
					"nodeId": this.tempNode.id
				}).then(res => {
					if (res.status == '1') {
						this.$message({
							message: '复制成功',
							type: 'success'
						});
						this.getNodeLists(this.currversions).then(res => {
							if (res.status == "1") {
								this.maxOrder = res.data.maxOrder
								this.NodeStart(res.data.engineNodeList)
							}
							this.loading = false
							this.tempNode = null
						}).catch(err => {
							this.loading = false
						})
					}
				}).catch(err => {
					this.loading = false
				})

			},

			async engChange(id, currId) { // 引擎选中
				this.loading = true
				this.DeployIngId = null
				let currVersion = null
				const res = await this.getversions(id)
				if (res.status != "1") {
					this.loading = false
					return
				}
				this.rightHide = false
				this.versions = res.data

				let temp = this.tempSubversions ? this.versions.find(x => x.engineVersion.versionId == this
					.tempSubversions.engineVersion.versionId) : []
				this.tempSubversions = temp


				let obj

				this.engid = id
				localStorage.setItem('engineId', id)
				// 如果有  currId （选中id）则直接选中

				if (currId) {
					this.VersionCurr(currId)
					this.loading = false
					return
				}


				res.data.forEach(value => {
					if (value.engineVersion != null) {
						if (value.engineVersion.bootState === 1) {
							this.DeployIngId = value.engineVersion.versionId
						}
						value.subEngineVersionList.forEach(item => {
							if (item.bootState === 1) {
								this.DeployIngId = item.versionId
							}
						})

					}
				})
				this.tabsModel = 'versions'

				if (this.DeployIngId) {
					this.VersionCurr(this.DeployIngId)
				} else {
					this.VersionCurr(res.data[res.data.length - 1].engineVersion.versionId)
				}
				this.loading = false


			},
			VersionCurr(id) {
				this.contentWidth = document.querySelector('.content').offsetWidth
				this.tempLinkNodeA = null
				let item = this.versions.find(x => x.engineVersion.versionId == id)
				item && (this.tempSubversions = item)
				this.loading = true
				this.currversions = id
				this.getNodeLists(id).then(res => {
					if (res.status == "1") {

						this.maxOrder = res.data.maxOrder
						this.NodeStart(res.data.engineNodeList)
					}
					this.loading = false
					this.tempNode = null
				}).catch(err => {
					this.loading = false
				})
			},

			async headerButtonClick(item) { // 点击按钮
				if (item.text == '部署/取消部署') {
					if (this.currversions) {
						if (this.DeployIngId !== this.currversions) {
							if (this.isGetTogether()) {
								this.getVersionDeploys(this.currversions).then(res => {
									if (res.status === "1") {
										if (res.data.status == '1') {
											this.DeployIngId = this.currversions
											this.$message({
												message: '部署成功',
												type: 'success'
											});
											this.engChange(this.engid)
										} else if (res.data.status == '2') {
											// this.DeployIngId = this.currversions
											this.$message({
												message: '部署以提交 请等待管理员审批',
												type: 'success'
											});
											this.engChange(this.engid, this.currversions)
										}
									}
								}).catch(err => {
									this.$message.error(err.msg)
								})
							} else {
								this.$message.error('请检查是否缺少并行节点或聚合节点！')
							}

						} else {
							this.getVersionUnDeploys(this.currversions).then(res => {
								if (res.status === "1") {
									this.DeployIngId = null
									this.$message({
										message: '取消部署成功',
										type: 'success'
									});
									this.engChange(this.engid, this.currversions)
								}
							})
						}
					} else {
						this.$message.error('先选择一个版本才能部署')
					}
				} else if (item.text == "复制版本") {

					const res = await this.getCopyVers(this.currversions)

					this.engChange(this.engid, this.currversions)

				} else if (item.text == "新建版本") {
					let tempid
					let is = false
					this.versions.forEach(value => {
						if (value.engineVersion.versionId == this.currversions) {
							tempid = value.engineVersion.version
							return
						}
						value.subEngineVersionList.forEach(item => {
							if (item.versionId == this.currversions) {
								tempid = value.engineVersion.version
								is = true
								return
							}
						})
						if (is) {
							return
						}
					})


					const res = await this.getAddVers(this.engid, tempid)

					this.engChange(this.engid, this.currversions)

				} else if (item.text == "清空节点") {
					if (this.DeployIngId == this.currversions) {
						this.$message.error('运行中的引擎 不能清空节点')
					} else {
						this.$confirm('此操作将永久清空节点?', '提示', {
							confirmButtonText: '确定',
							cancelButtonText: '取消',
							type: 'warning'
						}).then(() => {
							this.getClearNodes(this.currversions).then(res => {
								this.engChange(this.engid)
							})
						})

					}

				} else if (item.text == "生成png") {
					this.stage.saveImageInfo()

				} else if (item.text == "放大") {
					if (this.scaling < 10) {
						this.scaling += 1
						this.stage.zoomOut()
					}

				} else if (item.text == "缩小") {
					// console.log(this.scaling)
					if (this.scaling > 0) {
						this.scaling -= 1
						this.stage.zoomIn()
					}

				} else if (item.text == "批量测试") {
					this.tests()
				} else if (item.text == "批量删除") {
					this.batchDelect()
				}
			},

			batchDelect() { //批量删除
				let arr = this.Node.filter(value => {
					if (value.item.node.type == 1) return false
					return value.selected
				})
				if (arr.length === 0) {
					this.$message.error('请至少选择一个非开始节点,(按住Ctrl拖拽)')
					return
				}
				let arr2 = arr.map(value => {
					return {
						currentNodeId: value.id,
						preNodeId: value.parentId.length ? value.parentId.join(',') : -1
					}
				})
				setdelectNodeList(arr2).then(res => {
					if (res.status == "1") {
						this.$message({
							message: '删除成功',
							type: 'success'
						});
						arr.forEach(value => {
							this.delectNode(value, 'batchDelect')
						})
					}
				})
			},
			tests() { //打卡批量测试
				this.upShow = true
			},
			async getversions(param) {
				const data = await getversion(param);
				return data
			},
			async setRenameNodes(param) {
				const data = await setRenameNode(param);
				return data
			},
			async setmoveNodes(param) {
				const data = await setmoveNode(param);
				return data
			},
			async getNodeLists(param) {
				const data = await getNodeList(param);
				return data
			},
			async getEngineLists(param) {
				const data = await getEngineList(param);
				return data
			},
			async delectNodes(param) {
				const data = await setdelectNode(param);
				return data
			},
			async getVersionDeploys(param) {
				const data = await getVersionDeploy(param);
				return data
			},
			async getVersionUnDeploys(param) {
				const data = await getVersionUnDeploy(param);
				return data
			},
			async setAddNodes(param) {
				const data = await setAddNode(param);
				return data
			},
			async setType4s(param) {
				const data = await setType4(param);
				return data
			},
			async setupdatePropertys(param) {
				const data = await setupdateProperty(param);
				return data
			},
			async setremoveLinks(param) {
				const data = await setremoveLink(param);
				return data
			},
			async setAddNodeHBs(param) {
				const data = await setAddNodeHB(param);
				return data
			},
			async getCopyVers(param) {
				const data = await getCopyVer(param);
				return data
			},
			async getAddVers(id, Vid) {
				const data = await getAddVer(id, Vid);
				return data
			},
			async getClearNodes(id, Vid) {
				const data = await getClearNode(id, Vid);
				return data
			},
			async delectVers(id, Vid) {
				const data = await delectVer(id, Vid);
				return data
			},
			async getCopyNodes(param) {
				const data = await getCopyNode(param);
				return data
			},
			link(e, nodes) { // 连线


				if (e.button === 2 || (e.button === 0 && this.tempLinkNodeA != null)) { //首页需要是鼠标右键
					console.log(this.tempLinkNodeA)
					if (this.tempLinkNodeA == null) { //如果没有点击初始节点的话
						if (!nodes.nextNode || nodes.item.node.haveChildren) {
							if (nodes.item.node.haveChildren) {

								if (this.tempNode && this.tempNode.id) {
									if (this.tempNode.id === nodes.id) {
										this.$message({
											message: "多出口节点不能在'编辑'时'连线'",
											type: 'warning'
										});
										return
									}
								}
								let is = true
								if (nodes.nodeNexts) {
									nodes.nodeNexts.forEach(value => {
										if (!value.nextNode) {
											this.tempLinkNodeA = nodes
											this.dialogVisible = true
											is = false
											return
										}


									})
								} else {
									let is = false
								}
								if (is) {
									this.$message({
										message: '此节点的子节点已达上限或未定义子节点出口',
										type: 'warning'
									});
								}
							} else {
								this.tempLinkNodeA = nodes

							}
						} else {
							this.$message({
								message: '此节点只能有一个子节点',
								type: 'warning'
							});
						}
					} else { //如果有初始节点
						if (this.tempLinkNodeA.id != nodes.id) { //判断是否连续点击了两次 同一个按钮


							if (nodes.item.node.type === 1) { //如果第二次点击的是开始节点
								this.$message({
									message: '起点节点不能为子节点',
									type: 'warning'
								});
								this.tempLinkNodeA = null
								this.tempSelectNext = ''
								return
							}


							let isDAG = this.checkIsDAG(this.Node, {
								source: this.tempLinkNodeA.id,
								target: nodes.id
							});

							if (!isDAG) {

								this.$message({
									message: '不允许封闭连线',
									type: 'warning'
								});
								this.tempLinkNodeA = null
								this.tempSelectNext = ''
								return
							}
							// 验证并行节点内是否存在非策略节点（并行节点和聚合节点之间只能放策略节点）
							let isStrategy = this.checkIsStrategy(this.Node, this.tempLinkNodeA, nodes);
							if (!isStrategy) { //并行分支有没有非策略节点
								this.$message({
									message: '并行分支只能放策略节点',
									type: 'warning'
								});
								this.tempLinkNodeA = null
								this.tempSelectNext = ''
								return
							}
							var link = new JTopo.Link(this.tempLinkNodeA, nodes);
							if (this.tempSelectNext) {
								this.tempLinkNodeA.nodeNexts.forEach(value => {
									if (value.sandbox == this.tempSelectNext) {
										link.text = value.proportion
										link.fontColor = '#000'
										this.Node.forEach(item => {
											if (item.id == this.tempLinkNodeA.id) {
												item.nodeNexts.forEach(cont => {
													// console.log(value, cont)
													if (cont.sandbox == this
														.tempSelectNext) {
														// value.nextNode= nodes.nodeCode
														cont.nextNode = nodes.nodeCode
														console.log(cont.nextNode)
														return
													}
												})
												return
											}
										})

										return
									}
								})

							}

							if (!this.tempLinkNodeA.item.node.haveChildren) {

								this.tempLinkNodeA.nextNode = nodes.id
							}
							// nodes.parentId = this.tempLinkNodeA.id								

							let is = false
							if (nodes.parentId.length) { // 判断是否有重复连线
								nodes.parentId.forEach(value => {
									if (value === this.tempLinkNodeA.id) {
										is = true
									}
								})
							}
							if (is) {
								this.$message.error('请勿重复连线')
								return
							}


							nodes.parentId.push(this.tempLinkNodeA.id)

							// console.log('nodes.parentId',nodes.parentId)
							link.arrowsRadius = 10
							link.lineWidth = 1
							let obj = {}
							if (this.tempSelectNext) { //如果有多选出口项的
								obj = {
									"nodeId_1": String(this.tempLinkNodeA.id),
									"nodeId_2": String(nodes.id),
									node_json_1: JSON.stringify(this.tempLinkNodeA.nodeNexts)
								}
								if (this.tempLinkNodeA.item.node.type == 21) {
									obj.node_json_1 = JSON.stringify(this.tempLinkNodeA.nodeNexts.map(
										value => {
											return {
												"branchName": value.proportion,
												"nextNode": value.nextNode,
												"champion": value.champion
											}
										}))
								}
								if (this.tempLinkNodeA.item.node.type == 19) {
									obj.node_json_1 = JSON.stringify(this.tempLinkNodeA.nodeNexts.map(
										value => {
											return {
												"branchName": value.proportion,
												"nextNode": value.nextNode
											}
										}))
								}
							} else { //如果没有多个出口
								obj = {
									"nodeId_1": String(this.tempLinkNodeA.id),
									"nodeId_2": String(nodes.id),
								}
							}
							if (this.tempLinkNodeA.item.node.type != 3) { //如果不是分组节点
								this.setupdatePropertys(obj) //不是分组直接请求
							} else { //是分组要进行下一步
								let subarr = this.tempLinkNodeA.nodeNexts.map(value => {
									return value.nextNode
								})
								subarr = subarr.join(',')
								if (subarr[subarr.length - 1] == ',') {
									subarr = subarr.split("")
									subarr.pop()
									subarr = subarr.join("")
								}

								let tempnodeJson = JSON.parse(this.tempLinkNodeA.nodeJson)
								// console.log(tempnodeJson)
								tempnodeJson.conditions.forEach(value => {
									this.tempLinkNodeA.nodeNexts.forEach(item => {
										if (value.group_name == item.proportion) {
											value.nextNode = item.nextNode
										}
									})
								})

								this.setType4s({
									"id": this.tempLinkNodeA.id,
									"targetId": String(nodes.id),
									"initEngineVersionId": String(this.tempLinkNodeA.Vid),
									"nodeType": 3,
									"nodeName": this.tempLinkNodeA.text,
									"nodeCode": this.tempLinkNodeA.nodeCode,
									"nodeOrder": this.tempLinkNodeA.nodeOrder,
									"nextNodes": subarr,
									"nodeX": this.tempLinkNodeA.x,
									"nodeY": this.tempLinkNodeA.y,
									"nodeJson": JSON.stringify(tempnodeJson)
								})
							}

							this.scene.add(link);
							this.tempLinkNodeA = null;
							this.tempSelectNext = ''




						}
					}

				} else if (e.button === 0 && nodes.item.node.dataId != 9999) {
					this.tempType = nodes.item.node.type
					return
				}
			},

			nodesMousedown(item) { //在节点上按下鼠标
				this.tempMouseDown = item
			},
			nodesMouseup() { //在节点上放开鼠标
				this.tempMouseDown = null
			},

			initTopo() { //jtopo初始化

				this.canvas = document.getElementById('canvas');
				this.stage = new JTopo.Stage(this.canvas);
				this.stage.wheelZoom = null;

				this.scene = new JTopo.Scene(this.stage);
				this.scene.background = "./img/decisionBcg.jpg"

				var that = this
				this.scene.mousemove(function(e) {
					if (that.tempLinkNodeA && that.tempLinkNodeZ) {
						that.tempLinkNodeZ.setLocation(e.x, e.y);
					}
				});

				this.scene.mouseup(function(e) {
					// console.log(e.target)
					if (that.tempMouseDown !== null) {
						// console.log(e)
						that.addNode(e)
						that.tempMouseDown = null
						return
					}
					if ((e.button == 0) || (e.button == 2 && e.target == null) || (!e.target instanceof JTopo
							.Node)) {

						if (that.tempLinkNodeA != null) {
							console.log(1)
							that.tempLinkNodeA = null
						}
					}


				});
				this.scene.dbclick((e) => {
					if (e.button == 0 && e.target instanceof JTopo.Link) {
						let event = e.target



						this.$confirm('确定断开此处的连线？', '提示', {
							confirmButtonText: '确定',
							cancelButtonText: '取消',
							type: 'warning'
						}).then(() => {
							this.unlink(event)

						}).catch(() => {
							this.$message({
								type: 'info',
								message: '已取消断开'
							});
						});



					}
				})

			},
			unlink(event) { //删除连线
				this.setremoveLinks({
					currentNodeId: event.nodeZ.id,
					preNodeId: event.nodeA.id,
				}).then(res => {
					if (res.status == "1") {
						this.$message({
							type: 'success',
							message: '连线以断开!'
						});
						// event.nodeZ.parentId = null
						let tempNum = -1
						event.nodeZ.parentId.forEach((value, index) => {
							if (value == event.nodeA.id) {
								tempNum = index
							}
						})
						if (tempNum == -1) {
							this.$message.error('子节点父属性无此id')
							return
						}
						event.nodeZ.parentId.splice(tempNum, 1)


						// console.log(event.nodeZ, event.nodeA)
						if (event.nodeA.item.node.haveChildren) {
							event.nodeA.nodeNexts.forEach(value => {
								if (value.nextNode == event.nodeZ.nodeCode && event.text == value
									.proportion) {
									value.nextNode = ''
								}
							})
						} else {
							event.nodeA.nextNode = null
						}
						this.scene.remove(event)
					}
				})
			},
			checkIsDAG(nodeArr, currentedge) { //判断是否有环
				//邻接矩阵
				const graph = {};
				//结点个数和边的个数
				//标记矩阵,0为当前结点未访问,1为访问过,-1表示当前结点后边的结点都被访问过。
				const visited = {};
				//是否是DAG(有向无环图)
				let isDAG = true;
				//创建图,以邻接矩阵表示
				function DFS(i) {
					// console.log('正在访问结点' + nodes[i]);
					//结点i变为访问过的状态
					visited[nodes[i]] = 1;
					for (let j = 0; j < nodes.length; j++) {
						//如果当前结点有指向的结点
						if (graph[nodes[i]][nodes[j]] != 0) {
							//并且已经被访问过
							if (visited[nodes[j]] == 1) {
								isDAG = false; //有环
								break;
							} else if (visited[nodes[j]] == -1) {
								//当前结点后边的结点都被访问过，直接跳至下一个结点
								continue;
							} else {
								DFS(j); //否则递归访问
							}
						}
					}
					//遍历过所有相连的结点后，把本节点标记为-1
					visited[nodes[i]] = -1;
				}

				function create(nodes, edges) {
					for (let i = 0; i < nodes.length; i++) {
						const pre = nodes[i];
						graph[pre] = {};
						for (let j = 0; j < nodes.length; j++) {
							const next = nodes[j];
							graph[pre][next] = 0;
						}
					}
					for (let k = 0; k < edges.length; k++) {
						const edge = edges[k];
						graph[edge.source][edge.target] = 1;
					}
					//初始化color数组为0，表示一开始所有顶点都未被访问过
					for (let i = 0; i < nodes.length; i++) {
						visited[nodes[i]] = 0;

					}
				}
				let edges = [];
				let nodes = [];
				nodeArr.map(x => {
					if (x.nodeNexts) {
						x.nodeNexts.map(y => {
							if (y.nextNode) {
								nodeArr.map(n => {
									if (n.nodeCode == y.nextNode) {
										edges.push({
											source: x.id,
											target: n.id
										})
									}
								})
							}
						})
					} else {
						if (x.nextNode) {
							edges.push({
								source: x.id,
								target: x.nextNode
							})
						}
					}
				})
				edges.push(currentedge);
				edges.forEach(e => {
					const {
						source,
						target
					} = e;
					if (!nodes.includes(source)) {
						nodes.push(source);
					}
					if (!nodes.includes(target)) {
						nodes.push(target);
					}
				});
				create(nodes, edges);
				for (let i = 0; i < nodes.length; i++) {
					//该结点后边的结点都被访问过了，跳过它
					if (visited[nodes[i]] == -1) {
						continue;
					}
					DFS(i);
					if (!isDAG) {
						return false; //有环
					}
				}
				if (isDAG) {
					return true //无环
				}
			},
			checkIsStrategy(nodeArr, currentNode, targetNode) { //验证策略节点
				let result = true;
				let isPolymerizes = false;
				if (targetNode.item.node.haveChildren) {
					if (currentNode.item.node.type != 20) {
						let parentIdArr = currentNode.parentId;
						check(parentIdArr)
						if (currentNode.item.node.type == 19) {
							result = false
						}
					}

				}
				return result

				function check(arr) {
					arr.forEach((value) => {
						nodeArr.forEach(x => {
							if (x.id == value) {
								if (x.item.node.type == 20) {
									result = true;
									isPolymerizes = true;
								}
								if (x.item.node.type == 19 && !isPolymerizes) {
									result = false;
									return
								} else {
									if (x.parentId.length && result) {
										check(x.parentId)
									}
								}
							}
						})
					})
				}
			},
			isGetTogether() { //判断是否确实并行或者聚合
				let parallelArr = []; //是否存在并行节点
				let polymerizesArr = [];
				this.Node.forEach(x => {
					if (x.item.node.type == 19) {
						parallelArr.push(x)
					}
				});
				if (parallelArr.length > 0) {
					this.Node.forEach(x => {
						if (x.item.node.type == 20) {
							polymerizesArr.push(x);
						}
					})
				}
				return parallelArr.length == polymerizesArr.length
			}

		},
		computed: {
			getWidth() {
				if (this.$store.state.barShrink) {

					return (this.contentWidth - 50) + 'px'
				} else {
					return (this.contentWidth - 230) + 'px'
				}
			},
			getHeight() {
				// console.log(this.contentHeight)
				if (this.contentHeight) {
					return (this.contentHeight * 0.95) + 'px'
				} else {
					return '810px'
				}
			}
		},
		watch: {
			rightHide(res) {
				if (res) {
					this.tempNode = null
				}
			},
			tabsModel(res) {
				if (res == 'versions') {
					this.tempNode = null
				}

			},
			tempLinkNodeA() {
				if (this.tempLinkNodeA !== null) {
					this.tempLinkNodeZ = new JTopo.Node('tempZ');
					this.tempLinkNodeZ.setSize(1, 1);
					this.templink = new JTopo.Link(this.tempLinkNodeA, this.tempLinkNodeZ);
					this.templink.arrowsRadius = 10
					this.templink.lineWidth = 1
					this.tempLinkNodeZ.setLocation(this.tempLinkNodeA.x, this.tempLinkNodeA.y);
					this.tempLinkNodeZ.visible = false
					this.scene.add(this.tempLinkNodeZ)
					this.scene.add(this.templink)
				} else {
					this.tempLinkNodeZ = null;
					this.scene.remove(this.templink)
					this.templink = null
				}
			},
			tempMouseDown() {
				if (this.tempMouseDown) {
					// console.log(this.tempMouseDown,this.$refs.tempimg.children[0])
					if (this.tempMouseDown.node.url2) {
						this.$refs.tempimg.children[0].src = this.path + this.tempMouseDown.node.url2
						this.$refs.tempimg.children[0].style.width = 80 + 'px'
						this.$refs.tempimg.children[0].style.height = 40 + 'px'
					} else {
						this.$refs.tempimg.children[0].src = this.path + this.tempMouseDown.node.url
						this.$refs.tempimg.children[0].style.width = 40 + 'px'
						this.$refs.tempimg.children[0].style.height = 40 + 'px'
					}
					this.$refs.tempimg.style.display = 'block'
					window.onmousemove = (e) => {
						this.$refs.tempimg.style.top = (e.y - 20) + 'px'
						this.$refs.tempimg.style.left = (e.x - 20) + 'px'
					}
				} else {
					this.$refs.tempimg.style.display = 'none'
					window.onmousemove = () => {}
				}
			}
		}
	};
</script>

<style>

	.table{
	/* border-bottom:  1px solid #ddd; */
	border-left:  1px solid #ddd;
	min-height: 50px;
	display: flex;
	min-width: 300px;
}
.table div{
	min-height: 50px;
	width: 300px;
	box-sizing: border-box;
}
.table p{
	width: 300px;
	display: flex;
	
}
.SCO_scoreHome{
	width: 75vw;
	height: 55vh;
	overflow: scroll;
	transform-origin: 0 0;
	user-select:none;
}

.SCO_cont::-webkit-scrollbar{
	display: none;
}
.SCO_cont{
	height: 60vh;
	overflow: scroll;
}
.SCO_tableHeader{
	display: flex;
	margin-left: 20px;
	/* margin-top: 20px; */
	border-top: 1px solid #ddd;
}
.SCO_tableHeader>div{
	height: 40px;
	width: 300px;
	flex-shrink: 0;
	display: flex;
	justify-content: center;
	align-items: center;
	border-left: 1px solid #ddd;
	font-size: 14px;
}
.sco_cont_home{
	overflow: scroll;
	flex: 1;
}
.sco_version{
	margin-top: 20px;
	padding-left: 20px;
}
.sco_version_buttom{
	/* width: 100px; */
	margin-left: 20px !important;
}
</style>
<template>
	<div class="dataManageRedact" v-loading="loading">
		<div :class="smallHeader?'MR_header MR_headerSmall':'MR_header'">
			<div>
				<div>
					<el-button type="primary" icon="el-icon-arrow-left" circle @click="mixinClose"></el-button>
				</div>
				<div>
					<span v-if="id===0">新增评分卡 :</span>
					<span v-else>编辑评分卡 :</span>
				</div>
			</div>
			<div>
				<el-button :icon="smallHeader?'el-icon-bottom':'el-icon-top'" circle @click="openHeader">
				</el-button>
				<el-button type="success" icon="el-icon-check" circle @click="submit" :disabled="addVersionStatus"></el-button>
				<!-- <el-button type="danger" icon="el-icon-close" circle @click="$emit('close')"></el-button> -->
			</div>
		</div>
		<div :class="smallHeader?'MR_input MR_inputSmall':'MR_input'">
			<div>
				<p>评分卡代码: </p>
				<el-input placeholder="请输入评分卡代码" maxlength="30" v-model="code" :disabled="addVersionStatus" clearable></el-input>
			</div>
			<div>
				<p> 评分卡名称: </p>
				<el-input placeholder="请输入评分卡名称" maxlength="20" v-model="name" :disabled="addVersionStatus" clearable></el-input>
			</div>
		</div>
		<div :class="smallHeader?'MR_scope MR_scopeSmall':'MR_scope'">
			<div>
				<p>评分卡描述: </p>
				<el-input placeholder="请输入评分卡描述" maxlength="300" v-model="description" :disabled="addVersionStatus" clearable></el-input>
			</div>
		</div>


		<version style="margin:5px;" :id="id" :ruleVersionList="versionList" :version="version"
		 :addVersionStatus="addVersionStatus" :addVersionDialog="addVersionDialog" :addVersionLoading="addVersionLoading"
		 @addVersion="addVersion" @addVersionClose="addVersionClose" @copyVersion="copyVersion" @delectVersion="delectVersion"
		 @updateVersion="updateVersion" @addVersionSure="addVersionSure" @Dialog="addVersionDialog=$event" @addVersionExamine="addVersionExamine"
		 @versionChange="versionChange" @StatusChange="addVersionStatus=$event"></version>


		<div class="sco_cont_home">
			<div class="SCO_tableHeader">
				<div style="background-color: #409EFF;color: #fff;">维度</div>
				<div style="width: 100px;background-color: #409EFF;color: #fff;">权重</div>
				<div v-for=" value in tier" style="background-color: #409EFF;color: #fff;">
					指标{{value}}
				</div>
				<div style="border-top: 1px solid #ddd;border-right: 1px solid #ddd;margin-top: -1px;margin-left: 1px;background-color: #409EFF;color: #fff;width: 200px;">
					评分
				</div>
			</div>
			<div style="display: flex;margin-left: 20px;">
				<div style="border-right: 1px solid #ddd;border-left: 1px solid #ddd;border-top: 1px solid #ddd;">
					<div v-for="(value,index) in table" style="min-height: 50px;display: flex;border-bottom: 1px solid #ddd;position: relative;">
						<div style="min-width: 300px;display: flex;align-items: center;justify-content: center;position: relative;">
							<div style="position: absolute;left: 0;top: 0;color: #409EFF;">
								<el-dropdown @command="handleCommand">
									<span class="el-dropdown-link">
										<i class="el-icon-s-operation"></i>
									</span>
									<el-dropdown-menu slot="dropdown" trigger="click">
										<el-dropdown-item icon="el-icon-bottom" :command="'addBrother|'+index">向下添加</el-dropdown-item>
										<el-dropdown-item icon="el-icon-right" :command="'addSon|'+index" v-show="!value.children.length">向右添加</el-dropdown-item>
										<el-dropdown-item icon="el-icon-close" :command="'delect|'+index" v-show="index!=0">删除此字段</el-dropdown-item>
										<el-dropdown-item icon="el-icon-document-copy" :command="'copy|'+index">复制此字段</el-dropdown-item>
									</el-dropdown-menu>
								</el-dropdown>
							</div>
							<textInput :text="String(value.dimensionName)" :center="true" :examine="3" @input="dimensioninput(value,$event)"></textInput>
						</div>
						<div style="width: 100px;borderLeft:1px solid #ddd;display: flex;align-items: center;justify-content: center;flex-shrink: 0;">
							<textInput :text="String(value.weight)" :examine='2' :center="true" @input="value.weight=$event"></textInput>
						</div>
						<recursion :data="value" :tier="1"></recursion>
					</div>
				</div>
			</div>

			<p style="color: #F56C6C;font-size: 12px;margin-left: 20px;margin-top: 10px;">(1,10]表示 大于1 并且 小于等于 10 <br> 双击编辑</p>

			<div style="margin-top: 50px;margin-bottom: 20px;">
				<span style="font-size: 14px;margin-left: 20px;margin-right: 10px;color: #409EFF;">计算方式:</span>
				<el-select v-model="scoreCalculateType" placeholder="请选择" size="mini">
					<el-option :key="1" label="求和" :value="1"></el-option>
					<el-option :key="2" label="加权求和" :value="2"></el-option>
				</el-select>
			</div>

			<outcontent :outcontent="outcontent" type="scorecard" style="margin-top: 20px;margin-left: 50px;">
				<div style="display:flex; align-items: center;">
					<el-select v-model="out" filterable placeholder="请选择" style="width: 200px;" clearable>
						<el-option v-for="item in FieldUser" :key="item.id" :label="item.fieldCn" :value="item.fieldEn">
						</el-option>
					</el-select>
					<p style="margin: 10px;">
						=
					</p>
					<el-select filterable value="评分" disabled style="width: 255px;">
					</el-select>
				</div>
			</outcontent>

		</div>

	</div>
</template>

<script>
	import mangeRedactMixin from '@/utils/contminxin/MangeRedactMixin.js'
	import '@/assets/css/ManageRedact.css'
	import version from '@/components/common/Version.vue'
	import outcontent from '@/components/models/outcontent.vue'
	import textInput from '../textInput.vue'
	import recursion from './recursion.vue'
	import bus from './bus.js'
	export default {
		mixins: [mangeRedactMixin],
		components: {
			outcontent,
			recursion,
			textInput,
			version
		},
		props: {
			getData: {
				type: Object,
				default: () => {
					return {}
				}
			},
			fieldTypeId: {
				type: Number,
				default: 0,
			},

			id: {
				type: Number,
				default: 0
			},

			nameId: {
				type: Number,
				default: 0
			}
		},

		data() {
			return {
				addVersionLoading: false,
				addVersionDialog: false,
				tempadd: {},
				out: '',
				versionList: [],
				addVersionStatus: false,
				version: {},
				table: [{
					"id": null,
					"cardId": '',
					"dimensionName": "",
					"fieldId": '',
					"weight": 1,
					"executeType": null,
					"createTime": null,
					"updateTime": null,
					"children": [{
						condition: '',
						children: [],
						id: null,
						"score": 1,
						"fieldId": '',
						"coefficient": 1,
						"calculateType": 1,
						custom: '',
					}]
				}],
				score: null,
				loading: false,
				valueScope: '',
				code: '',
				name: '',
				description: '',
				"outcontent": [],
				scoreCalculateType: 1,
				max: 0,

				parentId: ''
			}
		},

		created() {

			bus.$on('look', () => {
				console.log(this.table)
			})
			if (this.id != 0) {
				this.loading = true
				this.getData.getVersion({
					"id": this.id,
				}).then(res => {
					if (res.status == '1') {
						this.versionList = res.data.versionList
						this.version = JSON.parse(JSON.stringify(this.versionList[0]))
						this.code = res.data.code
						this.name = res.data.name
						this.description = res.data.description
						this.parentId = res.data.parentId
						this.getScorecardCont()



					}

				})

			}

		},
		computed: {
			FieldUser() {
				return this.$store.state.FieldUser.data.fieldList
			},
			
			
			
			tier() {

				this.max = 0

				this.each(this.table, 1)
				return this.max - 1
			}
		},
		mounted() {

		},
		methods: {
			updateVersion(tempVersion) {
				this.addVersionLoading = true
				let obj = {
					scorecardId: this.id,
					id: this.version.id,
					scorecardDimension: this.table,
					scoreCalculateType: this.scoreCalculateType,
					resultFieldEn: this.out,
					strategyOutputList: this.outcontent,
					versionCode: tempVersion.versionCode,
					description: tempVersion.description
				}
				this.getData.updateVersion(obj).then(res => {
					if (res.status == '1') {
						this.$message({
							message: '版本重命名成功',
							type: 'success'
						});
						
						this.versionList = res.data
						this.version = JSON.parse(JSON.stringify(this.versionList[0]))
						this.addVersionDialog = false
						this.addVersionStatus = false
						this.getScorecardCont()
					}
					this.addVersionLoading = false
				})
			},
			addVersionSure(tempVersion) {
				this.addVersionLoading = true
				let obj = {
					scorecardId: this.id,
					scorecardDimension: this.table,
					scoreCalculateType: this.scoreCalculateType,
					resultFieldEn: this.out,
					strategyOutputList: this.outcontent,
					versionCode: tempVersion.versionCode,
					description: tempVersion.description
				}
				this.getData.addVersion(obj).then(res => {
					if (res.status == '1') {
						this.$message({
							message: '添加版本成功',
							type: 'success'
						});
						
						this.versionList = res.data
						this.version = JSON.parse(JSON.stringify(this.versionList[0]))
						this.addVersionDialog = false
						this.addVersionStatus = false
						this.getScorecardCont()
					}
					this.addVersionLoading = false
				})
			},
			copyVersion(tempVersion) {
				this.addVersionLoading = true
				let obj = {
					scorecardId: this.id,
					id: this.version.id,
					versionCode: tempVersion.versionCode,
					description: tempVersion.description
				}
				this.getData.copyVersion(obj).then(res => {
					if (res.status == '1') {
						this.$message({
							message: '复制版本成功',
							type: 'success'
						});
						
						this.versionList = res.data
						this.version = JSON.parse(JSON.stringify(this.versionList[0]))
						this.addVersionDialog = false
						this.addVersionStatus = false
						this.getScorecardCont()
					}
					this.addVersionLoading = false
				})
			},
			addVersionExamine() {
				if (this.verification()) {
					return
				}
				this.addVersionDialog = true
			},
			versionChange() {
				this.versionList.forEach(value => {
					if (value.id === this.version.id) {
						this.version = JSON.parse(JSON.stringify(value))
					}
				})
				this.getScorecardCont()
			},
			getScorecardCont() {
				this.loading = true
				this.getData.getInfo({
					id: this.version.id
				}).then(res => {
					if (res.status == "1") {
						this.out = res.data.resultFieldEn
						this.outcontent = res.data.strategyOutputList
						if (res.data.scorecardDimension.length > 0) {
							this.table = res.data.scorecardDimension
						}
						if (res.data.scoreCalculateType) {
							this.scoreCalculateType = res.data.scoreCalculateType
						}

					}
					this.loading = false
				})
			},
			delectVersion() { //删除版本
				this.getData.delectVersion({
					status: -1,
					ids: [this.version.id],
					strategyId: this.id
				}).then(res => {
					if (res.status == "1") {
						this.$message({
							message: '删除成功',
							type: 'success'
						});
						
						this.versionList = res.data
						this.version = JSON.parse(JSON.stringify(this.versionList[0]))
						this.getScorecardCont()
					}
				})
			},
			addVaersionSure() { // 二次确认添加新版本
				this.addVersionLoading = true
				let that = this
				if (this.verificationTempVersion()) {
					that.addVersionLoading = false
					return
				}

				if (this.addVersionStatus) { // 添加版本
					add()
				} else if (this.updateVersion) { //重命名版本
					updateVersion()
				} else { // 复制版本
					copy()

				}


				function add() {
					let obj = {
						scorecardId: that.id,
						scorecardDimension: that.table,
						scoreCalculateType: that.scoreCalculateType,
						resultFieldEn: that.out,
						strategyOutputList: that.outcontent,
						versionCode: that.tempVersion.versionCode,
						description: that.tempVersion.description
					}
					that.getData.addVersion(obj).then(res => {
						if (res.status == '1') {
							that.$message({
								message: '添加版本成功',
								type: 'success'
							});
							that.$store.dispatch('regetcache','SCO')
							that.versionList = res.data
							that.version = JSON.parse(JSON.stringify(that.versionList[0]))
							that.addVersionDialog = false
							that.addVersionStatus = false
							that.getScorecardCont()
						}
						that.addVersionLoading = false
					})
				}

				function copy() {
					let obj = {
						scorecardId: that.id,
						id: that.version.id,
						versionCode: that.tempVersion.versionCode,
						description: that.tempVersion.description
					}
					that.getData.copyVersion(obj).then(res => {
						if (res.status == '1') {
							that.$message({
								message: '复制版本成功',
								type: 'success'
							});
							that.$store.dispatch('regetcache','SCO')
							that.versionList = res.data
							that.version = JSON.parse(JSON.stringify(that.versionList[0]))
							that.addVersionDialog = false
							that.addVersionStatus = false
							that.getScorecardCont()
						}
						that.addVersionLoading = false
					})
				}

				function updateVersion() {
					let obj = {
						scorecardId: that.id,
						id: that.version.id,
						scorecardDimension: that.table,
						scoreCalculateType: that.scoreCalculateType,
						resultFieldEn: that.out,
						strategyOutputList: that.outcontent,
						versionCode: that.tempVersion.versionCode,
						description: that.tempVersion.description
					}
					that.getData.updateVersion(obj).then(res => {
						if (res.status == '1') {
							that.$message({
								message: '版本重命名成功',
								type: 'success'
							});
							that.$store.dispatch('regetcache','SCO')
							that.versionList = res.data
							that.version = JSON.parse(JSON.stringify(that.versionList[0]))
							that.addVersionDialog = false
							that.addVersionStatus = false
							that.getScorecardCont()
						}
						that.addVersionLoading = false
					})
				}




			},
			sureAddVersion() { // 确认添加新版本
				if (this.verification()) {
					return
				}
				this.addVersionDialog = true

			},
			reset() { //重置默认数据
				this.out = ''
				this.table = [{
					"id": null,
					"cardId": '',
					"dimensionName": "",
					"fieldId": '',
					"weight": 1,
					"executeType": null,
					"createTime": null,
					"updateTime": null,
					"children": [{
						condition: '',
						children: [],
						id: null,
						"score": 1,
						"fieldId": '',
						"coefficient": 1,
						"calculateType": 1,
						custom: '',
					}]
				}]
				this.outcontent = []
				this.scoreCalculateType = 1
			},
			addVersion() {
				this.addVersionStatus = true
				this.tempadd = {
					out: this.out,
					table: this.table,
					outcontent: this.outcontent,
					scoreCalculateType: this.scoreCalculateType
				}
				this.reset()
			},
			addVersionClose() {
				this.addVersionStatus = false

				this.out = this.tempadd.out
				this.table = this.tempadd.table
				this.outcontent = this.tempadd.outcontent
				this.scoreCalculateType = this.tempadd.scoreCalculateType

			},
			dimensioninput(value, e) {
				value.dimensionName = e
			},
			handleCommand(str) {
				console.log(str)
				switch (str.split('|')[0]) {
					case 'addBrother':
						this.addBrother(str.split('|')[1])
						break;
					case 'delect':
						this.delect(str.split('|')[1])
						break;
					case 'addSon':
						this.addSon(str.split('|')[1])
						break;
					case 'copy':
						this.copy(str.split('|')[1])
						break;
				}
			},
			each(data, floor) {
				data.forEach(e => {
					e.floor = floor
					if (floor > this.max) {
						this.max = floor
					}
					if (e.children.length > 0) {
						this.each(e.children, floor + 1)
					}
				})
			},
			objectSpanMethod() {
				return {
					rowspan: 1,
					colspan: 1
				}
			},
			outAdd(index) {
				this.outcontent.splice(index, 0, {
					"fieldId": "",
					"fieldValue": ""
				})
			},
			outDelect(index) {
				this.outcontent.splice(index, 1, )
			},
			examine(item, issumm) {

				item.forEach(value => {
					let Z = /^((\[|\()(\d(\d)*(\.(\d)+)?)|\(),((\d(\d)*(\.(\d)+)?(\]|\)))|\))$/

					if (value.children.length > 0) {
						this.examine(value.children, issumm)
					}
					if (value.children.length == 0) {
						if (value.calculateType == 1 && (String(value.score).trim() === "" || isNaN(Number(String(value.score).trim())))) {
							issumm.is = false
							issumm.msg = '请检查是否有分值未填'
						}
						if (value.calculateType == 2 && (String(value.coefficient).trim() === "" || isNaN(Number(String(value.coefficient)
								.trim())))) {
							issumm.is = false
							issumm.msg = '请检查是否有系数未填'
						}
						if (value.calculateType == 3 && (String(value.custom).trim() === "" || JSON.parse(value.custom).formula.trim() ===
								"")) {
							issumm.is = false
							issumm.msg = '请检查是否有自定义评分未填'
						}


					}
					if (!value.weight && value.weight != 0) {
						if (!Z.test(value.condition)) {
							issumm.is = false
							issumm.msg = '请检查是否有范围未填'
						}
					}
					if (!value.weight && value.weight != 0) {
						if (!value.fieldId) {
							issumm.is = false
							issumm.msg = '请检查是否有指标未选'
						}
					} else {
						if (!String(value.dimensionName).trim()) {
							issumm.is = false
							issumm.msg = '请检查是否有维度未填'
						}
					}

				})
			},
			verification() {
				let reg = /[\u4e00-\u9fa5]+/g;
				if (this.code.match(reg) != null) {
					this.$message.error('代码不允许出现中文');
					return true
				}
				if(this.verificationCode(this.code)||this.verificationName(this.name)){
					return true
				}
				let issumm = {
					is: true,
					msg: '提交失败 请检查是否有未填项'
				}


				if (this.code.trim() == '') {
					this.$message.error('请填入评分卡代码，并检查空格');
					return true
				}
				if (this.name.trim() == '') {
					this.$message.error('请填入评分卡名称，并检查空格');
					return true
				}
				if (this.description.trim() == '') {
					this.$message.error('请填入评分卡描述，并检查空格');
					return true
				}




				this.outcontent.forEach(value => {
					if (value.fieldId === "" || String(value.fieldValue).trim() === "" || value.variableType === "") {

						issumm.is = false
						issumm.msg = '请检查自定义输出部分是否有未填项'
					}
					if (value.variableType == 3 && (String(value.fieldValue).trim() === "" || JSON.parse(value.fieldValue).formula.trim() ===
							'')) {
						issumm.is = false
						issumm.msg = '请检查自定义输出部分是否有未填项'
					}
				})


				this.examine(this.table, issumm)
				if (!issumm.is) {
					this.$message.error(issumm.msg);
					issumm.is = true
					return true
				}
				let num = 0
				let is = false
				this.table.forEach(value => {
					if (Number(value.weight) < 0) {
						is = true
						return true
					}
					if (String(value.weight).trim() == "") {
						is = true
					}
					num += Number(value.weight)

				})
				if (is) {
					this.$message.error('权重不允许有负数或空值或空格');
					return true
				}
				if (num != 1 && num != 100 && num != 0) {
					this.$message.error('权重的和需要为100或1或0');
					return true
				}
			},
			submit() {

				if (this.verification()) {
					return
				}
				let obj = {
					"code": this.code.trim(),
					"name": this.name.trim(),
					"description": String(this.description).trim(),
					"author": null,
					"userId": null,
					"status": 1,
					"created": null,
					"updated": null,
					versionList: [{
						scorecardDimension: this.table,
						scoreCalculateType: this.scoreCalculateType,
						resultFieldEn: this.out,
						strategyOutputList: this.outcontent
					}]
				}

				if (this.id == 0) {
					this.loading = true
					obj.parentId = this.nameId == 99999999 ? 0 : this.nameId
					obj.parentIds = this.nameId == 99999999 ? 0 : this.nameId
					obj.versionList[0].versionCode = 'V:0'
					obj.versionList[0].description = '初始版本'
					this.getData.setsave(obj).then(res => {
						this.loading = false
						if (res.status === "1") {
							this.$message({
								message: '添加成功',
								type: 'success'
							});
							
							this.$emit('Ok', res.data.id)
							this.versionList = res.data.versionList
							this.version = JSON.parse(JSON.stringify(this.versionList[0]))
						}
					}).catch(err => {
						this.loading = false
						this.$message.error('网络出现问题-_-');
					})
				} else {
					obj.id = this.id
					obj.parentId = this.parentId
					obj.parentIds = this.parentId
					obj.versionList[0].id = this.version.id

					this.loading = true
					this.getData.updatafield(obj).then(res => {
						this.loading = false
						if (res.status === "1") {
							this.$message({
								message: '修改成功',
								type: 'success'
							});
							
							// this.$emit('Ok')
						}
					}).catch(err => {
						this.loading = false
						this.$message.error('网络出现问题-_-');
					})
				}
			},
			change(index) {
				this.ruledata[index].operator = ""
				this.ruledata[index].fieldValue = ""
			},
			faadd(index) { //rule父节点添加
				console.log(index)
				this.ruledata.content.splice(index, 0, {
					output: {
						field_name: '1'
					}
				})
				console.log(this.ruledata)

			},
			fadelect(index) {
				this.ruledata.splice(index, 1)
			},
			// ====================================== v2.1 部分===================================
			addSon(index) {
				this.table[index].children.push({
					condition: '',
					children: [],
					id: null,
					"score": 1,
					"coefficient": null,
					"calculateType": 1,
					custom: '',
				})
			},
			delect(index) {
				this.table.splice(index, 1)
			},
			addBrother(index) {
				this.table.splice(index + 1, 0, {
					weight: 0,
					condition: '',
					dimensionName: '',
					children: [{
						condition: '',
						children: [],
						id: null,
						"score": 1,
						"fieldId": '',
						"coefficient": null,
						"calculateType": 1,
						custom: '',
					}],
					id: null,
					"score": 1,
					"coefficient": null,
					"calculateType": 1,
					custom: '',
				})
			},
			copy(index) {
				let obj = JSON.parse(JSON.stringify(this.table[index]))
				obj.id = null
				this.table.splice(index + 1, 0, obj)
				bus.$emit('look')

			},

		}











	}
</script>

<style>

.dec_top{
	
	display: flex;
}
.dec_cont{

}
.dec_table_header{
	width: 350px;
	border-left: 1px solid #ddd;
	border-bottom: 1px solid #ddd;
}
.dec_table_comm{
}
.dec_score{
	width: 199px;
	height: 80px;
	text-align: center;
	line-height: 80px;
	border-left: 1px solid #ddd;
	border-bottom: 1px solid #ddd;
	box-sizing: border-box;
}
.dec_home{
	padding-bottom: 20px;
	border-top: 1px solid #ddd;
	overflow: scroll;
	flex: 1;
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
					<span v-if="id===0">新增决策表 :</span>
					<span v-else>编辑决策表 :</span>
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
				<p>决策代码: </p>
				<el-input placeholder="请输入决策表代码" maxlength="30" v-model="code" clearable :disabled="addVersionStatus"></el-input>
			</div>
			<div>
				<p>决策名称:</p>
				<el-input placeholder="请输入规则名称" maxlength="20" v-model="name" clearable :disabled="addVersionStatus"></el-input>
			</div>
			<div>

			</div>
		</div>
		<div :class="smallHeader?'MR_scope MR_scopeSmall':'MR_scope'">
			<div>
				<p>决策描述: </p>
				<el-input placeholder="请输入决策描述" maxlength="300" v-model="description" clearable :disabled="addVersionStatus"></el-input>
			</div>
		</div>

		<version style="margin:5px;" :id="id" :ruleVersionList="decisionTablesVersionList" :version="version"
		 @addVersion="addVersion" @addVersionClose="addVersionClose" @copyVersion="copyVersion" @delectVersion="delectVersion"
		 @updateVersion="updateVersion" @addVersionSure="addVersionSure" :addVersionDialog="addVersionDialog" @Dialog="addVersionDialog=$event"
		 @addVersionExamine="addVersionExamine" :addVersionLoading="addVersionLoading" @versionChange="versionChange"
		 :addVersionStatus="addVersionStatus" @StatusChange="addVersionStatus=$event"></version>
		<!-- 版本部分 -->
		<div class="dec_home" v-if="leftDetailVo&&topDetailVo&&resultList" :key="num">
			<!-- 决策主体 -->
			<div class="dec" style="width: auto;">
				<div class="dec_top">
					<!-- top -->
					<div class="dec_table_header" :style="{width:width,flexShrink:'0'}">
					</div>
					<div class="dec_table_comm">
						<topRecursion :data="topDetailVo" direction="row"></topRecursion>
					</div>
				</div>
				<div class="dec_cont" style="display: flex;">
					<recursion :data="leftDetailVo" ref="left" id="left" style="flex-shrink: 0;flex-grow:0"></recursion>
					<div>
						<div v-for="(value,index) in resultList" :key="index" style="display: flex;">
							<div v-for="(item,inde) in value" :key="inde" class="dec_score" :style="{borderRight:inde==value.length-1?'1px solid #ddd':''}">
								<textInput :text="String(item)" click="click" :center="true" :examine="3" @input="input($event,index,inde)"></textInput>
							</div>
						</div>
					</div>
				</div>
			</div>
			<p style="font-size: 12px;color: #666;margin-top: 5px;">*如果只有一行或者一列 且 没有次级节点 则 此行/列 可以为空 执行时会被默认选中</p>
			<outcontent :outcontent="outcontent" type="decision_tables" style="margin-top: 20px;margin-left: 50px;">
				<div style="display:flex; align-items: center;">
					<el-select v-model="out" filterable placeholder="请选择" style="width: 200px;" clearable>
						<el-option v-for="item in FieldUser" :key="item.id" :label="item.fieldCn" :value="item.fieldEn">
						</el-option>
					</el-select>
					<p style="margin: 10px;">
						=
					</p>
					<el-select filterable value="决策结果" disabled style="width: 255px;">
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
	import recursion from './recursion.vue'
	import topRecursion from './topRecursion.vue'
	import textInput from '../textInput.vue'
	import bus from './bus.js'
	export default {
		mixins: [mangeRedactMixin],
		components: {
			version,
			outcontent,
			topRecursion,
			recursion,
			textInput,
			bus
		},
		props: {
			fieldTypeId: {
				type: Number,
				default: 0,
			},
			getData: {
				type: Object,
				default () {
					return {}
				}
			},
			id: {
				type: Number,
				default: 0
			},
			type: {
				type: Number,
				default: 1
			},
			nameId: {
				type: Number,
				default: 0
			}
		},

		data() {
			return {
				num: 0,
				addVersionStatus: false,
				addVersionLoading: false,
				addVersionDialog: false,
				tempadd: {},
				out: '',
				"outcontent": [],
				SpecialField: {
					score: '',
					ruleAudit: 5,
					lastLogical: '-1'
				},
				loading: false,
				priority: '',
				code: '',
				name: '',
				description: '',
				leftDetailVo: null,
				topDetailVo: null,
				resultList: null,
				width: '200px',
				leftLast: [],
				leftLastobj: [],
				topLast: [],
				TopLastobj: [],
				parentId: '',
				decisionTablesVersionList: [],
				version: {}
			}
		},
		created() {
			if (this.id != 0) {
				this.loading = true
				this.getData.getVersion(this.id).then(res => {
					if (res.status == '1') {
						this.code = res.data.code
						this.name = res.data.name
						this.priority = res.data.priority
						this.description = res.data.description
						this.parentId = res.data.parentId
						// 版本部分
						this.decisionTablesVersionList = res.data.decisionTablesVersionList
						this.version = JSON.parse(JSON.stringify(this.decisionTablesVersionList[0]))

						this.getDecCont()
					}

				})
			} else {
				this.leftDetailVo = [{
					"fieldId": '',
					"fieldEn": [],
					"logical": "&&",
					// "type": 2,
					"indexValue": 0,
					"valueType": null,
					"conditionList": [{
						"operator": "",
						"fieldValue": "",
						variableType: 1
					}],
					"children": [],
					random: Math.random()
				}]
				this.topDetailVo = [{
					"fieldId": '',
					"fieldEn": [],
					"logical": "&&",
					// "type": 2,
					"indexValue": 0,
					"valueType": null,
					"conditionList": [{
						"operator": "",
						"fieldValue": "",
						variableType: 1
					}],
					"children": [],
					random: Math.random()
				}]
				this.resultList = [
					[0]
				]
			}
		},
		computed: {
			FieldUser() {
				if (!this.$store.state.FieldUser) {
					return []
				} else {
					return this.$store.state.FieldUser.data.fieldList
				}
			}
		},
		mounted() {
			bus.$on('reCount', (e) => {
				this.$nextTick(() => {
					if (this.$refs.left) {
						this.width = this.$refs.left.$el.clientWidth + 'px'
					}
					if (e == 'addLeft') {
						this.addLeft()
					} else if (e == 'addTop') {
						this.addTop()
					} else if (e == 'delectLeft') {
						this.delectLeft()
					} else if (e == 'delectTop') {
						this.delectTop()
					} else if (e.type == 'copyTop') {
						this.copyTop(e.data)
					} else if (e.type == 'copyLeft') {
						this.copyLeft(e.data)
					}
				})
			})
			this.getLast()
		},
		methods: {
			versionChange(e) {
				this.version = JSON.parse(JSON.stringify(e))
				this.getDecCont()
			},
			addVersionExamine() {
				if (this.examine()) {
					return
				}
				this.addVersionDialog = true
			},
			addVersionSure(tempVersion) {
				this.Enformat(this.leftDetailVo,'String')
				this.Enformat(this.topDetailVo,'String')
				this.addVersionLoading = true
				let obj = {
					decisionTablesId: this.id,
					versionCode: tempVersion.versionCode,
					description: tempVersion.description,
					resultFieldEn: this.out,
					strategyOutputList: this.outcontent,
					leftDetailVo: this.leftDetailVo,
					topDetailVo: this.topDetailVo,
					resultSet: {
						resultList: this.resultList,
						columns: this.resultList[0].length,
						rows: this.resultList.length,
					}
				}
				this.getData.addVersion(obj).then(res => {
					if (res.status == '1') {
						this.$message({
							message: '添加版本成功',
							type: 'success'
						});
					
						this.decisionTablesVersionList = res.data
						this.version = JSON.parse(JSON.stringify(this.decisionTablesVersionList[0]))
						this.getDecCont()
					}
					this.addVersionStatus = false
					this.addVersionDialog = false
					this.addVersionLoading = false
				})
			},
			updateVersion(tempVersion) {
				if (this.examine()) {
					return
				}
				this.Enformat(this.leftDetailVo,'String')
				this.Enformat(this.topDetailVo,'String')
				this.addVersionLoading = true
				let obj = {
					decisionTablesId: this.id,
					id: this.version.id,
					versionCode: tempVersion.versionCode,
					description: tempVersion.description,
					resultFieldEn: this.out,
					strategyOutputList: this.outcontent,
					leftDetailVo: this.leftDetailVo,
					topDetailVo: this.topDetailVo,
					resultSet: {
						resultList: this.resultList,
						columns: this.resultList[0].length,
						rows: this.resultList.length,
					}

				}
				this.getData.updateVersion(obj).then(res => {
					if (res.status == '1') {
						this.$message({
							message: '版本重命名成功',
							type: 'success'
						});
						this.decisionTablesVersionList = res.data
					
						this.version = JSON.parse(JSON.stringify(this.decisionTablesVersionList[0]))
						this.getDecCont()

					}
					this.addVersionDialog = false
					this.addVersionLoading = false
				})
			},
			delectVersion() {
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
					
						this.decisionTablesVersionList = res.data
						this.version = JSON.parse(JSON.stringify(this.decisionTablesVersionList[0]))
						this.getDecCont()
					}
				})
			},
			copyVersion(tempVersion) {
				this.addVersionLoading = true
				this.getData.copyVersion({
					decisionTablesId: this.id,
					id: this.version.id,
					versionCode: tempVersion.versionCode,
					description: tempVersion.description
				}).then(res => {
					if (res.status == '1') {
						this.$message({
							message: '复制版本成功',
							type: 'success'
						});
					
						this.decisionTablesVersionList = res.data
						this.version = JSON.parse(JSON.stringify(this.decisionTablesVersionList[0]))
						this.getDecCont()

					}
					this.addVersionDialog = false
					this.addVersionLoading = false
				})
			},
			addVersionClose() {

				this.out = this.tempadd.out
				this.outcontent = this.tempadd.outcontent
				this.leftDetailVo = this.tempadd.leftDetailVo
				this.topDetailVo = this.tempadd.topDetailVo
				this.resultList = this.tempadd.resultList
				this.getLast()
				this.$nextTick(() => {
					this.width = this.$refs.left.$el.clientWidth + 'px'
				})
			},
			addVersion() {
				this.tempadd = {
					out: this.out,
					outcontent: this.outcontent,
					leftDetailVo: this.leftDetailVo,
					topDetailVo: this.topDetailVo,
					resultList: this.resultList,
				}

				this.reset()
				this.getLast()
				this.$nextTick(() => {
					this.width = this.$refs.left.$el.clientWidth + 'px'
				})
			},
			getDecCont() {
				this.loading = true
				this.getData.getInfo(this.version.id).then(res => {
					if (res.status == '1') {
						this.num++
						this.out = res.data.resultFieldEn
						this.outcontent = res.data.strategyOutputList
						// console.log(this.outcontent)
						// this.$set(this.outcontent,res.data.strategyOutputList)
						this.leftDetailVo = res.data.leftDetailVo
						this.topDetailVo = res.data.topDetailVo
						this.resultList = res.data.resultSet.resultList
						
						this.Enformat(this.leftDetailVo,'Array')
						this.Enformat(this.topDetailVo,'Array')
						
						setTimeout(() => {
							this.width = this.$refs.left.$el.clientWidth + 'px'
						}, 10)
						this.getLast()
						this.loading = false
					}
				})
			},
			reset() {
				this.out = ''
				this.outcontent = []
				this.leftDetailVo = [{
					"fieldId": '',
					"fieldEn": [],
					"logical": "&&",
					// "type": 2,
					"indexValue": 0,
					"valueType": null,
					"conditionList": [{
						"operator": "",
						"fieldValue": "",
						variableType: 1
					}],
					"children": [],
					random: Math.random()
				}]
				this.topDetailVo = [{
					"fieldId": '',
					"fieldEn": [],
					"logical": "&&",
					// "type": 2,
					"indexValue": 0,
					"valueType": null,
					"conditionList": [{
						"operator": "",
						"fieldValue": "",
						variableType: 1
					}],
					"children": [],
					random: Math.random()
				}]
				this.resultList = [
					[0]
				]
			},
			examine() {
				let reg = /[\u4e00-\u9fa5]+/g;
				if (this.code.match(reg) != null) {
					this.$message.error('代码不允许出现中文');
					return true
				}
				if(this.verificationCode(this.code)||this.verificationName(this.name)){
					return true
				}
				if (this.code.trim() == '') {
					this.$message.error('请填入决策表代码，并检查空格');
					return true
				}
				if (this.name.trim() == '') {
					this.$message.error('请填入决策表名称，并检查空格');
					return true
				}
				if (this.description.trim() == '') {
					this.$message.error('请填入决策表描述，并检查空格');
					return true
				}
				let is = {
					is: false,
					msg: '提交失败，请检查是否有未填项'
				}
				this.getIsempty(is, this.leftDetailVo)
				this.getIsempty(is, this.topDetailVo)
				this.outcontent.forEach(value => {
					if (value.fieldId === "" || String(value.fieldValue).trim() === "" || value.variableType === "") {
						is.is = true
						is.msg = '请检查自定义输出部分是否有未填项'
					}
					if (value.variableType == 3 && (String(value.fieldValue).trim() === "" || JSON.parse(value.fieldValue).formula.trim() ===
							'')) {
						is.is = true
						is.msg = '请检查自定义输出部分是否有未填项'
					}
				})
				if (is.is) {
					this.$message.error(is.msg)
					return true
				}
				
				this.leftLastobj = []
				this.TopLastobj = []
				this.getLastObj(this.leftLastobj, this.leftDetailVo, true)
				this.getLastObj(this.TopLastobj, this.topDetailVo, true)
				this.leftLastobj.forEach((value, index) => {
					value.indexValue = index
					value.type=2
				})
				this.TopLastobj.forEach((value, index) => {
					value.indexValue = index
					value.type=2
				})
				let is2 = false
				this.resultList.forEach(value => {
					value.forEach(item => {
						if (String(item).trim() === "") {
							is2 = true
						}
					})
				})
				if (is2) {
					this.$message.error('得分不能为空')
					return true
				}
			},
			getLast() {
				this.leftLastobj = []
				this.TopLastobj = []
				this.getleftLast().then(res => {
					this.leftLast = res
				})
				this.gettopLast().then(res => {
					this.topLast = res
				})
				this.getLastObj(this.leftLastobj, this.leftDetailVo)
				this.getLastObj(this.TopLastobj, this.topDetailVo)
			},
			input(e, index, inde) {
				this.resultList[index][inde] = e
			},
			copyTop(data) {
				let arr = [] //总对象
				let copearr = [] // 插入的索引
				let datalist = [] // 取出的对象
				let datalistarr = [] // 取出的对象 在总对象索引
				let temparr = [] //临时储存即将插入的内容
				this.getLastObj(arr, this.topDetailVo)
				arr.forEach((value, index) => {
					let is = true
					this.TopLastobj.forEach(item => {
						if (item == value) {
							is = false
						}

					})
					if (is) {
						copearr.push(index)
					}
				})
				this.getLastObj(datalist, [data])
				datalist.forEach(value => {
					arr.forEach((item, index) => {
						if (item == value) {
							datalistarr.push(index)
						}
					})
				})
				this.resultList.forEach(item => {
					copearr.forEach((value, index) => {
						item.splice(value, 0, item[datalistarr[index]])
					})
				})
				this.TopLastobj = arr

				this.getLast()
			},
			copyLeft(data) {
				let arr = [] //总对象
				let copearr = [] // 插入的索引
				let datalist = [] // 取出的对象
				let datalistarr = [] // 取出的对象 在总对象索引
				let temparr = [] //临时储存即将插入的内容
				this.getLastObj(arr, this.leftDetailVo)
				arr.forEach((value, index) => {
					let is = true
					this.leftLastobj.forEach(item => {
						if (item == value) {
							is = false
						}
					})
					if (is) {
						copearr.push(index)
					}
				})
				this.getLastObj(datalist, [data])
				datalist.forEach(value => {
					arr.forEach((item, index) => {
						if (item == value) {
							datalistarr.push(index)
						}
					})
				})

				console.log(copearr, datalistarr)


				datalistarr.forEach(value => {
					temparr.push(this.resultList[value])
				})
				this.resultList.splice(copearr[0], 0, ...JSON.parse(JSON.stringify(temparr)))
				this.leftLastobj = arr
				this.getLast()
			},
			delectTop() {
				let arr = []
				let delectarr = []
				this.getLastObj(arr, this.topDetailVo)
				if (arr.length == this.TopLastobj.length) {
					this.TopLastobj = arr;
					this.getLast()
					return
				}

				this.TopLastobj.forEach((value, index) => {
					let is = true
					arr.forEach(item => {
						if (item == value) {
							is = false
						}

					})
					if (is) {
						delectarr.push(index)
					}
				})
				console.log(this.TopLastobj, arr, delectarr)
				this.TopLastobj = arr

				delectarr.forEach(value => {
					this.resultList.forEach(item => {
						item[value] = null
					})
				})
				this.resultList = this.resultList.map(value => {
					let arr = value.filter((item) => {
						return item !== null
					})
					return arr
				})
				this.getLast()
			},
			delectLeft() {
				let arr = []
				let delectarr = []
				this.getLastObj(arr, this.leftDetailVo)
				if (arr.length == this.leftLastobj.length) {
					this.leftLastobj = arr;
					this.getLast()
					return
				}
				this.leftLastobj.forEach((value, index) => {
					let is = true
					arr.forEach(item => {
						if (item == value) {
							is = false
						}
					})
					if (is) {
						delectarr.push(index)
					}
				})
				this.leftLastobj = arr


				delectarr.forEach(value => {
					delete this.resultList[value]
				})
				this.resultList = this.resultList.filter(value => {
					return value
				})
				this.getLast()
			},
			addTop() {
				this.gettopLast().then(res => {
					this.TopLastobj = []
					this.getLast()
					if (res.length == this.topLast.length) {
						this.topLast = res
						return
					}
					for (let i = 0; i < res.length; i++) {
						if (this.topLast[i] != res[i]) {

							this.resultList.forEach(value => {
								value.splice(i, 0, 0)
							})
							// console.log(this.topLast , res)
							this.topLast = res
							return
						}
					}


				})
			},
			addLeft() {
				this.getleftLast().then(res => {
					this.leftLastobj = []
					this.getLast()


					if (res.length == this.leftLast.length) {
						this.leftLast = res
						return
					}

					for (let i = 0; i < res.length; i++) {
						if (this.leftLast[i] != res[i]) {
							this.gettopLast().then(res => {
								let arr = []
								res.forEach(value => {
									arr.push(0)
								})
								this.resultList.splice(i, 0, arr)
							})
							this.leftLast = res
							return
						}
					}
				})
			},
			getLastObj(arr, obj, type = false) {
				if (!Array.isArray(obj)) {
					return
				}
				obj.forEach(value => {
					if (type) {
						value.valueType = this.mixinGetvalueType(value.fieldId)
					}
					if (value.children.length > 0) {
						this.getLastObj(arr, value.children, type)
					} else {
						arr.push(value)
					}
				})
			},
			async getleftLast() {
				let arr = []
				await this.$nextTick(() => {
					arr = document.querySelectorAll('.leftlast')
				})
				return arr
			},
			async gettopLast() {
				let arr = []
				await this.$nextTick(() => {
					arr = document.querySelectorAll('.toplast')
				})
				return arr
			},
			getIsempty(is, obj) {
				console.log(obj)
				if(obj.length==1&&obj[0].children.length==0&&(obj[0].fieldId==''||obj[0].fieldId==null)){
					return
				}
				obj.forEach(value => {
					if(value.indexValue != undefined){
						delete value.indexValue
					}
					value.type = 1
					value.conditionList.forEach(value => {
						if (!value.operator || (!value.fieldValue && value.fieldValue !== 0)) {
							is.is = true
							is.msg = "请检查是否有指标的条件未填写"
						}
					})
					if (!value.fieldId) {
						is.is = true
						is.msg = "请检查是否有指标未选"
					}
					if (value.children.length > 0) {
						this.getIsempty(is, value.children)
					}
				})
			},
			Enformat(arr,str){
				arr.forEach(value=>{
					if(str=='String'){
						if(Array.isArray(value.fieldEn)){  //兼容老数据
							value.fieldEn=value.fieldEn.join('.')
						}
					}else if(str=='Array'){
						value.fieldEn=value.fieldEn.split('.')
					}
					
					if (value.children.length > 0) {
						this.Enformat(value.children,str)
					}
				})
			},
			submit() {
				this.loading = true
				if (this.examine()) {
					this.loading = false
					return
				}
				
				
				this.Enformat(this.leftDetailVo,'String')
				this.Enformat(this.topDetailVo,'String')
				
				let obj = {
					"code": this.code.trim(),
					"name": this.name.trim(),
					"description": String(this.description).trim(),
					decisionTablesVersionList: [{
						resultFieldEn: this.out,
						strategyOutputList: this.outcontent,
						leftDetailVo: this.leftDetailVo,
						topDetailVo: this.topDetailVo,
						resultSet: {
							resultList: this.resultList,
							columns: this.resultList[0].length,
							rows: this.resultList.length,
						}
					}]

				}
				if (this.id == 0) {

					obj.decisionTablesVersionList[0].versionCode = 'V:0'
					obj.decisionTablesVersionList[0].description = '初始版本'
					obj.parentId = this.nameId == 99999999 ? 0 : this.nameId,
						this.getData.setsave(obj).then(res => {
							this.loading = false
							if (res.status === "1") {
								this.$message({
									message: '添加成功',
									type: 'success'
								});
								this.$emit('Ok')
							}
						}).catch(err => {
							this.loading = false
							this.$message.error('网络出现问题-_-');
						})
				} else {
					obj.id = this.id
					obj.parentId = this.parentId
					obj.decisionTablesVersionList[0].id = this.version.id
					obj.decisionTablesVersionList[0].decisionTablesId = this.id

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






				console.log(obj)
			}
		},
		watch: {

		},
		computed: {

		}











	}
</script>

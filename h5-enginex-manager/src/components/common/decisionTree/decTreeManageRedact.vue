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
	border-top: 1px solid #ddd;
	overflow: scroll;
	flex: 1;
}



</style>
<template>
	<!-- :style="{width:$store.state.barShrink?'95vw':'85vw'}" -->
	<div class="dataManageRedact" v-loading="loading" >

		<div :class="smallHeader?'MR_header MR_headerSmall':'MR_header'">
			<div>
				<div>
					<el-button type="primary" icon="el-icon-arrow-left" circle @click="mixinClose"></el-button>
				</div>
				<div>
					<span v-if="id===0">新增决策树 :</span>
					<span v-else>编辑决策树 :</span>
				</div>
			</div>
			<div>
				<el-button :icon="smallHeader?'el-icon-bottom':'el-icon-top'" circle @click="openHeader">
				</el-button>
				<el-button type="success" icon="el-icon-check" circle @click="submit" :disabled="addVersionStatus"></el-button>
			</div>
		</div>
		<div :class="smallHeader?'MR_input MR_inputSmall':'MR_input'">
			<div>
				<p>决策树代码: </p>
				<el-input placeholder="请输入决策树代码" maxlength="30" v-model="code" clearable :disabled="addVersionStatus"></el-input>
			</div>
			<div>
				<p>决策树名称:</p>
				<el-input placeholder="请输入决策树名称" maxlength="20" v-model="name" clearable :disabled="addVersionStatus"></el-input>
			</div>
			<div>

			</div>
		</div>
		<div :class="smallHeader?'MR_scope MR_scopeSmall':'MR_scope'">
			<div>
				<p>决策树描述: </p>
				<el-input placeholder="请输入决策描述" maxlength="300" v-model="description" clearable :disabled="addVersionStatus"></el-input>
			</div>
		</div>

		<version style="margin:5px;" :id="id" :ruleVersionList="versionList" :version="version" @addVersion="addVersion"
		 @addVersionClose="addVersionClose" @copyVersion="copyVersion" @delectVersion="delectVersion" @updateVersion="updateVersion"
		 @addVersionSure="addVersionSure" :addVersionDialog="addVersionDialog" @Dialog="addVersionDialog=$event"
		 @addVersionExamine="addVersionExamine" :addVersionLoading="addVersionLoading" @versionChange="versionChange"
		 :addVersionStatus="addVersionStatus" @StatusChange="addVersionStatus=$event"></version>
		<!-- 版本部分 -->
			
			<div class="dec_home">
				<tree :treeData="detailList[0]"></tree>
				<outcontent :outcontent="outcontent" type="decision_tree" style="margin-top: 20px;margin-left: 50px;">
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
	import tree from '@/components/common/decisionTree/tree.vue'
	import version from '@/components/common/Version.vue'
	import outcontent from '@/components/models/outcontent.vue'
	import textInput from '../textInput.vue'
	export default {
		mixins:[mangeRedactMixin],
		components: {
			tree,
			version,
			outcontent,
			textInput,
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
				detailList:[],
				num:0,
				addVersionStatus: false,
				addVersionLoading: false,
				addVersionDialog: false,
				tempadd: {},
				out: '',
				"outcontent": [],
				loading: false,
				priority: '',
				code: '',
				name: '',
				description: '',
				parentId: '',
				versionList: [],
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
						this.parentId = res.data.folderId
						// 版本部分
						this.versionList = res.data.versionList
						this.version = JSON.parse(JSON.stringify(this.versionList[0]))
						this.getDecCont()
					}
				})
			} else {
				this.detailList = [{
					nodeType:'', //普通节点为1 最终节点为2
					variableType:1,
					resultValue:'',
					logical:'&&',
					fieldEn:[],
					fieldId:'',
					conditionList:[{
						variableType:1,
						operator:'',
						fieldValue:''
					}],
					children:[]
				}]
				// 新增初始化
				
			}
		},
		computed: {
			FieldUser() {
				if (!this.$store.state.FieldUser) {
					return []
				} else {
					return this.$store.state.FieldUser.data.fieldList
				}
			},
			
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
				this.addVersionLoading = true
				this.Enformat(this.detailList,'String')
				let obj = {
					decisionTreeId: this.id,
					versionCode: tempVersion.versionCode,
					description: tempVersion.description,
					resultFieldEn: this.out,
					strategyOutputList: this.outcontent,
					detailList:this.detailList
				}
				this.getData.addVersion(obj).then(res => {
					if (res.status == '1') {
						this.$message({
							message: '添加版本成功',
							type: 'success'
						});
						
						this.versionList = res.data
						this.version = JSON.parse(JSON.stringify(this.versionList[0]))
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
				this.addVersionLoading = true
				
				this.Enformat(this.detailList,'String')
				let obj = {
					decisionTreeId: this.id,
					id: this.version.id,
					versionCode: tempVersion.versionCode,
					description: tempVersion.description,
					resultFieldEn: this.out,
					strategyOutputList: this.outcontent,
					detailList:this.detailList

				}
				this.getData.updateVersion(obj).then(res => {
					if (res.status == '1') {
						this.$message({
							message: '版本重命名成功',
							type: 'success'
						});
						
						this.versionList = res.data
						this.version = JSON.parse(JSON.stringify(this.versionList[0]))
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
						
						this.versionList = res.data
						this.version = JSON.parse(JSON.stringify(this.versionList[0]))
						this.getDecCont()
					}
				})
			},
			copyVersion(tempVersion) {
				this.addVersionLoading = true
				this.getData.copyVersion({
					decisionTreeId: this.id,
					id: this.version.id,
					versionCode: tempVersion.versionCode,
					description: tempVersion.description
				}).then(res => {
					if (res.status == '1') {
						this.$message({
							message: '复制版本成功',
							type: 'success'
						});
						
						this.versionList = res.data
						this.version = JSON.parse(JSON.stringify(this.versionList[0]))
						this.getDecCont()

					}
					this.addVersionDialog = false
					this.addVersionLoading = false
				})
			},
			addVersionClose() {
				this.out = this.tempadd.out
				this.outcontent = this.tempadd.outcontent
				this.detailList = this.tempadd.detailList
				
			},
			addVersion() {
				this.tempadd = {
					out: this.out,
					outcontent: this.outcontent,
					detailList: this.detailList,
					
				}

				this.reset()
			},
			getDecCont() {
				this.loading = true
				this.getData.getInfo(this.version.id).then(res => {
					if (res.status == '1') {
						this.num++
						this.out = res.data.resultFieldEn
						this.outcontent = res.data.strategyOutputList
						this.detailList =  res.data.detailList
						
						this.Enformat(this.detailList,'Array')
						this.loading = false
					}
				})
			},
			reset() {
				this.out = ''
				this.outcontent = []
				this.detailList = [{
					variableType:1,
					resultValue:'',
					logical:'&&',
					fieldEn:[],
					fieldId:'',
					conditionList:[{
						variableType:1,
						operator:'',
						fieldValue:''
					}],
					children:[]
				}]
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
					this.$message.error('请填入决策树代码，并检查空格');
					return true
				}
				if (this.name.trim() == '') {
					this.$message.error('请填入决策树名称，并检查空格');
					return true
				}
				if (this.description.trim() == '') {
					this.$message.error('请填入决策树描述，并检查空格');
					return true
				}
				
				let is = {
					is: false,
					msg: '提交失败，请检查是否有未填项'
				}
				this.getIsempty(is, this.detailList)
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
				// if (!this.out) {
				// 	this.$message.error('请填入输出字段')
				// 	return true
				// }
			},
			getIsempty(is, obj) {
				obj.forEach(value => {

					value.conditionList.forEach(value => {
						if (!value.operator || (!value.fieldValue && value.fieldValue !== 0)) {
							is.is = true
							is.msg = "请检查是否有指标的条件未填写"
						}
					})
					if (!value.fieldId) {
						is.is = true
						is.msg = "请检查是否有节点无数据"
					}
					if (value.children.length > 0) {
						this.getIsempty(is, value.children)
						value.nodeType=1
					}else{
						value.nodeType=2
						if(value.resultValue===""){
							is.is = true
							is.msg = "请检查是否有最终节点无结果"
						}
					}
				})
			},
			submit() {
				console.log(this.detailList)
				
				this.loading = true
				if (this.examine()) {
					this.loading = false
					return
				}
				this.Enformat(this.detailList,'String')
				let obj = {
					"code": this.code.trim(),
					"name": this.name.trim(),
					"description": String(this.description).trim(),
					versionList: [{
						resultFieldEn: this.out,
						strategyOutputList: this.outcontent,
						detailList:this.detailList
					}]

				}
				if (this.id == 0) {
					obj.versionList[0].versionCode = 'V:0'
					obj.versionList[0].description = '初始版本'
					obj.folderId = this.nameId == 99999999 ? 0 : this.nameId,
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
					obj.folderId = this.parentId
					obj.versionList[0].id = this.version.id
					obj.versionList[0].decisionTreeId = this.id
					
					this.getData.updatafield(obj).then(res => {
						this.loading = false
						if (res.status === "1") {
							this.$message({
								message: '修改成功',
								type: 'success'
							});
							
						}
					}).catch(err => {
						this.loading = false
						this.$message.error('出现问题-_-');
					})
				}
			}
		},
		watch: {

		},
		computed: {

		}











	}
</script>

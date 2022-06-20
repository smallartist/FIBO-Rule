<template>
	<div>
		<div class="business_header">
			<el-button type="primary" @click="add">新增</el-button>
			<!-- <el-input v-model="search" placeholder="请输入内容" style="width: 200px;margin: 0 10px;"></el-input> -->
			<!-- <el-button icon="el-icon-search" circle></el-button> -->
		</div>

		<el-table :data="list" border style="width: 100%;margin-top: 20px;" v-loading="tableLoading">
			<el-table-column prop="businessName" label="业务类型" align="center">
			</el-table-column>
			<el-table-column prop="businessChildName" label="业务子类型" align="center">
			</el-table-column>
			<el-table-column prop="sendType" label="发送方式" align="center">
			</el-table-column>
			<el-table-column prop="eventType" label="事件类型" align="center">
			</el-table-column>
			<el-table-column prop="isUnsubscribe" label="是否取消订阅" align="center">
			</el-table-column>
			<el-table-column prop="isManualIntervention" label="是否需要人工干预" align="center">
			</el-table-column>

			<el-table-column prop="" label="操作" align="center">
				<template slot-scope="scope">

					<el-button icon="el-icon-setting" @click="setList(scope.row)" circle size="mini"></el-button>
					<el-button type="danger" icon="el-icon-delete" @click="deleteList(scope.row.id)" size="mini" circle>
					</el-button>
				</template>
			</el-table-column>

		</el-table>
		<div style="display: flex;justify-content: flex-end;padding-right: 20px;padding-top: 20px;">
			<el-pagination background :current-page.sync="pageNum" layout="prev, pager, next" :total="total"
				@current-change="getTable">
			</el-pagination>
		</div>



		<el-dialog title="" :visible.sync="dialogVisible" width="500px" @close="dialogClose">
			<div v-loading="dialogLoading">

				<autoInput v-if="dialogVisible" :rule="businessInputRule" :data="tempDialogData"></autoInput>

				<el-form class="formItenBox">
					<div class="formItem">
						<p class="formItemTitle">黑名单</p>
						<div class="formInput" style="display: flex;align-items: center;padding: 10px;border-bottom: 1px solid #ddd;">
							<div>
								<p v-for="value in ruleInfo.blacklist">
									{{value.ruleName}}:{{value.versionCode}}

								</p>
							</div>
							<el-button icon="el-icon-setting" style="margin-left: 20px;" circle
								@click="SetRuleInfo('blacklist')"></el-button>
						</div>
					</div>
					<div class="formItem">
						<p class="formItemTitle">拦截规则</p>
						<div class="formInput" style="display: flex;align-items: center;padding: 10px;">
							<div>
								<p v-for="value in ruleInfo.intercept">
									{{value.ruleName}}:{{value.versionCode}}
					
								</p>
							</div>
							<el-button icon="el-icon-setting" style="margin-left: 20px;" circle
								@click="SetRuleInfo('intercept')"></el-button>
						</div>
					</div>
					<div class="formItem">
						<p class="formItemTitle">阈值规则</p>
						<div class="formInput" style="display: flex;align-items: center;padding: 10px;border-bottom: 1px solid #ddd;">
							<div>
								<p v-for="value in ruleInfo.threshold">
									{{value.ruleName}}:{{value.versionCode}}

								</p>
							</div>
							<el-button icon="el-icon-setting" style="margin-left: 20px;" circle
								@click="SetRuleInfo('threshold')"></el-button>
						</div>
					</div>
					<div class="formItem">
						<p class="formItemTitle">互斥规则</p>
						<div class="formInput" style="display: flex;align-items: center;padding: 10px;border-bottom: 1px solid #ddd;">
							<div>
								<p v-for="value in ruleInfo.mutex">
									{{value.ruleName}}:{{value.versionCode}}

								</p>
							</div>
							<el-button icon="el-icon-setting" style="margin-left: 20px;" circle
								@click="SetRuleInfo('mutex')"></el-button>
						</div>
					</div>
					<div class="formItem">
						<p class="formItemTitle">合并规则</p>
						<div class="formInput" style="display: flex;align-items: center;padding: 10px;border-bottom: 1px solid #ddd;">
							<div>
								<p v-for="value in ruleInfo.merge">
									{{value.ruleName}}:{{value.versionCode}}

								</p>
							</div>
							<el-button icon="el-icon-setting" style="margin-left: 20px;" circle
								@click="SetRuleInfo('merge')"></el-button>
						</div>
					</div>
					


				</el-form>



				<div style="display: flex;justify-content: flex-end; padding: 10px;">
					<el-button @click="dialogVisible = false">取 消</el-button>
					<el-button type="primary" @click="dialogcallback">确 定</el-button>
				</div>

			</div>





			<multipleRule v-if="innerVisible" :visible.sync="innerVisible" :listOr="listOperation" @OK="multipleRuleOK"
				@close="multipleRuleclose" :tempRuleOr="tempMultipleRuleSelete" :folderList="treeList"> </multipleRule>


		</el-dialog>










	</div>

</template>

<script>
	import multipleRule from '@/components/common/multipleRule.vue'
	import {
		businessRuleRelGetList,
		businessRuleRelAdd,
		businessRuleRelDelete,
		businessRuleRelUpdate,
		getRulesListTree,
	} from '@/api/index.js'
	import autoInput from '@/components/common/autoInput.vue'
	import businessInputRule from '@/utils/businessJs/input.js'
	const tempDialogDataOr = {
		businessName: '',
		businessChildName: '',
		backlog: '',
		businessCode: '',
		businessChildCode: '',
		sendType: '',
		eventType: '',
		isUnsubscribe: '',
		isManualIntervention: '',

	}
	const ruleInfoOr = {
		blacklist: [],
		threshold: [],
		mutex: [],
		merge: [],
		intercept: []
	}
	export default {
		components: {
			autoInput,
			multipleRule
		},
		data() {
			return {
				search: '',
				list: [],
				pageNum: 1,
				total: 0,
				dialogVisible: false,
				dialogcallback: () => {},
				multipleRulecallback: () => {},
				tempDialogData: Object.assign({}, tempDialogDataOr),
				businessInputRule: [],
				ruleInfo: Object.assign({}, ruleInfoOr),
				innerVisible: false,
				tempMultipleRuleSelete: [],
				tableLoading: false,
				dialogLoading: false,
				treeList:[]
			}
		},
		created() {
			this.getTable()
			getRulesListTree({
				parentId: 0,
				treeType: "8",
				type: 1
			}).then(res => {
				if(res.status=='1'){
					this.deepGetTreeList(res.data[0].children) 
				}
				console.log(this.treeList)
			})
			this.businessInputRule = businessInputRule
			console.log(this.businessInputRule)
		},
		methods: {
			deepGetTreeList(arr){
				arr.forEach(value=>{
					this.treeList.push({id:value.id,name:value.name})
					if(value.children.length>0){
						this.deepGetTreeList(value.children)
					}
				})
			},
			deleteList(id) {
				this.$confirm('确定删除?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {

					businessRuleRelDelete({
						id: id
					}).then(res => {
						if (res.status == '1') {
							this.$message({
								type: 'success',
								message: '删除成功!'
							});
							this.getTable()
						}
					})


				})
			},
			getTable() {
				this.tableLoading = true
				businessRuleRelGetList({
					pageNo: this.pageNum,
					pageSize: 10
				}).then(res => {
					if (res.status == '1') {
						this.list = res.data.list
						this.total = res.data.total
						this.tableLoading = false
					}
				})
			},
			setList(obj) {
				this.dialogVisible = true
				this.tempDialogData = Object.assign({}, obj)
				this.ruleInfo = Object.assign({}, JSON.parse(this.tempDialogData.ruleInfo))
				console.log(this.ruleInfo)
				this.dialogcallback = () => {

					if (this.verify()) {
						return
					}
					let obj = JSON.parse(JSON.stringify(this.tempDialogData))
					obj.blacklist = this.ruleInfo.blacklist
					obj.threshold = this.ruleInfo.threshold
					obj.mutex = this.ruleInfo.mutex
					obj.merge = this.ruleInfo.merge
					obj.intercept = this.ruleInfo.intercept
					// console.log(obj,this.ruleInfo)
					this.dialogLoading = true
					businessRuleRelUpdate(obj).then(res => {
						if (res.status == 1) {
							this.getTable()
							this.$message.success('修改成功')
							this.dialogVisible = false
							this.dialogClose()
							this.dialogLoading = false
						}
					})
				}
			},
			add() {
				this.dialogVisible = true


				this.dialogcallback = () => {

					if (this.verify()) {
						return
					}
					let obj = JSON.parse(JSON.stringify(this.tempDialogData))
					obj.blacklist = this.ruleInfo.blacklist
					obj.threshold = this.ruleInfo.threshold
					obj.mutex = this.ruleInfo.mutex
					obj.merge = this.ruleInfo.merge
					obj.intercept = this.ruleInfo.intercept

					this.dialogLoading = true
					businessRuleRelAdd(obj).then(res => {
						if (res.status == 1) {
							this.getTable()
							this.$message.success('添加成功')
							this.dialogVisible = false
							this.dialogClose()
							this.dialogLoading = false
						}
					})

				}

			},
			verify() {
				let is = {
					is: false,
					mes: ''
				}
				console.log(this.tempDialogData)

				if (this.tempDialogData.businessName.trim() == '') {
					is.is = true
					is.msg = '请填写业务类型'
				}
				if (this.tempDialogData.businessCode.trim() == '') {
					is.is = true
					is.msg = '请填写业务类型编码'
				}
				if (this.tempDialogData.businessChildName.trim() == '') {
					is.is = true
					is.msg = '请填写业务子类型'
				}
				if (this.tempDialogData.businessChildCode.trim() == '') {
					is.is = true
					is.msg = '请填写业务子类型编码'
				}

				if (is.is) {
					this.$message.error(is.msg)
				}
				return is.is

			},
			dialogClose() {
				this.tempDialogData = Object.assign({}, tempDialogDataOr)
				this.ruleInfo = Object.assign({}, ruleInfoOr)
			},
			SetRuleInfo(str) {
				if (Array.isArray(this.ruleInfo[str])) {
					this.tempMultipleRuleSelete = this.ruleInfo[str].map(value => value.ruleId + '/' + value.versionId)
				}


				this.innerVisible = true

				this.multipleRulecallback = (res) => {
					this.ruleInfo[str] = res
				}

			},
			multipleRuleclose() {
				this.multipleRulecallback = () => {}
				this.tempMultipleRuleSelete = []
			},

			multipleRuleOK(arr) {
				this.innerVisible = false
				let arr2 = arr.map(value => {
					let obj = this.listOperation.find(x => x.id == Number(value.split('/')[0]))
					let version = obj.versionList.find(x => x.id == Number(value.split('/')[1]))

					if (obj) {
						return {
							versionId: version.id,
							versionCode: version.versionCode,
							ruleId: obj.id,
							ruleName: obj.name
						}
					} else {
						return {}
					}


				})


				this.multipleRulecallback(arr2)

			}

		},
		computed: {
			listOperation() {
				return this.$store.state.listOperation
			}
		}

	}
</script>

<style>
	.business_header {
		display: flex;
		align-items: center;
		padding-left: 20px;
		margin-top: 30px;
	}

	.textLayout {
		display: flex;
		font-size: 16px;
		height: 40px;
		padding-left: 30px;
		margin-top: 10px;
	}

	.textLayout>p {
		height: 100%;
		line-height: 30px;
	}

	.textLayout>p:nth-of-type(1) {
		width: 42%;
		text-align: right;
		margin-right: 20px;
		line-height: 40px;
	}
</style>

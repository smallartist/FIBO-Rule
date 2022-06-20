<template>
	<div>
		<div style="padding-top: 20px;">
			<el-button type="primary" @click="add">添加引擎</el-button>

		
		</div>

		<el-table :data="list" border style="width: 100%;margin-top: 20px;" v-loading="loading">
			<el-table-column prop="id" label="引擎ID" align="center">
				
			</el-table-column>
			<el-table-column prop="name" label="名称" align="center">
				<template slot-scope="scope">
					<el-button type="text" @click="go(scope.row.id)">{{scope.row.name}}</el-button>
				</template>
			</el-table-column>
			<el-table-column prop="description" label="描述" align="center">
				
			</el-table-column>
			
			
			<el-table-column prop="createDatetime" label="创建时间" align="center">
				<template slot-scope="scope">
					{{new Date(scope.row.createDatetime).format('yyyy-MM-dd hh:mm:ss')}}
				</template>
			</el-table-column>
			<el-table-column prop="" label="操作" align="center">
				<template slot-scope="scope">
					<div style="display: flex;justify-content: center;">

						<el-button type="primary" icon="el-icon-edit" circle size="mini" @click="update(scope.row)">
						</el-button>
						<el-button type="danger" icon="el-icon-delete" circle size="mini"
							@click="deleteMq(scope.row.id)"></el-button>
					</div>
				</template>
			</el-table-column>
		</el-table>

		<div style="display: flex;justify-content: flex-end;padding-top: 20px;padding-right: 20px;">
			<el-pagination background layout="prev, pager, next" :current-page.sync="pageNum" :total="total"
				@current-change="getList">
			</el-pagination>
		</div>





		<el-dialog title="添加/修改" :visible.sync="dialogVisible" width="30%" @close="close">
			<div v-loading="dialogLoading">
				<el-form ref="form" :model="form" label-width="120px" :rules="rules">
					<el-form-item label="引擎名称" prop="name">
						<el-input v-model="form.name"></el-input>
					</el-form-item>
					<el-form-item label="引擎描述" prop="description">
						<el-input v-model="form.description"></el-input>
					</el-form-item>
					
				</el-form>
				<div style="display: flex;justify-content: flex-end;">
					<el-button @click="dialogVisible = false">取 消</el-button>
					<el-button type="primary" @click="dialogCallBack">确 定</el-button>
				</div>
			</div>
		</el-dialog>

	</div>
</template>

<script>
	import {
		engineGetList,
		engineAdd,
		engineUpdate,
		engineUpdateStatus,
		engineGetEngineCode
	} from '@/api/index.js'

	const formOr = {
		id: '',
		name:'',
		description:'',
		engineType:'data_flow_engine'
	}
	export default {
		name:'dataFlowEngine',
		data() {
			return {
				list: [],
				pageNum: 1,
				total: 0,
				dialogVisible: false,
				loading: false,
				dialogLoading: false,
				typeList: [{
					label: 'kafka',
					value: 'kafka'
				}],
				
				rules: {
					name: [{
						required: true,
						message: '请输入名称',
						trigger: 'blur'
					}, ],
					description: [{
						required: true,
						message: '请输入描述',
						trigger: 'blur'
					} ],
					
				},
				form: JSON.parse(JSON.stringify(formOr)),
				dialogCallBack: () => {}
			}
		},
		created() {
			this.getList()
		},
		methods: {
			go(id){
				this.$router.push({name:'dataFlowEngineCont',params:{id:id}})
			},
			deleteMq(id) {
				this.$confirm('确定删除？', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'error'
				}).then(() => {
					engineUpdateStatus({
						ids: id,
						status: -1
					}).then(res => {
						if (res.status == '1') {
							this.getList()
						}
					})
				}).catch(() => {
					this.$message({
						type: 'info',
						message: '已取消删除'
					});
				});
			},
			close() {
				this.form = JSON.parse(JSON.stringify(formOr))
				this.dialogCallBack = () => {}
			},
			update(item) {
				
				this.form = {
					id: item.id,
					name: item.name,
					description: item.description,
				}
				this.dialogVisible = true

				this.dialogCallBack = () => {
					if (this.verify()) {
						return
					}
					this.dialogLoading = true
					engineUpdate(this.form).then(res => {
						if (res.status == '1') {
							this.dialogVisible = false
							this.getList()
						}
						this.dialogLoading = false
					})

				}


			},
			add() {
				this.dialogVisible = true
				
				this.dialogCallBack = () => {
					if (this.verify()) {
						return
					}
					delete this.form.id
					this.dialogLoading = true
					engineGetEngineCode().then(res=>{
						if(res.status==1){
							
							engineAdd({...this.form,code:res.data}).then(res => {
								if (res.status == '1') {
									this.dialogVisible = false
									this.getList()
								}
								this.dialogLoading = false
							})
						}
						this.dialogLoading = false
					})
					




				}
			},
			verify() {
				let is = {
					is: false,
					msg: ''
				}
				if (!this.form.name||this.form.name.trim() === '') {
					is.is = true
					is.msg = '请输入名称'
				}
				if (!this.form.description||this.form.description.trim() === '') {
					is.is = true
					is.msg = '请输入描述'
				}
			
				if (is.is) {
					this.$message.error(is.msg)
				}
				return is.is
			},
			getList() {
				this.loading = true
				engineGetList({
					pageNum: this.pageNum,
					pageSize: 10,
					entity:{
						engineType:'data_flow_engine'
					}
				}).then(res => {
					if (res.status == 1) {
						this.list = res.data.list
						this.total = res.data.total
					}
					this.loading = false
				})
			}
		}
	}
</script>

<style>
</style>



<!-- <template>
	<div>
		1
	</div>
</template>

<script>
	import {
		engineGetList
	} from '@/api/index.js'
	export default{
		created() {
			engineGetList({
				pageNum:1,
				pageSize:10,
				entity:{
					engineType:'data_flow_engine'
				}
			})
		}
	}
</script>

<style>
</style> -->

<template>
	<div class="content-wrapper">
		<div>
			<el-row :gutter="20">
				<!-- <el-col :span="16"> -->
				<div>
					<el-button @click="add()" type="primary">新增</el-button>
					<el-button @click="using(-1)" type="danger" :disabled="multipleSelection.length>0?false:'disabled'">删除</el-button>
					<el-button @click="using(1)" type="success" :disabled="multipleSelection.length>0?false:'disabled'">启用</el-button>
					<el-button @click="using(0)" type="warning" :disabled="multipleSelection.length>0?false:'disabled'">停用</el-button>
				</div>
				<!-- </el-col> --> 
			</el-row>
		</div>
		<div class="tab-wrapper">
			<div>
				<el-table border ref="multipleTable" :data="dataList" tooltip-effect="dark" style="width: 100%" @selection-change="handleSelectionChange">
					<el-table-column type="selection" width="55">
					</el-table-column>

					<el-table-column prop="id" width="200" label="ID">
					</el-table-column>

					<el-table-column prop="roleName" label="角色名" width="" show-overflow-tooltip>
						<!-- <template slot-scope="scope">
								{{scope.row.name}}
							</template> -->
					</el-table-column>

					<el-table-column prop="status" width="" label="状态">
						<template slot-scope="scope">
							{{scope.row.status===0?'禁用':'启用'}}
						</template>
					</el-table-column>


					<el-table-column label="操作" align="center" size="s">
						<template slot-scope="scope">
							<el-tooltip content="编辑" placement="left">
								<el-button icon="el-icon-edit" circle size="mini" @click="roleEdit(scope.row)"></el-button>
							</el-tooltip>
							<el-tooltip content="权限分配" placement="left">
								<el-button icon="el-icon-edit-outline" circle size="mini" @click="editDiaglo(scope.row)"></el-button>
							</el-tooltip>
							<el-tooltip content="删除" placement="right">
								<el-button icon="el-icon-delete" circle size="mini" @click="setStatus(-1,scope.row.id)"></el-button>
							</el-tooltip>
						</template>
					</el-table-column>
				</el-table>
				<el-pagination class="pagination-wrapper" background hide-on-single-page :current-page="pager.pageNum" :page-count="pager.lastPage"
				 layout="prev, pager, next" @current-change="surrentChange">
				</el-pagination>
			</div>
		</div>
		<!-- 权限分配 -->
		<authority-assignment-dialog :dialogVisible="dialogVisible" @closeEvent="handleClose" :dataItem="dataItem"
		 :listOrganArr="listOrganArr"></authority-assignment-dialog>
		<!-- 添加编辑角色 -->
		<el-dialog title="角色配置" :visible.sync="dialogFormVisible" width="30%" class="edit-password-dialog">
			<el-form :model="form" ref="changeRoleForm" label-width="100px" label-position="left">
				<el-form-item label="角色名" prop="roleName" :rules="[
                        { required: true, message: '不能为空！'}
                    ]">
					<el-input v-model="form.roleName" autocomplete="off"></el-input>
				</el-form-item>
				<el-form-item label="所属公司" prop="organId" :rules="[
                        { required: true, message: '请选择公司', trigger: 'change'}
                    ]">
					<el-select v-model="form.organId" placeholder="请选择所属公司" :disabled="listOrganArr.length<2?true:false">
						<el-option v-for="item in listOrganArr" :key="item.id" :label="item.name" :value="item.id"></el-option>
					</el-select>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="changeRole('changeRoleForm')">确 定</el-button>
				<el-button @click="cancel()">取 消</el-button>

			</div>
		</el-dialog>

	</div>
</template>
<script>
	import {
		getRoleList,
		roleUpdateStatus,
		getAllValidOrgan,
		saveRole,
		updateRole
	} from '@/api/index.js'
	import AuthorityAssignmentDialog from '../../models/authorityAssignmentDialog.vue'
	export default {
		name:'roleManagement',
		components: {
			AuthorityAssignmentDialog
		},
		data() {
			return {
				page: 1,
				pageSize: 10,
				pager: {},
				dataList: [],
				multipleSelection: [],
				dialogVisible: false,
				dataItem: {},
				listOrganArr: [],
				dialogFormVisible: false,
				form: {
					roleName: "",
					organId: ""
				},
				currItem: {},
				disabled: false
			};
		},
		created() {
			this.getlist();
			this.organList();
		},
		methods: {
			cancel() {
				this.currItem = {};
				this.dialogFormVisible = false;
				this.form = {
					roleName: "",
					organId: ""
				}
				this.$refs["changeRoleForm"].resetFields();
			},
			changeRole(myForm) {
				let form = this.form;
				this.$refs[myForm].validate((valid) => {
					if (valid) {
						if (JSON.stringify(this.currItem) == '{}') {
							this.saveRole(form);
						} else {
							form.id = this.currItem.id;
							this.updateRole(form);
						}
					} else {

					}
				});
			},
			roleEdit(e) {
				this.disabled = true;
				this.currItem = e;
				console.log(e);
				this.form = {
					roleName: e.roleName,
					organId: e.organId
				}
				this.dialogFormVisible = true;
				if (this.listOrganArr.length == 1) {
					this.form.organId = this.listOrganArr[0].id
				}
			},
			async updateRole(form) {
				const data = await updateRole(form)
				if (data.status != "0") {
					this.$message({
						message: '操作成功！',
						type: "success"
					});
					this.getlist();
					this.cancel();
				}
			},
			async saveRole(form) {
				const data = await saveRole(form)
				if (data.status != "0") {
					this.$message({
						message: '操作成功！',
						type: "success"
					});
					this.getlist();
					this.cancel();
				}
			},
			handleClose() {
				this.dataItem = {};
				this.dialogVisible = false;
			},
			// 添加
			add() {
				this.disabled = false;
				this.dataItem = {};
				this.dialogFormVisible = true;
				if (this.listOrganArr.length == 1) {
					this.form.organId = this.listOrganArr[0].id
				}
			},
			// 权限分配
			editDiaglo(e) {
				this.dataItem = e;
				this.dialogVisible = true;
			},
			setStatus(status, ids) {
				let msg = "确认修改用户状态？"
				if (status == -1) {
					msg = "此操作将永久删除该文件, 是否继续?"
				}
				this.$confirm(msg, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.updateStatus(status, ids + '');
				});
			},
			using(num) {
				let selArr = [];
				this.multipleSelection.forEach(value => {
					selArr.push(value.id);
				})
				if (selArr.length <= 0) {
					this.$message({
						message: '请选择！',
						type: "warning"
					});
					return
				}
				this.setStatus(num, selArr.join(','));
			},
			selectChanged(e) {
				this.page = 1;
				this.value = e;
				this.getlist();
			},
			handleSelectionChange(val) {
				this.multipleSelection = val;
			},
			surrentChange(e) {
				this.page = e;
				this.getlist();
			},
			async getlist() {
				const data = await getRoleList({
					pageNo: this.page,
					pageSize: this.pageSize
				})
				if (data.status != "0") {
					const listUser = data.data.listRole;
					const pager = data.data.pager;
					this.pager = pager;
					this.dataList = listUser
				}
			},
			async updateStatus(status, arrStr) {
				const data = await roleUpdateStatus({
					"ids": arrStr,
					"status": status
				})
				if (data.status != "0") {
					this.$message({
						message: '操作成功！',
						type: "success"
					});
					this.getlist();
				}
			},
			async organList() {
				const data = await getAllValidOrgan({})
				if (data.status != "0") {
					this.listOrganArr = data.data

				}
			}
		}
	};
</script>
<style>
	.tab-wrapper {
		padding: 21px 0;
	}

	.pagination-wrapper {
		margin-right: 40px;
		margin-top: 40px;
		text-align: right;
	}

	.edit-password-dialog .el-input {
		width: 70%;
	}
</style>

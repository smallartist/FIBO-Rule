<template>
	<div class="content-wrapper">
		<div>
			<el-row :gutter="20">
				<!-- <el-col :span="16"> -->
				<div>
					<el-button @click="add" type="primary">新增</el-button>
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

					<el-table-column prop="account" width="" label="账号">
					</el-table-column>

					<el-table-column prop="sysRole.roleName" label="角色" width="" show-overflow-tooltip>
						<!-- <template slot-scope="scope">
								{{scope.row.name}}
							</template> -->
					</el-table-column>

					<!-- <el-table-column
                        prop="queryField"
                        label="查询主键"
                        width="200">
                        </el-table-column> -->

					<el-table-column prop="employeeId" label="员工编号">
					</el-table-column>

					<el-table-column prop="sysOrgan.id" width="" label="组织ID">
					</el-table-column>

					<el-table-column prop="sysOrgan.name" width="" label="组织名称">
						<!-- <template slot-scope="scope">
								{{scope.row.status==1?'启用':scope.row.status==0?'停用':'删除'}}
							</template> -->
					</el-table-column>

					<el-table-column prop="status" width="" label="状态">
						<template slot-scope="scope">
							{{scope.row.status===0?'停用':scope.row.status==1?'启用':'删除'}}
						</template>
					</el-table-column>

					<el-table-column prop="birth" label="创建时间">
						<template slot-scope="scope">
							{{scope.row.birth|formatDate}}
						</template>
					</el-table-column>

					<el-table-column label="操作" align="center" size="s">
						<template slot-scope="scope">
							<el-tooltip content="编辑" placement="left">
								<el-button icon="el-icon-edit" circle size="mini" @click="editDiaglo(scope.row)"></el-button>
							</el-tooltip>
							<el-tooltip content="删除" placement="left">
								<el-button icon="el-icon-delete" circle size="mini" @click="setStatus(-1,scope.row.id)"></el-button>
							</el-tooltip>
							<el-tooltip content="修改密码" placement="right">
								<el-button icon="el-icon-edit-outline" circle size="mini" @click="showDialogChangePassword(scope.row.id)"></el-button>
							</el-tooltip>
						</template>
					</el-table-column>
				</el-table>
				<el-pagination class="pagination-wrapper" background hide-on-single-page :current-page="pager.pageNum" :page-count="pager.lastPage"
				 layout="prev, pager, next" @current-change="surrentChange">
				</el-pagination>
				<!-- :page-size="pager.size" -->
				<!-- :total="pager.total" -->
			</div>
		</div>
		<!-- 添加/编辑 -->
		<!-- <add-block-white :dialogVisible="dialogVisible" @closeEvent="handleClose" :dataItem="dataItem"></add-block-white> -->
		<add-user-dialog :dialogVisible="dialogVisible" @closeEvent="handleClose" :dataItem="dataItem" :listOrganArr="listOrganArr"></add-user-dialog>

		<!-- 修改密码 -->
		<el-dialog title="修改密码" :visible.sync="dialogFormVisible" width="30%" class="edit-password-dialog">
			<el-form :model="form" ref="changePasswordForm">
				<el-form-item label="新密码" label-width="100px" prop="password" :rules="[
                        { required: true, message: '不能为空！'}
                    ]">
					<el-input v-model="form.password" autocomplete="off" type="password"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="closeChangePasswordForm()">取 消</el-button>
				<el-button type="primary" @click="changePassword('changePasswordForm')">确 定</el-button>
			</div>
		</el-dialog>

	</div>
</template>
<script>
	import {
		formatDate
	} from '@/assets/utils.js'
	import {
		getUserList,
		userUpdateStatus,
		getAllValidOrgan,
		updateUserPassword
	} from '@/api/index.js'
	import AddUserDialog from '../../models/addUserDialog.vue'
	export default {
		name:'userManagement',
		components: {
			AddUserDialog
		},
		//过滤
		filters: {
			formatDate(time) {
				let date = new Date(time)
				return formatDate(date, 'yyyy-MM-dd')
			}
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
					password: ""
				},
				currId: ""
			};
		},
		created() {
			this.getlist();
			this.organList();
		},
		methods: {
			closeChangePasswordForm() {
				this.currId = "";
				this.dialogFormVisible = false;
				this.$refs["changePasswordForm"].resetFields();
			},
			changePassword(myForm) {
				this.$refs[myForm].validate((valid) => {
					if (valid) {
						this.$confirm("确认修改", '提示', {
							confirmButtonText: '确定',
							cancelButtonText: '取消',
							type: 'warning'
						}).then(() => {
							this.updateUserPassword(this.currId, this.form.password);
						});
					} else {

					}
				});
			},
			showDialogChangePassword(id) {
				this.currId = id;
				this.dialogFormVisible = true;
			},
			async updateUserPassword(id, password) {
				const data = await updateUserPassword({
					id: id,
					password: password
				})
				if (data.status === "0") {
					this.$message.error(data.msg);
					if (data.error === "01000103") {
						this.$router.push('/login')
					}
					return
				} else {
					this.$message({
						message: '修改成功！',
						type: "success"
					});
					this.closeChangePasswordForm();
				}
			},
			handleClose() {
				this.dataItem = {};
				this.dialogVisible = false;
				this.getlist();
			},
			// 添加
			add() {
				this.dialogVisible = true;
				// let e = "";
				// this.$router.push({path:'/blackWhiteManage/addBlackWihite/$'})
			},
			// 编辑
			editDiaglo(e) {
				this.dataItem = e;
				this.dialogVisible = true;
				// this.$router.push({path:`/blackWhiteManage/addBlackWihite/${e.id}`})
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
					this.updateStatus(status, ids);
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
			async getlist(e) {
				const data = await getUserList({
					pageNo: this.page,
					pageSize: this.pageSize
				})
				if (data.status === "0") {
					this.$message.error(data.msg);
					if (data.error === "01000103") {
						this.$router.push('/login')
					}
					return
				} else {
					const listUser = data.data.listUser;
					const pager = data.data.pager;
					this.pager = pager;
					this.dataList = listUser
				}
			},
			async updateStatus(status, arrStr) {
				const data = await userUpdateStatus({
					"ids": arrStr,
					"status": status
				})
				if (data.status === "0") {
					this.$message.error(data.msg);
					if (data.error === "01000103") {
						this.$router.push('/login')
					}
					return
				} else {
					this.$message({
						message: '操作成功！',
						type: "success"
					});
					this.getlist();
				}
			},
			async organList() {
				const data = await getAllValidOrgan({})
				if (data.status === "0") {
					this.$message.error(data.msg);
					if (data.error === "01000103") {
						this.$router.push('/login')
					}
					return
				} else {
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

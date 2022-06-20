<template>
	<div class="content-wrapper">
		<div>
			<el-row :gutter="20">
				<!-- <el-col :span="16"> -->
				<div >
					<el-button @click="add()" type="primary">新增</el-button>
					<el-button @click="using(-1)" type="danger">删除</el-button>
					<el-button @click="using(1)" type="success">启用</el-button>
					<el-button @click="using(0)" type="warning">停用</el-button>
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

					<el-table-column prop="name" label="公司名称" width="" show-overflow-tooltip>
						<!-- <template slot-scope="scope">
								{{scope.row.name}}
							</template> -->
					</el-table-column>

					<el-table-column prop="code" label="公司代号" width="" show-overflow-tooltip>
					</el-table-column>

					<el-table-column prop="status" width="" label="状态">
						<template slot-scope="scope">
							{{scope.row.status===0?'停用':'启用'}}
						</template>
					</el-table-column>

					<el-table-column prop="token" label="TOKEN" width="" show-overflow-tooltip>
					</el-table-column>

					<el-table-column label="操作" align="center" size="s">
						<template slot-scope="scope">
							<el-tooltip content="编辑" placement="left">
								<el-button icon="el-icon-edit" circle size="mini" @click="organEdit(scope.row)"></el-button>
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
		<!-- 添加编辑 -->
		<el-dialog title="配置账号" :visible.sync="dialogFormVisible" width="30%" class="edit-password-dialog">
			<el-form :model="form" ref="changeOrganForm" label-width="100px" label-position="left">
				<el-form-item label="名称" prop="name" :rules="[
                        { required: true, message: '不能为空！'}
                    ]">
					<el-input v-model="form.name" autocomplete="off"></el-input>
				</el-form-item>
				<el-form-item label="代号" prop="code" :rules="[
                        { required: true, message: '不能为空！'}
                    ]">
					<el-input v-model="form.code" autocomplete="off"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="changeOrgan('changeOrganForm')">确 定</el-button>
				<el-button @click="cancel()">取 消</el-button>

			</div>
		</el-dialog>

	</div>
</template>
<script>
	import {
		getOrganList,
		updateStatusOrgan,
		saveOrgan,
		updateOrgan
	} from '@/api/index.js'
	export default {
		name:'organizationManagement',
		data() {
			return {
				page: 1,
				pageSize: 10,
				pager: {},
				dataList: [],
				multipleSelection: [],
				dialogFormVisible: false,
				form: {
					name: "",
					code: ""
				},
				currItem: {}
			};
		},
		created() {
			this.getlist();
		},
		methods: {
			cancel() {
				this.currItem = {};
				this.dialogFormVisible = false;
				this.form = {
					name: "",
					code: ""
				}
				this.$refs["changeOrganForm"].resetFields();
			},
			changeOrgan(myForm) {
				let form = this.form;
				this.$refs[myForm].validate((valid) => {
					if (valid) {
						if (JSON.stringify(this.currItem) == '{}') {
							this.save(form);
						} else {
							form.id = this.currItem.id;
							this.update(form);
						}
					} else {

					}
				});
			},
			organEdit(e) {
				this.currItem = e;
				this.form = {
					name: e.name,
					code: e.code
				}
				this.dialogFormVisible = true;
			},
			async update(form) {
				const data = await updateOrgan(form)
				if (data.status != "0") {
					this.$message({
						message: '操作成功！',
						type: "success"
					});
					this.getlist();
					this.cancel();
				}
			},
			async save(form) {
				const data = await saveOrgan(form)
				if (data.status != "0") {
					this.$message({
						message: '操作成功！',
						type: "success"
					});
					this.getlist();
					this.cancel();
				}
			},
			// 添加
			add() {
				this.dialogFormVisible = true;
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
				const data = await getOrganList({
					pageNo: this.page,
					pageSize: this.pageSize
				})
				if (data.status != "0") {
					const listData = data.data.listOrgan;
					const pager = data.data.pager;
					this.pager = pager;
					this.dataList = listData
				}
			},
			async updateStatus(status, arrStr) {
				const data = await updateStatusOrgan({
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

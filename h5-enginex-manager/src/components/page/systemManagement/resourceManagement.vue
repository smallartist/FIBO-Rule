<template>
	<div class="content-wrapper">
		<div>
			<el-row :gutter="20">
				<div>
					<el-button @click="add()" type="primary">新增</el-button>
					<el-button @click="using(-1)" type="danger">删除</el-button>
				</div>
			</el-row>
		</div>
		<div class="tab-wrapper">

			<!-- <el-tabs v-model="tabs" @tab-click="pager.pageNum=1;getlist()">
				<el-tab-pane label="EngineX" name="EngineX"></el-tab-pane>
				<el-tab-pane label="DataX" name="DataX"></el-tab-pane>
			</el-tabs> -->

			<div v-loading="loading">
				<el-table border ref="multipleTable" :data="dataList" tooltip-effect="dark" style="width: 100%"
					@selection-change="handleSelectionChange">
					<el-table-column type="selection" width="55">
					</el-table-column>

					<el-table-column prop="code" width="200" label="资源编号">
					</el-table-column>

					<el-table-column prop="name" label="名称" width="" show-overflow-tooltip>
					</el-table-column>

					<el-table-column prop="url" label="路径" width="" show-overflow-tooltip>
					</el-table-column>

					<el-table-column prop="icon" label="图标" width="" show-overflow-tooltip>
					</el-table-column>

					<el-table-column prop="sort" label="排序" width="" show-overflow-tooltip>
					</el-table-column>

					<el-table-column prop="birth" label="创建时间" width="" show-overflow-tooltip>
						<template slot-scope="scope">
							{{scope.row.birth|formatDate}}
						</template>
					</el-table-column>

					<el-table-column label="操作" align="center" size="s">
						<template slot-scope="scope">
							<el-tooltip content="编辑" placement="left">
								<el-button icon="el-icon-edit" circle size="mini" @click="editDiaglo(scope.row)">
								</el-button>
							</el-tooltip>
							<el-tooltip content="删除" placement="right">
								<el-button icon="el-icon-delete" circle size="mini" @click="setStatus(-1,scope.row.id)">
								</el-button>
							</el-tooltip>
						</template>
					</el-table-column>
				</el-table>
				<el-pagination class="pagination-wrapper" background  :current-page="pager.pageNum"
					:total="pager.total" layout="prev, pager, next" @current-change="surrentChange">
				</el-pagination>
			</div>
		</div>
		<add-resource-dialog  :dialogVisible="dialogVisible" @closeEvent="handleClose" :dataItem="dataItem">
		</add-resource-dialog>
	</div>
</template>
<script>
	import {
		formatDate
	} from '@/assets/utils.js'
	import {
		getRsourceMenuList,
		resourceUpdateStatus
	} from '@/api/index.js'
	import AddResourceDialog from '../../models/addResourceDialog.vue'
	export default {
		name: 'resourceManagement',
		components: {
			AddResourceDialog
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
				dialogFormVisible: false,
				tabs:'EngineX',
				loading:false
			};
		},
		created() {
			this.getlist();
		},
		methods: {
			handleClose() {
				this.dataItem = {};
				this.dialogVisible = false;
				this.getlist();
			},
			// 添加
			add() {
				this.dataItem = {};
				this.dialogVisible = true;
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
			handleSelectionChange(val) {
				this.multipleSelection = val;
			},
			surrentChange(e) {
				this.page = e;
				this.getlist();
			},
			async getlist() {
				this.loading = true
				const data = await getRsourceMenuList({
					pageNo: this.page,
					pageSize: this.pageSize,
					entity:{
						resourceSystem:this.tabs
					}
				})
				if (data.status != "0") {
					const listUser = data.data.listMenu;
					const pager = data.data.pager;
					this.pager = pager;
					this.dataList = listUser
					this.loading = false
				}
			},
			async updateStatus(status, arrStr) {
				const data = await resourceUpdateStatus({
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

<template>
	<div style="height: 90%; ">
		<!-- 创建和搜索 -->
		<div class="datasou_top" style="height: 10%;">
			<el-row style="margin-bottom: 30px;margin-top: 20px;">
				<el-col :span="15">
					<el-button style="margin-right:20px;" type="primary" @click="createEngine">创建数据源</el-button>
					<el-select v-model="sourceType" placeholder="请选择数据源类型" @change="changeSource">
						<el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-col>
			</el-row>
		</div>

		<div class="datasou_cont" style="height: 80%;" v-if="data">
			<div class="datasou_table">
				<el-row>


					<el-table border :data="data.data.data" style="width: 100%" v-loading="loading">

						<el-table-column label="序号" align="center">
							<template slot-scope="scope">
								{{((pageNo-1)*10+scope.$index+1)}}
							</template>
						</el-table-column>
						<el-table-column prop="name" label="name" align="center" show-overflow-tooltip>
						</el-table-column>
						<el-table-column v-if="sourceType == 'Spark'" prop="sparkHome" label="park-home" align="center"
							show-overflow-tooltip>
						</el-table-column>
						<el-table-column v-if="sourceType == 'Spark'" prop="appName" label="app-name" align="center"
							show-overflow-tooltip>
						</el-table-column>
						<el-table-column v-if="sourceType == 'Spark'" prop="masterUrl" label="master" align="center"
							show-overflow-tooltip>
						</el-table-column>

						<el-table-column v-if="sourceType == 'Hive'" prop="url" label="url" align="center"
							show-overflow-tooltip>
						</el-table-column>

						<el-table-column v-if="sourceType != 'Hive'&&sourceType!='Spark'" prop="host" label="host"
							align="center" show-overflow-tooltip>
						</el-table-column>
						<el-table-column v-if="sourceType != 'Hive'&&sourceType!='Spark'" prop="port" label="port"
							align="center">
						</el-table-column>
						<el-table-column v-if="sourceType !='Hive'&&sourceType!='Spark'" prop="dbName" label="db-name"
							align="center" show-overflow-tooltip>
						</el-table-column>
						<el-table-column v-if="sourceType!='Spark'" prop="userName"
							label="user-name" align="center" show-overflow-tooltip>
						</el-table-column>

						<el-table-column prop="updataTime" label="最后更新时间" align="center">
							<template slot-scope="scope">
								<span style="white-space: nowrap;">{{
											new Date(scope.row.updateTime).toLocaleDateString().replace(/\//g, "-") + " " + new Date(scope.row.updateTime).toTimeString().substr(0, 8)
											}}</span>
							</template>
						</el-table-column>

						<el-table-column label="操作" align="center" size="s">
							<template slot-scope="scope">
								<div>
									<el-button icon="el-icon-search" circle size="mini"
										@click="dialogShow(scope.$index)"></el-button>
									<el-button type="primary" icon="el-icon-edit" circle size="mini"
										@click="modificationShow(scope.row.id)"></el-button>
									<el-button type="danger" icon="el-icon-delete" circle size="mini"
										@click="deletelistsor(scope.row.id)"></el-button>
								</div>
							</template>
						</el-table-column>

					</el-table>

				</el-row>

			</div>
			<div style="float: right;margin-right: 120px;margin-top: 40px;">
				<el-pagination :current-page="pageNo" @current-change="currpage" background layout="prev, pager, next"
					:total="data.data.pager.total" v-if="data!==null">
				</el-pagination>
			</div>
		</div>

		<el-dialog title="数据库详情" :visible.sync="dialogVisible" width="40%" :before-close="handleClose"
			class="DataSource_look">
			<div v-if="tempDialogData!==null">
				<div class="textLayout">
					<p>连接名称：</p>
					<p v-if="tempDialogData.name">{{tempDialogData.name}}</p>
				</div>

				<div class="textLayout" v-if="tempDialogData.type == 'Spark'">
					<p>spark-home：</p>
					<p v-if="tempDialogData.sparkHome">{{tempDialogData.sparkHome}}</p>
				</div>
				<div class="textLayout" v-if="tempDialogData.type == 'Spark'">
					<p>app-name：</p>
					<p v-if="tempDialogData.appName">{{tempDialogData.appName}}</p>
				</div>
				<div class="textLayout" v-if="tempDialogData.type == 'Spark'">
					<p>master：</p>
					<p v-if="tempDialogData.masterUrl">{{tempDialogData.masterUrl}}</p>
				</div>

				<div class="textLayout" v-if="tempDialogData.type == 'Hive'">
					<p>地址：</p>
					<p v-if="tempDialogData.url">{{tempDialogData.url}}</p>
				</div>

				<div class="textLayout" v-if="tempDialogData.type != 'Hive'&&tempDialogData.type!='Spark'">
					<p>主机地址：</p>
					<p v-if="tempDialogData.host">{{tempDialogData.host}}</p>
				</div>
				<div class="textLayout" v-if="tempDialogData.type != 'Hive'&&tempDialogData.type!='Spark'">
					<p>端口：</p>
					<p v-if="tempDialogData.port">{{tempDialogData.port}}</p>
				</div>

				<div class="textLayout" v-if="tempDialogData.type !='Hive'&&tempDialogData.type!='Spark'">
					<p>数据库名称：</p>
					<p v-if="tempDialogData.dbName">{{tempDialogData.dbName}}</p>
				</div>

				<div class="textLayout" v-if="tempDialogData.type!='Spark'">
					<p>用户名：</p>
					<p v-if="tempDialogData.userName">{{tempDialogData.userName}}</p>
				</div>

				<div class="textLayout">
					<p>创建时间：</p>
					<p v-if="tempDialogData.createTime">
						{{new Date(tempDialogData.updateTime).toLocaleDateString().replace(/\//g, "-") + " " + new Date(tempDialogData.updateTime).toTimeString().substr(0, 8)}}
					</p>
				</div>
				<div class="textLayout">
					<p>创建人：</p>
					<p v-if="tempDialogData.creator">{{tempDialogData.creatorName}}</p>
				</div>
				<div class="textLayout">
					<p>修改时间：</p>
					<p v-if="tempDialogData.updateTime">
						{{new Date(tempDialogData.updateTime).toLocaleDateString().replace(/\//g, "-") + " " + new Date(tempDialogData.updateTime).toTimeString().substr(0, 8)}}
					</p>
				</div>
				<div class="textLayout">
					<p>修改人：</p>
					<p v-if="tempDialogData.modifier">{{tempDialogData.modifierName}}</p>
				</div>


				<div class="textLayout">
					<p>数据库类型：</p>
					<p v-if="tempDialogData.type">{{tempDialogData.type}}</p>
				</div>
			</div>
			<div v-else>
				数据错误 请重新尝试
			</div>
			<span slot="footer" class="dialog-footer">

				<el-button type="primary" @click="dialogVisible = false;tempDialogData=null">确 定</el-button>
			</span>

		</el-dialog>
		<el-dialog :title="modificationId===null?'创建'+sourceType+'数据源':'修改'+sourceType+'数据源'"
			:visible.sync="modificationVisible" width="35%" :before-close="handleClose">
			<div>
				<div class="textLayout">
					<p>name：</p>
					<p>
						<el-input placeholder="名称" v-model="modificationData.name" clearable maxlength="20">
						</el-input>
					</p>
				</div>

				<div class="textLayout" v-if="sourceType == 'Spark'">
					<p>spark-home：</p>
					<p>
						<el-input placeholder="如：/opt/soft/spark-2.4.6-bin-hadoop2.7"
							v-model="modificationData.sparkHome" clearable>
						</el-input>
					</p>
				</div>
				<div class="textLayout" v-if="sourceType == 'Spark'">
					<p>app-name：</p>
					<p>
						<el-input placeholder="如：sparkTest" v-model="modificationData.appName" clearable>
						</el-input>
					</p>
				</div>
				<div class="textLayout" v-if="sourceType == 'Spark'">
					<p>master：</p>
					<p>
						<el-input placeholder="如：local[4]" v-model="modificationData.masterUrl" clearable>
						</el-input>
					</p>
				</div>


				<div class="textLayout" v-if="sourceType != 'Hive'&&sourceType!='Spark'">
					<p>host：</p>
					<p>
						<el-input placeholder="如:192.168.1.1" v-model="modificationData.host" clearable maxlength="20">
						</el-input>
					</p>
				</div>

				<div class="textLayout" v-if="sourceType == 'Hive'">
					<p>url：</p>
					<p>
						<el-input placeholder="请输入地址" v-model="modificationData.url" clearable maxlength="50">
						</el-input>
					</p>
				</div>

				<div class="textLayout" v-if="sourceType != 'Hive'&&sourceType!='Spark'">
					<p>port：</p>
					<p>
						<el-input :placeholder="sourceType=='Redis'?'如：6379':'如：3306'" v-model="modificationData.port"
							clearable maxlength="20">
						</el-input>
					</p>
				</div>
				<div class="textLayout" v-if="sourceType !='Hive'&&sourceType!='Spark'">
					<p>db：</p>
					<p v-if="sourceType =='MySQL'||sourceType =='Oracle'||sourceType =='Sqlserver'">
						<el-input placeholder="请输入数据库名称" v-model="modificationData.dbName" clearable maxlength="50">
						</el-input>
					</p>
					<p v-if="sourceType =='Redis'">
						<el-input placeholder="0~15" type="number" v-model="modificationData.dbName" clearable>
						</el-input>
					</p>
				</div>
				<div class="textLayout" v-if="sourceType!='Spark'">
					<p>user-name：</p>
					<p>
						<el-input placeholder="请输入用户名" v-model="modificationData.userName" clearable maxlength="50">
						</el-input>
					</p>
				</div>
				<div class="textLayout" v-if="sourceType!='Spark'">
					<p>password：</p>
					<p>
						<el-input placeholder="请输入密码" v-model="modificationData.password" clearable maxlength="50"
							auto-complete="new-password" show-password>
						</el-input>
					</p>
				</div>
			</div>
			<span slot="footer" class="dialog-footer">
				<el-button @click="handleClose">取 消</el-button>
				<el-button type="primary" @click="modificationSure">确 定</el-button>
			</span>

		</el-dialog>

	</div>
</template>

<script>
	import {
		getDataSourcelist
	} from '../../api/index';
	import {
		setDataSource
	} from '../../api/index';
	import {
		deleteDataSource,
		updataDataSource
	} from '../../api/index';
	export default {
		name: 'datasource',
		data() {
			return {
				loading: false,
				searchString: "",
				data: null,
				currentPage: null,
				dialogVisible: false,
				tempDialogData: null,
				pageNo: 1,
				modificationId: null,
				modificationVisible: false,
				modificationData: {
					name: "",
					host: "",
					url: "",
					port: "",
					dbName: "",
					userName: "",
					password: "",
					sparkHome: "",
					appName: "",
					masterUrl: ""
				},
				options: [{
					value: 'MySQL',
					label: 'MySQL'
				}, {
					value: 'Redis',
					label: 'Redis'
				}, {
					value: 'Hive',
					label: 'Hive'
				}, {
					value: 'Spark',
					label: 'Spark'
				}, {
					value: 'Oracle',
					label: 'Oracle'
				}, {
					value: 'Sqlserver',
					label: 'Sqlserver'
				}],
				sourceType: 'MySQL'
			}
		},
		created() {
			this.initData();
		},
		methods: {
			initData() {
				let params = {
					pageNo: this.pageNo,
					pageSize: 10,
					typeList: [this.sourceType]
				}
				this.getDataSourcelists(params)
			},
			changeSource(e) {
				this.initData();
			},
			// 关闭弹窗所有弹窗 且清除临时数据
			handleClose() {
				this.dialogVisible = false
				this.modificationVisible = false

				this.tempDialogData = null
				this.modificationData = {
					host: "",
					userName: "",
					dbName: "",
					name: "",
					password: "",
					port: "",
				}

				this.modificationId = null
			},
			// 查看  弹窗显示
			dialogShow(index) {
				this.tempDialogData = this.data.data.data[index]
				this.dialogVisible = true
			},
			// 修改  弹窗显示
			modificationShow(id) {
				this.modificationId = id
				this.data.data.data.forEach(value => {
					if (value.id === id) {
						this.modificationData = {
							...value
						}
					}
				})

				this.modificationVisible = true
			},
			checkMySql() {
				if (this.modificationData.name.indexOf(' ') !== -1 || String(this.modificationData.host).indexOf(' ') !== -
					1 ||
					this.modificationData.userName.indexOf(' ') !== -1 || this.modificationData.password.indexOf(' ') !== -
					1 || this.modificationData
					.port.indexOf(' ') !== -1 || this.modificationData.dbName.indexOf(' ') !== -1) {
					this.$message.error('不允许出现空格！')
					return false;
				} else {
					if (this.modificationData.name.length < 4 || this.modificationData.host.length < 4 || this
						.modificationData.userName
						.length < 4 || this.modificationData.password.length < 4 || this.modificationData.port.length <
						4 || this.modificationData
						.dbName.length < 4) {
						this.$message.error('均为必填项且长度不小于4！')
						return false;
					} else {
						if (this.isChinese(this.modificationData.host) || this.isChinese(this.modificationData.userName) ||
							this.isChinese(this.modificationData.port)) {
							this.$message.error('主机地址，端口，用户名 不允许出现中文')
							return
						}

						let params = {}
						params.name = this.modificationData.name
						params.host = this.modificationData.host
						params.userName = this.modificationData.userName
						params.password = this.modificationData.password
						params.port = this.modificationData.port
						params.dbName = this.modificationData.dbName
						return params;
					}
				}
			},
			checkRedis() {
				if (this.modificationData.name.indexOf(' ') !== -1 || String(this.modificationData.host).indexOf(' ') !== -
					1 ||
					this.modificationData.password.indexOf(' ') !== -1 || this.modificationData
					.port.indexOf(' ') !== -1 || this.modificationData.dbName.indexOf(' ') !== -1) {
					this.$message.error('不允许出现空格！')
					return false;
				} else {
					if (this.modificationData.name.length < 4 || this.modificationData.host.length < 4 || this
						.modificationData.password.length < 4 || this.modificationData.port.length < 4) {
						this.$message.error('均为必填项且长度不小于4！')
						return false;
					} else {
						if (this.isChinese(this.modificationData.host) || this.isChinese(this.modificationData.port)) {
							this.$message.error('主机地址，端口 不允许出现中文')
							return
						}
						if (this.modificationData.dbName < 0 || this.modificationData.dbName > 15) {
							this.$message.error('db超出有效范围！')
							return
						}
						console.log(this.modificationData.dbName);
						let params = {}
						params.name = this.modificationData.name
						params.host = this.modificationData.host
						params.userName = this.modificationData.userName
						params.password = this.modificationData.password
						params.port = this.modificationData.port
						params.dbName = this.modificationData.dbName ? this.modificationData.dbName : 0;
						return params;
					}
				}
			},
			checkHive() {
			
				if (this.modificationData.name.indexOf(' ') !== -1 || this.modificationData.userName.indexOf(' ') !== -1 ||
					this.modificationData.password.indexOf(' ') !== -1 || this.modificationData
					.url.indexOf(' ') !== -1) {
					this.$message.error('不允许出现空格！')
					return false;
				} else {
					if (this.modificationData.name.length < 4 || this.modificationData.url.length < 4 || this
						.modificationData.password.length < 4 || this.modificationData.userName.length < 4) {
						this.$message.error('均为必填项且长度不小于4！')
						return false;
					} else {
						if (this.isChinese(this.modificationData.url) || this.isChinese(this.modificationData.userName)) {
							this.$message.error('连接地址，用户名 不允许出现中文')
							return false
						}

						let params = {}
						params.name = this.modificationData.name
						params.url = this.modificationData.url
						params.userName = this.modificationData.userName
						params.password = this.modificationData.password
						return params;
					}
				}
			},
			checkSpark() {
				if (this.modificationData.name.indexOf(' ') !== -1 || this.modificationData.sparkHome.indexOf(' ') !== -
					1 ||
					this.modificationData.appName.indexOf(' ') !== -1 || this.modificationData.masterUrl.indexOf(' ') !== -
					1) {
					this.$message.error('不允许出现空格！')
					return false;
				} else {
					if (this.modificationData.name.length < 4 || this.modificationData.sparkHome.length < 4 || this
						.modificationData.appName.length < 4 || this.modificationData.masterUrl.length < 4) {
						this.$message.error('均为必填项且长度不小于4！')
						return false;
					} else {
						if (this.isChinese(this.modificationData.sparkHome) || this.isChinese(this.modificationData
								.appName) || this.isChinese(this.modificationData.masterUrl)) {
							this.$message.error('spark-home，app-name，master 不允许出现中文')
							return false
						}

						let params = {}
						params.name = this.modificationData.name
						params.sparkHome = this.modificationData.sparkHome
						params.appName = this.modificationData.appName
						params.masterUrl = this.modificationData.masterUrl
						return params;
					}
				}
			},
			// 确定修改或者添加
			modificationSure() {
				let params = null;
				switch (this.sourceType) {
					case 'MySQL':
						params = this.checkMySql();
						break;
					case 'Redis':
						params = this.checkRedis();
						break;
					case 'Hive':
						params = this.checkHive();
						break;
					case 'Spark':
						params = this.checkSpark();
						break;
					case 'Oracle':
						params = this.checkMySql();
						break;
					case 'Sqlserver':
						params = this.checkMySql();
						break;
					default:

						break;
				}
				if (params) {
					params.type = this.sourceType;
					if (this.modificationId !== null) {
						params.id = this.modificationData.id
						this.updataDataSource(params).then(res => {
							if (res.status === "1") {
								this.data.data.data.forEach((value, index) => {
									if (value.id === this.modificationData.id) {
										console.log(value.id, this.modificationData.id)
										this.$set(this.data.data.data, index, {
											...this.modificationData
										})
									}
								})
								this.$message({
									message: '修改成功',
									type: 'success'
								});
								this.handleClose()
							}
						})
					} else {
						params.id = ""
						this.setDataSources(params).then(res => {
							if (res.status === "1") {
								// 创建成功方法
								this.initData();
								// this.pageNo = 1
								// this.getDataSourcelists({
								// 	pageNo: this.pageNo,
								// 	pageSize: 10
								// })
								this.$message({
									message: '创建成功',
									type: 'success'
								});
								this.handleClose()
							}
						})
					}
				}


				// 	}

				// }
				// console.log(this.modificationData.name.indexOf(' '))
			},
			// 新增数据源
			async setDataSources(params) {
				this.loading = true
				const data = await setDataSource(params)
				this.loading = false
				return data
			},
			// 修改数据源信息
			async updataDataSource(params) {
				this.loading = true
				const data = await updataDataSource(params)
				this.loading = false;
				return data
			},
			createEngine() {
				this.modificationVisible = true
			},
			searchEngine() {},
			async getDataSourcelists(params) {
				this.loading = true
				const data = await getDataSourcelist(params)
				if (data.status === "1") {
					this.data = data
					this.pageSize = data.data.pager.pages
				} else {
					this.$message.error('访问出错了-_-');
				}
				this.loading = false
			},
			deletelistsor(id) {

				this.$confirm('确定删除？', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'error'
				}).then(() => {
					this.deletelists(id)
				}).catch(() => {
					this.$message({
						type: 'info',
						message: '已取消删除'
					});
				});
			},
			deletelists(id) {
				this.deletelist(id).then(res => {
					if (res.status === "1") {
						this.initData();
						// this.getDataSourcelists({
						// 	pageNo: this.pageNo,
						// 	pageSize: 10
						// })
						this.$message({
							message: '删除成功',
							type: 'success'
						});
					}
				})
			},
			async deletelist(id) {
				this.loading = true
				return await deleteDataSource(id)
				this.loading = false
			},
			currpage(num) {
				this.pageNo = num
				this.getDataSourcelists({
					pageNo: this.pageNo,
					pageSize: 10,
					typeList:[this.sourceType]
				})
				console.log(this.pageNo)
			},
			isChinese(temp) {
				// var re = /[\u4E00-\u9FA5]|[\uFE30-\uFFA0]/gi;
				// if (re.test(temp)) {
				// 	console.log(temp)
				// 	return false
				// };
				// return true;
				if (escape(temp).indexOf("%u") < 0) {
					return false
				} else {
					return true;
				}

			}
		}




	}
</script>

<style>
	.el-pagination.is-background .btn-next,
	.el-pagination.is-background .btn-prev,
	.el-pagination.is-background .el-pager li {
		background-color: #fff !important;
	}

	.el-pagination.is-background .el-pager li:not(.disabled).active {
		background-color: #409EFF !important;
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

	.DataSource_look .textLayout>p:nth-of-type(2) {
		border-radius: 5px;
		line-height: 40px;
		box-sizing: border-box;
	}

	.el-dialog {
		border-radius: 20px !important;
	}
</style>

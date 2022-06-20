<template>
	<div style="height: 90%; " class="portS">
		<!-- 创建和搜索 -->
		<div class="datasou_top" style="height: 10%;">
			<el-row style="margin-bottom: 30px;margin-top: 20px;">
				<el-col :span="15">
					<el-button type="primary" @click="createEngine">创建接口</el-button>
				</el-col>

			</el-row>
		</div>


		<div class="datasou_cont" style="height: 80%;" v-if="data">
			<div class="datasou_table">
				<el-row>


					<el-table border :data="data.data.klist" style="width: 100%" :cell-style="{padding: '10px'}"
						v-loading="loading">

						<el-table-column prop="id" label="ID" align="center" show-overflow-tooltip>
						</el-table-column>
						<el-table-column prop="name" label="连接名称" align="center" show-overflow-tooltip>
						</el-table-column>
						<el-table-column prop="url" label="连接地址" align="center" show-overflow-tooltip>
						</el-table-column>
						<el-table-column prop="method" label="请求方法" align="center" show-overflow-tooltip>
						</el-table-column>
						<el-table-column prop="requestBody" label="请求体类型" align="center" show-overflow-tooltip>
						</el-table-column>
						<el-table-column prop="" label="调用量" align="center" show-overflow-tooltip>
							{{1}}
						</el-table-column>
						<el-table-column prop="creatorName" label="创建人" align="center" show-overflow-tooltip>
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
								<div style="display: flex;">
									<el-button icon="el-icon-search" circle size="mini"
										@click="modificationShow(scope.row.id,true)"></el-button>
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
				<el-pagination :current-page="pageNum" @current-change="currpage" background layout="prev, pager, next"
					:total="data.data.pageInfo.total" v-if="data!==null">
				</el-pagination>
			</div>
		</div>
<!-- 
		<el-dialog title="接口详情" :visible.sync="dialogVisible" width="40%" :before-close="handleClose"
			class="DataSource_look">
			<div v-if="tempDialogData!==null">
				<div class="port_textLayout">
					<p>连接名称：</p>
					<p v-if="tempDialogData.name">{{tempDialogData.name}}</p>
				</div>
				<div class="port_textLayout">
					<p>连接地址：</p>
					<p v-if="tempDialogData.url">{{tempDialogData.url}}</p>
				</div>
				<div class="port_textLayout">
					<p>请求方式：</p>
					<p v-if="tempDialogData.method">{{tempDialogData.method}}</p>
				</div>
				<div class="port_textLayout" >
					<p>请求头：</p>
					<div v-if="tempDialogData.requestHeaders" >
						<p
							v-for="value in typeof tempDialogData.requestHeaders == 'string'?JSON.parse(tempDialogData.requestHeaders):tempDialogData.requestHeaders">
							{{value.key}}:{{value.value}}
						</p>
					</div>
				</div>
				
				<div class="port_textLayout" >
					<p>请求入参：</p>
					<div v-if="tempDialogData.bindParam" >
						<p>变量:</p>
						<p v-for="value in JSON.parse(tempDialogData.bindParam).variable">
							{{value.key}}  :  {{value.value}}
							
						</p>
						
						
						<p>字典变量:</p>
						<p v-for="value in JSON.parse(tempDialogData.bindParam).dictVariable">
							{{value.key}}  : {{value.type}} :{{value.value}}
							
						</p>
						
					</div>
				</div>
				
				
				<div class="port_textLayout" style="margin-top: 20px;">
					<p>请求体：</p>
					<el-input type="textarea" rows="8" placeholder="格式:{page:1}   变量格式:name:{name}"
						v-model="tempDialogData.requestBody" disabled style="width: 400px;">
					</el-input>
				</div>
				<div class="port_textLayout">
					<p>响应体：</p>
					<el-input type="textarea" rows="8" v-model="tempDialogData.responseBody" disabled
						style="width: 400px;">
					</el-input>
				</div>
				<div class="port_textLayout">
					<p>创建时间：</p>
					<p v-if="tempDialogData.createTime">
						{{new Date(tempDialogData.updateTime).toLocaleDateString().replace(/\//g, "-") + " " + new Date(tempDialogData.updateTime).toTimeString().substr(0, 8)}}
					</p>
				</div>
				<div class="port_textLayout">
					<p>创建人：</p>
					<p v-if="tempDialogData.creatorName">{{tempDialogData.creatorName}}</p>
				</div>
				<div class="port_textLayout">
					<p>修改时间：</p>
					<p v-if="tempDialogData.updateTime">
						{{new Date(tempDialogData.updateTime).toLocaleDateString().replace(/\//g, "-") + " " + new Date(tempDialogData.updateTime).toTimeString().substr(0, 8)}}
					</p>
				</div>
				<div class="port_textLayout">
					<p>修改人：</p>
					<p v-if="tempDialogData.modifierName">{{tempDialogData.modifierName}}</p>
				</div>
			</div>
			<div v-else>
				数据错误 请重新尝试
			</div>
			<span slot="footer" class="dialog-footer">
				<el-button type="primary" @click="dialogVisible = false;tempDialogData=null">确 定</el-button>
			</span>

		</el-dialog> -->
		<el-dialog :title="show?'查看详情':modificationId===null?'创建接口':'修改接口'" :visible.sync="modificationVisible" width="60%"
			class="portS_dialog" :before-close="handleClose">
			<div v-loading="dialogLoading">
				<div class="port_textLayout">
					<p><span style="color: red;">*</span>接口名称：</p>
					<p>
						<el-input placeholder="请输入连接名称" v-model="modificationData.name" clearable maxlength="20" :disabled="show">
						</el-input>
					</p>
				</div>

				<div class="port_textLayout">
					<p><span style="color: red;">*</span>请求地址：</p>
					<p>
						<el-input placeholder="如:192.168.1.1" v-model="modificationData.url" clearable maxlength="100" :disabled="show">
						</el-input>
					</p>
				</div>
				<div class="port_textLayout">
					<p><span style="color: red;">*</span>请求方法：</p>
					<p>
						<el-select v-model="modificationData.method" placeholder="请选择请求方法" :disabled="show">
							<el-option label="Get" value="GET"></el-option>
							<el-option label="Post" value="POST"></el-option>
						</el-select>
					</p>
				</div>

				<div class="port_textLayout">
					<p>请求头参数：</p>
					<div>
						<p style="display: flex;align-items: center;margin-top: 5px;">
							<el-input placeholder="key" v-model="modificationData.requestHeaders[0].key" disabled
								clearable size="mini" :disabled="show"></el-input>
							<span>:</span>
							<el-select v-model="modificationData.requestHeaders[0].value" size="mini" :disabled="show"
								placeholder="请选择请求方法">
								<el-option label="/json" value="application/json"></el-option>
								<el-option label="/x-www-form-urlencoded" value="application/x-www-form-urlencoded">
								</el-option>
							</el-select>
							<i class="el-icon-circle-plus-outline" style="color: #409EFF;font-size: 20px;"
								 v-if="!show" @click="addheader(0)"></i>
							<i class="el-icon-circle-close" style="color: #ddd;font-size: 20px;"  v-if="!show"></i>
						</p>

						<p v-for="(value,index) in modificationData.requestHeaders"
							style="display: flex;align-items: center;margin-top: 5px;" v-if="index!=0">
							<el-input placeholder="key" v-model="value.key" clearable size="mini" :disabled="show"></el-input>
							<span>:</span>
							<el-input placeholder="value" v-model="value.value" clearable size="mini" :disabled="show"></el-input>
							<i class="el-icon-circle-plus-outline" style="color: #409EFF;font-size: 20px;"
								@click="addheader(index)"  v-if="!show"></i>
							<i class="el-icon-circle-close" style="color: #F56C6C;font-size: 20px;"
								@click="delectheader(index)" v-if="!show"></i>
						</p>
					</div>
				</div>
				
				
				<div  class="port_textLayout">
					<p>请求入参：</p>
					<bindParam v-if="modificationVisible" :Variable="modificationData.bindParam.variable" :disabled="show"  :dictVariable="modificationData.bindParam.dictVariable" size="mini"></bindParam>
				</div>
				
				
				<div class="port_textLayout">
					<p>请求体：</p>
					<p>
						<el-input type="textarea" rows="12" placeholder="格式:{page:1}   变量格式:name:{name}"
							v-model="modificationData.requestBody" style="width: 400px;" :disabled="show">
						</el-input>
					</p>
				</div>
				<div class="port_textLayout">
					<p>测试请求：</p>
					<p>
						<el-button plain @click="getHttpResponses" :disabled="show">请求测试</el-button>
						<el-button plain @click="lookBig">大屏查看请求响应体</el-button>
					</p>
				</div>
				<div class="port_textLayout">
					<p>响应体：</p>
					<p>
						<el-input type="textarea" :disabled="show" placeholder="点击请求测试获取此值" v-model="modificationData.responseBody"
							style="width: 400px;">
						</el-input>
					</p>
				</div>
			</div>
			<span slot="footer" class="dialog-footer">
				<el-button @click="handleClose" :disabled="dialogLoading?'disabled':false">取 消</el-button>
				<el-button type="primary" :disabled="dialogLoading?'disabled':false" @click="modificationSure">确 定
				</el-button>
			</span>
			<el-dialog width="70%" title="数据" :visible.sync="dialogVisibleBigData" append-to-body :close-on-click-modal="false">
				<span>
					ps:在此处也可以修改数据
				</span>
				<div style="display: flex;justify-content: space-between;width: 100%;">
					
					<div style="width: 50%;">
						<p>请求体：</p>
						<p>
							<el-input type="textarea" rows="26" placeholder="格式:{page:1}   变量格式:name:{name}" :disabled="show"
								v-model="modificationData.requestBody" >
							</el-input>
						</p>
					</div>
					<div style="width: 50%;">
						<p>响应体：</p>
						<p>
							<el-input type="textarea" rows="26" :disabled="show" placeholder="点击请求测试获取此值" v-model="modificationData.responseBody"
								>
							</el-input>
						</p>
					</div>
				</div>
				
				<span slot="footer" class="dialog-footer">
				
					<el-button type="primary"  @click="dialogVisibleBigData=false">确 定
					</el-button>
				</span>
			</el-dialog>
		</el-dialog>






	</div>
</template>

<script>
	import {
		getInterfaceList,
		addInterface,
		updateInterface,
		deleteInterface,
		getHttpResponse
	} from '../../api/index';
	import bindParam from '@/components/common/bindParam.vue'
	const bindParamOr = {
					variable:[],
					dictVariable:[],
				}
	export default {
		name: "portSource",
		components:{bindParam},
		data() {
			return {
				dialogLoading: false,
				loading: false,
				searchString: "",
				data: null,
				currentPage: null,
				dialogVisible: false,
				tempDialogData: null,
				pageNum: 1,
				modificationId: null,
				modificationVisible: false,
				show:false,
				modificationData: {
					name: "",
					url: '',
					method: '',
					requestBody: '',
					requestHeaders: [{
						key: 'Content-Type',
						value: 'application/json'
					}],
					responseBody: '',
					bindParam:{
						variable:[],
						dictVariable:[],
					},
					// bindParam:null
				},
				dialogVisibleBigData: false,
				
				
			}
		},
		created() {
			let params = {
				pageNum: this.pageNum,
				pageSize: 10,
				"interfaceInfo": {}
			}
			this.getInterfaceLists(params)
		},
		methods: {
			lookBig() {
				this.dialogVisibleBigData = true
			},
			random() {
				return Math.floor(Math.random() * 5000)
			},
			addheader(index) {
				this.modificationData.requestHeaders.splice(index + 1, 0, {
					key: '',
					value: ''
				})
			},
			delectheader(index) {
				this.modificationData.requestHeaders.splice(index, 1)
			},
			// 测试连接
			getHttpResponses() {

				let subobj = {}
				this.modificationData.requestHeaders.forEach(value => {
					subobj[value.key] = value.value
				})

				let Params = {
					url: this.modificationData.url,
					method: this.modificationData.method,
					requestHeaders: JSON.stringify(subobj),
					requestBody: this.modificationData.requestBody,
					bindParam : JSON.stringify(this.modificationData.bindParam)
				}
				this.dialogLoading = true
				getHttpResponse(Params).then(res => {
					if (res.status == "1") {
						this.modificationData.responseBody = JSON.stringify(JSON.parse(res.data), null, 4)
					}
					this.dialogLoading = false
				})
			},
			// 关闭弹窗所有弹窗 且清除临时数据
			handleClose() {
				this.dialogVisible = false
				this.modificationVisible = false
				this.show = false
				this.tempDialogData = null
				this.modificationData = {
					name: "",
					url: '',
					method: '',
					requestBody: '',
					requestHeaders: [{
						key: 'Content-Type',
						value: 'application/json'
					}],
					responseBody: '',
					bindParam:{
						variable:[],
						dictVariable:[],
					},
				}

				this.modificationId = null
			},
			// 查看  弹窗显示
			dialogShow(index) {
				this.tempDialogData = this.data.data.klist[index]
				console.log(this.tempDialogData)
				// this.tempDialogData.requestHeaders = JSON.parse(this.tempDialogData.requestHeaders)
				this.dialogVisible = true
			},
			// 修改  弹窗显示
			modificationShow(id,show) {
				this.modificationId = id
				this.data.data.klist.forEach(value => {
					if (value.id === id) {
						this.modificationData = {
							...value
						}
						this.modificationData.bindParam = value.bindParam?JSON.parse(value.bindParam) : JSON.parse(JSON.stringify(bindParamOr))
						
						
						if (value.requestHeaders) {
							if (typeof this.modificationData.requestHeaders == "string") {
								this.modificationData.requestHeaders = JSON.parse(value.requestHeaders)


							}

						} else {
							this.modificationData.requestHeaders = [{
								key: 'Content-Type',
								value: 'application/json'
							}]
						}

					}
				})
				this.modificationVisible = true
				if(show){
					this.show = true
				}
			},
			// 确定修改或者添加
			modificationSure() {
				if(this.show){
					this.handleClose()
					return
				}
				if (this.modificationData.name.indexOf(' ') !== -1 || this.modificationData.url.indexOf(' ') !== -1 || this
					.modificationData
					.method == "") {
					this.$message.error('必填项不允许出现空格！')
				} else {
					if (this.modificationData.name.length < 4 || this.modificationData.url.length < 4) {
						this.$message.error('请检查必填项且长度不小于4！')
					} else {
						let is = false
						this.modificationData.requestHeaders.forEach(value => {
							if (value.key.trim() == "" || value.value.trim() == "") {
								is = true
							}
						})

						if (is) {
							this.$message.error('请填入请求头');
							return
						}
						// this.modificationData.responseBody
						if (!this.isJSON(this.modificationData.responseBody)) {
							this.$message.error('请检查 响应体 是否为JSON格式');
							return
						}
						this.dialogLoading = true
						
						let params = {
							name : this.modificationData.name,
							url : this.modificationData.url,
							method : this.modificationData.method,
							requestBody : this.modificationData.requestBody,
							responseBody : this.modificationData.responseBody,
							bindParam : JSON.stringify(this.modificationData.bindParam)
						}

						

						let subobj = {}
						this.modificationData.requestHeaders.forEach(value => {
							subobj[value.key] = value.value
						})

						params.requestHeaders = String(JSON.stringify(subobj))
				
						if (this.modificationId !== null) {
							params.id = this.modificationData.id
						
							updateInterface(params).then(res => {
								if (res.status == "1") {
									this.data.data.klist.forEach((value, index) => {
										if (value.id === this.modificationData.id) {
											this.modificationData.bindParam = JSON.stringify(this.modificationData.bindParam)
											this.$set(this.data.data.klist, index, {
												...this.modificationData
											})
										}
									})
									this.$message({
										message: '修改成功',
										type: 'success'
									});
									this.dialogLoading = false
									this.handleClose()
								}
							})
						} else {
							params.id = ""
							this.setDataSources(params).then(res => {
								if (res.status === "1") {
									// 创建成功方法
									this.pageNum = 1
									this.getInterfaceLists({
										pageNum: this.pageNum,
										pageSize: 10,
										"interfaceInfo": {}
									})
									this.$message({
										message: '创建成功',
										type: 'success'
									});
									this.dialogLoading = false
									this.handleClose()
								}
							})
						}

					}

				}
				// console.log(this.modificationData.name.indexOf(' '))
			},
			async setDataSources(params) {
				this.loading = true
				const data = await addInterface(params)
				this.loading = false
				return data
			},
			createEngine() {
				this.modificationVisible = true
			},
			searchEngine() {},
			async getInterfaceLists(params) {
				this.loading = true
				const data = await getInterfaceList(params)
				if (data.status === "1") {
					this.data = data
					data.data.klist = data.data.klist.map(value => {
						value.requestBody = this.isJSON(value.requestBody) ? JSON.stringify(JSON.parse(value
							.requestBody), null, 4) : ''
						value.responseBody = this.isJSON(value.responseBody) ? JSON.stringify(JSON.parse(value
							.responseBody), null, 4) : ''

						if (!Array.isArray(JSON.parse(value.requestHeaders))) { //讲 key:value 格式 更改为老格式
							value.requestHeaders = JSON.stringify(this.format(JSON.parse(value
								.requestHeaders)))


						}

						return value
					})
					this.page = data.data.pageInfo
				} else {
					this.$message.error('访问出错了-_-');
				}
				this.loading = false
			},
			format(obj) {
				return Object.keys(obj).map(value => {
					return {
						key: value,
						value: obj[value]
					}


				})



			},
			deletelistsor(id) {

				this.$confirm('确定删除？', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'error'
				}).then(() => {
					this.deletelists({
						id: id
					})
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
						this.getInterfaceLists({
							pageNum: this.pageNum,
							pageSize: 10,
							"interfaceInfo": {}
						})
						this.$message({
							message: '删除成功',
							type: 'success'
						});
					}
				})
			},
			async deletelist(id) {
				this.loading = true
				return await deleteInterface(id)
				this.loading = false
			},
			currpage(num) {
				this.pageNum = num
				this.getInterfaceLists({
					pageNum: this.pageNum,
					pageSize: 10,
					"interfaceInfo": {}
				})
				console.log(this.pageNum)
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

			},
			isJSON(str) {
				if (typeof str == 'string') {
					try {
						var obj = JSON.parse(str);
						if (typeof obj == 'object' && obj) {
							return true;
						} else {
							return false;
						}

					} catch (e) {
						console.log('error：' + str + '!!!' + e);
						return false;
					}
				}
			},
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

	.portS_dialog .port_textLayout {
		display: flex;
		font-size: 16px;
		height: 40px;
		padding-left: 0px;
		margin-top: 10px;
	}

	.port_textLayout>p {
		height: 100%;
		line-height: 30px;
	}

	.portS_dialog .port_textLayout>p:nth-of-type(1) {
		width: 32%;
		text-align: right;
		margin-right: 20px;
		line-height: 40px;
	}

	.portS .DataSource_look .port_textLayout {
		display: flex;
		align-items: center;
		margin-top: 5px;
	}

	.DataSource_look .port_textLayout>p:nth-of-type(2) {
		border-radius: 5px;
		line-height: 40px;
		box-sizing: border-box;
	}

	.el-dialog {
		border-radius: 20px !important;
	}

	.portS_dialog .port_textLayout {
		height: unset !important;
	}
</style>

<template>
	<div v-loading="loading">

		<!-- 创建和搜索 -->
		<el-row style="margin-bottom: 30px;margin-top: 20px;">
			<el-col :span="15">
				<el-button type="primary" @click="createEngine">创建引擎</el-button>
			</el-col>
			<el-col :span="6">
				<el-input placeholder="请输入搜索关键字" v-model="searchString">
					<el-button style="margin-top: 6px;" slot="suffix" icon="el-icon-search" size="mini" circle @click="searchEngine">
					</el-button>
				</el-input>
			</el-col>
		</el-row>

		<!-- 表格显示 -->
		<el-row>
			<el-table border :data="enginelist" style="width: 100%" :cell-style="{padding: '10px'}" >

				<el-table-column prop="id" label="引擎ID" align="center">
					<template slot-scope="scope">
						<el-link :underline="false" @click="engineDisplay(scope.row.id)">
							<!-- {{scope.$index}} -->
							{{scope.row.id}}
						</el-link>
					</template>
				</el-table-column>

				<el-table-column prop="name" label="名称" align="center" show-overflow-tooltip>
					<template slot-scope="scope">
						<el-link :underline="false" @click="engineDisplay(scope.row.id)">
							{{scope.row.name}}
						</el-link>
					</template>
				</el-table-column>

				<el-table-column prop="description" label="描述" align="center" show-overflow-tooltip>
					<template slot-scope="scope">
						<el-link :underline="false" @click="engineDisplay(scope.row.id)">
							{{scope.row.description}}
						</el-link>
					</template>
				</el-table-column>

				<el-table-column prop="runState" label="运行状态" align="center">
					<template slot-scope="scope">
						<el-link :underline="false" v-if="scope.row.runState==1" @click="engineDisplay(scope.row.id)">
							运行中
						</el-link>
						<el-link :underline="false" v-else @click="engineDisplay(scope.row.id)">
							停止
						</el-link>
					</template>
				</el-table-column>

				<el-table-column prop="createDatetime" label="最后更新时间" align="center">
					<template slot-scope="scope">
						<el-link :underline="false" @click="engineDisplay(scope.row.id)">
							{{dateFormat(scope.row.createDatetime)}}
						</el-link>
					</template>
				</el-table-column>

				<el-table-column label="操作" align="center">
					<template slot-scope="scope">
						<el-button type="info" icon="el-icon-s-tools" circle @click="operateEngine(scope.row.id)">
						</el-button>
					</template>
				</el-table-column>

			</el-table>
		</el-row>

		<!-- 分页 -->
		<el-row style="margin: 20px 50px 0 75%;">
			<el-pagination background layout="prev, pager, next" :total="pager.total" :page-size="pager.pageSize"
			 @current-change="handleCurrentChange">
			</el-pagination>
		</el-row>

		<!-- 弹窗 -->
		<el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="30%" :before-close="handleClose">
			<!-- <span>这是一段信息</span> -->
			<el-row style="margin-bottom: 20px;">
				<el-col :span="4" style="padding-top: 9px;">引擎编码：</el-col>
				<el-col :span="20">
					<el-input placeholder="" v-model="dialogCode" :disabled="true">
					</el-input>
				</el-col>
			</el-row>
			<el-row style="margin-bottom: 20px;">
				<el-col :span="4" style="padding-top: 9px;">引擎名称：</el-col>
				<el-col :span="20">
					<el-input v-model="dialogName" placeholder="请输入内容"></el-input>
				</el-col>
			</el-row>

			<el-row style="margin-bottom: 20px;">
				<el-col :span="4" style="padding-top: 9px;">引擎描述：</el-col>
				<el-col :span="20">
					<el-input type="textarea" :rows="4" placeholder="请输入内容" v-model="dialogDescription">
					</el-input>
				</el-col>
			</el-row>

			<el-row style="margin-bottom: 20px;">
				<el-col :span="4" style="padding-top: 9px;">回调地址：</el-col>
				<el-col :span="20">
					<el-input v-model="dialogCallbackUrl" placeholder=""></el-input>
				</el-col>
			</el-row>
			<el-row style="margin-bottom: 20px;">
				<el-col :span="4" style="padding-top: 9px;">异常回调：</el-col>
				<el-col :span="20">
					<el-input v-model="dialogExceptionCallbackUrl" placeholder="异常回调地址"></el-input>
				</el-col>
			</el-row>

			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogVisible = false">取 消</el-button>
				<el-button type="primary" @click="saveEngine">确 定</el-button>
			</span>
		</el-dialog>

	</div>
</template>

<script>
	import bus from './bus.js'
	import {
		getEngineList
	} from '../../api/index';
	import {
		getEngineUUID
	} from '../../api/index';
	import {
		engineInitupdate
	} from '../../api/index';
	import {
		updateEngine
	} from '../../api/index';
	export default {
		name:'engineListV2',
		data() {
			
			return {
				loading: false,
				visible: false,
				searchString: '',
				enginelist: [],
				dialogVisible: false,
				dialogTitle: '创建引擎',
				dialogId: '',
				dialogCode: '',
				dialogName: '',
				dialogDescription: '',
				dialogCallbackUrl: '',
				dialogExceptionCallbackUrl:'',
				pager: {
					total: 0,
					pageSize: 10,
					pageNum: 1,
				}
			}
		},
		mounted: function() {
			let param = {
				pageNo: 1,
				pageSize: 10
			}
			this.getEngineLists(param);
		},
		methods: {
			async getEngineLists(param) {
				const data = await getEngineList(param);
				this.enginelist = data.data.enginelist;
				this.pager.total = data.data.pager.total;
				this.pager.pageSize = data.data.pager.pageSize;
				this.pager.pageNum = data.data.pager.pageNum;
			},

			// 时间格式化
			dateFormat: function(value) {
				let date = new Date(value);
				let y = date.getFullYear();
				let MM = date.getMonth() + 1;
				MM = MM < 10 ? ('0' + MM) : MM;
				let d = date.getDate();
				d = d < 10 ? ('0' + d) : d;
				let h = date.getHours();
				h = h < 10 ? ('0' + h) : h;
				let m = date.getMinutes();
				m = m < 10 ? ('0' + m) : m;
				let s = date.getSeconds();
				s = s < 10 ? ('0' + s) : s;
				return y + '-' + MM + '-' + d + ' ' + h + ':' + m + ':' + s;
			},

			// 搜索
			searchEngine: function() {
				
				this.getEngineLists({
					searchString: this.searchString
				});
				// getEngineList({ searchString: this.searchString }).then(res => {
				//   paging(res.data);
				//   //this.enginelist = res.data.enginelist;
				//   //this.pager.total = res.data.total;
				//   //this.pager.pageSize = res.data.pageSize;
				//   //this.pager.pageNum = res.data.pageNum;
				// });
			},
			// 表格中点击
			engineDisplay: function(id) {
				//alert(id);
				let h5Path = '';
				window.localStorage.setItem("engineId", id);
				bus.$emit('EngineSwitchover',id)
				
				window.top.location.href = h5Path + "/#/engine_decision_flow";
			},
			// 对话框
			handleClose(done) {
				// 点击外部
				this.$confirm('确认关闭？')
					.then(_ => {
						done();
					})
					.catch(_ => {});
			},
			// 创建引擎
			createEngine: function() {
				getEngineUUID().then(res => {
					this.dialogCode = res.data;

					this.dialogTitle = '创建引擎';
					this.dialogId = '';
					this.dialogName = '';
					this.dialogDescription = '';
					this.dialogCallbackUrl = '';
					this.dialogExceptionCallbackUrl = '';
					
					this.dialogVisible = true;
				});

			},
			// 编辑引擎
			operateEngine: function(id) {
				/*  调用后端接口  */
				engineInitupdate(id).then(res => {
					this.dialogTitle = '编辑引擎';
					this.dialogId = id;
					this.dialogCode = res.data.code;
					this.dialogName = res.data.name;
					this.dialogDescription = res.data.description;
					this.dialogCallbackUrl = res.data.callbackUrl;
					this.dialogExceptionCallbackUrl = res.data.exceptionCallbackUrl;
				
					this.dialogVisible = true;
				});
			},
			// 保存
			saveEngine: function() {
				let engine = {
					id: this.dialogId,
					code: this.dialogCode,
					name: this.dialogName,
					description: this.dialogDescription,
					callbackUrl: this.dialogCallbackUrl,
					exceptionCallbackUrl:this.dialogExceptionCallbackUrl
				};
				this.loading = true
				updateEngine(engine).then(res => {
					if (res.data == 1) {
						this.getEngineLists({
							pageNo: 1
						}); // 重新加载表格数据
						this.dialogVisible = false;
						
					}
					this.loading = false
				});
				// window.location.reload();
			},

			// 页数改变
			handleCurrentChange: function(currentPage) {
				// this.pager.pageNum = currentPage;
				this.loading = true
				this.getEngineLists({
					pageNo: currentPage
				}).then(res => {
					this.loading = false
				});
			}
		}
	}
</script>

<style>
</style>

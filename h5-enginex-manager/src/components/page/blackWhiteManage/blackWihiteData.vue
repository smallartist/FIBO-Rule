<template>
	<div class="content-wrapper">
		<div>
			<el-row :gutter="20">
				<el-col :span="16">
					<div>
						<el-button @click="add" type="primary">新增</el-button>
						<el-button @click="using(-1)" type="danger" :disabled="multipleSelection.length<1?'disabled':false">删除</el-button>
						<el-button @click="using(1)" type="success" :disabled="multipleSelection.length<1?'disabled':false">启用</el-button>
						<el-button @click="using(0)" type="warning" :disabled="multipleSelection.length<1?'disabled':false">停用</el-button>
					</div>
				</el-col>
				<el-col :span="8" style="text-align: right;">
					
					<div>
						<el-button slot="trigger" type="primary" style="margin-right:2em;" @click="downloadTemplate">下载模板</el-button>
					
					</div>
				</el-col>
			</el-row>
		</div>
		<div class="tab-wrapper">
			<div>
				<el-table border ref="multipleTable" :data="dataList" tooltip-effect="dark" style="width: 100%" @selection-change="handleSelectionChange">
					<el-table-column type="selection" width="55">
					</el-table-column>

					<el-table-column prop="listName" label="名称" width="" show-overflow-tooltip>
					</el-table-column>

					<el-table-column prop="listDesc" label="描述">
					</el-table-column>

					<el-table-column prop="status" width="" label="状态">
						<template slot-scope="scope">
							{{scope.row.status==1?'启用':scope.row.status===0?'停用':'删除'}}
						</template>
					</el-table-column>

					<el-table-column prop="nickName" width="" label="创建人">
					</el-table-column>

					<el-table-column prop="created" label="创建时间">
						<template slot-scope="scope">
							{{scope.row.created|formatDate}}
						</template>
					</el-table-column>
					<el-table-column label="操作" align="center" size="s">
						<template slot-scope="scope">
							<el-tooltip content="编辑名单库" placement="left">
								<el-button icon="el-icon-setting" circle size="mini" @click="editDiaglo(scope.row)"></el-button>
							</el-tooltip>
							<el-tooltip content="导入名单库数据" placement="top">
								<el-button icon="el-icon-upload" circle size="mini" @click="importTemplate(scope.row)"></el-button>
							</el-tooltip>							
							<el-tooltip content="查看名单库数据" placement="right">
								<el-button icon="el-icon-view" circle size="mini" @click="lookImportTemplate(scope.row)"></el-button>
							</el-tooltip>
						</template>
					</el-table-column>
				</el-table>
				<el-pagination class="pagination-wrapper" background hide-on-single-page :current-page="pager.pageNum" :page-count="pager.lastPage"
				 layout="prev, pager, next" @current-change="surrentChange">
				</el-pagination>
			</div>
		</div>
		<!-- 添加/编辑 -->
		<add-block-white :dialogVisible="dialogVisible" @closeEvent="handleClose" :dataItem="dataItem"></add-block-white>
		<el-dialog title="导入名单库数据" :visible.sync="upShow" width="30%">
			<div v-loading="Uploadloading">
				<div style="margin: 0 auto;display: flex;justify-content: center;" :disabled="true">
					<el-upload class="upload-demo" ref="upload" action="doUpload" :limit="1" :file-list="fileList" :before-upload="beforeUpload">
						<el-button slot="trigger" size="small" type="primary" style="margin-top: 60px;">选取文件</el-button>

						<div slot="tip" class="el-upload__tip">只能上传excel文件，且不超过5MB</div>
						<div slot="tip" class="el-upload-list__item-name">{{fileName}}</div>
					</el-upload>
				</div>

				<div slot="footer" class="dialog-footer" style="margin-left: 30%;margin-top: 30px;">
					<el-button @click="upShow=false">取消</el-button>
					<el-button type="primary" @click="submitUpload()" :disabled="fileName?false:'disabled'">确定</el-button>
				</div>
			</div>
		</el-dialog>
		<el-dialog title="名单库数据" :visible.sync="lookBox">
			<el-table border :data="listData">
				
				<el-table-column property="id" label="id" width="60"></el-table-column>
				<el-table-column property="created" label="创建时间" width="150">
					<template slot-scope="scope">
						{{scope.row.created|formatDate}}
					</template>
				</el-table-column>
				<el-table-column v-for="(item,idx) in 20" :key="idx" :label="'t'+idx" :property="'t'+idx" width="200">
				</el-table-column>
				
			</el-table>
			<el-pagination class="pagination-wrapper" background :current-page="pager2.pageNum" :page-count="pager2.lastPage"
				 layout="prev, pager, next" @current-change="pageChange">
			</el-pagination>
		</el-dialog>
	</div>
</template>
<script>
	import {
		formatDate
	} from '@/assets/utils.js'
	import {
		getBlackWhiteList,
		updateStatus,
		listmanageUpload,
		getListDbData
	} from '@/api/index.js'
	import AddBlockWhite from '../../models/addBlockWhite.vue';
	export default {
		name:'blackWihiteData',
		components: {
			AddBlockWhite
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
				pager2:{},
				options: [{
						value: 'w',
						label: '白名单'
					},
					{
						value: 'b',
						label: '黑名单'
					}
				],
				value: 'b',
				dataList: [],
				multipleSelection: [],
				dialogVisible: false,
				dataItem: {},
				Uploadloading:false,
				currentData:{},
				upShow:false,
				fileList: [],
				fileName:"",
				lookBox:false,
				listData: []
			};
		},
		created() {
			this.getlist();
		},
		methods: {
			submitUpload() {
				this.Uploadloading = true;
				// console.log('上传' + this.files.name)
				if (this.fileName == "") {
					this.$message.warning('请选择要上传的文件！')
					this.Uploadloading = false
					return false
				}
				let fileFormData = new FormData();
				fileFormData.append('file', this.files); //filename是键，file是值，就是要传的文件，test.zip是要传的文件名
				fileFormData.append('engineId',this.engid)
				this.listmanageUpload(this.currentData.id,fileFormData).then(res=>{
						if (res.status === "1") {
							this.$alert(res.data, '提示', {
								confirmButtonText: '确定',
								callback: action => {
									this.upShow=false;
								}
							});
						}
						this.Uploadloading = false
				})
			},
			beforeUpload(file) {
				this.files = file;
				const extension = file.name.split('.')[1] === 'xls'
				const extension2 = file.name.split('.')[1] === 'xlsx'
				const isLt2M = file.size / 1024 / 1024 < 5
				if (!extension && !extension2) {
					this.$message.warning('上传模板只能是 xls、xlsx格式!')
					return
				}
				if (!isLt2M) {
					this.$message.warning('上传模板大小不能超过 5MB!')
					return
				}
				this.fileName = file.name;
				return false // 返回false不会自动上传
			},
			async listmanageUpload(id,param) {
				const data = await listmanageUpload(id,param);
				return data
			},
			async listmanageDownTemplate(){
				const data = await listmanageDownTemplate();
				return data
			},
			async getListDbData(param){
				const data = await getListDbData(param);
				return data
			},
			lookImportTemplate(e){
				console.log("查看",e)
				this.currentData=e;
				this.lookBox=true;
				this.getListDbData({
					"id": e.id,
					"pageNo": 1,
					"pageSize": 5
				}).then(res=>{
					console.log(res);
					this.listData=res.data.list;
					this.pager2 = res.data.pageInfo
				})
			},
			pageChange(e) {
				// this.page2 = e;
				this.getListDbData({
					"id": this.currentData.id,
					"pageNo": e,
					"pageSize": 5
				}).then(res=>{
					console.log(res);
					this.listData=res.data.list;
					this.pager2 = res.data.pageInfo
				})
			},
			importTemplate(e){
				this.currentData=e;
				this.upShow=true;
			},
			downloadTemplate(){
				console.log("下载模板！")
				window.open(window.origin + '/Riskmanage/v2/datamanage/listmanage/downTemplate')
				// this.listmanageDownTemplate().then(res=>{
				// 	console.log(res);
				// 	const content = res.data
            	// 	const blob = new Blob([content])
				// 	console.log(blob);
				// 	const fileName = '名单库模板.xlsx'
				// 	if ('download' in document.createElement('a')) { // 非IE下载
				// 		const elink = document.createElement('a')
				// 		elink.download = fileName
				// 		elink.style.display = 'none'
				// 		elink.href = URL.createObjectURL(blob)
				// 		document.body.appendChild(elink)
				// 		elink.click()
				// 		URL.revokeObjectURL(elink.href) // 释放URL 对象
				// 		document.body.removeChild(elink)
				// 	} else { // IE10+下载
				// 		navigator.msSaveBlob(blob, fileName)
				// 	}
				// });
				
			},			
			handleClose() {
				this.dialogVisible = false;
				this.getlist();
			},
			// 添加名单库
			add() {
				this.dialogVisible = true;
				this.dataItem = {};
				// let e = "";
				// this.$router.push({path:'/blackWhiteManage/addBlackWihite/$'})
			},
			// 编辑名单库 
			editDiaglo(e) {
				this.dataItem = e;
				this.dialogVisible = true;
				// this.$router.push({path:`/blackWhiteManage/addBlackWihite/${e.id}`})
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
				switch (num) {
					case -1:
						this.updateStatus(-1, selArr.join(','));
						break;
					case 1:
						this.updateStatus(1, selArr.join(','));
						break;
					case 0:
						this.updateStatus(0, selArr.join(','));
						break;
					default:
						break;
				}
				this.getlist();
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
				const data = await getBlackWhiteList({
					listType: 'b',					
					pageNo: this.page,
					pageSize: this.pageSize
				})
				if (data.status != "0") {
					const listDbs = data.data.listDbs;
					const pager = data.data.pager;
					this.pager = pager;
					this.dataList = listDbs
				}
			},
			async updateStatus(status, arrStr) {
				const data = await updateStatus({
					"ids": arrStr,
					"status": status,
					"listType": this.value
				})
				if (data.status != "0") {
					this.$message({
						message: '操作成功！',
						type: "success"
					});
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
</style>

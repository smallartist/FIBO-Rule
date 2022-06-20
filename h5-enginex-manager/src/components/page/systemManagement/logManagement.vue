<template>
	<div class="content-wrapper">
		<div>
			<el-row>
				<el-col :span="18">
					<div >
						当前时间：
						<el-date-picker v-model="dateValue" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期"
						 value-format="yyyy-MM-dd" @change="getlist">
						</el-date-picker>
					</div>
				</el-col>
				<el-col :span="6">
					<div >
						<el-input placeholder="搜索" v-model="searchVal" class="input-with-select">
							<el-button slot="append" icon="el-icon-search" @click="getlist"></el-button>
						</el-input>
					</div>
				</el-col>
			</el-row>
		</div>
		<div class="tab-wrapper">
			<div>
				<el-table border :data="dataList" style="width: 100%">

					<el-table-column prop="id" width="200" label="ID">
					</el-table-column>

					<el-table-column prop="organName" label="公司名称" width="" show-overflow-tooltip>
					</el-table-column>

					<el-table-column prop="opName" label="操作名称" width="" show-overflow-tooltip>
					</el-table-column>

					<el-table-column prop="opUserName" label="操作人员名称" width="" show-overflow-tooltip>
					</el-table-column>

					<el-table-column prop="ip" label="IP" width="" show-overflow-tooltip>
					</el-table-column>

					<el-table-column prop="startTime" label="开始时间" width="" show-overflow-tooltip>
						<template slot-scope="scope">
							{{new Date(scope.row.startTime).format('yyyy-MM-dd hh:mm:ss')}}
						</template>
					</el-table-column>

					<el-table-column prop="endTime" label="结束时间" width="" show-overflow-tooltip>
						<template slot-scope="scope">
							{{new Date(scope.row.endTime).format('yyyy-MM-dd hh:mm:ss')}}
						</template>
					</el-table-column>

					<el-table-column label="操作" align="center" size="s">
						<template slot-scope="scope">
							<el-tooltip content="查看" placement="left">
								<el-button icon="el-icon-view" circle size="mini" @click="lockData(scope.row)"></el-button>
							</el-tooltip>
						</template>
					</el-table-column>
				</el-table>
				<el-pagination class="pagination-wrapper" background hide-on-single-page :current-page="pager.pageNum" :page-count="pager.lastPage"
				 layout="prev, pager, next" @current-change="surrentChange">
				</el-pagination>
			</div>
		</div>

		<!-- 查看 -->
		<el-dialog title="日志详情" :visible.sync="dialogVisible" width="30%" center>
			<div class="log-info-wrapper">
				<p><strong>操作名称：</strong>{{currentItem.opName}}</p>
				<p><strong>操作人员名称：</strong>{{currentItem.opUserName}}</p>
				<p><strong>IP：</strong>{{currentItem.ip}}</p>
				<p><strong>请求路径：</strong>{{currentItem.requestPath}}</p>
				<p><strong>请求参数：</strong>{{currentItem.requestParam}}</p>
				<p><strong>响应参数：</strong>{{currentItem.responseParam}}</p>
			</div>
			<span slot="footer" class="dialog-footer">
				<el-button type="primary" @click="dialogVisible = false">关 闭</el-button>
			</span>
		</el-dialog>

	</div>
</template>
<script>
	import {
		formatDate
	} from '@/assets/utils.js'
	import {
		getLogList
	} from '@/api/index.js'
	export default {
		//过滤
		name:'logManagement',
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
				dateValue: [formatDate(new Date, 'yyyy-MM-dd'), formatDate(new Date, 'yyyy-MM-dd')],
				searchVal: "",
				dialogVisible: false,
				currentItem: {}
			};
		},
		created() {
			this.getlist();
		},
		methods: {
			lockData(e) {
				this.currentItem = e;
				this.dialogVisible = true;
			},
			surrentChange(e) {
				this.page = e;
				this.getlist();
			},
			async getlist() {
				const data = await getLogList({
					searchKey: this.searchVal,
					startDate: this.dateValue[0],
					endDate: this.dateValue[1],
					pageNo: this.page,
					pageSize: this.pageSize
				})
				if (data.status != "0") {
					const listUser = data.data.logList;
					const pager = data.data.pager;
					this.pager = pager;
					this.dataList = listUser
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

	.content-wrapper .el-date-editor .el-range-separator {
		width: 10%;
	}

	.el-dialog--center .el-dialog__body {
		max-height: 500px;
		overflow-y: scroll;
	}

	.log-info-wrapper p {
		line-height: 2;
		margin-bottom: 5px;
	}
</style>

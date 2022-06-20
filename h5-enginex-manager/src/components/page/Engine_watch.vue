<style>
	.engWatch_header{
	display: flex;
	justify-content: flex-start;
}
.engWatch_look:hover{
	cursor:pointer
}
</style>

<template>
	<div>
		<div class="engWatch_header" v-loading="headerLoading">
			<el-select v-model="engineId" filterable placeholder="请选择引擎" @change="currPage=1;getList()">
				<el-option v-for="item in engineList" :key="item.id" :label="item.name" :value="item.id">
				</el-option>
			</el-select>

			<el-input v-model="searchInput" placeholder="业务ID" style="width: 30%;margin-left: 30px;">
				<template slot="append"><i class="el-icon-search" @click="currPage=1;getList()"></i></template>
			</el-input>

			<el-date-picker v-model="selectTime" type="daterange" align="right" unlink-panels range-separator="至"
			 start-placeholder="开始日期" end-placeholder="结束日期" :picker-options="pickerOptions" style="margin-left: 30px;" @change="currPage=1;getList()">
			</el-date-picker>

		</div>

		<div style="margin-top: 30px;" v-loading="loading">
			<el-table :data="tableData" style="width: 100%">
				<el-table-column :prop="item.prop" :label="item.label" v-for="item in tabel">
					<template slot-scope="scope">
						<span v-if="item.label=='创建时间'">
							{{new Date(scope.row[item.prop]).format('yyyy-MM-dd hh:mm:ss')}}
						</span>
						<p v-else-if="item.label=='操作'" @click="goInfo(scope.row)" class="engWatch_look">
							<i class="el-icon-search"></i>查看
						</p>
						<span v-else-if="item.label=='执行结果'">
							{{scope.row[item.prop]?JSON.parse(scope.row[item.prop]).msg:'无结果'}}
						</span>
						<span v-else>
							{{scope.row[item.prop]}}
						</span>
					</template>
				</el-table-column>



			</el-table>
			<el-pagination style="float: right;margin-top: 20px;" background layout="prev, pager, next" :total="total"
			 :current-page="currPage" @current-change="currPage=$event;getList()">
			</el-pagination>



		</div>


	</div>
</template>

<script>
	import {
		getMonitorResults,
		getEngineList
	} from '@/api/index.js'
	export default {
		name: 'engineWatch',
		data() {
			return {
				total: 0,
				currPage: 1,
				headerLoading: false,
				loading: false,
				tableData: [],
				tabel: [ // 表格
					{
						label: '引擎',
						prop: 'engineName'
					},
					{
						label: '业务ID',
						prop: 'pid'
					},
					{
						label: '执行结果',
						prop: 'output'
					},
					{
						label: '创建时间',
						prop: 'createDatetime'
					},
					{
						label: '操作',
						prop: ''
					}
				],
				searchInput: '', // 搜索
				engineId: '', // 选中的引擎
				// engineList: [], //引擎列表
				selectTime: '', // 时间选择框
				pickerOptions: {
					shortcuts: [{
						text: '最近一周',
						onClick(picker) {
							const end = new Date();
							const start = new Date();
							start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
							picker.$emit('pick', [start, end]);
						}
					}, {
						text: '最近一个月',
						onClick(picker) {
							const end = new Date();
							const start = new Date();
							start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
							picker.$emit('pick', [start, end]);
						}
					}, {
						text: '最近三个月',
						onClick(picker) {
							const end = new Date();
							const start = new Date();
							start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
							picker.$emit('pick', [start, end]);
						}
					}]
				},
			}
		},
		created() {
			// this.headerLoading = true
			this.selectTime = [
				this.fun_date(-7), new Date()
			]
			this.engineId = Number(localStorage.getItem('engineId'))
			this.getList()
			// getEngineList({
			// 	pageNo: 1,
			// 	pageSize: 999999
			// }).then(res => {
			// 	if (res.status == "1") {
			// 		this.engineList = res.data.enginelist
			// 	}
			// 	this.headerLoading = false
			// }).catch(err => {
			// 	this.headerLoading = false
			// })
		},
		computed:{
			engineList(){
				
				if(this.$store.state.Engine){
					return this.$store.state.Engine
				}
				return []
				
			}
		},
		methods: {
			goInfo(item) {
				console.log(item.hbaseRowKey)
				this.$router.push('/engine_result_cont/' + item.hbaseRowKey+'|'+item.id)
			},
			getList() {
				this.getMonitorResult(this.engineId, this.formateDate(this.selectTime[0]), this.formateDate(this.selectTime[1]),
					this.searchInput, this.currPage, 10)
			},
			getDateRange(dateNow, intervalDays, bolPastTime) {
				let oneDayTime = 24 * 60 * 60 * 1000;
				let list = [];
				let lastDay;

				if (bolPastTime == true) {
					lastDay = new Date(dateNow.getTime() - intervalDays * oneDayTime);
					list.push(this.formateDate(lastDay));
					list.push(this.formateDate(dateNow));
				} else {
					lastDay = new Date(dateNow.getTime() + intervalDays * oneDayTime);
					list.push(this.formateDate(dateNow));
					list.push(this.formateDate(lastDay));
				}
				return list;
			},
			formateDate(time) {
				let year = time.getFullYear()
				let month = time.getMonth() + 1
				let day = time.getDate()

				if (month < 10) {
					month = '0' + month
				}
				if (day < 10) {
					day = '0' + day
				}
				return year + '-' + month + '-' + day + ''

			},
			fun_date(num) {
				var date1 = new Date();
				//今天时间
				var time1 = date1.getFullYear() + "-" + (date1.getMonth() + 1) + "-" + date1.getDate()
				console.log(time1);
				var date2 = new Date(date1);
				date2.setDate(date1.getDate() + num);
				return date2;
			},
			async getMonitorResult(engineId, startDate, endDate, businessId, pageNo, pageSize) {
				// {Number} engineId 引擎id
				// {String} [startDate] 开始时间，示例："2021-03-26 23:59:59" 或 "2020-03-26"
				// {String} [endDate] 结束时间，示例："2021-03-26 23:59:59" 或 "2020-03-26"
				// {String} [businessId] 业务ID
				// {Number} [pageNo=1] 第几页
				// {Number} [pageSize=10] 每页的条数
				this.loading = true
				await getMonitorResults({
					engineId: engineId,
					startDate: startDate,
					endDate: endDate,
					businessId: businessId,
					pageNo: pageNo,
					pageSize: pageSize
				}).then(res => {
					if (res.status == "1") {
						this.tableData = res.data.pager.list
						this.total = res.data.pager.total
					}
					this.loading = false
				}).catch(err => {
					this.loading = false
				})
			},
		}


	}
</script>

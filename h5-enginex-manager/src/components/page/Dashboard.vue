<template>
	<div>
		<el-row :gutter="20">
			<el-col :span="8">
				<el-card shadow="hover" class="mgb20" style="height:252px;">
					<div class="user-info">
						<img src="../../assets/img/img.jpg" class="user-avator" alt />
						<div class="user-info-cont">
							<div class="user-info-name">{{name}}</div>
							<!-- <div>{{role}}</div> -->
						</div>
					</div>
					<div class="user-info-list">
						上次登录时间：
						<span>{{lastLoginTime | formatDate}}</span>
					</div>
				</el-card>
				<el-card shadow="hover" style="height:252px;">
					<div slot="header" class="clearfix">
						<span>引擎详情</span>
					</div>
					<span v-if="engineRatio1">{{engineRatio1.engineName}}
						<el-progress :percentage="engineRatio1.useRatio" color="#42b983"></el-progress>
					</span>
					<span v-if="engineRatio2">{{engineRatio2.engineName}}
						<el-progress :percentage="engineRatio2.useRatio" color="#f1e05a"></el-progress>
					</span>
					<span v-if="engineRatio3">{{engineRatio3.engineName}}
						<el-progress :percentage="engineRatio3.useRatio"></el-progress>
					</span>
					<span v-if="engineRatio4">{{engineRatio4.engineName}}
						<el-progress :percentage="engineRatio4.useRatio" color="#f56c6c"></el-progress>
					</span>
				</el-card>
			</el-col>
			<el-col :span="16">
				<el-row :gutter="20" class="mgb20">
					<el-col :span="8">
						<el-card shadow="hover" :body-style="{padding: '0px'}">
							<div class="grid-content grid-con-1">
								<i class="el-icon-lx-people grid-con-icon"></i>
								<div class="grid-cont-right">
									<div class="grid-num">{{engineNum}}</div>
									<div>引擎</div>
								</div>
							</div>
						</el-card>
					</el-col>
					<el-col :span="8">
						<el-card shadow="hover" :body-style="{padding: '0px'}">
							<div class="grid-content grid-con-2">
								<i class="el-icon-lx-notice grid-con-icon"></i>
								<div class="grid-cont-right">
									<div class="grid-num">{{engineNodeNum}}</div>
									<div>决策节点</div>
								</div>
							</div>
						</el-card>
					</el-col>
					<el-col :span="8">
						<el-card shadow="hover" :body-style="{padding: '0px'}">
							<div class="grid-content grid-con-3">
								<i class="el-icon-lx-goods grid-con-icon"></i>
								<div class="grid-cont-right">
									<div class="grid-num">{{engineResultNum}}</div>
									<div>决策结果</div>
								</div>
							</div>
						</el-card>
					</el-col>
				</el-row>
				<el-card shadow="hover" style="height:403px;">
					<div slot="header" class="clearfix">
						<span>活动日志</span>
					</div>
					<el-table :show-header="false" :data="todoList" style="width:100%;">
						<el-table-column>
							<template slot-scope="scope">
								<div class="todo-item">{{scope.row.opUserName}} {{scope.row.ip}} 在{{scope.row.startTime | formatDate}}
									{{scope.row.opName}}</div>
							</template>
						</el-table-column>
					</el-table>
				</el-card>
			</el-col>
		</el-row>
		<el-row :gutter="20">
			<el-col :span="12">
				<el-card shadow="hover">
					<charts sid="dashboardLeft" height='250px' width='100%' :option="options"></charts>
				</el-card>
			</el-col>
			<el-col :span="12">
				<el-card shadow="hover">
					<charts sid="dashboardRight" height='250px' width='100%' :option="options2"></charts>
				</el-card>
			</el-col>
		</el-row>
	</div>
</template>

<script>
	import charts from '@/components/common/charts.vue'
	import bus from '../common/bus';
	import {
		getIndexInfo
	} from '../../api/index';
	export default {
		name: 'dashboard',
		components: {
			// Schart,
			charts
		},
		data() {
			return {
				name: localStorage.getItem('ms_username'),
				lastLoginTime: null,
				engineNum: null,
				engineNodeNum: null,
				engineResultNum: null,
				engineRatio1: {},
				engineRatio2: {},
				engineRatio3: {},
				engineRatio4: {},

				todoList: [],
				options: {
					title: {
						text: '最近几天引擎使用情况',
						left: 'center'
					},
					grid: {
						bottom: '10% '
					},
					xAxis: [{
						type: 'category',
						axisTick: {
							show: false
						},
						data: []
					}],
					yAxis: [{
						type: 'value'
					}],
					tooltip: {
						trigger: 'axis'
					},
					legend: {
						orient: 'vertical',
						left: 'left',
					},
					series: []

				},
				options2: {
					title: {
						text: '最近几个月引擎使用趋势图',
						left: 'center'
					},
					grid: {
						bottom: '10% '
					},
					xAxis: [{
						type: 'category',
						axisTick: {
							show: false
						},
						data: []
					}],
					yAxis: [{
						type: 'value'
					}],
					tooltip: {
						trigger: 'item'
					},
					series: []
				
				},
			};
		},

		computed: {
			role() {
				return this.name === 'admin' ? '超级管理员' : '普通用户';
			}
		},
		// created() {
		//     this.handleListener();
		//     this.changeDate();
		// },
		// activated() {
		//     this.handleListener();
		// },
		// deactivated() {
		//     window.removeEventListener('resize', this.renderChart);
		//     bus.$off('collapse', this.handleBus);
		// },

		created() {
			this.getIndexInfos();
		},

		methods: {
			changeDate() {
				const now = new Date().getTime();
				this.data.forEach((item, index) => {
					const date = new Date(now - (6 - index) * 86400000);
					item.name = `${date.getFullYear()}/${date.getMonth() + 1}/${date.getDate()}`;
				});
			},

			async getIndexInfos() {
				const result = await getIndexInfo();
				const data = result.data;


				// 引擎基本信息
				this.engineNum = data.engineBaseInfo.engineNum;
				this.engineNodeNum = data.engineBaseInfo.engineNodeNum;
				this.engineResultNum = data.engineBaseInfo.engineResultNum;

				// 引擎使用占比
				this.engineRatio1 = data.engineUseRatio[0];
				this.engineRatio2 = data.engineUseRatio[1];
				this.engineRatio3 = data.engineUseRatio[2];
				this.engineRatio4 = data.engineUseRatio[3];

				// 最近几天引擎使用情况



				var labelOption = {
					show: true,
					position: 'insideBottomRight',
					distance: 10,
					align: 'left',
					verticalAlign: 'middle',
					rotate: 90,
					// formatter: '{c}  {name|{a}}',
					fontSize: 12,
					rich: {
						name: {}
					}
				};


				this.options.xAxis[0].data = data.recentDayMap.labels;
				let arr  = data.recentDayMap.datasets.map(value => {
					return {
						name: value.label,
						type: 'bar',
						barGap: 0,
						label: labelOption,
						emphasis: {
							focus: 'series'
						},
						data: value.data
					}
				})
				
				
				this.options.series = arr.filter((value,index)=>{
					return index<5
				})

				// 最近几个月引擎使用情况
				this.options2.xAxis[0].data = data.recentMonthMap.labels;
				this.options2.series = data.recentMonthMap.datasets.map(value => {
					return {
						name: value.label,
						type: 'line',
						barGap: 0,
						label: labelOption,
						emphasis: {
							focus: 'series'
						},
						data: value.data
					}
				})

				// 上次登录时间
				this.lastLoginTime = data.lastLoginTime;

				// 活动日志
				this.todoList = data.logList;
			}
		},

		filters: {
			formatDate: function(value) {
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
			}
		}

	};
</script>


<style scoped>
	.el-row {
		margin-bottom: 20px;
	}

	.grid-content {
		display: flex;
		align-items: center;
		height: 100px;
	}

	.grid-cont-right {
		flex: 1;
		text-align: center;
		font-size: 14px;
		color: #999;
	}

	.grid-num {
		font-size: 30px;
		font-weight: bold;
	}

	.grid-con-icon {
		font-size: 50px;
		width: 100px;
		height: 100px;
		text-align: center;
		line-height: 100px;
		color: #fff;
	}

	.grid-con-1 .grid-con-icon {
		background: rgb(45, 140, 240);
	}

	.grid-con-1 .grid-num {
		color: rgb(45, 140, 240);
	}

	.grid-con-2 .grid-con-icon {
		background: rgb(100, 213, 114);
	}

	.grid-con-2 .grid-num {
		color: rgb(45, 140, 240);
	}

	.grid-con-3 .grid-con-icon {
		background: rgb(242, 94, 67);
	}

	.grid-con-3 .grid-num {
		color: rgb(242, 94, 67);
	}

	.user-info {
		display: flex;
		align-items: center;
		padding-bottom: 20px;
		border-bottom: 2px solid #ccc;
		margin-bottom: 20px;
	}

	.user-avator {
		width: 120px;
		height: 120px;
		border-radius: 50%;
	}

	.user-info-cont {
		padding-left: 50px;
		flex: 1;
		font-size: 14px;
		color: #999;
	}

	.user-info-cont div:first-child {
		font-size: 30px;
		color: #222;
	}

	.user-info-list {
		font-size: 14px;
		color: #999;
		line-height: 25px;
	}

	.user-info-list span {
		margin-left: 70px;
	}

	.mgb20 {
		margin-bottom: 20px;
	}

	.todo-item {
		font-size: 14px;
	}

	.todo-item-del {
		text-decoration: line-through;
		color: #999;
	}

	.schart {
		width: 100%;
		height: 300px;
	}
</style>

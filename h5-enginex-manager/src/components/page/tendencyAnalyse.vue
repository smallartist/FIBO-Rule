<style>
	.tendencyAna{
		height: 100%;
		overflow: scroll;
	}
	.tendencyAna::-webkit-scrollbar{
		display: none;
	}
	.tendencyAna_header{
	display: flex;
	justify-content: space-between;
}
.tendAna_chartHome{
	
}
.tendAna_chartHome>div{
	display: flex;
	justify-content: space-between;
}
.tendAna_chartHome>div>div{
	background-color: #fff;
	width: 49%;
	border-radius: 10px;
}
.tendencyAna_summaryHome{
	display: flex;
	justify-content: space-between;
	align-items: center;
}
.tendencyAna_summaryHome>div{
	display: flex;
	align-items: center;
}
.tendencyAna_summaryTitle{
	/* font-weight: bold; */
	    height: 42px;
	    line-height: 19px;
	    background: #4d9900;
	    color: #fff;
	    padding: 2px 8px;
	    border-radius: 4px;
	    font-size: 14px;
	    text-align: center;
}
.tendencyAna_summaryCont{
	display: flex;
	align-items: center;
	font-size: 14px;
}
.tendencyAna_summaryCont>div{
	display: flex;
	flex-direction: column;
	align-items: center;
	margin-left: 10px;
}
.tendencyAna_summaryCont>div>div{
	display: flex;
	align-items: center;
}
</style>

<template>
	<div class="tendencyAna">
		<div class="tendencyAna_header">
			<div>
				
				<el-select v-model="engineId" :loading="enginelist.length==0" style="margin-right: 20px;" filterable placeholder="请选择引擎"
				 @change="getEngVersion">
					<el-option v-for="item in enginelist" :key="item.id" :label="item.name" :value="item.id">
					</el-option>
				</el-select>
				<el-select v-model="versionId" :loading="versionList.length==0" style="margin-right: 20px;" filterable placeholder="请选择版本"
				 @change="getData">
					<el-option v-for="item in versionList" :key="item.id" :label="item.label" :value="item.id">
					</el-option>
				</el-select>
				<el-date-picker v-model="time" type="daterange" align="right" unlink-panels :clearable="false" range-separator="至" start-placeholder="开始日期"
				 end-placeholder="结束日期" :picker-options="pickerOptions" @change="getData">
				</el-date-picker>
			</div>
			<div>
				<!-- <el-button>选择模板</el-button>
				<el-button>保存模板</el-button>
				<el-button>复制模板</el-button> -->
			</div>
		</div>

		<p v-if="!option1.series[0].data.length&&!option2PutList.length&&!option3PutList.length&&!option4PutList.length&&!option5PutList.length&&!option6PutList.length&&!engineSummary"
		 style="text-align: center;font-size: 100px;color: #d0d0d0;margin-top: 50px;user-select: none;">
			请选择引擎信息
		</p>
		<el-row :gutter="20" style="margin-top: 20px;">
			<el-col :span="24">
				<el-card shadow="hover" v-if="engineSummary">
					<div class="tendencyAna_summaryHome">

						<div>
							<div class="tendencyAna_summaryTitle">
								调用<br>次数
							</div>
							<div class="tendencyAna_summaryCont">
								<div>
									<p>昨日</p>
									<p>{{engineSummary.engine_call.yesterdayData}}</p>
								</div>
								<div>
									<p>今日</p>
									<p>{{engineSummary.engine_call.todayData}}</p>
								</div>
								<div>
									<p>今日预计</p>
									<div>
										<p :style="{color:engineSummary.engine_call.growthRate>1?'#0f0':'#f00'}">{{Math.abs(engineSummary.engine_call.growthRate*100-100)}}%
											<i :class="engineSummary.engine_call.growthRate>1?'el-icon-top':'el-icon-bottom'"></i></p>
										<p>{{engineSummary.engine_call.predictData}}</p>
									</div>
								</div>
							</div>
						</div>
						<div>
							<div class="tendencyAna_summaryTitle">
								决策<br>结果
							</div>
							<div class="tendencyAna_summaryCont">
								<div>
									<p>昨日</p>
									<p>{{engineSummary.decision_result.yesterdayData}}</p>
								</div>
								<div>
									<p>今日</p>
									<p>{{engineSummary.decision_result.todayData}}</p>
								</div>
								<div>
									<p>今日预计</p>
									<div>
										<p :style="{color:engineSummary.decision_result.growthRate>1?'#0f0':'#f00'}">{{Math.abs(engineSummary.decision_result.growthRate*100-100)}}%
											<i :class="engineSummary.decision_result.growthRate>1?'el-icon-top':'el-icon-bottom'"></i></p>
										<p>{{engineSummary.decision_result.predictData}}</p>
									</div>
								</div>
							</div>
						</div>
						<div>
							<div class="tendencyAna_summaryTitle">
								规则<br>命中
							</div>
							<div class="tendencyAna_summaryCont">
								<div>
									<p>昨日</p>
									<p>{{engineSummary.rule_hit.yesterdayData}}</p>
								</div>
								<div>
									<p>今日</p>
									<p>{{engineSummary.rule_hit.todayData}}</p>
								</div>
								<div>
									<p>今日预计</p>
									<div>
										<p :style="{color:engineSummary.rule_hit.growthRate>1?'#0f0':'#f00'}">{{Math.abs(engineSummary.rule_hit.growthRate*100-100)}}%
											<i :class="engineSummary.rule_hit.growthRate>1?'el-icon-top':'el-icon-bottom'"></i></p>
										<p>{{engineSummary.rule_hit.predictData}}</p>
									</div>
								</div>
							</div>
						</div>
						<div>
							<div class="tendencyAna_summaryTitle">
								节点<br>命中
							</div>
							<div class="tendencyAna_summaryCont">
								<div>
									<p>昨日</p>
									<p>{{engineSummary.node_hit.yesterdayData}}</p>
								</div>
								<div>
									<p>今日</p>
									<p>{{engineSummary.node_hit.todayData}}</p>
								</div>
								<div>
									<p>今日预计</p>
									<div>
										<p :style="{color:engineSummary.node_hit.growthRate>1?'#0f0':'#f00'}">{{Math.abs(engineSummary.node_hit.growthRate*100-100)}}%
											<i :class="engineSummary.node_hit.growthRate>1?'el-icon-top':'el-icon-bottom'"></i></p>
										<p>{{engineSummary.node_hit.predictData}}</p>
									</div>
								</div>
							</div>
						</div>
						<div>
							<div class="tendencyAna_summaryTitle">
								评分卡<br>命中
							</div>
							<div class="tendencyAna_summaryCont">
								<div>
									<p>昨日</p>
									<p>{{engineSummary.scorecard.yesterdayData}}</p>
								</div>
								<div>
									<p>今日</p>
									<p>{{engineSummary.scorecard.todayData}}</p>
								</div>
								<div>
									<p>今日预计</p>
									<div>
										<p :style="{color:engineSummary.scorecard.growthRate>1?'#0f0':'#f00'}">{{Math.abs(engineSummary.scorecard.growthRate*100-100)}}%
											<i :class="engineSummary.scorecard.growthRate>1?'el-icon-top':'el-icon-bottom'"></i></p>
										<p>{{engineSummary.scorecard.predictData}}</p>
									</div>
								</div>
							</div>
						</div>
						<div>
							<div class="tendencyAna_summaryTitle">
								决策表<br>命中
							</div>
							<div class="tendencyAna_summaryCont">
								<div>
									<p>昨日</p>
									<p>{{engineSummary.decision_tables.yesterdayData}}</p>
								</div>
								<div>
									<p>今日</p>
									<p>{{engineSummary.decision_tables.todayData}}</p>
								</div>
								<div>
									<p>今日预计</p>
									<div>
										<p :style="{color:engineSummary.decision_tables.growthRate>1?'#0f0':'#f00'}">{{Math.abs(engineSummary.decision_tables.growthRate*100-100)}}%
											<i :class="engineSummary.decision_tables.growthRate>1?'el-icon-top':'el-icon-bottom'"></i></p>
										<p>{{engineSummary.decision_tables.predictData}}</p>
									</div>
								</div>
							</div>
						</div>
					</div>
				</el-card>
			</el-col>
		</el-row>
		<el-row :gutter="20" style="margin-top: 20px;">
			<el-col :span="12">
				<el-card shadow="hover" v-if="option1.series[0].data.length">
					<charts sid="chart1" :option="option1" :height="chart1Size[0]" :width="chart1Size[1]"></charts>
				</el-card>
			</el-col>
			<el-col :span="12">
				<el-card shadow="hover" style="position: relative;" v-if="option1.series[0].data.length">
					<el-table :data="option1TableData" style="width: 100%;height: 300px;" :default-sort="{prop:'resultCount',order:'descending'}">
						<el-table-column prop="result" label="调用时间" width="180">
						</el-table-column>
						<el-table-column prop="resultCount" label="值" sortable>
						</el-table-column>
					</el-table>
				</el-card>
			</el-col>
		</el-row>
		<el-row :gutter="20" style="margin-top: 20px;">
			<el-col :span="12">
				<el-card shadow="hover" v-if="option2PutList.length">
					<el-select v-model="option2Put" size="mini" multiple collapse-tags value-key="id" :loading="option2PutList.length==0"
					 style="margin-left: 20px;position: absolute;" filterable placeholder="请选择决策结果" :key="num">
						<el-option v-for="item in option2PutList" :key="item[0].id" :label="item[0].result" :value="item">
						</el-option>
					</el-select>
					<charts sid="a2" :option="option2" height="300px" width="750px"></charts>
				</el-card>
			</el-col>
			<el-col :span="12">
				<el-card shadow="hover" style="position: relative;" v-if="option2PutList.length">
					<el-table :data="option2TableData" style="width: 100%;height: 300px;" :default-sort="{prop:'resultCount',order:'descending'}">
						<el-table-column prop="result" label="结果" width="180">
						</el-table-column>
						<el-table-column prop="resultCount" label="总计值" sortable>
						</el-table-column>
					</el-table>
				</el-card>
			</el-col>
		</el-row>
		<el-row :gutter="20" style="margin-top: 20px;">
			<el-col :span="12">
				<el-card shadow="hover" style="position: relative;" v-if="option3PutList.length">
					<el-select v-model="option3Put" size="mini" multiple collapse-tags value-key="id" :loading="option3PutList.length==0"
					 style="margin-left: 20px;position: absolute;" filterable placeholder="请选择规则及版本" :key="num">
						<el-option v-for="item in option3PutList" :key="item[0].id" :label="item[0].ruleName+':'+item[0].ruleVersionCode"
						 :value="item">
						</el-option>
					</el-select>
					<charts sid="a3" :option="option3" height="300px" width="750px"></charts>
				</el-card>
			</el-col>
			<el-col :span="12">
				<el-card shadow="hover" v-if="option3PutList.length">
					<el-table :data="option3TableData" style="width: 100%;height: 300px;" :default-sort="{prop:'resultCount',order:'descending'}">
						<el-table-column prop="result" label="命中" width="180">
						</el-table-column>
						<el-table-column prop="resultCount" label="总计值" sortable>
						</el-table-column>
					</el-table>
				</el-card>
			</el-col>
		</el-row>
		<el-row :gutter="20" style="margin-top: 20px;">
			<el-col :span="12">
				<el-card shadow="hover" style="position: relative;" v-if="option4PutList.length">
					<el-select v-model="option4Put" size="mini" multiple collapse-tags value-key="id" :loading="option4PutList.length==0"
					 style="margin-left: 20px;position: absolute;" filterable placeholder="请选择节点" :key="num">
						<el-option v-for="item in option4PutList" :key="item[0].id" :label="item[0].nodeName" :value="item">
						</el-option>
					</el-select>
					<charts sid="a4" :option="option4" height="300px" width="750px"></charts>
				</el-card>
			</el-col>
			<el-col :span="12">
				<el-card shadow="hover" v-if="option4PutList.length">
					<el-table :data="option4TableData" style="width: 100%;height: 300px;" :default-sort="{prop:'resultCount',order:'descending'}">
						<el-table-column prop="result" label="命中" width="180">
						</el-table-column>
						<el-table-column prop="resultCount" label="总计值" sortable>
						</el-table-column>
					</el-table>
				</el-card>
			</el-col>
		</el-row>
		<el-row :gutter="20" style="margin-top: 20px;">
			<el-col :span="12">
				<el-card shadow="hover" style="position: relative;" v-if="option5PutList.length">
					<el-select v-model="option5Put" size="mini" :loading="option5PutList.length==0" style="margin-left: 20px;position: absolute;z-index: 5;"
					 filterable value-key="[0][0].id" placeholder="请选择评分卡节点" :key="num">
						<el-option v-for="item in option5PutList" :key="item[0][0].id" :label="item[0][0].scorecardName" :value="item">
						</el-option>
					</el-select>
					<charts sid="a5" :option="option5" height="300px" width="750px"></charts>
				</el-card>
			</el-col>
			<el-col :span="12">
				<el-card shadow="hover" style="position: relative;" v-if="option5PutList.length">
					<el-table :data="option5TableData" style="width: 100%;height: 300px;" :default-sort="{prop:'resultCount',order:'descending'}">
						<el-table-column prop="result" label="命中" width="180">
						</el-table-column>
						<el-table-column prop="resultCount" label="总计值" sortable>
						</el-table-column>
					</el-table>
				</el-card>
			</el-col>
		</el-row>
		<el-row :gutter="20" style="margin-top: 20px;">
			<el-col :span="12">
				<el-card shadow="hover" style="position: relative;" v-if="option6PutList.length">
					<el-select v-model="option6Put" size="mini" :loading="option6PutList.length==0" style="margin-left: 20px;position: absolute;z-index: 5;"
					 filterable value-key="[0][0].id" placeholder="请选择入参" :key="num">
						<el-option v-for="item in option6PutList" :key="item[0][0].id" :label="item[0][0].decisonTablesName" :value="item">
						</el-option>
					</el-select>
					<charts sid="a6" :option="option6" height="300px" width="750px"></charts>
				</el-card>
			</el-col>
			<el-col :span="12">
				<el-card shadow="hover" style="position: relative;" v-if="option6PutList.length">

					<el-table :data="option6TableData" style="width: 100%;height: 300px;" :default-sort="{prop:'resultCount',order:'descending'}">
						<el-table-column prop="result" label="命中" width="180">
						</el-table-column>
						<el-table-column prop="resultCount" label="总计值" sortable>
						</el-table-column>
					</el-table>

				</el-card>
			</el-col>
		</el-row>
	</div>
</template>

<script>
	import {
		getTendencyList,
		getEngineList,
		getversion,
		getAnalyseData,
		getAnalyseSummary
	} from '@/api/index.js'
	import charts from '@/components/common/charts.vue'
	export default {
		name: "tendencyAnalyse",
		components: {
			charts
		},
		created() {
			this.option2 = JSON.parse(JSON.stringify(this.option))
			this.option2.title.text = '决策结果'
			this.option3 = JSON.parse(JSON.stringify(this.option))
			this.option3.title.text = '规则命中'
			this.option4 = JSON.parse(JSON.stringify(this.option))
			this.option4.title.text = '节点命中'
			this.option5 = JSON.parse(JSON.stringify(this.option))
			this.option5.title.text = '评分卡命中'
			this.option6 = JSON.parse(JSON.stringify(this.option))
			this.option6.title.text = '决策表命中'
			this.time = [new Date().getTime() - 3600 * 1000 * 24 * 7, new Date()]
			getEngineList({
				pageNo: 1,
				pageSize: 999999
			}).then(res => {
				this.enginelist = res.data.enginelist
			})



		},
		data() {
			return {
				engineSummary: null,
				animationDuration: 500,
				num: 0,
				option1TableData: [],
				option2TableData: [],
				option3TableData: [],
				option4TableData: [],
				option5TableData: [],
				option6TableData: [],
				xAx: [],
				option6Put: [],
				option6PutList: [],
				option5Put: [],
				option5PutList: [],
				option4Put: [],
				option4PutList: [],
				option3Put: [],
				option3PutList: [],
				option2Put: [],
				option2PutList: [],
				engineId: '',
				enginelist: [],
				chart1Size: ['300px', '750px'],
				option1: {
					title: {
						text: '调用次数',
						left: 'center'
					},
					xAxis: {
						type: 'category',
						data: []
					},
					yAxis: {
						type: 'value'
					},
					tooltip: {
						trigger: 'item'
						// trigger: 'axis'
					},
					grid: {
						left: '50%'
					},
					legend: {
						orient: 'vertical',
						left: 'left',
						type: 'scroll'
					},
					series: [{
						right: '50%',
						type: 'pie',
						radius: '50%',
						data: [],
						emphasis: {
							itemStyle: {
								shadowBlur: 10,
								shadowOffsetX: 0,
								shadowColor: 'rgba(0, 0, 0, 0.5)'
							}
						}
					}, {
						data: [],
						type: 'line',
						// smooth: true
					}]
				},
				option: {
					title: {
						text: '决策结果',
						left: 'center'
					},
					xAxis: {
						type: 'category',
						data: []
					},
					yAxis: {
						type: 'value'
					},
					toolbox: {
						feature: {
							magicType: {
								type: ['line', 'bar']
							}
						}
					},
					tooltip: {
						// trigger: 'item'
						trigger: 'axis',
					},
					legend: {
						type: 'scroll',
						orient: 'vertical',
						left: 'left',
						padding: [40, 0, 0, 0],
					},
					series: [{
						data: [],
						// type: 'line',
						// smooth: true
					}]
				},
				option2: {},
				option3: {},
				option4: {},
				option5: {},
				option6: {},
				FieldList: [],
				dimeList: [{
						label: '调用次数',
						value: '1'
					},
					{
						label: '决策结果',
						value: '2'
					},
					{
						label: '命中规则',
						value: '3'
					},
					{
						label: '决策节点',
						value: '4'
					},
				],
				dimeStatistic: [],
				// data: [],
				time: [],
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
				versionList: [],
				versionId: '',
			}
		},
		methods: {
			creat(){
				this.option1TableData= []
				this.option2TableData= []
				this.option3TableData= []
				this.option4TableData= []
				this.option5TableData= []
				this.option6TableData= []
				this.option6Put= []
				this.option6PutList= []
				this.option5Put= []
				this.option5PutList= []
				this.option4Put= []
				this.option4PutList= []
				this.option3Put= []
				this.option3PutList= []
				this.option2Put= []
				this.option2PutList= []
				this.option2= {}
				this.option3= {}
				this.option4= {}
				this.option5= {}
				this.option6= {}
				// this.xAx = []
				this.option1={
					title: {
						text: '调用次数',
						left: 'center'
					},
					xAxis: {
						type: 'category',
						data: []
					},
					yAxis: {
						type: 'value'
					},
					tooltip: {
						trigger: 'item'
						// trigger: 'axis'
					},
					grid: {
						left: '50%'
					},
					legend: {
						orient: 'vertical',
						left: 'left',
						type: 'scroll'
					},
					series: [{
						right: '50%',
						type: 'pie',
						radius: '50%',
						data: [],
						emphasis: {
							itemStyle: {
								shadowBlur: 10,
								shadowOffsetX: 0,
								shadowColor: 'rgba(0, 0, 0, 0.5)'
							}
						}
					}, {
						data: [],
						type: 'line',
						// smooth: true
					}]
				}
				
				
				this.option2 = JSON.parse(JSON.stringify(this.option))
				this.option2.title.text = '决策结果'
				this.option3 = JSON.parse(JSON.stringify(this.option))
				this.option3.title.text = '规则命中'
				this.option4 = JSON.parse(JSON.stringify(this.option))
				this.option4.title.text = '节点命中'
				this.option5 = JSON.parse(JSON.stringify(this.option))
				this.option5.title.text = '评分卡命中'
				this.option6 = JSON.parse(JSON.stringify(this.option))
				this.option6.title.text = '决策表命中'
				
			},
			getEngVersion(e) {
				this.versionList = []
				this.versionId = ''
				getversion(e).then(res => {
					if (res.status == "1") {
						this.versionList = res.data.map((value, index) => {
							let obj = {
								id: value.engineVersion.versionId,
								label: 'V:' + index
							}
							return obj
						})
					}
				})
			},
			getXAx(date1, date2) {
				// console.log(this.intervalTime(date1,date2))
				let num = this.intervalTime(date1, date2)
				let arr = []
				for (let i = 0; i <= num; i++) {
					arr.push(this.formateDate(new Date(date1.getTime() + (i * 1000 * 60 * 60 * 24))))
				}
				return arr


			},
			intervalTime(startTime, endTime) {
				// var timestamp=new Date().getTime(); //计算当前时间戳
				var timestamp = (Date.parse(new Date())) / 1000; //计算当前时间戳 (毫秒级)
				var date1 = ""; //开始时间
				if (timestamp < startTime) {
					date1 = startTime;
				} else {
					date1 = timestamp; //开始时间
				}
				var date2 = endTime; //结束时间
				// var date3 = date2.getTime() - date1.getTime(); //时间差的毫秒数
				var date3 = (date2 - date1) * 1000; //时间差的毫秒数
				//计算出相差天数
				var days = Math.floor(date3 / (24 * 3600 * 1000 * 1000));

				return days
			},
			getSummary(){
				this.engineSummary = null
				getAnalyseSummary({
					versionId: this.versionId,
					engineId: this.engineId
				}).then(res=>{
					this.creatEngineSummary(res)
				})
			},
			getData() {
				if(this.versionId != ""){
					this.getSummary()
				}
				if (this.versionId == "" || this.time.length == 0) {
					return
				}
				
				let start = typeof this.time[0] == 'number' ? new Date(this.time[0]) : this.time[0]
				let end = typeof this.time[1] == 'number' ? new Date(this.time[1]) : this.time[1]
				this.xAx = this.getXAx(start, end)
				start.setHours(0)

				start.setMinutes(0)
				start.setSeconds(0)
				end.setHours(0)
				end.setMinutes(0)
				end.setSeconds(0)
				this.creat()
				getAnalyseData({
					start: start,
					end: end,
					versionId: this.versionId,
					engineId: this.engineId
				}).then(res => {
					console.log(res)
					if (res.status == "1") {
						this.num++
						let arr = []
						
						this.creatOption1(res, arr)
						this.creatOption2(res, arr)
						this.creatOption3(res, arr)
						this.creatOption4(res, arr)
						this.creatOption5(res, arr)
						this.creatOption6(res, arr)

						if (arr.length > 0) {
							this.$notify({
								title: '警告',
								message: `无${arr}数据`,
								type: 'warning'
							});
						}
					}
				})

			},
			creatEngineSummary(res) {

				this.engineSummary = res.data


			},
			creatOption1(res, arr) {
				if (res.data.callCountList.length == 0) {
					arr.push('调用次数')
					return
				}
				this.option1.series[0].data = res.data.callCountList.map(value => {
					return {
						name: this.formateDate(new Date(value.callDate)),
						value: value.callCount
					}
				})
				this.option1.xAxis.data = this.xAx
				this.option1.series[1].data = this.xAx.map(item => {
					let num = 0
					res.data.callCountList.forEach(cont => {
						if (this.formateDate(new Date(cont.callDate)) == item) {
							num = cont.callCount
						}
					})
					return num
				})
				let num = 0
				this.option1.series[1].data.forEach(value => {
					num += Number(value)
				})
				this.option1TableData = res.data.callCountList.map(value => {
					return {
						resultCount: value.callCount,
						result: this.formateDate(new Date(value.callDate))
					}
				})
				this.option1.title.text = `调用次数(共${num}次)`
			},
			creatOption2(res, arr) {
				if (res.data.decisionResultList.length == 0) {
					arr.push('决策结果')
					return
				}
				this.option2PutList = null
				this.option2PutList = res.data.decisionResultList
				this.option2Put = res.data.decisionResultList
				this.option2.xAxis.data = this.xAx
				this.option2TableData = res.data.decisionResultList.map(value => {
					let num = 0
					value.forEach(item => {
						num += item.resultCount
					})
					return {
						resultCount: num,
						result: value[0].result,
					}
				})

			},
			creatOption3(res, arr) {
				if (res.data.ruleList.length == 0) {
					arr.push('规则命中')
					return
				}
				this.option3PutList = res.data.ruleList
				this.option3Put = res.data.ruleList
				this.option3.xAxis.data = this.xAx
				this.option3TableData = res.data.ruleList.map(value => {
					let num = 0
					value.forEach(item => {
						num += item.hitCount
					})
					return {
						resultCount: num,
						result: value[0].ruleName,
					}
				})
			},
			creatOption4(res, arr) {
				if (res.data.engineNodeList.length == 0) {
					arr.push('节点命中')
					return
				}
				this.option4PutList = res.data.engineNodeList
				this.option4Put = res.data.engineNodeList
				this.option4.xAxis.data = this.xAx
				this.option4TableData = res.data.engineNodeList.map(value => {
					let num = 0
					value.forEach(item => {
						num += item.passCount
					})
					return {
						resultCount: num,
						result: value[0].nodeName,
					}
				})
			},
			creatOption5(res, arr) {
				if (res.data.scorecardList.length == 0) {
					arr.push('评分卡命中')
					return
				}
				this.option5PutList = res.data.scorecardList
				this.option5.xAxis.data = this.xAx
			},
			creatOption6(res, arr) {
				if (res.data.decisionTablesList.length == 0) {
					arr.push('决策表命中')
					return
				}
				this.option6PutList = res.data.decisionTablesList
				this.option6.xAxis.data = this.xAx
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
		},
		watch: {
			option2Put: {
				handler: function() {
					console.log(this.option2Put)
					this.option2.series = this.option2Put.map(value => {
						return {
							name: value[0].result,
							data: this.xAx.map(item => {
								let num = 0
								value.forEach(cont => {
									if (this.formateDate(new Date(cont.callDate)) == item) {
										num = cont.resultCount
									}
								})
								return num
							}),
							animationDuration: this.animationDuration,
							type: 'line',
						}
					})
				},
				deep: true
			},
			option3Put: {
				handler: function() {

					this.option3.series = this.option3Put.map(value => {
						return {
							name: value[0].ruleName + ':' + value[0].ruleVersionCode,
							data: this.xAx.map(item => {
								let num = 0
								value.forEach(cont => {
									if (this.formateDate(new Date(cont.callDate)) == item) {
										num = cont.hitCount
									}
								})
								return num
							}),
							animationDuration: this.animationDuration,
							type: 'line',
						}
					})
				},
				deep: true
			},
			option4Put: {
				handler: function() {

					this.option4.series = this.option4Put.map(value => {
						return {
							name: value[0].nodeName,
							data: this.xAx.map(item => {
								let num = 0
								value.forEach(cont => {
									if (this.formateDate(new Date(cont.callDate)) == item) {
										num = cont.passCount
									}
								})
								return num
							}),
							animationDuration: this.animationDuration,
							type: 'line',
						}
					})
				},
				deep: true
			},
			option5Put: {
				handler: function() {
					if (!this.option5Put) {
						return []
					}
					this.option5TableData = this.option5Put.map(value => {
						console.log(value)
						let num = 0
						value.forEach(item => {
							num += item.resultCount
						})
						return {
							resultCount: num,
							result: value[0].result,
						}
					})
					this.option5.series = this.option5Put.map(value => {
						return {
							name: value[0].result,
							data: this.xAx.map(item => {
								let num = 0
								value.forEach(cont => {
									if (this.formateDate(new Date(cont.callDate)) == item) {
										num = cont.resultCount
									}
								})
								return num
							}),
							animationDuration: this.animationDuration,
							type: 'line',
						}
					})
				},
				deep: true
			},
			option6Put: {
				handler: function() {
					if (!this.option6Put) {
						return []
					}
					this.option6TableData = this.option6Put.map(value => {
						let num = 0
						value.forEach(item => {
							num += item.resultCount
						})
						return {
							resultCount: num,
							result: value[0].result,
						}
					})
					this.option6.series = this.option6Put.map(value => {
						return {
							name: value[0].result,
							data: this.xAx.map(item => {
								let num = 0
								value.forEach(cont => {
									if (this.formateDate(new Date(cont.callDate)) == item) {
										num = cont.resultCount
									}
								})
								return num
							}),
							animationDuration: this.animationDuration,
							type: 'line',
						}
					})
				},
				deep: true
			}
		}








	}
</script>

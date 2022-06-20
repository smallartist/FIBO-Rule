<template>
	<div style="height: 100%;overflow: auto;">
		<div style="display: flex;padding: 10px;">
			<el-input v-model="params.eventRequestId" size="mini" placeholder="请输入事件流水id" class="ruleLog_input" @keyup.enter.native="getTable">
			</el-input>
			<el-input v-model="params.businessName" size="mini" placeholder="请输入业务类型名称" class="ruleLog_input" @keyup.enter.native="getTable">
			</el-input>
			<el-input v-model="params.businessChildName" size="mini" placeholder="请输入业务子类型名称" class="ruleLog_input" @keyup.enter.native="getTable">
			</el-input>
			<el-input v-model="params.templateId" size="mini" placeholder="请输入模板ID" class="ruleLog_input" @keyup.enter.native="getTable">
			</el-input>

			<el-date-picker v-model="params.Time" size="mini" style="margin-left: 20px;" type="datetimerange" 
				range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" @change="getTable">
			</el-date-picker>


			<el-button icon="el-icon-search" size="mini" circle @click="getTable" style="margin-left: 20px;">
			</el-button>
			<el-button icon="el-icon-delete" size="mini" @click="reSetParams" circle></el-button>

		</div>


		<el-table :data="tableData" border style="width: 100%" v-loading="loading">
			<el-table-column prop="batchNo" label="批次号" width="150">
			</el-table-column>
			<el-table-column prop="eventRequestId" label=" 事件流水id" width="150">
			</el-table-column>
			<el-table-column prop="eventId" label=" 事件ID" width="150">
			</el-table-column>
			<el-table-column prop="eventName" label=" 事件名称" width="200">
			</el-table-column>
			<el-table-column prop="templateId" label=" 模板ID" width="150">
			
			</el-table-column>
			<el-table-column prop="templateName" label="模板名称" width="250">
			</el-table-column>
			<el-table-column prop="businessName" label=" 业务类型名称" width="150">
			</el-table-column>
			<el-table-column prop="businessChildName" label="业务子类型名称" width="150">
			</el-table-column>
			
			<el-table-column prop="customerName" label="客户姓名" width="100">
			</el-table-column>
			<el-table-column prop="customerMobile" label=" 客户手机号" width="150">
			</el-table-column>
			<el-table-column prop="policyNo" label="保单号" width="100">
			</el-table-column>
			<el-table-column prop="sendPlatform" label="发送平台" width="100">
			</el-table-column>
			<el-table-column prop="callStartTime" label="规则执行开始时间" width="250">
				<template slot-scope="scope">
					{{new Date(scope.row.callStartTime).format('yyyy-MM-dd hh:mm:ss')}}
				</template>
			</el-table-column>
			<el-table-column prop="callTime" label="批次耗时 ms" width="120">
			</el-table-column>
			<el-table-column prop="callStatus" label="执行结果" width="100">
				<template slot-scope="scope">
					{{scope.row.callStatus == '1'?'成功':'失败'}}
				</template>
			</el-table-column>
			<el-table-column prop="ruleLogIds" label="规则执行记录表ids" width="100">
			</el-table-column>
			<el-table-column label="查看" width="100" fixed="right">
				<template slot-scope="scope">
					<el-button icon="el-icon-search" circle @click="look(scope.row.ruleLogIds)"></el-button>
				</template>
			</el-table-column>




		</el-table>




		<div style="padding: 10px;display: flex;justify-content: flex-end;">
			<el-pagination background layout="prev, pager, next" :current-page.sync="pageNo" @current-change="getTable"
				:total="total">
			</el-pagination>
		</div>


		<el-dialog title="查看" :visible.sync="dialogVisible" width="80%" @close="details = []">
			<div v-loading="dialogLoading">
				<el-table :data="details" border style="width: 100%">
					<el-table-column prop="ruleName" label="规则名称">
					</el-table-column>
					<el-table-column prop="ruleCode" label="规则编号">
					</el-table-column>
					<el-table-column prop="ruleType" label="规则类型">
					</el-table-column>
					<el-table-column prop="ruleDescription" label="规则描述">
						<template slot-scope="scope">
							<el-popover placement="top-start" width="500" trigger="hover" :content="scope.row.ruleDescription">
								<div style="white-space: nowrap;overflow: hidden;text-overflow: ellipsis;" slot="reference">
									{{scope.row.ruleDescription}}
								</div>
							</el-popover>
						</template>
					</el-table-column>
					<el-table-column prop="executeSwitch" label="执行开关">
						<template slot-scope="scope">
							{{scope.row.executeSwitch == '1'?'开启':'关闭'}}
						</template>
					</el-table-column>
					<el-table-column prop="validStartTime" label="执行有效期-开始时间">
						<template slot-scope="scope">
							{{scope.row.validStartTime?new Date(scope.row.validStartTime).format('yyyy-MM-dd hh:mm:ss'):''}}
						</template>
					</el-table-column>
					<el-table-column prop="validEndTime" label="执行有效期-结束时间">
						<template slot-scope="scope">
							{{scope.row.validEndTime?new Date(scope.row.validEndTime).format('yyyy-MM-dd hh:mm:ss'):''}}
						</template>
					</el-table-column>
					<el-table-column prop="ruleResult" label="规则输出结果">
						<template slot-scope="scope">
							<el-popover placement="top-start" width="500" trigger="hover" :content="scope.row.ruleResult">
								<div style="white-space: nowrap;overflow: hidden;text-overflow: ellipsis;" slot="reference">
									{{scope.row.ruleResult}}
								</div>
							</el-popover>
						</template>
					</el-table-column>
				</el-table>




			</div>
			<span slot="footer" class="dialog-footer">
				<el-button type="primary" @click="dialogVisible = false">确 定</el-button>
			</span>
		</el-dialog>







	</div>
</template>

<script>
	import {
		getEventLogList,
		getEventLogDetails
	} from '@/api/index.js'
	const paramOr = {
		eventRequestId: '',
		businessName: '',
		businessChildName: '',
		templateId: '',
		Time: [new Date().getTomorrow(0), new Date],
	}

	export default {
		data() {
			return {
				tableData: [],
				params: JSON.parse(JSON.stringify(paramOr)),
				pageNo: 1,
				total: 0,
				loading: false,
				dialogVisible: false,
				dialogLoading: false,
				details: []
			}
		},
		created() {
			this.getTable()
		},
		methods: {
			reSetParams() {
				this.params = JSON.parse(JSON.stringify(paramOr))
				this.params.Time = [new Date().getTomorrow(0), new Date()]
				
			},
			getTable() {
				console.log(this.params)
				if (this.params.eventRequestId == '' && !this.params.Time) {
					this.$message.error('事件流水号、执行时间段”不能同时为空')
					return
				}

				let params = JSON.parse(JSON.stringify(this.params))
				params.startTime = params.Time[0] ? params.Time[0] : null
				params.endTime = params.Time[1] ? params.Time[1] : null

				params.pageNo = this.pageNo
				this.loading = true
				getEventLogList(params).then(res => {

					if (res.status == '1') {
						this.tableData = res.data.list
						this.total = res.data.total
					}
					this.loading = false
				})

			},
			look(ids) {

				this.dialogLoading = true
				this.dialogVisible = true
				getEventLogDetails({
					ruleLogIds: ids
				}).then(res => {
					if (res.status == '1') {
						this.details = res.data
					}
					this.dialogLoading = false
				})

			}
		}

	}
</script>

<style>
	.ruleLog_input {
		width: 200px;
		margin-left: 20px;
	}
	
</style>

<template>
	<div style="height: 100%;overflow: auto;">
		<div style="display: flex;padding: 10px;">
			<el-input v-model="params.ruleName" size="mini" placeholder="请输入规则名称" class="ruleLog_input"
				@keyup.enter.native="getTable"></el-input>
			<el-input v-model="params.ruleCode" size="mini" placeholder="请输入规则编号" class="ruleLog_input"
				@keyup.enter.native="getTable"></el-input>
			<!-- <el-input v-model="params.ruleType" size="mini" placeholder="请输入规则类型" class="ruleLog_input" @keyup.enter.native="getTable">
				
			</el-input> -->
			<el-select v-model="params.ruleType"  size="mini" placeholder="请选择规则类型" class="ruleLog_input" @change="getTable" clearable>
				<el-option v-for="item in ruleTypeList" :key="item.value" :label="item.label" :value="item.value">
				</el-option>
			</el-select>

			<el-input v-model="params.businessName" size="mini" placeholder="请输入业务类型名称" class="ruleLog_input"
				@keyup.enter.native="getTable">
			</el-input>
			<el-input v-model="params.businessChildName" size="mini" placeholder="请输入业务子类型名称" class="ruleLog_input"
				@keyup.enter.native="getTable">
			</el-input>

			<el-date-picker v-model="params.Time" size="mini" style="margin-left: 20px;" type="datetimerange"
				range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" @change="getTable">
			</el-date-picker>


			<el-button icon="el-icon-search" size="mini" circle @click="getTable" style="margin-left: 20px;">
			</el-button>
			<el-button icon="el-icon-delete" size="mini" @click="reSetParams" circle></el-button>

		</div>


		<el-table :data="tableData" border style="width: 100%" v-loading="loading">
			<el-table-column prop="ruleName" label="规则名称" width="250">
			</el-table-column>
			<el-table-column prop="ruleCode" label=" 规则编号" width="230">
			</el-table-column>
			<el-table-column prop="ruleType" label=" 规则类型" width="150">
			</el-table-column>
			<el-table-column prop="businessName" label=" 业务类型名称" width="150">
			</el-table-column>
			<el-table-column prop="businessChildName" label="业务子类型名称" width="150">
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
			<el-table-column prop="executeSwitch" label="执行开关" width="100">
				<template slot-scope="scope">
					{{scope.row.executeSwitch==1?"开启":'关闭'}}
				</template>
			</el-table-column>
			<el-table-column prop="validStartTime" label="执行有效期-开始时间" width="155">
				<template slot-scope="scope">
					{{scope.row.validStartTime?new Date(scope.row.validStartTime).format('yyyy-MM-dd hh:mm:ss'):''}}
				</template>
			</el-table-column>
			<el-table-column prop="validEndTime" label="执行有效期-结束时间" width="155">
				<template slot-scope="scope">
					{{scope.row.validStartTime?new Date(scope.row.validEndTime).format('yyyy-MM-dd hh:mm:ss'):''}}
				</template>
			</el-table-column>
			<el-table-column prop="executeNum" label="执行次数" width="100">
			</el-table-column>


		</el-table>




		<div style="padding: 10px;display: flex;justify-content: flex-end;">
			<el-pagination background layout="prev, pager, next" :current-page.sync="pageNo" @current-change="getTable"
				:total="total">
			</el-pagination>
		</div>









	</div>
</template>

<script>
	import {
		getRuleLogList
	} from '@/api/index.js'
	const paramOr = {
		ruleName: '',
		ruleCode: '',
		ruleType: '',
		businessName: '',
		businessChildName: '',
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
				ruleTypeList: [
					{
						value: "blacklist",
						label: "黑名单"
					},
					{
						value: "intercept",
						label: "拦截规则"
					},
					{
						value: "threshold",
						label: "阈值规则"
					},
					{
						value: "mutex",
						label: "互斥规则"
					},
					{value: "merge",
						label: "合并规则"
					},
					
					
					
					
					
				]
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
				if (this.params.ruleName == '' && this.params.ruleCode == '' && !this.params.Time) {
					this.$message.error('“规则编号、规则名称、规则执行时间段” 不能同时为空')
					return
				}

				let params = JSON.parse(JSON.stringify(this.params))
				params.startTime = params.Time[0] ? params.Time[0] : null
				params.endTime = params.Time[1] ? params.Time[1] : null

				params.pageNo = this.pageNo
				this.loading = true
				getRuleLogList(params).then(res => {

					if (res.status == '1') {
						this.tableData = res.data.list
						this.total = res.data.total
					}
					this.loading = false
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

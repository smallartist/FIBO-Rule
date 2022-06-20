<template>
	<div>
		<el-row style="margin-bottom: 30px;margin-top: 20px;">
			<el-col :span="15">
				<span>&nbsp;</span>
			</el-col>
			<el-col :span="6">

				<div style="display: flex;">
					<el-select v-model="search.applyType" placeholder="请选择" clearable>
						<el-option v-for="item in getObjList(optionList)" :key="item.value" :label="item.label.label" :value="item.value">
						</el-option>
					</el-select>
					
					<el-select v-model="search.applyStatus" placeholder="请选择" clearable style="margin-left: 10px;">
						<el-option v-for="item in getObjList(status)" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
					
					<el-button icon="el-icon-search" circle  style="margin-left: 10px;" @click="getTable"></el-button>
					
				</div>

			</el-col>
		</el-row>

		<!-- 表格显示 -->
		<el-row>
			<el-table border :data="enginelist" style="width: 100%" :cell-style="{padding: '10px'}">
				<el-table-column prop="id" label="ID" align="center">
				</el-table-column>
				<el-table-column prop="applyType" label="类型" align="center" show-overflow-tooltip>

					<template slot-scope="scope">
						{{optionList[scope.row.applyType].label}}
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="提交审批时间" align="center">
					<template slot-scope="scope">
						{{new Date(scope.row.createTime).format('yyyy-MM-dd hh:mm:ss')}}
					</template>
				</el-table-column>
				<el-table-column prop="createUserName" label="提交人" align="center">
				</el-table-column>
				<el-table-column prop="applyStatus" label="状态" align="center">
					<template slot-scope="scope">
						{{status[scope.row.applyStatus]}}
					</template>
				</el-table-column>
				<el-table-column prop="approvalUserName" label="审批人" align="center">
				</el-table-column>
				<el-table-column label="操作" align="center">
					<template slot-scope="scope">
						<div style="display: flex;justify-content: space-around;" class="examineC_button">
							<p>查看详情</p>
						</div>
					</template>
				</el-table-column>

			</el-table>
		</el-row>

		<!-- 分页 -->
		<el-row style="margin: 20px 50px 0 75%;">
			<el-pagination background layout="prev, pager, next" :total="pager.total" :page-size="pager.pageSize"
				:current-page.sync="pager.pageNum" @current-change="handleCurrentChange">
			</el-pagination>
		</el-row>
	</div>
</template>

<script>
	import {
		approvalGetApprovalList,
		approvalUpdateApplyStatus
	} from '@/api/index.js'
	export default {
		name: 'examineCentre',
		data() {
			return {
				status: {
					"-1": '审批取消',
					"0": '待审批',
					"1": '审批通过',
					"2": '审批不通过'
				},
				enginelist: [],
				pager: {
					total: 0,
					pageSize: 10,
					pageNum: 1
				},
				search:{
					applyType:'',
					applyStatus:''
				},
				optionList: {
					jclbbbs: {
						label: '决策流版本部署',
					},
					jcljdxg: {
						label: '决策流节点修改',
					},
					jcljdsc: {
						label: '决策流节点删除',
					},
					jcljdwy: {
						label: '决策流节点位移',
					},
					fzgzbbfb: {
						label: '复杂规则版本发布',
					},
					fzgzbbsc: {
						label: '复杂规则版本删除',
					},
					fzgzbbxg: {
						label: '复杂规则版本修改',
					},
					pfkbbfb: {
						label: '评分卡版本发布',
					},
					pfkbbsc: {
						label: '评分卡版本删除',
					},
					pfkbbxg: {
						label: '评分卡版本修改',
					},
					jcbbbfb: {
						label: '决策表版本发布',
					},
					jcbbbsc: {
						label: '决策表版本删除',
					},
					jcbbbxg: {
						label: '决策表版本修改',
					},
					jcsbbfb: {
						label: '决策树版本发布',
					},
					jcsbbsc: {
						label: '决策树版本删除',
					},
					jcsbbxg: {
						label: '决策树版本修改',
					}

				},

			}
		},
		created() {
			this.getTable()
		},
		methods: {
			getObjList(obj){
				let arr = Object.keys(obj).map(value=>{
					return {
						value:value,
						label:obj[value]
					}
				})
				return arr
				
			},
			handleCurrentChange() {
				this.getTable()
			},
			getTable() {
				approvalGetApprovalList({
					pageNum: this.pager.pageNum,
					pageSize: this.pager.pageSize,
					entity: {
						createUserId: 0,
						applyType:this.search.applyType,
						applyStatus:this.search.applyStatus?+this.search.applyStatus:''
					}
				}).then(res => {
					if (res.status == '1') {
						this.enginelist = res.data.list
						this.pager.total = res.data.total







					}
				})
			}
		}
	}
</script>

<style>
	.examineC_button>p {
		cursor: pointer;
	}

	.examineC_button>p:hover {

		color: #409EFF;
	}
</style>

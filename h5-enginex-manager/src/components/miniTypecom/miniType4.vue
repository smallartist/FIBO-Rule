<template>
	<div v-loading="loading">
		<div style="border-top: 1px solid #ddd;padding: 5px ;">
			<p style="margin-top: 10px; margin-bottom: 10px;font-size: 14px;font-weight: bold;">当前选择的评分卡：</p>
			<div style="margin-bottom: 10px;">
				<p style="color: #409EFF;cursor: pointer;display: inline-block;" @click="showDialog()">{{snapshot.name}}</p>
			</div>
			<div style="border-top: 1px solid #ddd;padding: 5px 0;font-size: 14px;">
				版本：
				<el-select :value="snapshot.versionList[0].versionCode" size='mini' disabled>
				</el-select>
			</div>
		</div>
		<el-dialog
			:title="snapshot.name"
			:visible.sync="dialogVisible"
			width="60%">
			<div class="">
				<div class="sco_cont_home mask-wrapper">
					<div class="SCO_tableHeader">
						<div style="background-color: #409EFF;color: #fff;">维度</div>
						<div style="width: 100px;background-color: #409EFF;color: #fff;">权重</div>
						<div v-for="(value,idx) in tier" style="background-color: #409EFF;color: #fff;" :key="idx">
							指标{{value}}
						</div>
						<div style="border-top: 1px solid #ddd;border-right: 1px solid #ddd;margin-top: -1px;margin-left: 1px;background-color: #409EFF;color: #fff;width: 200px;">
							评分
						</div>
					</div>
					<div style="display: flex;margin-left: 20px;">
						<div style="border-right: 1px solid #ddd;border-left: 1px solid #ddd;border-top: 1px solid #ddd;">
							<div v-for="(value,index) in table" style="min-height: 50px;display: flex;border-bottom: 1px solid #ddd;position: relative;" :key="index">
								<div style="min-width: 300px;display: flex;align-items: center;justify-content: center;position: relative;">
									{{String(value.dimensionName)}}
								</div>
								<div style="width: 100px;borderLeft:1px solid #ddd;display: flex;align-items: center;justify-content: center;flex-shrink: 0;">
									{{String(value.weight)}}
								</div>
								<recursion :data="value" :tier="1" :disabled="true"></recursion>
							</div>
						</div>
					</div>
				</div>
				<div v-if="strategySnapshot" class="strategy-box mask-wrapper">
					<div style="margin-top: 10px;margin-bottom: 20px;">
						<span style="font-size: 14px;margin-left: 20px;margin-right: 10px;color: #409EFF;">计算方式:</span>
						<el-select v-model="strategySnapshot.scoreCalculateType" placeholder="请选择" size="mini">
							<el-option :key="1" label="求和" :value="1"></el-option>
							<el-option :key="2" label="加权求和" :value="2"></el-option>
						</el-select>
					</div>

					<outcontent :outcontent="strategySnapshot.strategyOutputList" type="scorecard" style="margin-top: 20px;margin-left: 50px;">
						<div style="display:flex; align-items: center;">
							<el-select v-model="strategySnapshot.resultFieldEn" placeholder="请选择" style="width: 200px;" clearable>
								<el-option v-for="item in FieldUser" :key="item.id" :label="item.fieldCn" :value="item.fieldEn">
								</el-option>
							</el-select>
							<p style="margin: 10px;">
								=
							</p>
							<el-select value="评分" disabled style="width: 255px;">
							</el-select>
						</div>
					</outcontent>
				</div>
			</div>
			
		</el-dialog>
	</div>
</template>

<script>
	import dataManageRedact from '@/components/common/SCO/SCOManageRedact.vue'
	import {getStrategy} from '@/api/index.js'
	import recursion from '@/components/common/SCO/recursion.vue'
	import outcontent from '@/components/models/outcontent.vue'
	export default{
		components: {
			dataManageRedact,
			recursion,
			outcontent
		},
		props:{
			snapshot:{
				type:String,
				default:'无'
			},
			rowkey:{
				type:String,
				default:""
			}
		},
		data() {
			return {
				loading:false,
				dialogVisible: false,
				table:[],
				max: 0,
				tier:1,
				strategySnapshot:null
			}
		},
		created(){
			
		},
		methods:{
			showDialog(){
				let versionId = this.snapshot.versionList[0].id;
				this.getStrategy({"hbaseRowKey":this.rowkey+versionId},()=>{
					this.dialogVisible = true;	
				});
							
			},
			async getStrategy(param,fn){
				this.loading = true
				const data = await getStrategy(param);
				this.loading = false;
				if(data.status=='1'){
					if (data.data.monitorInfo.snapshot.scorecardDimension.length > 0) {
						this.strategySnapshot = data.data.monitorInfo.snapshot;
						this.table = data.data.monitorInfo.snapshot.scorecardDimension;	
						this.getTier();
						fn&&fn()
					}
				}
				
			},
			each(data, floor) {
				data.forEach(e => {
					e.floor = floor
					if (floor > this.max) {
						this.max = floor
					}
					if (e.children.length > 0) {
						this.each(e.children, floor + 1)
					}
				})
			},
			getTier() {
				this.max = 0
				this.each(this.table, 1)
				this.tier = this.max - 1
			}
		},
		computed:{
			Fielduser() {
				if (this.$store.state.FieldUser != null) {
					this.loading = false
					return this.$store.state.FieldUser.data.fieldList
				} else {
					return []
				}
			}
		}
	}
</script>

<style>
	.sco_cont_home{
		width: 100%;
		/* min-height: 300px; */
		overflow: auto;
		/* max-height: 500px; */
	}
	.SCO_tableHeader{
		border-top: none;
	}
	.strategy-box{
		margin: 20px;
	}
	.el-select .el-input.is-disabled .el-input__inner,
	.el-input.is-disabled .el-input__inner{
		background-color: #fff;
		color: #606266;
	}
	.mask-wrapper{
		position: relative;
		z-index: 3;
	}
	.mask-wrapper::before{
		width: 100%;
		height: 100%;
		content: "";
		position: absolute;
		left: 0;
		top: 0;
		background-color: rgba(0, 0, 0, 0);
		z-index: 4;
	}
</style>

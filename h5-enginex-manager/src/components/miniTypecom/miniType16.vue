<template>
	<div v-loading="loading">
		<div style="border-top: 1px solid #ddd;padding: 5px ;">
			<p style="margin-top: 10px; margin-bottom: 10px;font-size: 14px;font-weight: bold;">当前选择的节点：</p>
			<div style="margin-bottom: 10px;">
				<p style="color: #409EFF;cursor: pointer;display: inline-block;" @click="showDialog()">{{snapshot.name}}</p>
			</div>
			<div style="border-top: 1px solid #ddd;padding: 5px 0;font-size: 14px;">
				版本：
				<el-select :value="snapshot.decisionTablesVersionList[0].versionCode" size='mini' disabled>
				</el-select>
			</div>
		</div>
		<el-dialog
			:title="snapshot.name"
			:visible.sync="dialogVisible"
			width="60%">
			<div class="dec_home mask-wrapper" v-if="leftDetailVo&&topDetailVo&&resultList">
				<!-- 决策主体 -->
				<div class="dec" style="width: 100%;">
					<div class="dec_top">
						<!-- top -->
						<div class="dec_table_header" :style="{width:width,flexShrink:'0'}">
						</div>
						<div class="dec_table_comm">
							<topRecursion :data="topDetailVo" direction="row"></topRecursion>
						</div>
					</div>
					<div class="dec_cont" style="display: flex;">
						<recursion :data="leftDetailVo" ref="left" id="left" style="flex-shrink: 0;flex-grow:0"></recursion>
						<div>
							<div v-for="(value,index) in resultList" :key="index" style="display: flex;">
								<div v-for="(item,inde) in value" :key="inde" class="dec_score" :style="{borderRight:inde==value.length-1?'1px solid #ddd':''}">
									<textInput :text="String(item)" click="click" :center="true" :examine="3"></textInput>
								</div>
							</div>
						</div>
					</div>
				</div>
				<outcontent :outcontent="strategyOutputList" type="decision_tables" style="margin-top: 20px;margin-left: 50px;">
					<div style="display:flex; align-items: center;">
						<el-select v-model="resultFieldEn" filterable placeholder="请选择" style="width: 200px;">
							<el-option v-for="item in FieldUser" :key="item.id" :label="item.fieldCn" :value="item.fieldEn">
							</el-option>
						</el-select>
						<p style="margin: 10px;">
							=
						</p>
						<el-select filterable value="决策结果" disabled style="width: 255px;">
						</el-select>
					</div>
				</outcontent>
			</div>
			
		</el-dialog>
	</div>
</template>

<script>
	import {getStrategy} from '@/api/index.js'
	import outcontent from '@/components/models/outcontent.vue'
	import recursion from '@/components/common/decision/recursion.vue'
	import topRecursion from '@/components/common/decision/topRecursion.vue'
	import textInput from '@/components/common/textInput.vue'
	export default{
		components: {
			outcontent,
			recursion,
			topRecursion,
			textInput
		},
		props:{
			snapshot:{
				type:Object,
				default:{}
			},
			rowkey:{
				type:String,
				default:""
			}
		},
		data() {
			return {
				loading:false,
				dialogVisible:false,
				// snapshotStrategy:{
					leftDetailVo:[],
					topDetailVo:[],
					resultList:[],
				// },
				strategyOutputList:[],
				resultFieldEn:"",
				width: '200px'
			}
		},
		created(){
			 
		},
		methods:{
			showDialog(){
				// let currentVersion =  this.vresionList.find(x=>x.versionCode==this.cardVersion)
				let versionId = this.snapshot.decisionTablesVersionList[0].id?this.snapshot.decisionTablesVersionList[0].id:'';
				this.getStrategy({"hbaseRowKey":this.rowkey+versionId},()=>{
					this.dialogVisible = true;	
				});
							
			},
			async getStrategy(param,fn){
				this.loading = true
				const data = await getStrategy(param);
				this.loading = false;
				if(data.status=='1'){
					let {leftDetailVo,topDetailVo,resultSet} = data.data.monitorInfo.snapshot
					this.leftDetailVo =leftDetailVo
					this.topDetailVo = topDetailVo
					this.resultList = resultSet.resultList
					console.log(this.leftDetailVo,this.topDetailVo,this.resultList)
					this.resultFieldEn = data.data.monitorInfo.snapshot.resultFieldEn;
					this.strategyOutputList = data.data.monitorInfo.snapshot.strategyOutputList
					fn&&fn()
				}
				
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
	.dec_top{		
		display: flex;
	}
	.dec_cont{

	}
	.dec_table_header{
		width: 350px;
		border-top: 1px solid #ddd;
		border-left: 1px solid #ddd;
		border-bottom: 1px solid #ddd;
	}
	.dec_table_comm{
		border-top: 1px solid #ddd;
	}
	.dec_score{
		width: 199px;
		height: 80px;
		text-align: center;
		line-height: 80px;
		border-left: 1px solid #ddd;
		border-bottom: 1px solid #ddd;
		box-sizing: border-box;
	}
	.dec_home{
		/* border-top: 1px solid #ddd; */
		overflow: auto;
		max-height: 53vh;
		width: 100%;
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

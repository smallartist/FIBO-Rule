<template>
	<div v-loading="loading">
		<div style="border-top: 1px solid #ddd;padding: 5px ;">
			<p style="margin-top: 10px; margin-bottom: 10px;font-size: 14px;font-weight: bold;">已选节点：</p>
			<div style="margin-bottom: 10px;" v-for="value in selArr" :key="value.id">		
				<!--  @click="showDialog(value)" -->
				<p style="color: #409EFF;cursor: pointer;display: inline-block;">
					<!-- <i class="el-icon-success" style="margin-right:5px;color:#67C23A;"></i> -->
					<!-- <i class="el-icon-error" style="margin-right:5px;color:#F56C6C;"></i> -->
					{{value.listName}}</p>
			</div>
			<el-pagination small layout="prev, pager, next" :total="selList.length" :page-size="pageSize" @current-change="page=$event" style="margin-top: 10px;">
			</el-pagination>
		</div>
		<add-block-white :dialogVisible="dialogVisible" @closeEvent="dialogVisible=false" :disabled="true" :title="currentStrategy.listName" :dataItem="dataItem"></add-block-white>
	</div>
</template>

<script>
	import {getStrategy} from '@/api/index.js'
	import AddBlockWhite from '@/components/models/addBlockWhite.vue';
	export default{
		components: {
			AddBlockWhite
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
		created(){
			this.init();
		},
		data() {
			return {
				loading:false,
				dialogVisible:false,
				selList:[],
				page:1,
				pageSize:10,
				currentStrategy:{},
				dataItem:{}
			}
		},
		methods:{
			init(){
				// let {listDbs,selectListDbs}= this.snapshot;
				// let selList=[];
				// listDbs.forEach(value => {
				// 	selectListDbs.innerIdList.forEach(v=>{
				// 		if(value.id==v){
				// 			selList.push(value);
				// 		}
				// 	})
				// });
				this.selList=JSON.parse(this.snapshot.snapshot);
			},
			showDialog(val){
				console.log(val)
				this.currentStrategy= val;
				let id = val.id;
				if(id){
					this.getStrategy({"hbaseRowKey":this.rowkey+id},()=>{
						this.dialogVisible = true;	
					});
				}			
			},
			async getStrategy(param,fn){
				this.loading = true
				const data = await getStrategy(param);
				this.loading = false;
				if(data.status=='1'){
					this.dataItem = data.data.monitorInfo.snapshot;						
					fn&&fn()
				}
				
			}
		},
		computed:{
			selArr() {
				if (this.selList) {
					let arr = this.selList.filter((value, index) => {
						return (index >= ((this.page - 1) * this.pageSize) && index < (this.page * this.pageSize))
					})
					return arr
				} else {
					return []
				}
			}
		}
	}
</script>

<style>
</style>

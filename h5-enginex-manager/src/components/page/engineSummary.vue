<style >
	.engSu{
		display: flex;
		border: 1px solid #ccc;
		align-items: center;
		height: 80px;
		margin-top: 20px;
		border-radius: 5px;
		justify-content: space-around;
		font-size: 14px;
		width: 100%;
	}
	.engSu_cont{
		display: flex;
		justify-content: space-around;
		align-items: center;
		border: 1px dashed #ccc;
		height: 50px;
		margin-left: 20px;
		border-radius: 5px;
		transition: all 1s;
		/* border: 1px solid #ddd; */
	}
	.engSu_cont>div:nth-of-type(1){
		background-color: #2db46c;
		height: 50px;
		line-height: 50px;
		padding: 8px;
		display: flex;
		align-items: center;
		border-radius: 5px;
		color: #fff;
	}
	.engSu_day{
		display: flex;
		justify-content: space-around;
		padding-left: 20px;
	}
	.engSu_day>div{
		width: 80px;
		display: flex;
		flex-direction: column;
		align-items: center;
	}
	.engSu_day>div>p:nth-of-type(1){
		margin-bottom: 8px;
	}
	.engSu_name{
		border: 1px solid #ddd;
		width: 150px;
		text-align: center;
		/* background-color: #000000; */
	}
	.engSu_top{
		
		width: 50px;
		border-radius: 50%;
		height: 50px;
		line-height: 50px;
		text-align: center;
		margin-top: 20px;
		margin-left: 20px;
		color: #fff;
		font-size: 26px;
	}
	.engSuFor{
		display: flex;
		align-items: center;
		position: absolute;
		width: 90%;
		margin: 0 auto;
		transition: all 0.3s;
		left: 0;
		right: 0;
	}
	.engSuForHome{
		position: relative;
	}
</style>
<template>
	<div :style="{width:'100%',height:klist.length*120+'px'}" class="engSuForHome">
		<div v-for="(value,index) in klist" :key="value.id" class="engSuFor" :style="{top:index*120+'px'}">
			<div class="engSu">
				<div class="engSu_name">
					{{value.name}}
				</div>
				<div class="engSu_cont">
					<div>
						调用次数
					</div>
					<div class="engSu_day">
						<div>
							<p>昨日</p>
							<p>{{value.call.yesterday}}</p>
						</div>
						<div>
							<p>今日</p>
							<p>{{value.call.today}}</p>
						</div>
						<div>
							<p>今日预计</p>
							<p>{{value.call.todayPredict}}</p>
						</div>
						
					</div>
				</div>
				<div class="engSu_cont">
					<div>
						决策结果
					</div>
					<div  class="engSu_day">
						<div>
							<p>昨日</p>
							<p>{{value.call.yesterday}}</p>
						</div>
						<div>
							<p>今日</p>
							<p>{{value.call.today}}</p>
						</div>
						<div>
							<p>今日预计</p>
							<p>{{value.call.todayPredict}}</p>
						</div>
						
					</div>
				</div>
				<div class="engSu_cont">
					<div>
						命中规则
					</div>
					<div  class="engSu_day">
						<div>
							<p>昨日</p>
							<p>{{value.call.yesterday}}</p>
						</div>
						<div>
							<p>今日</p>
							<p>{{value.call.today}}</p>
						</div>
						<div>
							<p>今日预计</p>
							<p>{{value.call.todayPredict}}</p>
						</div>
					</div>
				</div>
			</div>
			<div class="engSu_top" :style="{backgroundColor:value.top?'#349d00':'#dcdcdc'}" @click="switchTop(value)">
				<i class="el-icon-top"></i>
			</div>
		</div>
		
	</div>
</template>

<script>
	import {getengineSummaryList} from '../../api/index'
	export default{
		name:'engineSummary',
		data(){
			return {
				topKlist:[],
				klist:[]
			}
		},
		created() {
			getengineSummaryList().then(res=>{
				if(res.status=="1"){
					this.klist = res.data.klist
				}
			})
			
		},
		methods:{
			switchTop(item){
				this.klist.forEach((value,index)=>{
					if(value == item){
						this.klist.splice(index,1)
					}
				})
				item.top=!item.top
				if(item.top){
					this.klist.unshift(item)
				}else{
					this.klist.push(item)
				}
				
			},
		}
	}
</script>


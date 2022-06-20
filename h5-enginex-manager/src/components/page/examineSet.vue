<template>
	<div class="examSet">
		<div class="examSet_left">
			<div class="examSet_left_cont">
				<div v-for="value in optionList">
							<p class="examSet_subtitle">{{value.className}}</p>
							<div v-for="item in value.list" class="examSet_cont">
								<div :style="{color:item.status?'#409eff':'#666'}">{{item.label}}</div>
								<div>
									<el-switch v-model="item.status" active-color="#13ce66" inactive-color="#ff4949" @change="change(item)" :active-value="1"
				:inactive-value="0">
									</el-switch>
				
				
								</div>
							</div>
						</div>
			</div>



		</div>

	</div>
</template>

<script>
	import {
		approvalConfigGetApprovalList,
		approvalConfigUpdateApprovalStatus
	} from '@/api/index.js'
	export default {
		data() {
			return {
				optionList: [{
						className: '决策流',
						list: [{
							label: '版本部署',
							name: 'jclbbbs',
							status: 0
						}, {
							label: '节点修改',
							name: 'jcljdxg',
							status: 0
						}, {
							label: '节点删除',
							name: 'jcljdsc',
							status: 0
						}, {
							label: '节点位移',
							name: 'jcljdwy',
							status: 0
						}, ]
					},
					{
						className: '复杂规则集',
						list: [{
							label: '版本发布',
							name: 'fzgzbbfb',
							status: 0
						}, {
							label: '版本删除',
							name: 'fzgzbbsc',
							status: 0
						}, {
							label: '版本修改',
							name: 'fzgzbbxg',
							status: 0
						}]
					},
					{
						className: '评分卡',
						list: [{
							label: '版本发布',
							name: 'pfkbbfb',
							status: 0
						}, {
							label: '版本删除',
							name: 'pfkbbsc',
							status: 0
						}, {
							label: '版本修改',
							name: 'pfkbbxg',
							status: 0
						}]
					},
					{
						className: '决策表',
						list: [{
							label: '版本发布',
							name: 'jcbbbfb',
							status: 0
						}, {
							label: '版本删除',
							name: 'jcbbbsc',
							status: 0
						}, {
							label: '版本修改',
							name: 'jcbbbxg',
							status: 0
						}]
					},
					{
						className: '决策树',
						list: [{
							label: '版本发布',
							name: 'jcsbbfb',
							status: 0
						}, {
							label: '版本删除',
							name: 'jcsbbsc',
							status: 0
						}, {
							label: '版本修改',
							name: 'jcsbbxg',
							status: 0
						}]
					},
				],
			}
		},
		created() {
			approvalConfigGetApprovalList({page:0,pageSize:0,entity:{}}).then(res=>{
				if(res.status=='1'){
					this.optionList.forEach(value=>{
						value.list.forEach(item=>{
							let status = res.data.list.find(x=>x.approvalType ===item.name)
							if(status.approvalStatus===0||status.approvalStatus===1){
								item.status=status.approvalStatus
							}
						})
					})
				}
			})
		},
		methods:{
			change(item){
				approvalConfigUpdateApprovalStatus({
					list:[{
						approvalType:item.name
					}],
					status:item.status?1:0 ,
					
				})
			}
		}









	}
</script>
<style type="text/css">
	.examSet {
		display: flex;
		height: 100%;
	}

	.examSet_left {
		margin: 20px;
		overflow: scroll;
		
	}
	.examSet_left_cont{
		background-color: #fff;
		padding: 20px;
		border-radius: 20px;
	}
	.examSet_left::-webkit-scrollbar,
	.examSet_right::-webkit-scrollbar {
		display: none;
	}

	.examSet_right {
		padding: 20px;
		flex: 1;
		overflow: scroll;
	}

	.examSet_subtitle {
		border: 1px solid #ddd;
		border-top: 0;
		font-size: 18px;
		color: #555;
		padding: 5px;
	}

	/* body .examSet_subtitle:nth-of-type(1){
		border-top:1px solid #ddd;
	} */
	.examSet_cont {
		display: flex;
		align-items: center;
	}

	.examSet_cont>div {
		color: #666;
		padding: 4px 14px;
		border-bottom: 1px solid #ddd;
		border-right: 1px solid #ddd;
	}

	.examSet_cont>div:nth-of-type(1) {
		border-left: 1px solid #ddd;
	}
</style>

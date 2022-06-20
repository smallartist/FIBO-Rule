<template>
	<div>

		<el-drawer title="操作提示" :visible="open" @update:visible="$emit('update:open',false)">
			
			<div v-for="value in data" style="padding-left: 10px;">
				<p style="font-size: 18px;font-weight: bold;">{{value.title}}</p>
				<div v-for="(item,index) in value.cont" style="padding-left: 10px;margin-top: 3px;">
					<p :style="{color:item.type=='limit'?'#f56c6c':'#000'}">{{index+1}}.{{item.text}}</p>
				</div>
			</div>
			
			
			
		</el-drawer>
		
		
	</div>


</template>

<script>
	export default {
		props: {
			open: false
		},
		data() {
			return{
				data:[{
					title:'节点',
					cont:[
						{text:'红色节点以及名单库决策选项统称为策略节点'},
						{text:'紫色节点统称为分支节点'},
						{text:'分支节点后可以有多个节点，但策略节点后只能有一个节点'},
						{text:'拖动节点可以改变位置，双击节点可以编辑节点内容'},
						
					]
				},{
					title:'连线',
					cont:[
						{text:'右键点击节点可以呼出连线，再次右击可以连接下一个节点'},
						{text:'连线不能封闭',type:'limit'},
						{text:'并行节点后只能有策略节点,需要使用分支节点可以先聚合',type:'limit'},
						{text:'多出口节点需要先定义出口并提交才能连线',type:'limit'},
						{text:'多出口节点不能同时编辑和连线',type:'limit'},
						
					]
				},{
					title:'版本',
					cont:[
						{text:'新建和复制版本会生成一个小版本，小版本部署成功后会变成主版本'},
					]
				}]
			}
		}
	}
</script>

<style>
</style>

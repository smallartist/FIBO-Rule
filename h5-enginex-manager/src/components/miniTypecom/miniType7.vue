<template>
	<div>
		<div style="border-top: 1px solid #ddd;padding: 5px ;">
			<div style="height: 80%;overflow: scroll;overflow-x: hidden;" class="mask-wrapper">
				<div v-for="(item,index) in snapshot.nodeSnapshot" :key="index" style="display:flex;align-items: center;margin-top: 10px;">
					<p style="font-size: 12px;margin-right: 5px;">分流{{item.sandbox}}:</p>
					<div style="width: 100px;margin-right: 5px;">
						<el-input v-model="item.proportion" type='number' size="mini" placeholder="%"><i slot="suffix" style="font-size: 15px;">%</i></el-input>
					</div>
					<i class="el-icon-plus" style="color: #409EFF;font-size: 18px;" v-show="index>0&&index==snapshot.nodeSnapshot.length-1"></i>
					<i class="el-icon-close" style="color: #ff5500;font-size: 18px;" v-show="index>1"></i>
				</div>
			</div>
		</div>
		
	</div>
</template>

<script>
	export default{
		components: {
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
			}
		},
		methods:{
			
		},
	}
</script>

<style>
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

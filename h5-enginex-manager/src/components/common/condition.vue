<template>
	<div>	
	 <div>
		<div>
			+
		</div> 
		<div>
			X
		</div>
		<div>
			 <el-select v-model="value" placeholder="请选择">
			    <el-option
			      v-for="item in inp1"
			      :key="item.value"
			      :label="item.label"
			      :value="item.value">
			    </el-option>
			  </el-select>
		</div>
		<div>
			<input type="text">
		</div>
		<div>
			<input type="text">
		</div>
		<div>
			<input type="text">
		</div>
	 </div>
	
	
	
	</div>
</template>

<script>
	export default{
		porps:{
			
		},
		data(){
			return {
				value:1,
			}
		}
	}
	
	
	
	
	
	
</script>

<style>
</style>

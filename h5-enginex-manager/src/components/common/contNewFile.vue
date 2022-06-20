<template>
	<div class="cont_new_file">
		<div v-if="!newf" @click="newFile"><i class="el-icon-folder-add" @click="newFile" style="margin-right: 10px;"></i>新建文件夹</div>
		<div v-else style="padding: 5px;box-sizing:border-box;">
			<div style="display: flex;align-items: center;">
				<i class="el-icon-folder-add" @click="newFile" style="margin-right: 10px;"></i>
				<el-input :value="value" @input="$emit('input',$event)" placeholder="请输入新文件夹名字,最长20个字符" size="small" @keyup.enter.native="newFileSure"></el-input>
			</div>
			<el-button style="margin-left: 60px;margin-top: 10px;" type="danger" icon="el-icon-close" circle size="mini"
			 @click="newFileClose"></el-button>
			<el-button type="success" icon="el-icon-check" circle size="mini" @click="newFileSure"></el-button>
		</div>
	</div>
</template>

<script>
	export default{
		props:{
			newf:{
				type:Boolean,
				default:false
			},
			value:{
				type:String,
				default:''
			}
		},
		methods:{
			newFile(){
				this.$emit('newFile')
			},
			newFileSure(){
				this.$emit('newFileSure')
			},
			newFileClose(){
				this.$emit('input','')
				this.$emit('update:newf',false)
				// newf=false;
			}
		}
	}
</script>

<style>
</style>

<template>
	<div>
		<div v-for="item in data">
			<file :class="item.id===currid?'file_select':''" @curr="curr" :item="item"
			 @RenameClose="RenameClose" @updatafilelist="updatafilelist" @fileRight="fileRight"></file>
			 
			<div v-for="value in item.children">
				
				<file :class="value.id===currid?'file_select':''" @curr="curr" :item="value"
				 @RenameClose="RenameClose" @updatafilelist="updatafilelist" @fileRight="fileRight"></file>
				
				<div v-for="cont in value.children">
					
					<file :class="cont.id===currid?'file_select':''" @curr="curr" :item="cont"
					 @RenameClose="RenameClose" @updatafilelist="updatafilelist" @fileRight="fileRight"></file>
					
					<div v-for="cont1 in cont.children">
						
						<file :class="cont1.id===currid?'file_select':''" @curr="curr" :item="cont1"
						 @RenameClose="RenameClose" @updatafilelist="updatafilelist" @fileRight="fileRight"></file>
						<div v-for="cont2 in cont1.children">
							
							<file :class="cont2.id===currid?'file_select':''" @curr="curr" :item="cont2"
							 @RenameClose="RenameClose" @updatafilelist="updatafilelist" @fileRight="fileRight"></file>
							<div v-for="cont3 in cont2.children">
								
								<file :class="cont3.id===currid?'file_select':''" @curr="curr" :item="cont3"
								 @RenameClose="RenameClose" @updatafilelist="updatafilelist" @fileRight="fileRight"></file>
								
								
								
							</div>
							
							
						</div>
						
						
					</div>
					
				</div>
				
				
			</div>
			
			
			
			
			
		</div>
		<div class="fileHint" :style="{top:tempHintTop+'px',left:tempHintLeft+'px',display:tempHintTop&&tempHintLeft?'block':'none'}">
			<p>
				<el-button type="text" size="mini" @click="RenameFun">重命名</el-button>
			</p>
			<p>
				<el-button type="text" size="mini" @click="delectFun">删除</el-button>
			</p>
			<p>
				<el-button type="text" size="mini" @click="tempHintLeft=null;tempHintTop=null">取消</el-button>
			</p>
		</div>


	</div>
</template>

<script>
	import file from './file.vue'
	export default {
		components: {
			file
		},
		data(){
			return {
				tempHintTop : null,
				tempHintLeft :null,
				tempId:null
			}
		},
		props: {
			data: {
				type: Array,
				default () {
					return []
				}
			},
			currid:{
				type:Number,
				default : 99999999
			}
		},
		created() {
			setTimeout(() => {
				console.log(this.data)
			}, 1000)
		},
		methods: {
			curr(e) {
				this.tempHintLeft = null
				this.tempHintTop = null
				this.tempId = null
				this.$emit('curr',e)
			},
			RenameClose() {
				this.$emit('RenameClose',this.tempId )
			},
			updatafilelist(e) {
				this.$emit('updatafilelist',e)
			},
			fileRight(e){
				this.tempHintLeft = e.e.x
				this.tempHintTop = e.e.y
				this.tempId = e.item.id
			},
			RenameFun(){
				this.$emit('RenameFun',this.tempId )
				this.tempHintTop = null,
				this.tempHintLeft =null,
				this.tempId=null
			},
			delectFun(){
				this.$confirm('确定删除？', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$emit('delectFun',this.tempId )
					this.tempHintTop = null,
					this.tempHintLeft =null,
					this.tempId=null
				}).catch(() => {
					this.$message({
						type: 'info',
						message: '已取消删除'
					});
					this.tempHintTop = null
					this.tempHintLeft = null
				});
				
			}
		}


	}
</script>

<style>
</style>

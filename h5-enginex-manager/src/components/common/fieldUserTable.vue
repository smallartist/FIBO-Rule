<style>
	.fieldUserTable {
		background-color: #fff;
		position: fixed;
		width: 400px;
		height: 400px;
		overflow: hidden;
		z-index: 9;
		border: 6px solid #9bcdff;
		border-radius: 5px;
		box-sizing: border-box;
		
	}
	.FUT_header {
		background-color: #3584d3;
		color: #fff;
		display: flex;
		justify-content: space-between;
		padding: 3px;
		box-sizing: border-box;
		position: absolute;
		top: 0;
		height: 30px;
		width: 100%;
	
	}
	.FUT_table::-webkit-scrollbar {
		display: none;
		/* Chrome Safari */
	}
	.FUT_table{
		height: 93%;
		overflow: scroll;
		overflow-x: hidden;
		-moz-user-select: none;
		-webkit-user-select: none;
		-ms-user-select: none;
		-khtml-user-select: none;
		user-select: none;
		margin-top: 30px;
		
	}
	.FUT_table>p {
		margin-top: 2px;
	}

	.FUT_table>p:hover {
		color: #fff;
		background-color: #9bcdff;
	}
	#fieldUserTabletempcurr{
		background-color: #d9ebff;
	}
</style>

<template>
	<div>

		<div v-show="show" class="fieldUserTable" @mousedown="mousedowm" ref="UserTable" :style="{top:this.tempTop+'px',left:this.tempLeft+'px'}">
			<div class="FUT_header" >
				<p>字段列表</p>
				<p><i class="el-icon-close" @click="$emit('close')"></i></p>
			</div>
			<div class="FUT_table" v-show="fieldUserRemind.length>0" id="FUT_table">
				<p v-for="(item,index) in fieldUserRemind" @dblclick="dbc(item.fieldCn)" :id="index==tempCurIndex?'fieldUserTabletempcurr':''">{{item.fieldCn?item.fieldCn:item.fieldEn}}</p>

			</div>








		</div>
	</div>


</template>

<script>
	export default {
		props: {
			text: {
				type: String,
				default: ""
			},
			fieldUser: {
				type: Array||Boolean,
				default: false
			},
			show:{
				type:Boolean,
				default:false
			}
		},
		data() {
			return {
				tempCurIndex:0,
				tempTop:300,
				tempLeft:800,
				drag:false,
				tempClientX:0,
				tempClientY:0,
				tempOffsetLeft:0,
				tempOffsetTop:0,
				temptext:"",
			}
		},
		created() {
			
		},
		beforeUnmount() {
			
		},
		mounted() {
			window.onmousemove=(e)=>{
				if(this.drag){
					this.tempTop=e.clientY-(this.tempClientY-this.tempOffsetTop)
					this.tempLeft=e.clientX-(this.tempClientX-this.tempOffsetLeft)
				}
			},
			window.onmouseup=()=>{
				this.drag=false
			}
			
		},
		
		computed: {
			fieldUserRemind() {
				if (this.show === true) {
					let arr = []
					
					this.fieldUser.forEach(value => {

						if (value.fieldCn.indexOf(this.text) !== -1) {
							arr.push({ ...value
							})

						}
					})
				
					return arr
				} else {
					return []
				}



			}
		},
		watch: {
			show(){
				// console.log(this.show)
				if(this.show){
					window.onkeydown=(e)=>{
						if(e.key=='ArrowUp'||e.key=='ArrowDown'){
							e.preventDefault()
							var tempcurrDom = document.getElementById('fieldUserTabletempcurr')
							var FUTTable = document.getElementById('FUT_table')
							if(e.key=='ArrowUp'&&this.tempCurIndex>0){
								this.tempCurIndex--
							}
							if(e.key=='ArrowDown'&&this.tempCurIndex<this.fieldUserRemind.length-1){
								this.tempCurIndex++
							}
							FUTTable.scrollTop = tempcurrDom.offsetTop-180
						}
						if(e.key=='Enter'){
							e.preventDefault()
							this.dbc(this.fieldUserRemind[this.tempCurIndex].fieldCn)
						}
						
					}
				}else{
					window.onkeydown=()=>{
						
					}
				}
			},
			text(){
				this.tempCurIndex = 0
				var FUTTable = document.getElementById('FUT_table')
				FUTTable.scrollTop =0
			}
		},
		methods: {
			mousedowm(e){
				this.tempClientX = e.clientX;
				this.tempClientY = e.clientY;
				this.tempOffsetLeft=this.$refs.UserTable.offsetLeft
				this.tempOffsetTop=this.$refs.UserTable.offsetTop
				
				
				
				this.drag=true
				
			},
			dbc(e){
				this.$emit('dbc',e)
			}
		}



	}
</script>

<style>
	.type {
		border-top: 1px solid #ddd;
		padding: 5px;
		box-sizing: border-box;
	}

	.type_header {
		font-size: 12px;
		font-weight: bold;
	}
	
	input[type=number] {  
	    -moz-appearance:textfield;  
	}  
	input[type=number]::-webkit-inner-spin-button,  
	input[type=number]::-webkit-outer-spin-button {  
	    -webkit-appearance: none;  
	    margin: 0;  
	}  
    .type18_submit_home{
		display: flex;
		align-items: center;
		justify-content: center;
		position: absolute;
		left: 80%;
		margin-top: 30px;
		top: 74.5vh;
		z-index: 999;
	}
    .join{
        padding: 0 5px;
    }
    .el-tag{
        margin-right: 10px;
    }

</style>

<template>
	<div class="type4 type" style="height: 100%;">
		<div class="type_header">
			<p>节点类型:并行</p>
		</div>
		<div>
			<div v-for="(item,index) in nodeJson" :key="index" style="display:flex;align-items: center;margin-top: 10px;">
				<p style="font-size: 12px;margin-right: 5px;">分组名称:</p>
                <el-input v-model="item.proportion" placeholder="临时分组" size="mini" style="width:100px;font-size: 12px;"></el-input>
                <span class="join"></span>
				<i class="el-icon-plus" style="color: #409EFF;font-size: 18px;" v-show="index>0&&index==nodeJson.length-1" @click="add(index)"></i>
				<i class="el-icon-close" style="color: #ff5500;font-size: 18px;" v-show="index>1" @click="delect(index)"></i>
			</div>
		</div>
		<div class="type18_submit_home"  v-if="!readOnly">
			<el-button type="primary" round @click="submit">提交</el-button>
		</div>

	</div>
</template>

<script>
	import {
		setType4
	} from '@/api/index.js'
	export default {
		components: {
			setType4
		},
		data() {
			return {
				num: 0,
				nodeJson: ''
			}
		},
		created() {
			console.log(this.nodeNexts)
			this.crea()
		},
		props: {
			data: {
				type: Object,
				default () {
					return {}
				}
			},
			nodeNexts: {
				type: Array,
				default () {
					return []
				}
			},
			readOnly:{
				type: Boolean,
				default :false
			}
		},
		methods: {
			submit() {
				console.log(this.nodeJson)
                this.nodeNexts.forEach((value,index)=>{
                    if(this.nodeJson[index]&& this.nodeJson[index].proportion==value.proportion){
                        this.nodeJson[index].nextNode= value.nextNode
                    }                    
                })
				this.setType({
					"id": this.data.id,
					"initEngineVersionId": String(this.data.Vid),
					"nodeType": 19,
					"nodeName": this.data.text,
					"nodeCode": this.data.nodeCode,
					"nodeOrder": this.data.nodeOrder,
					"nodeX": this.data.x,
					"nodeY": this.data.y,
					"nodeJson": JSON.stringify(this.nodeJson),
					"snapshot": JSON.stringify(this.nodeJson)
				}).then(res => {
					if (res.status == "1") {
                        
						this.$emit('callback', JSON.stringify(this.nodeJson))
						this.$emit('setNextNodes', this.nodeJson)
						this.$message({
							message: '提交成功',
							type: 'success'
						});
					}
				})
			},
			add(index) {
				this.num += 1
				this.nodeJson.splice(index + 1, 0, {
					"proportion": "分组"+(index+2),
					"sandbox": this.num,
					"nextNode": ""
				})
			},
			delect(index) {
                if(this.nodeNexts[index]&&this.nodeNexts[index].nextNode!=''){
                    this.$message({
						message: '该分支有子节点，请清除后提交',
						type: 'warning'
					});
                }else{
                    this.nodeJson.splice(index, 1)
                }				
			},
			crea() {
				if (this.data.nodeJson == null) {
					let obj = [
                        {
                            "proportion": "分组1",
                            "nextNode": "",
							"sandbox": 1,
                        },
                        {
                            "proportion": "分组2",
                            "nextNode": "",
							"sandbox": 2,
                        }
                    ]
					this.$emit('callback', JSON.stringify(obj))
					this.nodeJson = obj
				} else {
                    this.nodeJson = JSON.parse(this.data.nodeJson)
				}
				
				let num = 0
				this.nodeJson.forEach(value => {
					if (parseInt(value.sandbox) > num) {
						num = parseInt(value.sandbox)
					}
				})
				this.num = num
				
			},
			async setType(param) {
				const data = await setType4(param);
				return data
			},
		},
		watch: {
			
			'data.nodeNexts': {
				 handler:function(){
					this.crea()
				},
				deep: true
			}
		}


	}
</script>

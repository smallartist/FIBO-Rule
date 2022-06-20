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
			<p>节点类型:冠军挑战</p>
		</div>
		<div>
			<div v-for="(item,index) in shunt" :key="index" style="display:flex;align-items: center;margin-top: 10px;">
				<!-- <p style="font-size: 12px;margin-right: 5px;">冠军组{{item.sandbox}}:</p> -->
                <el-input v-model="item.proportion" placeholder="临时分支" size="mini" style="width:100px;font-size: 12px;"></el-input>
                <span class="join"></span>
				<!-- <div style="width: 100px;margin-right: 5px;">
					<el-input v-model="item.proportion" type='number' size="mini" placeholder="%"><i slot="suffix" style="font-size: 15px;">%</i></el-input>
				</div> -->
                <el-tag type="warning" effect="dark" size="mini" v-if="index == championid">冠军分支</el-tag>
                <el-tag type="" effect="plain" size="mini" v-if="index != championid" @click="setChampion(index)">指定为冠军分支</el-tag>
				<i class="el-icon-plus" style="color: #409EFF;font-size: 18px;" v-show="index>0&&index==shunt.length-1" @click="add(index)"></i>
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
				shunt: '',
                championid:0
			}
		},
		created() {
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
            setChampion(index){
                this.championid = index;
                this.shunt.forEach((value,idx)=>{
                    if(idx==index){
                        value.champion = 1
                    }else{
                        value.champion = 0
                    }
                })
            },
			submit() {
                this.nodeNexts.forEach((value,index)=>{
                    if(this.shunt[index]&&this.shunt[index].proportion==value.proportion){
                        this.shunt[index].nextNode= value.nextNode
                    }                    
                })
				this.setType({
					"id": this.data.id,
					"initEngineVersionId": String(this.data.Vid),
					"nodeType": 21,
					"nodeName": this.data.text,
					"nodeCode": this.data.nodeCode,
					"nodeOrder": this.data.nodeOrder,
					"nodeX": this.data.x,
					"nodeY": this.data.y,
					"nodeJson": JSON.stringify(this.shunt),
					"snapshot": JSON.stringify(this.shunt),
				}).then(res => {
					if (res.status == "1") {
                       
						this.$emit('callback', JSON.stringify(this.shunt))
						this.$emit('setNextNodes', this.shunt)
						this.$message({
							message: '提交成功',
							type: 'success'
						});
					}
				})
			},
			add(index) {
				this.num += 1
				this.shunt.splice(index + 1, 0, {
					"proportion": "分支"+(index + 2),
					"nextNode": "",
					"sandbox": this.num,
                    "champion":0
				})
			},
			delect(index) {
                if(this.championid==index)this.setChampion(0)
                if(this.nodeNexts[index]&&this.nodeNexts[index].nextNode!=''){
                    this.$message({
						message: '该分支有子节点，请清除后提交',
						type: 'warning'
					});
                }else{
                    this.shunt.splice(index, 1)
                }
				
			},
			crea() {
				if (this.data.nodeJson == null) {
					let obj = [
                        {
                            "proportion": "分支1",
                            "nextNode": "",
                            "champion": 1,
							"sandbox":1
                        },
                        {
                            "proportion": "分支2",
                            "nextNode": "",
                            "champion": 0,
							"sandbox":2
                        }
                    ]
					this.$emit('callback', JSON.stringify(obj))
					this.shunt = obj
				} else {
                    this.shunt = JSON.parse(this.data.nodeJson)
				}
				let num = 0
				this.shunt.forEach((value,idx) => {
					if (value.champion == 1) {
						this.championid = idx;
					}
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
					// this.datajson.conditions=this.nodeNexts
					// console.log(this.nodeNexts)
					// console.log(1)
					this.crea()
				},
				deep: true
			}
		}


	}
</script>

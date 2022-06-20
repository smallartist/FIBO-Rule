<style>
	.type {
        width: 400px;
		border-top: 1px solid #ddd;
		padding: 5px;
		box-sizing: border-box;
	}

	.type_header {
        /* width: 300px; */
		font-size: 12px;
		font-weight: bold;
	}
    .type18_summber_home{
		display: flex;
		align-items: center;
		justify-content: center;
		position: absolute;
		left: 80%;
		margin-top: 30px;
		top: 74.5vh;
		z-index: 999;
	}
    
</style>

<template>
	<div class="type" v-loading="loading">
		<div class="type_header">
			<p>节点类型:远程调用</p>
		</div>
		<!-- <div> -->
            <div style="background-color: #aaa; height: 30px;line-height: 30px; color: #fff;padding: 10px; box-sizing: border-box; margin: 10px 0;display: flex;align-items: center;justify-content: space-between;">
                <p>调用配置</p>
            </div>
            <div class="call-mode-wrapper">
                <!-- <el-input
                    placeholder="请选择接口"
                    v-model="interfaceVal.name"
                    size="mini"
                    @focus='selInterfaceVal'>
                    <i slot="suffix" class="el-input__icon el-icon-arrow-down"></i>
                </el-input> -->
                <el-select v-model="radio" placeholder="选择接口" size="mini" @change="selRadio" style="width:100%;">
                    <el-option
                        v-for="item in flist"
                        :key="item.id"
                        :label="item.name"                
                        :value="item.id">
                    </el-option>
                </el-select>
                <br>
                <el-select v-model="callType" placeholder="选择调用方式" size="mini" style="width:100%;margin-top:10px;">
                    <el-option
                    v-for="item in callModeOptions"
                    :key="item.value"
                    :label="item.label"                
                    :value="item.value">
                    </el-option>
                </el-select>
            </div>            
		<!-- </div>-->
        <div v-if="responseBody.length>0"> 
             <div style="background-color: #aaa; height: 30px;line-height: 30px; color: #fff;padding: 10px; box-sizing: border-box; margin: 10px 0;display: flex;align-items: center;justify-content: space-between;">
                <p>结果配置</p>
            </div>
            <div class="setting-wrapper">
                <el-select v-model="targetResult" placeholder="选择指标" size="mini" style="width:40%">
                    <el-option v-for="(item,index) in Fielduser" :key="index" :label="item.fieldCn" :value="item.id">
				    </el-option>
                </el-select> 
                <div class="line">=</div>
                <el-cascader
                placeholder="选择结果"
                v-model="value"
                :options="responseBody"
                size="mini"
                @change="handleChange"></el-cascader>
            </div>
		</div>
        <div class="type18_summber_home"  v-if="!readOnly">
			<el-button type="primary"  round @click="submit">提交</el-button>
		</div>
        <el-dialog title="选择接口" width="30%" :visible.sync="dialogTableVisible" :modal="true" :append-to-body="true">
			<el-table :data="flist" size="mini">
				<el-table-column label="单选" width="100">
					<template slot-scope="scope">
						<el-radio v-model="radio" :label="scope.row.id">&nbsp;</el-radio>
					</template>
				</el-table-column>
				<el-table-column property="name" label="名称"></el-table-column>
			</el-table>
			<el-pagination layout="prev, pager, next" @current-change="currpage" :current-page="pager.pageNum" :page-count="pager.lastPage">
			</el-pagination>
			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogTableVisible = false">取 消</el-button>
				<el-button type="primary" @click="radioSubmit">确 定</el-button>
			</span>
		</el-dialog>
	</div>
</template>

<script>
	import {getInterfaceList , setType4} from '@/api/index.js'
	export default {
		data() {
			return {
				loading: false,
                interfaceVal:{},
                callModeOptions: [
					{
                    value: 3,
                    label: '中断'
                },{
                    value: 2,
                    label: '同步'
                },{
                    value: 1,
                    label: '异步'
                } 
				// , {
    //                 value: 3,
    //                 label: '中断'
    //             },
				],
                callType: 2,
                flist:[],
                radio: null,
                dialogTableVisible:false,
                list: null,
                page: 1,
				pageSize: 10,
				pager: {},
                targetResult:'',
                value:[],
                responseBody:[],
                nodeJson:[]                
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
			readOnly:{
				type: Boolean,
				default :false
			}
		},
        computed:{
            Fielduser() {
				if (this.$store.state.FieldUser != null) {
					this.loading = false
					return this.$store.state.FieldUser.data.fieldList
				} else {
					return []
				}
			}
        },
		methods: {
            handleChange(value){
            },
            currpage(e) {
				this.page = e;
                this.getInterfaceList();
			},
            submit(){                
                let nodeJson = {
                    "callConfig": {
                        "interfaceId": null,
                        "interfaceName": "",
                        "callType": this.callType,
                        "callTypeName": this.callModeOptions.find(x=>x.value==this.callType).label
                    },
                    "resultConfig": []
                }
                if(JSON.stringify(this.interfaceVal) !='{}'){                    
                    nodeJson.callConfig.interfaceId = this.interfaceVal.id;
                    nodeJson.callConfig.interfaceName = this.interfaceVal.name;
                    if(this.responseBody.length>0){
                        if(this.targetResult && this.value.length>0){                            
                            const filed = this.Fielduser.find(x=>x.id==this.targetResult)
                            let objString = '';
                            console.log(!isNaN(this.value[0]))
                            if(!isNaN(this.value[0])){
                                objString += '['+this.value[0]+']'
                            }else{
                                objString += this.value[0];
                            }
                            for(let i=1;i<this.value.length;i++){
                                let item = this.value[i];
                                console.log(item);
                                if((!isNaN(item))){
                                    objString += '['+item+']';
                                }else{
                                    objString += '.'+item;
                                }                                
                            }
                            let result = {
                                "fieldId": filed.id?filed.id:'',
                                "fieldEn": filed.fieldEn?filed.fieldEn:'',
                                "fieldCn": filed.fieldCn?filed.fieldCn:'',
                                "parseField": objString
                            }
                            nodeJson.resultConfig.push(result);
                        }else{
                            this.$message.error('结果配置字段缺失')
                            return 
                        }
                    }
                    this.setType({
                        "id": this.data.id,
                        "initEngineVersionId": String(this.data.Vid),
                        "nodeType": 18,
                        "nodeName": this.data.text,
                        "nodeCode": this.data.nodeCode,
                        "nodeOrder": this.data.nodeOrder,
                        "nodeX": this.data.x,
                        "nodeY": this.data.y,
                        nodeJson: JSON.stringify(nodeJson),
                        snapshot: JSON.stringify(nodeJson),
                        params: JSON.stringify({
                            "dataId": "18",
                            "url": "/Riskmanage/resource/images/decision/childEngine.png",
                            "type": "18"
                        })
                    }).then(res => {
                        if (res.status == "1") {
                            this.$message({
                                message: '提交成功',
                                type: 'success'
                            });
                            this.$emit('callback', JSON.stringify(nodeJson))
                        }
                        this.loading = false
                    })
                }else{
                    this.$message.error('未选择接口')
                }
                
            },
            async setType(param) {
				const data = await setType4(param);
				return data
			},
            selRadio(val){
                this.interfaceVal = this.flist.find(x=>x.id==this.radio);
                let obj = this.deepGetLayout(JSON.parse(this.interfaceVal.responseBody))
                this.responseBody = obj;
                this.value=[];
            },
            // 选择接口
            radioSubmit(){
                this.interfaceVal = this.flist.find(x=>x.id==this.radio);
                let obj = this.deepGetLayout(JSON.parse(this.interfaceVal.responseBody))
                this.responseBody = obj;
                this.value=[];
                this.dialogTableVisible = false;
            },
			crea() {
              
                this.getInterfaceList();
                this.nodeJson = this.data.nodeJson ? JSON.parse(this.data.nodeJson) : {
                    "callConfig": {
                        "interfaceId": null,
                        "interfaceName": "",
                        "callType": this.callType,
                        "callTypeName": this.callModeOptions.find(x=>x.value==this.callType).label
                    },
                    "resultConfig": []
                }     
			},
            initShow(){
                const {resultConfig} = this.nodeJson;
                this.callType = this.nodeJson.callConfig.callType;
                const {callConfig} = this.nodeJson;
                if(callConfig.interfaceId){
                    this.radio = callConfig.interfaceId;
                    this.radioSubmit();
                }
                if(resultConfig.length>0){
                    let result = resultConfig[0];
                    this.targetResult = result.fieldId;
                    let str = result.parseField.replace(/\[/g,".").replace(/\]/g,"");    
                    let valueArr = str.split('.');  
                    valueArr.map((x,i)=>{
                        if(x==''){
                            valueArr.splice(i,1)
                        }
                    });              
                    this.value = valueArr;
                }
            },
            selInterfaceVal(){                
                this.dialogTableVisible=true;

            },
            async getInterfaceList() {
				this.loading = true
                const data = await getInterfaceList({
					pageNo: this.page,
                    pageSize: this.pageSize,
                    "interfaceInfo": {}
				})
				// const data = await getInterfaceList(params)
				if (data.status === "1") {
					this.flist = data.data.klist
					this.pager = data.data.pageInfo                    
                    this.initShow(); 
				} else {
					this.$message.error('访问出错了-_-');
				}
				this.loading = false
			},
            deepGetLayout(obj) {
				let sobj = []
				for (let key in obj) {
					if (obj.hasOwnProperty(key)) {
						if (typeof obj[key] == 'object' && !Array.isArray(obj[key]) && obj[key] != null) {
							sobj.push({
								label: key,
								value: key,
								children: this.deepGetLayout(obj[key])
							})

						} else if (typeof obj[key] == 'object' && Array.isArray(obj[key]) && obj[key] != null) {
                            let children =[];
                            obj[key].map((x,idx)=>{
                                children.push({
                                    label: idx,
                                    value: idx+'',
                                    children: this.deepGetLayout(obj[key][0])
                                })
                            })
                            sobj.push({
                                label: key,
                                value: key,
                                children: children
                            })
                            // sobj.push({
                            //     label: key,
                            //     value: key,
                            //     children: [{
                            //         label: '元素',
                            //         value: '[]',
                            //         children: this.deepGetLayout(obj[key][0])
                            //     }]
                            // })
							
						} else {
							sobj.push({
								label: key,
								value: key,
							})
						}
					}
				}
				return sobj
			}
		},
		watch: {
			data() {
				this.crea()
			}
		}


	}
</script>

<template> 
    <div class="field-wrapper">
        <el-dialog title="输入参数" :visible.sync="isShow" width="50%" :before-close="fieldClose">
			<div class="rule_dialg_header" style="padding-left:0;">
				当前选项为： {{radio.fieldCn}}
				<div>
					<el-input v-model="search" maxlength="30"  placeholder="快速搜索"></el-input>
				</div>
			</div>
			<div class="rule_dialg_cont">
                <div v-for="(value,index) in fradioList" :key="index">
					
                    <el-radio v-model="active" :label="value.id" border size="mini" @change="change">{{value.fieldCn}}</el-radio>
                </div>
			</div>
			<span slot="footer" class="dialog-footer">
				<el-button @click="fieldClose">取 消</el-button>
				<el-button type="primary" @click="dialogSure()">确 定</el-button>
			</span>
		</el-dialog>
    </div>
</template>
<script>
    export default {
        props:{
            isShow:{
                type: Boolean,
                default: false
            },
            radioList:{
                type:Array,
                default(){
                    return []
                }
            }
        },
        data(){
            return {
                radio: {},
                search:"",
                active:""
            }
        },
        created(){
            
        },
        computed: {            
            outPutUser() {
				if (this.$store.state.Output) {
					return this.$store.state.Output.data.paramMap.fieldList
				} else {
					return this.$store.state.Output
				}
			},            
            fradioList() {
				if(this.search!=""){
					let arr = []					
					arr=this.radioList.filter(value=>{
						if(value.fieldCn.indexOf(this.search)!=-1){
							return true
						}else{
							return false
						}
					})
					return arr
				}else{
					return this.radioList
				}				
			}       
        },
        methods:{
            change(e){
                this.active = e;
                // this.radio=this.radioList[this.active];
                this.radio=this.mixinGetValueById(e)
            },
            fieldClose(){
                this.$confirm('确认关闭？')
                .then(_ => {
                    this.radio={};
                    this.search="";
                    this.active="";
                    this.$emit('fieldClose');
                })
                .catch(_ => {});                    
            },
            dialogSure(){    
                if(typeof this.radio.id == "undefined"){
					this.$message.error('请选择一个字段，或者选择取消')
				}else{	
                    this.$emit('selEvent',this.radio)	
                    this.radio={};		
                    this.search="";	
                    this.active="";
                    this.$emit('fieldClose');
                }
            }
        }
    }
</script>
<style scoped>
	.rule_fa {
		display: flex;
		width: 20%;
		height: 30%;
		margin: 10px 10px 0 0;
		flex-shrink: 0;
	}

	.rule_son {
		display: flex;
		margin-top: 10px;
		justify-content: flex-start;
	}
	.rule_dialg_header{
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 40px;
	}
	.rule_dialg_cont{
		display: flex;
		flex-wrap: wrap;
		justify-content: flex-start;
		height: 40vh;
		overflow: scroll;
		overflow-x: hidden;
		align-items: flex-start;
		align-content: flex-start;
        margin-top: 20px;
	}
	.rule_dialg_cont>div{
		width: 30%;
		overflow: hidden;
		margin-top: 10px;
		margin-right: 10px;
		background-color: #fff;
	}
    .rule_dialg_cont .el-radio--mini.is-bordered{
        border: none;
    }
	.rule_dialg_cont>div:hover{
		
		overflow: unset;
	}
    .field-wrapper .el-dialog__body{
        padding-top: 10px;
    }
</style>
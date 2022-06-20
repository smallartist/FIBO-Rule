

<template>
	<div class="cont_cont">

		<div class="cont_left" v-loading="leftloading">
			<div class="cont_header">
				<p class="cont_header_title">{{title}}</p>
				<p class="cont_header_subtitle">{{title}}</p>
			</div>
			
			<contNewFule v-model="tempNewF" :newf.sync="newf" @newFileSure="newFileSure" @newFile="newFile" ></contNewFule>
			<div class="cont_list">
				<fileHome :data="list" @curr="clickCurrid" :currid="currid" @RenameFun="RenameFun" @RenameClose="RenameClose"
				 @updatafilelist="updatafilelist" @delectFun="delectFun">
				</fileHome>
			</div>
		</div>
		<div class="cont_right" v-loading="contloading" @click="tempHintLeft=null;tempHintTop=null;">
			<div v-if="!listRedact">
				<div v-if="showRight===false" class="cont_right_hint">
					请先选择左侧文件夹
				</div>
				<div v-else>
					<div class="cont_right_top">
						<div>
							<el-button type="primary" @click="listRedact=true" :disabled="currid!=99999999?false:'disabled'">新增</el-button>
							<el-button type="danger" @click="using(-1)" :disabled="this.selection.length>0?false:'disabled'">删除</el-button>
							<el-button type="success" @click="using(1)" :disabled="this.selection.length>0?false:'disabled'">启用</el-button>
							<el-button type="warning" @click="using(0)" :disabled="this.selection.length>0?false:'disabled'">停用</el-button>
							<!-- <el-select v-model="tempMove" placeholder="移动到:" style="margin-left: 10px;" :disabled="this.selection.length>0?false:'disabled'" filterable @change="mixinMoveChange"> -->
							<el-select v-model="tempMove" placeholder="移动到:" style="margin-left: 10px;" :disabled="this.selection.length>0?false:'disabled'"
							 filterable @change="moveChange">
								<el-option v-for="value in listunfold" :key="value.id" :label="value.name" :value="value.id" v-show="value.id!=99999999"></el-option>
							</el-select>
							<!-- 断点  准备移动 -->
						</div>
						<div v-if="getData.type==1">
							<el-button @click="upShow=true">批量导入</el-button>
							<el-button @click="down">模板下载</el-button>
						</div>
					</div>
					<div class="cont_right_cont">
						<div v-if="data">
							<el-table border :data="data.data.klist" @select-all="selectAll" @select="select" style="width: 100%"
							 :cell-style="{padding: '10px'}">
								<el-table-column type="selection" width="70">
								</el-table-column>
								<el-table-column v-for="item in getData.row" :key="item.id" :prop="item.row" :label="item.label" align="center">
									<template slot-scope="scope">
										<span v-if="item.type==='Blooen'">
											{{scope.row[item.row]?"是":"否"}}
										</span>
										<span v-else-if="item.type==='State'">
											{{scope.row[item.row]=="1"?'启用':'未启用'}}
										</span>
										<span v-else-if="item.type==='type'">
											{{scope.row[item.row]=="1"?'数值型':(scope.row[item.row]=="2"?'字符型':(scope.row[item.row]=="3"?'枚举型':(scope.row[item.row]=="4"?'小数型':(scope.row[item.row]=="5"?'数组型':(scope.row[item.row]=="6"?'JSON型':'')))))}}
										</span>
										<span v-else-if="item.fn">
											{{item.fn(scope.row[item.row])}}
										</span>
										<span v-else-if="item.type==='Time'" style="white-space: nowrap;" class="contText">{{
													new Date(scope.row[item.row]).toLocaleDateString().replace(/\//g, "-") + " " + new Date(scope.row[item.row]).toTimeString().substr(0, 8)
													}}</span>
										<span class="contText" v-else>
											{{scope.row[item.row]}}
										</span>
									</template>
								</el-table-column>
								<el-table-column label="操作" align="center" size="s">
									<template slot-scope="scope">
										<el-button icon="el-icon-setting" circle size="mini" @click="dialogShow(scope.row.id)"></el-button>
									</template>
								</el-table-column>
							</el-table>
							<el-pagination style="float: right;margin-right: 40px;margin-top: 40px;" :current-page="currPage"
							 @current-change="clickpage" background layout="prev, pager, next" :total="data.data.pager.total">
							</el-pagination>
						</div>
					</div>
				</div>


			</div>
			<template v-else>
				<dataManageRedact @close="listRedact=false;tempRedactId=0" @Ok="listRedact=false;tempRedactId=0;getlist();currPage=1"
				 :updata="getData.updatafield" :id='tempRedactId' :fieldTypeId="currid" :setsave="getData.setsave" :getInfo="getData.getInfo"
				 :ftype="getData.type"></dataManageRedact>
			</template>
		</div>

		<el-dialog title="上传文件" :visible.sync="upShow" width="30%" :before-close="upShowClose">
			<div style="margin: 0 auto;display: flex;justify-content: center;">
				<el-upload class="upload-demo" ref="upload" action="doUpload" :limit="1" :file-list="fileList" :before-upload="beforeUpload"
				 v-loading="Uploadloading">
					<el-button slot="trigger" size="small" type="primary">选取文件</el-button>
					<div slot="tip" class="el-upload__tip">只能上传excel文件，且不超过5MB</div>
					<div slot="tip" class="el-upload-list__item-name">{{fileName}}</div>
				</el-upload>
			</div>
			<span slot="footer" class="dialog-footer">
				<el-button @click="upShow = false">取消</el-button>
				<el-button type="primary" @click="submitUpload()">确定</el-button>
			</span>
		</el-dialog>
	</div>
</template>

<script>
	import contNewFule from '@/components/common/contNewFile.vue'
	import '@/assets/css/cont.css'
	import fileHome from '@/components/common/fileHome.vue'
	import dataManageRedact from '@/components/common/dataManageRedact.vue'
	import contmixin from '@/utils/contminxin/contmixin.js'
	import {
		updateFieldFolder
	} from '@/api/index.js'
	export default {
		mixins: [
			contmixin
		],
		components: {
			fileHome,
			dataManageRedact,
			updateFieldFolder,
			contNewFule
		},
	
		props: {
			title: {
				type: String,
				default: ''
			},
			getData: {
				type: Object,
				default: null
			},

		},
		watch: {
			list() {
				if (this.list.length > 0) {
					this.leftloading = false
				}
			}
		},
		data() {
			return {
				list: [],
				Uploadloading: false,
				tempMove: '',
				leftloading: true,
				fileName: "",
				fileList: [],
				upShow: false,
				currPage: 1,
				currid: null,
				data: null,
				contloading: false,
				newf: false,
				tempNewF: "",
				tempHintTop: null,
				tempHintLeft: null,
				tempId: null,
				listRedact: false, //新增页面开启
				tempRedactId: 0,
				selection: []
			}
		},
		created() {
			this.getData.getTree({
				type: this.getData.type
			}).then(res => {
				this.list = this.listTreeDeep(res.data, 1)
				this.clickCurrid(99999999)
			})
		},
		methods: {
			moveChange(e) { //移动文件夹
				let arr = this.selection.map((value) => {
					return value.id
				})
				if (arr.length < 1) {
					this.$message.error('未选择任何文件');
					return
				}
				let params = {
					ids: arr,
					folderId: e
				}
				updateFieldFolder(params).then(res => {
					if (res.status == "1") {
						this.clickCurrid(this.currid)
						this.$message({
							message: '移动成功',
							type: 'success'
						});
						this.selection = []
					}
				})
				this.tempMove = ""
			},

			down() {
				window.open(window.origin + '/Riskmanage/v2/datamanage/field/downTemplate')
			},
			delectFun(id) {
				let name
				this.deepGetCurr(id, this.list, (value) => {
					name = value.name
				})
				let params = {
					status: -1,
					id: id,
					fieldType: name
				}
				this.getData.updatalist(params).then(res => {
					if (res.status === "1") {
						this.$message({
							type: 'success',
							message: '删除成功!'
						});

						this.deepGetCurr(id, this.list, (value, item, index) => {
							item.splice(index, 1)
						})


					}
					this.leftloading = false
					this.currid = 99999999
					this.getlist()

				}).catch(() => {
					this.$message.error("请求失败了" + '-_-');
					this.leftloading = false
				})
			},
			updatafilelist(params) {
				this.leftloading = true
				let tempNum = null

				this.deepGetCurr(params.id, this.list, (value) => {
					tempNum = value.parentId
				})
				params.parentId = tempNum == 99999999 ? 0 : tempNum
				tempNum = null
				let obj = {
					fieldType: params.name,
					id: params.id,
					parentId: params.parentId
				}
				this.getData.updatalist(obj).then(res => {
					if (res.status === "1") {
						this.$message({
							message: '修改成功',
							type: 'success'
						});
						this.deepGetCurr(params.id, this.list, (value) => {
							value.name = params.name
							value.Rename = false
						})
						this.leftloading = false
					} else {
						this.leftloading = false
					}
				}).catch(() => {
					this.$message.error("请求失败了" + '-_-');
					this.leftloading = false
				})
			},


			getlist() {
				this.contloading = true
				this.listRedact = false
				let params = {
					"isCommon": 1,
					"fieldTypeId": String(this.currid),
					"pageNo": 1
				}
				this.getData.getlist(params).then(res => {
					this.data = res
					this.contloading = false
				})

			},
			clickpage(e) {
				this.currPage = e
				this.contloading = true
				let params = {
					"isCommon": 1,
					"fieldTypeId": String(this.currid),
					"pageNo": e
				}
				this.getData.getlist(params).then(res => {
					if (res.status == "1") {
						this.data = res
						this.selection = []
						this.contloading = false
					}
				})
			},
			using(id) {

				let arr = this.selection.map((value) => {
					return value.id
				})
				if (arr.length < 1) {
					this.$message.error('未选择任何文件');
					return
				}
				let params = {
					status: id,
					ids: arr.join(','),
					fieldTypeId: this.currid
				}

				this.getData.fieldusing(params).then(res => {
					if (res.status == "1") {
						this.$message({
							message: '操作成功',
							type: 'success'
						});
						this.getlist()
						this.$store.dispatch('reGetfielduser')
					}
				})
			},
			newFileSure() {
				this.leftloading = true
				let params = {
					parentId: this.currid,
					fieldType: this.tempNewF,
					type: this.getData.type
				}
				if(this.mixnewFileZindexVerify(this.list,this.currid)==6){
					this.$message.error('已达到最深层级')
					this.leftloading = false
					return
				}
				
				this.mixnewFileSure(params)
			}
		}



	}
</script>

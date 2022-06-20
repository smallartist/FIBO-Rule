
<template>
	<div class="cont_cont">

		<div class="cont_left" v-loading="leftloading" v-if="!listRedact">
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

		<div class="cont_right" v-loading="contloading" @click="tempHintLeft=null;tempHintTop=null;" >
			<div v-if="!listRedact">
				<div v-if="showRight===false" class="cont_right_hint">
					请先选择左侧文件夹
				</div>
				<div v-else>
					<div class="cont_right_top">
						<div>
							<el-button type="primary" @click="listRedact=true" :disabled="currid!=99999999?false:'disabled'">新增</el-button>
							<el-button type="danger" @click="using(-1);$store.dispatch('regetcache', 'decisionTree')" :disabled="this.selection.length>0?false:'disabled'">删除</el-button>
							<el-button type="success" @click="using(1)" :disabled="this.selection.length>0?false:'disabled'">启用</el-button>
							<el-button type="warning" @click="using(0)" :disabled="this.selection.length>0?false:'disabled'">停用</el-button>
							<el-select v-model="tempMove" placeholder="移动到:" style="margin-left: 10px;" :disabled="this.selection.length>0?false:'disabled'"
							 filterable @change="mixinMoveChange">
								<el-option v-for="value in listunfold" :key="value.id" :label="value.name" :value="value.id" v-show="value.id!=99999999"></el-option>
							</el-select>
						</div>
						<div>
							<el-input placeholder="请输入搜索内容" v-model="search">
								<i slot="suffix" class="el-input__icon el-icon-search" @click="getsearch"></i>
							</el-input>
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
											{{scope.row[item.row]==1?'启用':'未启用'}}
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
							 @current-change="clickpage" background layout="prev, pager, next" :total="data.data.pageInfo.total">
							</el-pagination>
						</div>
					</div>
				</div>
			</div>
			<div v-if="listRedact" style="height: 100%;overflow: hidden;">
				<dataManageRedact @close="listRedact=false;tempRedactId=0" @Ok="listRedact=false;tempRedactId=0;getlist()" :getData="getData"
				 :id='tempRedactId' :nameId="currid" :type="getData.type"></dataManageRedact>
			</div>
		</div>
	</div>



</template>

<script>
	import contNewFule from '@/components/common/contNewFile.vue'
	import '@/assets/css/cont.css'
	import fileHome from '@/components/common/fileHome.vue'
	import dataManageRedact from './decTreeManageRedact.vue'
	import contmixin from '@/utils/contminxin/contmixin.js'
	export default {
		mixins: [
			contmixin
		],
		components: {
			contNewFule,
			fileHome,
			dataManageRedact
		},
		created() {
			this.getData.getTree({
				parentId: 0,
				treeType: this.getData.treeType,
				type: 1
			}).then(res => {
				this.list = this.listTreeDeep(res.data, 1)
				this.clickCurrid(99999999)
			})


		},
		props: {
			title: {
				type: String,
				default: ''
			},
			getData: {
				type: Object,
				default: null
			}
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
				tempMove: '',
				leftloading: true,
				search: "",
				fileName: "",
				fileList: [],
				upShow: false,
				currPage: 1,
				currid: null,
				data: null,
				contloading: false,
				newf: false,
				tempNewF: "",
				listRedact: false, //新增页面开启
				tempRedactId: 0,
				selection: []
			}
		},
		methods: {

			getsearch() {
				this.contloading = true
				let params = {
					entity: {
						name: this.search,
						"folderId": String(this.currid) === "99999999" ? '' : String(this.currid),
					},
					key: "ruleName",
					status: "0,1",
					
					"pageNum": 1,
					isSearch: 1
				}
				this.getData.getlist(params).then(res => {
					this.data = res
					this.contloading = false
				})
				this.currPage = 1
			},

			getlist() {
				this.contloading = true
				this.listRedact = false
				let params = {
					status: "0,1",
					entity: {},
					"pageNum": 1
				}
				if (String(this.currid) !== "99999999") {
					params.entity.folderId = this.currid
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
					status: "0,1",
					entity: {},
					"pageNum": e
				}
				if (String(this.currid) !== "99999999") {
					params.entity.folderId = this.currid
				}
				this.getData.getlist(params).then(res => {
					if (res.status == 1) {
						this.data = res
						this.selection = []
						this.contloading = false
					}
				})
			},

			newFileSure() {
				this.leftloading = true
				let params = {
					parentId: String(this.currid),
					name: this.tempNewF,
					"treeType":this.getData.treeType,
					"type": "1",
					"engineId": ""
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

<template>
	<div>
		<div class="Rule_version">
			<el-select value="V:0" placeholder="请选择版本" v-if="!id" :disabled="true" @change="versionChange" size='mini'>
			</el-select>
			<el-select v-model="version.id" placeholder="请选择版本" v-else :disabled="addVersionStatus"
				@change="versionChange" size='mini'>
				<el-option v-for="item in ruleVersionList" :key="item.id" :label="item.versionCode" :value="item.id">
				</el-option>
			</el-select>
			<el-button plain class="Rule_version_buttom" style="margin-left: 10px;" size='mini' @click="addVersion"
				v-show="!addVersionStatus" :disabled="!id">新增版本</el-button>
			<el-button plain class="Rule_version_buttom" size='mini' type="success" @click="sureAddVersion"
				v-show="addVersionStatus">确认新增</el-button>
			<el-button plain class="Rule_version_buttom" size='mini' type="danger" @click="addVersionClose"
				v-show="addVersionStatus">取消新增</el-button>
			<el-button plain class="Rule_version_buttom" size='mini' @click="$emit('Dialog',true)"
				:disabled="addVersionStatus||!id">复制版本</el-button>
			<el-button plain class="Rule_version_buttom" size='mini' @click="updateVersion=true;$emit('Dialog',true)"
				:disabled="addVersionStatus||!id">版本重命名</el-button>
			<el-button plain class="Rule_version_buttom" size='mini' type="danger" @click="delectVersion"
				:disabled="addVersionStatus||!id">删除此版本</el-button>
			<el-button plain class="Rule_version_buttom" size='mini' @click="$emit('exportVersion')"
				v-if="exportVersion" :disabled="addVersionStatus||!id">导出此版本</el-button>
			<el-button plain class="Rule_version_buttom" size='mini' @click="importVersion" v-if="exportVersion"
				:disabled="addVersionStatus||!id">导入版本</el-button>

			<span style="font-size: 12px;color: #999;margin-left: 10px;">切换版本并不会自动保存内容 请手动保存</span>

		</div>
		<span style="color: #00A854;font-size: 14px;margin-left: 10px;">版本描述：{{id?version.description:'初始版本'}}</span>
		<el-dialog title="提示" :visible.sync="addVersionDialog" width="30%"
			@close="updateVersion=false;tempVersion.versionCode='',tempVersion.description=''"
			:close-on-click-modal="false" :close-on-press-escape="false" :show-close="false">
			<div v-loading="addVersionLoading">
				<p style="margin-top: 20px;"><span style="width: 30%;">新版本名称：</span>
					<el-input placeholder="请输入新版本名称" maxlength="30" v-model="tempVersion.versionCode" clearable
						style="width: 70%;"></el-input>
				</p>
				<p style="margin-top: 20px;"><span style="width: 30%;">新版本描述：</span>
					<el-input placeholder="请输入新版本描述" maxlength="30" v-model="tempVersion.description" clearable
						style="width: 70%;"></el-input>
				</p>
				<div slot="footer" class="dialog-footer" style="text-align: right;margin-top: 20px;">
					<el-button @click="$emit('Dialog',false)">取 消</el-button>
					<el-button type="primary" @click="addVersionSure">确 定</el-button>
				</div>
			</div>
		</el-dialog>
		<el-dialog title="提示" :visible.sync="importVersionDialog" width="30%" @close="" :close-on-click-modal="false"
			:close-on-press-escape="false" :show-close="false">
			<div v-loading="importVersionLoading">
				<p style="margin-top: 20px;"><span style="width: 30%;">新版本名称：</span>
					<el-input placeholder="请输入新版本名称" maxlength="30" v-model="tempVersion.versionCode" clearable
						style="width: 70%;"></el-input>
				</p>
				<p style="margin-top: 20px;"><span style="width: 30%;">新版本描述：</span>
					<el-input placeholder="请输入新版本描述" maxlength="30" v-model="tempVersion.description" clearable
						style="width: 70%;"></el-input>
				</p>

				<div style="display: flex;justify-content: center;padding-top: 20px;">
					<el-upload class="upload-demo" ref="upload" action="doUpload" :limit="1" :file-list="fileList"
						:before-upload="beforeUpload" v-loading="Uploadloading">
						<el-button slot="trigger" size="small" type="primary">选取文件</el-button>
						<div slot="tip" class="el-upload__tip">只能上传json文件，且不超过5MB</div>
						<div slot="tip" class="el-upload-list__item-name">{{fileName}}</div>
					</el-upload>
				</div>

				<div slot="footer" class="dialog-footer" style="text-align: right;margin-top: 20px;">
					<el-button @click="importVersionDialog=false">取 消</el-button>
					<el-button type="primary" @click="importVersionSure">确 定</el-button>
				</div>
			</div>
		</el-dialog>
	</div>







</template>

<script>
	export default {
		props: {
			ruleVersionList: {
				type: Array,
				default () {
					return []
				}
			},
			version: {
				type: Object,
				default () {
					return {
						id: '',
						description: '',
						versionCode: ''
					}
				}
			},
			id: {
				type: Number,
				default: 0
			},
			addVersionDialog: {
				type: Boolean,
				default: false
			},
			addVersionLoading: {
				type: Boolean,
				default: false
			},
			addVersionStatus: {
				type: Boolean,
				default: false
			},
			exportVersion: {
				type: Boolean,
				default: false
			}
		},
		data() {
			return {
				tempJsonObj: null,
				files: null,
				fileName: '',
				fileList: [],
				importVersionDialog: false,
				importVersionLoading: false,
				Uploadloading: false,
				tempVersion: {
					versionCode: '',
					description: '',
				},
			}
		},
		methods: {
			beforeUpload(file) {
				// console.log(file, '文件');
				this.files = file;
				const extension = file.name.split('.')[file.name.split('.').length - 1] === 'json'
				const isLt2M = file.size / 1024 / 1024 < 5
				if (!extension) {
					this.$message.warning('上传模板只能是 json格式!')
					return
				}
				if (!isLt2M) {
					this.$message.warning('上传模板大小不能超过 5MB!')
					return
				}
				this.fileName = file.name;

				var reader = new FileReader() // 新建一个FileReader
				reader.readAsText(file, 'UTF-8') // 读取文件
				reader.onload =(evt)=>{ // 读取完文件之后会回来这里
					this.tempJsonObj = JSON.parse(evt.target.result) // 读取文件内容
					console.log(this.tempJsonObj)
				}
				return false // 返回false不会自动上传
			},
			importVersionSure() {
				if(this.tempJsonObj==null){
					this.$message.error('请上传文件')
					return
				}
				if(this.tempVersion.versionCode.trim()===''){
					this.$message.error('请填写新版本名称')
					return
				}
				if(this.tempVersion.description.trim()===''){
					this.$message.error('请填写新版本描述')
					return
				}
				console.log(JSON.parse(JSON.stringify(this.tempJsonObj)))
				this.$emit('importNewVersion',{data:JSON.parse(JSON.stringify(this.tempJsonObj)),name:JSON.parse(JSON.stringify(this.tempVersion))})
				this.tempJsonObj=null
				this.tempVersion.versionCode=''
				this.tempVersion.description=''
				this.importVersionDialog = false
			},
			importVersion() {
				this.importVersionDialog = true
			},
			versionChange(e) {
				this.ruleVersionList.forEach(value => {
					if (value.id === this.version.id) {
						this.$emit('versionChange', value)
					}
				})

			},
			addVersion(e) {
				// this.addVersionStatus = true
				this.$emit('StatusChange', true)
				this.$emit('addVersion', e)
			},
			sureAddVersion(e) {
				this.$emit('addVersionExamine', this.tempVersion)
				// this.addVersionDialog = true
				// this.$emit('Dialog',true)
			},
			delectVersion(e) {
				if (this.ruleVersionList.length == 1) {
					this.$message.error('最少要有一个版本存在')
					return
				}
				this.$confirm('确定删除?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$emit('delectVersion', e)
				}).catch(() => {
					this.$message({
						type: 'info',
						message: '已取消删除'
					});
				});

			},
			addVersionSure(e) {
				if (this.verificationTempVersion()) {
					this.addVersionLoading = false
					return
				}


				console.log(1)
				if (this.addVersionStatus) { // 添加版本
					this.$emit('addVersionSure', this.tempVersion)
				} else if (this.updateVersion) { //重命名版本
					this.$emit('updateVersion', this.tempVersion)
				} else { // 复制版本
					this.$emit('copyVersion', this.tempVersion)
				}
				// this.addVersionStatus = false
				// this.$emit('StatusChange',false)

			},
			addVersionClose(e) {
				// this.addVersionStatus = false
				this.$emit('StatusChange', false)
				this.$emit('addVersionClose', e)
			},
			verificationTempVersion() {
				if (this.tempVersion.versionCode.trim() === "") {
					this.$message.error('请填入新版本名称')
					return true
				}
				let is = {
					is: false,
					msg: '',
				}
				this.ruleVersionList.forEach(value => {
					if (value.versionCode === this.tempVersion.versionCode) {
						is.is = true
						is.msg = '已存在此版本名'
					}
				})
				if (is.is) {
					this.$message.error(is.msg)
					return true
				}
				if (this.tempVersion.description.trim() === "") {
					this.$message.error('请填入新版本描述')
					return true
				}
				return false
			},
		}


	}
</script>

<style>
	.Rule_version {
		/* margin-top: 20px; */
		padding-left: 10px;
	}
</style>

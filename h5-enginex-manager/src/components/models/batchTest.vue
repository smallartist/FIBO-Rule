<template>
	<div v-show="upShow">
		<el-dialog title="上传测试文件" :visible.sync="upShow" width="30%" :before-close="upShowClose">
			<div v-loading="Uploadloading">
				<div style="margin: 0 auto;display: flex;justify-content: center;" :disabled="true">
					<el-upload class="upload-demo" ref="upload" action="doUpload" :limit="1" :file-list="fileList"
						:before-upload="beforeUpload">
						<el-button slot="trigger" size="small" type="primary" style="margin-top: 60px;">选取文件</el-button>

						<div slot="tip" class="el-upload__tip">只能上传excel文件，且不超过5MB</div>
						<div slot="tip" class="el-upload-list__item-name">{{fileName}}</div>
					</el-upload>
				</div>

				<div slot="footer" class="dialog-footer" style="margin-left: 30%;margin-top: 30px;">

					<el-button slot="trigger" size="mini" type="primary" @click="accessTemplate">下载模板</el-button>
					<!-- getDownloadJsonField -->
					<el-button slot="trigger" size="mini" type="primary" @click="JsonFieldOpen">查看JSON</el-button>
					<el-button @click="$emit('upshowf')">取消</el-button>
					<el-button type="primary" @click="submitUpload()" :disabled="fileName?false:'disabled'">确定
					</el-button>
				</div>
			</div>


		</el-dialog>

		<el-dialog title="查看JSON" :visible.sync="JsonField" width="50%" append-to-body>
			<div v-loading="JsonFieldloading">

				<el-input type="textarea" rows="20" id="tempJsonValue" :value="annotationJson">

				</el-input>
				<div slot="footer" class="dialog-footer" style="display: flex;justify-content: flex-end;align-items: center;margin-top: 20px;">
					<span style="color: #409EFF;font-size: 12px;margin-right: 10px;">点击复制按钮，可以复制无注释的JSON</span>
					<el-button type="primary" @click="copy">复制</el-button>
					<el-button type="primary" @click="JsonField=false">确定</el-button>
					
				</div>
			</div>

		</el-dialog>

		<el-dialog title="批量测试结果" :visible.sync="resultShow" width="50%" append-to-body>
			<div>
				<el-row class="top-head">
					<el-col :span="8">
						<div class="grid-content">执行状态</div>
					</el-col>
					<el-col :span="8">
						<div class="grid-content">执行结果</div>
					</el-col>
					<el-col :span="8">
						<div class="grid-content">条数</div>
					</el-col>
				</el-row>
				<div class="result-box">
					<el-row v-for="(item,key) in result.success">
						<el-col :span="8">
							<div class="grid-content">成功</div>
						</el-col>
						<el-col :span="8">
							<div class="grid-content">{{key?key:key==0?'0':'-'}}</div>
						</el-col>
						<el-col :span="8">
							<div class="grid-content">{{item?item:'-'}}</div>
						</el-col>
					</el-row>
					<el-row>
						<el-col :span="8">
							<div class="grid-content">失败</div>
						</el-col>
						<el-col :span="8">
							<div class="grid-content">-</div>
						</el-col>
						<el-col :span="8">
							<div class="grid-content">{{result.failNum}}</div>
						</el-col>
					</el-row>
				</div>
				<el-divider></el-divider>
				<el-row>
					<el-col :span="8">
						<div class="grid-content">总计</div>
					</el-col>
					<el-col :span="8">
						<div class="grid-content">&nbsp;</div>
					</el-col>
					<el-col :span="8">
						<div class="grid-content">{{result.total}}</div>
					</el-col>
				</el-row>
			</div>

		</el-dialog>

	</div>
</template>

<script>
	import {
		getDownloadJsonField,
		batchTest
	} from '@/api/index.js'
	export default {
		components: {
			getDownloadJsonField
		},
		props: {
			upShow: {
				type: Boolean,
				default: false
			},
			versionId: {
				type: Number | String,
				default: 0
			},
			engid: {
				type: Number | String,
				default: null
			}
		},
		data() {
			return {
				JsonField: false,
				Uploadloading: false,
				fileName: '',
				fileList: [],
				tempJson: '',
				annotationJson: '',
				JsonFieldloading: false,
				resultloading: false,
				resultShow: false,
				result: {}
			}
		},

		methods: {
			copy() {


				const text = document.createElement('textarea');
				text.value = this.tempJson;
				document.body.appendChild(text);
				text.select();
				document.execCommand('Copy');
				text.remove();

				this.$message({
					message: '复制成功',
					type: 'success'
				});
			},
			JsonFieldOpen() {
				this.tempJson = ""
				this.JsonFieldloading = true
				this.JsonField = true
				getDownloadJsonField({
					versionId: Number(this.versionId)
				}).then(res => {
					this.JsonFieldloading = false
					if (res.status == 1) {
						this.tempJson = JSON.stringify(JSON.parse(res.data), null, 4)

						this.annotationJson = this.str2asc(this.tempJson)

					} else {
						this.tempJson = '暂无数据'
					}
				})





			},
			str2asc(str) {
				let str2 = str.replace(/\n/g, '@')
				let arr = Object.keys(JSON.parse(str).biz_data.fields) || []
				arr.forEach(value => {
					var z = new RegExp(`@\\s*"${value}":`)

					try {
						let subStr = str2.match(z)[0]
						str2 = str2.replace(subStr,
							`@            //${this.mixinGetCnByEn(value)||'未找到此字段'}${subStr}`)
					} catch (err) {

					}

				})
				str2 = str2.replace(/@/g, '\n')

				return str2

			},
			accessTemplate() {
				var that = this
				let size = 0;
				var data = {
					versionId: Number(this.versionId),
				}
				this.Uploadloading = true
				fetch('/Riskmanage/v2/engine/createSampleData', {
						method: 'POST',
						body: JSON.stringify(data),
						headers: {
							'token': localStorage.getItem("token"),
							'content-type': 'application/json'
						},
					}).then(response => {
						if (response.ok) {
							// console.log(response)
							return response;
						} else {
							// console.log("请求失败")
						}
					})
					.then(response => response.body)
					.then(body => {
						const reader = body.getReader();

						return new ReadableStream({
							start(controller) {
								return pump();

								function pump() {
									return reader.read().then(res => { //res  ({ done, value }) 
										// 读不到更多数据就关闭流
										// console.log(res, "res");
										const {
											done,
											value
										} = res;
										if (done) {
											// console.log("end")
											controller.close();
											// return;
										}
										size += value.length || 0;
										// console.log(size, "size")
										// 将下一个数据块置入流中
										controller.enqueue(value);
										// this.Uploadloading = false
										return pump();
									});
								}
							}
						})
					})
					.then(stream => new Response(stream))
					.then(response => that.savingFile(response, '引擎入参.xlsx'))
					.catch(err => console.error(err));
			},
			beforeUpload(file) {
				// console.log(file, '文件');
				this.files = file;
				const extension = file.name.split('.')[1] === 'xls'
				const extension2 = file.name.split('.')[1] === 'xlsx'
				const isLt2M = file.size / 1024 / 1024 < 5
				if (!extension && !extension2) {
					this.$message.warning('上传模板只能是 xls、xlsx格式!')
					return
				}
				if (!isLt2M) {
					this.$message.warning('上传模板大小不能超过 5MB!')
					return
				}
				this.fileName = file.name;
				return false // 返回false不会自动上传
			},
			submitUpload() {
				this.Uploadloading = true
				// console.log('上传' + this.files.name)
				if (this.fileName == "") {
					this.$message.warning('请选择要上传的文件！')
					this.Uploadloading = false
					return false
				}
				let fileFormData = new FormData();
				fileFormData.append('file', this.files); //filename是键，file是值，就是要传的文件，test.zip是要传的文件名
				fileFormData.append('engineId', this.engid)
				this.batchTest(fileFormData).then(res => {
					if (res.status === "1") {
						this.upShowClose()
						this.resultShow = true;
						this.result = res.data;
					}
					this.Uploadloading = false
				})
				// console.log(this.getData)
				// this.getData.fieldsubmit(fileFormData).then(res => {
				// 	if (res.status === "1") {

				// 		this.upShowClose()
				// 	}
				// 	this.Uploadloading = false
				// })





			},
			upShowClose() {
				this.$emit('upshowf')
			},
			savingFile(response, fileName) {
				const that = this;
				response.blob().then(blob => {

					if (typeof FileReader === 'undefined') {
						notification.open({
							message: '您的浏览器不支持 FileReader，请升级浏览器',
						})
					}
					const reader = new FileReader();
					reader.addEventListener("loadend", function() {
						let resu = '';
						try {
							resu = JSON.parse(reader.result);
							// resu = eval('('+ reader.result + ')')
							if (resu.code == 500) {
								notification.open({
									message: resu.msg,
								})
							} else if (resu.code == 401) {
								notification.error({
									message: resu.msg
								})
							}
						} catch (e) {
							that.Uploadloading = false
							//捕获错误 说明是文本字符串 
							resu = reader.result;
							downloadBlob(blob, fileName);
						}

					});
					reader.readAsText(blob);

					//下载
					function downloadBlob(blob, fileName) {
						let blobUrl = window.URL.createObjectURL(blob);
						let a = document.createElement('a');
						a.href = blobUrl;
						a.target = '_blank';
						a.style.display = 'none'
						document.body.appendChild(a)
						a.download = fileName;
						a.click();
						window.URL.revokeObjectURL(blobUrl);
						document.body.removeChild(a)
						that.Uploadloading = false
						// that.setState({
						// 	downloading: false
						// })
					}
				}).catch(err => {
					that.Uploadloading = false
					// console.log(err)
					if (err == 'TypeError: Failed to fetch') {
						that.accessTemplate()
					}
				})
			},
			async batchTest(param) {
				const data = await batchTest(param);
				return data
			},
		}
	}
</script>

<style>
	.result-box {
		min-height: 100px;
		max-height: 300px;
		overflow-y: scroll;
	}

	.top-head .grid-content {
		background: #F2F6FC;
	}

	.grid-content {
		line-height: 35px;
		text-align: center;
	}
</style>

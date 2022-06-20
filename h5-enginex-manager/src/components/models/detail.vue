<template>
	<div class="detailmodel" >
		
		<el-card shadow="never" v-loading="loading">
			<div>
				<el-button type="primary" icon="el-icon-arrow-left" circle @click="$emit('close')"></el-button>
			</div>
			<el-form :model="model" :rules="rules" ref="modelFrom" label-width="150px">
				<el-form-item class="cWidth" label="模型名称：" prop="modelName">
					<el-input v-model="model.modelName"></el-input>
				</el-form-item>
				<el-form-item class="cWidth" label="模型描述：">
					<el-input v-model="model.description"></el-input>
				</el-form-item>
				<el-form-item label="模型文件：">
					<single-upload @getMessage="uploadSuccess" :fileList='fileList'></single-upload>
				</el-form-item>
				<el-card class="box-card card_left" shadow='never'>
					<div slot="header" class="clearfix">
						<span>模型解析字段</span>
					</div>
					<div class="overSro">
						<div v-for="item in model.modelFieldArr" class="text item">
							{{item}}
						</div>
					</div>
				</el-card>
				<div class="card_left padding_bot">
					<el-transfer filterable :filter-method="filterMethod" :titles="titles" target-order="push" filter-placeholder="请输入字段名称"
					 v-model="model.mappingFieldArr" :data="transferData">
					</el-transfer>
				</div>
				<el-form-item class="cWidth" label="输出字段：" prop="resultFieldEn">

					<outcontent :outcontent="model.strategyOutputList" type="models" style="margin-top: 20px;margin-left: 50px;">
						<div style="display:flex; align-items: center;">
							<el-select v-model="model.resultFieldEn" filterable placeholder="请选择" style="width: 200px;">
								<el-option v-for="item in FieldUser" :key="item.id" :label="item.fieldCn" :value="item.fieldEn">
								</el-option>
							</el-select>
							<p style="margin: 10px;">
								=
							</p>
							<el-select filterable value="预测结果" disabled style="width: 255px;">
							</el-select>
						</div>
					</outcontent>

				</el-form-item>
				<el-form-item>
					<el-button type="primary" @click="onSubmit('modelFrom')">提交</el-button>
					<el-button v-if="!isEdit" @click="resetForm('modelFrom')">重置</el-button>
				</el-form-item>
			</el-form>
		</el-card>
	</div>
</template>
<script>
	import {
		getAllFields,
		saveModel,
		updateModel,
		getModelDetailInfo
	} from '../../api/index';
	import outcontent from '@/components/models/outcontent.vue'

	import SingleUpload from '@/components/upload/singleUpload'
	const defaultModel = {
		strategyOutputList: [],
		modelName: '',
		description: '',
		fileName: '',
		resultFieldEn: '',
		filePath: '',
		modelFieldArr: [],
		mappingFieldArr: [],
	};
	export default {
		name: 'BrandDetail',
		components: {
			SingleUpload,
			outcontent,
		},
		props: {
			isEdit: {
				type: Boolean,
				default: false
			},
			id: {
				type: Number,
				default: 0
			}
		},
		data() {
			return {
				loading:false,
				model: Object.assign({}, defaultModel),
				fileList: [], // 回显文件信息
				rules: {
					modelName: [{
							required: true,
							message: '请输入模型名称',
							trigger: 'blur'
						},
						{
							min: 2,
							max: 100,
							message: '长度在 2 到 100 个字符',
							trigger: 'blur'
						}
					]
				},

				transferData: [],
				titles: ['待选指标', '已选指标'],
				filterMethod(query, item) {
					return item.label.indexOf(query) > -1;
				}
			}
		},
		mounted() {
			this.model.strategyOutputList = []
			this.getAllFields();
			this.dogetModelDetailInfo();
		},
		methods: {
			async getAllFields() {
				const {
					data
				} = await getAllFields();
				this.transferData = data;
			},
			dogetModelDetailInfo() {
				if (this.isEdit) {
					this.loading = true
					getModelDetailInfo(this.id).then(({
						data
					}) => {
						this.loading = false
						this.model = data;
						this.fileList.push({
							name: data.fileName,
							url: data.filePath
						});
					});
				} else {
					this.model = JSON.parse(JSON.stringify(defaultModel));
				}
			},
			uploadSuccess(data) {
				this.model.fileName = data.fileName;
				this.model.filePath = data.filePath;
				this.model.modelFieldArr = data.fieldList;
			},

			onSubmit(formName) {
				this.$refs[formName].validate((valid) => {
					if (valid) {
						if (this.model.fileName == '') {
							this.$message({
								message: '请先上传模型文件',
								type: 'error',
								duration: 2000
							});
							return false;
						}
						if (this.model.modelFieldArr.length != this.model.mappingFieldArr.length) {
							this.$message({
								message: '模型解析字段和映射字段数量不一致',
								type: 'error',
								duration: 3000
							});
							return false;
						}
						
						let is =false
						this.model.strategyOutputList.forEach(value => {
							if (value.fieldId === "" || String(value.fieldValue).trim() === "" || value.variableType === "") {
								is = true
							}
						})
						if(is){
							this.$message.error('请检查输出字段是否填写')
							return false
						}
						
						this.$confirm('是否提交数据', '提示', {
							confirmButtonText: '确定',
							cancelButtonText: '取消',
							type: 'warning'
						}).then(() => {
							if (this.isEdit) {
								this.loading = true
								updateModel(this.$route.query.id, this.model).then(response => {
									if (response.error == '00000000') {
										this.$refs[formName].resetFields();
										this.model.strategyOutputList = []
										this.$message({
											message: '修改成功',
											type: 'success',
											duration: 1000
										});
										this.$emit('close')
									}
									this.loading = false
								});
							} else {
								this.loading = true
								saveModel(this.model).then(response => {
									if (response.error == '00000000') {
										this.$refs[formName].resetFields();
										this.model.strategyOutputList = []
										this.model = JSON.parse(JSON.stringify(defaultModel))
										this.$message({
											message: '提交成功',
											type: 'success',
											duration: 1000
										});
										this.$emit('close')
									}
									this.loading = false
								});
							}
						});

					} else {
						this.$message({
							message: '验证失败',
							type: 'error',
							duration: 1000
						});
						return false;
					}
				});
			},
			resetForm(formName) {
				this.$refs[formName].resetFields();
				this.model = JSON.parse(JSON.stringify(defaultModel));
				this.model.strategyOutputList = []
			}
		}
	}
</script>
<style scoped>
	.detailmodel {
		padding-bottom: 20px;
		height: 100%;
		overflow: scroll;
		overflow-x: hidden;
	}

	.text {
		font-size: 14px;
	}

	.item {
		margin-bottom: 18px;
	}

	.clearfix:before,
	.clearfix:after {
		display: table;
		content: "";
	}

	.clearfix:after {
		clear: both
	}

	.box-card {
		width: 582px;
	}

	.cWidth {
		width: 650px;
	}

	.card_left {
		margin: 0 0px 24px 68px;
	}

	.padding_bot {
		padding-bottom: 60px;
	}

	.overSro {
		height: 220px !important;
		overflow: auto;
	}
</style>

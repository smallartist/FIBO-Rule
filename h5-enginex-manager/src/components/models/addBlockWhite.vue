<template>
	<div class="blackwhite-edit-wrapper">
		<el-dialog :title="title" :visible.sync="dialogVisible" width="70%" :before-close="handleClose">
			<el-form ref="form" :model="form" :rules="rules" label-width="120px" :class="disabled?'mask-wrapper':''">
				<el-row>
					<el-col :span="6">
						<div class="grid-conten">
							<el-form-item label="名称" prop="listName">
								<el-input v-model="form.listName"></el-input>
							</el-form-item>
						</div>
					</el-col>
					<!-- <el-col :span="6">
						<div class="grid-conten">
							<el-form-item label="数据来源" prop="dataSource">
								<el-select v-model="form.dataSource" placeholder="请选择数据来源">
									<el-option label="待选" value="0"></el-option>
									<el-option label="外部" value="1"></el-option>
									<el-option label="内部" value="2"></el-option>
								</el-select>
							</el-form-item>
						</div>
					</el-col> -->
					<!-- <el-col :span="6">
						<div class="grid-conten">
							<el-form-item label="名单库类型" prop="listType">
								<el-select v-model="form.listType" placeholder="请选择名单库类型">
									<el-option label="白名单" value="w"></el-option>
									<el-option label="黑名单" value="b"></el-option>
								</el-select>
							</el-form-item>
						</div>
					</el-col> -->
					<!-- <el-col :span="6">
						<el-form-item label="名单库属性" prop="listAttr">
							<el-input v-model="form.listAttr"></el-input>
						</el-form-item>
					</el-col> -->
					<el-col :span="12">
						<el-form-item label="描述" prop="listDesc">
							<el-input type="input" v-model="form.listDesc"></el-input>
						</el-form-item>
					</el-col>
				</el-row>

				<!-- <el-form-item label="描述" prop="listDesc">
					<el-input type="textarea" v-model="form.listDesc"></el-input>
				</el-form-item> -->

				<el-form-item label="" prop="matchType">
					<el-radio-group v-model="form.matchType">
						<el-radio label="精准匹配"></el-radio>
						<el-radio label="模糊匹配"></el-radio>
					</el-radio-group>
				</el-form-item>
				<el-form-item label="名单库字段" prop="queryType">
					<el-radio-group v-model="form.queryType">
						<el-radio label="or"></el-radio>
						<el-radio label="and"></el-radio>
					</el-radio-group>
				</el-form-item>

				<el-form-item label="">
					<div class="tag-wrapper">
						<el-tag :key="tag.fieldCn" v-for="tag in tableColumn" :closable="!dataItem.id"
							:disable-transitions="false" @close="tagClose(tag)">
							{{tag.fieldCn}}
						</el-tag>
						<el-button class="button-new-tag" size="small" icon="el-icon-circle-plus-outline"
							@click="addTag1"></el-button>
					</div>
				</el-form-item>
				<el-form-item label="查询匹配字段">
					<div class="tag-wrapper">
						<el-tag :key="tag.fieldCn" v-for="tag in queryField" :closable="!dataItem.id"
							:disable-transitions="false" @close="tagClose1(tag)">
							{{tag.fieldCn}}
						</el-tag>
						<el-button class="button-new-tag" size="small" icon="el-icon-circle-plus-outline"
							@click="addTag2"></el-button>
					</div>
				</el-form-item>
				<el-form-item label="输出字段" prop="resultFieldEn">

					<outcontent :outcontent="form.strategyOutputList" type="list_db" :zhezhao="false"
						style="margin-top: 20px;margin-left: 50px;">

						<div style="display:flex; align-items: center;">
							<el-select v-model="form.resultFieldEn" filterable placeholder="请选择" style="width: 200px;">
								<el-option v-for="item in FieldUser" :key="item.id" :label="item.fieldCn"
									:value="item.fieldEn">
								</el-option>
							</el-select>
							<p style="margin: 10px;">
								=
							</p>
							<el-select filterable value="是否命中" disabled style="width: 255px;">
							</el-select>
						</div>

					</outcontent>


				</el-form-item>

				<el-form-item v-if="!disabled">
					<el-button type="primary" @click="onSubmit('form')">确认</el-button>
					<el-button @click="handleClose">取消</el-button>
				</el-form-item>
			</el-form>
		</el-dialog>
		<field-user-dialog @selEvent="selFielod" @fieldClose="fieldClose" :isShow="isshow" :type="isTpye"
			:radioList="radioList"></field-user-dialog>
	</div>
</template>
<script>
	import FieldUserDialog from './fieldUserDialog.vue'
	import {
		saveBlackWhiteList,
		updateBlackWhiteList
	} from '@/api/index.js'
	import outcontent from '@/components/models/outcontent.vue'
	var validateName = (rule, value, callback) => {
		if (value === '') {
			callback(new Error('请输入名称'));
		} else if (value.indexOf('@') != -1 || value.indexOf('%') != -1) {
			callback(new Error('名称中不能含有 ‘@’ ‘%’ '));
		} else {
			callback();
		}
	};
	export default {
		name: 'addBlockWhite',
		components: {
			FieldUserDialog,
			outcontent
		},
		props: {
			dialogVisible: {
				type: Boolean,
				default: false
			},
			dataItem: {
				type: Object,
				default () {
					return {}
				}
			},
			disabled: {
				type: Boolean,
				default () {
					return false
				}
			},
			title: {
				type: String,
				default () {
					return "添加编辑"
				}
			}
		},
		computed: {
			FieldUser() {
				if (this.$store.state.FieldUser) {
					return this.$store.state.FieldUser.data.fieldList
				} else {
					return this.$store.state.FieldUser
				}
			}
		},
		watch: {
			dataItem(newVal) {
				if (JSON.stringify(newVal) !== '{}') {
					this.form = {
						listName: newVal.listName,
						// dataSource: newVal.dataSource.toString(),
						listType: newVal.listType,
						listDesc: newVal.listDesc,
						matchType: newVal.matchType == 1 ? '精准匹配' : '模糊匹配',
						queryType: newVal.queryType == 1 ? 'and' : 'or',
						// listAttr: newVal.listAttr,
						resultFieldEn: newVal.resultFieldEn,
						strategyOutputList: newVal.strategyOutputList
					}
					let tableColumnArr = newVal.tableColumn.split(',');
					let queryFieldArr = newVal.queryField.split(',');
					this.FieldUser.forEach(value => {
						tableColumnArr.forEach(item => {
							if (value.id == item) {
								this.tableColumn.push(value);
							}
						})
						queryFieldArr.forEach(item => {
							if (value.id == item) {
								this.queryField.push(value);
							}
						})
					})
				} else {
					if (this.$refs['form'] != undefined) {
						this.$refs["form"].resetFields();
					}
				}
			},
			dialogVisible: function(val, oldVla) {
				if (this.$refs['form'] != undefined) {
					this.$refs["form"].resetFields();
				}
			}
		},
		data() {
			return {
				text: "text",
				isshow: false,
				form: {
					strategyOutputList: [],
					resultFieldEn: '',
					listName: '',
					// dataSource: '',
					listType: 'b',
					listDesc: '',
					matchType: '',
					queryType: '',
					// listAttr: ''
				},
				rules: {
					listName: [{
						required: true,
						validator: validateName,
						trigger: 'blur'
					}],
					listDesc: [{
						required: true,
						message: '请输入描述',
						trigger: 'blur'
					}],
					matchType: [{
						required: true,
						message: '请选择匹配类型',
						trigger: 'change'
					}],
					queryType: [{
						required: true,
						message: '请选择维护字段',
						trigger: 'change'
					}],
					resultFieldEn: [{
						required: true,
						message: '选择输出字段',
						trigger: 'blur'
					}]
				},
				isTpye: 0,
				tableColumn: [],
				queryField: [],
				radioList: []
			}
		},
		methods: {
			selFielod(data) {
				let check = true;
				if (this.isTpye === 0) {
					this.tableColumn.forEach(value => {
						if (value.id === data.id) {
							this.$message.error('选择项重复，请重新选择一个')
							this.addTag1();
							check = false;
						}
					})
					check && this.tableColumn.push(data)
				} else {
					this.queryField.forEach(value => {
						if (value.id === data.id) {
							this.$message.error('选择项重复，请重新选择一个')
							this.addTag2();
							check = false;
						}
					})
					check && this.queryField.push(data);
				}

			},
			fieldClose() {
				this.isshow = false;
			},
			// 新增或编辑名单库
			onSubmit(form) {
				console.log(form)
				let is = false
				this.form.strategyOutputList.forEach(value => {
					if (value.fieldId === "" || String(value.fieldValue).trim() === "" || value.variableType ===
						"") {
						is = true
					}
				})
				if (is) {
					this.$message.error('请检查输出字段是否未填')
					return
				}
				this.$refs[form].validate((valid) => {
					if (valid) {
						let form = this.form;
						let tableColumn = [],
							queryField = [];
						this.tableColumn.forEach(item => {
							tableColumn.push(item.id)
						})
						this.queryField.forEach(item => {
							queryField.push(item.id)
						})


						if (tableColumn.length <= 0) {
							this.$message.error("请选择维护字段");
							return
						}
						if (queryField.length <= 0) {
							this.$message.error("请选择查询主键");
							return
						}

						form.tableColumn = tableColumn.join(',');
						form.queryField = queryField.join(',');

						form.matchType = form.matchType == "精准匹配" ? 1 : 0
						// form.dataSource = parseInt(form.dataSource)
						form.queryType = form.queryType == 'and' ? 1 : 0

						console.log(form)




						if (JSON.stringify(this.dataItem) == '{}') {

							// 新增
							this.save(form);
						} else {
							// 编辑
							form.id = this.dataItem.id



							this.update(form);
						}
					} else {

						return false;
					}
				});
			},
			async save(form) {
				const data = await saveBlackWhiteList(form)
				if (data.status != "0") {
					this.$message({
						message: '添加成功！',
						type: 'success'
					});
					this.handleClose();
				}
			},
			async update(form) {
				const data = await updateBlackWhiteList(form)
				if (data.data) {
					this.$message({
						message: '修改成功！',
						type: 'success'
					});
					this.handleClose();
				}
			},
			addTag1() {
				this.isTpye = 0
				this.isshow = true;
				this.radioList = this.FieldUser
			},
			addTag2() {
				this.isTpye = 1
				this.isshow = true;
				this.radioList = this.tableColumn
			},
			tagClose(tag) {
				this.tableColumn.splice(this.tableColumn.indexOf(tag), 1);
				if (this.queryField.indexOf(tag) != -1) {
					this.queryField.splice(this.queryField.indexOf(tag), 1);
				}
			},
			tagClose1(tag) {
				this.queryField.splice(this.queryField.indexOf(tag), 1);
			},
			handleClose(done) {
				this.tableColumn = [];
				this.queryField = [];
				this.form = {
					listName: '',
					// dataSource: '',
					listType: 'b',
					listDesc: '',
					matchType: '',
					queryType: '',
					// listAttr: '',
					resultFieldEn: '',
					strategyOutputList: []
				}
				this.$emit('closeEvent');
			}
		}
	}
</script>

<style>
	.el-tag {
		margin-left: 10px;
	}

	.blackwhite-edit-wrapper .tag-wrapper {
		border: 1px #E6E6E6 solid;
		height: 120px;
		border-radius: 4px;
		display: flex;
		align-items: center;
		overflow-y: scroll;
		display: flex;
		flex-wrap: wrap;
		padding: 10px 0;
	}

	.blackwhite-edit-wrapper .tag-wrapper span {
		margin: 10px 0 10px 10px;
	}

	.blackwhite-edit-wrapper .button-new-tag {
		margin-left: 10px;
		height: 32px;
		line-height: 30px;
		padding-top: 0;
		padding-bottom: 0;
		border: none;
		font-size: 24px;
	}

	.mask-wrapper {
		position: relative;
		z-index: 3;
	}

	.mask-wrapper::before {
		width: 100%;
		height: 100%;
		content: "";
		position: absolute;
		left: 0;
		top: 0;
		background-color: rgba(0, 0, 0, 0);
		z-index: 4;
	}
</style>

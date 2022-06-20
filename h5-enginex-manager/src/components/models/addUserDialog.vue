<template> 
	<div class="user-edit-wrapper">
		<el-dialog title="配置账号" :visible.sync="dialogVisible" width="50%" @open="open" :before-close="handleClose">
			<el-form ref="myform" :model="form" :rules="rules" label-width="80px" label-position="left">
				<el-row>
					<el-col :span="8">
						<div class="grid-conten">
							<el-form-item label="账号" prop="account">
								<el-input v-model="form.account"></el-input>
							</el-form-item>
						</div>
					</el-col>
					<el-col :span="8">
						<div class="grid-conten">
							<el-form-item label="公司" prop="organId">
								<el-select v-model="form.organId" :disabled="listOrganArr.length==1?true:false" placeholder="" @change="organChange">
									<el-option v-for="item in listOrganArr" :key="item.id" :label="item.name" :value="item.id"></el-option>
								</el-select>
							</el-form-item>
						</div>
					</el-col>
					<el-col :span="8">
						<div class="grid-conten">
							<el-form-item label="角色">
								<el-select v-model="form.sysRole" placeholder="">
									<el-option v-for="item in validRoleArr" :key="item.id" :label="item.roleName" :value="item.id"></el-option>
								</el-select>
							</el-form-item>
						</div>
					</el-col>
				</el-row>

				<el-divider></el-divider>
				<h3 style="margin-bottom:40px">
					使用人信息：
				</h3>

				<el-row>
					<el-col :span="12">
						<div class="grid-conten">
							<el-form-item label="员工编号" prop="employeeId">
								<el-input v-model="form.employeeId"></el-input>
							</el-form-item>
						</div>
					</el-col>
					<el-col :span="12">
						<div class="grid-conten">
							<el-form-item label="姓名" prop="nickName">
								<el-input v-model="form.nickName"></el-input>
							</el-form-item>
						</div>
					</el-col>
				</el-row>

				<el-row>
					<el-col :span="12">
						<div class="grid-conten">
							<el-form-item label="手机号" prop="cellphone">
								<el-input v-model="form.cellphone"></el-input>
							</el-form-item>
						</div>
					</el-col>
					<el-col :span="12">
						<div class="grid-conten">
							<el-form-item label="邮箱" prop="email">
								<el-input v-model="form.email"></el-input>
							</el-form-item>
						</div>
					</el-col>
				</el-row>


				<el-form-item class="btn-group">
					<el-button type="primary" @click="onSubmit('myform')">确认</el-button>
					<el-button @click="handleClose()">取消</el-button>
				</el-form-item>
			</el-form>
		</el-dialog>
	</div>
</template>
<script>
	import {
		getAllValidRole,
		saveUser,
		updateUser
	} from '@/api/index.js'
	export default {
		name: 'userDialog',
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
			listOrganArr: {
				type: Array,
				default () {
					return []
				}
			}
		},
		created() {

		},
		watch: {
			dataItem(newVal) {
				this.dataItem = newVal;
				if (JSON.stringify(newVal) !== '{}') {
					this.validRole(newVal.sysOrgan.id);
					this.disabled = true;
					this.form = {
						organId: newVal.organId,
						employeeId: newVal.employeeId,
						account: newVal.account,
						nickName: newVal.nickName,
						cellphone: newVal.cellphone,
						email: newVal.email,
						sysRole: newVal.sysRole.id
					}
				}
			},
			dialogVisible: function(val, oldVla) {
				console.log(1)
				if (this.$refs['myform'] != undefined) {
					this.$refs["myform"].resetFields();

				}
			}
		},
		data() {
			let checkPhone = (rule, value, callback) => {
				if (!value) {
					return callback(new Error('手机号不能为空'));
				} else {
					const reg = /^1[3|4|5|7|8][0-9]\d{8}$/
					if (reg.test(value)) {
						callback();
					} else {
						return callback(new Error('请输入正确的手机号'));
					}
				}
			};
			return {
				text: "text",
				form: {
					organId: '',
					employeeId: '',
					account: '',
					nickName: '',
					cellphone: '',
					email: '',
					sysRole: ''
				},
				disabled: false,
				validRoleArr: [],
				rules: {
					account: [{
						required: true,
						message: '请输入账号',
						trigger: 'blur'
					}],
					nickName: [{
						required: true,
						message: '请输入姓名',
						trigger: 'blur'
					}],
					cellphone: [{
						validator: checkPhone,
						required: true,
						trigger: 'blur'
					}],
					email: [{
							required: true,
							message: '请输入邮箱',
							trigger: 'blur'
						},
						{
							type: 'email',
							message: '请输入正确的邮箱地址',
							trigger: ['blur', 'change']
						}
					],
					employeeId: [{
						required: true,
						message: '请输入员工编号',
						trigger: 'blur'
					}],
					organId: [{
						required: true,
						message: '请选择公司',
						trigger: 'change'
					}]
				}
			}
		},
		methods: {
			open() {
				if (this.listOrganArr.length == 1) {
					this.form.organId = this.listOrganArr[0].id
					this.organChange(this.listOrganArr[0].id)
				}
			},
			organChange(e) {
				this.validRoleArr = [];
				this.form.sysRole = "";
				this.validRole(e);
			},
			// 新增或编辑名单库
			onSubmit(myForm) {
				let form = this.form;
				form.sysRole = {
					id: this.form.sysRole
				}
				this.$refs[myForm].validate((valid) => {
					if (valid) {
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
				const data = await saveUser(form)
				if (data.status != "0") {
					this.$message({
						message: '添加成功！',
						type: 'success'
					});
					this.handleClose();
				}

			},
			async update(form) {
				const data = await updateUser(form)
				if (data.status != "0") {
					if (data.data) {
						this.$message({
							message: '修改成功！',
							type: 'success'
						});
						this.handleClose();
					}
				}
			},
			async validRole(id) {
				const data = await getAllValidRole({
					"organId": id
				})
				if (data.status != "0") {
					this.validRoleArr = data.data
				}
			},
			handleClose() {
				this.form = {
					organId: '',
					employeeId: '',
					account: '',
					nickName: '',
					cellphone: '',
					email: '',
					sysRole: ''
				}
				this.validRoleArr = [];
				this.disabled = false;
				this.$emit('closeEvent');
			}
		}
	}
</script>

<style>
	.el-dialog__body {
		padding: 20px 40px;
	}

	.user-edit-wrapper .el-input {
		width: 80%;
	}

	.user-edit-wrapper .btn-group {
		margin-top: 30px;
	}
</style>

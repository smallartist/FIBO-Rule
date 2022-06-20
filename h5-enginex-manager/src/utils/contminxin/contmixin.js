export default {
	data() {
		return {

		}
	},
	created() {
		
	},
	computed: {
		showRight() {
			if (this.currid) {
				return true
			} else {
				return false
			}
		},
		listunfold() {
			let arr = []

			this.deepGetListunfold(this.list, arr)

			console.log(arr)
			return arr
		}
	},
	mounted() {},
	methods: {
		
		deepGetListunfold(list, arr) {
			list.forEach(value => {
				arr.push(value)
				if (value.children.length > 0) {
					this.deepGetListunfold(value.children, arr)
				}
			})
		},
		mixnewFileZindexVerify(arr,id){
			let ZIndex = 1
			b(arr)
			function b(arr){
				console.log(arr)
				arr.forEach(value=>{
					if(value.id==id){
						ZIndex = value.ZIndex
					}
					if(value.children.length>0){
						b(value.children)
					}
				})
			}
			
			return ZIndex
			
			
			
			
		},
		mixnewFileSure(params) {
			let is = false
			let parentId = params.parentId
			let tempId = this.currid
			if (this.tempNewF.trim() == "") {
				this.$message.error('文件夹名不能为空');
				this.leftloading = false
				return
			}
			
			this.tempNewF = this.tempNewF.trim()
			this.deepGetCurr(parentId, this.list, (value) => {
				value.children.forEach(item => {
					if (item.name == params.name || item.name == params.fieldType) {
						is = true
					}
				})
			})

			// 验证重名

			if (is) {
				this.$message.error('同一文件夹下不允许同名');
				this.leftloading = false
				return
			}

			if (params.parentId == "99999999") {
				params.parentId = "0"
			}
			this.getData.addlist(params).then((res) => {
				if (res.status === "1") {
					this.$message({
						message: '添加成功',
						type: 'success'
					});
					this.leftloading = false
					this.deepGetCurr(parentId, this.list, (value) => {
						value.children.push({
							ZIndex: value.ZIndex + 1,
							name: this.tempNewF,
							id: res.data.fieldTypeId || res.data.node.id,
							open: false,
							show: value.open,
							Rename: false,
							parentId: parentId,
							children: []
						})

					})

				}
				this.newf = false
				this.tempNewF = ""
			}).catch(() => {
				this.$message.error("请求失败了" + '-_-');
				this.leftloading = false
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
				nameId: this.currid
			}

			this.getData.fieldusing(params).then(res => {
				if (res.status == "1") {
					this.$message({
						message: '操作成功',
						type: 'success'
					});
					if (this.getData.type == 1 || this.getData.type == 2) {
						this.$store.dispatch('reGetRuleList')
					}
					this.getlist()
					this.selection = []
				}
			})
		},
		clickCurrid(id) {

			this.currid = id
			this.currPage = 1

			this.listRedact = false;
			this.tempRedactId = 0
			this.selection = []
			let tempIs = false
			this.list.forEach(value => {
				if (value.id === id && value.Rename) {
					tempIs = true
				}
			})
			if (!tempIs) {
				this.RenameFun()
			}
			this.getlist()
			this.deepGetCurr(id, this.list, deepSetCurr)

			// 递归寻找文件夹


			// 打开关闭文件夹
			function deepSetCurr(obj) {
				if (obj.open) {
					// 关闭文件夹
					obj.open = false
					deepCloseFile(obj)
				} else {
					// 打开文件夹
					obj.open = true
					obj.children.forEach(value => {
						value.show = true
					})
				}
			}

			function deepCloseFile(obj) {

				if (obj.children.length > 0) {
					obj.children.forEach(value => {
						value.show = false
						deepCloseFile(value)
					})
				}

			}


		},
		deepGetCurr(id, item, fn, fn2) {
			item.forEach((value, index) => {
				if (value.id == id) {
					fn(value, item, index)
				} else {
					if (fn2) {
						fn2(value, item, index)
					}
					if (value.children.length > 0) {
						this.deepGetCurr(id, value.children, fn, fn2)
					}
				}




			})
		},
		listTreeDeep(item, num) {
			let arr = item.map((value) => {
				return {
					id: value.id,
					parentId: value.parentId,
					name: value.name || value.fieldType,
					ZIndex: num,
					open: false,
					show: num === 1 ? true : false,
					Rename: false,
					children: this.listTreeDeep(value.children, num + 1)
				}
			})


			return arr
		},
		RenameFun(id) {
			this.deepGetCurr(id, this.list, (value) => {
				value.Rename = true
			}, (value) => {
				value.Rename = false
			})
		},
		newFile() {
			if (!this.currid) {
				this.$message({
					message: '请先选择左侧文件夹',
					type: 'warning'
				});
			} else {
				this.newf = true
			}
		},
		RenameClose() {
			this.deepGetCurr(0, this.list, () => {}, (value) => {
				value.Rename = false
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
			this.getData.updatalist(params).then(res => {
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
		delectFun(id) {
			let name
			let parentId
			this.deepGetCurr(id, this.list, (value) => {
				name = value.name
				parentId = value.parentId
			})
			let params = {
				status: "-1",
				id: id,
				name: name,
				"type": "1",
				"engineId": "",
				"parentId": parentId == 99999999 ? 0 : parentId
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
		dialogShow(id) {
			this.tempRedactId = id
			this.listRedact = true
		},
		select(selection) {
			this.selection = selection
		},
		selectAll(selection) {
			this.selection = selection
		},
		fileRight(e, item) {
			this.tempHintLeft = e.x
			this.tempHintTop = e.y
			this.tempId = item.id
		},
		beforeUpload(file) {
			console.log(file, '文件');
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
			console.log('上传' + this.files.name)
			if (this.fileName == "") {
				this.$message.warning('请选择要上传的文件！')
				this.Uploadloading = false
				return false
			}
			let fileFormData = new FormData();
			fileFormData.append('file', this.files); //filename是键，file是值，就是要传的文件，test.zip是要传的文件名
			this.getData.fieldsubmit(fileFormData).then(res => {
				if (res.status === "1") {



					this.callbackresult = res.data
					this.upCallbackShow = true
					this.upShowClose()
					this.getlist()
					this.$store.dispatch('reGetRuleList')
					this.$store.dispatch('reGetfielduser')
				}
				this.Uploadloading = false
			})





		},
		upShowClose() {
			this.upShow = false
		}
	}
}

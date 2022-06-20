export default {
	install(Vue) {
		Vue.mixin({
			data() {
				return {
					mixinlogical: [{
						value: '&&',
						label: 'AND'
					},{
						value: '||',
						label: 'OR'
					}]
				}
			},
			created() {

			},
			computed: {
				FieldUserObj() {
					return this.$store.state.FieldUserObj ? this.$store.state.FieldUserObj.data.fieldList : []
				},
				FieldUser() {
					return this.$store.state.FieldUser ? this.$store.state.FieldUser.data.fieldList : []
				},
				Sourcelist() {
					return this.$store.state.Sourcelist ? this.$store.state.Sourcelist:[]
				}
			},
			mounted() {},
			methods: {
				randomAdd(item,is) {
					if(is||!item){
						return
					}
					if (item.random) {
						setTimeout(() => {
							item.random++
						}, 200)
					} else {
						// item.random=1
						setTimeout(() => {
							this.$set(item, 'random', 1)
						}, 200)
					}
				},
				verificationCode(str){
					let z = /^(?!_)(?!.*?_$)[a-zA-Z0-9_]+$/
					if(!z.test(str)){
						this.$message.error('Code只允许使用 字母 数字 下划线 且不能以下划线开头')
						return true
					}
					return false
				},
				verificationName(str){
					let z = /^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]+$/
					if(!z.test(str)){
						this.$message.error('名称只允许使用 中文 字母 数字 下划线 且不能以下划线开头')
						return true
					}
					return false
				},
				mixinSaveJSON(data, filename){
					if(!data) {
						this.$message.error('保存的数据为空');
						return;
					}
					if(!filename) 
						filename = 'json.json'
					if(typeof data === 'object'){
						data = JSON.stringify(data, undefined, 4)
					}
					var blob = new Blob([data], {type: 'text/json'}),
					e = document.createEvent('MouseEvents'),
					a = document.createElement('a')
					a.download = filename
					a.href = window.URL.createObjectURL(blob)
					a.dataset.downloadurl = ['text/json', a.download, a.href].join(':')
					e.initMouseEvent('click', true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null)
					a.dispatchEvent(e)
				},
				mixinGetValueTypeByJSONEn(e){
					if(!e||e.length==0) return
					if(!Array.isArray(e)) e=e.split('.')
					let obj =this.FieldUserObj
					e.forEach((value,index)=>{
						if(index) obj = obj['children']
						obj = obj.find(x=>x.value==value)
					})
					return obj.valueType
					
				},
				mixinGetValueByCn(e) {
					let num  = this.FieldUser.find(x=>x.fieldCn===e)
					// this.FieldUser.forEach(value => {
					// 	if (value.fieldCn === e) {
					// 		num = value
					// 	}
					// })
					return num
				},
				mixinReGetStorage(){
					this.$store.dispatch('reGetfielduser')
					this.$store.dispatch('reGetRuleList')
					
					Object.keys(this.$store.state.cacheList).forEach(value=>{
						this.$store.dispatch('regetcache', value)
					})
					
				},
				mixinGetLogical(str){
					let num  = this.mixinlogical.find(x=>x.value===str)
					// this.mixinlogical.forEach(value=>{
					// 	if(str==value.value){
					// 		num = value.label
					// 	}
					// })
					return num&&num.label
					
				},
				mixinGetValueById(e) {
					let num  =this.FieldUser.find(x=>x.id===e)
					// this.FieldUser.forEach(value => {
					// 	if (value.id === e) {
					// 		num = value
					// 	}
					// })
					return num
				},
				mixinGetCnByEn(e) {
					let num  =this.FieldUser.find(x=>x.fieldEn===e)
					// this.FieldUser.forEach(value => {
					// 	if (value.fieldEn === e) {
					// 		num = value.fieldCn
					// 	}
					// })
					return num&&num.fieldCn
				},
				mixinGetIdByEn(e) {
					let num =this.FieldUser.find(x=>x.fieldEn===e)
					// this.FieldUser.forEach(value => {
					// 	if (value.fieldEn === e) {
					// 		num = value.id
					// 	}
					// })
					return num&&num.id
				},
				mixinGetValueTypeByEn(e) {
					let num =this.FieldUser.find(x=>x.fieldEn===e)
					// this.FieldUser.forEach(value => {
					// 	if (value.fieldEn === e) {
					// 		num = value.valueType
					// 	}
					// })
					return num&&num.valueType
				},
				mixinGetFieldByEn(e) {
					let num  =this.FieldUser.find(x=>x.fieldEn=== e)
					// this.FieldUser.forEach(value => {
					// 	if (value.fieldEn === e) {
					// 		num = value
					// 	}
					// })
					return num
				},
				mixinMoveChange(e) {

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
					this.getData.updateParent(params).then(res => {
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
				mixinDeepCopy() {
					let result;
					if (typeof target === 'object') {
						if (Array.isArray(target)) {
							result = [];
							for (let i in target) {
								result.push(this.mixinDeepCopy(target[i]))
							}
						} else if (target === null) {
							result = null;
						} else if (target.constructor === RegExp) {
							result = target;
						} else {
							result = {};
							for (let i in target) {
								result[i] = this.mixinDeepCopy(target[i]);
							}
						}
					} else {
						result = target;
					}
					return result;
				},
				mixinGetvalueType(cont) {
					let num = this.FieldUser.find(x=>x.id===parseInt(cont))
					
					
					// this.FieldUser.forEach(value => {
					// 	if (value.id === parseInt(cont)) {
					// 		num = value.valueType
					// 	}
					// })
					
					return num&&num.valueType
				},
				mixinGetvalueEn(cont) {
					let num = this.FieldUser.find(x=>x.id===parseInt(cont))
					// this.FieldUser.forEach(value => {
					// 	if (value.id === parseInt(cont)) {
					// 		num = value.fieldEn
					// 	}
					// })
					return num&&num.fieldEn
				},
				mixinGetvalueCn(cont) {
					let num = this.FieldUser.find(x=>x.id===parseInt(cont))
					// this.FieldUser.forEach(value => {
					// 	if (value.id === parseInt(cont)) {
					// 		num = value.fieldCn
					// 	}
					// })
					if(!num&&cont!==""&&cont!==null){
						console.log(num,cont)
						num="(此字段丢失)"
					}
					return num&&num.fieldCn
				},
				mixinGetvalueCode(cont) {
					let num = this.FieldUser.find(x=>x.id===parseInt(cont))
					
					return num &&num.fieldCode
					
					
				}
			}
		})
	}
}

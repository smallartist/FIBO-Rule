<template>

	<div :style="{display:show?'block':'none',position:'relative'}">
		<div @click="opening()" :style="{marginLeft:(5+retract*10)+'px',display:show?'flex':'none'}" class="file_file"  @contextmenu.prevent="opening();$emit('fileRight',{e:$event,item:item})">
			<p style="width: 16px;">
				<i :class="open?'el-icon-arrow-down':'el-icon-arrow-right'" :style="{display:iconshow?'inline':'none'}"></i>
			</p>
			<i :class="iconshow?open?'el-icon-folder-opened file_icon':'el-icon-folder file_icon':'el-icon-folder file_icon'"></i>
			<span class="file_name" v-show="Rename">
				<el-input v-model="inputValue" placeholder="回车键确认,esc取消" size="mini" ref="input" @keyup.enter="submit"></el-input>
			</span>
			<span class="file_name" v-show="!Rename">{{name}}</span>
		</div>
	</div>
</template>

<script>
	import bus from '@/components/common/bus.js'
	export default {
		props: {
			item: {
				type: Object,
				default () {
					return {}
				}
			}
		},
		data() {
			return {
				name: '',
				Rename: '',
				retract: '',
				open: '',
				show: '',
				
				id: '',
				inputValue: ''
			}
		},
		mounted() {
			this.$refs.input.$refs.input.onkeydown = (e) => {
				this.keyDown(e)
			}
			this.setItem(this.item)
		},
		methods: {
			setItem(e){
				this.name =e.name
				this.Rename=e.Rename
				this.retract=e.ZIndex
				this.open=e.open
				this.show=e.show
				this.id=e.id
			},
			keyDown(e) {
				// console.log(e)
				if (e.key === "Enter") {
					if (this.inputValue.length > 20) {
						this.$message({
							message: '最大长度20个字符',
							type: 'warning'
						});
					} else {
						let params = {
							name: this.inputValue.trim(),
							id: this.id
						}
						this.$emit("updatafilelist", params)


					}
				} else if (e.key === "Escape") {
					this.inputValue = ""
					this.$emit('RenameClose', this.id)

				}
			},

			opening() {
				if (!this.Rename) {
					this.$emit('curr', this.id)
				}

			},
			submit(e) {
				console.log(1)
			}
		},
		watch: {
			Rename() {
				if (this.Rename === true) {
					setTimeout(() => {
						this.$refs.input.focus()
					}, 10)
				}
			},
			item: {
				deep: true, //深度监听设置为 true
				handler: function(e) {
					this.setItem(e)
				}
			}
		},
		computed:{
			iconshow(){
				return !!this.item.children.length
			}
		}






	}
</script>

<style>
	.file_file {
		transition: all .3s;
		align-items: center;
		padding: 5px;
		-moz-user-select: none;
		-webkit-user-select: none;
		-ms-user-select: none;
		-khtml-user-select: none;
		user-select: none;
	}

	.file_icon {
		margin-right: 5px;
	}

	.file_name {
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
	}
</style>

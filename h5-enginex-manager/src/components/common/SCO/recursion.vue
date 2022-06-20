<style>
	.SCO_defen {
		position: absolute;
		right: -200px;
		display: flex;
		align-items: center;
		justify-content: flex-start;
		width: 200px;
		height: 50px;
		border-bottom: 1px solid #ddd;
		border-right: 1px solid #ddd;
		/* margin-top: 1px; */
	}

	.el-dropdown-link {
		cursor: pointer;
		color: #409EFF;
	}

	.el-icon-arrow-down {
		font-size: 12px;
	}

	.demonstration {
		display: block;
		color: #8492a6;
		font-size: 14px;
		margin-bottom: 20px;
	}
</style>
<template>
	<div style="width: 100%;">
		<div v-for="(item,index) in data.children" :style="{'boxSizing':'border-box','marginLeft':'-1px',display:'flex',borderLeft:'1px solid #ddd',minHeight:'50px',borderBottom:index==(data.children.length-1)?'':'1px solid #ddd'}">
			<div style="min-width: 295px;margin-left: 5px;margin-top: 5px;position: relative;">


				<div style="margin-left: 20px;">
					<textSelect :data="item" :Number="true"></textSelect>
					<textInput :text="String(item.condition)" @input="item.condition=$event"></textInput>
				</div>
				<div style="position: absolute;left: 0;top: 0;font-size: 12px;color: #409EFF;">
					<el-dropdown @command="handleCommand" trigger="click">
						<span class="el-dropdown-link">
							<i class="el-icon-s-operation"></i>
						</span>
						<el-dropdown-menu slot="dropdown">
							<el-dropdown-item icon="el-icon-bottom" :command="'addBrother|'+index">向下添加</el-dropdown-item>
							<el-dropdown-item icon="el-icon-right" :command="'addSon|'+index" v-show="!item.children.length">向右添加</el-dropdown-item>
							<el-dropdown-item icon="el-icon-close" :command="'delect|'+index">删除此字段</el-dropdown-item>
							<el-dropdown-item icon="el-icon-document-copy" :command="'copy|'+index">复制此字段</el-dropdown-item>
						</el-dropdown-menu>
					</el-dropdown>
				</div>
			</div>
			<recursion v-if="item.children.length>0" :data="item" :tier="tier+1"></recursion>
			<div v-else class="SCO_defen" >
				<textSelect :data="item" :type="2"  @change="item.score=1;item.coefficient=1;item.custom='';"></textSelect>
				<textInput style="width: 110px;" v-show="item.calculateType==1" :text="String(item.score)" :examine="2" @input="item.score=$event"></textInput>
				<textInput style="width: 110px;" v-show="item.calculateType==2" :text="String(item.coefficient)" :examine="2" @input="item.coefficient=$event"></textInput>
				<textCustom style="width: 110px;" v-if="item.calculateType==3" v-model="item.custom" ></textCustom>
				
			
			</div>
		</div>
	</div>
</template>

<script>
	
	import textInput from '@/components/common/textInput.vue'
	import textCustom from '@/components/common/textCustom.vue'
	import textSelect from '@/components/common/textSelect.vue'
	import bus from './bus.js'
	export default {
		components: {
			textInput,
			textSelect,
			textCustom
		},
		name: 'recursion',
		props: {
			data: {
				type: Object,
				default () {
					return {}
				}
			},
			tier: {
				type: Number,
				default: -1
			}
		},
		created() {
			// console.log(this.tier,this.data)
			this.data.children.forEach(value=>{
				if(value.coefficient==null){
					value.coefficient = 0
				}
			})
		},
		beforeDestroy() {

		},
		methods: {
			
			handleCommand(str) {
				console.log(str)
				switch (str.split('|')[0]) {
					case 'addBrother':
						this.addBrother(str.split('|')[1])
						break;
					case 'delect':
						this.delect(str.split('|')[1])
						break;
					case 'addSon':
						this.addSon(str.split('|')[1])
						break;
					case 'copy':
						this.copy(str.split('|')[1])
						break;
				}
			},
			addSon(index) {
				this.data.children[index].children.push({
					condition: '',
					children: [],
					id: null,
					"score": 1,
					"fieldId":'',
					"coefficient": null,
					"calculateType": 1,
					custom:'',
				})
			},
			delect(index) {
				this.data.children.splice(Number(index), 1)
			},
			addBrother(index) {
				this.data.children.splice(Number(index) + 1, 0, {
					condition: '',
					children: [],
					"score": 1,
					"coefficient": null,
					"fieldId":'',
					"calculateType": 1,
					custom:'',
					id: null,
				})
				bus.$emit('look')
			},
			copy(index) {
				let obj = JSON.parse(JSON.stringify(this.data.children[index]))
				obj.id = null
				this.data.children.splice(Number(index) + 1, 0, obj)
			},
			
		}
	}
</script>

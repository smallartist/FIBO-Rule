<style>
	.textSelectSpan {
		font-size: 14px;
		width: 120px;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}

	.textSelectSpan:hover {
		overflow: unset;

	}
</style>
<template>
	<div style="display: flex;" v-if="type==1">
		<p v-show="!openSelect" @dblclick="openSelect=true;" class="textSelectSpan" :style="{'color':color}">{{data.fieldName?data.fieldName:'请选择指标'}}</p>
		<el-select v-model="data.fieldId" v-show="openSelect" filterable remote placeholder="请选择" size="mini" @change="change"
		 ref="select" @blur="blur" @visible-change="visibleChange">
			<el-option v-for="item in FieldUser" :key="item.id" :label="item.fieldCn" :value="item.id">
			</el-option>
		</el-select>
	</div>
	<div style="display: flex;width: 80px;margin-left: 5px;" v-else-if="type==2">
		<p v-show="!openSelect" @dblclick="openSelect=true;" class="textSelectSpan" :style="{'color':color}">{{data.calculateType==1?'分值':data.calculateType==2?'系数':'自定义'}}</p>
		<el-select v-model="data.calculateType" v-show="openSelect" filterable placeholder="请选择" size="mini" @change="change"
		 ref="select" @blur="blur" @visible-change="visibleChange">
			<el-option :key="1" label="分值" :value="1"></el-option>
			<el-option :key="2" label="系数" :value="2"></el-option>
			<el-option :key="3" label="自定义" :value="3"></el-option>
		</el-select>
		:
	</div>
	<div style="display: flex;width: 30px;margin-left: 5px;" v-else-if="type==3">
		<p v-show="!openSelect" @dblclick="openSelect=true;" class="textSelectSpan" :style="{'color':color}">{{data.operator?data.operator=='contains'?'包含':data.operator=='not contains'?'不包含':data.operator:'-'}}</p>
		<ruleRelation v-model="data.operator" :openValue2="false" :valueType="valueType" :openSelect="openSelect" :type="2" v-show="openSelect"
		 size="mini" @change="change" ref="select" @blur="blur" @visible-change="visibleChange" :andTextInput="true"></ruleRelation>
		:
	</div>
	<div style="display: flex;width: 40px;margin-left: 5px;" v-else-if="type==4">
		<p v-show="!openSelect" @dblclick="openSelect=true;" class="textSelectSpan" :style="{'color':color}">{{data.logical=='||'?'OR':'AND'}}</p>
		<el-select v-model="data.logical" v-show="openSelect" filterable placeholder="请选择" size="mini" @change="change" ref="select" @blur="blur" @visible-change="visibleChange">
			<el-option :key="1" label="AND" value="&&"></el-option>
			<el-option :key="2" label="OR" value="||"></el-option>
		</el-select>
	</div>
</template>

<script>
	import ruleRelation from '@/components/common/ruleRelation.vue'
	export default {
		components: {
			ruleRelation
		},
		props: {
			valueType: {
				type: Number,
				default: 1
			},
			type: {
				type: Number,
				default: 1
			},
			data: {
				type: Object,
				default () {
					return {}
				}
			},
			Number:{
				type:Boolean,
				default:false
			}
		},
		data() {
			return {
				color: '#000',
				openSelect: false
			}
		},
		created() {
			
			if (this.type == 1) {
				if (!this.data.fieldId) {
					this.data.fieldName = '请选择指标'
				} else {
					this.data.fieldName = this.mixinGetvalueCn(this.data.fieldId)
				}
				
			}
		},
		mounted() {


		},
		watch: {
			openSelect() {
				if (this.openSelect && this.type != 3) {
					this.$nextTick(() => {
						this.$refs.select.focus()
					})
				}
			},
			'data.operator'() {
				if (this.data.operator == "" || this.data.operator == "-") {
					this.color = '#F56C6C'
				}
			}
		},
		methods: {
			visibleChange(e) {
				if (!e) {
					
						this.openSelect = false
					
				}
			},
			blur() {
				setTimeout(() => {
					this.openSelect = false
				}, 200)
			},
			change(e) {


				// this.type==3?return : ''
				if(this.type==1){
					this.FieldUser.forEach(value => {
						if (value.id == e) {
							this.data.fieldName = value.fieldCn
						}
					})
				}
				setTimeout(() => {
					this.openSelect = false
				}, 200)
				this.color = '#409EFF'

				this.$emit('change')
			}
		},
		computed: {
			FieldUser() {
				if (this.$store.state.FieldUser == null) {
					return []
				} else {
					if(this.Number){
						return this.$store.state.FieldUser.data.fieldList.filter(value=>value.valueType==1)
					}else{
						return this.$store.state.FieldUser.data.fieldList
					}
				}

			},

		}
	}
</script>

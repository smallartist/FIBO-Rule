<template>

	<el-dialog :visible="strategyDialogVisible" width="70%"
		@update:visible="$emit('update:strategyDialogVisible',$event)" :show-close="false">
		<div style="position: relative;">
			<div style="position: absolute;height: 100%;width: 100%;z-index: 9999;">

			</div>
			<!-- {{data}} -->
			<div v-if="data.strategyType==2">
				<div v-if="data.difficulty==2">

					<type2 :ZIndex="1" :data="data.snapshot.ruleConditionVo"></type2>

					<div class="rule_outcontent_box">
						<p>命中输出：</p>
						<div class="rule_home" style="margin-top: 10px;">
							<div class="rule_fa">
								<el-button icon="el-icon-plus" circle @click="outAdd(0)" disabled></el-button>
								<el-button icon="el-icon-close" circle disabled='disabled' style="margin-right: 10px">
								</el-button>
							</div>
							<el-select v-model="data.snapshot.resultFieldEn" filterable placeholder="请选择"
								style="width: 200px;" clearable>
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

						<outcontent :outcontent="data.snapshot.strategyOutputList" :ruleOut="true" type="complex_rule"
							:outType="{
						outType: 'success'
					}">
							<div style="display: flex;align-items: center;">
								<el-select v-model="data.snapshot.scoreFieldEn" filterable placeholder="请选择"
									style="width: 200px;" clearable>
									<el-option v-for="item in FieldUser" :key="item.id" :label="item.fieldCn"
										:value="item.fieldEn">
									</el-option>
								</el-select>
								<p style="margin: 10px;">=</p>
								<div>
									<el-input v-model="data.snapshot.score" maxlength="30" style="width: 255px;">
										<template slot="prepend">得分</template>
									</el-input>
								</div>
							</div>
						</outcontent>
					</div>
					<br>
					<div class="rule_outcontent_box">
						<p>未命中输出:</p>
						<outcontent :outcontent="data.snapshot.failOutputList" :unone="true" :ruleOut="true"
							type="complex_rule" :outType="{
						outType: 'fail'
					}">

						</outcontent>
						<div>
						</div>
					</div>
				</div>
				<div v-if="data.difficulty==3">
					<el-input type="textarea" :rows="30" placeholder="请输入内容"
						:value="JSON.parse(data.snapshot.scriptContent).formula">
					</el-input>








				</div>


			</div>
			<div v-if="data.strategyType==4" class="watch_SCO">
				<div class="SCO_tableHeader">
					<div style="background-color: #409EFF;color: #fff;">维度</div>
					<div style="width: 100px;background-color: #409EFF;color: #fff;">权重</div>
					<div v-for=" value in tier" style="background-color: #409EFF;color: #fff;">
						指标{{value}}
					</div>
					<div style="border-top: 1px solid #ddd;border-right: 1px solid #ddd;margin-top: -1px;margin-left: 1px;background-color: #409EFF;color: #fff;width: 200px;">
						评分
					</div>
				</div>
				<div style="display: flex;margin-left: 20px;">
					
					<div style="border-right: 1px solid #ddd;border-left: 1px solid #ddd;border-top: 1px solid #ddd;">
						
						<div v-for="(value,index) in data.snapshot.scorecardDimension" style="min-height: 50px;display: flex;border-bottom: 1px solid #ddd;position: relative;">
							<div style="min-width: 300px;display: flex;align-items: center;justify-content: center;position: relative;">
								<div style="position: absolute;left: 0;top: 0;color: #409EFF;">
									<el-dropdown >
										<span class="el-dropdown-link">
											<i class="el-icon-s-operation"></i>
										</span>
										<el-dropdown-menu slot="dropdown" trigger="click">
											<el-dropdown-item icon="el-icon-bottom" :command="'addBrother|'+index">向下添加</el-dropdown-item>
											<el-dropdown-item icon="el-icon-right" :command="'addSon|'+index" v-show="!value.children.length">向右添加</el-dropdown-item>
											<el-dropdown-item icon="el-icon-close" :command="'delect|'+index" v-show="index!=0">删除此字段</el-dropdown-item>
											<el-dropdown-item icon="el-icon-document-copy" :command="'copy|'+index">复制此字段</el-dropdown-item>
										</el-dropdown-menu>
									</el-dropdown>
								</div>
								<textInput :text="String(value.dimensionName)" :center="true" :examine="3" ></textInput>
							</div>
							<div style="width: 100px;borderLeft:1px solid #ddd;display: flex;align-items: center;justify-content: center;flex-shrink: 0;">
								<textInput :text="String(value.weight)" :examine='2' :center="true" @input="value.weight=$event"></textInput>
							</div>
							<SCOrecursion :data="value" :tier="1"></SCOrecursion>
						</div>
					</div>
				</div>
				
				
				<div style="margin-top: 50px;margin-bottom: 20px;">
					<span style="font-size: 14px;margin-left: 20px;margin-right: 10px;color: #409EFF;">计算方式:</span>
					<el-select v-model="data.snapshot.scoreCalculateType" placeholder="请选择" size="mini">
						<el-option :key="1" label="求和" :value="1"></el-option>
						<el-option :key="2" label="加权求和" :value="2"></el-option>
					</el-select>
				</div>
				<outcontent :outcontent="data.snapshot.strategyOutputList" type="scorecard" style="margin-top: 20px;margin-left: 50px;">
					<div style="display:flex; align-items: center;">
						<el-select v-model="data.snapshot.resultFieldEn" filterable placeholder="请选择" style="width: 200px;" clearable>
							<el-option v-for="item in FieldUser" :key="item.id" :label="item.fieldCn" :value="item.fieldEn">
							</el-option>
						</el-select>
						<p style="margin: 10px;">
							=
						</p>
						<el-select filterable value="评分" disabled style="width: 255px;">
						</el-select>
					</div>
				</outcontent>
				
				
			</div>
			
			<div v-if="data.strategyType==17" style="background-color: #f3f3f3;padding: 20px;">
				<tree :treeData="data.snapshot.detailList[0]"></tree>

				<outcontent :outcontent="data.snapshot.strategyOutputList" type="decision_tree"
					style="margin-top: 20px;margin-left: 50px;">
					<div style="display:flex; align-items: center;">
						<el-select v-model="data.snapshot.resultFieldEn" filterable placeholder="请选择"
							style="width: 200px;" clearable>
							<el-option v-for="item in FieldUser" :key="item.id" :label="item.fieldCn"
								:value="item.fieldEn">
							</el-option>
						</el-select>
						<p style="margin: 10px;">
							=
						</p>
						<el-select filterable value="决策结果" disabled style="width: 255px;">
						</el-select>
					</div>
				</outcontent>


			</div>

			<div v-if="data.strategyType==16">
				<div class="dec" style="width: auto;">
					<div class="dec_top">
						<!-- top -->
						<div class="dec_table_header" :style="{width:width,flexShrink:'0'}">
						</div>
						<div class="dec_table_comm">
							<topRecursion :data="data.snapshot.topDetailVo" direction="row"></topRecursion>
						</div>
					</div>
					<div class="dec_cont" style="display: flex;">
						<recursion :data="data.snapshot.leftDetailVo" ref="left" id="left"
							style="flex-shrink: 0;flex-grow:0"></recursion>
						<div>
							<div v-for="(value,index) in data.snapshot.resultSet.resultList" :key="index"
								style="display: flex;">
								<div v-for="(item,inde) in value" :key="inde" class="dec_score"
									:style="{borderRight:inde==value.length-1?'1px solid #ddd':''}">
									<textInput :text="String(item)" click="click" :center="true" :examine="3"
										@input="input($event,index,inde)"></textInput>
								</div>
							</div>
						</div>
					</div>
				</div>
			
				<outcontent :outcontent="data.snapshot.strategyOutputList" type="decision_tables" style="margin-top: 20px;margin-left: 50px;">
					<div style="display:flex; align-items: center;">
						<el-select v-model="data.snapshot.resultFieldEn" filterable placeholder="请选择" style="width: 200px;" clearable>
							<el-option v-for="item in FieldUser" :key="item.id" :label="item.fieldCn" :value="item.fieldEn">
							</el-option>
						</el-select>
						<p style="margin: 10px;">
							=
						</p>
						<el-select filterable value="决策结果" disabled style="width: 255px;">
						</el-select>
					</div>
				</outcontent>
			
			</div>



		</div>

		<span slot="footer" class="dialog-footer">
			<el-button type="primary" @click="$emit('update:strategyDialogVisible',false)">确 定</el-button>
		</span>
	</el-dialog>











</template>

<script>
	import SCOrecursion from '@/components/common/SCO/recursion.vue'
	import recursion from '@/components/common/decision/recursion.vue'
	import topRecursion from '@/components/common/decision/topRecursion.vue'
	import textInput from '@/components/common/textInput.vue'
	import tree from '@/components/common/decisionTree/tree.vue'
	import type2 from '@/components/models/RuleCont.vue'
	import outcontent from '@/components/models/outcontent.vue'
	export default {
		components: {
			SCOrecursion,
			type2,
			outcontent,
			tree,
			recursion,
			topRecursion,
			textInput
		},

		props: {
			strategyDialogVisible: {
				type: Boolean,
				default: false
			},
			dataCont: {
				type: Object,
				default: null
			}
		},
		data() {
			return {
				data: {},
				width: '0px',
				max: 0,
			}
		},
		created() {
			if (!this.dataCont || !this.dataCont.strategyType) return
			this.data = JSON.parse(JSON.stringify(this.dataCont))
			this.data.snapshot = JSON.parse(this.data.snapshot)
			if (this.dataCont.strategyType == 2) {
				this.Type2redeepverify(this.data.snapshot.ruleConditionVo)
			}
			if (this.dataCont.strategyType == 17) {
				this.Type1617Enformat(this.data.snapshot.detailList, 'Array')
			}
			if (this.dataCont.strategyType == 16) {
				this.Type1617Enformat(this.data.snapshot.leftDetailVo,'Array')
				this.Type1617Enformat(this.data.snapshot.topDetailVo,'Array')
			}
			console.log(this.data)





		},
		mounted() {
			this.$nextTick(() => {
				if (this.$refs.left) {
					this.width = this.$refs.left.$el.clientWidth + 'px'
				}
			})
		},
		computed: {
			FieldUser() {
				if (this.$store.state.FieldUser) {
					console.log(this.$store.state.FieldUser.data.fieldList)
					return this.$store.state.FieldUser.data.fieldList
				} else {
					return []
				}
			},
			tier() {
			
				this.max = 0
				
				this.each(this.data.snapshot.scorecardDimension, 1)
				return this.max - 1
			}
		},
		methods: {
			Type2redeepverify(obj) {
				if (obj.fieldEn) {
					if ((obj.fieldEn.indexOf('.') != -1 && obj.fieldEn[0] == '%') || obj.conditionType == 3 || obj
						.conditionType === 2) {
						obj.fieldEn = obj.fieldEn.split('.')
					}
				}
				if (obj.children.length > 0) {
					obj.children.forEach(value => {
						this.Type2redeepverify(value)
					})
				}
				if (obj.loopGroupActions.length > 0) {
					obj.loopGroupActions.forEach(value => {
						if (value.actionValue.indexOf('.') != -1) {
							value.actionValue = value.actionValue.split('.')
						}
					})

				}
			},
			Type1617Enformat(arr, str) {
				arr.forEach(value => {
					if (str == 'String') {
						if (Array.isArray(value.fieldEn)) { //兼容老数据
							value.fieldEn = value.fieldEn.join('.')
						}
					} else if (str == 'Array') {
						value.fieldEn = value.fieldEn.split('.')
					}

					if (value.children.length > 0) {
						this.Type1617Enformat(value.children, str)
					}
				})
			},
			each(data, floor) {
				data.forEach(e => {
					e.floor = floor
					if (floor > this.max) {
						this.max = floor
					}
					if (e.children.length > 0) {
						this.each(e.children, floor + 1)
					}
				})
			},
			
			
		}
	}
</script>

<style >
	.watch_SCO p{
		margin-bottom: unset;
	}
	.rule_outcontent_box {
		border: 1px dotted #00000022;

		margin-top: 20px;
		padding: 10px 0 10px 10px;
	}
	.dec_top{
		
		display: flex;
	}
	.dec_table_header{
		width: 350px;
		border-left: 1px solid #ddd;
		border-bottom: 1px solid #ddd;
	}
	.dec_score{
		width: 199px;
		height: 80px;
		text-align: center;
		line-height: 80px;
		border-left: 1px solid #ddd;
		border-bottom: 1px solid #ddd;
		box-sizing: border-box;
	}
	.SCO_tableHeader{
		display: flex;
		margin-left: 20px;
		/* margin-top: 20px; */
		border-top: 1px solid #ddd;
	}
	.SCO_tableHeader>div{
		height: 40px;
		width: 300px;
		flex-shrink: 0;
		display: flex;
		justify-content: center;
		align-items: center;
		border-left: 1px solid #ddd;
		font-size: 14px;
	}
</style>

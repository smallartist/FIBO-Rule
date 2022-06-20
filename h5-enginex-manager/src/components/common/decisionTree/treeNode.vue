<template>
	<table v-if="treeData && treeData.variableType" style="margin: 0 auto;">
		<tr>
			<td :colspan="treeData.children ? treeData.children.length * 2 : 1" :class="{parentLevel: treeData.children, extend: (treeData.children && treeData.children.length)||treeData.resultValue!=''}">
				<div :class="{node: true, hasMate: treeData.mate}">
					<div class="person" @click="$emit('click-node', treeData)">
						<el-popover placement="top" style="display: flex;" trigger="hover">
							<div style="margin: 0;text-align: center;">
								<el-button size="mini" type="primary" @click="addStock(0)" v-if="treeData.resultValue==''">添加</el-button>
								<el-button size="mini" type="primary" @click="addStock(2)" v-if="treeData.children.length==0&&treeData.resultValue==''">添加结果</el-button>
								<el-button size="mini" type="primary" @click="addStock(3)" v-if="treeData.resultValue!=''">删除结果</el-button>
								<el-button size="mini" type="primary" @click="addStock(4)" v-if="treeData.resultValue!=''">更改结果</el-button>
								<el-button type="primary" size="mini" @click="addStock(1)">编辑</el-button>
								<el-button type="primary" size="mini" @click="deleteStock">删除</el-button>
							</div>
							<div class="avat" slot="reference">
								<p style="font-weight: bold;">
									{{treeData.fieldId===''?'初始节点':mixinGetvalueCn(treeData.fieldId)}}
								</p>
								<div style="display: flex;justify-content: center;">
									<p style="padding-right: 5px;border-right: 2px dotted #999;margin-right: 5px;min-width: 20px;">{{treeData.logical}}</p>
									<div>
										<p v-for="item in treeData.conditionList">
											{{item.operator}}
											{{item.variableType==3?JSON.parse(item.fieldValue).formula:item.fieldValue}}
											
										</p>
									</div>
								</div>
							</div>
						</el-popover>
					</div>
				</div>
				<!-- <div class="extend_handle" v-if=" (treeData.children && treeData.children.length)||treeData.resultValue!=''" @click="toggleExtend(treeData)"></div> -->
			</td>
		</tr>
		<!-- 这是一个递归组件,注意,这里还要调用,需要传递的数据这里也要传递,否则操作时拿不到子级的数据 -->
		<tr v-if="treeData.children && treeData.children.length">
			<td v-for="(children, index) in treeData.children" :key="index" colspan="2" class="childLevel">
				<TreeChart :json="children" @add="$emit('add', $event)" @delete="$emit('delete', $event)" @click-node="$emit('click-node', $event)" />
			</td>
		</tr>
		<tr v-if="treeData.resultValue!=''">
			<td colspan="2" class="childLevel">
				<div class="resultValue" >
					{{treeData.variableType==3?JSON.parse(treeData.resultValue).formula:treeData.variableType==2?mixinGetCnByEn(treeData.resultValue):treeData.resultValue}}
				</div>
			</td>
		</tr>
	</table>
</template>

<script>
	export default {
		name: "TreeChart",
		props: {
			json: {}, // 渲染数据
		},
		data() {
			return {
				treeData: {},
			};
		},
		created() {
			// console.log(this.treeData)
		},
		watch: {
			json: {
				// 遍历当前的数据
				handler: function(Props) {
					let extendKey = function(jsonData) {
						jsonData.extend =
							jsonData.extend === void 0 ? true : !!jsonData.extend;

						return jsonData;
					};
					if (Props) {
						this.treeData = extendKey(Props);
					}
				},
				immediate: true,
				deep: true
			}
		},
		methods: {
			toggleExtend(treeData) {
				treeData.extend = !treeData.extend;
				this.$forceUpdate();
			},

			
			addStock(val) {
				// console.log(this.treeData)
				this.$emit('add', {
					val: val,
					data: this.treeData
				})
			},

			
			deleteStock() {
				this.$emit('delete', this.treeData)
			}
		}
	};
</script>

<style lang="less">
	.resultValue{
		max-width: 180px;
		margin: 0 auto;
		word-break: break-word;
		margin-top: 5px;
		font-size: 14px;
		background-color: #e29700;
		padding: 10px 5px 5px 5px;
		border-radius: 4px;
		white-space: initial;
	}
	table {
		border-collapse: separate !important;
		border-spacing: 0 !important;
	}

	td {
		position: relative;
		vertical-align: top;
		padding: 0 0 40px 0;
		text-align: center;
	}

	.parent {
		background: #199ed8 !important;
		font-weight: bold;
	}

	.extend_handle {
		position: absolute;
		left: 50%;
		bottom: 27px;
		width: 10px;
		height: 10px;
		padding: 10px;
		transform: translate3d(-15px, 0, 0);
		cursor: pointer;
	}

	.extend_handle:before {
		content: "";
		display: block;
		width: 100%;
		height: 100%;
		box-sizing: border-box;
		border: 2px solid;
		border-color: #ccc #ccc transparent transparent;
		transform: rotateZ(135deg);
		transform-origin: 50% 50% 0;
		transition: transform ease 300ms;
	}

	.extend_handle:hover:before {
		border-color: #333 #333 transparent transparent;
	}

	.extend .extend_handle:before {
		transform: rotateZ(-45deg);
	}

	.extend::after {
		content: "";
		position: absolute;
		left: 50%;
		bottom: 15px;
		height: 15px;
		border-left: 2px solid #ccc;
		transform: translate3d(-1px, 0, 0)
	}

	.childLevel::before {
		content: "";
		position: absolute;
		left: 50%;
		bottom: 100%;
		height: 15px;
		border-left: 2px solid #ccc;
		transform: translate3d(-1px, 0, 0)
	}

	.childLevel::after {
		content: "";
		position: absolute;
		left: 0;
		right: 0;
		top: -15px;
		border-top: 2px solid #ccc;
	}

	.childLevel:first-child:before,
	.childLevel:last-child:before {
		display: none;
	}

	.childLevel:first-child:after {
		left: 50%;
		height: 15px;
		border: 2px solid;
		border-color: #ccc transparent transparent #ccc;
		border-radius: 6px 0 0 0;
		transform: translate3d(1px, 0, 0)
	}

	.childLevel:last-child:after {
		right: 50%;
		height: 15px;
		border: 2px solid;
		border-color: #ccc #ccc transparent transparent;
		border-radius: 0 6px 0 0;
		transform: translate3d(-1px, 0, 0)
	}

	.childLevel:first-child.childLevel:last-child::after {
		left: auto;
		border-radius: 0;
		border-color: transparent #ccc transparent transparent;
		transform: translate3d(1px, 0, 0)
	}

	.node {
		position: relative;
		display: inline-block;
		box-sizing: border-box;
		text-align: center;
		padding: 0 5px;
	}

	.node .person {
		padding-top: 15px;
		position: relative;
		display: inline-block;
		z-index: 2;
		width: 180px;
		overflow: hidden;
	}

	.node .person .avat {
		padding: 5px;
		padding-top: 10px;
		display: block;
		width: 100%;
		height: 100%;
		margin: auto;
		word-break: break-all;
		background: #fff;
		box-sizing: border-box;
		border-radius: 4px;
		font-size: 12px;

		.opreate_icon {
			display: none;
		}

		&:hover {
			.opreate_icon {
				display: block;
				position: absolute;
				top: -3px;
				right: -3px;
				padding: 5px;
			}
		}

		&.company {
			background: #199ed8;
		}

		&.other {
			background: #ccc;
		}
	}

	.node .person .avat img {
		cursor: pointer;
	}

	.node .person .name {
		height: 2em;
		line-height: 2em;
		overflow: hidden;
		width: 100%;
	}

	.node.hasMate::after {
		content: "";
		position: absolute;
		left: 2em;
		right: 2em;
		top: 15px;
		border-top: 2px solid #ccc;
		z-index: 1;
	}

	.node.hasMate .person:last-child {
		margin-left: 1em;
	}

	.el-dialog__header {
		padding: 0;
		padding-top: 30px;
		margin: 0 30px;
		border-bottom: 1px solid #F1F1F1;
		text-align: left;

		.el-dialog__title {
			font-size: 14px;
			font-weight: bold;
			color: #464C5B;
			line-height: 20px;
		}
	}

	.tips {
		padding: 0 20px;

		.el-select {
			width: 100%;
		}

		.blue {
			color: #00B5EF;
		}

		.check {
			margin-left: 100px;
		}

		.inquiry {
			font-weight: bold;
		}

		.el-form-item__label {
			display: block;
			float: none;
			text-align: left;
		}

		.el-form-item__content {
			margin-left: 0;
		}
	}

	.el-dialog__body {
		padding: 30px 25px;

		p {
			margin-bottom: 15px;
		}
	}

	.el-dialog__headerbtn {
		top: 30px;
		right: 30px;
	}

	// 竖向
	.landscape {
		transform: translate(-100%, 0) rotate(-90deg);
		transform-origin: 100% 0;

		.node {
			text-align: left;
			height: 8em;
			width: 8em;
		}

		.person {
			position: relative;
			transform: rotate(90deg);
			// padding-left: 4.5em;
			// height: 4em;
			top: 35px;
			left: 12px;
			width: 110px;
		}
	}

	.el-popover {
		.el-button {
			padding: 8px !important;
			margin-left: 5px !important;
			float: left;
		}
	}
</style>

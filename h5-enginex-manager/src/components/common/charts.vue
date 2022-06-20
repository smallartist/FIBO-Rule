<template>
	<div :id="sid" :style="{height: height,width:width}">
		
	</div>
</template>

<script>
	import * as echarts from 'echarts';
	export default {
		props: {
			sid: {
				type: String,
				default: 'echartsId'
			},
			height: {
				type: String,
				default: '300px'
			},
			width: {
				type: String,
				default: '300px'
			},
			option: {
				type: Object,
				default () {
					return {}
				}
			}
		},
		data() {
			return {
				chartDom: null,
				myChart: null
			}
		},
		mounted() {
			this.chartDom = document.getElementById(this.sid);
			this.myChart = echarts.init(this.chartDom);
			this.myChart.setOption(this.option);
		},
		watch: {
			option: {
				handler: function() {
					this.myChart.clear()
					this.myChart.setOption(this.option);
				},
				// 开启深度监听：只要obj中的任何一个属性发生改变，都会触发相应的代码
				deep: true
			}
		}
	}
</script>

<style>
</style>

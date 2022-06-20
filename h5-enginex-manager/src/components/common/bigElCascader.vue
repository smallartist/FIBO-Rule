<template>
	<el-cascader :value="value" @input="$emit('input',$evnet)" v-el-select-loadmore="loadMore(num)" filterable
		:size="size" :options="MyOptions" clearable @change="$emit('change',$event)" :key="Mykey" :props="props">
	</el-cascader>
</template>

<script>
	export default {
		props: {
			value: {
				type: Array,
				default () {
					return []
				}
			},
			options: {
				type: Array,
				default () {
					return []
				}
			},
			Mykey: {
				type: Number,
				default: 1
			},
			props: {
				type: Object,
				default () {
					return {
						expandTrigger: 'hover'
					}
				}
			},
			size: {
				type: String,
				default: 'mini'
			}
		},
		data() {
			return {
				num: 10,
				MyOptions: [],
				page: 1
			}
		},
		created() {
			this.loadMore()
		},
		methods: {
			loadMore(n) {
				let start = (this.page - 1) * 10
				this.MyOptions.push(...this.options.splice(start, start + 10))
				console.log(this.MyOptions)
				this.page++
				
			},

		},
		directives: {

			'el-select-loadmore': (el, binding) => {
				// 获取element-ui定义好的scroll盒子
				const SELECTWRAP_DOM = el.querySelector(".el-cascader-menu .el-scrollbar__wrap");
				console.log(SELECTWRAP_DOM)
				if (SELECTWRAP_DOM) {
					SELECTWRAP_DOM.addEventListener("scroll", function() {
						/**
						 * scrollHeight 获取元素内容高度(只读)
						 * scrollTop 获取或者设置元素的偏移值,
						 *  常用于:计算滚动条的位置, 当一个元素的容器没有产生垂直方向的滚动条, 那它的scrollTop的值默认为0.
						 * clientHeight 读取元素的可见高度(只读)
						 * 如果元素滚动到底, 下面等式返回true, 没有则返回false:
						 * ele.scrollHeight - ele.scrollTop === ele.clientHeight;
						 */
						const condition = this.scrollHeight - this.scrollTop <= this.clientHeight;
						if (condition) this.loadMore();
					
					});
				}
			}
		}
	}
</script>

<style>
</style>

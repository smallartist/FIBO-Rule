<style>
	.zoomView {
		background-color: #eee;
		height: 250px;
		/* overflow: hidden; */
		position: relative;
	}

	#zoomView_cont {
		background-color: #DDDDDD;
		height: 100%;
		width: 100%;
		transition: all 0.1s;
		position: absolute;
		user-select: none;
		overflow: hidden;
	}
	.zoomView_modal{
		/* background-color: #0000FF; */
		position: absolute;
		width: 100%;
		height: 100%;
		z-index: 9;
	}
</style>

<template>
	<div class="zoomView" @mouseup="mouseup" ref="zoomView">
		<span style="font-size: 12px;color: #aaa;">双击回归原位置</span>
		<div id="zoomView_cont" ref="zoomViewCont" :style="{transform: 'scale('+mult+','+mult+')',top:top,left:left}">
			<div class="zoomView_modal">
				
			</div>
			<slot></slot>
		</div>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				mult: 1,
				ismove: false,
				timeout: null,
				top: '0px',
				left: '0px',
				ClientX: '',
				ClientY: '',
				OffsetLeft: '',
				OffsetTop: '',
			}
		},
		beforeDestroy() {
			window.onmousemove = () => {}
			window.mouseup = () => {}
		},
		mounted() {
			this.$refs.zoomView.onmousewheel = (e) => {
				this.mult -= e.deltaY / 1000
				if (this.mult >= 1) {

				} else {
					this.mult = 1
				}
			}
			this.$refs.zoomView.onmousedown = (e) => {
				this.ismove = true
				this.ClientX = e.clientX;
				this.ClientY = e.clientY;
				this.OffsetLeft = this.$refs.zoomViewCont.offsetLeft
				this.OffsetTop = this.$refs.zoomViewCont.offsetTop
			}
			window.onmousemove = (e) => {
				if (this.ismove && (new Date() - this.timeout > 50 || this.timeout == null)) {


					this.top = e.clientY - (this.ClientY - this.OffsetTop) + 'px'
					this.left = e.clientX - (this.ClientX - this.OffsetLeft) + 'px'



					this.timeout = new Date()
				}

			}
			window.mouseup = () => {
				this.ismove = false
			}
			this.$refs.zoomViewCont.ondblclick = () => {
				this.top = '0px'
				this.left = '0px'
				this.mult = 1
			}
			this.$refs.zoomView.ondblclick = () => {
				this.top = '0px'
				this.left = '0px'
				this.mult = 1
			}

		},
		methods: {
			mouseup() {
				this.ismove = false
			}
		}
	}
</script>

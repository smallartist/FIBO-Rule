<template>
	<div>
		<codemirror v-model="code"></codemirror>
	</div>
</template>
<script type="text/javascript">
	import codemirror from '@/components/common/codemirror.vue'
	export default {
		components: {
			codemirror
		},
		data() {
			return {
				code: '12344356s'
			}
		},
		watch:{
			code(val){
				// console.log(val)
			}
		}


	}
</script>

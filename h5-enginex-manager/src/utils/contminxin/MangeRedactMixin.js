import bus from '@/components/common/bus.js';
export default {
	created(){
		bus.$emit('collapseHeader',true)
	},
	data(){
		return {
			smallHeader:false
		}
	},
	methods:{
		openHeader(){
			this.smallHeader = !this.smallHeader 
		},
		mixinClose(){
			this.$emit('close')
			bus.$emit('collapseHeader',false)
		}
	}
}
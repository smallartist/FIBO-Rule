<template>
	<!-- <div> -->
		<el-form class="formItenBox">
			<div v-for="value in rule" :key="value.name">
				<!-- {{value.name}} -->
				<div class="formItem" >
					<p class="formItemTitle">{{ value.label }}</p>
					<el-input v-if="value.type == String" v-model="data[value.name]" size="mini" class="formInput">
					</el-input>
					<el-input v-if="value.type == Number" type="number" v-model="data[value.name]" size="mini"
						class="formInput"></el-input>
				
					<el-switch v-if="value.type == Boolean" v-model="data[value.name]" active-color="#13ce66"
						inactive-color="#ff4949" size="mini"></el-switch>
					<el-date-picker v-if="value.type == Date" v-model="data[value.name]" type="datetime"
						placeholder="选择日期时间" size="mini" class="formInput">
					</el-date-picker>
					<el-select v-if="value.type == 'Enum'" v-model="data[value.name]" size="mini" 
						class="formInput"> 
						<el-option v-for="item in value.Enum" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
					
				</div>
			</div>
		</el-form>
	<!-- </div> -->
</template>

<script>
	export default {
		props: {
			rule: {
				type: Array,
				default () {
					return []
				}
			},
			data: {
				type: Object,
				default () {
					return {}
				}
			}

		},
		created() {
			
			this.rule.forEach(value=>{
				if(value.defaultValue){
					if(!this.data[value.name]){
						this.data[value.name] = value.defaultValue
					}
				}
				
			})
		}
	}
</script>

<style>
	.formItenBox {
	  align-content: space-around;
	  height: 100%;
	  display: flex;
	  align-content: flex-start;
	  flex-flow: row wrap;
	  flex-direction: column;
	}
	.formInput {
	  width: 300px !important;
	}
	.formItem {
	  width: 420px;
	  display: flex;
	  
	  margin-top: 5px;
	  align-items: center;
	}
	.formItemTitle {
	  width: 40%;
	  justify-content: flex-end;
	  padding-right: 5px;
	  display: flex;
	  align-items: center;
	}
</style>

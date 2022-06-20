import ElementUI from 'element-ui';
export function endRuleVerification(obj){
	let is= {
		is:false,
		msg:''
	}
	
	if(!obj.selectedRule.length){
		return  false
	}
	if(obj.output.fieldId===''){
		is.is = true
		is.msg = '请选择终止结果变量'
	}
	if(obj.output.fieldValue===''){
		is.is = true
		is.msg = '请填写终止结果的输出值'
	}
	obj.conditions.forEach((value,index)=>{
		if(value.fieldCode===''){
			is.is = true
			is.msg ='请查看是否有终止选项未选'
		}
		if(value.operator===''){
			is.is = true
			is.msg ='请查看是否有运算符未选'
		}
		if(value.value===''){
			is.is = true
			is.msg ='请查看是否有终止值未填写'
		}
		if(value.relativeOperator===''&&index!=obj.conditions.length-1){
			is.is = true
			is.msg ='请查看是否连接符未选'
		}
		
		
		
		
		
	})
	if(is.is){
		ElementUI.Message.error(is.msg)
	}
	return is.is
	
	
	
	
	
	
	
	
	
}
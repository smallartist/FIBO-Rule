package com.fibo.ddp.common.utils.util.runner.jeval.function.math;

import com.fibo.ddp.common.utils.util.runner.jeval.EvaluationConstants;
import com.fibo.ddp.common.utils.util.runner.jeval.Evaluator;
import com.fibo.ddp.common.utils.util.runner.jeval.function.*;

import java.math.BigDecimal;
import java.util.ArrayList;


/**
 * 计算多个数字的平均值
 */
public class Average implements Function {

	@Override
	public String getName() {
		return "avg";
	}

	@Override
	public FunctionResult execute(Evaluator evaluator, String arguments)
			throws FunctionException {
		Double result = null;

		ArrayList<Double> numbers = FunctionHelper.getDoubles(arguments,
				EvaluationConstants.FUNCTION_ARGUMENT_SEPARATOR);

		int count = numbers.size() ;
		if (count < 2) {
			throw new FunctionException("Two numeric arguments are required at least.");
		}
		
		double sum=0;  
		for (Double num : numbers) {
			 //为什么使用这样的方法,而不直接相加呢？
			 //原因是Java中的简单浮点数类型float和double不能够进行运算，会出现类似如下情况
			 //eg:sum(1,2,3,1.2,2.0,3.6)=12.79999999999而不等于12.8
			 BigDecimal b1=new BigDecimal(Double.toString(sum));  
             BigDecimal b2=new BigDecimal(Double.toString(num));
             sum=b1.add(b2).doubleValue();
		}
        
		result = new Double(sum/count);
		return new FunctionResult(result.toString(), 
				FunctionConstants.FUNCTION_RESULT_TYPE_NUMERIC);
	}

}

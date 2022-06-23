package com.fibo.ddp.common.utils.util.runner;

import com.fibo.ddp.common.utils.constant.CommonConst;
import com.fibo.ddp.common.utils.constant.enginex.EngineOperator;
import com.fibo.ddp.common.utils.util.CollectionUtil;
import com.fibo.ddp.common.utils.util.runner.jeval.EvaluationException;
import com.fibo.ddp.common.utils.util.runner.jeval.Evaluator;

import java.util.Map;

/**
 * 表达式解析器入口
 */
public class JevalUtil {

	/**
	 * 获取执行布尔值结果
	 * @param expression
	 * @param params
	 * @return
	 * @throws EvaluationException
	 */
	public static Boolean evaluateBoolean(String expression,Map<String,Object> params) throws EvaluationException {
		Evaluator evaluator = getEvaluator(params);
		return evaluator.getBooleanResult(expression);
	}

	/**
	 * 获取执行数字结果
	 * @param expression
	 * @param params
	 * @return
	 * @throws EvaluationException
	 */
	public static Double evaluateNumric(String expression,Map<String,Object> params) throws EvaluationException{
		Evaluator evaluator = getEvaluator(params);
		return evaluator.getNumberResult(expression);
	}

	/**
	 * 获取执行String结果
	 * @param expression
	 * @param params
	 * @return
	 * @throws EvaluationException
	 */
	public static String evaluateString(String expression,Map<String,Object> params) throws EvaluationException{
		Evaluator evaluator = getEvaluator(params);
		return evaluator.evaluate(expression,false,true);
	}

	/**
	 * 获取绑定参数的Evaluator
	 * @param params
	 * @return
	 * @throws EvaluationException
	 */
	private static Evaluator getEvaluator(Map<String,Object> params){
		Evaluator evaluator = new Evaluator();
		if(params != null && !params.isEmpty()){
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				if(null!=entry.getValue()){
					evaluator.putVariable(entry.getKey(), entry.getValue().toString());
				}
			}
		}
		return evaluator;
	}

	/**
	 * 根据区间表达式解析区间
	 * @param expression,eg:[3,5],inputParam:字段code
	 * @return
	 */
	public static String getNumericInterval(String expression,String param){
		StringBuffer result = new StringBuffer();
		//先把变量进行加工#{inputParam}
		param = EngineOperator.OPERATOR_VARIABLE_LEFT+param+EngineOperator.OPERATOR_VARIABLE_RIGHT;
		//如果是纯数字，代表==，直接拼接
		if(!expression.startsWith(EngineOperator.OPERATOR_LEFT_PARENTHESES) && !expression.startsWith(EngineOperator.OPERATOR_LEFT_BRACKET)
				&& !expression.endsWith(EngineOperator.OPERATOR_RIGHT_PARENTHESES) && !expression.endsWith(EngineOperator.OPERATOR_RIGHT_BRACKET)){
			result.append(param).append(CommonConst.SYMBOL_BLANK).append(EngineOperator.OPERATOR_EQUALS_RELATION).append(CommonConst.SYMBOL_BLANK).append(expression);
			return result.toString();
		}
		//获取到取值区间
		String exp = expression.substring(1, expression.length()-1);
		String[] segments = null;
		if(exp.startsWith(CommonConst.SYMBOL_COMMA)){
			segments = new String[1];
			segments[0] = exp.substring(1);
		}else{
			segments = exp.split(CommonConst.SYMBOL_COMMA);
		}
		//判断取值范围(,3)(4,)
		if(segments.length == 1){
			//说明是(,3),(4,)
			if(expression.substring(1, expression.length()-1).startsWith(CommonConst.SYMBOL_COMMA)){
				//以逗号开始(,3)
				if(expression.endsWith(EngineOperator.OPERATOR_RIGHT_PARENTHESES)){
					//小括号
					result.append(param).append(CommonConst.SYMBOL_BLANK).append(EngineOperator.OPERATOR_LESS_RELATION).append(CommonConst.SYMBOL_BLANK).append(segments[0]);
				}else if(expression.endsWith(EngineOperator.OPERATOR_RIGHT_BRACKET)){
					//大括号
					result.append(param).append(CommonConst.SYMBOL_BLANK).append(EngineOperator.OPERATOR_LESS_EQUALS_RELATION).append(CommonConst.SYMBOL_BLANK).append(segments[0]);
				}
			}else{
				//以逗号结尾(4,)
				if(expression.startsWith(EngineOperator.OPERATOR_LEFT_PARENTHESES)){
					//小括号
					result.append(param).append(CommonConst.SYMBOL_BLANK).append(EngineOperator.OPERATOR_GREATER_RELATION).append(CommonConst.SYMBOL_BLANK).append(segments[0]);
				}else if(expression.startsWith(EngineOperator.OPERATOR_LEFT_BRACKET)){
					//大括号
					result.append(param).append(CommonConst.SYMBOL_BLANK).append(EngineOperator.OPERATOR_GREATER_EQUALS_RELATION).append(CommonConst.SYMBOL_BLANK).append(segments[0]);
				}
			}
		}else if(segments.length == 2){
			//开始符号
			if(expression.startsWith(EngineOperator.OPERATOR_LEFT_PARENTHESES)){
				//小括号
				result.append(param).append(CommonConst.SYMBOL_BLANK).append(EngineOperator.OPERATOR_GREATER_RELATION).append(CommonConst.SYMBOL_BLANK).append(segments[0]);
			}else if(expression.startsWith(EngineOperator.OPERATOR_LEFT_BRACKET)){
				//大括号
				result.append(param).append(CommonConst.SYMBOL_BLANK).append(EngineOperator.OPERATOR_GREATER_EQUALS_RELATION).append(CommonConst.SYMBOL_BLANK).append(segments[0]);
			}
			//都是&&关系
			result.append(CommonConst.SYMBOL_BLANK).append(EngineOperator.OPERATOR_AND_RELATION).append(CommonConst.SYMBOL_BLANK);
			//结束符号
			if(expression.endsWith(EngineOperator.OPERATOR_RIGHT_PARENTHESES)){
				//小括号
				result.append(param).append(CommonConst.SYMBOL_BLANK).append(EngineOperator.OPERATOR_LESS_RELATION).append(CommonConst.SYMBOL_BLANK).append(segments[1]);
			}else if(expression.endsWith(EngineOperator.OPERATOR_RIGHT_BRACKET)){
				//大括号
				result.append(param).append(CommonConst.SYMBOL_BLANK).append(EngineOperator.OPERATOR_LESS_EQUALS_RELATION).append(CommonConst.SYMBOL_BLANK).append(segments[1]);
			}			
		}
		return result.toString();
	}
	
	/**
	 * 变量值转义
	 * @param fieldsMap
	 * @param variablesMap
	 * @return
	 */
	public static Map<String,Object> convertVariables(Map<String,Integer> fieldsMap,Map<String,Object> variablesMap){
		if(CollectionUtil.isNotNullOrEmpty(variablesMap)){
			if(!CollectionUtil.isNotNullOrEmpty(fieldsMap)){
				return variablesMap;
			}
			String key = "";
			Integer value = null;
			for (Map.Entry<String, Object> entry : variablesMap.entrySet()) {
				key = entry.getKey();
				value = fieldsMap.get(key);
				if(value == null){
					continue;
				}
				//2代表字符串
				if(value == 2){
					String variableValue = CommonConst.SYMBOL_SINGLE_QUOTA+variablesMap.get(key).toString()+CommonConst.SYMBOL_SINGLE_QUOTA;
					variablesMap.put(key, variableValue);
				}			  
			}
		}
		return variablesMap;
	}
}

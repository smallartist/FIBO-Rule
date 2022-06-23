package com.fibo.ddp.common.service.enginex.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fibo.ddp.common.model.enginex.risk.EngineNode;
import com.fibo.ddp.common.utils.constant.CommonConst;
import com.fibo.ddp.common.utils.constant.enginex.EngineOperator;
import com.fibo.ddp.common.utils.util.runner.JevalUtil;
import com.fibo.ddp.common.utils.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

import static com.fibo.ddp.common.service.enginex.util.EngineNodeUtilV2.convertClassifyV2;
import static com.fibo.ddp.common.service.enginex.util.EngineNodeUtilV2.convertDecisionV2;

public class EngineNodeUtil {

	/**
	 * 组装规则节点参数信息
	 * @param param
	 * @return
	 */
	public static EngineNode boxEngineNode(Map<String,Object> param){
		//组装节点基本信息
		EngineNode engineNode = boxEngineNode_Common(param);
		return engineNode;
	}

	/**
	 * 组装一个引擎节点的基本参数
	 * @param param
	 * @return
	 */
	private static EngineNode boxEngineNode_Common(Map<String,Object> param){
		//节点对应版本
		Long versionId = Long.parseLong((String)param.get("initEngineVersionId"));
		//父节点
		String parentId = (String)param.get("parentId");
		//节点名称
		String nodeName = (String) param.get("nodeName");
		//节点nodeCode
		String nodeCode = (String) param.get("nodeCode");
		//节点顺序
		int nodeOrder = Integer.parseInt(param.get("nodeOrder").toString()) ;
		//节点类型
		int nodeType = Integer.parseInt(param.get("nodeType").toString()) ;
		//节点横坐标
		double nodeX = Double.parseDouble(param.get("nodeX").toString());
		//节点纵坐标
		double nodeY =  Double.parseDouble(param.get("nodeY").toString());
		//下个节点nodeCode
		String nextNodes = (String)param.get("nextNodes");
		//nodeJson
		String nodeJson = (String)param.get("nodeJson");
		String snapshot = (String)param.get("snapshot");
		EngineNode engineNode = new EngineNode();
		engineNode.setVersionId(versionId);
		engineNode.setNodeName(nodeName);
		engineNode.setNodeCode(nodeCode);
		engineNode.setNodeOrder(nodeOrder);
		engineNode.setNodeType(nodeType);
		engineNode.setNodeJson(nodeJson);
		engineNode.setNodeX(nodeX);
		engineNode.setNodeY(nodeY);
		engineNode.setNextNodes(nextNodes);
		engineNode.setParentId(parentId);
		//snapshot节点快照
		engineNode.setSnapshot(snapshot);
		//组装特殊节点的nodeScript
		convertNodeScript(engineNode);

		return engineNode;
	}
	
	/**
	 * 组装一个引擎节点的基本参数
	 * @param engineNode
	 * @return
	 */
	public static EngineNode boxEngineNodeJson(EngineNode engineNode){		
		//组装特殊节点的nodeScript
		convertNodeScript(engineNode);
		return engineNode;
	}

	public static void convertNodeScript(EngineNode node){
		int nodeType = node.getNodeType();
		switch (nodeType) {
		case 2:
			//规则集
			EngineNodeUtilV2.convertRule(node);
			break;
		case 3:
			//分群
			// convertClassify(node);
			convertClassifyV2(node);  // 调用 V2
			break;
		case 4://评分卡
		case 5://名单库
		case 15://模型
		case 16://决策表
		case 17://决策树
			EngineNodeUtilV2.convertRule(node);
			break;
		case 7:
			//沙盒节点
			convertSandBox(node);
			break;
		case 9:
			//决策选项
			// convertDecision(node);
			convertDecisionV2(node);  // 调用 V2
			break;
		default:
			break;
		}
	}
	
	/**
	 * 处理沙河比例
	 * @param node
	 */
	private static void convertSandBox(EngineNode node) {
		String nodeJson = node.getNodeJson();
		if(StringUtil.isValidStr(nodeJson)){
			JSONArray jsonArray = JSONArray.parseArray(nodeJson);
			if(jsonArray != null && !jsonArray.isEmpty()){
				int size = jsonArray.size();
				int sum = 0;
				for (int i = 0; i < size; i++) {
					sum+=jsonArray.getJSONObject(i).getIntValue("proportion");
				}
				//把这个值put进每个json
				for (int i = 0; i < size; i++) {
					jsonArray.getJSONObject(i).put("sum", sum);
				}
			}
			node.setNodeScript(jsonArray.toString());
		}
	}

	/**
	 * 处理分群
	 * @param node
	 */
	private static void convertClassify(EngineNode node){
		String nodeJson = node.getNodeJson();
		if(StringUtil.isValidStr(nodeJson)){
			JSONObject jsonObject = JSONObject.parseObject(nodeJson);
			if(jsonObject != null && !jsonObject.isEmpty()){
				JSONArray conditions = jsonObject.getJSONArray("conditions");
				if(null != conditions && !conditions.isEmpty()){
					JSONArray conditionArray = processClassifyConditions(conditions);
					JSONObject resultjson = new JSONObject();
					resultjson.put("fields", jsonObject.getJSONArray("fields"));
					resultjson.put("conditions", conditionArray);
					node.setNodeScript(resultjson.toString());
				}
			}
		}
	}

	/**
	 * 处理分群条件
	 * @param conditions
	 * @return
	 */
	private static JSONArray processClassifyConditions(JSONArray conditions){
		JSONArray resultArray = new JSONArray();
		int size = conditions.size();
		JSONObject condition = null;
		JSONObject resultJson = null;
		String nextNode = "";
		for (int i = 0; i < size; i++) {
			condition = conditions.getJSONObject(i);
			//获取nextNode,如果nextNode为空，则跳过
			nextNode = condition.getString("nextNode");
			if(null == nextNode || CommonConst.STRING_EMPTY.equals(nextNode)){
				continue;
			}else{
				//开始解析公式
				JSONArray formulas = condition.getJSONArray("formulas");
				if(formulas != null && !formulas.isEmpty()){
					resultJson = new JSONObject();
					resultJson.put("nextNode", nextNode);
					resultJson.put("formula", processClassifyFormulas(formulas));
					resultArray.add(resultJson);
				}else{
					resultJson = new JSONObject();
					resultJson.put("nextNode", nextNode);
					resultJson.put("formula", "");
					resultArray.add(resultJson);
				}
			}
		}			
		return resultArray;
	}
	
	/**
	 * 处理分群公式
	 * @param formulas
	 * @return
	 */
	private static String processClassifyFormulas(JSONArray formulas){
		int size = formulas.size();
		if(size == 1){
			return convertFormula(formulas.getJSONObject(0));
		}
		StringBuffer sb = new StringBuffer();
		JSONObject formula = null;
		for (int i = 0; i < size; i++) {
			formula = formulas.getJSONObject(i);
			if(i == 0){
				sb.append(EngineOperator.OPERATOR_LEFT_PARENTHESES);
				sb.append(convertFormula(formula));
				sb.append(EngineOperator.OPERATOR_RIGHT_PARENTHESES);				
			}else{
				sb.append(CommonConst.SYMBOL_BLANK);
				sb.append(formulas.getJSONObject(i-1).getString("sign"));
				sb.append(CommonConst.SYMBOL_BLANK);
				sb.append(EngineOperator.OPERATOR_LEFT_PARENTHESES);
				sb.append(convertFormula(formula));
				sb.append(EngineOperator.OPERATOR_RIGHT_PARENTHESES);
			}
		}
		return sb.toString();
	}
	
	/**
	 * 转化公式
	 * @param formula
	 * @return
	 */
	private static String convertFormula(JSONObject formula){
		//公式一
		String fieldCode1 = EngineOperator.OPERATOR_VARIABLE_LEFT+formula.getString("field_code1")+EngineOperator.OPERATOR_VARIABLE_RIGHT;
		String operator1 = formula.getString("operator1");
		Object value1 = formula.get("value1");
		//公式一和公式二之间的关系
		String relativeOperator = formula.getString("relative_operator");
		//公式二
		String fieldCode2 = EngineOperator.OPERATOR_VARIABLE_LEFT+formula.getString("field_code2")+EngineOperator.OPERATOR_VARIABLE_RIGHT;
		String operator2 = formula.getString("operator2");
		Object value2 = formula.get("value2");
		StringBuffer sb = new StringBuffer();
		if(StringUtil.isValidStr(relativeOperator)){
			sb.append(EngineOperator.OPERATOR_LEFT_PARENTHESES);
			sb.append(convertOperator(fieldCode1, operator1, value1));
			sb.append(EngineOperator.OPERATOR_RIGHT_PARENTHESES);
			sb.append(CommonConst.SYMBOL_BLANK);
			sb.append(relativeOperator);
			sb.append(CommonConst.SYMBOL_BLANK);
			sb.append(EngineOperator.OPERATOR_LEFT_PARENTHESES);
			sb.append(convertOperator(fieldCode2, operator2, value2));
			sb.append(EngineOperator.OPERATOR_RIGHT_PARENTHESES);
		}else{
			sb.append(EngineOperator.OPERATOR_LEFT_PARENTHESES);
			sb.append(convertOperator(fieldCode1, operator1, value1));
			sb.append(EngineOperator.OPERATOR_RIGHT_PARENTHESES);
		}
		return sb.toString();
	}
	
	/**
	 * 特殊处理字符串函数(等于不等于,包含不包含)
	 * @param variable
	 * @param operator
	 * @param value
	 * @return
	 */
	private static String convertOperator(String variable,String operator,Object value){
		StringBuffer sb = new StringBuffer();
		//凡是contians，notContains,equals,notEquals
		if(variable.equalsIgnoreCase(EngineOperator.OPERATOR_CONTAINS_STRING)
				|| variable.equalsIgnoreCase(EngineOperator.OPERATOR_UNCONTAINS_STRING)
				|| variable.equalsIgnoreCase(EngineOperator.OPERATOR_EQUALS_STRING)
				|| variable.equalsIgnoreCase(EngineOperator.OPERATOR_UNEQUALS_STRING)){
			sb.append(operator).append(EngineOperator.OPERATOR_LEFT_PARENTHESES);
			sb.append(variable).append(CommonConst.SYMBOL_COMMA).append(value).append(EngineOperator.OPERATOR_RIGHT_PARENTHESES);
		}else{
			sb.append(variable).append(operator).append(value);
		}
		return sb.toString();
	}
	
	/**
	 * 处理决策选项
	 * @param node
	 */
	private static void convertDecision(EngineNode node){
		String nodeJson = node.getNodeJson();
		if(StringUtil.isValidStr(nodeJson)){
			JSONObject jsonObject = JSONObject.parseObject(nodeJson);
			if(jsonObject.containsKey("inputs")){
				//获取变量个数
				int inputs = jsonObject.getInteger("inputs");
				switch (inputs) {
				case 1:
					//单个决策变量
					handleSingleDecisionVariable(node,jsonObject);
					break;
				case 2:
					//两个决策变量
					handleDoubleDecisionVariables(node,jsonObject);
					break;
				case 3:
					//三个或以上决策变量
					handleMoreThanTripleDecisionVariables(node,jsonObject);
					break;
				default:
					break;
				}	
			}
		}
	}
	
	/**
	 * 决策节点单个决策变量解析
	 * @param engineNode
	 * @param jsonObject
	 */
	private static void handleSingleDecisionVariable(EngineNode engineNode,JSONObject jsonObject){
		JSONObject resultJson = new JSONObject();
		JSONArray realConditions = new JSONArray();
		int conditionType = jsonObject.getIntValue("condition_type");
		if(conditionType == 2){
			engineNode.setNodeScript(engineNode.getNodeJson());
		}else if(conditionType == 1){			
			//获取条件
			JSONArray conditions = jsonObject.getJSONArray("conditions");
			if(null != conditions && !conditions.isEmpty()){
				int size = conditions.size();
				JSONObject condition = null;
				for (int i = 0; i < size; i++) {
					condition = conditions.getJSONObject(i);
					//获取formula
					JSONArray subArray = condition.getJSONArray("formula");
					if(null != subArray && !subArray.isEmpty()){
						JSONObject result = new JSONObject();
						result.put("result", condition.get("result"));
						result.put("resultKey", condition.get("resultKey"));
						int subSize = subArray.size();
						JSONObject subJson = null;
						StringBuffer expression = new StringBuffer();
						for (int j = 0; j < subSize; j++) {
							subJson = subArray.getJSONObject(j);
							if(j != 0){
								expression.append(CommonConst.SYMBOL_BLANK);
								if(EngineOperator.OPERATOR_AND_STRING_RELATION.equalsIgnoreCase(subArray.getJSONObject(j).getString("sign"))){
									expression.append(EngineOperator.OPERATOR_AND_RELATION);
								}else if(EngineOperator.OPERATOR_OR_STRING_RELATION.equalsIgnoreCase(subArray.getJSONObject(j).getString("sign"))){
									expression.append(EngineOperator.OPERATOR_OR_RELATION);
								}
								expression.append(CommonConst.SYMBOL_BLANK);
							}
							expression.append(EngineOperator.OPERATOR_LEFT_PARENTHESES);
							String operator = subJson.getString("operator");
							if(jsonObject.getJSONArray("input").getJSONObject(0).getIntValue("field_type") == 2){
								expression.append(operator);
								expression.append(EngineOperator.OPERATOR_LEFT_PARENTHESES);
								expression.append(EngineOperator.OPERATOR_VARIABLE_LEFT+subJson.getString("field_code")+EngineOperator.OPERATOR_VARIABLE_RIGHT);
								expression.append(CommonConst.SYMBOL_COMMA);
								expression.append("'"+subJson.get("result")+"'");
								expression.append(EngineOperator.OPERATOR_RIGHT_PARENTHESES);
							}else{
								expression.append(EngineOperator.OPERATOR_VARIABLE_LEFT+subJson.getString("field_code")+EngineOperator.OPERATOR_VARIABLE_RIGHT);
								expression.append(CommonConst.SYMBOL_BLANK);
								expression.append(operator);
								expression.append(CommonConst.SYMBOL_BLANK);
								expression.append(subJson.get("result"));
							}
							expression.append(EngineOperator.OPERATOR_RIGHT_PARENTHESES);
						}
						result.put("formula", expression.toString());
						realConditions.add(result);
					}
				}
				resultJson.put("inputs", jsonObject.getIntValue("inputs"));
				resultJson.put("condition_type", jsonObject.getIntValue("condition_type"));
				resultJson.put("input", jsonObject.getJSONArray("input"));
				resultJson.put("output", jsonObject.getJSONObject("output"));
				resultJson.put("conditions", realConditions);
			}
			engineNode.setNodeScript(resultJson.toString());
		}
	}
	
	/**
	 * 处理两个决策变量
	 * @param engineNode
	 * @param jsonObject
	 */
	private static void handleDoubleDecisionVariables(EngineNode engineNode,JSONObject jsonObject){
		JSONObject resultJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		//获取类型
		int conditionType = jsonObject.getIntValue("condition_type");
		//获取条件
		JSONArray conditions = jsonObject.getJSONArray("conditions");
		//判断输出字段类型
		JSONArray inputArray = jsonObject.getJSONArray("input");
		Map<String,Integer> inputFieldTypeMap = new HashMap<String, Integer>(); 
		if(inputArray != null && !inputArray.isEmpty()){
			for (int i = 0; i < inputArray.size(); i++) {
				inputFieldTypeMap.put(inputArray.getJSONObject(i).getString("field_code"), inputArray.getJSONObject(i).getIntValue("field_type"));
			}
		}
		if(null != conditions && !conditions.isEmpty()){
			if(conditionType == 1){
				int size = conditions.size();
				JSONObject condition = null;
				for (int i = 0; i < size; i++) {
					condition = conditions.getJSONObject(i);
					//获取result
					Object result = condition.get("result");
					Object resultKey = condition.get("resultKey");
					JSONObject formula = condition.getJSONObject("formula");
					StringBuffer expression = new StringBuffer();
					//eg：(#{a}>3 && #{b} <45)
					expression.append(EngineOperator.OPERATOR_LEFT_PARENTHESES);

					//字段一是数字
					if(inputFieldTypeMap.get(formula.getString("field_code1")) == 1){
						expression.append(JevalUtil.getNumericInterval(formula.getString("expression1"), formula.getString("field_code1")));
					}
					
					//字段1是枚举
					if(inputFieldTypeMap.get(formula.getString("field_code1")) == 3){
						expression.append(EngineOperator.OPERATOR_VARIABLE_LEFT);
						expression.append(formula.getString("field_code1"));
						expression.append(EngineOperator.OPERATOR_VARIABLE_RIGHT);
						expression.append(EngineOperator.OPERATOR_EQUALS_RELATION);
						expression.append(formula.getString("expression1"));
					}
					
					//字段1是字符串
					if(inputFieldTypeMap.get(formula.getString("field_code1")) == 2){
						expression.append(EngineOperator.OPERATOR_EQUALS_STRING);
						expression.append(EngineOperator.OPERATOR_LEFT_PARENTHESES);
						expression.append(EngineOperator.OPERATOR_VARIABLE_LEFT);
						expression.append(formula.getString("field_code1"));
						expression.append(EngineOperator.OPERATOR_VARIABLE_RIGHT);
						expression.append(CommonConst.SYMBOL_COMMA);
						expression.append("'"+formula.getString("expression1")+"'");
						expression.append(EngineOperator.OPERATOR_RIGHT_PARENTHESES);
					}
					
					expression.append(CommonConst.SYMBOL_BLANK);
					expression.append(EngineOperator.OPERATOR_AND_RELATION);
					expression.append(CommonConst.SYMBOL_BLANK);
					
					//字段2是数字
					if(inputFieldTypeMap.get(formula.getString("field_code2")) == 1){
						expression.append(JevalUtil.getNumericInterval(formula.getString("expression2"), formula.getString("field_code2")));
					}
					
					//字段3是枚举
					if(inputFieldTypeMap.get(formula.getString("field_code2")) == 3){
						expression.append(EngineOperator.OPERATOR_VARIABLE_LEFT);
						expression.append(formula.getString("field_code2"));
						expression.append(EngineOperator.OPERATOR_VARIABLE_RIGHT);
						expression.append(EngineOperator.OPERATOR_EQUALS_RELATION);
						expression.append(formula.getString("expression2"));
					}
					
					//字段2是字符串
					if(inputFieldTypeMap.get(formula.getString("field_code2")) == 2){
						expression.append(EngineOperator.OPERATOR_EQUALS_STRING);
						expression.append(EngineOperator.OPERATOR_LEFT_PARENTHESES);
						expression.append(EngineOperator.OPERATOR_VARIABLE_LEFT);
						expression.append(formula.getString("field_code2"));
						expression.append(EngineOperator.OPERATOR_VARIABLE_RIGHT);
						expression.append(CommonConst.SYMBOL_COMMA);
						expression.append("'"+formula.getString("expression2")+"'");
						expression.append(EngineOperator.OPERATOR_RIGHT_PARENTHESES);
					}
					
					expression.append(EngineOperator.OPERATOR_RIGHT_PARENTHESES);
					
					JSONObject resultSubJson = new JSONObject();
					resultSubJson.put("result", result);
					resultSubJson.put("resultKey", resultKey);
					resultSubJson.put("formula", expression.toString());
					resultArray.add(resultSubJson);
				}
				resultJson.put("inputs", jsonObject.getIntValue("inputs"));
				resultJson.put("condition_type", jsonObject.getIntValue("condition_type"));
				resultJson.put("input", jsonObject.getJSONArray("input"));
				resultJson.put("output", jsonObject.getJSONObject("output"));
				resultJson.put("conditions", resultArray);
				engineNode.setNodeScript(resultJson.toString());
			}else{
				//公式
				engineNode.setNodeScript(jsonObject.toString());
			}
		}
	}
	
	/**
	 * 处理三个或以上决策变量
	 * @param engineNode
	 * @param jsonObject
	 */
	private static void handleMoreThanTripleDecisionVariables(EngineNode engineNode,JSONObject jsonObject){
		engineNode.setNodeScript(jsonObject.toString());
	}

}

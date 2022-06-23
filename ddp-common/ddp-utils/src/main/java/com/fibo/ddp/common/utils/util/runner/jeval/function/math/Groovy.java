/*
 * Copyright 2002-2007 Robert Breidecker.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fibo.ddp.common.utils.util.runner.jeval.function.math;

import com.fibo.ddp.common.utils.common.MD5;
import com.fibo.ddp.common.utils.util.runner.jeval.EvaluationException;
import com.fibo.ddp.common.utils.util.runner.jeval.Evaluator;
import com.fibo.ddp.common.utils.util.runner.jeval.function.Function;
import com.fibo.ddp.common.utils.util.runner.jeval.function.FunctionException;
import com.fibo.ddp.common.utils.util.runner.jeval.function.FunctionResult;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * This class is a function that executes within Evaluator. The function returns
 * the ceiling value of a double value. See the Math.ceil(double) method in the
 * JDK for a complete description of how this function works.
 */
@Component
public class Groovy implements Function {

	private static final Logger logger = LoggerFactory.getLogger(Groovy.class);

	private static final ScriptEngineManager factory = new ScriptEngineManager();

	public static String GROOVY_SHELL_KEY_PREFIX = "GROOVY_SHELL#";

//	private RedisManager redisManager;

	private static Cache<String, ScriptEngine> scriptClassCache = Caffeine.newBuilder()
			.expireAfterWrite(1, TimeUnit.HOURS)
			.expireAfterAccess(1, TimeUnit.HOURS)
			.build();

	/**
	 * Returns the name of the function - "def main".
	 * 
	 * @return The name of this function class.
	 */
	public String getName() {
		return "def main";
	}

	/**
	 * Executes the function for the specified argument. This method is called
	 * internally by Evaluator.
	 * 
	 * @param evaluator
	 *            An instance of Evaluator.
	 * @param arguments
	 *            A string argument that will be converted to a double value and
	 *            evaluated.
	 * 
	 * @return The ceiling of the argument.
	 * 
	 * @exception FunctionException
	 *                Thrown if the argument(s) are not valid for this function.
	 */
	public FunctionResult execute(final Evaluator evaluator, final String arguments)
			throws FunctionException {
		return null;
	}

//	public String execute(final String expression, Map<String, Object> data) throws EvaluationException {
//		String result = null;
//		try {
//			ScriptEngine scriptEngine = null;
//			String scriptMd5 = PYTHON_SHELL_KEY_PREFIX + MD5.GetMD5Code(expression);
//			ScriptEngine value = (ScriptEngine) SerializeUtils.deserialize(redisManager.get(scriptMd5.getBytes()));
//			if(value != null){
//				scriptEngine = value;
//			} else {
//				scriptEngine = factory.getEngineByName("groovy");
//				scriptEngine.eval(expression);
//				redisManager.set(scriptMd5.getBytes(), SerializeUtils.serialize(scriptEngine), 120);
//			}
//
//			Object functionResult = ((Invocable) scriptEngine).invokeFunction("main", data);
//			result = functionResult.toString();
//		} catch (Exception e) {
//			throw new EvaluationException("执行groovy脚本出错", e);
//		}
//		return result;
//	}

	public String execute(final String expression, Map<String, Object> data) throws EvaluationException {
		String result = null;
		try {
			ScriptEngine scriptEngine = null;
			String scriptMd5 = GROOVY_SHELL_KEY_PREFIX + MD5.GetMD5Code(expression);
			ScriptEngine value = scriptClassCache.getIfPresent(scriptMd5);
			if(value != null){
				scriptEngine = value;
			} else {
				scriptEngine = factory.getEngineByName("groovy");
				scriptEngine.eval(expression);
				scriptClassCache.put(scriptMd5, scriptEngine);
			}

			Object functionResult = ((Invocable) scriptEngine).invokeFunction("main", data);
			if (functionResult!=null){
				result = functionResult.toString();
			}
//			result = functionResult.toString();
		} catch (Exception e) {
			throw new EvaluationException("执行groovy脚本出错", e);
		}
		return result;
	}
	@Autowired
	private Python python;
	public Object executeForObject(final String expression, Map<String, Object> data) throws EvaluationException {
		Object result = null;
		try {
			ScriptEngine scriptEngine = null;
			String scriptMd5 = GROOVY_SHELL_KEY_PREFIX + MD5.GetMD5Code(expression);
			ScriptEngine value = scriptClassCache.getIfPresent(scriptMd5);
			if(value != null){
				scriptEngine = value;
			} else {
				scriptEngine = factory.getEngineByName("groovy");
				scriptEngine.eval(expression);
				scriptClassCache.put(scriptMd5, scriptEngine);
			}

			Object functionResult = ((Invocable) scriptEngine).invokeFunction("main", data);
			if (functionResult!=null){
//				if (functionResult instanceof HashMap||functionResult instanceof ArrayList){
//					result = JSON.toJSONString(functionResult);
//				}else
//				if (functionResult instanceof String){
//					result = functionResult.toString();
//				}else {
//					result = functionResult;
//				}
				result = functionResult;
			}
		} catch (Exception e) {
			throw new EvaluationException("执行groovy脚本出错", e);
		}
		return result;
	}
}
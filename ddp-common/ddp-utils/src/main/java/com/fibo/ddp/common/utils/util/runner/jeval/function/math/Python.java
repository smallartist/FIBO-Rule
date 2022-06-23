package com.fibo.ddp.common.utils.util.runner.jeval.function.math;

import com.fibo.ddp.common.utils.common.MD5;
import com.fibo.ddp.common.utils.util.runner.jeval.EvaluationException;
import com.fibo.ddp.common.utils.util.runner.jeval.Evaluator;
import com.fibo.ddp.common.utils.util.runner.jeval.function.Function;
import com.fibo.ddp.common.utils.util.runner.jeval.function.FunctionException;
import com.fibo.ddp.common.utils.util.runner.jeval.function.FunctionResult;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.python.core.PyDictionary;
import org.python.core.PyObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class Python implements Function {

    private static final Logger logger = LoggerFactory.getLogger(Groovy.class);

    private static final ScriptEngineManager factory = new ScriptEngineManager();

    public static String PYTHON_SHELL_KEY_PREFIX = "JYTHON_SHELL#";

    private static Cache<String, ScriptEngine> scriptClassCache = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .expireAfterAccess(1, TimeUnit.HOURS)
            .build();
    /**
     * Returns the name of the function - "def main".
     *
     * @return The name of this function class.
     */
    @Override
    public String getName() {
        return "if __name__ == \"__main__\":";
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
    @Override
    public FunctionResult execute(Evaluator evaluator, String arguments) throws FunctionException {
        return null;
    }

    public Object executeForObject(final String expression, Map<String, Object> data) throws EvaluationException {
        Object result = null;
        try {
            ScriptEngine scriptEngine = null;
            String scriptMd5 = PYTHON_SHELL_KEY_PREFIX + MD5.GetMD5Code(expression);
            ScriptEngine value = scriptClassCache.getIfPresent(scriptMd5);
            Object functionResult = null;

            if(value != null){
                scriptEngine = value;
            } else {
                scriptEngine = factory.getEngineByName("python");
                scriptEngine.eval(expression);
                scriptClassCache.put(scriptMd5, scriptEngine);
            }
            PyDictionary pyDictionary = new PyDictionary();
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                pyDictionary.put(entry.getKey(),entry.getValue());
            }

//            PythonInterpreter interpreter = new PythonInterpreter();
//            interpreter.exec(new String(expression.getBytes()));
//            PyFunction python_main = interpreter.get("python_main", PyFunction.class);
//            PyObject pyObject = python_main.__call__(pyDictionary);
//            System.out.println(pyObject);
//            String ret = pyObject.toString();
//            String newStr = new String(ret.getBytes("iso8859-1"), "utf-8");  //通过new String(ret.getBytes("iso8859-1"), "utf-8")转一下就好了
//            System.out.println(newStr);  //newStr就不会乱码了
//            System.out.println(getEncode(String.valueOf(pyObject)));
            functionResult = ((Invocable) scriptEngine).invokeFunction("python_main", pyDictionary);
            if (functionResult instanceof PyDictionary){
                PyObject resultPy = (PyObject)functionResult;
                String ret = resultPy.toString();//这里ret可能会乱码
                System.out.println(ret);
            }
            result = functionResult;
        } catch (Exception e) {
            e.printStackTrace();
            throw new EvaluationException("执行Python脚本出错", e);
        }
        return result;
    }
    // 这里可以提供更多地编码格式,另外由于部分编码格式是一致的所以会返回 第一个匹配的编码格式 GBK 和 GB2312
    public static final String[] encodes = new String[] { "UTF-8", "GBK", "GB2312", "ISO-8859-1", "ISO-8859-2" };

    /**
     * 获取字符串编码格式
     *
     * @param str
     * @return
     */
    public static String getEncode(String str) {
        byte[] data = str.getBytes();
        byte[] b = null;
        a:for (int i = 0; i < encodes.length; i++) {
            try {
                b = str.getBytes(encodes[i]);
                if (b.length!=data.length)
                    continue;
                for (int j = 0; j < b.length; j++) {
                    if (b[j] != data[j]) {
                        continue a;
                    }
                }
                return encodes[i];
            } catch (UnsupportedEncodingException e) {
                continue;
            }
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
//        Properties props = new Properties();
////        props.put("python.home", "F:\\Java\\jython\\jython2.7.1\\Lib");
//        props.put("python.console.encoding", "UTF-8");
//        props.put("python.security.respectJavaAccessibility", "false");
//        props.put("python.import.site", "false");
//        Properties preprops = System.getProperties();
//        PythonInterpreter.initialize(preprops, props, new String[0]);
//        PythonInterpreter interpreter = new PythonInterpreter();
//        interpreter.exec("#coding=UTF-8 \n" +
//                "print('a智障v')");
//        interpreter.execfile("E:\\python\\迭代求阶乘.py");


//        PythonInterpreter interpreter = new PythonInterpreter();
//        interpreter.exec("# -*- encoding: utf-8 -*- \na='智障'; ");
//        interpreter.exec("print a;");
//        interpreter.exec("print '智障';");
//        String s =  "python \ndef python_main(_):\n" +
//                "    # result 为返回结果其中内部字段解释为：ruleScore（规则命中时得分），hitResult规则是否命中可选值为：命中/未命中\n" +
//                "    # fieldList 为输出字段列表，内部为字典表，updateInputMap 为需要更新到入参的变量是一个字典表\n" +
//                "\n" +
//                "    result = {\"ruleScore\":0,\"hitResult\":\"未命中\",\"fieldList\":[],\"updateInputMap\":{}}\n" +
//                "    print(_)\n" +
//                "    print(\"未命中\")\n" +
//                "    result[\"ruleScore\"] = 420\n" +
//                "    result[\"hitResult\"] = \"命中\"\n" +
//                "    return result\n" +
//                "\n" +
//                "if __name__ == \"__main__\":\n" +
//                "    python_main(params)";
//        String[] inputParam = new String[2];
//        inputParam[0] = "python3";
//        inputParam[1] = s;
//        Runtime.getRuntime().exec(s);
    }

}

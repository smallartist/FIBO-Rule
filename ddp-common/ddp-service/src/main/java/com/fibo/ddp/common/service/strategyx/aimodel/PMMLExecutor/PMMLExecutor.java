package com.fibo.ddp.common.service.strategyx.aimodel.PMMLExecutor;

import org.jpmml.evaluator.Evaluator;

import java.util.List;
import java.util.Map;

public interface PMMLExecutor {

    Evaluator loadPmml(String filePath);

    double predict(Evaluator evaluator, Map<String, Object> input);

    List<String> parseField(Evaluator evaluator);
}

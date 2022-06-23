package com.fibo.ddp.common.service.strategyx.aimodel.PMMLExecutor.impl;

import com.fibo.ddp.common.service.strategyx.aimodel.PMMLExecutor.PMMLExecutor;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.PMML;
import org.jpmml.evaluator.*;
import org.jpmml.model.PMMLUtil;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class PMMLExecutorRFImpl implements PMMLExecutor {

    @Override
    public Evaluator loadPmml(String filePath) {
//        String filePath = "D:\\models\\model_RF.pmml";
        PMML pmml = new PMML();
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (inputStream == null) {
            return null;
        }

        InputStream is = inputStream;
        try {
            pmml = PMMLUtil.unmarshal(is);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ModelEvaluatorFactory modelEvaluatorFactory = ModelEvaluatorFactory.newInstance();
        Evaluator evaluator = modelEvaluatorFactory.newModelEvaluator(pmml);
        pmml = null;
        return evaluator;
    }

    @Override
    public double predict(Evaluator evaluator, Map<String, Object> input) {
        List<InputField> inputFields = evaluator.getInputFields();
        Map<FieldName, FieldValue> arguments = new LinkedHashMap<>();
        for (InputField inputField : inputFields) {
            FieldName inputFieldName = inputField.getName();
            Object rawValue = input.get(inputFieldName.getValue());
            FieldValue inputFieldValue = inputField.prepare(rawValue);
            arguments.put(inputFieldName, inputFieldValue);
        }

        Map<FieldName, ?> results = evaluator.evaluate(arguments);
        List<TargetField> targetFields = evaluator.getTargetFields();

        TargetField targetField = targetFields.get(0);
        FieldName targetFieldName = targetField.getName();

        Object targetFieldValue = results.get(targetFieldName);
        System.out.println("target: " + targetFieldName.getValue() + ", value: " + targetFieldValue);

        double value_1 = 0.0f;
        if (targetFieldValue instanceof ProbabilityDistribution) {
            value_1 = ((ProbabilityDistribution) targetFieldValue).getValue("1");
        }
        return value_1;
    }

    @Override
    public List<String> parseField(Evaluator evaluator) {
        List<String> fieldList = new ArrayList<>();
        List<InputField> inputFields = evaluator.getInputFields();
        for (InputField inputField : inputFields) {
            FieldName inputFieldName = inputField.getName();
            fieldList.add(inputFieldName.getValue());
        }
        return fieldList;
    }
}

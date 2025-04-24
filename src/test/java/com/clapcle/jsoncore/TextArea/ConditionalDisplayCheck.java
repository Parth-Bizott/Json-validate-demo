package com.clapcle.jsoncore.TextArea;

import com.clapcle.jsoncore.formjson.form.Form;
import com.clapcle.jsoncore.formjson.jsonparser.ValidateError;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static com.clapcle.jsoncore.formjson.jsonparser.FormJsonParser.parseForm;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConditionalDisplayCheck {
    @Test
    void checkConditionalDisplay_metWithCondition() throws IOException {

        String jsonContent = Files.readString(Paths.get("/home/bizott-2/ERP/json-core-zip/json-core/src/test/resources/TextArea/SampleJson/conditionalDisplay_sample.json"));
        Form form = parseForm(jsonContent);

        String dataContent = Files.readString(Paths.get("/home/bizott-2/ERP/json-core-zip/json-core/src/test/resources/TextArea/dataJson/conditionalDisplay_data.json"));
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Map<String, String>> dataMap = objectMapper.readValue(dataContent, new TypeReference<Map<String, Map<String, String>>>() {
        });

        Map<String,Object> finalDataMap = new HashMap<>();

        dataMap.forEach((k,v) -> {
            finalDataMap.putAll(v);
        });

        Map<String, Map<String, ValidateError>> errorMap = form.validate(dataMap, finalDataMap);

        Map<String, ValidateError> personalInfo = errorMap.get("personalInfo");

        assertEquals("FAIL", personalInfo.get("professionalSummary").getValidationStatus().toString());

        assertEquals("PASS", personalInfo.get("personSurName").getValidationStatus().toString());

        assertEquals("PASS", personalInfo.get("expectedSalary").getValidationStatus().toString());
    }

    @Test
    void checkConditionalDisplay_metWithConditionSuccess() throws IOException {

        String jsonContent = Files.readString(Paths.get("/home/bizott-2/ERP/json-core-zip/json-core/src/test/resources/TextArea/SampleJson/conditionalDisplay_sample.json"));
        Form form = parseForm(jsonContent);

        String dataContent = Files.readString(Paths.get("/home/bizott-2/ERP/json-core-zip/json-core/src/test/resources/TextArea/dataJson/conditionalDisplay_data_success.json"));
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Map<String, String>> dataMap = objectMapper.readValue(dataContent, new TypeReference<Map<String, Map<String, String>>>() {
        });

        Map<String,Object> finalDataMap = new HashMap<>();

        dataMap.forEach((k,v) -> {
            finalDataMap.putAll(v);
        });

        Map<String, Map<String, ValidateError>> errorMap = form.validate(dataMap, finalDataMap);

        Map<String, ValidateError> personalInfo = errorMap.get("personalInfo");

        assertEquals("PASS", personalInfo.get("professionalSummary").getValidationStatus().toString());

        assertEquals("PASS", personalInfo.get("personSurName").getValidationStatus().toString());

        assertEquals("PASS", personalInfo.get("expectedSalary").getValidationStatus().toString());
    }
}

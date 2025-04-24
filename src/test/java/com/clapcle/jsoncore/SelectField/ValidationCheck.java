package com.clapcle.jsoncore.SelectField;

import com.clapcle.jsoncore.formjson.form.Form;
import com.clapcle.jsoncore.formjson.jsonparser.ValidateError;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static com.clapcle.jsoncore.formjson.jsonparser.FormJsonParser.parseForm;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ValidationCheck {

    @Test
    void checkValidation_required() throws IOException, URISyntaxException {

        String jsonContent = Files.readString(Paths.get(ClassLoader.getSystemResource("SelectField/Samplejson/validation_sample.json").toURI()));
        Form form = parseForm(jsonContent);

        String dataContent = Files.readString(Paths.get(ClassLoader.getSystemResource("SelectField/dataJson/validation_data_required.json").toURI()));
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Map<String, String>> dataMap = objectMapper.readValue(dataContent, new TypeReference<Map<String, Map<String, String>>>() {
        });

        Map<String,Object> finalDataMap = new HashMap<>();

        dataMap.forEach((k,v) -> {
            finalDataMap.putAll(v);
        });

        Map<String, Map<String, ValidateError>> errorMap = form.validate(dataMap, finalDataMap);

        Map<String, ValidateError> personalInfo = errorMap.get("personalInfo");

        assertEquals("FAIL", personalInfo.get("degreeLevel").getValidationStatus().toString());
    }

    @Test
    void checkValidation_all() throws IOException, URISyntaxException {
        String jsonContent = Files.readString(Paths.get(ClassLoader.getSystemResource("SelectField/Samplejson/validation_sample.json").toURI()));
        Form form = parseForm(jsonContent);

        String dataContent = Files.readString(Paths.get(ClassLoader.getSystemResource("SelectField/dataJson/validation_data_all.json").toURI()));
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Map<String, String>> dataMap = objectMapper.readValue(dataContent, new TypeReference<Map<String, Map<String, String>>>() {
        });

        Map<String,Object> finalDataMap = new HashMap<>();

        dataMap.forEach((k,v) -> {
            finalDataMap.putAll(v);
        });

        Map<String, Map<String, ValidateError>> errorMap = form.validate(dataMap, finalDataMap);

        Map<String, ValidateError> personalInfo = errorMap.get("personalInfo");

        assertEquals("PASS", personalInfo.get("degreeLevel").getValidationStatus().toString());
    }

    @Test
    void checkValidation_malicious() throws IOException {
        String jsonContent = Files.readString(Paths.get("/home/bizott-2/ERP/json-core-zip/json-core/src/test/resources/PhoneField/SampleJson/validation_sample.json"));
        Form form = parseForm(jsonContent);

        String dataContent = Files.readString(Paths.get("/home/bizott-2/ERP/json-core-zip/json-core/src/test/resources/PhoneField/dataJson/validation_data_malicious.json"));
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Map<String, String>> dataMap = objectMapper.readValue(dataContent, new TypeReference<Map<String, Map<String, String>>>() {
        });

        Map<String,Object> finalDataMap = new HashMap<>();

        dataMap.forEach((k,v) -> {
            finalDataMap.putAll(v);
        });

        Map<String, Map<String, ValidateError>> errorMap = form.validate(dataMap, finalDataMap);

        Map<String, ValidateError> personalInfo = errorMap.get("personalInfo");

        assertEquals("FAIL", personalInfo.get("phoneNumber").getValidationStatus().toString());
    }

}

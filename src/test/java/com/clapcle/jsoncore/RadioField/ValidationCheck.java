package com.clapcle.jsoncore.RadioField;

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
        String jsonContent = Files.readString(Paths.get(ClassLoader.getSystemResource("RadioField/SampleJson/validation_sample.json").toURI()));
        Form form = parseForm(jsonContent);

        String dataContent = Files.readString(Paths.get(ClassLoader.getSystemResource("RadioField/dataJson/validation_data_required.json").toURI()));
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Map<String, Object>> dataMap = objectMapper.readValue(dataContent, new TypeReference<Map<String, Map<String, Object>>>() {
        });

        Map<String,Object> finalDataMap = new HashMap<>();

        dataMap.forEach((k,v) -> {
            finalDataMap.putAll(v);
        });

        Map<String, Map<String, ValidateError>> errorMap = form.validate(dataMap, finalDataMap);

        Map<String, ValidateError> personalInfo = errorMap.get("personalInfo");

        assertEquals("FAIL", personalInfo.get("educationLevel").getValidationStatus().toString());
    }


    @Test
    void checkValidation_all() throws IOException, URISyntaxException {
        String jsonContent = Files.readString(Paths.get(ClassLoader.getSystemResource("RadioField/SampleJson/validation_sample.json").toURI()));
        Form form = parseForm(jsonContent);

        String dataContent = Files.readString(Paths.get(ClassLoader.getSystemResource("RadioField/dataJson/validation_data_all.json").toURI()));
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Map<String, Object>> dataMap = objectMapper.readValue(dataContent, new TypeReference<Map<String, Map<String, Object>>>() {
        });

        Map<String,Object> finalDataMap = new HashMap<>();

        dataMap.forEach((k,v) -> {
            finalDataMap.putAll(v);
        });

        Map<String, Map<String, ValidateError>> errorMap = form.validate(dataMap, finalDataMap);

        Map<String, ValidateError> personalInfo = errorMap.get("personalInfo");

        assertEquals("PASS", personalInfo.get("educationLevel").getValidationStatus().toString());
    }

    @Test
    void checkValidation_malicious() throws IOException, URISyntaxException {
        String jsonContent = Files.readString(Paths.get(ClassLoader.getSystemResource("RadioField/SampleJson/validation_sample.json").toURI()));
        Form form = parseForm(jsonContent);

        String dataContent = Files.readString(Paths.get(ClassLoader.getSystemResource("RadioField/dataJson/validation_data_malicious.json").toURI()));
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Map<String, Object>> dataMap = objectMapper.readValue(dataContent, new TypeReference<Map<String, Map<String, Object>>>() {
        });

        Map<String,Object> finalDataMap = new HashMap<>();

        dataMap.forEach((k,v) -> {
            finalDataMap.putAll(v);
        });

        Map<String, Map<String, ValidateError>> errorMap = form.validate(dataMap, finalDataMap);

        Map<String, ValidateError> personalInfo = errorMap.get("personalInfo");

        assertEquals("FAIL", personalInfo.get("educationLevel").getValidationStatus().toString());
    }

}

package com.clapcle.jsoncore.formjson.form;


import com.clapcle.jsoncore.formjson.jsonparser.ValidateError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Form {
    private String formTitle;
    private String formDescription;
    private String submitButtonText;
    private Layout layout;
    private List<Section> sections;

//    // Field map for efficient lookup
//    private transient Map<String, Field> fieldMap;
//
//    public Map<String, Field> getFieldMap() {
//        if (fieldMap == null) {
//            fieldMap = new HashMap<>();
//            if (sections != null) {
//                for (Section section : sections) {
//                    if (section.getFields() != null) {
//                        for (Field field : section.getFields()) {
//                            fieldMap.put(field.getId(), field);
//                        }
//                    }
//                }
//            }
//        }
//        return fieldMap;
//    }

    public Map<String, Map<String, ValidateError>> validate(Map<String, Map<String, String>> dataMap, Map<String, Object> finalDataMap) {
        Map<String, Map<String, ValidateError>> errorMap = new HashMap<>();

        getSections().forEach(section -> {
            Map<String, String> map = dataMap.get(section.getId());

            errorMap.put(section.getId(), new HashMap<>());

            section.getFields().forEach(field -> {
                if (map.containsKey(field.getId())) {
                    errorMap.get(section.getId()).put(field.getId(), field.toValidate(finalDataMap));
                }
            });
        });
        return errorMap;
    }

}
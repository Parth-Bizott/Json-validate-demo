package com.clapcle.jsoncore.formjson.form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidationRule {
    private String type;
    private Object value;
    private String message;
}
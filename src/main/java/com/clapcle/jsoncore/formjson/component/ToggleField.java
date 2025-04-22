package com.clapcle.jsoncore.formjson.component;

import com.clapcle.jsoncore.formjson.form.Field;
import com.clapcle.jsoncore.formjson.jsonparser.ValidateError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ToggleField extends Field {
    private String onText;
    private String offText;
    private String onValue;
    private String offValue;

    @Override
    public ValidateError toValidate(Map<String, String> data) {
        return null;
    }
}
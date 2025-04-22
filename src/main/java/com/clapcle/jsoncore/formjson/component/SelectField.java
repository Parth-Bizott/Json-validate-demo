package com.clapcle.jsoncore.formjson.component;

import com.clapcle.jsoncore.formjson.form.Field;
import com.clapcle.jsoncore.formjson.jsonparser.ValidateError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SelectField extends Field {
    private boolean selectAll;
    private boolean allowCreate;
    private String createLabel;
    private int maxSelections;
    private DataSource dataSource;
    private List<String> displayFields;

    @Override
    public ValidateError toValidate(Map<String, String> data) {
        return null;
    }
}
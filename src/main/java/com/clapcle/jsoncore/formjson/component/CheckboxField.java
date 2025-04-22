package com.clapcle.jsoncore.formjson.component;

import com.clapcle.jsoncore.formjson.form.Field;
import com.clapcle.jsoncore.formjson.jsonparser.ValidateError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckboxField extends Field {
    private List<Option> options;
    private boolean allowSelectAll;
    private int minSelectCount;
    private int maxSelectCount;
    private boolean allowOther;
    private String otherPlaceholder;


    @Override
    public ValidateError toValidate(Map<String, String> data) {
        // written validation hear
        return null;
    }
}
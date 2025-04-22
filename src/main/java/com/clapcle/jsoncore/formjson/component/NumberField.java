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
public class NumberField extends Field {
    private double min;
    private double max;
    private double step;
    private boolean showSpinners;
    private String format;
    private int decimalPlaces;

    @Override
    public ValidateError toValidate(Map<String, String> data) {
        return null;
    }
}
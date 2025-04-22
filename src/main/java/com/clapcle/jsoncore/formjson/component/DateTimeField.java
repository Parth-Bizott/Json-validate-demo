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
public class DateTimeField extends Field {
    private String dateFormat;
    private String timeFormat;
    private String minDateTime;
    private String maxDateTime;
    private boolean showCalendarIcon;
    private boolean use12HourFormat;

    @Override
    public ValidateError toValidate(Map<String, String> data) {
        return null;
    }
}
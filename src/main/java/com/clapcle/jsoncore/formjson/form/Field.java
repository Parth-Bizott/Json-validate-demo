package com.clapcle.jsoncore.formjson.form;

import com.clapcle.jsoncore.formjson.component.*;
import com.clapcle.jsoncore.formjson.jsonparser.ValidateError;
import com.clapcle.jsoncore.formjson.jsonparser.ValidationStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextField.class, name = "TEXT"),
        @JsonSubTypes.Type(value = TextAreaField.class, name = "TEXTAREA"),
        @JsonSubTypes.Type(value = CurrencyField.class, name = "CURRENCY"),
        @JsonSubTypes.Type(value = PhoneField.class, name = "PHONE"),
        @JsonSubTypes.Type(value = CurrencyField.class, name = "CURRENCY"),
        @JsonSubTypes.Type(value = RadioField.class, name = "RADIO"),
        @JsonSubTypes.Type(value = EmailField.class, name = "EMAIL"),
        @JsonSubTypes.Type(value = DateRangePicker.class, name = "DATE_RANGE_PICKER"),
        @JsonSubTypes.Type(value = SelectField.class, name = "SELECT"),
        @JsonSubTypes.Type(value = SelectSearchField.class, name = "SELECT_SEARCH"),
        @JsonSubTypes.Type(value = MultiSelectField.class, name = "MULTISELECT"),
        @JsonSubTypes.Type(value = MultiSelectSearchField.class, name = "MULTI_SELECT_SEARCH"),
        @JsonSubTypes.Type(value = DateField.class, name = "DATE"),
        @JsonSubTypes.Type(value = TimeField.class, name = "TIME"),
        @JsonSubTypes.Type(value = PasswordField.class, name = "PASSWORD"),
        @JsonSubTypes.Type(value = CheckboxField.class, name = "CHECKBOX"),
        @JsonSubTypes.Type(value = DateTimeField.class, name = "DATETIME"),
        @JsonSubTypes.Type(value = BooleanField.class, name = "BOOLEAN"),
        @JsonSubTypes.Type(value = NumberField.class, name = "NUMBER"),
        @JsonSubTypes.Type(value = RepeatingSectionField.class, name = "REPEATING_SECTION"),
        @JsonSubTypes.Type(value = UrlField.class, name = "URL"),
        @JsonSubTypes.Type(value = RangeField.class, name = "RANGE")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Field {

    private String id;
    private String name;
    private String label;

    @JsonProperty("type")
    private String type;
    private Object defaultValue;
    private String description;
    private boolean hidden;
    private boolean disabled;
    private String placeholder;
    private String helperText;
    private String visibility;

    @JsonProperty("editability")
    private String editability;

    @JsonProperty("conditionalDisplay")
    private String conditionalDisplay;

    @JsonProperty("validations")
    private List<ValidationRule> validationRules;

    public abstract ValidateError toValidate(Map<String, Object> data) throws Exception;

    public ValidateError checkMaliciousInput(Object requestedValue) {
        ValidateError validateError = new ValidateError();

        if (requestedValue == null) {
            return null;
        }
        String maliciousPattern = "(?i)(<script>|</script>|<.*?javascript:.*?>|--|;|\\b(select|drop|insert|delete|update|union|xpath|eval)\\b|&#[xX]?[0-9a-fA-F]+;)";


        if (requestedValue instanceof String strVal) {
            if (strVal.matches(".*" + maliciousPattern + ".*")) {
                validateError.setValidationStatus(ValidationStatus.FAIL);
                validateError.setErrorMessage("The provided value '" + requestedValue + " was not accepted due to potentially malicious input detected:");
                return validateError;
            }
        } else if (requestedValue instanceof List<?> list) {
            for (Object item : list) {
                if (item instanceof String strItem && strItem.matches(".*" + maliciousPattern + ".*")) {
                    validateError.setValidationStatus(ValidationStatus.FAIL);
                    validateError.setErrorMessage("The provided value '" + requestedValue + " was not accepted due to potentially malicious input detected:");
                    return validateError;
                }
            }
        } else {
            String strVal = String.valueOf(requestedValue);
            if (strVal.matches(".*" + maliciousPattern + ".*")) {
                validateError.setValidationStatus(ValidationStatus.FAIL);
                validateError.setErrorMessage("The provided value '" + requestedValue + " was not accepted due to potentially malicious input detected:");
                return validateError;
            }
        }
        return null;

    }
}
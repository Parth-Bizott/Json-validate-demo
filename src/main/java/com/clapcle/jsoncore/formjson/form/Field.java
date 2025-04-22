package com.clapcle.jsoncore.formjson.form;

import com.clapcle.jsoncore.formjson.component.CurrencyField;
import com.clapcle.jsoncore.formjson.component.TextAreaField;
import com.clapcle.jsoncore.formjson.component.TextField;
import com.clapcle.jsoncore.formjson.jsonparser.ValidateError;
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
        @JsonSubTypes.Type(value = CurrencyField.class, name = "CURRENCY")
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
    private String readOnlyDisplay;
    private String placeholder;
    private String helperText;
    private String visibility;

    @JsonProperty("editability")
    private String editability;

    @JsonProperty("conditionalDisplay")
    private String conditionalDisplay;

    @JsonProperty("validations")
    private List<ValidationRule> validationRules;

    public abstract ValidateError toValidate(Map<String, String> data);
}
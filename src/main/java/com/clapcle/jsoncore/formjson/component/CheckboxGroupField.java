package com.clapcle.jsoncore.formjson.component;

import com.clapcle.jsoncore.formjson.component.subcomponent.Option;
import com.clapcle.jsoncore.formjson.form.Field;
import com.clapcle.jsoncore.formjson.form.ValidationRule;
import com.clapcle.jsoncore.formjson.jsonparser.ValidateError;
import com.clapcle.jsoncore.formjson.jsonparser.ValidationStatus;
import com.clapcle.jsoncore.formjson.util.FormValidationUtility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckboxGroupField extends Field {
    private List<Option> options;
    private boolean allowSelectAll;
    private int maxSelectCount;
    private boolean allowOther;
    private String otherPlaceholder;


    @Override
    public ValidateError toValidate(Map<String, Object> data) {
        ValidateError validateError = new ValidateError();
        Object requestValue = data.get(getId());
        List<?> selectedValues = null;
        if (requestValue instanceof List) {
            selectedValues = (List<?>) requestValue;
        } else if (requestValue instanceof String stringValue) {
            selectedValues = List.of(stringValue);
        }


        if (getConditionalDisplay() != null && !getConditionalDisplay().isEmpty()) {
            boolean b = FormValidationUtility.validateFormula(data, getConditionalDisplay());
            if (!b && requestValue != null) {
                validateError.setValidationStatus(ValidationStatus.FAIL);
                validateError.setErrorMessage("The provided value '" + requestValue + " was not accepted due to failing the conditional display criteria.");
                return validateError;
            }
        }

        if (getEditability() != null && !getEditability().isEmpty()) {
            boolean b = FormValidationUtility.validateFormula(data, getEditability());
            if (!b && requestValue != null) {
                validateError.setValidationStatus(ValidationStatus.FAIL);
                validateError.setErrorMessage("The provided value '" + requestValue + " was not accepted due to failing the editability criteria.");
            }
        }

        if (ObjectUtils.isNotEmpty(getValidationRules())) {
            for (ValidationRule rule : getValidationRules()) {
                String type = rule.getType();
                String value = (String) rule.getValue();
                switch (type) {
                    case "required":
                        if ("true".equalsIgnoreCase(value) && (selectedValues == null || selectedValues.isEmpty())) {
                            validateError.setValidationStatus(ValidationStatus.FAIL);
                            validateError.setValidationRule(rule);
                            return validateError;
                        }
                        break;
                }

                switch (type) {
                    case "minSelections":
                        int min = Integer.parseInt(value);
                        if (selectedValues == null || selectedValues.size() < min) {
                            validateError.setValidationStatus(ValidationStatus.FAIL);
                            validateError.setValidationRule(rule);
                            return validateError;
                        }
                        break;
                }
            }
        }
        validateError.setValidationStatus(ValidationStatus.PASS);
        return validateError;

    }
}
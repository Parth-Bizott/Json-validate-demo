package com.clapcle.jsoncore.formjson.component;

import com.clapcle.jsoncore.formjson.form.Field;
import com.clapcle.jsoncore.formjson.form.ValidationRule;
import com.clapcle.jsoncore.formjson.jsonparser.ValidateError;
import com.clapcle.jsoncore.formjson.jsonparser.ValidationStatus;
import com.clapcle.jsoncore.formjson.util.FormValidationUtility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RepeatingSectionField extends Field {
    private List<? extends Field> fields;

    @Override
    public ValidateError toValidate(Map<String, Object> data) throws Exception {

        ValidateError validateError = new ValidateError();
        Object requestValue = data.get(getId());
        List<?> list = new ArrayList<>();
        if (requestValue instanceof List<?>) {
            list = (List<?>) requestValue;
        } else {
            throw new RuntimeException("The provided value '" + requestValue + "' was not accepted due to not being a list.");
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
                return validateError;
            }
        }

        if (ObjectUtils.isNotEmpty(getValidationRules())) {
            for (ValidationRule rule : getValidationRules()) {
                String type = rule.getType();
                String value = rule.getValue().toString();
                switch (type) {
                    case "required":
                        if ("true".equalsIgnoreCase(value) && (list == null || list.isEmpty())) {
                            validateError.setValidationStatus(ValidationStatus.FAIL);
                            validateError.setValidationRule(rule);
                            return validateError;
                        }
                        break;
                    case "minSelections":
                        int min = Integer.parseInt(value);
                        if (list == null || list.size() < min) {
                            validateError.setValidationStatus(ValidationStatus.FAIL);
                            validateError.setValidationRule(rule);
                            return validateError;
                        }
                        break;
                    case "maxSelections":
                        int max = Integer.parseInt(value);
                        if (list != null && list.size() > max) {
                            validateError.setValidationStatus(ValidationStatus.FAIL);
                            validateError.setValidationRule(rule);
                            return validateError;
                        }
                        break;
                }
            }
        }

        for (Object map : list) {
            if (!(map instanceof Map)) {
                throw new RuntimeException("The provided value '" + map + "' was not accepted due to not being a map.");
            }
            for (Field field : fields) {
                try {
                    validateError = field.toValidate((Map<String, Object>) map);
                    if (validateError != null && validateError.getValidationStatus().equals(ValidationStatus.FAIL)) {
                        return validateError;
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return validateError;
    }
}

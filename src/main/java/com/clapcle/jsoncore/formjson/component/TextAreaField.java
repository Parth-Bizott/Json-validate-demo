package com.clapcle.jsoncore.formjson.component;

import com.clapcle.jsoncore.formjson.form.Field;
import com.clapcle.jsoncore.formjson.form.ValidationRule;
import com.clapcle.jsoncore.formjson.jsonparser.ValidateError;
import com.clapcle.jsoncore.formjson.jsonparser.ValidationStatus;
import com.clapcle.jsoncore.formjson.util.FormValidationUtility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TextAreaField extends Field {

    @Override
    public ValidateError toValidate(Map<String, String> data) {

        ValidateError validateError = new ValidateError();
        String requestValue = data.get(getId());

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
                        if ("true".equalsIgnoreCase(value) && (requestValue == null || requestValue.trim().isEmpty())) {
                            validateError.setValidationStatus(ValidationStatus.FAIL);
                            validateError.setValidationRule(rule);
                            return validateError;
                        }
                        break;
                }
                switch (type) {
                    case "minLength":
                        if (requestValue != null && requestValue.length() < Integer.parseInt(value)) {
                            validateError.setValidationStatus(ValidationStatus.FAIL);
                            validateError.setValidationRule(rule);
                            return validateError;
                        }
                }

                switch (type) {
                    case "maxLength":
                        if (requestValue != null && requestValue.length() > Integer.parseInt(value)) {
                            validateError.setValidationStatus(ValidationStatus.FAIL);
                            validateError.setValidationRule(rule);
                            return validateError;
                        }
                }
                switch (type) {
                    case "pattern":
                        if (requestValue != null && !requestValue.matches(value)) {
                            validateError.setValidationStatus(ValidationStatus.FAIL);
                            validateError.setValidationRule(rule);
                            return validateError;
                        }
                }
            }
        }
        validateError.setValidationStatus(ValidationStatus.PASS);
        return validateError;
    }
}
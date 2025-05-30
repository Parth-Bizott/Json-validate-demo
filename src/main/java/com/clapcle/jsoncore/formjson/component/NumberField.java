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
public class NumberField extends Field {
    private String min;
    private String max;
    private double step;
    private String format;

    @Override
    public ValidateError toValidate(Map<String, Object> data) {

        ValidateError validateError = new ValidateError();
        String requestValue = (String) data.get(getId());

        ValidateError maliciousCheck = super.checkMaliciousInput(requestValue);
        if (maliciousCheck != null) {
            return maliciousCheck;
        }

        if(getMax()!=null && getMin()!=null){
            if(Integer.parseInt(requestValue) < Integer.parseInt(getMin()) || Integer.parseInt(requestValue) > Integer.parseInt(getMax())){
                validateError.setValidationStatus(ValidationStatus.FAIL);
                validateError.setErrorMessage("The provided value '" + requestValue + " was not accepted due to failing the range criteria.");
                return validateError;
            }
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
                String value = (String) rule.getValue();

                switch (type) {
                    case "required":
                        if ("true".equalsIgnoreCase(value) && (requestValue == null || requestValue.trim().isEmpty())) {
                            validateError.setValidationStatus(ValidationStatus.FAIL);
                            validateError.setValidationRule(rule);
                            return validateError;
                        }
                        break;
                    case "maxValue":
                        if (requestValue != null && Integer.parseInt(requestValue) > Integer.parseInt(value)) {
                            validateError.setValidationStatus(ValidationStatus.FAIL);
                            validateError.setValidationRule(rule);
                            return validateError;
                        }
                        break;
                    case "minValue":
                        if (requestValue != null && Integer.parseInt(requestValue) < Integer.parseInt(value)) {
                            validateError.setValidationStatus(ValidationStatus.FAIL);
                            validateError.setValidationRule(rule);
                            return validateError;
                        }
                        break;
                    case "pattern":
                        if (requestValue != null && !requestValue.matches(value)) {
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
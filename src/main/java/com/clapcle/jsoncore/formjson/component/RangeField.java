package com.clapcle.jsoncore.formjson.component;

import com.clapcle.jsoncore.formjson.form.Field;
import com.clapcle.jsoncore.formjson.form.ValidationRule;
import com.clapcle.jsoncore.formjson.jsonparser.ValidateError;
import com.clapcle.jsoncore.formjson.jsonparser.ValidationStatus;
import com.clapcle.jsoncore.formjson.util.FormValidationUtility;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RangeField extends Field {

    private String startRange;
    private String endRange;

    @Override
    public ValidateError toValidate(Map<String, Object> data) throws Exception {
        ValidateError validateError = new ValidateError();

        String requestValue = (String) data.get(getId());
        ValidateError maliciousCheck = super.checkMaliciousInput(requestValue);
        if (maliciousCheck != null) {
            return maliciousCheck;
        }

        if(getStartRange() != null && getEndRange() != null){
            if(Integer.parseInt(requestValue) < Integer.parseInt(getStartRange()) || Integer.parseInt(requestValue) > Integer.parseInt(getEndRange())){
                validateError.setValidationStatus(ValidationStatus.FAIL);
                validateError.setErrorMessage("The provided value '" + requestValue + "' is not within the range of " + getStartRange() + " to " + getEndRange());
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
                    case "maxRange":
                        if (requestValue != null && Integer.parseInt(requestValue) > Integer.parseInt(value)) {
                            validateError.setValidationStatus(ValidationStatus.FAIL);
                            validateError.setValidationRule(rule);
                            return validateError;
                        }
                        break;


                    case "minRange":
                        if (requestValue != null && Integer.parseInt(requestValue) < Integer.parseInt(value)) {
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

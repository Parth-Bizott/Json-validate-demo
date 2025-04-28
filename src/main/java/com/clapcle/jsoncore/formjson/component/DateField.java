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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DateField extends Field {
    private String format;

    @Override
    public ValidateError toValidate(Map<String, Object> data) {
        ValidateError validateError = new ValidateError();
        String requestValue = (String) data.get(getId());

        ValidateError maliciousCheck = super.checkMaliciousInput(requestValue);
        if (maliciousCheck != null) {
            return maliciousCheck;
        }

        if (getConditionalDisplay() != null && !getConditionalDisplay().isEmpty()) {
            boolean conditionPassed = FormValidationUtility.validateFormula(data, getConditionalDisplay());
            if (!conditionPassed && requestValue != null) {
                validateError.setValidationStatus(ValidationStatus.FAIL);
                validateError.setErrorMessage("The provided value '" + requestValue + " was not accepted due to failing the conditional display criteria.");
                return validateError;
            }
        }

        if (getEditability() != null && !getEditability().isEmpty()) {
            boolean editable = FormValidationUtility.validateFormula(data, getEditability());
            if (!editable && requestValue != null) {
                validateError.setValidationStatus(ValidationStatus.FAIL);
                validateError.setErrorMessage("The provided value '" + requestValue + " was not accepted due to failing the editability criteria.");
                return validateError;
            }
        }

        String dateFormat = (getFormat() != null && !getFormat().isEmpty()) ? getFormat() : "yyyy-MM-dd";

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
                    case "minDate":
                        if (requestValue != null && !requestValue.trim().isEmpty()) {
                            LocalDate inputDate = LocalDate.parse(requestValue, DateTimeFormatter.ofPattern(dateFormat));
                            LocalDate min = LocalDate.parse(value, DateTimeFormatter.ofPattern(dateFormat));
                            if (inputDate.isBefore(min)) {
                                validateError.setValidationStatus(ValidationStatus.FAIL);
                                validateError.setValidationRule(rule);
                                return validateError;
                            }
                        }
                        break;
                    case "maxDate":
                        if (requestValue != null && !requestValue.trim().isEmpty()) {
                            LocalDate inputDate = LocalDate.parse(requestValue, DateTimeFormatter.ofPattern(dateFormat));
                            LocalDate max = LocalDate.parse(value, DateTimeFormatter.ofPattern(dateFormat));
                            if (inputDate.isAfter(max)) {
                                validateError.setValidationStatus(ValidationStatus.FAIL);
                                validateError.setValidationRule(rule);
                                return validateError;
                            }
                        }
                        break;
                }
            }
        }
        validateError.setValidationStatus(ValidationStatus.PASS);
        return validateError;

    }
}
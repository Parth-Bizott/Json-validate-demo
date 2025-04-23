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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DateTimeField extends Field {
    private String dateTimeFormat;
    private String minDateTime;
    private String maxDateTime;
    private boolean showCalendarIcon;
    private boolean use12HourFormat;

    @Override
    public ValidateError toValidate(Map<String, Object> data) {
        ValidateError validateError = new ValidateError();
        String requestValue = (String) data.get(getId());

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
            }
        }
        String dateTimeFormat = (getDateTimeFormat() != null && !getDateTimeFormat().isEmpty()) ? getDateTimeFormat() : "yyyy-MM-dd HH:mm";

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
                    case "minDate":
                        if (requestValue != null && !requestValue.trim().isEmpty()) {
                            LocalDateTime input = LocalDateTime.parse(requestValue, DateTimeFormatter.ofPattern(dateTimeFormat));
                            LocalDate inputDate = input.toLocalDate();
                            LocalDate ruleDate = LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            if (inputDate.isBefore(ruleDate)) {
                                validateError.setValidationStatus(ValidationStatus.FAIL);
                                validateError.setValidationRule(rule);
                                return validateError;
                            }
                        }
                        break;
                }

                switch (type) {
                    case "maxDate":
                        if (requestValue != null && !requestValue.trim().isEmpty()) {
                            LocalDateTime input = LocalDateTime.parse(requestValue, DateTimeFormatter.ofPattern(dateTimeFormat));
                            LocalDate inputDate = input.toLocalDate();
                            LocalDate ruleDate = LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            if (inputDate.isAfter(ruleDate)) {
                                validateError.setValidationStatus(ValidationStatus.FAIL);
                                validateError.setValidationRule(rule);
                                return validateError;
                            }
                        }
                        break;
                }

                switch (type) {
                    case "minTime":
                        if (requestValue != null && !requestValue.trim().isEmpty()) {
                            LocalDateTime input = LocalDateTime.parse(requestValue, DateTimeFormatter.ofPattern(dateTimeFormat));
                            LocalTime inputTime = input.toLocalTime();
                            LocalTime ruleTime = LocalTime.parse(value, DateTimeFormatter.ofPattern("HH:mm"));
                            if (inputTime.isBefore(ruleTime)) {
                                validateError.setValidationStatus(ValidationStatus.FAIL);
                                validateError.setValidationRule(rule);
                                return validateError;
                            }
                        }
                        break;
                }

                switch (type) {
                    case "maxTime":
                        if (requestValue != null && !requestValue.trim().isEmpty()) {
                            LocalDateTime input = LocalDateTime.parse(requestValue, DateTimeFormatter.ofPattern(dateTimeFormat));
                            LocalTime inputTime = input.toLocalTime();
                            LocalTime ruleTime = LocalTime.parse(value, DateTimeFormatter.ofPattern("HH:mm"));
                            if (inputTime.isAfter(ruleTime)) {
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
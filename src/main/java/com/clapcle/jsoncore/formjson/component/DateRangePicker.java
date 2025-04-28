package com.clapcle.jsoncore.formjson.component;

import com.clapcle.jsoncore.formjson.form.Field;
import com.clapcle.jsoncore.formjson.form.ValidationRule;
import com.clapcle.jsoncore.formjson.jsonparser.ValidateError;
import com.clapcle.jsoncore.formjson.jsonparser.ValidationStatus;
import com.clapcle.jsoncore.formjson.util.FormValidationUtility;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DateRangePicker extends Field {

    private String dateFormat;
    private String startRange;
    private String endRange;

    @Override
    public ValidateError toValidate(Map<String, Object> data) throws Exception {


        ValidateError validateError = new ValidateError();
        Object requestValue = data.get(getId());

        Map<String, Object> dateRange = (Map<String, Object>) requestValue;

        String startDateStr = (String) dateRange.get("startDate");
        String endDateStr = (String) dateRange.get("endDate");

        if (startDateStr == null || startDateStr.trim().isEmpty()) {
            validateError.setValidationStatus(ValidationStatus.FAIL);
            validateError.setErrorMessage("Start Date is required.");
            return validateError;
        }

        if (endDateStr == null || endDateStr.trim().isEmpty()) {
            validateError.setValidationStatus(ValidationStatus.FAIL);
            validateError.setErrorMessage("End Date is required.");
            return validateError;
        }

        ValidateError maliciousCheck = super.checkMaliciousInput(requestValue);
        if (maliciousCheck != null) {
            return maliciousCheck;
        }

        String dateTimeFormat = (getDateFormat() != null && !getDateFormat().isEmpty()) ? getDateFormat() : "yyyy-MM-dd ";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormat);

        LocalDate startDate = (startDateStr != null && !startDateStr.isEmpty()) ? LocalDate.parse(startDateStr, formatter) : null;
        LocalDate endDate = (endDateStr != null && !endDateStr.isEmpty()) ? LocalDate.parse(endDateStr, formatter) : null;

        if (startDate != null && getStartRange() != null && !getStartRange().isEmpty()) {
            LocalDate configuredStartRange = LocalDate.parse(getStartRange(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (startDate.isBefore(configuredStartRange)) {
                validateError.setValidationStatus(ValidationStatus.FAIL);
                validateError.setErrorMessage("Start date must be after " + getStartRange());
                return validateError;
            }
        }

        if (endDate != null && getEndRange() != null && !getEndRange().isEmpty()) {
            LocalDate configuredEndRange = LocalDate.parse(getEndRange(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (endDate.isAfter(configuredEndRange)) {
                validateError.setValidationStatus(ValidationStatus.FAIL);
                validateError.setErrorMessage("End date must be before " + getEndRange());
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


        if (getValidationRules() != null) {
            for (ValidationRule rule : getValidationRules()) {
                String type = rule.getType();
                String value = (String) rule.getValue();

                switch (type) {

                    case "required":
                        if ("true".equalsIgnoreCase(value)) {
                            if (startDate == null || endDate == null) {
                                validateError.setValidationStatus(ValidationStatus.FAIL);
                                validateError.setValidationRule(rule);
                                return validateError;
                            }
                        }
                        break;

                    case "minDate":
                        if (startDate != null) {
                            LocalDate minDate = LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            if (startDate.isBefore(minDate)) {
                                validateError.setValidationStatus(ValidationStatus.FAIL);
                                validateError.setValidationRule(rule);
                                return validateError;
                            }
                        }
                        break;

                    case "maxDate":
                        if (endDate != null) {
                            LocalDate maxDate = LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            if (endDate.isAfter(maxDate)) {
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


      /*  Map<String, String> rangePickerValues = (Map<String, String>) data.get("vacationPeriod");
        if (rangePickerValues != null) {
            String startDateStr = rangePickerValues.get("startDate");
            String endDateStr = rangePickerValues.get("endDate");

            if (startDateStr != null && endDateStr != null) {
                LocalDate startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                LocalDate endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                if (getStartRange() != null) {
                    LocalDate startRangeDate = LocalDate.parse(getStartRange(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    if (startDate.isBefore(startRangeDate)) {
                        validateError.setValidationStatus(ValidationStatus.FAIL);
                        validateError.setErrorMessage("Start date cannot be earlier than the defined start range: " + startRange);
                        return validateError;
                    }
                }

                if (getEndRange() != null) {
                    LocalDate endRangeDate = LocalDate.parse(getEndRange(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    if (endDate.isAfter(endRangeDate)) {
                        validateError.setValidationStatus(ValidationStatus.FAIL);
                        validateError.setErrorMessage("End date cannot be later than the defined end range: " + endRange);
                        return validateError;
                    }
                }

                if (getValidationRules() != null) {
                    for (ValidationRule rule : getValidationRules()) {
                        String type = rule.getType();
                        String value = (String) rule.getValue();

                        switch (type) {
                            case "required":
                                if ("true".equalsIgnoreCase(value) && (startDateStr == null || startDateStr.trim().isEmpty() || endDateStr == null || endDateStr.trim().isEmpty())) {
                                    validateError.setValidationStatus(ValidationStatus.FAIL);
                                    validateError.setErrorMessage("Both startDate and endDate are required but not provided.");
                                    validateError.setValidationRule(rule);
                                    return validateError;
                                }
                                break;

                            case "minDate":
                                LocalDate minDate = LocalDate.parse(value);
                                if (startDate.isBefore(minDate) || endDate.isBefore(minDate)) {
                                    validateError.setValidationStatus(ValidationStatus.FAIL);
                                    validateError.setErrorMessage("Dates cannot be earlier than the minimum allowed date: " + minDate);
                                    validateError.setValidationRule(rule);
                                    return validateError;
                                }
                                break;
                            case "maxDate":
                                LocalDate maxDate = LocalDate.parse(value);
                                if (startDate.isAfter(maxDate) || endDate.isAfter(maxDate)) {
                                    validateError.setValidationStatus(ValidationStatus.FAIL);
                                    validateError.setErrorMessage("Dates cannot be later than the maximum allowed date: " + maxDate);
                                    validateError.setValidationRule(rule);
                                    return validateError;
                                }
                                break;
                        }
                    }
                }
            }
        }

        validateError.setValidationStatus(ValidationStatus.PASS);
        return validateError;*/
    }
}

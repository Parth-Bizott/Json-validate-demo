package com.clapcle.jsoncore.formjson.component;

import com.clapcle.jsoncore.formjson.component.subcomponent.DataSource;
import com.clapcle.jsoncore.formjson.form.Field;
import com.clapcle.jsoncore.formjson.form.ValidationRule;
import com.clapcle.jsoncore.formjson.jsonparser.ValidateError;
import com.clapcle.jsoncore.formjson.jsonparser.ValidationStatus;
import com.clapcle.jsoncore.formjson.util.FormValidationUtility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MultiSelectSearchField extends Field {
    private boolean selectAll;
    private boolean allowCreate;
    private String createLabel;
    private int maxSelections;
    private DataSource dataSource;
    private List<String> displayFields;

    @Override
    public ValidateError toValidate(Map<String, Object> data) throws Exception {

        ValidateError validateError = new ValidateError();
        Object requestValue = data.get(getId());
        List<String> selectedValues = null;
        if (requestValue instanceof List) {
            selectedValues = (List<String>) requestValue;
        } else if (requestValue instanceof String stringValue) {
            selectedValues = List.of(stringValue);
        }

        List<String> allData = getDataSource().getData();
        System.out.println("allData: " + allData);

        if (!allData.containsAll(selectedValues)) {
            validateError.setValidationStatus(ValidationStatus.FAIL);
            validateError.setErrorMessage("The provided value '" + requestValue + " was not available in parent list");
            return validateError;
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
                String value = rule.getValue().toString();
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

                switch (type) {
                    case "maxSelections":
                        int max = Integer.parseInt(value);
                        if (selectedValues != null && selectedValues.size() > max) {
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
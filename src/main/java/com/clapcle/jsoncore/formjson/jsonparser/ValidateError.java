package com.clapcle.jsoncore.formjson.jsonparser;

import com.clapcle.jsoncore.formjson.form.ValidationRule;
import lombok.Data;

@Data
public class ValidateError {
    private ValidationStatus validationStatus;
    private ValidationRule validationRule;
    private String errorMessage;

}

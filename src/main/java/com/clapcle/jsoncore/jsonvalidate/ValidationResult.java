package com.clapcle.jsoncore.jsonvalidate;

import lombok.Data;
import com.networknt.schema.ValidationMessage;
import java.util.*;

@Data
public class ValidationResult {
    private Map<String, List<String>> errors = new HashMap<>();
    private boolean valid = true;

    public void addErrors(String schemaName, Set<ValidationMessage> messages) {
        if (!messages.isEmpty()) {
            valid = false;
            errors.computeIfAbsent(schemaName, k -> new ArrayList<>())
                .addAll(messages.stream()
                    .map(ValidationMessage::getMessage)
                    .toList());
        }
    }
}
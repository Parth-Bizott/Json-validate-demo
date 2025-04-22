package com.clapcle.jsoncore.jsonvalidate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/validate")
public class ValidationController {

    private final JsonValidationService validationService;

    public ValidationController(JsonValidationService validationService) {
        this.validationService = validationService;
    }

    @PostMapping
    public ResponseEntity<ValidationResult> validateJson() throws IOException {
        ValidationResult result = validationService.validateJson();
        return ResponseEntity.ok(result);
    }
}
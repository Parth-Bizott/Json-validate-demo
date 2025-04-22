package com.clapcle.jsoncore.jsonvalidate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class JsonValidationService {

    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;
    private final Map<String, JsonSchema> schemaCache;

    public JsonValidationService(ResourceLoader resourceLoader, ObjectMapper objectMapper) {
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
        this.schemaCache = new HashMap<>();
        initializeSchemas();
    }

    private void initializeSchemas() {
        loadSchema("main-schema");
        loadSchema("layout-schema");
        loadSchema("step-schema");
        loadSchema("section-schema");
        loadSchema("field-schema");
        loadSchema("validation-schema");
        loadSchema("condition-schema");
    }

//    private static void loadSchema(String schemaName) {
//        try {
//            JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
//            final ObjectMapper objectMapper = new ObjectMapper();
//            JsonNode schemaNode = objectMapper.readTree(new FileInputStream("/home/darshit-bizott/Clapcle/json-core/src/main/resources/schemas/"+schemaName+".json"));
//            schemaCache.put(schemaName, factory.getSchema(schemaNode));
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to load schema: " + schemaName, e);
//        }
//    }

    private void loadSchema(String schemaName) {
        try {
            Resource resource = resourceLoader.getResource("classpath:schemas/" + schemaName + ".json");
            JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
            JsonNode schemaNode = objectMapper.readTree(resource.getInputStream());
            schemaCache.put(schemaName, factory.getSchema(schemaNode));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load schema: " + schemaName, e);
        }
    }

//    public static void main(String[] args) {
//        try {
//            initializeSchemas();
//            String jsonContent = Files.readString(Paths.get("/home/darshit-bizott/Downloads/finalJson (2).json"));
//            final ObjectMapper objectMapper = new ObjectMapper();
//            JsonNode jsonNode = objectMapper.readTree(jsonContent);
//            validateJson(jsonNode);
//            System.out.println(jsonNode.get("layout"));
//        } catch (IOException e) {
//            System.err.println("Error reading or parsing file: " + e.getMessage());
//        }
//    }

    public ValidationResult validateJson() throws IOException {

        String jsonContent = Files.readString(Paths.get("/home/darshit-bizott/Downloads/finalJson (2).json"));

        JsonNode jsonNode = objectMapper.readTree(jsonContent);

        ValidationResult result = new ValidationResult();

        // Validate against main schema
        Set<ValidationMessage> mainValidation = schemaCache.get("main-schema").validate(jsonNode);
        if (!mainValidation.isEmpty()) {
            result.addErrors("Main Schema", mainValidation);
        }

//        // Validate layout
//        if (true || jsonNode.has("layout")) {
//
//            Set<ValidationMessage> layoutValidation = schemaCache.get("layout-schema")
//                .validate(jsonNode.get("layout"));
//            if (!layoutValidation.isEmpty()) {
//                result.addErrors("Layout Schema", layoutValidation);
//            }
//        }
//
//        // Validate sections
////            if (true && (jsonNode.has("sections") || jsonNode.get("sections").isArray())) {
//        for (JsonNode section : jsonNode.get("sections")) {
//            Set<ValidationMessage> sectionValidation = schemaCache.get("section-schema")
//                .validate(section);
//            if (!sectionValidation.isEmpty()) {
//                result.addErrors("Section Schema", sectionValidation);
//            }
//
//            // Validate fields within sections
//            if (section.has("fields") && section.get("fields").isArray()) {
//                for (JsonNode field : section.get("fields")) {
//                    Set<ValidationMessage> fieldValidation = schemaCache.get("field-schema")
//                        .validate(field);
//                    if (!fieldValidation.isEmpty()) {
//                        result.addErrors("Field Schema", fieldValidation);
//                    }
//                }
//            }
//        }
//            }

        return result;
    }
}
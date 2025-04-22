//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.Data;
//
//import java.io.File;
//import java.util.*;
//
//public class FormValidationSystem {
//
//    // Core data structures remain mostly the same, but with simplified models
//    @Data
//    public static class FormDefinition {
//        private String formTitle;
//        private String formDescription;
//        private String submitButtonText;
//        private Layout layout;
//        private List<Section> sections;
//
//        // Field map for efficient lookup
//        private transient Map<String, Field> fieldMap;
//
//        public Map<String, Field> getFieldMap() {
//            if (fieldMap == null) {
//                fieldMap = new HashMap<>();
//                if (sections != null) {
//                    for (Section section : sections) {
//                        if (section.getFields() != null) {
//                            for (Field field : section.getFields()) {
//                                fieldMap.put(field.getId(), field);
//                            }
//                        }
//                    }
//                }
//            }
//            return fieldMap;
//        }
//
//        public Field getField(String fieldId) {
//            return getFieldMap().get(fieldId);
//        }
//    }
//
//    @Data
//    public static class Layout {
//        private Integer columnCount;
//        private String cssClass;
//        private List<LayoutOption> layoutOptions;
//        // Other properties from JSON will be ignored
//    }
//
//    @Data
//    public static class LayoutOption {
//        private String type;
//        private List<Step> steps;
//    }
//
//    @Data
//    public static class Step {
//        private String id;
//        private String title;
//        private String description;
//        private Integer order;
//        private List<String> sectionId;
//    }
//
//    @Data
//    public static class Section {
//        private String id;
//        private String title;
//        private String description;
//        private Integer order;
//        private Layout layout;
//        private Object visibility;  // Using Object since this is complex
//        private Object editability; // Using Object since this is complex
//        private List<Field> fields;
//    }
//
//    @Data
//    public static class Field {
//        private String id;
//        private String name;
//        private String label;
//        private String type;
//        private String defaultValue;
//        private String description;
//        private Boolean hidden;
//        private Boolean disabled;
//        private String placeholder;
//        private String formatter;
//        private String dependsOn;  // Key field for inter-field dependencies
//        private String readOnlyDisplay;
//        private String helperText;
//        private String cssClass;
//        private Layout layout;
//        private List<Validation> validations;
//        private Object conditions;     // Using Object for complex types
//        private Object visibility;     // Using Object for complex types
//        private Object editability;    // Using Object for complex types
//        private Object conditionalDisplay; // Using Object for complex types
//    }
//
//    @Data
//    public static class Validation {
//        private String type;
//        private String value;
//        private String message;
//    }
//
//    /**
//     * Main validator class specifically focusing on dependsOn relationships
//     */
//    public static class FormDependencyValidator {
//        private final FormDefinition formDefinition;
//
//        public FormDependencyValidator(FormDefinition formDefinition) {
//            this.formDefinition = formDefinition;
//        }
//
//        /**
//         * Validate form data with special attention to dependencies
//         */
//        public ValidationResult validateFormData(Map<String, Object> formData) {
//            ValidationResult result = new ValidationResult();
//
//            // Build dependency graph
//            Map<String, Set<String>> dependencyGraph = buildDependencyGraph();
//
//            // Check for cyclic dependencies
//            if (hasCyclicDependencies(dependencyGraph)) {
//                result.addGlobalError("Form contains cyclic dependencies which cannot be validated");
//                return result;
//            }
//
//            // Get a topologically sorted list of fields (dependency order)
//            List<String> validationOrder = topologicalSort(dependencyGraph);
//
//            // Validate fields in proper dependency order
//            for (String fieldId : validationOrder) {
//                if (formData.containsKey(fieldId)) {
//                    validateField(fieldId, formData.get(fieldId), formData, result);
//                }
//            }
//
//            return result;
//        }
//
//        /**
//         * Build dependency graph for all fields
//         */
//        private Map<String, Set<String>> buildDependencyGraph() {
//            Map<String, Set<String>> graph = new HashMap<>();
//
//            // Initialize graph with all fields
//            formDefinition.getFieldMap().keySet().forEach(fieldId -> {
//                graph.put(fieldId, new HashSet<>());
//            });
//
//            // Add dependency edges
//            for (Field field : formDefinition.getFieldMap().values()) {
//                if (field.getDependsOn() != null && !field.getDependsOn().isEmpty()) {
//                    String dependsOnField = field.getDependsOn();
//                    if (graph.containsKey(dependsOnField)) {
//                        // This field depends on dependsOnField
//                        graph.get(dependsOnField).add(field.getId());
//                    }
//                }
//            }
//
//            return graph;
//        }
//
//        /**
//         * Detect cyclic dependencies in the form definition
//         */
//        private boolean hasCyclicDependencies(Map<String, Set<String>> graph) {
//            Set<String> visited = new HashSet<>();
//            Set<String> recursionStack = new HashSet<>();
//
//            for (String node : graph.keySet()) {
//                if (hasCyclicDependenciesDFS(node, graph, visited, recursionStack)) {
//                    return true;
//                }
//            }
//
//            return false;
//        }
//
//        private boolean hasCyclicDependenciesDFS(String node, Map<String, Set<String>> graph,
//                                                 Set<String> visited, Set<String> recursionStack) {
//            // If node is not visited, mark it visited
//            if (!visited.contains(node)) {
//                visited.add(node);
//                recursionStack.add(node);
//
//                // Recur for all neighbors
//                for (String neighbor : graph.get(node)) {
//                    if (!visited.contains(neighbor) &&
//                            hasCyclicDependenciesDFS(neighbor, graph, visited, recursionStack)) {
//                        return true;
//                    } else if (recursionStack.contains(neighbor)) {
//                        return true;
//                    }
//                }
//            }
//
//            recursionStack.remove(node);
//            return false;
//        }
//
//        /**
//         * Sort fields in topological order (dependency order)
//         */
//        private List<String> topologicalSort(Map<String, Set<String>> graph) {
//            List<String> result = new ArrayList<>();
//            Set<String> visited = new HashSet<>();
//
//            // Create a reverse graph for easier processing
//            // In the reversed graph, edges go from dependent field to dependency
//            Map<String, Set<String>> reverseGraph = new HashMap<>();
//            graph.keySet().forEach(node -> reverseGraph.put(node, new HashSet<>()));
//
//            for (String node : graph.keySet()) {
//                for (String dependent : graph.get(node)) {
//                    reverseGraph.get(dependent).add(node);
//                }
//            }
//
//            // Process all nodes
//            for (String node : reverseGraph.keySet()) {
//                if (!visited.contains(node)) {
//                    topologicalSortDFS(node, reverseGraph, visited, result);
//                }
//            }
//
//            return result;
//        }
//
//        private void topologicalSortDFS(String node, Map<String, Set<String>> graph,
//                                        Set<String> visited, List<String> result) {
//            visited.add(node);
//
//            // Process all dependencies first
//            for (String dependency : graph.get(node)) {
//                if (!visited.contains(dependency)) {
//                    topologicalSortDFS(dependency, graph, visited, result);
//                }
//            }
//
//            // Add current node
//            result.add(node);
//        }
//
//        /**
//         * Validate a single field, considering its dependencies
//         */
//        private void validateField(String fieldId, Object value, Map<String, Object> formData, ValidationResult result) {
//            Field field = formDefinition.getField(fieldId);
//            if (field == null) {
//                result.addFieldError(fieldId, "Unknown field");
//                return;
//            }
//
//            // Check if dependency fields exist and are valid
//            if (field.getDependsOn() != null && !field.getDependsOn().isEmpty()) {
//                String dependsOnFieldId = field.getDependsOn();
//                Field dependsOnField = formDefinition.getField(dependsOnFieldId);
//
//                if (dependsOnField == null) {
//                    result.addFieldError(fieldId, "Depends on undefined field: " + dependsOnFieldId);
//                    return;
//                }
//
//                // Check if dependency field has a value in the data
//                if (!formData.containsKey(dependsOnFieldId)) {
//                    result.addFieldError(fieldId, "Required dependency field '" + dependsOnFieldId + "' is missing");
//                    return;
//                }
//
//                // Here you can implement specific dependency validation rules
//                // For example, validate that the value of this field is consistent with its dependency
//                validateDependencyRelationship(field, dependsOnField, value, formData.get(dependsOnFieldId), result);
//            }
//
//            // Apply field validations
//            applyFieldValidations(field, value, result);
//        }
//
//        /**
//         * Validate the relationship between a field and its dependency
//         */
//        private void validateDependencyRelationship(Field field, Field dependsOnField,
//                                                    Object fieldValue, Object dependsOnValue, ValidationResult result) {
//            // This is where you'd implement specific validation logic based on the types of fields
//            // For example:
//
//            // 1. If dependsOn is a country field and this is a currency field, validate currency format
//            if ("COUNTRY".equalsIgnoreCase(dependsOnField.getType()) && "CURRENCY".equalsIgnoreCase(field.getType())) {
//                validateCurrencyForCountry(field.getId(), fieldValue, dependsOnValue, result);
//            }
//
//            // 2. If dependsOn is a state field and this is an address field, validate address format
//            else if ("STATE".equalsIgnoreCase(dependsOnField.getType()) && "TEXT".equalsIgnoreCase(field.getType())
//                    && "personName".equals(field.getId())) {
//                validateNameForState(field.getId(), fieldValue, dependsOnValue, result);
//            }
//
//            // Add more specific validation cases as needed
//        }
//
//        /**
//         * Example currency validation based on country
//         */
//        private void validateCurrencyForCountry(String fieldId, Object value, Object country, ValidationResult result) {
//            if (value == null || country == null) return;
//
//            String currencyStr = value.toString().trim();
//            String countryStr = country.toString();
//
//            // Example validation: India requires more than ₹10,000
//            if ("India".equalsIgnoreCase(countryStr)) {
//                try {
//                    double amount = Double.parseDouble(currencyStr.replaceAll("[^0-9.]", ""));
//                    if (amount < 10000) {
//                        result.addFieldError(fieldId, "Salary must be at least ₹10,000 for India");
//                    }
//                } catch (NumberFormatException e) {
//                    result.addFieldError(fieldId, "Invalid currency format");
//                }
//            }
//
//            // Add more country-specific validations as needed
//        }
//
//        /**
//         * Example name validation based on state
//         */
//        private void validateNameForState(String fieldId, Object value, Object state, ValidationResult result) {
//            if (value == null || state == null) return;
//
//            String name = value.toString().trim();
//            String stateStr = state.toString();
//
//            // Example validation: California requires full name (first and last)
//            if ("California".equalsIgnoreCase(stateStr)) {
//                if (!name.contains(" ")) {
//                    result.addFieldError(fieldId, "Full name (first and last) is required for California residents");
//                }
//            }
//
//            // Add more state-specific validations as needed
//        }
//
//        /**
//         * Apply basic validations defined for a field
//         */
//        private void applyFieldValidations(Field field, Object value, ValidationResult result) {
//            if (field.getValidations() == null) {
//                return;
//            }
//
//            String strValue = value != null ? value.toString() : "";
//
//            for (Validation validation : field.getValidations()) {
//                String type = validation.getType();
//                String validationValue = validation.getValue();
//                String message = validation.getMessage();
//
//                switch (type) {
//                    case "required":
//                        if (Boolean.parseBoolean(validationValue) && (value == null || strValue.isEmpty())) {
//                            result.addFieldError(field.getId(), message);
//                        }
//                        break;
//
//                    case "minLength":
//                        if (value != null && strValue.length() < Integer.parseInt(validationValue)) {
//                            result.addFieldError(field.getId(), message);
//                        }
//                        break;
//
//                    case "maxLength":
//                        if (value != null && strValue.length() > Integer.parseInt(validationValue)) {
//                            result.addFieldError(field.getId(), message);
//                        }
//                        break;
//
//                    case "pattern":
//                        if (value != null && !strValue.matches(validationValue)) {
//                            result.addFieldError(field.getId(), message);
//                        }
//                        break;
//
//                    case "minValue":
//                        try {
//                            if (value != null) {
//                                double numValue = Double.parseDouble(strValue.replaceAll("[^0-9.]", ""));
//                                double minValue = Double.parseDouble(validationValue);
//                                if (numValue < minValue) {
//                                    result.addFieldError(field.getId(), message);
//                                }
//                            }
//                        } catch (NumberFormatException e) {
//                            result.addFieldError(field.getId(), "Not a valid number");
//                        }
//                        break;
//
//                    case "maxValue":
//                        try {
//                            if (value != null) {
//                                double numValue = Double.parseDouble(strValue.replaceAll("[^0-9.]", ""));
//                                double maxValue = Double.parseDouble(validationValue);
//                                if (numValue > maxValue) {
//                                    result.addFieldError(field.getId(), message);
//                                }
//                            }
//                        } catch (NumberFormatException e) {
//                            result.addFieldError(field.getId(), "Not a valid number");
//                        }
//                        break;
//                }
//            }
//        }
//    }
//
//    /**
//     * Class to hold validation results
//     */
//    @Data
//    public static class ValidationResult {
//        private boolean valid = true;
//        private List<String> globalErrors = new ArrayList<>();
//        private Map<String, List<String>> fieldErrors = new HashMap<>();
//
//        public void addGlobalError(String errorMessage) {
//            valid = false;
//            globalErrors.add(errorMessage);
//        }
//
//        public void addFieldError(String fieldId, String errorMessage) {
//            valid = false;
//            fieldErrors.computeIfAbsent(fieldId, k -> new ArrayList<>()).add(errorMessage);
//        }
//    }
//
//    /**
//     * Example usage
//     */
//    public static void main(String[] args) {
//        try {
//            // Load form definition
//            ObjectMapper mapper = new ObjectMapper();
//            // Configure mapper to ignore unknown properties
//            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
//            FormDefinition formDefinition = mapper.readValue(
//                    new File("/home/darshit-bizott/Downloads/finalJson (2).json"), FormDefinition.class);
//
//            // Create validator
//            FormDependencyValidator validator = new FormDependencyValidator(formDefinition);
//
//            // Example form data to validate
//            Map<String, Object> formData = new HashMap<>();
//            formData.put("personName", "John");
//            formData.put("expectedSalary", "500");
//            formData.put("country", "United States of America");
//            formData.put("state", "California"); // Added to test dependency validation
//
//            // Validate
//            ValidationResult result = validator.validateFormData(formData);
//
//            // Print results
//            System.out.println("Validation passed: " + result.isValid());
//
//            if (!result.getGlobalErrors().isEmpty()) {
//                System.out.println("Global Errors:");
//                result.getGlobalErrors().forEach(error -> System.out.println("  - " + error));
//            }
//
//            if (!result.getFieldErrors().isEmpty()) {
//                System.out.println("Field Errors:");
//                result.getFieldErrors().forEach((fieldId, errors) -> {
//                    System.out.println("Field: " + fieldId);
//                    errors.forEach(error -> System.out.println("  - " + error));
//                });
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
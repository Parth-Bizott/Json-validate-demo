package com.clapcle.jsoncore.formjson.jsonparser;

import com.clapcle.jsoncore.formjson.form.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormJsonParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Parse a JSON string into a FormDefinition object
     *
     * @param json The JSON string to parse
     * @return A FormDefinition object
     * @throws IOException If there's an error parsing the JSON
     */
    public static Form parseForm(String json) throws IOException {
        JsonNode rootNode = objectMapper.readTree(json);
        Form form = new Form();

        // Parse basic form properties
        form.setFormTitle(rootNode.get("formTitle").asText());
        form.setFormDescription(rootNode.get("formDescription").asText());
        form.setSubmitButtonText(rootNode.get("submitButtonText").asText());

        // Parse layout
        form.setLayout(parseLayout(rootNode.get("layout")));

        // Parse sections
        form.setSections(parseSections(rootNode.get("sections")));

        return form;
    }

    private static Layout parseLayout(JsonNode layoutNode) {
        Layout layout = new Layout();
        layout.setColumnCount(layoutNode.get("columnCount").asInt());

        // Parse layout options
        List<LayoutOption> layoutOptions = new ArrayList<>();
        JsonNode optionsNode = layoutNode.get("layoutOptions");
        for (JsonNode optionNode : optionsNode) {
            LayoutOption option = new LayoutOption();
            option.setType(optionNode.get("type").asText());

            // Parse steps
            List<Step> steps = new ArrayList<>();
            for (JsonNode stepNode : optionNode.get("steps")) {
                Step step = new Step();
                step.setId(stepNode.get("id").asText());
                step.setTitle(stepNode.get("title").asText());
                step.setDescription(stepNode.get("description").asText());
                step.setOrder(stepNode.get("order").asInt());

                // Parse section IDs
                List<String> sectionIds = new ArrayList<>();
                for (JsonNode sectionIdNode : stepNode.get("sectionId")) {
                    sectionIds.add(sectionIdNode.asText());
                }
                step.setSectionId(sectionIds);

                steps.add(step);
            }
            option.setSteps(steps);
            layoutOptions.add(option);
        }
        layout.setLayoutOptions(layoutOptions);

        return layout;
    }

    private static List<Section> parseSections(JsonNode sectionsNode) {
        List<Section> sections = new ArrayList<>();

        for (JsonNode sectionNode : sectionsNode) {
            Section section = new Section();
            section.setId(sectionNode.get("id").asText());
            section.setTitle(sectionNode.get("title").asText());
            section.setDescription(sectionNode.get("description").asText());
            section.setOrder(sectionNode.get("order").asInt());

            // Parse visibility
            if (sectionNode.has("visibility")) {
                section.setVisibility(sectionNode.get("visibility").asText());
            }

            // Parse editability
            if (sectionNode.has("editability")) {
                section.setEditability(sectionNode.get("editability").asText());
            }

            List<Field> fields = new ArrayList<>();
            try {
                fields = objectMapper.treeToValue(sectionNode.get("fields"), new TypeReference<List<Field>>() {
                });
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            section.setFields(fields);
            sections.add(section);
        }

        return sections;
    }


    public static void main(String[] args) {
        try {

            String jsonContent = Files.readString(Paths.get("/home/darshit-bizott/WorkSpace/Clapcle/Json-validate-demo/src/main/resources/FE-JSON/finaljson.json"));
            Form form = parseForm(jsonContent);
//
////            JsonNode jsonNode = objectMapper.readTree(jsonContent);
//            List<Field> fields = objectMapper.readValue(jsonContent,  new TypeReference<List<Field>>(){});

//            System.out.println("Successfully parsed form: " + ((TextField)textField).getFormatter());
            System.out.println("Successfully parsed form: " + form.getFormDescription());

            List<Map<String,Object>> addressHistoryList = new ArrayList<>();
            Map<String,Object> addressHistory = new HashMap<>();
            addressHistory.put("address1", "123");
            addressHistoryList.add(addressHistory);

            Map<String,Map<String,Object>> dataMap = new HashMap<>();
            dataMap.put("personalInfo", new HashMap<>());
            dataMap.get("personalInfo").put("personName", "dd");
            dataMap.get("personalInfo").put("expectedSalary", "20");
            dataMap.get("personalInfo").put("professionalSummary", "this take");
            dataMap.get("personalInfo").put("degreeLevel", "high_school");
            dataMap.get("personalInfo").put("companySelection", "1");
            dataMap.get("personalInfo").put("addressHistory", addressHistoryList);



            Map<String,Object> finalDataMap = new HashMap<>();

            dataMap.forEach((k,v) -> {
                finalDataMap.putAll(v);
            });

            Map<String, Map<String, ValidateError>> errorMap = form.validate(dataMap, finalDataMap);

            System.out.println("validate field " + errorMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
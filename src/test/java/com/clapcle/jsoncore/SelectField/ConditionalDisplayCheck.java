package com.clapcle.jsoncore.SelectField;

import com.clapcle.jsoncore.formjson.component.subcomponent.ApiDataSource;
import com.clapcle.jsoncore.formjson.form.Form;
import com.clapcle.jsoncore.formjson.jsonparser.ValidateError;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static com.clapcle.jsoncore.formjson.jsonparser.FormJsonParser.parseForm;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConditionalDisplayCheck {

    @Mock
    private ApiDataSource apiDataSource;

//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private RestTemplate restTemplate;


    @Test
    void checkConditionalDisplay_metWithCondition() throws Exception {

        String jsonContent = Files.readString(Paths.get(ClassLoader.getSystemResource("SelectField/Samplejson/conditionalDisplay_sample.json").toURI()));
        Form form = parseForm(jsonContent);

        String dataContent = Files.readString(Paths.get(ClassLoader.getSystemResource("SelectField/dataJson/conditionalDisplay_data.json").toURI()));
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Map<String, String>> dataMap = objectMapper.readValue(dataContent, new TypeReference<Map<String, Map<String, String>>>() {
        });

//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
//
//        Map<String, String> item = Map.of("valueKey", "ExpectedValue");
//        ResponseBean responseBean = new ResponseBean();
//        responseBean.setRData(List.of(item));
//
//        ResponseEntity<ResponseBean> responseEntity = new ResponseEntity<>(responseBean, HttpStatus.OK);
//        RestTemplate restTemplate1 = Mockito.mock(RestTemplate.class);
//        Mockito.when(restTemplate1.exchange(
//                Mockito.eq("http://localhost:9999" + "/trial/get-module-master"),
//                Mockito.eq(HttpMethod.GET),
//                Mockito.eq(requestEntity),
//                Mockito.eq(ResponseBean.class)
//        )).thenReturn(responseEntity);


        Map<String, Object> finalDataMap = new HashMap<>();

        dataMap.forEach((k, v) -> {
            finalDataMap.putAll(v);
        });

        // Mock the RestTemplate response
//        Map<String, String> item = Map.of("valueKey", "ExpectedValue");
//        ResponseBean responseBean = new ResponseBean();
//        responseBean.setRData(List.of(item));
//        // Set up responseBean with test data
//
//        ResponseEntity<ResponseBean> mockResponse =
//                new ResponseEntity<>(responseBean, HttpStatus.OK);
//
//        when(restTemplate.exchange(
//                anyString(),
//                any(HttpMethod.class),
//                any(HttpEntity.class),
//                eq(ResponseBean.class)
//        )).thenReturn(mockResponse);
//
//        // Create and test ApiDataSource
//        ApiDataSource apiDataSource = new ApiDataSource();
//        apiDataSource.setEndpoint("/api/data");
//        apiDataSource.setMethod("GET");
//
//        ResponseMapping mapping = new ResponseMapping();
//        mapping.setValue("name");  // assuming this is the field you want to map
//        apiDataSource.setResponseMapping(mapping);
//
//        List<String> result = apiDataSource.getData();


//        List<String> expectedData = Arrays.asList("data1", "data2", "data3");
//        when(apiDataSource1.getData()).thenReturn(expectedData);


        Map<String, Map<String, ValidateError>> errorMap = form.validate(dataMap, finalDataMap);

        Map<String, ValidateError> personalInfo = errorMap.get("personalInfo");

        assertEquals("FAIL", personalInfo.get("degreeLevel").getValidationStatus().toString());

        assertEquals("PASS", personalInfo.get("expectedSalary").getValidationStatus().toString());
    }

    @Test
    void checkConditionalDisplay_metWithConditionSuccess() throws IOException, URISyntaxException {

        String jsonContent = Files.readString(Paths.get(ClassLoader.getSystemResource("SelectField/Samplejson/conditionalDisplay_sample.json").toURI()));
        Form form = parseForm(jsonContent);

        String dataContent = Files.readString(Paths.get(ClassLoader.getSystemResource("SelectField/dataJson/conditionalDisplay_data_success.json").toURI()));
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Map<String, String>> dataMap = objectMapper.readValue(dataContent, new TypeReference<Map<String, Map<String, String>>>() {
        });

        Map<String, Object> finalDataMap = new HashMap<>();

        dataMap.forEach((k, v) -> {
            finalDataMap.putAll(v);
        });

        Map<String, Map<String, ValidateError>> errorMap = form.validate(dataMap, finalDataMap);

        Map<String, ValidateError> personalInfo = errorMap.get("personalInfo");

        assertEquals("PASS", personalInfo.get("degreeLevel").getValidationStatus().toString());

        assertEquals("PASS", personalInfo.get("expectedSalary").getValidationStatus().toString());
    }
}

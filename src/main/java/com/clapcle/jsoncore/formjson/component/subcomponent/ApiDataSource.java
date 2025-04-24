package com.clapcle.jsoncore.formjson.component.subcomponent;

import com.clapcle.jsoncore.formjson.util.ResponseBean;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiDataSource extends DataSource {
    private String endpoint;
    private String method;
    private String searchParam;
    private int minChars;
    private int debounceMs;
    private ResponseMapping responseMapping;

    public ApiDataSource() {
        this.type = "API";
    }

    @Override
    public List<String> getData() throws Exception {
        String host = "http://localhost:9999";

        // Setup headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the request
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        // Call the endpoint
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ResponseBean> response = new ResponseEntity<>(HttpStatus.OK);
        if (method.equalsIgnoreCase("POST")) {
            response = restTemplate.exchange(
                    host + endpoint,
                    HttpMethod.GET,
                    requestEntity,
                    ResponseBean.class
            );
        } else if (method.equalsIgnoreCase("GET")) {
            response = restTemplate.exchange(
                    host + endpoint,
                    HttpMethod.GET,
                    requestEntity,
                    ResponseBean.class
            );
        }

        // Check if request failed (Note: original condition was reversed)
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new Exception("Error in REST call: " + response.getStatusCode());
        }

        // Extract and map the data
        List<Object> result = (List<Object>) response.getBody().getRData(); // Be cautious with unchecked casts
        List<String> data = new ArrayList<>();

        if (result != null) {
            for (Object o : result) {
                if (o instanceof Map<?, ?> map) {
                    Object mappedValue = map.get(responseMapping.getValue());
                    data.add(mappedValue.toString());
                }
            }
        }

        return data;
    }

}
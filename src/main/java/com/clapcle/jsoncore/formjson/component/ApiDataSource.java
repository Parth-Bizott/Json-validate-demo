package com.clapcle.jsoncore.formjson.component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

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
        this.type = "api";
    }
}
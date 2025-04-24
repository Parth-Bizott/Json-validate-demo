package com.clapcle.jsoncore.formjson.component.subcomponent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StaticOption {
    private String value;
    private String label;
    private String description;
}
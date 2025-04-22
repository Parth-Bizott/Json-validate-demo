package com.clapcle.jsoncore.formjson.component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Option {
    private String value;
    private String label;
    private String description;
    private String icon;
}
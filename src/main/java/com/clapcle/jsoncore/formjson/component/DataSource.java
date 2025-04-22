package com.clapcle.jsoncore.formjson.component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class DataSource {
    protected String type;
    protected List<String> dependsOn;
}
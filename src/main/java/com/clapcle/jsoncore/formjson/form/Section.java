package com.clapcle.jsoncore.formjson.form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Section {
    private String id;
    private String title;
    private String description;
    private int order;
    private String visibility;
    private String editability;
    private List<? extends Field> fields;
}
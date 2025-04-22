
package com.clapcle.jsoncore.formjson.form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Step {
    private String id;
    private String title;
    private String description;
    private int order;
    private List<String> sectionId;
}
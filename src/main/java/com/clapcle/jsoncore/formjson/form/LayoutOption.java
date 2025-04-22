
package com.clapcle.jsoncore.formjson.form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LayoutOption {
    private String type;
    private List<Step> steps;
}
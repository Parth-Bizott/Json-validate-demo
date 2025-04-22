package com.clapcle.jsoncore.formjson.form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Layout {
    private int columnCount;
    private List<LayoutOption> layoutOptions;
}
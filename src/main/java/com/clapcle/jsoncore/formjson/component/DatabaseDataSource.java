package com.clapcle.jsoncore.formjson.component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DatabaseDataSource extends DataSource {
    private String entity;
    private List<String> searchFields;
    private String displayTemplate;
    private String valueField;
    private String orderBy;

    public DatabaseDataSource() {
        this.type = "database";
    }

    @Override
    public List<String> getData() {
        return List.of();
    }
}
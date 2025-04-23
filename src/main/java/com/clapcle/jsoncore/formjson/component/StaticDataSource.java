package com.clapcle.jsoncore.formjson.component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StaticDataSource extends DataSource {
    private List<StaticOption> data;

    public StaticDataSource() {
        this.type = "STATIC";
    }

    @Override
    public List<String> getData() {
        if (data != null) {
            return data.stream().map(StaticOption::getValue).collect(Collectors.toList());
        }
        return List.of();
    }

}


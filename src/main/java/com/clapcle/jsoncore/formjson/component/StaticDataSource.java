package com.clapcle.jsoncore.formjson.component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StaticDataSource extends DataSource {
    private List<StaticOption> data;

    public StaticDataSource() {
        this.type = "static";
    }

}


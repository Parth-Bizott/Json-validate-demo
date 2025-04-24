package com.clapcle.jsoncore.formjson.component.subcomponent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import java.util.List;

@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ApiDataSource.class, name = "API"),
        @JsonSubTypes.Type(value = DatabaseDataSource.class, name = "DATABASE"),
        @JsonSubTypes.Type(value = StaticDataSource.class, name = "STATIC")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class DataSource {
    protected String type;
    protected List<String> dependsOn;

    public abstract List<String> getData() throws Exception;
}
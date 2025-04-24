package com.clapcle.jsoncore.formjson.factory;

import com.clapcle.jsoncore.formjson.component.subcomponent.ApiDataSource;
import com.clapcle.jsoncore.formjson.component.subcomponent.DataSource;
import com.clapcle.jsoncore.formjson.component.subcomponent.DatabaseDataSource;
import com.clapcle.jsoncore.formjson.component.subcomponent.StaticDataSource;

public class DataSourceFactory {
    public static DataSource create(String type) {
        return switch (type) {
            case "static" -> new StaticDataSource();
            case "api" -> new ApiDataSource();
            case "database" -> new DatabaseDataSource();
            default -> throw new IllegalArgumentException("Unknown dataSource type: " + type);
        };
    }
}
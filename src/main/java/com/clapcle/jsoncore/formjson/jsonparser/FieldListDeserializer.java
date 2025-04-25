package com.clapcle.jsoncore.formjson.jsonparser;

import com.clapcle.jsoncore.formjson.form.Field;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.List;

public class FieldListDeserializer extends JsonDeserializer<List<Field>> {
    @Override
    public List<Field> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        return jp.readValueAs(new TypeReference<List<Field>>() {});
    }
}
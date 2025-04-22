package com.clapcle.jsoncore.formjson.component;

import com.clapcle.jsoncore.formjson.form.Field;
import com.clapcle.jsoncore.formjson.jsonparser.ValidateError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileField extends Field {
    private List<String> allowedFileTypes;
    private long maxFileSize; // in bytes
    private int maxNumberOfFiles;
    private boolean dragDrop;
    private boolean showPreview;
    private boolean allowDelete;

    @Override
    public ValidateError toValidate(Map<String, String> data) {
        return null;
    }
}

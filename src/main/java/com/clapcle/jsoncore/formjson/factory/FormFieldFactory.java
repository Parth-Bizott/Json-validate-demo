package com.clapcle.jsoncore.formjson.factory;

import com.clapcle.jsoncore.formjson.form.Field;
import com.clapcle.jsoncore.formjson.component.CurrencyField;
import com.clapcle.jsoncore.formjson.component.TextAreaField;
import com.clapcle.jsoncore.formjson.component.TextField;

public class FormFieldFactory {
    public static Field createField(String type) {
        switch (type) {
            case "TEXT":
                return new TextField();
            case "TEXTAREA":
                return new TextAreaField();
            case "CURRENCY":
                return new CurrencyField();
            // Add other field types as needed
            default:
                throw new IllegalArgumentException("Unsupported field type: " + type);
        }
    }
}
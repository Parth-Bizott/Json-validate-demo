package com.clapcle.jsoncore.formjson.util;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptException;
import java.util.Map;

public class FormValidationUtility {

    public static  boolean validateFormula(Map<String, Object> data, String formula) {

        if (formula != null) {
            try {
                return Boolean.parseBoolean(validateCondition(formula, data));
            } catch (ScriptException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static String validateCondition(String formula, Map<String, Object> data) throws ScriptException {
        var engine = new javax.script.ScriptEngineManager().getEngineByName("JavaScript");
        Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
        bindings.putAll(data);
        Object result = engine.eval(formula);
        return result.toString();
    }
}

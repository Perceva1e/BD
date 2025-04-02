package model;

import java.util.LinkedHashMap;
import java.util.Map;

public class DynamicEntity {
    private final String name;
    private final Map<String, String> fields;

    public DynamicEntity(String name) {
        this.name = name;
        this.fields = new LinkedHashMap<>();
        fields.put("ID", "SERIAL PRIMARY KEY");
    }

    public void addField(String fieldName, String fieldType) {
        fields.put(fieldName.toUpperCase(), fieldType);
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    @Override
    public String toString() {
        return name; // Возвращаем имя сущности
    }
}
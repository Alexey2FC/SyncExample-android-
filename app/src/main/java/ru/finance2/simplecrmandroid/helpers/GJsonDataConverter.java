package ru.finance2.simplecrmandroid.helpers;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

public class GJsonDataConverter implements TypeDataConverter<JsonObject> {

    private static GJsonDataConverter instance;
    public static GJsonDataConverter getInstance() {
        if( instance == null ) {
            instance = new GJsonDataConverter();
        }
        return instance;
    }

    private boolean notExistOrNull(JsonObject jsonObject, String columnName) {
        return (!jsonObject.has(columnName)) || (jsonObject.get(columnName) == JsonNull.INSTANCE );
    }

    @Override
    public Integer getInteger(JsonObject jObject, String columnName, Integer defaultValue) {
        if( notExistOrNull(jObject, columnName) ) return defaultValue;
        try {
            return jObject.get(columnName).getAsInt();
        } catch (Exception e) { e.printStackTrace(); }
        return defaultValue;
    }

    @Override
    public Long getLong(JsonObject jObject, String columnName, Long defaultValue) {
        if( notExistOrNull(jObject, columnName) ) return defaultValue;
        try {
            return jObject.get(columnName).getAsLong();
        } catch (Exception e) { e.printStackTrace(); }
        return defaultValue;
    }

    @Override
    public Double getDouble(JsonObject jObject, String columnName, Double defaultValue) {
        if( notExistOrNull(jObject, columnName) ) return defaultValue;
        try {
            return jObject.get(columnName).getAsDouble();
        } catch (Exception e) { e.printStackTrace(); }
        return defaultValue;
    }

    @Override
    public String getString(JsonObject jObject, String columnName, String defaultValue) {
        if( notExistOrNull(jObject, columnName) ) return defaultValue;
        try {
            return jObject.get(columnName).getAsString();
        } catch (Exception e) { e.printStackTrace(); }
        return defaultValue;
    }
}

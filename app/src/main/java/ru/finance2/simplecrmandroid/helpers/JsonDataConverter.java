package ru.finance2.simplecrmandroid.helpers;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonDataConverter implements TypeDataConverter<JSONObject> {

    private static JsonDataConverter instance;
    public static JsonDataConverter getInstance() {
        if( instance == null ) {
            instance = new JsonDataConverter();
        }
        return instance;
    }

    @Override
    public Integer getInteger(JSONObject jObject, String columnName, Integer defaultValue) {
        if( jObject.isNull(columnName) ) {
            return defaultValue;
        }
        try {
            return jObject.getInt(columnName);
        } catch (JSONException e) {e.printStackTrace();}
        return defaultValue;
    }

    @Override
    public Long getLong(JSONObject jObject, String columnName, Long defaultValue) {
        if( jObject.isNull(columnName) ) {
            return defaultValue;
        }
        try {
            return jObject.getLong(columnName);
        } catch (JSONException e) {e.printStackTrace();}
        return defaultValue;
    }

    @Override
    public Double getDouble(JSONObject jObject, String columnName, Double defaultValue) {
        if( jObject.isNull(columnName) ) {
            return defaultValue;
        }
        try {
            return jObject.getDouble(columnName);
        } catch (JSONException e) {e.printStackTrace();}
        return defaultValue;
    }

    @Override
    public String getString(JSONObject jObject, String columnName, String defaultValue) {
        if( jObject.isNull(columnName) ) {
            return defaultValue;
        }
        try {
            return jObject.getString(columnName);
        } catch (JSONException e) {e.printStackTrace();}
        return defaultValue;
    }
}

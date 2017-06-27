package ru.finance2.simplecrmandroid.helpers;

public interface TypeDataConverter<T> {
    public Integer getInteger(T source, String columnName, Integer defaultValue);
    public Long getLong(T source, String columnName, Long defaultValue);
    public Double getDouble(T source, String columnName, Double defaultValue);
    public String getString(T source, String columnName, String defaultValue);
}

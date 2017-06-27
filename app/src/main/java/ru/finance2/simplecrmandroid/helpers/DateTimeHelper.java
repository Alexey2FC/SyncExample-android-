package ru.finance2.simplecrmandroid.helpers;

public class DateTimeHelper {
    public static long currentTimeInSeconds() {
        return (System.currentTimeMillis()/1000);
    }
}

package ru.finance2.simplecrmandroid.models;

import android.database.sqlite.SQLiteDatabase;

abstract public class DbModel {
    abstract public boolean save(SQLiteDatabase dbHelper);

    private transient boolean isNewRecord = false;
    private transient boolean isFromServer = false;

    public DbModel setIsNewRecord(boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
        return this;
    }

    public DbModel setIsFromServer(boolean isFromServer) {
        this.isFromServer = isFromServer;
        return this;
    }

    public boolean isNewRecord() {
        return this.isNewRecord;
    }

    public boolean isFromServer() {
        return this.isFromServer;
    }
}
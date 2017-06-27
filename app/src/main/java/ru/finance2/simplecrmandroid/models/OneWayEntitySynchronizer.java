package ru.finance2.simplecrmandroid.models;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;

import ru.finance2.simplecrmandroid.helpers.ApiHelper;
import ru.finance2.simplecrmandroid.helpers.SyncHelper;
import ru.finance2.simplecrmandroid.net.OneWayEntityNetClient;

public class OneWayEntitySynchronizer {

    private OneWayEntityNetClient oneWayEntityNetClient = new OneWayEntityNetClient(ApiHelper.getOneWayEntitySyncApiPath(), ApiHelper.getToken());
    private SQLiteDatabase db;

    public OneWayEntitySynchronizer(SQLiteDatabase db) {
        this.db = db;
    }

    public boolean loadEntities() {
        try {
            List<JsonObject> netEntities = oneWayEntityNetClient.getEntities();
            if(netEntities != null) {        // если успешно получили записи с сервера
                OneWayEntity.deleteAll(db);  // удаляем все записи локально
                for (JsonObject currentEntityGJson : netEntities) { // и записываем пришедшие с сервера
                    OneWayEntity entity = new OneWayEntity().setFromGJsonObject(currentEntityGJson);
                    entity.save(db);
                }
            } else { return false; }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void log(String s) {
        Log.d("ONE_WAY_ENTITY", s);
    }


    public void cancelNetClient() { this.oneWayEntityNetClient.cancel(); }


    public static void doSync(SQLiteDatabase db, SyncHelper.IPublish publisher) {
        publisher.publish("Синхронизация односторонних сущностей...");
        OneWayEntitySynchronizer synchronizer = new OneWayEntitySynchronizer(db);
        if( synchronizer.loadEntities() ) {
            publisher.publish("Успех");
        } else {
            publisher.publish("Ошибка");
        }
    }
}

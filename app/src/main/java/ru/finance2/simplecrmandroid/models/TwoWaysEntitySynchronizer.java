package ru.finance2.simplecrmandroid.models;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;

import ru.finance2.simplecrmandroid.MainDbHelper;
import ru.finance2.simplecrmandroid.helpers.ApiHelper;
import ru.finance2.simplecrmandroid.helpers.SyncHelper;
import ru.finance2.simplecrmandroid.net.TwoWaysEntityNetClient;

public class TwoWaysEntitySynchronizer {

    private TwoWaysEntityNetClient twoWaysEntityNetClient = new TwoWaysEntityNetClient(ApiHelper.getTwoWaysEntitySyncApiPath(), ApiHelper.getToken());
    private SQLiteDatabase db;

    public TwoWaysEntitySynchronizer(SQLiteDatabase db) {
        this.db = db;
    }

    public boolean loadEntities() {
        try {
            List<JsonObject> netEntities = twoWaysEntityNetClient.getEntities();
            if(netEntities != null) {
                for (JsonObject currentEntityGJson : netEntities) {
                    TwoWaysEntity remoteEntity = new TwoWaysEntity().setFromGJsonObject(currentEntityGJson);
                    TwoWaysEntity localEntity  = TwoWaysEntity.getById(db, remoteEntity.id);
                    remoteEntity.setIsFromServer(true);

                    if (localEntity == null) {
                        // Если в локальной БД нет такой сущности - то добавляем
                        remoteEntity.setIsNewRecord(true);
                        remoteEntity.save(db);
                        log("Добавление сущности " + remoteEntity.id);
                    } else if (localEntity.last_time_update < remoteEntity.last_time_update ) {
                        // Если локальная запись сущности существует и ее время последнего обновления
                        // меньше времени последнего обновления цели с сервера, то обновляем
                        remoteEntity.save(db);
                        log("Обновление сущности " + remoteEntity.id);
                    } else {
                        log("Сущность " + remoteEntity.id + " не нуждается в обновлении");
                        // Ничего не делаем, если пришедшая с сервера сущность есть в локальной базе,
                        // но её время последнего обновления меньше или равно времени последнего обновления локальной записи
                    }
                }
            } else { return false; }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean pushEntities() {
        try {
            List<TwoWaysEntity> entitiesList = TwoWaysEntity.getAll(db);
            if (entitiesList != null) {
                return twoWaysEntityNetClient.pushEntities(entitiesList);
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void log(String s) {
        Log.d("TWO_WAYS_ENTITY", s);
    }


    public void cancelNetClient() { this.twoWaysEntityNetClient.cancel(); }


    public static void doSync(SQLiteDatabase db, SyncHelper.IPublish publisher) {
        publisher.publish("Синхронизация двусторонних сущностей...");
        TwoWaysEntitySynchronizer synchronizer = new TwoWaysEntitySynchronizer(db);
        if ( synchronizer.loadEntities() && synchronizer.pushEntities()) {
            publisher.publish("Успех");
        } else {
            publisher.publish("Ошибка");
        }
    }
}

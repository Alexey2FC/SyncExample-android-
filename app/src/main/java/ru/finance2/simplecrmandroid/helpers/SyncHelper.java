package ru.finance2.simplecrmandroid.helpers;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import org.json.JSONException;

import java.io.IOException;

import ru.finance2.simplecrmandroid.Application;
import ru.finance2.simplecrmandroid.MainDbHelper;
import ru.finance2.simplecrmandroid.models.OneWayEntity;
import ru.finance2.simplecrmandroid.models.OneWayEntitySynchronizer;
import ru.finance2.simplecrmandroid.models.TwoWaysEntity;
import ru.finance2.simplecrmandroid.models.TwoWaysEntitySynchronizer;


public class SyncHelper {
    MainDbHelper dbHelper = Application.getInstance().getMainDbHelper();

    public interface IPublish {
        void publish(String progress);
    }

    /**
     * Осуществляет полную синхронизацию всех таблиц в обоих направлениях
     * ("сервер"->"клиент", "клиент"->"сервер")
     * @param publisher объект для оповещения о состоянии синхронизации
     * @return "true" в случае успешной синхронизации
     */
    public boolean doFullSync(IPublish publisher) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //----- Синхронизация односторонних сущностей //
        OneWayEntitySynchronizer.doSync(db, publisher);
        //----- Синхронизация двусторонних сущностей //
        TwoWaysEntitySynchronizer.doSync(db, publisher);
        //-------------------------------
        publisher.publish("Завершено");
        return true;
    }

}

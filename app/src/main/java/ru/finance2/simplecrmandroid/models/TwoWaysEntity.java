package ru.finance2.simplecrmandroid.models;

import ru.finance2.simplecrmandroid.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import ru.finance2.simplecrmandroid.MainDbHelper;
import ru.finance2.simplecrmandroid.helpers.CursorDataConverter;
import ru.finance2.simplecrmandroid.helpers.DateTimeHelper;
import ru.finance2.simplecrmandroid.helpers.GJsonDataConverter;
import ru.finance2.simplecrmandroid.helpers.JsonDataConverter;
import ru.finance2.simplecrmandroid.helpers.TypeDataConverter;

public class TwoWaysEntity extends DbModel {

    public static final String TABLE_NAME = "two_ways_sync_entity";


    //------ SQL - запросы --------

    // Запрос создания таблицы
    public static final String CREATE_TABLE_QUERY =
            "CREATE TABLE " + TABLE_NAME+ " ("
                    + "id                INTEGER PRIMARY KEY, "
                    + "name              TEXT DEFAULT \"\", "
                    + "last_time_update  INTEGER DEFAULT 0" +
            ");";
    //---- / SQL - запросы---------


    // ---- Поля сущности ----
    public Long id               = -1L;
    public String name           = "";
    public Long last_time_update = 0L;  // таймстамп в СЕКУНДАХ
    // --- / Поля сущности ---


    public TwoWaysEntity() {}

    public boolean save(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();

        cv.put("id"              , this.id);
        cv.put("name"            , this.name);
        cv.put("last_time_update", this.last_time_update);

        if (this.last_time_update == 0 || !isFromServer()) { // ВАЖНО: при обновлении сущности в локальной базе через
            // формы андройд-приложения важно всегда менять last_time_update на текущий таймстамп
            // (чтобы веб-сервер при синхронизации заметил,
            // что запись из андройд приложения изменилась и применил эти изменения на своей стороне)
            this.last_time_update = DateTimeHelper.currentTimeInSeconds();
            cv.put("last_time_update", this.last_time_update);
        }

        if( this.id != -1L ) {         // если какой-то айдишник уже задан - то тут 2 варианта:
                                      // 1) либо запись была считана локально - тогда при сохранении её надо ОБНОВИТЬ
                                      // 2) либо запись была создана удаленно. Тогда её надо ДОБАВИТЬ с тем айдишником, который был назначен сервером сервера
            cv.put("id", this.id);
            if( isNewRecord() ) {     // этот метод как раз и позволяет определить, обновлять или добавлять запись, если айди уже был задан
                                      // подразумевается, что если мы добавляем запись с сервера, где айди уже заполнен сервером - то нужно перед
                                      // вызовом метода "save" вызывать метод "setIsNewRecord( true )"
                return db.insert(TABLE_NAME, null, cv)!=-1;
            }
            return db.update(TABLE_NAME, cv, "id="+this.id, null) > 0;
        }
        cv.put("id", DateTimeHelper.currentTimeInSeconds()); // если айдишник не был назначен извне - значит мы локально сохраняем новую запись,
                                                             // и нужно сгенерировать новый айдишник, уникальный внутри таблицы как на вебе так и локально
        return db.insert(TABLE_NAME, null, cv) != -1L;
    }

    /**
     * @return true, если удаление сущности было успешным<br/>
     * false, если не удалось удалить сущность
     */
    public boolean delete(MainDbHelper dbHelper) {
        return dbHelper.getWritableDatabase().delete(TABLE_NAME, "id="+this.id, null) > 0;
    }

    public static void deleteAll(MainDbHelper dbHelper) {
        deleteAll(dbHelper.getWritableDatabase());
    }

    public static void deleteAll(SQLiteDatabase db) {
        db.execSQL("DELETE FROM "+TABLE_NAME+" WHERE 1=1");
    }

    /**
     * Формирует массив объектов из курсора.
     * Если курсор пуст (не указывает ни на одну запись) - метод возвращает пустой массив.
     * @param c
     * @return
     */
    private static ArrayList<TwoWaysEntity> innerFill(Cursor c) {
        ArrayList<TwoWaysEntity> result = new ArrayList<>();
        c.moveToFirst();
        for( int i=0; i<c.getCount(); i++ ) {
            TwoWaysEntity entity = new TwoWaysEntity();
            entity.setFromCursorRow(c);
            result.add(entity);
            c.moveToNext();
        }
        c.close();
        return result;
    }


    public static ArrayList<TwoWaysEntity> getAll(SQLiteDatabase db) {
        return getAllByCondition(db, null);
    }

    public static ArrayList<TwoWaysEntity> getAllByCondition(SQLiteDatabase db, String condition) {
        return innerFill(readAllByCondition(db, condition, null));
    }

    /**
     * Возвращает объект сущности по заданному id.
     * Возврашает null, если не нашлось записи с таким id
     * @param db
     * @param id
     * @return
     */
    public static TwoWaysEntity getById(SQLiteDatabase db, long id) {
        Cursor c = readById(db, id);
        ArrayList<TwoWaysEntity> result = innerFill(c);
        if( result.size() > 0 ) {
            return result.get(0);
        }
        return null;
    }


    public TwoWaysEntity setFromCursorRow(Cursor c) {
        return setFromSource(c, CursorDataConverter.getInstance());
    }

    public TwoWaysEntity setFromJsonObject(JSONObject j) {
        return setFromSource(j, JsonDataConverter.getInstance());
    }

    public TwoWaysEntity setFromGJsonObject(JsonObject j) {
        return setFromSource(j, GJsonDataConverter.getInstance());
    }

    public TwoWaysEntity setFromSource(Object source, TypeDataConverter converter) {
        this.id               = converter.getLong(   source, "id",   this.id   );
        this.name             = converter.getString( source, "name", this.name );
        this.last_time_update = converter.getLong(   source, "last_time_update", this.last_time_update );
        return this;
    }

    public static Cursor readAll(SQLiteDatabase db) {
        return readAllByCondition(db, null, null);
    }

    /**
     * @param id id сущности
     * @return Курсор выборки сущности по id
     */
    public static Cursor readById(SQLiteDatabase db, Long id) {
        return readAllByCondition(db, "id="+id, null);
    }

    public static Cursor readAllByCondition(SQLiteDatabase db, String condition, String orderBy) {
        return db.query(TABLE_NAME, null, condition, null, null, null, orderBy);
    }

    /**
     * Пересоздает таблицу сущностей<br/>
     * (Создает, если таблицы нет)
     */
    public static void recreateTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        db.execSQL(CREATE_TABLE_QUERY);
    }

}

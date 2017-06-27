package ru.finance2.simplecrmandroid.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import ru.finance2.simplecrmandroid.MainDbHelper;
import ru.finance2.simplecrmandroid.helpers.CursorDataConverter;
import ru.finance2.simplecrmandroid.helpers.GJsonDataConverter;
import ru.finance2.simplecrmandroid.helpers.JsonDataConverter;
import ru.finance2.simplecrmandroid.helpers.TypeDataConverter;

public class OneWayEntity extends DbModel {

    public static final String TABLE_NAME = "one_way_sync_entity";

    //------ SQL - запросы --------

    // Запрос создания таблицы
    public static final String CREATE_TABLE_QUERY =
            "CREATE TABLE " + TABLE_NAME+ " ("
                    + "id INTEGER PRIMARY KEY, "
                    + "name TEXT DEFAULT \"\"" +
            ");";
    //---- / SQL - запросы---------


    // ---- Поля сущности ----
    public Long id     = -1L;
    public String name = "";
    // --- / Поля сущности ---


    public OneWayEntity() {}

    public boolean save(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();

        cv.put("id",   this.id);
        cv.put("name", this.name);

        return db.insert(TABLE_NAME, null, cv)!=-1;
    }

    /**
     * @return true, если удаление сущности было успешным<br/>
     * false, если не удалось удалить сущность
     */
    public boolean delete(SQLiteDatabase db) {
        return db.delete(TABLE_NAME, "id="+this.id, null) > 0;
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
    private static ArrayList<OneWayEntity> innerFill(Cursor c) {
        ArrayList<OneWayEntity> result = new ArrayList<>();
        c.moveToFirst();
        for( int i=0; i<c.getCount(); i++ ) {
            OneWayEntity entity = new OneWayEntity();
            entity.setFromCursorRow(c);
            result.add(entity);
            c.moveToNext();
        }
        c.close();
        return result;
    }


    public static ArrayList<OneWayEntity> getAll(SQLiteDatabase db) {
        return getAllByCondition(db, null);
    }

    public static ArrayList<OneWayEntity> getAllByCondition(SQLiteDatabase db, String condition) {
        return innerFill(readAllByCondition(db, condition, null));
    }

    /**
     * Возвращает объект сущности по заданному id.
     * Возврашает null, если не нашлось записи с таким id
     * @param db
     * @param id
     * @return
     */
    public static OneWayEntity getById(SQLiteDatabase db, long id) {
        Cursor c = readById(db, id);
        ArrayList<OneWayEntity> result = innerFill(c);
        if( result.size() > 0 ) {
            return result.get(0);
        }
        return null;
    }

    public OneWayEntity setFromCursorRow(Cursor c) {
        return setFromSource(c, CursorDataConverter.getInstance());
    }

    public OneWayEntity setFromJsonObject(JSONObject j) {
        return setFromSource(j, JsonDataConverter.getInstance());
    }

    public OneWayEntity setFromGJsonObject(JsonObject j) {
        return setFromSource(j, GJsonDataConverter.getInstance());
    }

    public OneWayEntity setFromSource(Object source, TypeDataConverter converter) {
        this.id   = converter.getLong(   source, "id",   this.id   );
        this.name = converter.getString( source, "name", this.name );
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
    public static void recreateTable(MainDbHelper dbHelper) {
        dbHelper.getWritableDatabase().execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        dbHelper.getWritableDatabase().execSQL(CREATE_TABLE_QUERY);
    }
}

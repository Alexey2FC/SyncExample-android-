package ru.finance2.simplecrmandroid.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import ru.finance2.simplecrmandroid.MainDbHelper;

/**
 * Класс сущности
 */
public class Entity {
    public static final String TABLE_NAME = "entity";

    //------ SQL - запросы --------

    // Запрос создания таблицы
    public static final String CREATE_TABLE_QUERY =
            "CREATE TABLE "+TABLE_NAME+" ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "name TEXT"
                    + ");";
    //---- / SQL - запросы---------


    // ---- Поля сущности ----
    private int id = -1;
    private String name;
    // --- / Поля сущности ---


    public Entity(EntityBuilder builder) {
        this.id = builder.getId();
        this.name = builder.getName();
    }

    // ---- Геттеры ----
    public int getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    // -- / Геттеры ----


    /**
     * Сохраняет текущую сущность, если это НОВАЯ сущность.<br/>
     * Обновляет текущую сущность, если это сущность из БД<br/>
     * --------------------------------------<br/>
     * <b>Примечание:</b> "новизна" сущности определяется по значению поля id. <br/>
     * Если id == -1 - то это значит, что сущность НОВАЯ.<br/>
     * Если id != -1 - это значит, что сущность была считана из БД (или id был задан вручную).
     * @return true если добавление/обновление сущности было успешным.<br/>
     * false если не удалось добавить/обновить сущность
     */
    public boolean save(MainDbHelper dbHelper){
        ContentValues cv = new ContentValues();

        cv.put("name", this.name);

        if( this.id != -1 ) {
            cv.put("id", this.id);
            return dbHelper.getWritableDatabase().update(TABLE_NAME, cv, "id="+this.id, null) > 0;
        }
        return dbHelper.getWritableDatabase().insert(TABLE_NAME, null, cv)!=-1;
    }

    /**
     * @return true, если удаление сущности было успешным<br/>
     * false, если не удалось удалить сущность
     */
    public boolean delete(MainDbHelper dbHelper) {
        return dbHelper.getWritableDatabase().delete(TABLE_NAME, "id="+this.id, null) > 0;
    }

    /**
     * @param id Объект сущности по заданному id
     * @return объект сущности с данным id из БД<br/>
     * null, если записи с таким id не существует
     */
    public static Entity getById(MainDbHelper dbHelper, int id) {
        Cursor c = readById(dbHelper, id);

        c.moveToFirst();
        if( c.getCount() > 0 ) {
            EntityBuilder builder = new EntityBuilder();
            builder.setFromCursorRow(c);
            return builder.build();
        }

        return null;
    }

    /**
     * @return Массив объектов из всех содержащихся в БД сущностей данного класса
     */
    public static ArrayList<Entity> getAll(MainDbHelper dbHelper) {
        ArrayList<Entity> result = new ArrayList<>();
        Cursor c = readAll(dbHelper);

        c.moveToFirst();
        Log.d("TP", "COUNT "+c.getCount());
        for( int i=0; i<c.getCount(); i++ ) {
            EntityBuilder builder = new EntityBuilder();
            builder.setFromCursorRow(c);
            result.add(builder.build());
            c.moveToNext();
        }
        c.close();
        return result;
    }

    /**
     * @return Курсор выборки ВСЕХ сущностей
     */
    public static Cursor readAll(MainDbHelper dbHelper) {
        return dbHelper.getWritableDatabase().rawQuery(
                "SELECT * FROM entity"
                ,null
        );
    }

    /**
     * @param id id сущности
     * @return Курсор выборки сущности по id
     */
    public static Cursor readById(MainDbHelper dbHelper, int id) {
        return dbHelper.getWritableDatabase().rawQuery(
                "SELECT id, name FROM entity "
               +"WHERE id="+id
                ,null
        );
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

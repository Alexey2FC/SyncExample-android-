package ru.finance2.simplecrmandroid.models;

import android.database.Cursor;
import android.util.Log;

/**
 * Класс-помощник в построении объектов сущности
 */
public class EntityBuilder {
    // ------ Поля построителя сущности (совпадают с полями сущности) ----
    private int id = -1;
    private String name;
    // ---- / Пола построителя сущности -----

    public EntityBuilder() {}

    public EntityBuilder(Entity entity) {
        this.setId(entity.getId());
        this.setName(entity.getName());
    }


    // ----- Сеттеры --------
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    // --- / Сеттеры --------

    // ----- Геттеры --------
    public int getId() {
        return this.id;
    }

    public String getName(){
        return this.name;
    }
    // --- / Геттеры --------


    /**
     * Загружает данные из строки выборки сущности, на которую указывает курсор
     * @param c курсор, указывающий на строку, содержащую поля из сущности для построения
     */
    public void setFromCursorRow(Cursor c) {
        this.id    = c.getInt(   c.getColumnIndex("id"));
        this.name  = c.getString(c.getColumnIndex("name"));
    }

    /**
     * Осуществляет валидацию полей
     * @return true, если валидация прошла успешно<br/>
     * false, если возникли ошибки валидации
     */
    public boolean validate(){
        if( (this.name == null) || (this.name.replaceAll("\\s+", "").equals("")) ) {
            Log.d("INFO", "Имя не может быть пустым");
            return false;
        }
        return true;
    }


    /**
     * @return построенный на основе данного билдера объект сущности
     */
    public Entity build() {
        return build(true);
    }

    /**
     * @param validate true, если необходимо осуществить валидацию,<br/>
     * false - если валидация не требуется
     * @return построенный на основе данного билдера объект сущности либо null,<br/>
     * если параметр validate=true и валидация методом validate() не прошла
     */
    public Entity build(boolean validate) {
        if( validate && !validate() ) {
            return null;
        }

        return new Entity(this);
    }

}

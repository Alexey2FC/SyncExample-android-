package ru.finance2.simplecrmandroid.controllers;

import ru.finance2.simplecrmandroid.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.finance2.simplecrmandroid.R;
import ru.finance2.simplecrmandroid.models.Entity;
import ru.finance2.simplecrmandroid.models.EntityBuilder;


public class EntityForm extends AppCompatActivity {
    // Поле ввода названия сущности
    EditText editName;

    // Построитель объекта сущности
    EntityBuilder currentEntityBuilder;

    // Кнопка сохранения
    Button saveBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Устанавливаем для данной активити представление, содержащее поля формы создания сущности
        setContentView(R.layout.simple_entity_form);

        editName = (EditText) findViewById(R.id.editName);
        saveBtn = (Button) findViewById(R.id.saveBtn);

        // Извлечение id сущности, переданное при старте активити
        int entityId = getIntent().getIntExtra("ID", -1);
        Log.d("INFO", "ENT ID IS " +entityId);

        // Получаем объект по заданному id
        Entity currentEntity = Entity.getById(Application.getInstance().getMainDbHelper(), entityId);

        // Создаем построитель сущности на основе объекта сущности
        if( currentEntity != null ) {
            currentEntityBuilder = new EntityBuilder(currentEntity);
        } else {
            currentEntityBuilder = new EntityBuilder();
        }

        // Установка обработчика нажатия на кнопку сохранения
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

    }

    /**
     * Заполнение объекта построителя сущности данными из полей формы
     */
    private void fillEntityBuilderFromFormFields() {
        currentEntityBuilder.setName(editName.getText().toString());
    }

    /**
     * Заполняет поля формы данными из объекта построителя
     */
    private void fillFormFieldsFromEntityBuilder() {
        editName.setText(currentEntityBuilder.getName());
    }

    /**
     * Обработка сохранения сущности
     */
    private void save() {
        fillEntityBuilderFromFormFields();

        // Получаем объект сущности из построителя сущности
        Entity currentEntity = currentEntityBuilder.build();

        // Если он равен null (то есть валидация не прошла при построении), то выводим уведомление
        if( currentEntity == null ) {
            Toast.makeText(this, "Неправильные данные", Toast.LENGTH_SHORT).show();
        // иначе сохраняем сущность и завершаем активити
        } else {
            currentEntity.save(Application.getInstance().getMainDbHelper());
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillFormFieldsFromEntityBuilder();
    }
}

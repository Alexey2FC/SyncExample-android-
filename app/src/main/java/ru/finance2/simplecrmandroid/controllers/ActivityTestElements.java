package ru.finance2.simplecrmandroid.controllers;

import ru.finance2.simplecrmandroid.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.util.ArrayList;

import ru.finance2.simplecrmandroid.R;
import ru.finance2.simplecrmandroid.adapters.EntitySpinnerAdapter;
import ru.finance2.simplecrmandroid.models.Entity;


public class ActivityTestElements extends AppCompatActivity {
    // Выпадающий список с сущностями
    Spinner simpleSpinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Устанавливаем для данной активити представление, содержащее выпадающий список (Spinner)
        setContentView(R.layout.simple_test_elements);

        simpleSpinner = (Spinner) findViewById(R.id.simpleSpinner);

        // Создание адаптера сущностей для выпадающего списка
        final EntitySpinnerAdapter spinnerAdapter = new EntitySpinnerAdapter(this);

        // Загрузка списка сущностей в виде массива
        ArrayList<Entity> entities = Entity.getAll(Application.getInstance().getMainDbHelper());

        // Загрузка массива сущностей в адаптер
        spinnerAdapter.addAll(entities);

        // Установка адаптера для spinnerAdapter
        simpleSpinner.setAdapter(spinnerAdapter);
    }
}

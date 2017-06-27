package ru.finance2.simplecrmandroid.controllers;

import ru.finance2.simplecrmandroid.Application;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;


import ru.finance2.simplecrmandroid.MainDbHelper;
import ru.finance2.simplecrmandroid.R;
import ru.finance2.simplecrmandroid.adapters.OneWayEntityAdapter;
import ru.finance2.simplecrmandroid.models.OneWayEntity;

public class OneWaySyncEntityList extends AppCompatActivity {

    private ListView lv;
    private OneWayEntityAdapter adapter;
    private MainDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_way_sync_entity_list);

        dbHelper = Application.getInstance().getMainDbHelper();


        adapter = new OneWayEntityAdapter(this, android.R.layout.simple_list_item_1);
        adapter.addAll(OneWayEntity.getAll(dbHelper.getWritableDatabase()));
        lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(adapter);

    }
}

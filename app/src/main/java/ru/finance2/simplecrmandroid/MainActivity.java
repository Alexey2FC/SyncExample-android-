package ru.finance2.simplecrmandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import ru.finance2.simplecrmandroid.controllers.ActivityTestElements;
import ru.finance2.simplecrmandroid.controllers.EntityForm;
import ru.finance2.simplecrmandroid.controllers.EntityList;
import ru.finance2.simplecrmandroid.controllers.OneWaySyncEntityList;
import ru.finance2.simplecrmandroid.controllers.TwoWaysSyncEntityList;
import ru.finance2.simplecrmandroid.helpers.AndroidDatabaseManager;
import ru.finance2.simplecrmandroid.models.Entity;
import ru.finance2.simplecrmandroid.models.OneWayEntity;
import ru.finance2.simplecrmandroid.models.TwoWaysEntity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void one_way_sync_entity_list(View view) {
        startActivity(new Intent(this, OneWaySyncEntityList.class));
    }

    public void two_ways_sync_entity_list(View view) {
        startActivity(new Intent(this, TwoWaysSyncEntityList.class));
    }

    public void sync_all(View view) {
        startActivity(new Intent(this, SyncDatabase.class));
    }

    public void recreate_all(View view) {
        MainDbHelper dbHelper = Application.getInstance().getMainDbHelper();

        Entity.recreateTable(dbHelper);
        OneWayEntity.recreateTable(dbHelper);
        TwoWaysEntity.recreateTable(dbHelper.getWritableDatabase());
        Toast.makeText(this, "Таблицы пересозданы", Toast.LENGTH_SHORT).show();
    }

    public void database_manager(View view) {
        startActivity(new Intent(this, AndroidDatabaseManager.class));
    }

    public void startActivityTestElements(View view) {
        startActivity(new Intent(this, ActivityTestElements.class));
    }

    public void startEntityList(View view) {
        startActivity(new Intent(this, EntityList.class));
    }

    public void startEntityForm(View view) {
        startActivity(new Intent(this, EntityForm.class));
    }
}

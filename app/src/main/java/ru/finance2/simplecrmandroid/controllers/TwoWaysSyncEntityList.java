package ru.finance2.simplecrmandroid.controllers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import ru.finance2.simplecrmandroid.Application;
import ru.finance2.simplecrmandroid.MainDbHelper;
import ru.finance2.simplecrmandroid.R;
import ru.finance2.simplecrmandroid.adapters.TwoWaysEntityAdapter;
import ru.finance2.simplecrmandroid.models.TwoWaysEntity;

public class TwoWaysSyncEntityList extends AppCompatActivity implements AdapterView.OnItemClickListener {
    public static final int STATE_ADD = 1;
    public static final int STATE_UPDATE = 2;


    private ListView lv;
    private TwoWaysEntityAdapter adapter;
    private MainDbHelper dbHelper;
    private Button addBtn;
    private EditText entityNameEdit;
    private int currentState = STATE_ADD;
    private long currentUpdatingEntityID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_ways_sync_entity_list);

        dbHelper = Application.getInstance().getMainDbHelper();

        entityNameEdit = (EditText) findViewById(R.id.entity_name_edit);
        addBtn = (Button) findViewById(R.id.add_btn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( currentState == STATE_ADD ) {
                    addNewEntity();
                } else {
                    updateEntity();
                }
            }
        });

        adapter = new TwoWaysEntityAdapter(this, android.R.layout.simple_list_item_1);

        lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadList();
    }


    private void addNewEntity() {
        String name = entityNameEdit.getText().toString();
        entityNameEdit.setText("");

        if( name.equals("") ) {
            return;
        }

        TwoWaysEntity entity = new TwoWaysEntity();
        entity.name = name;

        if( entity.save(dbHelper.getWritableDatabase()) ) {
            Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
            reloadList();
        } else {
            Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateEntity() {
        String name = entityNameEdit.getText().toString();
        entityNameEdit.setText("");

        if( name.equals("") ) {
            return;
        }

        TwoWaysEntity entity = TwoWaysEntity.getById(dbHelper.getWritableDatabase(), currentUpdatingEntityID);
        entity.name = name;
        if( entity.save(dbHelper.getWritableDatabase()) ) {
            Toast.makeText(this, "Обновлена запись #"+currentUpdatingEntityID, Toast.LENGTH_SHORT).show();
            reloadList();
        } else {
            Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show();
        }

        setState(STATE_ADD);
    }

    private void setState(int state) {
        currentState = state;
        switch (state) {
            case STATE_ADD:
                currentUpdatingEntityID = -1;
                addBtn.setText("Добавить");
                break;
            case STATE_UPDATE:
                addBtn.setText("Обновить");
                break;
        }
    }


    private void reloadList() {
        adapter.clear();
        adapter.addAll(TwoWaysEntity.getAll(dbHelper.getWritableDatabase()));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TwoWaysEntity entity = adapter.getItem(position);
        System.out.println("Clicked: " + entity.name);

        currentUpdatingEntityID = entity.id;
        setState(STATE_UPDATE);
        entityNameEdit.setText(entity.name);
    }
}

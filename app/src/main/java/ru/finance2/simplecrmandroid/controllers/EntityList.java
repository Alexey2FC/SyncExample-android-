package ru.finance2.simplecrmandroid.controllers;

import ru.finance2.simplecrmandroid.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.finance2.simplecrmandroid.R;
import ru.finance2.simplecrmandroid.adapters.EntityAdapter;
import ru.finance2.simplecrmandroid.models.Entity;


/**
 * Активити со списком сущностей<br/>
 */
public class EntityList extends AppCompatActivity {
    // ListView с сущностями
    ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Устанавливаем для данной активити представление, содержащее список (ListView)
        setContentView(R.layout.simple_list_view);

        // Сохраняем listView из установленного представления
        listView = (ListView) findViewById(R.id.simpleListView);

        // Устанавливаем обработчик клика на элемент списка
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Выбираем объект сущности, расположенной в позиции списка, по котой произошел клик
                Entity selectedEntity = ((Entity) listView.getAdapter().getItem(position));

                // Создаем intent для старта новой активити формы (EntityForm)
                Intent intent = new Intent(EntityList.this, EntityForm.class);

                // Добавляем в intent идентификатор записи текущей выбранной сущности
                // (в дальнейшем EntityForm извлекает при старте этот ID и подгружает сущность, если она существует в БД)
                intent.putExtra("ID", selectedEntity.getId());
                startActivity(intent);
            }
        });


        // Устанавливаем обработчик длинного нажатия на элемент списка
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Выбираем объект сущности, расположенной в позиции списка, по которому произошло долгое нажатие
                Entity selectedEntity = ((Entity) listView.getAdapter().getItem(position));

                // Если запись удалилась успешно - выводим сообщение и перезагружаем список
                if( selectedEntity.delete(Application.getInstance().getMainDbHelper()) ) {
                    Toast.makeText(EntityList.this, "Запись удалена", Toast.LENGTH_SHORT).show();
                    reloadList();
                // Если запись не удалилась - выводим сообщение об ошибке
                } else {
                    Toast.makeText(EntityList.this, "Не удалось удалить запись", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    /**
     * Обновление всех элементов списка
     */
    private void reloadList() {
        // Создание адаптера
        EntityAdapter adapter = new EntityAdapter(this, android.R.layout.simple_list_item_1);

        // Загрузка списка сущностей в виде массива
        ArrayList<Entity> entities = Entity.getAll(Application.getInstance().getMainDbHelper());

        // Загрузка массива сущностей в адаптер
        adapter.addAll(entities);

        // Установка адаптера для listView
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadList();
    }
}

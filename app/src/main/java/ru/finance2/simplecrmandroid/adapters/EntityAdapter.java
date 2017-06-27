package ru.finance2.simplecrmandroid.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ru.finance2.simplecrmandroid.models.Entity;

public class EntityAdapter extends ArrayAdapter<Entity> {
    public EntityAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        // Извлечение сущности по позиции
        Entity entity = getItem(position);

        // Заполняем представление элемента списка данными из сущности
        TextView textView = (TextView)view.findViewById(android.R.id.text1);
        textView.setText(entity.getId() + ":" + entity.getName());
        return view;
    }
}

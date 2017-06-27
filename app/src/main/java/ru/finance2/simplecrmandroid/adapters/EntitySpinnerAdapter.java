package ru.finance2.simplecrmandroid.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ru.finance2.simplecrmandroid.R;
import ru.finance2.simplecrmandroid.models.Entity;

public class EntitySpinnerAdapter extends ArrayAdapter<Entity> {
    final static int RESOURCE_XML = R.layout.elements_spinner_item;

    public EntitySpinnerAdapter(Context context) {
        super(context, RESOURCE_XML);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getDropDownView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        Entity entity = getItem(position);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        // Создаем представление элемента списка из xml-ресурса
        View view = inflater.inflate(RESOURCE_XML, null);

        // Заполняем представление элемента списка данными из сущности
        TextView textView = (TextView)view.findViewById(android.R.id.text1);
        TextView textView2 = (TextView)view.findViewById(android.R.id.text2);
        textView.setText(""+entity.getId());
        textView2.setText(""+entity.getName());
        return view;
    }
}

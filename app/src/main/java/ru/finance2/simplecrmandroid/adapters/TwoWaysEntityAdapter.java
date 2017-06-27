package ru.finance2.simplecrmandroid.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ru.finance2.simplecrmandroid.models.TwoWaysEntity;

/**
 * Created by faiz on 07.10.2016.
 */

public class TwoWaysEntityAdapter extends ArrayAdapter<TwoWaysEntity> {


    public TwoWaysEntityAdapter(Context context, int resource) {
        super(context, resource);
    }

    public TwoWaysEntityAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView nameView = (TextView) view.findViewById(android.R.id.text1);

        TwoWaysEntity entity = getItem(position);
        nameView.setText(entity.name);

        return view;
    }
}

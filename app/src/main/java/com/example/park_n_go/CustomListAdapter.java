package com.example.park_n_go;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomListAdapter extends ArrayAdapter<String> {

    //field variables
    private Context context;
    private List name;
    private List type;
    public CustomListAdapter( @NonNull Context context, List name, List type) {
        super(context, R.layout.activity_manage_users, name);

        this.context=context;
        this.name=name;
        this.type=type;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.activity_custom_list, null,true);

        TextView nameTV=rowView.findViewById(R.id.user_name);
        TextView typeTV=rowView.findViewById(R.id.user_type);

        nameTV.setText(name.get(position).toString());
        typeTV.setText(type.get(position).toString());

        return rowView;

    }
}

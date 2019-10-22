package com.example.charan.lab4_csis4280;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<WorldPopulation> worldPopulationList;
    public CustomAdapter(Context context,ArrayList<WorldPopulation> worldPopulationList)
    {
        this.context=context;
        this.worldPopulationList=worldPopulationList;
    }
    @Override
    public int getCount() {
        return worldPopulationList.size();
    }

    @Override
    public Object getItem(int position) {
        return worldPopulationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if(view==null)
        {
            LayoutInflater layoutInflater= LayoutInflater.from(context);
            view=layoutInflater.inflate(R.layout.list_layout,parent,false);
        }
        WorldPopulation worldPopulation=worldPopulationList.get(position);
        TextView txtCity=view.findViewById(R.id.txt_city);
        TextView txtLat=view.findViewById(R.id.txt_lat);
        TextView txtLng=view.findViewById(R.id.txt_lng);
        TextView txtPopulation=view.findViewById(R.id.txt_population);
        TextView txtAdmin=view.findViewById(R.id.txt_admin);
        TextView txtPopulationProper=view.findViewById(R.id.txt_populationProper);

        txtCity.setText(worldPopulation.getCity());
        txtLat.setText(""+worldPopulation.getLat());
        txtLng.setText(""+worldPopulation.getLng());
        txtAdmin.setText(worldPopulation.getAdmin());
        txtPopulation.setText(""+worldPopulation.getPopulation());
        txtPopulationProper.setText(""+worldPopulation.getPopulation_proper());

        return view;
    }
}

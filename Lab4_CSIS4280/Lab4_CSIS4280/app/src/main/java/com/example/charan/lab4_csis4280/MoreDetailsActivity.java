package com.example.charan.lab4_csis4280;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MoreDetailsActivity extends AppCompatActivity {

    ListView listView;
    Intent intent;
    String data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_details);
        listView=findViewById(R.id.detail_list);
        intent=getIntent();
        if(intent!=null)
        {
            data=intent.getStringExtra("result");
        }
        Log.d("testdddd", "onCreate: "+data);
        ArrayList<WorldPopulation> worldPopulationList=formatData(data);
        CustomAdapter customAdapter=new CustomAdapter(this,worldPopulationList);
        listView.setAdapter(customAdapter);


    }

    private ArrayList<WorldPopulation> formatData(String data) {
        ArrayList<WorldPopulation> worldPopulationList=new ArrayList<>();
        JSONObject jsonObject=null;
        try {
            JSONArray jsonArray=new JSONArray(data);
            for(int i=0;i<jsonArray.length();i++)
            {
                WorldPopulation worldPopulation=new WorldPopulation();
                jsonObject= (JSONObject) jsonArray.get(i);
                worldPopulation.setCity(jsonObject.getString("city"));
                worldPopulation.setLat(jsonObject.getDouble("lat"));
                worldPopulation.setLng(jsonObject.getDouble("lng"));
                worldPopulation.setAdmin(jsonObject.getString("admin"));
                worldPopulation.setPopulation(jsonObject.getInt("population"));
                worldPopulation.setPopulation_proper(jsonObject.getInt("population_proper"));
                worldPopulationList.add(worldPopulation);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return worldPopulationList;

    }

}
